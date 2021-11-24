package com.xxx.user.service;

import com.xxx.admin.dao.ShowUser;
import com.xxx.exception.pojo.ExceptionEnum;
import com.xxx.exception.pojo.CustomException;
import com.xxx.user.config.UserConstants;
import com.xxx.user.mapper.UserMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
//@Transactional
public class UserService {

    @Autowired
    private UserMapper userMapper;

//    @Autowired
//    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate  redisTemplate;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 检查数据是否唯一
     * @param data 要检查的数据
     * @param type 1：用户名  2：手机
     * @return true表示可用 false表示不可用
     */
    public Boolean checkData(String data, Integer type) {
        ShowUser user = new ShowUser();
        switch (type){
            case 1:
                user.setUserName(data);
                break;
            case 2:
                user.setUserPhone(data);
                break;
            default:
                throw new CustomException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        int count = userMapper.selectCount(user);
        return count != 1;
    }

    public void sendCheckCode(String phone) {
        //生成验证码
        String code = RandomStringUtils.randomNumeric(4);
        //保存到redis 这里设置了5分钟
        redisTemplate.opsForValue().set(UserConstants.REDIS_KEY_PRE+phone,code,300, TimeUnit.SECONDS);

        //发送消息给rabbitMQ
        Map<String,String> map = new HashMap<>();
        map.put("phone",phone);
        map.put("code",code);

        //amqpTemplate.convertAndSend(MQConstants.Exchange.SMS_EXCHANGE_NAME,MQConstants.RoutingKey.VERIFY_CODE_KEY,map);
    }


    //注册
    public void register(ShowUser user, String code) {
        //获取redis的code
//        String key = UserConstants.REDIS_KEY_PRE+user.getUserPhone();
//        String rCode = redisTemplate.opsForValue().get(key);
//        if(!StringUtils.equals(rCode,code)){
//            throw new CustomException(ExceptionEnum.INVALID_VERIFY_CODE);//验证码不对
//        }
        //给密码加密
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));

        int status = userMapper.insertSelective(user);
        if(status!=1){
            throw new CustomException(ExceptionEnum.INSERT_OPERATION_FAIL); //添加失败
        }
    }

    public ShowUser findUserByNameAndPassword(String username, String password) {
        ShowUser record  = new ShowUser();
        record.setUserName(username);
        ShowUser user = userMapper.selectOne(record);
        if(user == null){
            //用户名未找到
            throw new CustomException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
        if(!passwordEncoder.matches(password,user.getUserPassword())){
            throw new CustomException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }

        return user;
    }
}
