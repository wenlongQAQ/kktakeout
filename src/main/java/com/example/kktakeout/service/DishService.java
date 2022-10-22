package com.example.kktakeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.kktakeout.dto.DishDto;
import com.example.kktakeout.entity.Dish;

public interface DishService extends IService<Dish> {
    //新增菜品同时插入菜品对应的口味数据
     void saveWithFlavor(DishDto dishDto);
     void updateWithFlavor(DishDto dishDto);
}
