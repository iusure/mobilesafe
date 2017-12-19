package com.duanshuo.mobilesafe.com.duanshuo.mobilesafe.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;


/**
 * Created by ds on 2017/11/22.
 * 能够获取焦点的自定义TextView
 */

public class FocusTextView extends android.support.v7.widget.AppCompatTextView {
    //使用在通过java代码创建控件
    public FocusTextView(Context context) {
        super(context);
    }
    //由系统调用（带属性 +上下文环境的构造方法）
    public FocusTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    //由系统调用（带属性 +上下文环境的构造方法+布局文件中定义样式文件构造方法）
    public FocusTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //重写获取焦点的方法,由系统调用，调用的时候就能够获取焦点

    @Override
    public boolean isFocused() {
        return true;
    }
}
