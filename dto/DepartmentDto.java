package com.reggie.dto;

import com.reggie.entity.Department;
import com.reggie.entity.DepartmentEmployee;
import com.reggie.entity.Employee;
import lombok.Data;

import java.util.List;

@Data
public class DepartmentDto extends Department {
    private List<DepartmentEmployee> departmentEmployees;
    private List<Employee> employees;
}
