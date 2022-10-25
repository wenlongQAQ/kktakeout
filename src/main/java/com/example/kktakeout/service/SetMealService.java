package com.example.kktakeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.kktakeout.dto.SetmealDto;
import com.example.kktakeout.entity.Setmeal;

import java.util.List;

public interface SetMealService extends IService<Setmeal> {
    void setMealWithDish(SetmealDto setmealDto);
    void removeWithDIsh(List<Long> ids);
}
