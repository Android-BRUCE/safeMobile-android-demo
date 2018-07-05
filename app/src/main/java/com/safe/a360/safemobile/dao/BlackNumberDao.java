package com.safe.a360.safemobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.safe.a360.safemobile.bean.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BRUCE on 2016/5/29 0029.
 */
public class BlackNumberDao {

    public  BlackNumberOpenHelper helper;

    public BlackNumberDao(Context context){
        helper = new BlackNumberOpenHelper(context);
    }

    /**
     * 添加黑名单号码
     * @param number
     * @param mode
     * @return
     */
    public boolean add(String number , String mode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("number", number);
        contentValues.put("mode", mode);
       long rawid = db.insert("blacknumber",null,contentValues);
        if (rawid == -1){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 通过号码删除黑名单
     * @param number 需要移除黑名单的号码
     * @author BRUCE
     * @return true为删除成功，false为没有影响行数
     */
    public boolean delete(String number) {
        SQLiteDatabase db = helper.getWritableDatabase();
        long rawid = db.delete("blacknumber","number = ?",new String[]{number});
        if (rawid == 0) {//没有影响行数
            return false;
        }else {
            return true;
        }
    }

    /**
     * 通过号码改变拦截的模式
     * @param number
     */
    public boolean changeNumberMode(String number, String mode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mode", mode);
        long rawid = db.update("blacknumber",contentValues,"number = ?",new String[]{number});
        if (rawid == -1) {
            return false;
        }else {
            return true;
        }
    }

    /**
     * 返回一个黑名单号码的拦截模式
     * @return
     */
    public String findNumber(String number){
        SQLiteDatabase db = helper.getReadableDatabase();
        String mode = "0";
        Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "number = ?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()) {
            mode = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return mode;
    }

    /**
     * 查询所有的黑名单信息
     */
    public List<BlackNumberInfo> findAll(){
        SQLiteDatabase db = helper.getReadableDatabase();
        List<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
        Cursor cursor = db.query("blacknumber", new String[]{"number", "mode"}, null, null, null, null, null, null);
        while(cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setNumber(cursor.getString(0));
            blackNumberInfo.setMode(cursor.getString(1));
            blackNumberInfos.add(blackNumberInfo);
        }
        cursor.close();
        db.close();
        SystemClock.sleep(3000);//模拟网络延迟
        return blackNumberInfos;
    }
    /**
     * 分页加载数据方法
     * @param pageNumber 当前是第几页
     * @param pageSize 每页加载多少数据
     * @return
     * limit 表示限制当前有多少数据
     * offset 表示跳过 从第几条数据开始
     */
    public List<BlackNumberInfo> findPar(int pageNumber, int pageSize ) {
    	pageNumber = pageNumber - 1;
    	SQLiteDatabase db = helper.getReadableDatabase();
    	Cursor cursor = db.rawQuery("select number,mode from blacknumber limit ? offset ?",new String[]{String.valueOf(pageSize), String.valueOf(pageSize * pageNumber)});
    	ArrayList<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
    	while (cursor.moveToNext()) {
    		BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
    		blackNumberInfo.setNumber(cursor.getString(0));
    		blackNumberInfo.setMode(cursor.getString(1));
    		blackNumberInfos.add(blackNumberInfo);
    	}
    	cursor.close();
    	db.close();
		return blackNumberInfos;	
    }
    /**
     * 分批加载数据方法
     * @param startIndex 开始的位置
     * @param maxCount 每页展示最大的条目
     * @return
     */
    public List<BlackNumberInfo> findPar2(int startIndex, int maxCount ) {
    	
    	SQLiteDatabase db = helper.getReadableDatabase();
    	Cursor cursor = db.rawQuery("select number,mode from blacknumber limit ? offset ?",new String[]{String.valueOf(maxCount), String.valueOf(startIndex)});
    	ArrayList<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
    	while (cursor.moveToNext()) {
    		BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
    		blackNumberInfo.setNumber(cursor.getString(0));
    		blackNumberInfo.setMode(cursor.getString(1));
    		blackNumberInfos.add(blackNumberInfo);
    	}
    	cursor.close();
    	db.close();
    	//SystemClock.sleep(3000);
		return blackNumberInfos;	
    }
    /**
     * 获取总共有多少条数据。
     * @return
     */
    public int getAllNumber() {
    	SQLiteDatabase db = helper.getReadableDatabase();
    	Cursor cursor = db.rawQuery("select count( * ) from blacknumber ", null);
		cursor.moveToNext();
		int count = cursor.getInt(0);
		cursor.close();
		db.close();
		return count;
    }
}
