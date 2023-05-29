package com.xhj.mapper;


import com.github.pagehelper.Page;
import com.xhj.annotation.AutoFill;
import com.xhj.dto.DishPageQueryDTO;
import com.xhj.entity.Dish;
import com.xhj.enumeration.OperationType;
import com.xhj.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @Insert("INSERT INTO dish (name, category_id, price, image, description, status, create_time, update_time, create_user, update_user)" +
            " VALUES " +
            "(#{name},#{categoryId},#{price},#{image},#{description},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser});")
        @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    @AutoFill(OperationType.INSERT)
    void addDish(Dish dish);

    Page<DishVO> page(DishPageQueryDTO dishPageQueryDTO);

    @Select("select * from dish where id=#{id}")
    Dish getStatusById(Long id);

    void deleteById(Long id);

    @Select("select * from dish where id=#{id}")
    Dish getById(Long id);

    @AutoFill(OperationType.UPDATE)
    void updateDish(Dish dish);

    @Select("select * from dish where category_id=#{categoryId}")
    List<Dish> selectByCat(Long categoryId);

    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long setmealId);

    List<Dish> list(Dish dish);


    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
