package com.safe.a360.safemobile.activity;

import android.app.Activity;
import android.os.Bundle;

import com.safe.a360.safemobile.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

/**
 * Created by Administrator on 2018-07-14.
 */
@ContentView(R.layout.progressbar)
public class ProgressBarActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }
}
