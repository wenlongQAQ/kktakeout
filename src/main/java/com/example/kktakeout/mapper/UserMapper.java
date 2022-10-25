package com.example.kktakeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kktakeout.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
