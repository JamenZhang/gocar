package com.gocar.service.impl;

import com.gocar.dao.OrdersMapper;
import com.gocar.dao.StudentMapper;
import com.gocar.dao.MultiTableMapper;
import com.gocar.dto.Total;
import com.gocar.enums.CarStateEnum;
import com.gocar.enums.OrdersStateEnum;
import com.gocar.pojo.*;
import com.gocar.service.TotalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TotalServiceImpl implements TotalService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private MultiTableMapper multiTableMapper;


    @Override
    public Total getTotalBean() {
        Total total = new Total();
        int inStoreCarCount = 0;
        int beakdownCarCount = 0;
        int repairCarCount = 0;
        float beakdownMoney = 0;

        List<CarCategory> CarCategory = multiTableMapper.getCarAndCategory();
        for (CarCategory bc : CarCategory) {
            if(!CarStateEnum.BORROWED.getState().equals(bc.getbState()))
                inStoreCarCount ++;
            if(CarStateEnum.BREAKDOWN.getState().equals(bc.getbState())) {
                beakdownCarCount++;
                beakdownMoney += bc.getcPrice();
            }
            if(CarStateEnum.REPAIR.getState().equals(bc.getbState()))
                repairCarCount ++;
        }

        total.setTotalCarCount(CarCategory.size());
        total.setInStoreCarCount(inStoreCarCount);
        total.setBeakdownCarCount(beakdownCarCount);
        total.setRepairCarCount(repairCarCount);
        total.setBeakdownMoney(beakdownMoney);

        int rentCount = 0;
        float haveCash = 0;
        float profitMoney = 0;
        List<Orders> orders = ordersMapper.selectByExample(new OrdersExample());
        for (Orders order : orders) {
            if(OrdersStateEnum.NOT_RETURN.getState().equals(order.getoState())) {
                rentCount++;
                haveCash += order.getoCash();
            }
            if(!OrdersStateEnum.NOT_RETURN.getState().equals(order.getoState())){
                profitMoney += order.getoRealRent() + order.getoCash();
            }
        }

        total.setBorrowedCount(orders.size());
        //租金流水
        total.setRentCount(rentCount);
        total.setHaveCash(haveCash);
        total.setProfitMoney(profitMoney - beakdownMoney);

        total.setTotalStudentCount(studentMapper.countByExample(new StudentExample()));


        return total;
    }
}
