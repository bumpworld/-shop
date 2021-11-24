package com.xxx.admin.dao;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ShowUser {
    private Long id;
    private String userName;
    private String userPhone;
    private String userPassword;
    private String userPic;
    private Integer userGender;
    private String wxId;
    private Long gold;
    private Integer status;
    private Long createTime;
}
