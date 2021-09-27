package com.aike.mapper;

import com.aike.pojo.Item;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
public interface ItemMapper extends BaseMapper<Item> {
    @Select("select * from tb_item order by updated desc limit #{start},#{rows}")
    List<Item> findItemByPage(@Param("start") Integer start,@Param("rows") Integer rows);
}
