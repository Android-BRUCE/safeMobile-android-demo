package com.safe.a360.safemobile.activity;
import android.app.Activity;
import android.os.Bundle;
import com.safe.a360.safemobile.R;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;
/**
 * Created by Administrator on 2018-07-14.
 * extends Activity 才能设置页面背景透明的
 */
@ContentView(R.layout.progressbar)
public class ProgressBarActivity extends Activity {
    /**
     * 实例化静态对象，方便调用该类的方法。
     */
    public static ProgressBarActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        x.view().inject(this);
    }
}
