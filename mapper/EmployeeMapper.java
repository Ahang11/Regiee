package com.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.dto.EmployeeDto;
import com.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
    Page<EmployeeDto> queryPage(@Param("page")IPage<EmployeeDto> page, @Param("name") String name);
}
