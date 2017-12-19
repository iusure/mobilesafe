package com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.utils.ToastUtil;


/**
 * 设置向导的父类
 * Created by ds on 2017/12/12.
 */

public abstract class BaseSetUpActivity extends Activity {
    //手势识别器
    private GestureDetector mDetector;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //手势识别器
        mDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            //重写快速滑动方法

            /**
             *
             * @param e1 起点坐标
             * @param e2 终点坐标
             * @param velocityX 速度x
             * @param velocityY 速度y
             * @return
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(e2.getRawY()-e1.getRawY())>200){
                    //竖直方向滑动范围太大
                    ToastUtil.show(getApplicationContext(),"不能这样滑哦");
                    return true;
                }
                if (Math.abs(velocityX)<100){
                    //滑动太慢哦
                    ToastUtil.show(getApplicationContext(),"滑动太慢哦");
                    return true;
                }

                //判断左滑还是右滑
                //e2.getX() 相对父控件的x坐标
                //e2.getRawX() 屏幕的绝对坐标
                if (e2.getRawX()-e1.getRawX()>200){
                    //向右滑上一页
                    ShowPreviousPage();
                    return true;
                }
                if (e1.getRawX()-e2.getRawX()>200){
                    //向左滑 下一页
                    ShowNextPage();
                    return true;
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }
    /**
     * 按钮点击上一页
     * @param view
     */

    public void nextPage(View view){
        ShowNextPage();
    }

    /**
     * 按钮点击下一页
     * @param view
     */

    public void prePage(View view){

        ShowPreviousPage();
    }

        /**
         * 跳转上一页
         */
    public abstract void ShowPreviousPage();

    /**
     * 跳转下一页
     */
   public abstract void ShowNextPage();

    /**
     * 当界面被触摸时调用此方法
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //将事件委托给手势识别器处理
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}

