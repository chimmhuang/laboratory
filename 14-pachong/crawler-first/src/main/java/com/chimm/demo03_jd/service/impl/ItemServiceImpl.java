package com.chimm.demo03_jd.service.impl;

import com.chimm.demo03_jd.dao.ItemDao;
import com.chimm.demo03_jd.pojo.Item;
import com.chimm.demo03_jd.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author huangshuai
 * @date 2020/05/12
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemDao itemDao;

    @Override
    public List<Item> findAll(Item item) {
        Example example = Example.of(item);
        List list = this.itemDao.findAll(example);
        return list;
    }

    @Override
    @Transactional
    public void save(Item item) {
        this.itemDao.save(item);
    }
}
