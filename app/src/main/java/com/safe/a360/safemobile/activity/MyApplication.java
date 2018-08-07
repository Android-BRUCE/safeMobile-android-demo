package com.safe.a360.safemobile.activity;

import android.app.Application;

import com.mob.MobSDK;
import com.safe.a360.safemobile.utils.Logger;

import org.xutils.BuildConfig;
import org.xutils.x;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import cn.jpush.android.api.JPushInterface;

/**
 * 该类需要在清单文件中注册
 * Created by Administrator on 2018-07-14.
 */

public class MyApplication extends Application {
    /**
     * For developer startup JPush SDK
     * <p>
     * 一般建议在自定义 Application 类里初始化。也可以在主 Activity 里。
     */
    private static final String TAG = "JIGUANG-Example";

    @Override
    public void onCreate() {
        Logger.d(TAG, "[ExampleApplication] onCreate");
        super.onCreate();

        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 开启debug会影响性能
        // 全局默认信任所有https域名 或 仅添加信任的https域名
        // 使用RequestParams#setHostnameVerifier(...)方法可设置单次请求的域名校验
        x.Ext.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        /**
         * 激光推送（gradle集成）
         */
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush

/**
 * shareSDK
 */
        MobSDK.init(this);
    }
}
