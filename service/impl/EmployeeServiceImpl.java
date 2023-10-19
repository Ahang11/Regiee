package com.reggie.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.dto.EmployeeDto;
import com.reggie.entity.Employee;
import com.reggie.mapper.EmployeeMapper;
import com.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    /*
    @Autowired
    private EmployeeMapper employeeMapper;
   @Override
    public Page<EmployeeDto> pageWithDeptName(Page<EmployeeDto> page, String name) {
        List<EmployeeDto> employeeDtoList = employeeMapper.pageWithDeptName(page, name);
        page.setRecords(employeeDtoList);
        return page;
    }*/

    @Override
    public Page<EmployeeDto> queryPage(int page, int pageSize, String name) {
        Page<EmployeeDto> pageinfo = new Page<>(page, pageSize);
        return  this.getBaseMapper().queryPage(pageinfo, name);
    }
}
