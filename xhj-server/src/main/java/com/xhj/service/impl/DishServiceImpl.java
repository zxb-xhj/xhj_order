package com.xhj.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xhj.constant.StatusConstant;
import com.xhj.dto.DishDTO;
import com.xhj.dto.DishPageQueryDTO;
import com.xhj.entity.Dish;
import com.xhj.entity.DishFlavor;
import com.xhj.exception.DeletionNotAllowedException;
import com.xhj.mapper.DishFlavorMapper;
import com.xhj.mapper.DishMapper;
import com.xhj.mapper.SetmealDishMapper;
import com.xhj.result.PageResult;

import com.xhj.service.DishService;
import com.xhj.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    @Transactional
    public void saveDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.addDish(dish);

        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();

        if (flavors != null && flavors.size() > 0) {
            //flavors.stream().forEach()
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            //System.out.println("====================="+dishId+"=======================");
            dishFlavorMapper.addDishFla(flavors);
        }

    }


    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> page = dishMapper.page(dishPageQueryDTO);//后绪步骤实现

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        //判断当前菜品是否能够删除---是否存在起售中的菜品？？
        for (Long id : ids) {
            Dish dish = dishMapper.getStatusById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException("该商品属于热门商品，暂不能删除");
            }
        }

        //判断当前菜品是否能够删除---是否被套餐关联了？？根据id查询中间表是否有信息
        for (Long id : ids) {
            List<Long> setById = setmealDishMapper.getSetById(ids);
            System.out.println("======================getSetById========================" + setById.toString());
            if (!setById.isEmpty() || setById.size() > 0) {
                throw new DeletionNotAllowedException("该商品被套餐关联了，暂不能删除");
            }
        }

        //删除菜品表中的菜品数据

        for (Long id : ids) {
            dishMapper.deleteById(id);
            dishFlavorMapper.deleteById(id);
        }

    }

    @Override
    @Transactional
    public void update(DishDTO dishDTO) {
//修改菜品表基本信息
        System.out.println(dishDTO+"========================");
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.updateDish(dish);
// 删除原有的口味数据
        System.out.println(dishDTO+"================================================");
        dishFlavorMapper.deleteById(dishDTO.getId());
// 重新插入口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors!=null&&flavors.size()>0){
            flavors.forEach(dishFlavor->{
                dishFlavor.setDishId(dishDTO.getId());
                System.out.println(dishDTO.getId());
            });
        }
        System.out.println(flavors);
        dishFlavorMapper.addDishFla(flavors);
// 向口味表插入n条数据
    }

    @Override
    public List<Dish> selectByCat(Long categoryId) {
        List<Dish> dishes = dishMapper.selectByCat(categoryId);
        return dishes;
    }

    @Override
    public DishVO getById(Long id) {
        Dish byId = dishMapper.getById(id);

        List<DishFlavor> byDishId = dishFlavorMapper.getByDishId(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(byId, dishVO);
        dishVO.setFlavors(byDishId);
        return dishVO;
    }/**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
