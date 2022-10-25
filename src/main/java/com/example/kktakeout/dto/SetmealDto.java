package com.example.kktakeout.dto;


import com.example.kktakeout.entity.Setmeal;
import com.example.kktakeout.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
