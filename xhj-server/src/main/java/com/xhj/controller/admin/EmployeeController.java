package com.xhj.controller.admin;

import com.xhj.constant.JwtClaimsConstant;
import com.xhj.dto.EmployeeDTO;
import com.xhj.dto.EmployeeLoginDTO;
import com.xhj.dto.EmployeePageQueryDTO;
import com.xhj.entity.Employee;
import com.xhj.properties.JwtProperties;
import com.xhj.result.PageResult;
import com.xhj.result.Result;
import com.xhj.service.EmployeeService;
import com.xhj.utils.JwtUtil;
import com.xhj.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    @PostMapping
    @ApiOperation("新增员工")
    public Result addAmployee(@RequestBody EmployeeDTO employeeDTO){
        log.info("新增员工：{}",employeeDTO);
        employeeService.saveEmployee(employeeDTO);

        return Result.success();
    }


    @GetMapping("/page")
    @ApiOperation("分页查询所有员工信息")
    public Result selectForPage(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页参数:{}"+employeePageQueryDTO);
        PageResult pageResult = employeeService.queryForPage(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据员工id查询员工信息")
    public Result<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.queryById(id);
        System.out.println(employee);
        return Result.success(employee);
    }

    @PutMapping
    @ApiOperation("编辑员工信息")
    public Result updateEmployee(@RequestBody EmployeeDTO employeeDTO){
        log.info("编辑员工信息：{}",employeeDTO);
        employeeService.updateEmp(employeeDTO);
        return Result.success();
    }
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工账号")
    public Result updareEmpSta(@PathVariable Integer status,Long id){
        log.info("状态：{},员工id：{}",status==1?"启用":"禁用",id);
        employeeService.updareEmpSta(status,id);
        return Result.success();
    }
}
