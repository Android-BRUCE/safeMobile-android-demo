package com.safe.a360.safemobile.utils;

/**
 * Created by Administrator on 2018-07-14.
 */

public class SingleClick {
    private static final int DEFAULT_TIME = 2000;
    private static long lastTime;

    public static boolean isSingle(){
        boolean isSingle ;
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastTime <= DEFAULT_TIME){
            isSingle = true;
        }else{
            isSingle = false;
        }
        lastTime = currentTime;

        return isSingle;
    }
}
