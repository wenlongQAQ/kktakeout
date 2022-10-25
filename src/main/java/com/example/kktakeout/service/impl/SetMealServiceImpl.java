package com.example.kktakeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kktakeout.common.CustomException;
import com.example.kktakeout.dto.SetmealDto;
import com.example.kktakeout.entity.Setmeal;
import com.example.kktakeout.entity.SetmealDish;
import com.example.kktakeout.mapper.SetMealMapper;
import com.example.kktakeout.service.SetMealDishService;
import com.example.kktakeout.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, Setmeal> implements SetMealService {
    @Autowired
    private SetMealDishService setMealDishService;
    @Override
    @Transactional
    public void setMealWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
           item.setSetmealId(setmealDto.getId());
           return item;
        }).collect(Collectors.toList());

        setMealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐 同时删除套餐和菜品关联
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDIsh(List<Long> ids) {
        //查询套餐状态 确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids).eq(Setmeal::getStatus,1);

        int count = this.count(queryWrapper);
        //如果不能删除,抛出一个异常
        if(count>0){
            throw new CustomException("套餐正在售卖中,不能删除");
        }
        //如果可以删除,先删除套餐表中的数据
        this.removeByIds(ids);
        //删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> mealDishQueryWrapper = new LambdaQueryWrapper<>();
        mealDishQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setMealDishService.remove(mealDishQueryWrapper);
    }
}
