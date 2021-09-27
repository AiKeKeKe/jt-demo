package com.aike.service;

import com.aike.pojo.Item;
import com.aike.pojo.ItemDesc;
import com.aike.vo.EasyUI_Table;

public interface ItemService {

    EasyUI_Table findItemByPage(Integer page,Integer rows);

    void saveItem(Item item, ItemDesc itemDesc);

    void updateItem(Item item, ItemDesc itemDesc);

    void deleteItems(Long[] ids);

    void updateStatus(Long[] ids, int status);

    ItemDesc findItemDescById(Long itemId);
}
