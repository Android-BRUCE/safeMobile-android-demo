package com.safe.a360.safemobile.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.safe.a360.safemobile.R;

/**
 * Created by Administrator on 2018-07-03.
 */


public class LostFindActivity extends AppCompatActivity {
    private SharedPreferences mPref;
    private TextView phone;
    private ImageView pic;
    String pString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mPref = getSharedPreferences("config", MODE_PRIVATE);
        //判断是否进入并设置引导页面。
        boolean b = mPref.getBoolean("configed", false);
        if (b) {
            setContentView(R.layout.lost_find_activity);
            //将设置好的号码显示到页面上
            phone = (TextView) findViewById(R.id.tv_phone1);
            pString = mPref.getString("safe_phone", "");
            phone.setText(pString);
            //显示小锁是否锁上图案
            pic = (ImageView) findViewById(R.id.iv_piclock);
            boolean bpic = mPref.getBoolean("protect", false);
            if (bpic) {
                pic.setImageResource(R.drawable.lock);
            } else {
                pic.setImageResource(R.drawable.unlock);
            }
        } else {
            startActivity(new Intent(this, Setup1Activity.class));
            finish();
        }
    }
    /**
     * 再次进入引导页面
     */
    public void reEnter(View v){
        startActivity(new Intent(this, Setup1Activity.class));
        finish();
    }
}
