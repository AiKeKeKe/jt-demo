package com.aike.service;

import com.aike.vo.EasyUI_Tree;

import java.util.List;

public interface ItemCatService {
    String findItemCatNameById(Long itemCatId);

    List<EasyUI_Tree> findItemCatByParentId(Long parentId);
}
