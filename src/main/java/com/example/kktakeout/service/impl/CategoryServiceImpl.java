package com.example.kktakeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kktakeout.common.CustomException;
import com.example.kktakeout.entity.Category;
import com.example.kktakeout.entity.Dish;
import com.example.kktakeout.entity.Setmeal;
import com.example.kktakeout.mapper.CategoryMapper;
import com.example.kktakeout.service.CategoryService;
import com.example.kktakeout.service.DishService;
import com.example.kktakeout.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类,删除之前进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联了菜品 如果关联 直接抛出异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(dishLambdaQueryWrapper);
        if (count>0){
            //抛出异常
            throw new CustomException("当前分类中有菜品");
        }
        LambdaQueryWrapper<Setmeal> setmeal = new LambdaQueryWrapper<>();
        setmeal.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmeal);
        if (count2>0){
            //抛出异常
            throw new CustomException("当前分类中有套餐");
        }
        super.removeById(id);

        //查询是否关联了套餐

    }
}
