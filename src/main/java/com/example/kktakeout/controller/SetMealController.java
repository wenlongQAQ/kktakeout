package com.example.kktakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.kktakeout.common.R;
import com.example.kktakeout.dto.SetmealDishDto;
import com.example.kktakeout.dto.SetmealDto;
import com.example.kktakeout.entity.Category;
import com.example.kktakeout.entity.Dish;
import com.example.kktakeout.entity.Setmeal;
import com.example.kktakeout.entity.SetmealDish;
import com.example.kktakeout.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetMealController {
    @Autowired
    private SetMealDishService setMealDishService;
    @Autowired
    private SetMealService setMealService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 新增套餐
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setMealService.setMealWithDish(setmealDto);
        return R.success("保存成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Setmeal::getName,name);

        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setMealService.page(pageInfo,queryWrapper);
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> setmeals = pageInfo.getRecords();
        List<SetmealDto> setmealDtos = setmeals.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            Long categoryId = item.getCategoryId();
            BeanUtils.copyProperties(item,setmealDto);
            if (categoryId != null){
                Category byId = categoryService.getById(categoryId);
                setmealDto.setCategoryName(byId.getName());
            }
            return setmealDto;

        }).collect(Collectors.toList());
        dtoPage.setRecords(setmealDtos);

        return R.success(dtoPage);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info(ids.toString());
        setMealService.removeWithDIsh(ids);
        return R.success("删除成功");
    }

    /**
     * 查询套餐
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> listR(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(Setmeal::getStatus,1);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setMealService.list(queryWrapper);
        return R.success(list);
    }
//    @GetMapping("/dish/{id}")
//    public R<Setmeal> mealDish(@PathVariable Long id){
//        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Setmeal::getId,id);
//        Setmeal one = setMealService.getOne(queryWrapper);
//        return R.success(one);
//    }
    @GetMapping("/dish/{id}")
    public R<List<SetmealDishDto>> mealDish(@PathVariable Long id){
//        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Setmeal::getId,id);
//        Setmeal one = setMealService.getOne(queryWrapper);
        LambdaQueryWrapper<SetmealDish> mealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setMealDishService.list(mealDishLambdaQueryWrapper);
        List<SetmealDishDto> dtos = list.stream().map((item)->{
            SetmealDishDto dishDto = new SetmealDishDto();
            LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishLambdaQueryWrapper.eq(Dish::getId,item.getDishId());
            BeanUtils.copyProperties(item,dishDto);
            Dish one = dishService.getOne(dishLambdaQueryWrapper);
            dishDto.setImage(one.getImage());
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dtos);
    }
}

