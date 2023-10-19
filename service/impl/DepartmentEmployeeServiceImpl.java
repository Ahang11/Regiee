package com.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.entity.DepartmentEmployee;
import com.reggie.mapper.DepartmentEmployeeMapper;
import com.reggie.service.DepartmentEmployeeService;
import org.springframework.stereotype.Service;

@Service
public class DepartmentEmployeeServiceImpl extends ServiceImpl<DepartmentEmployeeMapper, DepartmentEmployee> implements DepartmentEmployeeService {
}
