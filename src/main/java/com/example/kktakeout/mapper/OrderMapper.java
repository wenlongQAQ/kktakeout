package com.example.kktakeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kktakeout.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
