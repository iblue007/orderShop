package com.xjt.baselib.bean;

import java.io.Serializable;

/**
 * Create by xuqunxing on  2020/1/22
 */
public class CommonResultMessage implements Serializable {

    private String message;
    private boolean success;
    private int code = 0 ;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
