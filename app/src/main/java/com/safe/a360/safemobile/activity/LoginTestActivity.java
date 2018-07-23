package com.safe.a360.safemobile.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.safe.a360.safemobile.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 登录demo
 * Created by Administrator on 2018-07-23.
 */
@ContentView(R.layout.activity_login)
public class LoginTestActivity extends BaseActivity {


    @ViewInject(R.id.et_username)
    private EditText et_username;

    @ViewInject(R.id.et_password)
    private EditText et_password;

    @ViewInject(R.id.login)
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 设置顶部状态栏
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.blue);

    }

    /**
     * 登录
     *
     * @param view
     */
    public void login(View view) {

        if (view.getId() == login.getId()) {

            RequestParams params = new RequestParams("http://www.wanjiakeji.net/apis/user/login");
            params.addQueryStringParameter("user_name", "17600181508");
            params.addQueryStringParameter("password", "123456");
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    JSONObject jsonObject = null;//我们需要把json串看成一个大的对象
                    try {
                        jsonObject = new JSONObject(result);
                        JSONObject data = jsonObject.getJSONObject("data");
                        String uid = (String) data.get("uid");
                        String user_synckey = (String) data.get("user_synckey");
                        preferences.edit().putString("uid", uid).commit();
                        preferences.edit().putString("user_synckey", user_synckey).commit();
                        preferences.edit().putString("loginStatus", "success").commit();
                        Toast.makeText(x.app(), uid + ":" + user_synckey, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
                @Override
                public void onCancelled(CancelledException cex) {
                    Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onFinished() {
                    String string = preferences.getString("loginStatus", null);
                    String uid = preferences.getString("uid", null);
                    if (string != null) {
                        Toast.makeText(x.app(), "登陆成功！", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(x.app(), "登陆失败！"+uid+string, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
