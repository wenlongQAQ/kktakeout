package com.example.kktakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.kktakeout.common.R;
import com.example.kktakeout.entity.Category;
import com.example.kktakeout.entity.Employee;
import com.example.kktakeout.service.CategoryService;
import com.example.kktakeout.service.impl.CategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService service;
    @PostMapping
    public R<String> save(@RequestBody Category category){
        service.save(category);
        return R.success("新增成功");
    }

    /**
     * 菜品分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize){
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByDesc(Category::getSort);
        //执行查询
        service.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id修改
     * @param category
     * @return
     */
    @PutMapping
    public R<String> edit(@RequestBody Category category){
        service.updateById(category);
        return R.success("修改成功");
    }
    @DeleteMapping
    private R<String> deleteByID(Long id){
        service.remove(id);
        return R.success("删除成功");
    }
}
