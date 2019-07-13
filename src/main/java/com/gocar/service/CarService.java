package com.gocar.service;

import com.gocar.dto.JsonResult;
import com.gocar.dto.Page;
import com.gocar.pojo.Car;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


public interface CarService {



    JsonResult add(MultipartFile carIcon, Car car, HttpServletRequest request,Integer bCount);

    JsonResult update(MultipartFile carIcon,Car car,HttpServletRequest request);

    Page<Car> findAllToPage(Integer page, Integer rows);

    //当前Car d掉 和 更新该分类的数量
    JsonResult deleteById(String bids,String cids);

    Car findById(Integer bid);
}
