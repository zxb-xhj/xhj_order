package com.xhj.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xhj.constant.MessageConstant;
import com.xhj.constant.PasswordConstant;
import com.xhj.constant.StatusConstant;
import com.xhj.dto.EmployeeDTO;
import com.xhj.dto.EmployeeLoginDTO;
import com.xhj.dto.EmployeePageQueryDTO;
import com.xhj.entity.Employee;
import com.xhj.exception.AccountLockedException;
import com.xhj.exception.AccountNotFoundException;
import com.xhj.exception.PasswordErrorException;
import com.xhj.mapper.EmployeeMapper;
import com.xhj.result.PageResult;
import com.xhj.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(password,employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    public void saveEmployee(EmployeeDTO employeeDTO) {
        Employee emp = new Employee();

        BeanUtils.copyProperties(employeeDTO,emp);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        emp.setPassword(passwordEncoder.encode(PasswordConstant.DEFAULT_PASSWORD));
        emp.setStatus(StatusConstant.ENABLE);

        employeeMapper.addEmployee(emp);
    }

    @Override
    public PageResult queryForPage(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        Page<Employee> employees = employeeMapper.queryForPage(employeePageQueryDTO);
        long total = employees.getTotal();
        List<Employee> result = employees.getResult();
        return new PageResult(total,result);
    }


    public Employee queryById(Long id) {
        Employee employee = employeeMapper.selectById(id);
        return employee;
    }

    @Override
    public void updateEmp(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        /*employee.setUpdateUser(BaseContext.getCurrentId());
        employee.setUpdateTime(LocalDateTime.now());*/
        employeeMapper.updateEmp(employee);
    }

    @Override
    public void updareEmpSta(Integer status, Long id) {
        Employee employee = Employee.builder().status(status).id(id).build();
        employeeMapper.updateEmp(employee);
    }

    public void updatePassword(int id,String password){
        employeeMapper.updatePassword(id,password);
    }

}
