package com.xhj.service;

import com.xhj.dto.ShoppingCartDTO;
import com.xhj.entity.ShoppingCart;
import java.util.List;

public interface ShoppingCartService {

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void add(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> list();
}
