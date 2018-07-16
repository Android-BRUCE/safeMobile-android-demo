package com.safe.a360.safemobile.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.safe.a360.safemobile.R;
import com.safe.a360.safemobile.utils.FormatTools;
import com.safe.a360.safemobile.utils.SingleClick;
import com.sevenheaven.segmentcontrol.SegmentControl;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.io.InputStream;

/**
 * 测试页面
 */
@ContentView(R.layout.garbage_recovery_add)
public class ScanResoultActivity extends AppCompatActivity {
    private static final int PHOTO_REQUEST_CODE = 1;

    @ViewInject(R.id.imageView1)
    private ImageView imageView;

    @ViewInject(R.id.segment_control)
    private SegmentControl mSegmentHorzontal;

    @ViewInject(R.id.segment_control2)
    private SegmentControl mSegmentHorzontal2;

    @ViewInject(R.id.segment_control3)
    private SegmentControl mSegmentHorzontal3;

    @ViewInject(R.id.segment_control4)
    private SegmentControl mSegmentHorzontal4;
    @ViewInject(R.id.user_name)
    private TextView user_name;
    @ViewInject(R.id.user_info)
    private TextView user_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mSegmentHorzontal.setOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
            @Override
            public void onSegmentControlClick(int index) {
                Toast.makeText(ScanResoultActivity.this, "onSegmentControlClick: index = " + index, Toast.LENGTH_SHORT).show();
                //  Log.i("FragmentActivity", "onSegmentControlClick: index = " + index);
            }
        });
        initUI();
    }

    public void selectImage(View v) {
        //所需权限
//    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PHOTO_REQUEST_CODE);
    }

    public void backprepage(View view) {
        this.finish();
    }

    private Uri uri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    uri = data.getData();
                    //通过uri的方式返回，部分手机uri可能为空
                    if (uri != null) {
                        try {
                            //通过uri获取到bitmap对象
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imageView.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        //部分手机可能直接存放在bundle中
                        Bundle bundleExtras = data.getExtras();
                        if (bundleExtras != null) {
                            Bitmap bitmaps = bundleExtras.getParcelable("data");
                            imageView.setImageBitmap(bitmaps);
                        }
                    }

                }
                break;
        }
    }

    /**
     * 提交页面内容
     *
     * @param v
     */
    public void commitresoult(View v) {
        Toast.makeText(ScanResoultActivity.this, "提交表单成功！", Toast.LENGTH_SHORT).show();
    }

    public void initUI() {
        //RequestParams params = new RequestParams("http://www.baidu.com/s");
        RequestParams params = new RequestParams("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/10");
        // params.addQueryStringParameter("wd", "xUtils");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                user_name.setText("张全但");
                user_info.setText("火星一号卫星9910号");
                Toast.makeText(x.app(), "success get", Toast.LENGTH_LONG).show();
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

            }
        });
    }

    private int isClickSuccess = 1;

    // 上传多文件示例
    @Event(value = R.id.tv_commit)
    private void onTest2Click(View view) {
        if (SingleClick.isSingle()) {
            Toast.makeText(x.app(), "点击过快！", Toast.LENGTH_SHORT).show();
        }
        if (isClickSuccess != 1) {
            Toast.makeText(x.app(), "请勿重读提交", Toast.LENGTH_LONG).show();
            return;
        }
        startActivity(new Intent(ScanResoultActivity.this, ProgressBarActivity.class));

        RequestParams params = new RequestParams("http://192.168.1.9:8080/upload");
        // 加到url里的参数, http://xxxx/s?wd=xUtils
        params.addQueryStringParameter("wd", "xUtils");
        // 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        // params.addBodyParameter("wd", "xUtils");
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            FormatTools instance = FormatTools.getInstance();
            inputStream = instance.Bitmap2InputStream(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 使用multipart表单上传文件
        params.setMultipart(true);
        params.addBodyParameter(
                "file",
                inputStream,
                null); // 如果文件没有扩展名, 最好设置contentType参数.
        /*try {
            params.addBodyParameter(
                    "file2",
                    new FileInputStream(new File("/sdcard/test2.jpg")),
                    "image/jpeg",
                    // 测试中文文件名
                    "你+& \" 好.jpg"); // InputStream参数获取不到文件名, 最好设置, 除非服务端不关心这个参数.
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }*/
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
                isClickSuccess = -1;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                // isClickSuccess = -1;
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {
                Toast.makeText(x.app(), "finished", Toast.LENGTH_LONG).show();
                //销毁progressBar
                ProgressBarActivity.instance.finish();
            }
        });
    }

}
