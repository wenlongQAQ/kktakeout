package com.example.kktakeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.kktakeout.entity.Orders;

public interface OrderService extends IService<Orders> {
    void submit(Orders orders);
}
