package com.aike.controller.web;

import com.aike.anno.Cache_find;
import com.aike.enu.KEY_ENUM;
import com.aike.pojo.Item;
import com.aike.pojo.ItemDesc;
import com.aike.service.ItemService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/web/item")
public class WebItemController {
    @Resource
    private ItemService itemService;

    //查询商品信息
    @RequestMapping("/findItemById/{itemId}")
    @Cache_find(keyType = KEY_ENUM.AUTO)
    public Item findItemById(@PathVariable Long itemId){
        return itemService.findItemById(itemId);
    }

    //查询商品详情信息
    @RequestMapping("/findItemDescById/{itemId}")
    @Cache_find(keyType = KEY_ENUM.AUTO)
    public ItemDesc findItemDescById(@PathVariable Long itemId) {
        return itemService.findItemDescById(itemId);
    }
}
