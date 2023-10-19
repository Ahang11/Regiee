package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.R;
import com.reggie.dto.DepartmentDto;
import com.reggie.entity.Department;
import com.reggie.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理
 */
@Slf4j
@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 新增某个部门
     * @param departmentDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DepartmentDto departmentDto){
        log.info("部门信息：{}",departmentDto);
        departmentService.saveWithEmployee(departmentDto);
        return R.success("部门新增成功！");
    }

    /**
     * 删除某个部门
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        departmentService.remove(ids);
        return R.success("部门删除成功！");
    }

    /**
     * 修改一个部门信息
     * @param department
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Department department){
        log.info("修改分类信息：{}",department);
        departmentService.updateById(department);
        return R.success("修改分类信息成功！");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page = {}, pageSize = {}, name = {}",page, pageSize, name);

        //构造分页构造器
        Page<Department> pageinfo = new Page<>(page, pageSize);

        //条件构造器
        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
        //过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Department::getDeptName, name);
        //排序条件
        queryWrapper.orderByDesc(Department::getUpdateTime);

        //执行查询
        departmentService.page(pageinfo,queryWrapper);
        return R.success(pageinfo);
    }

    /**
     * 不分页查询
     * @param name
     * @return
     */
    @GetMapping("/all")
    public R<List<Department>> listall(String name){
        log.info("name = {}", name);

        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Department::getDeptName, name);
        queryWrapper.orderByDesc(Department::getUpdateUser);

        List<Department> list = departmentService.list(queryWrapper);
        return R.success(list);
    }


}
