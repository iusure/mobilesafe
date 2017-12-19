package com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.duanshuo.mobilesafe.R;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.ConstantValue;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.SpUtils;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.ToastUtil;

/**
 * Created by ds on 2017/12/2.
 */

public class Setup4Activity extends BaseSetUpActivity{
    private  CheckBox cb_box;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        initUI();
    }

    private void initUI() {
        cb_box = (CheckBox) findViewById(R.id.cb_box);
        //1是否选中状态的回显
        Boolean open_security = SpUtils.getBoolean(this, ConstantValue.OPEN_SAFE_SECURITY, false);
        //2根据状态，修改checkbox后续文字显示
        cb_box.setChecked(open_security);
        if (open_security){
            cb_box.setText("安全设置已开启");
        }else {
            cb_box.setText("安全设置已关闭");
        }
        //3,点击过程中，监听选中状态发生改变过程，
      //  cb_box.setChecked(!cb_box.isChecked());
        cb_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //5根据开启还是关闭的状态去修改显示文字
                if (isChecked){
                    cb_box.setText("安全设置已开启");
                    SpUtils.putBoolean(getApplicationContext(),ConstantValue.OPEN_SAFE_SECURITY,true);
                }else {
                    cb_box.setText("安全设置已关闭");
                    SpUtils.putBoolean(getApplicationContext(),ConstantValue.OPEN_SAFE_SECURITY,false);
                }
            }
        });

    }

    /**
     * 跳转上一页
     */
    public void ShowPreviousPage() {
        Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_ami,R.anim.pre_out_ami);
    }
    /**
     * 跳转下一页
     */
    public void ShowNextPage() {
            overridePendingTransition(R.anim.next_in_ami,R.anim.next_out_ami);
            Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
            startActivity(intent);
            finish();
    }
}
