package com.gocar.service;

import com.gocar.dto.JsonResult;
import com.gocar.dto.Page;
import com.gocar.pojo.Orders;


public interface OrdersService {

    JsonResult add(Orders orders);

    Page<Orders> findAllToPage(Integer page, Integer rows);

    Orders findById(Integer oid);

    //归还单车
    JsonResult update(Orders orders);

    JsonResult deleteById(Integer oid,Integer oBid,String oState);
}
