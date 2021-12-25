package com.aike.dao;

import com.aike.pojo.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = "select * from tb_item order by updated desc limit :start , :rows", nativeQuery = true)
    List<Item> findItemByPage(Integer start, Integer rows);
}
