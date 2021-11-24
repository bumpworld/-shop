package com.xxx.auth.service;

import com.xxx.admin.dao.ShowUser;
import com.xxx.adnmin.user.client.UserClient;
import com.xxx.common.auth.pojo.Payload;
import com.xxx.common.auth.pojo.UserInfo;
import com.xxx.common.auth.rsa.JwtProperties;
import com.xxx.common.auth.rsa.JwtUtils;
import com.xxx.common.auth.rsa.RsaUtils;
import com.xxx.common.constant.Constants;
import com.xxx.exception.pojo.ExceptionEnum;
import com.xxx.exception.pojo.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AuthService {

//    @Autowired
//    private ApplicationInfoMapper applicationInfoMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserClient userClient;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public void login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        ShowUser user = userClient.findUserByNameAndPassword(username, password);
        //获取用户权限
        List<String> roles = new ArrayList<>();
        try{
            UserInfo userInfo = new UserInfo(user.getId(), user.getUserName(), roles);
            //构建token并写入浏览器的cookie中
            String token = createTokenWriteToCookie(userInfo);
            userInfo.setToken(token);
        }catch (Exception e){
            throw new CustomException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
    }

    private String createTokenWriteToCookie( UserInfo userInfo) throws Exception {
        String token = JwtUtils.generateTokenExpireInMinutes(userInfo, jwtProperties.getPrivateKey(),jwtProperties.getCookie().getExpire());
       return token;

    }



    //验证 //当过期时间还剩10分钟，就刷新过期时间
    public UserInfo verify(HttpServletRequest request, HttpServletResponse response) {
        //获取 cookie
        String token = request.getHeader(Constants.APP_TOKEN_HEADER);
        Payload<UserInfo> userInfoPayload = null;
        //解密
        try{
            userInfoPayload = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey(), UserInfo.class);

            //获取token的id
            String payloadId = userInfoPayload.getId();

            //比对redis的黑名单
            if(redisTemplate.hasKey(payloadId)){
                //用户已经退出
                throw new CustomException(ExceptionEnum.UNAUTHORIZED);
            }

            UserInfo userInfo = userInfoPayload.getUserInfo();

            //过期时间
            Date expiration = userInfoPayload.getExpiration();

            //刷新这个cookie的时间
            Date refreshDateTime = DateUtils.addSeconds(expiration, -jwtProperties.getCookie().getExpire() * 60);

            if(refreshDateTime.before(Calendar.getInstance().getTime())){
                token = createTokenWriteToCookie(userInfo);
                userInfo.setToken(token);
            }
            return userInfo;

        }catch (Exception e){
            //如果解析失败，表示用户没有登录，抛出异常
            throw new CustomException(ExceptionEnum.UNAUTHORIZED);
        }
    }

    //退出登录
    public void layout(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(Constants.APP_TOKEN_HEADER);
        try {
            //获取token
            //解析token 获取载荷
            Payload<Object> payload = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            //获取载荷id
            String payloadId = payload.getId();

            //计算过期时间
            Long remainTime = payload.getExpiration().getTime() - System.currentTimeMillis();

            //存入redis的黑名单  设置过期时间为token的有效时间
            redisTemplate.opsForValue().set(payloadId, "1", remainTime, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            e.printStackTrace();
            log.info("当前要退出登录的用户token已经失效！");
        }

    }




//    /*根据服务id和服务名称查询服务是否存在*/
//    public Boolean isUsable(Long id, String serviceName){
//        ApplicationInfo applicationInfo = applicationInfoMapper.selectByPrimaryKey(id);
//        if(applicationInfo == null){
//            //服务id未找到
//            return false;
//        }
//
//
//        if(!passwordEncoder.matches(serviceName,applicationInfo.getSecret())){
//            //服务密码不正确
//            return false;
//        }
//        return true;
//    }


//    /**
//     * 微服务的认证 先校验服务是否可用，在获取权限列表生成token返回
//     * @param id
//     * @param serviceName
//     * @return token
//     */
//    public String authorize(Long id, String serviceName) {
//        if(!isUsable(id,serviceName)){
//            log.error("【服务器申请token】异常！服务id或者服务名称不正确!");
//            throw new CustomException(ExceptionEnum.INVALID_SERVER_ID_SECRET);
//        }
//        //获取服务列表
//        //查询出当前服务所能访问的服务id列表
//        List<Long> serviceIds = applicationInfoMapper.queryTargetIdList(id);
//
//        //封装载荷对象
//        AppInfo appInfo = new AppInfo(id,serviceName,serviceIds);
//
//        //生成token
//        String token = JwtUtils.generateTokenExpireInMinutes(appInfo, jwtProperties.getPrivateKey(), jwtProperties.getApp().getExpire());
//
//        return token;
//    }

}
