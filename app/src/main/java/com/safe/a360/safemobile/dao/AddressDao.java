package com.safe.a360.safemobile.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 归属地查询工具类
 * @author BRUCE
 *
 */
public class AddressDao{
	private static final String PATH = "data/data/com.safe.a360.safemobile/files/address.db";
	public static String getAddress (String number) {
		String queryResult = "未知号码";//默认为未知号码
		// 手机号码特点: 1 + (3,4,5,6,7,8) + (9位数字)
		// 正则表达式
		// ^1[3-8]\d{9}$
		SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
		if(number.matches("^1[3-8]\\d{9}$")) {
			Cursor cursor = database.rawQuery("select location from data2 where id=(select outkey from data1 where id=?)"
					,new String[] {number.substring(0, 7)});
			if(cursor.moveToNext()) {
				queryResult =  cursor.getString(0);
			}
			
			cursor.close();
		}else if (number.matches("^\\d+$")) {//匹配其他数字
			switch (number.length()) {
			case 3:
				queryResult = "报警电话";
				break;
			case 4:
				queryResult = "模拟器";
				break;
			case 5:
				queryResult = "客服电话";
			case 6:
			case 7:
				queryResult = "本地电话";
				break;
			default:
				//查询区号
				if(number.startsWith("0") && number.length() > 10) {
					//查询四位区号
					Cursor cursor = database.rawQuery(
							"select location from data2 where area = ?", 
							new String[]{number.substring(1, 4)});//数据库把开头的0省去了
					
					if(cursor.moveToNext()){
						queryResult = cursor.getString(0);
					}else {
						cursor.close();
						
						//查询三位区号
						cursor = database.rawQuery(
								"select location from data2 where area = ?", 
								new String[]{number.substring(1, 3)});
						if(cursor.moveToNext()) {
							queryResult = cursor.getString(0);
						}
						cursor.close();
					}
				}
				break;
			}
		}
		database.close();//关闭数据库
		return queryResult;
	}
}
