package org.tvp.kirikiri2.util

import android.content.Context
import android.content.pm.PackageManager

object PackageUtil {

    fun GetVersion(context: Context): String? {
        var verstr: String? = null
        try {
            verstr = context.packageManager.getPackageInfo(
                context.packageName, 0
            ).versionName
        } catch (e1: PackageManager.NameNotFoundException) {
        }
        return verstr
    }
}