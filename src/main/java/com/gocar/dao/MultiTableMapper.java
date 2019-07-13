package com.gocar.dao;

import com.gocar.pojo.CarCategory;

import java.util.List;

/**多表View
 *
 */
public interface MultiTableMapper {

   List<CarCategory> getCarAndCategory();
   //...
}
