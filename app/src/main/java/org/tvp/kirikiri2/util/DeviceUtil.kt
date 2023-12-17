package org.tvp.kirikiri2.util

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import org.tvp.kirikiri2.KR2Activity

import org.tvp.kirikiri2.KR2Activity.GetInstance

object DeviceUtil {


    val deviceId: String
        @SuppressLint("MissingPermission")
        get() {
            val mgr =
                GetInstance()!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val DeviceID = mgr.deviceId
            if (DeviceID != null) {
                return "DevID:$DeviceID"
            }
            val androidId = Settings.Secure.getString(
                GetInstance()!!.contentResolver, Settings.Secure.ANDROID_ID
            )
            if (null != androidId && androidId.length > 8 &&
                "9774d56d682e549c" != androidId
            ) {
                return "AndroidID:$androidId"
            } else if (null != Build.SERIAL && Build.SERIAL.length > 3) {
                return "AndroidID:" + Build.SERIAL
            }
            return ""
        }

}