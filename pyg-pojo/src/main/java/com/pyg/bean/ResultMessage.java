package com.pyg.bean;

import java.io.Serializable;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/9 21:02
 * @description 封装前端带有信息的类
 **/


public class ResultMessage implements Serializable {

    private boolean success;

    private String message;

    public ResultMessage(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
