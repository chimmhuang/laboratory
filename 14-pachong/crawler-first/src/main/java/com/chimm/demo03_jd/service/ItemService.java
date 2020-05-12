package com.chimm.demo03_jd.service;

import com.chimm.demo03_jd.pojo.Item;

import java.util.List;

/**
 * @author huangshuai
 * @date 2020/05/12
 */
public interface ItemService {

    //根据条件查询数据
    public List<Item> findAll(Item item);

    //保存数据
    public void save(Item item);
}
