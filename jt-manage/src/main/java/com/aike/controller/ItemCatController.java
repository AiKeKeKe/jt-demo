package com.aike.controller;

import com.aike.anno.Cache_find;
import com.aike.enu.KEY_ENUM;
import com.aike.service.ItemCatService;
import com.aike.vo.EasyUI_Tree;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/item/cat")
public class ItemCatController {
    @Resource
    private ItemCatService itemCatService;

    @RequestMapping("/queryItemName")
    public String findItemCatNameById(Long itemCatId){
        return itemCatService.findItemCatNameById(itemCatId);
    }

    @RequestMapping(value = "/list")
    @Cache_find(keyType = KEY_ENUM.AUTO)
    public List<EasyUI_Tree> findItemCatByParentId(@RequestParam(name = "id", defaultValue = "0") Long parentId){
        return itemCatService.findItemCatByParentId(parentId);
    }

}
