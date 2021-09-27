package com.aike.service;

import com.aike.mapper.ItemDescMapper;
import com.aike.mapper.ItemMapper;
import com.aike.pojo.Item;
import com.aike.pojo.ItemDesc;
import com.aike.vo.EasyUI_Table;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemDescMapper itemDescMapper;

    @Override
    public EasyUI_Table findItemByPage(Integer page, Integer rows) {
        int total = itemMapper.selectCount(null);
        int start = (page - 1) * rows;
        List<Item> itemList = itemMapper.findItemByPage(start, rows);
        return new EasyUI_Table(total, itemList);
    }

    @Transactional
    @Override
    public void saveItem(Item item, ItemDesc itemDesc) {
        item.setStatus(1).setCreated(new Date()).setUpdated(item.getCreated());
        itemMapper.insert(item);
        itemDesc.setItemId(item.getId()).setCreated(item.getCreated()).setUpdated(item.getUpdated());
        itemDescMapper.insert(itemDesc);
    }

    @Transactional
    @Override
    public void updateItem(Item item, ItemDesc itemDesc) {
        item.setUpdated(new Date());
        itemMapper.updateById(item);
        itemDesc.setItemId(item.getId()).setUpdated(item.getUpdated());
        itemDescMapper.updateById(itemDesc);
    }

    @Override
    public void deleteItems(Long[] ids) {
        itemMapper.deleteBatchIds(Arrays.asList(ids));
        itemDescMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public void updateStatus(Long[] ids, int status) {
        Item item = new Item();
        item.setStatus(status).setUpdated(new Date());
        UpdateWrapper<Item> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", Arrays.asList(ids));
        itemMapper.update(item,updateWrapper);
    }

    @Override
    public ItemDesc findItemDescById(Long itemId) {
        return itemDescMapper.selectById(itemId);
    }
}
