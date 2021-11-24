package com.xxx.exception.pojo;


public class CustomException extends  RuntimeException {

    private Integer status; //这是我们可以指定的状态码

    public CustomException(Integer status, String message) {
        super(message);//这是错误信息
        this.status = status;
    }

    public CustomException(ExceptionEnum e) {
        super(e.getMessage());
        this.status = e.getStatus();
    }

    public Integer getStatus() {
        return status;
    }
}
