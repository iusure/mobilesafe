package com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.receiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.ConstantValue;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.SpUtils;

/**开启重启广播接受者
 * 需要权限：android.permission.RECEIVE_BOOT_COMPLETED
 *       <receiver android:name=".com.duanshuo.mobilesafe.receiver.BootCompleteReceiver">
 <intent-filter>
 <action android:name="android.intent.action.BOOT_COMPLETED"/>
 </intent-filter>
 </receiver>
 * Created by ds on 2017/12/12.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Boolean protect = SpUtils.getBoolean(context, ConstantValue.OPEN_SAFE_SECURITY, false);
        if (!protect){
            //如果没有开启保护，直接返回
            return;
        }
        String SaveSim = SpUtils.getString(context, ConstantValue.SIM_NUMBER, null);
        if (TextUtils.isEmpty(SaveSim)) {
            //获取当前sim卡和保存的sim卡进行比对
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            String currentSim = tm.getSimSerialNumber();
            if (SaveSim.equals(currentSim)){
                Log.i("BootCompleteReceiver","手机安全，放心使用");

            }else {
                Log.i("BootCompleteReceiver","sim卡已变化，发送报警短信");
                //需要权限
                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage(ConstantValue.CONTACT_PHONE,null,"sim Card Changed!!!",null,null);

            }

        }

    }
}
