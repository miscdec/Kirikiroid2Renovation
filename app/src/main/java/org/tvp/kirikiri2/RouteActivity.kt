package org.tvp.kirikiri2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import cn.vove7.bottomdialog.BottomDialog
import cn.vove7.bottomdialog.builder.message
import cn.vove7.bottomdialog.builder.oneButton
import cn.vove7.bottomdialog.builder.title
import com.afollestad.materialdialogs.MaterialDialog
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import jonathanfinerty.once.Once
import org.tvp.kirikiri2.util.ToastUtil
import java.util.*
import kotlin.system.exitProcess


class RouteActivity : AppCompatActivity() {
    val storage_permissions = Permission.MANAGE_EXTERNAL_STORAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        onceInstallChecker()
        whatisnewChecker()
        grantStoragePermission(this)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == XXPermissions.REQUEST_CODE) {
//            toast("检测到你刚刚从权限设置界面返回回来");
            if (!XXPermissions.isGranted(this,storage_permissions)){
                ToastUtil.showLongToast("获取存储权限失败,模拟器无法初始化")
                val dialog = MaterialDialog(this@RouteActivity)
                    .title(text = "need premission3")
                    .message(text = "Permission.MANAGE_EXTERNAL_STORAGE")
                dialog.show()
                finishActivity(0)
            }
        }
    }

    private fun whatisnewChecker() {
        val showWhatsNew = "showWhatsNewTag"
        if (!Once.beenDone(Once.THIS_APP_VERSION, showWhatsNew)) {
            showWhatIsNewDialog()
            Once.markDone(showWhatsNew)
        }
    }

    private fun onceInstallChecker() {
        val showAppTour = "show feature onboarding"
        // in the basic functionality activity
        Once.toDo(Once.THIS_APP_INSTALL,showAppTour );
        // back in the home activity
        if (Once.needToDo(showAppTour)) {
            // do some operations
            showAppFirstGlance()
            // after task has been done, mark it as done as normal
            Once.markDone(showAppTour);
        }
    }

    private fun showWhatIsNewDialog() {
        BottomDialog.builder(this) {
            title(context.getString(R.string.whatisnew_title))
            message(context.getString(R.string.whatisnew_content))
            oneButton(context.getString(R.string.whatisnew_ok), autoDismiss = true) {
            }
        }
    }
    private fun showAppFirstGlance() {
        BottomDialog.builder(this) {
            title(context.getString(R.string.first_glance_title))
            message(context.getString(R.string.first_glance_content))
            oneButton(context.getString(R.string.first_glance_ok), autoDismiss = true) {
            }
        }
    }

    private fun grantStoragePermission(context: Context) {
        if (!XXPermissions.isGranted(this,storage_permissions)){
            val dialog = MaterialDialog(context)
                .title(text = "need premission1")
                .message(text = "Permission.MANAGE_EXTERNAL_STORAGE")
            dialog.show {
                positiveButton(text = "Agree") { dialog ->
                    // Do something
//                    XXPermissions.startPermissionActivity(context, storage_permissions)
                    XXPermissions.with(this@RouteActivity) // 申请单个权限
                        .permission(storage_permissions) // 设置权限请求拦截器（局部设置）
                        //.interceptor(new PermissionInterceptor())
                        // 设置不触发错误检测机制（局部设置）
                        .request(object : OnPermissionCallback {
                            override fun onGranted(permissions: List<String>, all: Boolean) {
                                if (all) {
                                    gotoKR()
                                } else {
                                    ToastUtil.showLongToast("获取部分权限成功，但部分权限未正常授予")
                                }
                            }

                            override fun onDenied(permissions: List<String>, never: Boolean) {
                                ToastUtil.showLongToast("获取存储权限失败,模拟器无法初始化")
                                finish()

                            }
                        })
                }

                negativeButton(text = "Disagree") {
                    if (!XXPermissions.isGranted(context, storage_permissions)) {
                        dialog.dismiss()
                        ToastUtil.showLongToast("获取存储权限失败,模拟器无法初始化")
                        finishActivity(0)
                    }
                }
            }
        } else {
            gotoKR()
            finish()
        }



    }

    private fun gotoKR() {
        ToastUtil.showShortToast("获取存储权限成功")
        val intent: Intent = Intent(
            this@RouteActivity,
            Kirikiroid2::class.java
        )
        startActivity(intent)
    }


    private fun oncePermissonsTip() {
//        BottomDialog.builder(this) {
//            title(context.getString(R.string.once_permissons_tip_title))
//            message(R.string.once_permissons_tip_content)
//            oneButton("OK", autoDismiss = true) {
//            }
//        }
    }
}