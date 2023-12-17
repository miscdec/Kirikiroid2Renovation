package org.tvp.kirikiri2.util

import android.app.AlertDialog
import android.content.Context
import android.content.Context.STORAGE_SERVICE
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat.getSystemService
import com.amaze.filemanager.filesystem.ExternalSdCardOperation
import com.amaze.filemanager.filesystem.ExternalSdCardOperation.getExtSdCardFolder
import com.amaze.filemanager.filesystem.MediaStoreHack
import org.tvp.kirikiri2.KR2Activity
import java.io.*
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


class StorageUtil {

    var mStorageManager: StorageManager? = null
    var mMethodGetPaths: Method? = null
    var mGetVolumeState: Method? = null
    var _extSdPaths: Array<String>? = null


    fun getStoragePath(context: Context?): Array<String?> {
        var ret = arrayOfNulls<String>(0)
        if (mStorageManager == null) {
            mStorageManager = context!!.getSystemService(STORAGE_SERVICE) as StorageManager?
            try {
                mMethodGetPaths = StorageManager::class.java.getMethod("getVolumePaths")
                mGetVolumeState =
                    StorageManager::class.java.getMethod("getVolumeState", String::class.java)
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            }
        }
        if (mMethodGetPaths != null) {
            try {
                ret = mMethodGetPaths!!.invoke(mStorageManager) as Array<String?>
            } catch (e: IllegalArgumentException) {
            } catch (e: IllegalAccessException) {
            } catch (e: InvocationTargetException) {
            } catch (e: java.lang.Exception) {
            }
        }
        if (mGetVolumeState != null) {
            try {
                for (i in ret.indices) {
                    val status = mGetVolumeState!!.invoke(mStorageManager, ret[i]) as String
                    when {
                        Environment.MEDIA_MOUNTED == status || Environment.MEDIA_MOUNTED_READ_ONLY == status -> {
                        }
                        else -> {
                            ret[i] = null
                        }
                    }
                }
            } catch (e: IllegalArgumentException) {
            } catch (e: IllegalAccessException) {
            } catch (e: InvocationTargetException) {
            } catch (e: java.lang.Exception) {
            }
        }
        return ret
    }



    fun isOnExtSdCard(file: File, c: Context?): Boolean {
        return c?.let { getExtSdCardFolder(file, it) } != null
    }

    fun requireLEXA(path: String?) {
        KR2Activity.msgHandler.post { guideDialogForLEXA(path) }
    }

    fun guideDialogForLEXA(path: String?) {
        val builder = AlertDialog.Builder(KR2Activity.sInstance)
        val image = ImageView(KR2Activity.sInstance)
        image.setImageResource(KR2Activity.sInstance!!.get_res_sd_operate_step())
        builder
            .setView(image)
            .setTitle(path)
            .setPositiveButton("OK") { dialog, which -> KR2Activity.triggerStorageAccessFramework() }
            .setNegativeButton("Cancel") { dialog, which ->
                // nothing to do
            }
            .show()
    }


    fun WriteFile(path: String?, data: ByteArray?): Boolean {
        val target = File(path)
        if (target.exists()) {
            KR2Activity.DeleteFile(target.absolutePath) // to avoid number suffix name
        } else {
            val parent = target.parentFile
            if (parent != null && !parent.exists()) KR2Activity.CreateFolders(parent.absolutePath)
        }
        var out: OutputStream? = null

        // Try the normal way
        try {
            if (isWritable(target)) {
                val os: OutputStream = FileOutputStream(target)
                os.write(data)
                os.close()
                return true
            }

            // Try with Storage Access Framework.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP /*&& isOnExtSdCard(file, sInstance)*/) {
                val document =
                    ExternalSdCardOperation.getDocumentFile(target, false, KR2Activity.sInstance!!)
                try {
                    val docUri = document!!.uri
                    out = KR2Activity.sInstance!!.contentResolver.openOutputStream(docUri)
                } catch (e: FileNotFoundException) {
                    // e.printStackTrace();
                }
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                // Workaround for Kitkat ext SD card
                val uri = MediaStoreHack.getUriFromFile(target.absolutePath, KR2Activity.sInstance)
                out = KR2Activity.sInstance!!.contentResolver.openOutputStream(uri!!)
            } else {
                return false
            }
            if (out != null) {
                out.write(data)
                out.close()
                return true
            }
        } catch (e: IOException) {
            //return false;
        }
        return false
    }

    fun isWritable(file: File?): Boolean {
            if (file == null) return false
            val isExisting = file.exists()
            try {
                val output = FileOutputStream(file, true)
                try {
                    output.close()
                } catch (e: IOException) {
                    // do nothing.
                }
            } catch (e: FileNotFoundException) {
                return false
            }
            val result = file.canWrite()

            // Ensure that file is not created during this process.
            if (!isExisting) {
                file.delete()
            }
            return result
        }

    fun DeleteFile(path: String?): Boolean {

        val file = File(path)
        // First try the normal deletion.
        val fileDelete = deleteFilesInFolder(file, KR2Activity.sInstance)
        if (file.delete() || fileDelete) return true

        // Try with Storage Access Framework.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isOnExtSdCard(
                file,
                KR2Activity.sInstance
            )
        ) {
            val document =
                ExternalSdCardOperation.getDocumentFile(file, false, KR2Activity.sInstance!!)
            return document!!.delete()
        }

        // Try the Kitkat workaround.
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            val resolver = KR2Activity.sInstance!!.contentResolver
            return try {
                val uri = MediaStoreHack.getUriFromFile(file.absolutePath, KR2Activity.sInstance)
                resolver.delete(uri!!, null, null)
                !file.exists()
            } catch (e: Exception) {
                Log.e("FileUtils", "Error when deleting file " + file.absolutePath, e)
                false
            }
        }
        return !file.exists()
    }

    fun CreateFolders(path: String?): Boolean {
        val file = File(path)

        // Try the normal way
        if (file.mkdirs()) {
            return true
        }

        // Try with Storage Access Framework.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP /*&& FileUtil.isOnExtSdCard(file, context)*/) {
            val document =
                ExternalSdCardOperation.getDocumentFile(file, true, KR2Activity.sInstance!!)
            // getDocumentFile implicitly creates the directory.
            return document!!.exists()
        }

        // Try the Kitkat workaround.
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            try {
                return MediaStoreHack.mkdir(KR2Activity.sInstance, file)
            } catch (e: IOException) {
                //return false;
            }
        }
        return false
    }

    fun RenameFile(from: String?, to: String?): Boolean {
        val file = File(from)
        val target = File(to)
        if (!file.exists()) return false
        if (target.exists()) {
            if (!KR2Activity.DeleteFile(target.absolutePath)) return false
        }
        val parent = target.parentFile
        if (!parent.exists()) {
            if (!KR2Activity.CreateFolders(parent.absolutePath)) return false
        }
        // Try the normal way
        if (file.renameTo(target)) return true

        // Try with Storage Access Framework.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP /*&& isOnExtSdCard(file, sInstance)*/) {
            val document =
                ExternalSdCardOperation.getDocumentFile(file, false, KR2Activity.sInstance!!)
            if (document!!.renameTo(to!!)) return true
        }

        // Try Media Store Hack
        return if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            try {
                val input = FileInputStream(file)
                val filesize = file.length().toInt()
                val buffer = ByteArray(filesize)
                input.read(buffer)
                input.close()
                val out = MediaStoreHack.getOutputStream(KR2Activity.sInstance, target.absolutePath)
                out.write(buffer)
                out.close()
                MediaStoreHack.delete(KR2Activity.sInstance, file)
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                false
                //e.printStackTrace();
            }
        } else false
    }

    fun deleteFilesInFolder(folder: File?, context: Context?): Boolean {
        var totalSuccess = true
        if (folder == null) return false
        if (folder.isDirectory) {
            for (child in folder.listFiles()) {
                deleteFilesInFolder(child, context)
            }
            if (!folder.delete()) totalSuccess = false
        } else {
            if (!folder.delete()) totalSuccess = false
        }
        return totalSuccess
    }


}