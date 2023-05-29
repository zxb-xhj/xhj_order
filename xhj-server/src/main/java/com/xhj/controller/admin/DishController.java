package com.xhj.controller.admin;

import com.xhj.dto.DishDTO;
import com.xhj.dto.DishPageQueryDTO;
import com.xhj.entity.Dish;
import com.xhj.result.PageResult;
import com.xhj.result.Result;
import com.xhj.service.DishService;
import com.xhj.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation("新增菜品")
    @CacheEvict(cacheNames = "categoryCache",key = "#dishDTO.categoryId")
    public Result save(@RequestBody DishDTO dishDTO) {
        dishService.saveDish(dishDTO);

        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询菜品")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        PageResult page = dishService.page(dishPageQueryDTO);

        return Result.success(page);
    }

    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("=============要删除的id信息：{}"+ids);
        dishService.deleteByIds(ids);

        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("回显菜品")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("获取的信息：{}"+id);
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }


    @PutMapping
    @ApiOperation("修改菜品")
    @CacheEvict(cacheNames = "categoryCache",key = "#dishDTO.categoryId")
    public Result update(@RequestBody DishDTO dishDTO) {
        dishService.update(dishDTO);
        return Result.success();
    }
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List> selectByCat(Long categoryId){
        List<Dish> dishes = dishService.selectByCat(categoryId);

        return Result.success(dishes);
    }

}
