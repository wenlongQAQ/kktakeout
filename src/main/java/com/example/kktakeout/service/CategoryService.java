package com.example.kktakeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.kktakeout.entity.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
