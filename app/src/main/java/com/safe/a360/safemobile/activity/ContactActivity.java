package com.safe.a360.safemobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.safe.a360.safemobile.R;
import com.safe.a360.safemobile.utils.ContactUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2018-07-04.
 */

public class ContactActivity extends AppCompatActivity {
    private ListView lvList;
    private ArrayList<HashMap<String, String>> readContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        lvList = (ListView) findViewById(R.id.lv_list);

        readContact = ContactUtil.readContact(this);
        // System.out.println(readContact);
        lvList.setAdapter(new SimpleAdapter(this, readContact,
                R.layout.contact_list_item, new String[] { "name", "phone" },
                new int[] { R.id.tv_name, R.id.tv_phone }));

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String phone = readContact.get(position).get("phone");// 读取当前item的电话号码
                Intent intent = new Intent();
                intent.putExtra("phone", phone);
                setResult(Activity.RESULT_OK, intent);// 将数据放在intent中返回给上一个页面

                finish();
            }
        });
    }
}
