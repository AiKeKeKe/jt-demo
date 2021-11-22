package com.aike.service;

import com.aike.pojo.Item;
import com.aike.pojo.ItemDesc;
import com.aike.util.HttpClientService;
import com.aike.util.ObjectMapperUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WebItemServiceImpl implements WebItemService {

    @Resource
    private HttpClientService httpClient;

    @Override
    public Item findItemById(Long itemId) {
        String url = "http://manage.jt.com/web/item/findItemById/" + itemId;
        String itemJson = httpClient.doGet(url);
        return ObjectMapperUtil.toObject(itemJson, Item.class);
    }

    @Override
    public ItemDesc findItemDescById(Long itemId) {
        String url = "http://manage.jt.com/web/item/findItemDescById/" + itemId;
        String itemDescJSON = httpClient.doGet(url);
        return ObjectMapperUtil.toObject(itemDescJSON, ItemDesc.class);
    }
}
