package com.example.kktakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.kktakeout.common.R;
import com.example.kktakeout.entity.Employee;
import com.example.kktakeout.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
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
        request.getSession().setAttribute("employee",emp.getId());
        System.out.println(request.getSession().getAttribute("employee"));
        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //1.清理当前登录员工的id
        request.getSession().removeAttribute("employee");
        return R.success("清除成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        String password =DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(password);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        employee.setCreateUser((Long) request.getSession().getAttribute("employee"));
        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        boolean save = employeeService.save(employee);
        if (save) {
            return R.success("新增成功");
        }
        return R.error("注册失败里");
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){
        log.info("page = {} , pageSize={},name={}",page,pageSize,name);
        //构造分页构造器
        Page pageInfo = new Page<>(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
        //添加过滤条件
        queryWrapper.like(!StringUtils.isEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        employee.setUpdateTime(LocalDateTime.now());
        Long updateUser = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateUser(updateUser);
        employeeService.updateById(employee);
        return R.success(employee.getStatus()==0?"启用成功":"禁用成功");
    }
    @GetMapping("/{id}")
    public R<Employee> selectByid(@PathVariable Long id){
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
        queryWrapper.eq(Employee::getId,id);
        Employee one = employeeService.getOne(queryWrapper);
        if (one!=null){
            return R.success(one);
        }
        else return R.error("查询失败");
    }
}
