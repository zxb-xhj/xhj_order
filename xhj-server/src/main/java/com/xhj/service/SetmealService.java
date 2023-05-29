package com.xhj.service;

import com.xhj.dto.SetmealDTO;
import com.xhj.dto.SetmealPageQueryDTO;
import com.xhj.entity.Setmeal;
import com.xhj.result.PageResult;
import com.xhj.vo.DishItemVO;
import com.xhj.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    public void saveWithDish(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
    void deleteBatch(List<Long> ids);

    SetmealVO getByIdWithDish(Long id);

    /**
     * 修改套餐
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    void startOrStop(Integer status, Long id);
    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);

}
