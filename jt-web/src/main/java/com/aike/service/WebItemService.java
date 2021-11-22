package com.aike.service;

import com.aike.pojo.Item;
import com.aike.pojo.ItemDesc;

public interface WebItemService {

    Item findItemById(Long itemId);

    ItemDesc findItemDescById(Long itemId);
}
