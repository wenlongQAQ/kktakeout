package com.example.kktakeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kktakeout.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
