package com.example.kktakeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kktakeout.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
