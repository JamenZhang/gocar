package com.gocar.service.impl;

import com.gocar.dao.PlaceMapper;
import com.gocar.dto.JsonResult;
import com.gocar.enums.ResultEnum;
import com.gocar.pojo.Place;
import com.gocar.pojo.PlaceExample;
import com.gocar.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PlaceServiceImpl implements PlaceService {
    @Autowired
    private PlaceMapper placeMapper;

    @Override
    public List<Place> findAll() {
        return placeMapper.selectByExample(new PlaceExample());
    }

    @Override
    public JsonResult update(Place place) {
        place.setpUpdateTime(new Date());
        int i = placeMapper.updateByPrimaryKeySelective(place);
        return i > 0 ? new JsonResult(true, ResultEnum.UPDATE_SUCCESS)
                : new JsonResult(true, ResultEnum.UPDATE_FAIL);
    }

    @Override
    public JsonResult add(Place place) {
        place.setpCreateTime(new Date());
        place.setpUpdateTime(new Date());
        int i = placeMapper.insertSelective(place);
        return i > 0 ? new JsonResult(true, ResultEnum.ADD_SUCCESS)
                : new JsonResult(true, ResultEnum.ADD_FAIL);
    }

    @Override
    public JsonResult deleteById(Integer pid) {
        if(pid == null )return new JsonResult(false, ResultEnum.SYSTEM_ERROR);
        int i = placeMapper.deleteByPrimaryKey(pid);
        return i > 0 ? new JsonResult(true, ResultEnum.DELETE_SUCCESS)
                : new JsonResult(true, ResultEnum.DELETE_FAIL);
    }

    @Override
    public Place findById(Integer pid) {
        if(pid == null ) return null;
        return placeMapper.selectByPrimaryKey(pid);
    }
}
