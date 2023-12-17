package org.tvp.kirikiri2;

import android.app.Application;
import android.content.Context;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import org.tvp.kirikiri2.util.ToastUtil;

import java.util.List;

import jonathanfinerty.once.Once;

public class KR2Application extends Application {

    /**系统上下文*/
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        Once.initialise(this);
//        grantStoragePermission();
    }

    /**获取系统上下文：用于ToastUtil类*/
    public static Context getAppContext()
    {
        return mAppContext;
    }


}
