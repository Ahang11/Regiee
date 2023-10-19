package com.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.common.CustomerException;
import com.reggie.dto.DepartmentDto;
import com.reggie.entity.Department;
import com.reggie.entity.DepartmentEmployee;
import com.reggie.entity.Employee;
import com.reggie.mapper.DepartmentMapper;
import com.reggie.mapper.EmployeeMapper;
import com.reggie.service.DepartmentEmployeeService;
import com.reggie.service.DepartmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    /**
     * 新增一个部门
     */
    @Autowired
    private DepartmentEmployeeService departmentEmployeeService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public void saveWithEmployee(DepartmentDto departmentDto) {
        //保存部门的基本信息 部门关联着员工，所以在添加部门时要把员工加入
       this.save(departmentDto);
       //这是建立中间表DepartmentEmployee实现部门员工关联关系的代码
       /* List<DepartmentEmployee> departmentEmployees = departmentDto.getDepartmentEmployees();
        departmentEmployees.stream().map((item) -> {
            item.setEmployeeId(item.getId());
            item.setDepartmentId(departmentDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存部门和员工的关系
        departmentEmployeeService.saveBatch(departmentEmployees);*/

        if(departmentDto.getEmployees() != null){
            for(Employee employee : departmentDto.getEmployees()){
                employee.setDeptId(departmentDto.getId());
                employeeMapper.updateById(employee);
            }
        }
    }



    /**
     * 删除部门
     * @param ids
     */
    @Override
    public void remove(List<Long> ids) {
        //检查部门员工关联表中是否有与这些部门ID关联的员工，如果还有员工则无法删除
        LambdaQueryWrapper<DepartmentEmployee> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(DepartmentEmployee::getDepartmentId,ids);

        int count = departmentEmployeeService.count(queryWrapper1);
        if (count > 0){
            throw new CustomerException("部门中还有员工，无法删除！");
        }
        //若是可以删除，则先删除部门表中的关系
        this.removeByIds(ids);

        //删除部门员工关联表中的数据——
        LambdaQueryWrapper<DepartmentEmployee> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.in(DepartmentEmployee::getDepartmentId,ids);
        departmentEmployeeService.remove(queryWrapper2);
    }

}
