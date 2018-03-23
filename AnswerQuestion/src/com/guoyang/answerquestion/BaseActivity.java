package com.guoyang.answerquestion;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * @ 描述：Activity基类
 */

public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {

    @Override
    protected final void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //得到布局文件
        setContentView(getLayoutId());

        //初始化View
        initView();

        //初始化界面数据
        initData();

        //绑定监听器与适配器
        initListener();
    }

    /**
     * @return 布局文件id
     */
    abstract int getLayoutId();

    /**
     * 初始化View
     */
    void initView() {
    }

    ;

    /**
     * 初始化界面数据
     */
    void initData() {
    }

    ;

    /**
     * 绑定监听器与适配器
     */
    void initListener() {
    }

    ;

    /**
     * 对统一的按钮进行统一处理
     *
     * @param v 点击的View
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                processClick(v);
                break;
        }
    }

    /**
     * 点击事件
     *
     * @param v 点击的View
     */
    void processClick(View v) {
    }
    ;
}
