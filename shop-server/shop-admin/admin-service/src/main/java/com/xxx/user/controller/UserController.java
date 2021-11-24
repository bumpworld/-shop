package com.xxx.user.controller;

import com.xxx.admin.dao.ShowUser;
import com.xxx.exception.pojo.CustomException;
import com.xxx.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 检查用户名或者手机是否唯一
     * @param data 数据
     * @param type 类型 1：用户名 2：手机
     * @return true 表示可用 false 表示不可用
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkData(@PathVariable("data") String data,@PathVariable("type") Integer type){
        Boolean b = userService.checkData(data,type);
        return ResponseEntity.ok(b);
    }


    //获取短信验证码
    @PostMapping("/code")
    public ResponseEntity<Void> sendCheckCode(@RequestParam("phone")String phone){
        userService.sendCheckCode(phone);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //注册
    @PostMapping("/register")
    public ResponseEntity<Void> register( ShowUser user,
                                         BindingResult result, //此对象要紧挨着需要被校验的对象
                                         @RequestParam("code")String code) {

        //校验数据
        if(result.hasErrors()){
            //收集异常信息
            String errorStr = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining("|"));
            //抛出获取异常信息
            throw new CustomException(400, errorStr);
        }

        userService.register(user,code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //登录方法
    @GetMapping("/query")
    public ResponseEntity<ShowUser> findUserByNameAndPassword(@RequestParam("username") String username,
                                                          @RequestParam("password") String password){
        ShowUser user = userService.findUserByNameAndPassword(username, password);
        return ResponseEntity.ok(user);
    }
}
