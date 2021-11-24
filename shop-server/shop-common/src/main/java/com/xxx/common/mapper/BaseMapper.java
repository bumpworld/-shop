package com.xxx.common.mapper;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

//通用mapper的父类，来统一定义实现接口接口
//IdsMapper 字符串式的id
//list集合的id组

//让spring先创建该类的实例
@RegisterMapper
public interface BaseMapper<T> extends Mapper<T>, IdsMapper<T>, IdListMapper<T,Long> {
}
