package com.safe.a360.safemobile.bean;

/**
 * Created by BRUCE on 2016/5/29 0029.
 * 黑名单拦截模式的java bean
 */
public class BlackNumberInfo {
    /**
     * 黑名单号码
     */
    private String number;
    /**
     * 号码拦截模式
     * 1 ->全部拦截
     * 2 ->电话拦截
     * 3 ->短信拦截
     */
    private String mode;

    public BlackNumberInfo() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}

