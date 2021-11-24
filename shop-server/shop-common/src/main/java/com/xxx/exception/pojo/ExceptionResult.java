package com.xxx.exception.pojo;


import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.unit.DataUnit;
import org.springside.modules.utils.time.DateFormatUtil;

//自定义封装返回结构
public class ExceptionResult {

    private int status;//响应码
    private String message;//响应信息
    private String timestamp;//响应时间搓

    //这里接收一个LyException来初始化我们的数据
    public ExceptionResult(CustomException e) {
        this.status = e.getStatus();
        this.message = e.getMessage();
        this.timestamp = DateFormatUtil.formatDate("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
    }

}
