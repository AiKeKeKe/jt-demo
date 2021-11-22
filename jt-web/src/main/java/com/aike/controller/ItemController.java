package com.aike.controller;

import com.aike.pojo.Item;
import com.aike.pojo.ItemDesc;
import com.aike.service.WebItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/items")
public class ItemController {
    @Resource
    private WebItemService webItemService;

    /**
     * 从后台manage中获取数据,之后页面展现.
     * @param itemId
     * @param model
     * @return
     */
    @RequestMapping("/{itemId}")
    public String findItemById(@PathVariable Long itemId, Model model) {
        Item item = webItemService.findItemById(itemId);
        ItemDesc itemDesc = webItemService.findItemDescById(itemId);
        model.addAttribute("item", item);
        model.addAttribute("itemDesc", itemDesc);
        return "item";
    }

}
