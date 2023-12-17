package org.tvp.kirikiri2;

import org.jetbrains.annotations.NotNull;

public class NativeMethod {


    static private native void onMessageBoxOK(int nButton);
    static private native void onMessageBoxText(String text);
    static private native void onNativeExit();
    static public native void onNativeInit();
    static public native void onBannerSizeChanged(int w, int h);
    static native void initDump(String path);
    static private native void nativeOnLowMemory();

    
}
