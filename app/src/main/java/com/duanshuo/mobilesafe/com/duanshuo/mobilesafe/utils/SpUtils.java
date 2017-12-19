package com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ds on 2017/11/23.
 */

public class SpUtils {
    private static  SharedPreferences sp;
    //写

    /**
     *写入Boolean类型的变量至sp中
     * @param ctx 上下文环境
     * @param key  存储节点的名称
     * @param value 存储节点的值
     */
    public static void putBoolean(Context ctx,String key,boolean value){
        //(存储节点文件的名称，读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key,value).commit();
    }

    //读

    /**
     *读取Boolean类型的变量从sp中
     * @param ctx 上下文环境
     * @param key 存储节点的名称
     * @param defvalue 没有此节点的时候的默认值
     * @return 默认值或者此节点读取到的结果
     */
    public static Boolean getBoolean(Context ctx,String key,boolean defvalue){
        //(存储节点文件的名称，读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key,defvalue);
    }
    public static void putString(Context ctx,String key,String value){
        //(存储节点文件的名称，读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key,value).commit();
    }

    //读

    /**
     *读取Boolean类型的变量从sp中
     * @param ctx 上下文环境
     * @param key 存储节点的名称
     * @param defvalue 没有此节点的时候的默认值
     * @return 默认值或者此节点读取到的结果
     */
    public static String getString(Context ctx,String key,String defvalue){
        //(存储节点文件的名称，读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key,defvalue);
    }

    /**
     * 从sp中移除指定节点
     * @param ctx 上下文环境
     * @param key 需要移除节点的名称
     */
    public static void remove(Context ctx,String key) {
        //(存储节点文件的名称，读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().remove(key).commit();
    }
}
