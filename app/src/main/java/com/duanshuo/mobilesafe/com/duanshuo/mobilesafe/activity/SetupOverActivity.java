package com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.duanshuo.mobilesafe.R;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.ConstantValue;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.SpUtils;

import java.util.concurrent.CompletionService;

import static com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.ConstantValue.SETUP_OVER;

/**
 * Created by ds on 2017/11/30.
 */

public class SetupOverActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boolean setup_over = SpUtils.getBoolean(this, SETUP_OVER, false);
        if (setup_over){
            //密码输入成功,并且四个导航界面设置完成--->停留在设置完成功能列表界面
            setContentView(R.layout.activity_setup_over);
            TextView tvPhone = (TextView) findViewById(R.id.tv_safe_phone);
            ImageView ivLock = (ImageView)findViewById(R.id.iv_lock);
            String phone = SpUtils.getString(this, ConstantValue.CONTACT_PHONE, "");
            tvPhone.setText(phone);
            Boolean protect = SpUtils.getBoolean(this, ConstantValue.OPEN_SAFE_SECURITY, false);
            if (protect){
                ivLock.setImageResource(R.drawable.lock);
            }else {
                ivLock.setImageResource(R.drawable.unlock);
            }

        }else {
            //密码输入成功,四个导航界面没有设置完成--->跳转到导航界面第一个
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);

            //开启了一个新的界面后，关闭功能列表界面
            finish();

        }
    }

    /**
     * 重新进入向导
     * @param view
     */
    public void reSetup(View view){
        startActivity(new Intent(this,Setup1Activity.class));
        finish();
    }
}
