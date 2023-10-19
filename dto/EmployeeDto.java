package com.reggie.dto;

import com.reggie.entity.Employee;
import lombok.Data;

@Data
public class EmployeeDto extends Employee {
    private String deptName;
}
