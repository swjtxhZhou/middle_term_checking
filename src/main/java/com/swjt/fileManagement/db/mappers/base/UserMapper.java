package com.swjt.fileManagement.db.mappers.base;


import com.swjt.fileManagement.data.base.User;

import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface UserMapper extends Mapper<User> {
}