package com.gocar.service;

import com.gocar.dto.JsonResult;
import com.gocar.pojo.Category;

import java.util.List;


public interface CategoryService {

    List<Category> findAll();

    //更新单车的剩余数量数量
    boolean updateRemainById(Integer cid,Integer count);

    JsonResult add(Category category);

    JsonResult update(Category category);

    JsonResult deleteById(Integer cid);

    Category findById(Integer cid);
}
