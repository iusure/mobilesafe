package com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ds on 2017/11/24.
 */

public class Md5Util {
    public static StringBuffer stringBuffer;

    /**
     * 给指定字符串按照Md5算法去加密
     * @param psd 需要加密的密码  加盐处理
     * @return   md5后的字符串
     */

    public static String encoder(String psd) {
        // 1.指定加密算法类型
        try {
            //加盐处理
            psd = psd + "mobilesafe";
            MessageDigest digest = MessageDigest.getInstance("MD5");
            //2,将需要加密的字符串转成byte类型的数组，然后进行随机哈希过程
            byte[] bs = digest.digest(psd.getBytes());
            //System.out.println(bs.length);
            //3,循环遍历bs生成32位字符串，固定写法
            //4,拼接过程
            //5,打印测试
             stringBuffer = new StringBuffer();
            for (byte b : bs) {
                int i = b & 0xff;
                //int类型的i需要转换成16进制的字符
                String hexString = Integer.toHexString(i);
               // System.out.println(hexString);
                if (hexString.length()<2) {
                    hexString = "0"+hexString;
                }
                stringBuffer.append(hexString);
            }
            return stringBuffer.toString();
           // System.out.println(stringBuffer.toString());
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

}
