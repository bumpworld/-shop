package com.xxx.user.interceptor;

import com.xxx.common.auth.pojo.AppInfo;
import com.xxx.common.auth.pojo.Payload;
import com.xxx.common.auth.rsa.JwtUtils;
import com.xxx.common.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
@Slf4j
public class AppTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProp;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取token
        String token = request.getHeader(Constants.APP_TOKEN_HEADER);
        //解析token
        try{
            Payload<AppInfo> payload = JwtUtils.getInfoFromToken(token, jwtProp.getPublicKey(), AppInfo.class);
            //鉴权
            List<Long> targetList = payload.getUserInfo().getTargetList();
            if(CollectionUtils.isEmpty(targetList) || !targetList.contains(jwtProp.getApp().getId())){
                log.error("【服务鉴权】未通过！来访服务没有访问权限！");
                //阻止访问处理器
                return false;
            }
        }catch (Exception e){
            log.error("【服务鉴权】未通过！来访服务的token不合法！");
            //阻止访问处理器
            return false;
        }
        log.error("【服务鉴权】通过！");
        return true;
    }
}
