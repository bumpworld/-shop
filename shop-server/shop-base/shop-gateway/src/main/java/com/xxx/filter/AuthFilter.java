package com.xxx.filter;

import com.xxx.common.auth.pojo.Payload;
import com.xxx.common.auth.pojo.UserInfo;
import com.xxx.common.auth.rsa.JwtProperties;
import com.xxx.common.auth.rsa.JwtUtils;
import com.xxx.common.constant.Constants;
import com.xxx.config.FilterProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtProperties jwtProp;

    @Autowired
    private FilterProperties filterProp;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //获取请求响应对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();


        Payload<UserInfo> payload = null;
        if(isAllowPath(request.getURI().getPath())){
            //白名单放行
            return chain.filter(exchange);
        }

        //解析token
        try{
            //获取token
            String token = request.getHeaders().getFirst(Constants.APP_TOKEN_HEADER);
            payload = JwtUtils.getInfoFromToken(token, jwtProp.getPublicKey(), UserInfo.class);
        }catch (Exception e){
            //完成响应
            System.out.println("未登录授权");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //获取用户id放入请求头
        exchange.getRequest().mutate().header(Constants.AUTH_USER_ID_HEADER_KEY,payload.getUserInfo().getId().toString()).build();

        //放行
       return chain.filter(exchange);
    }

    private boolean isAllowPath(String path) {
        List<String> allowPaths = filterProp.getAllowPaths();

        for (String allowPath:allowPaths){
            if(path.contains(allowPath)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
