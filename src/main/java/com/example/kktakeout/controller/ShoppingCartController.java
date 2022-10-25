package com.example.kktakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.kktakeout.common.R;
import com.example.kktakeout.entity.ShoppingCart;
import com.example.kktakeout.service.ShoppingCartService;
import com.example.kktakeout.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info(shoppingCart.toString());
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        if(dishId!=null){
            //添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
        if (cartServiceOne!=null){
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number+1);
            cartServiceOne.setCreateTime(LocalDateTime.now());
            shoppingCartService.updateById(cartServiceOne);
        }else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne=shoppingCart;
        }
        return R.success(cartServiceOne);
    }

    /**
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> listR(){
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        Long currentId = BaseContext.getCurrentId();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,currentId);
        lambdaQueryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list();
        return R.success(list);
    }

    /**
     *
     * @return
     */
    @DeleteMapping("/clean")
    private R<String > clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        boolean remove = shoppingCartService.remove(queryWrapper);
        if (remove) {
            return R.success("清空成功");
        }
        else {
            return R.error("呜呜呜失败里");
        }
    }
}
