package com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.duanshuo.mobilesafe.R;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.ConstantValue;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.SpUtils;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.view.SettingItemView;

/**
 * Created by ds on 2017/11/23.
 */

public class SettingActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initUpdate();
        initAddress();
    }

    /**
     * 是否开启电话号码归属地查询
     */
    private void initAddress() {
        final SettingItemView siv_address = (SettingItemView) findViewById(R.id.siv_address);
        siv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = siv_address.isCheck();
                siv_address.setCheck(!isCheck);
            }
        });

    }

    /**
     * 版本更新开关
     */
    private void initUpdate() {
        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
        //获取已有的开关状态，用作显示
        boolean open_update =SpUtils.getBoolean(this, ConstantValue.OPEN_UPDATE,false);
        //是否选中，根据上一次存储的结果决定
        siv_update.setCheck(open_update);
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果之前是选中的，点击过后，变成未选中
                //如果之前是未选中的，点击过后，变成选中

                //获取之前的选中状态
                boolean isCheck = siv_update.isCheck();
                //将原有状态取反，就等同于上述两步操作
                siv_update.setCheck(!isCheck);
                //将取反后的状态存储到相应的sp中
                SpUtils.putBoolean(getApplicationContext(),ConstantValue.OPEN_UPDATE,!isCheck);

            }
        });

    }
}

