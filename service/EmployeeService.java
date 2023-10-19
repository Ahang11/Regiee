package com.reggie.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.entity.Employee;
import com.reggie.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService extends IService<Employee> {
    public Page<EmployeeDto> queryPage(int page, int pageSize, String name);
}
