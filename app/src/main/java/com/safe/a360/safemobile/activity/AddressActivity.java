package com.safe.a360.safemobile.activity;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.safe.a360.safemobile.R;
import com.safe.a360.safemobile.dao.AddressDao;


/**
 * 查询页面
 *
 * @author BRUCE
 */
public class AddressActivity extends AppCompatActivity {

    private EditText etNumber;

    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        etNumber = (EditText) findViewById(R.id.et_number);
        tvResult = (TextView) findViewById(R.id.tv_result);
        //监听文本内容的改变
        etNumber.addTextChangedListener(new TextWatcher() {
            //内容改变时调用
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String queryResult = AddressDao.getAddress(s.toString());
                tvResult.setText(queryResult);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void query(View v) {
        String number = etNumber.getText().toString().trim();
        if (!TextUtils.isEmpty(number)) {
            String queryResult = AddressDao.getAddress(number);
            tvResult.setText(queryResult);
        } else {//如果输入框为空就抖动输入框进行提示要求输入内容
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
            etNumber.startAnimation(animation);
            vibrate();
        }
    }

    /**
     * 手机震动
     * 需要权限 android.permission.VIBRATE
     */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        //1000,2000,1000,4000:等待一秒，震动2秒，等待2秒，震动4秒。-1：震动一次，其他数字表示从第几个位置开始执行。
        vibrator.vibrate(new long[]{1000, 2000, 1000, 4000}, -1);
    }
}
