package com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ds on 2017/11/21.
 */

public class ToastUtil {
    //打印吐司

    /**
     *
     * @param ctx 上下文环境
     * @param msg 打印文本内容
     */
    public static void show(Context ctx, String msg){
        Toast.makeText(ctx,msg,0).show();

    }
}
