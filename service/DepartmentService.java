package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.dto.DepartmentDto;
import com.reggie.entity.Department;

import java.util.List;

public interface DepartmentService extends IService<Department> {

    /**
     * 新增部门，同时需要保存部门和员工的关联关系
     * @param departmentDto
     */
    public void saveWithEmployee(DepartmentDto departmentDto);

    public void remove(List<Long> ids);
}
