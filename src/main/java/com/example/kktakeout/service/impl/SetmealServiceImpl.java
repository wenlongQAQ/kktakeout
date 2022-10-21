package com.example.kktakeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kktakeout.entity.Dish;
import com.example.kktakeout.entity.Setmeal;
import com.example.kktakeout.mapper.DishMapper;
import com.example.kktakeout.mapper.SetmealMapper;
import com.example.kktakeout.service.DishService;
import com.example.kktakeout.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService{
}
