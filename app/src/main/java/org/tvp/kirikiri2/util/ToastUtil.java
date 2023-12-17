package org.tvp.kirikiri2.util;

import android.view.Gravity;
import android.widget.Toast;

import org.tvp.kirikiri2.KR2Application;

public class ToastUtil {

    private static Toast toast;//实现不管我们触发多少次Toast调用，都只会持续一次Toast显示的时长

    /**
     * 短时间显示Toast【居下】
     * @param msg 显示的内容-字符串*/
    public static void showShortToast(String msg) {
        if(KR2Application.getAppContext() != null){
            if (toast == null) {
                toast = Toast.makeText(KR2Application.getAppContext(), msg, Toast.LENGTH_SHORT);
            } else {
                toast.setText(msg);
            }
            //1、setGravity方法必须放到这里，否则会出现toast始终按照第一次显示的位置进行显示（比如第一次是在底部显示，那么即使设置setGravity在中间，也不管用）
            //2、虽然默认是在底部显示，但是，因为这个工具类实现了中间显示，所以需要还原，还原方式如下：
            toast.show();
        }
    }




    /**
     * 长时间显示Toast【居下】
     * @param msg 显示的内容-字符串*/
    public static void showLongToast(String msg) {
        if(KR2Application.getAppContext() != null) {
            if (toast == null) {
                toast = Toast.makeText(KR2Application.getAppContext(), msg, Toast.LENGTH_LONG);
            } else {
                toast.setText(msg);
            }
            
            toast.show();
        }
    }


}
