package com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import com.duanshuo.mobilesafe.R;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.ConstantValue;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.SpUtils;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.view.SettingItemView;

/**
 * Created by ds on 2017/12/1.
 */

public class Setup2Activity extends BaseSetUpActivity {
    private SettingItemView siv_sim_bound;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        initUI();
    }

    private void initUI() {
        siv_sim_bound = (SettingItemView) findViewById(R.id.siv_sim_bound);
        //1,回显（读取已有的绑定状态用作显示，sp中是否存储了sim卡的序列号）
        String sim_number = SpUtils.getString(this, ConstantValue.SIM_NUMBER, "");
        //2,判断是否序列卡号为“”
        if (TextUtils.isEmpty(sim_number)) {
            siv_sim_bound.setCheck(false);

        } else {
            siv_sim_bound.setCheck(true);
        }
        siv_sim_bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //3,获取原有的状态()
                boolean isCheck = siv_sim_bound.isCheck();
                //4，将原有的状态取反
                //5,状态设置给当前条目
                siv_sim_bound.setCheck(!isCheck);

                if (!isCheck) {
                    //6,存储(序列卡号)
                    //6.1获取sim卡序列号TelephoneManager

                    TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    //6.2获取序列卡号

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    String simSerialNumber = manager.getSimSerialNumber();
                        //6.3存储
                        SpUtils.putString(getApplicationContext(),ConstantValue.SIM_NUMBER,simSerialNumber);
                }else {
                    //7,将存储序列卡号的节点从sp中删除
                    SpUtils.remove(getApplicationContext(),ConstantValue.SIM_NUMBER);
                }
            }
        });

    }


    /**
     * 跳转上一页
     */
    public void ShowPreviousPage() {
        Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
        startActivity(intent);
        finish();
        //开启平移动画
        overridePendingTransition(R.anim.pre_in_ami,R.anim.pre_out_ami);
    }
    /**
     * 跳转下一页
     */
    public void ShowNextPage() {
        String serialNumber = SpUtils.getString(this, ConstantValue.SIM_NUMBER, "");
        // if (!TextUtils.isEmpty(serialNumber)){
        Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
        startActivity(intent);
        finish();

        //}else {
        // ToastUtil.show(this,"请绑定sim卡");
        //}
        //开启平移动画
        overridePendingTransition(R.anim.next_in_ami,R.anim.next_out_ami);
    }

}


