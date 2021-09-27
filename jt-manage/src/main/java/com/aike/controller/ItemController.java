package com.aike.controller;

import com.aike.pojo.Item;
import com.aike.pojo.ItemDesc;
import com.aike.service.ItemService;
import com.aike.vo.EasyUI_Table;
import com.aike.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
public class ItemController {
    /**
     * 商品下架
     */
    private static final int OFF_SHELF = 1;
    /**
     * 商品上架
     */
    private static final int ON_SHELF = 2;

    @Autowired
    private ItemService itemService;

    @RequestMapping("/query")
    public EasyUI_Table findItemByPage(Integer page, Integer rows){
        return itemService.findItemByPage(page,rows);
    }

    @RequestMapping("/query/item/desc/{itemId}")
    public SysResult findItemDescById(@PathVariable Long itemId){
        ItemDesc itemDesc = itemService.findItemDescById(itemId);
        return SysResult.success(itemDesc);
    }

    @RequestMapping("/save")
    public SysResult saveItem(Item item, ItemDesc itemDesc) {
        itemService.saveItem(item, itemDesc);
        return SysResult.success();
    }

    @RequestMapping("/update")
    public SysResult updateItem(Item item, ItemDesc itemDesc) {
        itemService.updateItem(item, itemDesc);
        return SysResult.success();
    }

    @RequestMapping("/delete")
    public SysResult deleteItems(Long[] ids) {
        itemService.deleteItems(ids);
        return SysResult.success();
    }

    @RequestMapping("/instock")
    public SysResult itemInstock(Long[] ids) {
        itemService.updateStatus(ids, ON_SHELF);
        return SysResult.success();
    }

    @RequestMapping("/reshelf")
    public SysResult itemReshelf(Long[] ids) {
        itemService.updateStatus(ids, OFF_SHELF);
        return SysResult.success();
    }
}
