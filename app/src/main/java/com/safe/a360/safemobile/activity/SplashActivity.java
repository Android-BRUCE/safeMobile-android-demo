package com.safe.a360.safemobile.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.safe.a360.safemobile.R;
import com.safe.a360.safemobile.bean.VirusInfo;
import com.safe.a360.safemobile.dao.AntivirusDao;
import com.safe.a360.safemobile.utils.StreamUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 闪屏页面
 * @author BRUCE
 *
 */
public class SplashActivity extends AppCompatActivity {
	protected static final int CODE_UPDATE_DIALOG = 0;
	protected static final int CODE_ENTER_HOME = 1;
	protected static final int CODE_URL_ERROR = 2;
	protected static final int CODE_NET_ERROR = 3;
	protected static final int CODE_JSON_ERROR = 4 ;

	TextView tvVersion;
	TextView tvProgress;
	private String mVersionName;//服务器端版本名称
	private int mVersionCode;//服务器端版本号
	private String mDesc;//服务器端版本描述
	private String mDownloadUrl;//服务器端版本下载地址
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				showUpdateDailog();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "url错误", Toast.LENGTH_SHORT)
				.show();
				enterHome();
				break;
			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT)
				.show();
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "数据解析错误",
						Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_ENTER_HOME:
				enterHome();
				break;
			default:
				break;
			}
		};
	};
	private SharedPreferences spPreferences;
	private RelativeLayout rlRoot;// 根布局
	private AntivirusDao dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.splash_activity);
		tvProgress = (TextView) findViewById(R.id.tv_progress);// 默认隐藏
        tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText("版本名：" + getVersionName());
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        
        copyDB("address.db");//拷贝数据库到data/data/包名/files下？？
        copyDB("antivirus.db");
        /**
         * 更新病毒库
         */
        updateVirus();
        //p判断是否需要更新
        spPreferences = getSharedPreferences("config", MODE_PRIVATE);
        boolean b = spPreferences.getBoolean("auto_update", true);
        if(b) {
			System.out.println("=========================这是开启自动更新");
            checkVersion();
        }else {
			System.out.println("=========================这是关闭自动更新");
        	mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);// 延时2秒后发送消息为了闪屏页面的显示
		}
        
        //设置闪屏页面的渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1f);
        alphaAnimation.setDuration(2000);
        rlRoot.startAnimation(alphaAnimation);
        
        /**
         * @author BRUCE
         *  在手机桌面上显示应用快捷方式
         */
        createShoutcut();
       
    }
    /**
     * 更新病毒库
     */
    private void updateVirus() {
    	
    	dao = new AntivirusDao();
		// 从互联网获取更新
    	HttpUtils utils = new HttpUtils();
    	String url = "http://10.1.16.56:8080/virus.jason";
    	utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				try {
//				JSONObject object = new JSONObject(arg0.result);
				
//				String md5 = object.getString("md5");
//				
//				String desc = object.getString("desc");
				/**
				 * 应对大量数据，使用谷歌json jar包
				 */
				Gson gson = new Gson();
				VirusInfo virus = gson.fromJson(arg0.result, VirusInfo.class);
				/*
				 * 可以先在AntivirusDao进行判断 此病毒md5值在数据库中是否存在
				 */
				dao.addVirus(virus.md5, virus.desc);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		});
		
	}
	private void createShoutcut() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		intent.putExtra("duplicate", false);//false 不允许重复创建桌面快捷方式
		
		/**
		 * 1.做什么事情
		 * 2.叫什么名字
		 * 3.长什么样子
		 */
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,"手机卫士");
		//做什么事
		//不能使用显示意图。
		Intent shortCut = new Intent();
		shortCut.setAction("homeActovityStart");
		shortCut.addCategory("android.intent.category.DEFAULT");
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,shortCut);
		
		sendBroadcast(intent);
		
	}
	/**
     * 获取版本名字
     * @return
     */
    private String getVersionName() {
    	PackageManager packageManager = getPackageManager();
    	try {
			PackageInfo paInfo = packageManager.getPackageInfo(getPackageName(), 0);//获取包的信息
			//int versionCode  = paInfo.versionCode;
			String versionName = paInfo.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			// 找不到包名
			e.printStackTrace();
		}
    	
		return "";
	}
    
    /*
     * 获取本地app版本号
     */
    private int getVersionCode() {
    	PackageManager packageManager = getPackageManager();
    	try {
			PackageInfo paInfo = packageManager.getPackageInfo(getPackageName(), 0);//获取包的信息
			int versionCode  = paInfo.versionCode;
			//String versionName = paInfo.versionName;
			return versionCode;
		} catch (NameNotFoundException e) {
			// 找不到包名
			e.printStackTrace();
		}
    	
		return -1;
	}
    /**
     * 校验检查版本信息
     */
    private void checkVersion() {
		final long startTime = System.currentTimeMillis();
		
		Thread thread = new Thread(){
			public void run() {
				
				Message msg = Message.obtain();
				HttpURLConnection conn = null;
				try {
					URL url = new URL("http://10.0.2.2:8080/update.json");
					conn = (HttpURLConnection) url.openConnection();
					
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					conn.connect();
					
					int responsecode = conn.getResponseCode();
					if(responsecode == 200) {
						InputStream inputStream = conn.getInputStream();
						String readFrom = StreamUtils.readFromStrem(inputStream);//获取服务器端版本信息
						
						// 解析json
						JSONObject jsonObject = new JSONObject(readFrom);
						 System.out.println("网络返回:" + readFrom);
						mVersionName = jsonObject.getString("versionName");
						mVersionCode = jsonObject.getInt("versionCode");
						mDesc = jsonObject.getString("description");
						mDownloadUrl = jsonObject.getString("downloadUrl");	
						System.out.println("================版本描述:" + mDesc +mVersionName +mVersionCode);
						
						//对比版本号
						if(mVersionCode > getVersionCode()){
							// 服务器的VersionCode大于本地的VersionCode
							// 说明有更新, 弹出升级对话框
							msg.what = CODE_UPDATE_DIALOG;
						}else{
							//版本没有更新
							msg.what = CODE_ENTER_HOME;
						}
					}
				}catch (MalformedURLException e) {
						// url错误
						e.printStackTrace();
						msg.what = CODE_URL_ERROR;
					}catch (IOException e) {
					// 网络错误。
						msg.what = CODE_NET_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
						// jason解析失败
						msg.what = CODE_JSON_ERROR;
						e.printStackTrace();
					}finally {
						long endTime = System.currentTimeMillis();
						long timeUsed = endTime - startTime;// 访问网络花费的时间
						if (timeUsed < 2000) {
							// 强制休眠一段时间,保证闪屏页展示2秒钟
							try {
								Thread.sleep(2000 - timeUsed);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
							mHandler.sendMessage(msg);
							if (conn != null) {
								conn.disconnect();// 关闭网络连接
							
						}
					}
			}
			
		};
		thread.start();
	}
    /**
     * 升级对话框
     */
    private void showUpdateDailog(){
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("最新版本：" + mVersionName);
    		builder.setMessage(mDesc);
    		builder.setPositiveButton("立即更新", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					download();
				}
			});
    		
    		builder.setNegativeButton("稍后再说", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					enterHome();
					
				}
			});
    		//如果用户直接按返回键，触发这个方法。
    		builder.setOnCancelListener( new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					enterHome();					
				}
			});
    		builder.show();
    }
    /**
     * 下载apk；
     */
    private void download() {
    	
		if(Environment.getExternalStorageState().
				equals(Environment.MEDIA_MOUNTED)){
			
			tvProgress.setVisibility(View.VISIBLE);// 显示进度
			
			String target = Environment.getExternalStorageDirectory()
					+ "/update.apk";
			//xutils
			HttpUtils http = new HttpUtils();
			http.download(mDownloadUrl, target, new RequestCallBack<File>() {
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					// TODO Auto-generated method stub
					super.onLoading(total, current, isUploading);
					tvProgress.setText("下载进度：" + current * 100 / total + "%");
				}
				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.setDataAndType(Uri.fromFile(arg0.result), "application/vnd.android.package-archive");
					startActivityForResult(intent, 0);// 如果用户取消安装的话,
													// 会返回结果,回调方法onActivityResult
				}
				
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(SplashActivity.this, "下载失败!",
							Toast.LENGTH_SHORT).show();
					enterHome();
				}
			});
		}else {
			Toast.makeText(SplashActivity.this, "没有找到sdcard!",
					Toast.LENGTH_SHORT).show();
		}
	}
    /**
     * 用户取消安装
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	enterHome();
    } 
    /**
     * 进入主界面。
     */
    private void enterHome(){
    	Intent intent = new Intent(SplashActivity.this,LoginTestActivity.class);
    	startActivity(intent);
    	finish();
    }
    /**
     * 拷贝数据库。
     */
    private void copyDB(String dbName) {
    	File destFile  = new File(getFilesDir(),dbName);
    	if(destFile.exists()) {
    		System.out.println("数据库已经拷贝了");
    		return;
    	}
    	InputStream in = null;
    	FileOutputStream out = null;
    	try {
			in = getAssets().open(dbName);
			out = new FileOutputStream(destFile);
			
			byte[] b = new byte[1024];
			int len = 0;
			while((len = in.read(b)) != -1) {
				out.write(b, 0, len);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
}
