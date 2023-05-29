package com.xhj.service.impl;


import com.xhj.context.BaseContext;
import com.xhj.dto.ShoppingCartDTO;
import com.xhj.entity.Dish;
import com.xhj.entity.Setmeal;
import com.xhj.entity.ShoppingCart;
import com.xhj.mapper.DishMapper;
import com.xhj.mapper.SetmealMapper;
import com.xhj.mapper.ShoppingCartMapper;
import com.xhj.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //只能查询自己的购物车数据
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //判断当前商品是否在购物车中
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        //如果已经存在，就更新数量，数量加1
        if (list!=null && list.size()==1){
            shoppingCart= list.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber()+1);
            shoppingCartMapper.updateNumberById(shoppingCart);
        }
        //如果不存在，插入数据，数量就是1

        //判断当前添加到购物车的是菜品还是套餐
        Long dishId = shoppingCartDTO.getDishId();
        if (dishId!=null){
            //添加到购物车的是菜品
            Dish byId = dishMapper.getById(dishId);
            shoppingCart.setImage(byId.getImage());
            shoppingCart.setAmount(byId.getPrice());
            shoppingCart.setName(byId.getName());
        }else {
            Long setmealId = shoppingCartDTO.getSetmealId();
            Setmeal byId = setmealMapper.getById(setmealId);
            shoppingCart.setImage(byId.getImage());
            shoppingCart.setAmount(byId.getPrice());
            shoppingCart.setName(byId.getName());
        }
        //添加到购物车的是套餐
        shoppingCart.setNumber(1);
        shoppingCart.setCreateTime(LocalDateTime.now());
        System.out.println(shoppingCart+"============================");
        shoppingCartMapper.insert(shoppingCart);
    }

    @Override
    public List<ShoppingCart> list() {
        return shoppingCartMapper.list(ShoppingCart.builder().userId(BaseContext.getCurrentId()).build());
    }
}
