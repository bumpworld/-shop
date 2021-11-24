package com.xxx.auth.mapper;


import java.util.List;

public interface ApplicationInfoMapper{

    List<Long> queryTargetIdList(Long serviceId);
}