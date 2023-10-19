package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.dto.DepartmentDto;
import com.reggie.dto.EmployeeDto;
import com.reggie.entity.Employee;
import com.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import com.reggie.common.R;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j//用于生成日志记录器字段
@RestController//用于标识该类为控制器，并将方法的返回值作为 HTTP 响应的内容返回
@RequestMapping("/employee")
//是 Spring MVC 提供的注解，用于映射 HTTP 请求到处理器方法（Controller 方法）。在这里，@RequestMapping("/employee") 表示该控制器处理以 "/employee" 开头的请求路径。当收到 "/employee" 的 POST 请求时，将会调用与之对应的处理器方法。
public class EmployeeController {

    @Autowired
//是 Spring 的注解，用于实现依赖注入。在这里，@Autowired 注解被用于自动装配 EmployeeService 对象。通过使用 @Autowired 注解，Spring 将会自动查找匹配类型的 Bean（在这里是 EmployeeService），并将其注入到该字段中，从而实现依赖注入。
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param request//request   HTTP请求对象
     * @param employee//employee 员工对象，通过请求体传递
     * @return
     */
    @PostMapping("/login")//用于将HTTP post请求映射到特定处理程序的方法
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {//前端传过来json形式的，所以需要加@RequestBody注解
        //上边创建HttpServletRequest request是因为登录成功之后，需要把员工对象的id存储到Session一份，表示登陆成功。这样如果想获取登录用户的话，随时就可以获取出来，通过request对象get一个session
        //1.将页面提交的密码进行加密处理，已经封装到employee中(springboot自动把获取的数据进行分装所以直接get就行)
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();//创建queryWrapper对象是用于构建查询条件
        queryWrapper.eq(Employee::getUsername, employee.getUsername());//查询条件：等值查询。根据用户名Username
        Employee emp = employeeService.getOne(queryWrapper);//使用getone方法是因为在数据库设计表时username，索引类型是Unique，也就是说不重复。所以可以使用getone

        //3.如果没有查询到则返回登陆失败结果
        if (emp == null) {
            return R.error("登陆失败...");
        }

        //4.若在数据库中查到username，再进行密码比对,如果没有查询到返回登陆失败结果
        if (!emp.getPassword().equals(password)) {
            return R.error("登陆失败...");
        }

        //5.查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }

        //6.若以上5步都没有成立，则说明账号正常，登陆成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);

    }

    /**
     * 员工退出
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //1.清理Session中保存的当前员工id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功~");
    }

    /**
     * 新增员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工，员工信息：{}", employee.toString());

        //设置初始密码，进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());

        Long empID = (Long) request.getSession().getAttribute("employee");
        //employee.setCreateUser(empID);
        //employee.setUpdateUser(empID);

        employeeService.save(employee);

        return R.success("新增员工成功！");
    }

    /**
     * 员工信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {},pageSize = {},name = {}", page, pageSize, name);
        //构造分页构造器
        Page<Employee> pageinfo = new Page<>(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件,name不为空
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);//eq反映到sql上是 = ； like反映到sql上是 like 姓名建议用like,name可能为空
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageinfo, queryWrapper);
        return R.success(pageinfo);
    }

    /**
     * 根据id修改员工的信息
     *
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {

        //Long empId = (Long) request.getSession().getAttribute("employee");//点击禁用前端传过来的id会产生精度损失
        //employee.setUpdateTime(LocalDateTime.now());
        //employee.setUpdateUser(empId);
        //获取线程id
        long id = Thread.currentThread().getId();
        log.info("update的线程id为：{}", id);
        employeeService.updateById(employee);

        return R.success("员工信息修改成功~");
    }

    /**
     * 根据id查询员工信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询员工信息。。。");
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("没查询到对应员工信息。。");
    }


    public R<List<Employee>> list(Employee employee){
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();

        return null;
    }

    /**
     * 查询所有的员工信息（返回员工所在部门的名称，使用 left join 实现，分页查询）
     * @return
     */
    @GetMapping("/query")
    public R<IPage<EmployeeDto>> query(int page, int pageSize, String name){
        IPage<EmployeeDto> pageinfo = employeeService.queryPage(page, pageSize, name);
        return R.success(pageinfo);
    }


}
