package com.safe.a360.safemobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.safe.a360.safemobile.R;
import com.safe.a360.safemobile.view.SettingItem;
/**
 * Created by Administrator on 2018-07-03.
 */
public class Setup2Activity extends BaseSetupActivity {
    private SettingItem sim;
    String simString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup2_activity);
        simString = mPref.getString("sim", "");
        sim = (SettingItem) findViewById(R.id.siv_sim);

        //进入这个页面判断是否打勾了并选择是否显示勾上
        if(!TextUtils.isEmpty(simString)) {
            sim.setChecked(true);
        }else {
            sim.setChecked(false);
        }

        sim.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(sim.isChecked()) {
                    sim.setChecked(false);
                    mPref.edit().remove("sim").commit();
                }else {
                    sim.setChecked(true);
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String tString = telephonyManager.getSimSerialNumber();
                    mPref.edit().putString("sim", tString).commit();
                }

            }
        });
    }
    @Override
    public void showPreviousPage() {
        startActivity( new Intent(this,Setup1Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);

    }
    @Override
    public void showNextPage() {

        String mString = mPref.getString("sim", null);
        if(TextUtils.isEmpty(mString)){
            Toast.makeText(Setup2Activity.this, "必须绑定SIM卡！", Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity( new Intent(this,Setup3Activity.class));
        finish();

        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);

    }
}
