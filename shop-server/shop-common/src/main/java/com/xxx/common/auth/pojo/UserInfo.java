package com.xxx.common.auth.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class UserInfo {

    private Long id;

    private String username;
    
    private List<String> roles;

    private String token;

    public UserInfo(Long id, String username, List<String> roles){
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}