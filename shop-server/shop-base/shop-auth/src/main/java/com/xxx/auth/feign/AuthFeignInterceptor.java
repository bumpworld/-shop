package com.xxx.auth.feign;

import com.xxx.common.constant.Constants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


//feign的拦截器
@Component
public class AuthFeignInterceptor implements RequestInterceptor {

    @Autowired
    private TokenHolder tokenHolder;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        //在发起请求之前添加token进请求头
        requestTemplate.header(Constants.APP_TOKEN_HEADER,tokenHolder.getToken());
    }
}
