package com.example.kktakeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kktakeout.entity.Dish;
import com.example.kktakeout.mapper.DishMapper;
import com.example.kktakeout.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
