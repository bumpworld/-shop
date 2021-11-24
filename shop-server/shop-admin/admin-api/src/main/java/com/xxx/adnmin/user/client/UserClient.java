package com.xxx.adnmin.user.client;

import com.xxx.admin.dao.ShowUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient("admin-service")
public interface UserClient {

    @GetMapping("/query")
    public ShowUser findUserByNameAndPassword(@RequestParam("username") String username,
                                              @RequestParam("password") String password);

    @PutMapping("/stock/minus")
    public ResponseEntity<Void> minusStock(@RequestBody Map<Long, Integer> paramMap);
}
