package com.example.kktakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.kktakeout.common.R;
import com.example.kktakeout.entity.Employee;
import com.example.kktakeout.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1. 将页面提交的密码进行md5加密处理
        String password = employee.getPassword();
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2. 根据页面提交的用户信息查询数据库
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(lambdaQueryWrapper);
        //3.判断没有查询到则返回登录失败结果
        if (emp ==null){
            return R.error("登录失败,账号错误");
        }
        //4.密码比对
        if(!emp.getPassword().equals(md5Password)){
            return R.error("登录失败,密码错误");
        }
        //5.查看员工状态
        if (emp.getStatus()==0){
            return R.error("你已被禁用!");
        }
        //6.登陆成功,将员工ID存入Session并返回成功结果
        request.getSession().setAttribute("employee",employee.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //1.清理色送戳女色保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return R.success("清除成功");
    }
}
