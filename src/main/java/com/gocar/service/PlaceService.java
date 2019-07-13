package com.gocar.service;

import com.gocar.dto.JsonResult;
import com.gocar.pojo.Place;

import java.util.List;


public interface PlaceService {
    List<Place> findAll();

    JsonResult update(Place place);

    JsonResult add(Place place);

    JsonResult deleteById(Integer pid);

    Place findById(Integer pid);
}
