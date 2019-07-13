package com.gocar.service.impl;

import com.gocar.dao.CarMapper;
import com.gocar.dto.JsonResult;
import com.gocar.dto.Page;
import com.gocar.enums.CarStateEnum;
import com.gocar.enums.ResultEnum;
import com.gocar.pojo.Car;
import com.gocar.pojo.CarExample;
import com.gocar.pojo.Car;
import com.gocar.service.CarService;
import com.gocar.service.CategoryService;
import com.gocar.utils.ArithmeticUtil;
import com.gocar.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

@Service
public class CarControllerImpl implements CarService {
    @Autowired
    private CarMapper CarMapper;

    @Autowired
    private CategoryService categoryService;

    //事务控制 该单车分类剩余量+1
    @Transactional
    @Override
    public JsonResult add(MultipartFile carIcon, Car car, HttpServletRequest request, Integer bCount) {
        if(!carIcon.isEmpty()){
            String path = FileUtil.uploadImage(carIcon, "carIcon", request);
            if(path == null) return new JsonResult(false, ResultEnum.UPLOAD_TYPE_ERROR);
            car.setbIcon(path);
        }
        car.setbState(CarStateEnum.AVAILABLE.getState());
        car.setbCreateTime(new Date());
        car.setbUpdateTime(new Date());
        try{
            int row = 0;
            for (int i = 0; i < bCount; i++) {
                row += CarMapper.insertSelective(car);
            }
            if(row == bCount){
                return categoryService.updateRemainById(car.getbCid(),bCount) ?
                        new JsonResult(true, ResultEnum.ADD_SUCCESS):new JsonResult(false, ResultEnum.ADD_FAIL);
            }else{
                throw new RuntimeException();
            }
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new JsonResult(false,ResultEnum.SYSTEM_ERROR);
        }

    }

    @Override
    public JsonResult update(MultipartFile carIcon, Car car, HttpServletRequest request) {
        try{
            if(!carIcon.isEmpty()){
                //更新首先要先删除原来的文件
                if(car.getbIcon() != null){
                    File file = new File(request.getServletContext().getRealPath("/" + car.getbIcon()));
                    if(file != null) file.delete();
                }
                String path = FileUtil.uploadImage(carIcon, "carIcon", request);
                if(path == null) return new JsonResult(false, ResultEnum.UPLOAD_TYPE_ERROR);
                car.setbIcon(path);
            }
            car.setbUpdateTime(new Date());
            int i = CarMapper.updateByPrimaryKeySelective(car);
            return i > 0 ? new JsonResult(false, ResultEnum.UPDATE_SUCCESS)
                    : new JsonResult(false, ResultEnum.UPDATE_FAIL);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonResult(false, ResultEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public Page<Car> findAllToPage(Integer page, Integer rows) {
        Page<Car> bPage = new Page<>();
        List<Car> lists = CarMapper.findToPage((page-1)*rows,rows);
        //存入rows：返回的数据
        bPage.setRows(lists);
        //存入total：数据总数
        bPage.setTotal(CarMapper.countByExample(new CarExample()));
        //返回的格式为：{"total":18,"rows":[{"bid":352,"bName":"asd","bIcon":"images/carIcon/15585257235700464.jpg"}]}
        return bPage;

    }
    //事务控制 该单车的分类剩余量减size
    @Transactional
    @Override
    public JsonResult deleteById(String bids,String cids) {
        try{
            String[] bidList = bids.split(",");
            String[] cidList = cids.split(",");

            int row = 0;
            for (int i = 0; i < bidList.length; i++) {
                row += CarMapper.deleteByPrimaryKey(Integer.parseInt(bidList[i]));
            }
            if(row == bidList.length){ //更新分类表的剩余数量
                HashMap<String, Integer> cidMap = ArithmeticUtil.getElementForTimes(cidList);
                Set<Map.Entry<String, Integer>> entries = cidMap.entrySet();
                for (Map.Entry<String, Integer> entry : entries) {
                    boolean isSuccess = categoryService.updateRemainById(Integer.parseInt(entry.getKey()), - entry.getValue());
                    if(!isSuccess) throw new RuntimeException();
                }
                return new JsonResult(true, ResultEnum.DELETE_SUCCESS);
            }else{
                throw new RuntimeException();
            }
        }catch (Exception e){
            e.printStackTrace();
//            throw new RuntimeException(e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new JsonResult(false,ResultEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public Car findById(Integer bid) {
        return CarMapper.selectByPrimaryKey(bid);
    }



}
