package com.xhj.service;

import com.xhj.dto.DishDTO;
import com.xhj.dto.DishPageQueryDTO;
import com.xhj.entity.Dish;
import com.xhj.result.PageResult;
import com.xhj.vo.DishVO;

import java.util.List;

public interface DishService {

    void saveDish(DishDTO dishDTO);

    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    void deleteByIds(List<Long> id);

    DishVO  getById(Long id);

    void update(DishDTO dishDTO);

    List<Dish> selectByCat(Long categoryId);

    List<DishVO> listWithFlavor(Dish dish);
}
