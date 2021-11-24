package com.xxx.common.auth.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppInfo {

    private Long id;

    private String serviceName;
    
    private List<Long> targetList;
}