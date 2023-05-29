package com.xhj.mapper;

import com.github.pagehelper.Page;
import com.xhj.annotation.AutoFill;
import com.xhj.dto.EmployeePageQueryDTO;
import com.xhj.entity.Employee;
import com.xhj.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    //       INSERT into employee(NAME,username,PASSWORD,phone,sex,id_number)VALUES('21312','32321','32131','3213213','男','213213')
    @AutoFill(OperationType.INSERT)
    @Insert("insert into employee(name,username,password,phone,sex,id_number,create_time,update_time,create_user,update_user,status)values(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser},#{status})")
    void addEmployee(Employee employee);

    @Insert("select * from employee")
    Employee selectAll(Employee employee);

    Page<Employee> queryForPage(EmployeePageQueryDTO employeePageQueryDTO);

    @Select("select * from employee where id=#{id}")
    Employee selectById(Long id);

    @AutoFill(OperationType.UPDATE)
    void updateEmp(Employee employee);

    @Update("update employee set password = #{password} where id=#{id}")
    void updatePassword(@Param("id") int id,@Param("password") String password);
}
