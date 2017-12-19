package com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.duanshuo.mobilesafe.R;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.ConstantValue;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.SpUtils;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.ToastUtil;

/**
 * Created by ds on 2017/12/2.
 */

public class Setup3Activity extends BaseSetUpActivity{
    private  EditText et_phone_number;
    private  Button bt_select_number;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        initUI();
        
    }

    private void initUI() {
        //显示号码的输入框
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        //获取来联系人电话号码回显
        String phone = SpUtils.getString(this, ConstantValue.CONTACT_PHONE, "");
        et_phone_number.setText(phone);
        //点击选择联系人的对话框
        bt_select_number = (Button) findViewById(R.id.bt_select_number);
        bt_select_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null){
            //1返回到当前界面的时候要去接收结果的方法；
            String phone = data.getStringExtra("phone");
            //2将特殊字符过滤，（中划线转换成空字符串）
            phone = phone.replace("-","").replace("  ","").trim();
            et_phone_number.setText(phone);

            //3存储联系人至sp中
            SpUtils.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE,phone);

        }

    }

    /**
     * 跳转上一页
     */
    public void ShowPreviousPage() {
        Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
        startActivity(intent);
        finish();
        //开启平移动画
        overridePendingTransition(R.anim.pre_in_ami,R.anim.pre_out_ami);
    }
    /**
     * 跳转下一页
     */
    public void ShowNextPage() {
        //点击按钮后，需要获取输入框中的联系人，在做下一页操作
        String phone = et_phone_number.getText().toString();
        overridePendingTransition(R.anim.next_in_ami, R.anim.next_out_ami);

        //在sp存储了联系人以后才可以跳转下一页
        //  String contact_phone = SpUtils.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, "");
        if (!TextUtils.isEmpty(phone)) {
            Intent intent = new Intent(getApplicationContext(), Setup4Activity.class);
            startActivity(intent);
            finish();

            //如果现在是输入的电话号码，则需要去保存
            SpUtils.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE, phone);
        } else {
            ToastUtil.show(this, "请输入电话号码");
        }
    }
}
