package com.aike.service;

import com.aike.mapper.ItemCatMapper;
import com.aike.pojo.ItemCat;
import com.aike.vo.EasyUI_Tree;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private ItemCatMapper itemCatMapper;

    @Override
    public String findItemCatNameById(Long itemCatId) {
        ItemCat itemCat = itemCatMapper.selectById(itemCatId);
        return itemCat.getName();
    }

    @Override
    public List<EasyUI_Tree> findItemCatByParentId(Long parentId) {
        List<ItemCat> itemCatList = findItemCatList(parentId);
        List<EasyUI_Tree> treeList = new ArrayList<>();
        itemCatList.stream().forEach(
                itemCat -> {
                    EasyUI_Tree tree = new EasyUI_Tree();
                    tree.setId(itemCat.getId()).setText(itemCat.getName()).setState(itemCat.getIsParent() ? "closed" : "open");
                    treeList.add(tree);
                }
        );
        return treeList;
    }

    public List<ItemCat> findItemCatList(Long parentId) {
        QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        return itemCatMapper.selectList(queryWrapper);
    }
}
