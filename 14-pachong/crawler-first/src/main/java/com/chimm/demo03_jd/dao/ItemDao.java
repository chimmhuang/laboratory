package com.chimm.demo03_jd.dao;

import com.chimm.demo03_jd.pojo.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author huangshuai
 * @date 2020/05/12
 */
@Repository
public interface ItemDao extends JpaRepository<Item, Long> {

}
