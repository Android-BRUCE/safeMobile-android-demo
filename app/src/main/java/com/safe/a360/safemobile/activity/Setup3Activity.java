package com.safe.a360.safemobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.safe.a360.safemobile.R;

/**
 * Created by Administrator on 2018-07-04.
 */

public class Setup3Activity extends BaseSetupActivity {
    EditText etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup3_activity);
        etPhone = (EditText) findViewById(R.id.et_phone);
        String phoneString = mPref.getString("safe_phone", "");
        etPhone.setText(phoneString);
    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this, Setup2Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);
    }

    @Override
    public void showNextPage() {
        String phone = etPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(Setup3Activity.this, "请输入号码", Toast.LENGTH_SHORT).show();
            return;
        }
        mPref.edit().putString("safe_phone", phone).commit();

        startActivity(new Intent(this, Setup4Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    public void select(View view) {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == Activity.RESULT_OK) {
            String phone = data.getStringExtra("phone");
            // 替换-和空格
            phone = phone.replaceAll("-", "").replaceAll(" ", "");
// 把电话号码设置给输入框
            etPhone.setText(phone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
