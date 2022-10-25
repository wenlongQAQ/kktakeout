package com.example.kktakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.kktakeout.common.R;
import com.example.kktakeout.dto.DishDto;

import com.example.kktakeout.entity.Dish;
import com.example.kktakeout.service.CategoryService;
import com.example.kktakeout.service.DishFlavorService;
import com.example.kktakeout.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("添加成功");
    }

    /**
     * 分页查询菜品信息
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    private R<Page> page(Integer page,Integer pageSize,String name){
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,queryWrapper);
        //对象拷贝 将分页相关的信息赋值给 DishDto的分页,因为 Dish的分页 无法在页面上展示菜品的分类
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        //得到Dish数组
        List<Dish> records = pageInfo.getRecords();
        //根据Dish数组中的CategoryId查询CategoryName然后赋值给新的List
        List<DishDto> list = records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            Long categoryId = item.getCategoryId();
            BeanUtils.copyProperties(item,dishDto);
            if (categoryId!=null){
                String categoryName = categoryService.getById(categoryId).getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        //然后设置DishDto的page的数据
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询信息
     *
     * @param id
     * @return
     */

    @GetMapping("/{id}")
    public R<DishDto> getByID(@PathVariable Long id){
        Dish newDish = dishService.getById(id);
        Long categoryId = newDish.getCategoryId();
        String categoryName = categoryService.getById(categoryId).getName();
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(newDish,dishDto);
        dishDto.setCategoryName(categoryName);
        return R.success(dishDto);
    }

    /**
     * 修改员工信息
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> edit(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功");
    }
    /**
     * 根据id查询信息
     *
     * @param dish
     * @return
     */

    @GetMapping("/list")
    public R<List<Dish>> getCategoryDish(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);
    }
}
