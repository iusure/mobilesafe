package com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.duanshuo.mobilesafe.R;

/**
 * Created by ds on 2017/12/14.
 */

public class AToolActivity extends Activity{
    private TextView tv_query_phone_address;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atool);

        //电话归属地查询
        initPhoneAddress();
    }

    private void initPhoneAddress() {
        tv_query_phone_address = (TextView) findViewById(R.id.tv_query_phone_address);
        tv_query_phone_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),QueryAddressActivity.class));
            }
        });
    }
}
