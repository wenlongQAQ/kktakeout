package com.example.kktakeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kktakeout.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee>{
}
