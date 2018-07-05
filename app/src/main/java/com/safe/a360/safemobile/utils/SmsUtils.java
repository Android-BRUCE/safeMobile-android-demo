package com.safe.a360.safemobile.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 短信备份工具类
 * @author BRUCE
 *
 */
public class SmsUtils {
	/**
	 * 短信备份接口
	 * @author BRUCE
	 * @param before 备份前知道总的短信条数
	 * @param onBackUpSms 备份时候知道已经备份多少条了。
	 */
	public interface backUpCallBackSms {
		public void before(int count);
		public void onBackUpSms(int process);
	}
	public static boolean backUp(Context context, backUpCallBackSms backup) {
		/**
		 * 1.判断是否存在sd卡
		 * 2.如果有，获取系统短信
		 * 		使用内容观察者。
		 * 3.把短信写到SD卡中
		 */
		
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			ContentResolver resolver = context.getContentResolver();
			Uri uri = Uri.parse("content://sms");
			Cursor cursor = resolver.query(uri, new String[]{"address","date","type","body"}, //type:1是接收短信2是发送
					null, null, null);
			//获取当前一共短信条数。
			int count = cursor.getCount();
			backup.before(count);
			//pd.setMax(count);
			int process = 0;//进度条
			try {
				File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");
				FileOutputStream os = new FileOutputStream(file);
				// 得到序列化器
				// 在android系统里面所有有关xml的解析都是pull解析
				XmlSerializer serializer = Xml.newSerializer();
				// 把短信序列化到sd卡然后设置编码格式
				serializer.setOutput(os,"UTF-8");
				// standalone表示当前的xml是否是独立文件 ture表示文件独立。yes
				serializer.startDocument("UTF-8", true);
				serializer.startTag(null, "smss");//设置节点的名字
				//serializer.attribute(null, "size", value);
				
				while(cursor.moveToNext()){
					System.out.println("address = " + cursor.getString(0));
					serializer.startTag(null, "sms");
					
					serializer.startTag(null, "address");
					serializer.text(cursor.getString(0));
					serializer.endTag(null, "address");
					
					serializer.startTag(null, "date");
					serializer.text(cursor.getString(1));
					serializer.endTag(null, "date");
					
					serializer.startTag(null, "type");
					serializer.text(cursor.getString(2));
					serializer.endTag(null, "type");
					
					serializer.startTag(null, "body");
					//加密
					serializer.text(Crypto.encrypt("miyue", cursor.getString(3)));
					serializer.endTag(null, "body");
					
					serializer.endTag(null, "sms");
					
					process ++;
					//pd.setProgress(process);
					backup.onBackUpSms(process);
					SystemClock.sleep(200);
					
				}
				cursor.close();
				serializer.endTag(null, "smss");
				serializer.endDocument();
				os.flush();
				os.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
}
