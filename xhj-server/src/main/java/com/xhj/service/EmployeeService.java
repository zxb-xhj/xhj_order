package com.xhj.service;

import com.xhj.dto.EmployeeDTO;
import com.xhj.dto.EmployeeLoginDTO;
import com.xhj.dto.EmployeePageQueryDTO;
import com.xhj.entity.Employee;
import com.xhj.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void saveEmployee(EmployeeDTO employeeDTO);

    PageResult queryForPage(EmployeePageQueryDTO employeePageQueryDTO);

    Employee queryById(Long id);

    void updateEmp(EmployeeDTO employeeDTO);

    void updareEmpSta(Integer status, Long id);

    void updatePassword(int id,String password);
}
