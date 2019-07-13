package com.gocar.web;

import com.gocar.dto.JsonResult;
import com.gocar.dto.Page;
import com.gocar.pojo.Car;
import com.gocar.pojo.Category;
import com.gocar.service.CarService;
import com.gocar.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Controller
@RequestMapping("/car")
public class CarController {

    @Autowired
    private CarService CarService;

    @Autowired
    private CategoryService categoryService;



    //跳转到 Car(jsp)管理页面
    @RequestMapping("/carManage")
    public String CarManage(){
        return "car";
    }

    //添加或更新Car
    @RequestMapping(value = "/addOrUpdate",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addOrUpdate(MultipartFile carIcon, Car car, HttpServletRequest request,Integer bCount){
        if(bCount != null) return CarService.add(carIcon,car,request,bCount);
            return CarService.update(carIcon,car,request);
    }

    //showAll Car
    @RequestMapping("/showAll")
    @ResponseBody
    public Page<Car> show(Integer page, Integer rows){
        //返回jason数据
        return CarService.findAllToPage(page,rows);
    }

    //删除单车和更新单车的分类
    @RequestMapping(value = "/remove",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult remove(String bids,String cids){
        return CarService.deleteById(bids,cids);
    }
    //点击修改回显Car弹出表单
    @RequestMapping("/loadForm")
    @ResponseBody
    public Car loadForm(Integer bid){
        return CarService.findById(bid);
    }

    //回显Car分类
    @RequestMapping(value = "/loadCategory",method = RequestMethod.POST)
    @ResponseBody
    public List<Category> loadCategory(){
        return categoryService.findAll();
    }



}
