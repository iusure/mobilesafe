package com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.duanshuo.mobilesafe.R;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.ConstantValue;
import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.SpUtils;

/**
 * Created by ds on 2017/12/1.
 */

public class Setup1Activity extends BaseSetUpActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }
    /**
     * 跳转上一页
     */
    public void ShowPreviousPage() {
    }
    /**
     * 跳转下一页
     */
    public void ShowNextPage() {
        String serialNumber = SpUtils.getString(this, ConstantValue.SIM_NUMBER, "");
        // if (!TextUtils.isEmpty(serialNumber)){
        Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
        startActivity(intent);
        finish();

        //}else {
        // ToastUtil.show(this,"请绑定sim卡");
        //}
        //开启平移动画
        overridePendingTransition(R.anim.next_in_ami,R.anim.next_out_ami);
    }

}



