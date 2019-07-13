package com.gocar.service.impl;

import com.gocar.dao.CarMapper;
import com.gocar.dao.CategoryMapper;
import com.gocar.dao.MultiTableMapper;
import com.gocar.dao.OrdersMapper;
import com.gocar.dto.JsonResult;
import com.gocar.dto.Page;
import com.gocar.enums.CarStateEnum;
import com.gocar.enums.OrdersStateEnum;
import com.gocar.enums.ResultEnum;
import com.gocar.pojo.*;
import com.gocar.service.OrdersService;
import com.gocar.service.StudentService;
import com.gocar.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private StudentService studentService;
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CarMapper CarMapper;

    @Autowired
    private MultiTableMapper multiTableMapper;


    @Transactional
    @Override
    public JsonResult add(Orders orders) {
        try{
            if(studentService.findBySno(orders.getoSno()) == null)
                return new JsonResult(false, ResultEnum.NOT_FOUND_SNO);

            Car Car = CarMapper.selectByPrimaryKey(orders.getoBid());
            if(Car == null)
                return new JsonResult(false, ResultEnum.NOT_FOUND_Car);
            //判断是否一个学生下了第二个订单且第一个订单没归还
            OrdersExample ordersExample = new OrdersExample();
            ordersExample.createCriteria().andOSnoEqualTo(orders.getoSno())
                    .andOStateEqualTo(OrdersStateEnum.NOT_RETURN.getState());
            List<Orders> ordersList = ordersMapper.selectByExample(ordersExample);
            if(ordersList != null && ordersList.size() > 0)
                return new JsonResult(false,ResultEnum.REPEAT_ERROR);

            if(CarStateEnum.BORROWED.getState().equals(Car.getbState()))
                return new JsonResult(false,ResultEnum.Car_BORROWED);
            if(CarStateEnum.REPAIR.getState().equals(Car.getbState()))
                return new JsonResult(false,ResultEnum.Car_REPAIR);
            if(CarStateEnum.BREAKDOWN.getState().equals(Car.getbState()))
                return new JsonResult(false,ResultEnum.Car_BREAKDOWN);

            Car.setbState(CarStateEnum.BORROWED.getState());
            Car.setbUpdateTime(new Date());
            CarMapper.updateByPrimaryKey(Car);
            Category category = categoryMapper.selectByPrimaryKey(Car.getbCid());
            orders.setoCash(category.getcCash());
            orders.setoBorrowTime(DateUtil.yMdHmsToStr(new Date()));
            orders.setoState(OrdersStateEnum.NOT_RETURN.getState());
            orders.setoCreateTime(new Date());
            orders.setoUpdateTime(new Date());
            int i = ordersMapper.insertSelective(orders);
            return i > 0 ? new JsonResult(true, ResultEnum.ADD_SUCCESS)
                    : new JsonResult(false, ResultEnum.ADD_FAIL);
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new JsonResult(false,ResultEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public Page<Orders> findAllToPage(Integer page, Integer rows) {
        Page<Orders> oPage = new Page<>();
        List<Orders> lists = ordersMapper.findToPage((page-1)*rows,rows);
        oPage.setRows(lists);
        oPage.setTotal(ordersMapper.countByExample(new OrdersExample()));
        return oPage;
    }

    @Override
    public Orders findById(Integer oid) {
        if (oid == null) return null;
        return ordersMapper.selectByPrimaryKey(oid);
    }

    //归还单车
    @Transactional
    @Override
    public JsonResult update(Orders orders) {
        //计算 归还时间和借用时间的时间差 * 每小时价钱 = realRent
        try{
            double hour = 0;
            try {
                System.out.println(orders.getoBorrowTime());
                Date before = DateUtil.yMdHmsToDate(orders.getoBorrowTime());
                hour = Math.ceil(DateUtil.differHour(before,new Date() ));
            } catch (ParseException e) {
                e.printStackTrace();
                return new JsonResult(false,ResultEnum.SYSTEM_ERROR);
            }
            float realRent = 0;
            List<CarCategory> CarCategory = multiTableMapper.getCarAndCategory();
            for (CarCategory bc : CarCategory) {
                if(bc.getBid().intValue() == orders.getoBid().intValue() ) {
                    realRent = Double.valueOf(hour * bc.getcRent()).floatValue();
                    break;
                }
            }
            //设置单车状态
            Car Car = new Car();
            //归还单车完好 则退回押金
            if(OrdersStateEnum.RETURN.getState().equals(orders.getoState())){
                orders.setoRealRent(realRent);
                orders.setoCash(0f);
                Car.setbState(CarStateEnum.AVAILABLE.getState());
            }else
                //归还单车需要维修，扣除一半押金
                if(OrdersStateEnum.RETURN_REPAIR.getState().equals(orders.getoState())){
                    orders.setoRealRent(realRent);
                    orders.setoCash(orders.getoCash()/2);
                    Car.setbState(CarStateEnum.REPAIR.getState());
                }else
                    //归还单车报废，扣除全部押金
                    if(OrdersStateEnum.RETURN_BREAKDOWN.getState().equals(orders.getoState())){
                        orders.setoRealRent(realRent);
                        Car.setbState(CarStateEnum.BREAKDOWN.getState());
                    }else{
                        return new JsonResult(false,ResultEnum.UPDATE_FAIL);
                    }
            Car.setBid(orders.getoBid());
            Car.setbUpdateTime(new Date());
            CarMapper.updateByPrimaryKeySelective(Car);

            //设置订单归还时间
            orders.setoReturnTime(DateUtil.yMdHmsToStr(new Date()));
            orders.setoUpdateTime(new Date());


            int i = ordersMapper.updateByPrimaryKey(orders);
            return i > 0 ?new JsonResult(true, ResultEnum.UPDATE_SUCCESS)
                    : new JsonResult(false,ResultEnum.UPDATE_FAIL);
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new JsonResult(false,ResultEnum.SYSTEM_ERROR);
        }

    }

    @Transactional
    @Override
    public JsonResult deleteById(Integer oid,Integer oBid,String oState) {
        try{
            if(oid == null || oBid == null)
                return new JsonResult(false,ResultEnum.SYSTEM_ERROR);
            if(OrdersStateEnum.NOT_RETURN.getState().equals(oState)){
                Car Car = CarMapper.selectByPrimaryKey(oBid);
                if(Car != null) {
                    Car.setbState(CarStateEnum.AVAILABLE.getState());
                    Car.setbUpdateTime(new Date());
                    CarMapper.updateByPrimaryKeySelective(Car);
                }
            }
            int i = ordersMapper.deleteByPrimaryKey(oid);
            return i > 0 ?new JsonResult(true, ResultEnum.DELETE_SUCCESS)
                    : new JsonResult(false,ResultEnum.DELETE_FAIL);

        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new JsonResult(false,ResultEnum.SYSTEM_ERROR);
        }

    }


}
