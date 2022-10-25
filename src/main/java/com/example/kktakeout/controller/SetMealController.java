package com.example.kktakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.kktakeout.common.R;
import com.example.kktakeout.dto.SetmealDto;
import com.example.kktakeout.entity.Category;
import com.example.kktakeout.entity.Setmeal;
import com.example.kktakeout.service.CategoryService;
import com.example.kktakeout.service.SetMealDishService;
import com.example.kktakeout.service.SetMealService;
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
}
