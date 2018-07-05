package com.safe.a360.safemobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.safe.a360.safemobile.R;

/**
 * Created by Administrator on 2018-07-03.
 */

public class SettingItem extends RelativeLayout {

    private String mTitle;
    private String mDescOn;
    private String mDescOff;
    private TextView tvTitle;
    private TextView tvDesc;
    private CheckBox cbStatus;
    private static final String NAMESPACE = "http://schemas.android.com/apk/com.safe.a360.safemobile";

    public SettingItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initView();
    }
    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        //拿到属性的值
        mTitle = attrs.getAttributeValue(NAMESPACE,"title");
        mDescOn = attrs.getAttributeValue(NAMESPACE,"desc_on");
        mDescOff = attrs.getAttributeValue(NAMESPACE,"desc_off");
        //初始化
        initView();
    }
    public SettingItem(Context context){
        super(context);
        initView();
    }
    private void initView() {
        //将自定义好的布局文件设置给当前的SettingItem作为当前SettingItem的孩子
        View.inflate(getContext(), R.layout.view_setting_item, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        cbStatus = (CheckBox) findViewById(R.id.cb_status);
        settingTitle(mTitle);
    }
    //设置显示是否开启自动更新描述
    public void setDesc(String desc) {
        tvDesc.setText(desc);
    }

    private void settingTitle(String mTitle2) {
        // TODO Auto-generated method stub
        tvTitle.setText(mTitle2);
    }
    /**
     * 返回勾选状态
     *
     * @return
     */
    public boolean isChecked() {
        return cbStatus.isChecked();
    }

    public void setChecked(boolean check) {
        //打勾或者不打勾
        cbStatus.setChecked(check);
        // 根据选择的状态,更新文本描述
        if (check) {
            setDesc(mDescOn);
        } else {
            setDesc(mDescOff);
        }
    }
}
