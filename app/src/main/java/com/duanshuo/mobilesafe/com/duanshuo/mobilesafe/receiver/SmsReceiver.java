package com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

/**拦截短信
 * Created by ds on 2017/12/12.
 */

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object obj: objs) {
            //超过140字节，分多条发送
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
            String originatingAddress = sms.getOriginatingAddress();
            String messageBody = sms.getMessageBody();
        }

    }
}
