package com.xxx.auth.controller;

import com.xxx.auth.service.AuthService;
import com.xxx.common.auth.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;



    //登录、认证
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestParam("username") String username,
                                      @RequestParam("password") String password,
                                      HttpServletRequest request,
                                      HttpServletResponse response){

        authService.login(username,password,request,response);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //授权
    @GetMapping("/verify")
    public ResponseEntity<UserInfo> verify(HttpServletRequest request,
                                           HttpServletResponse response){
        UserInfo userInfo = authService.verify(request, response);
        return ResponseEntity.ok(userInfo);
    }

    /**
     * 退出登录
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> layout(HttpServletRequest request,
                                       HttpServletResponse response){

        authService.layout(request,response);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

//    /**
//     * 微服务的授权功能
//     * @param id       服务id
//     * @param serviceName   服务名称
//     * @return         给服务签发的token
//     */
//    @GetMapping("/authorization")
//    public ResponseEntity<String> authorize(@RequestParam("id") Long id,
//                                            @RequestParam("serviceName") String serviceName){
//        return ResponseEntity.ok(authService.authorize(id, serviceName));
//    }

}
