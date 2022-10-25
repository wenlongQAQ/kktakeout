package com.example.kktakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.kktakeout.common.R;
import com.example.kktakeout.entity.User;
import com.example.kktakeout.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public R<User> login(HttpServletRequest request, @RequestBody User user){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,user.getPhone());
        User one = userService.getOne(queryWrapper);
        if (one==null){
            User user1 = new User();
            user1.setPhone(user.getPhone());
            user1.setStatus(1);
            userService.save(user1);
            return R.error("注册成功 但是请你重新登陆");
        }else {
            request.getSession().setAttribute("user",one.getId());
        }
        return  R.success(one);
    }
}
