package com.gocar.service.impl;

import com.gocar.dao.AdminMapper;
import com.gocar.dao.PlaceMapper;
import com.gocar.dto.JsonResult;
import com.gocar.dto.Page;
import com.gocar.enums.ResultEnum;
import com.gocar.enums.RoleEnum;
import com.gocar.pojo.*;
import com.gocar.service.AdminService;
import com.gocar.utils.FileUtil;
import com.gocar.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private PlaceMapper placeMapper;
    @Override
    public String checkUserPwd(Admin admin) {
        String password = MD5Util.getMD5(admin.getaPassword());
        Admin realAdmin = findByUsername(admin.getaUsername());
        if(realAdmin != null ){
            if(password.equals(realAdmin.getaPassword())){
                return "成功";
            }
            return "密码错误!";
        }
        return "没有此账号!";
    }

    @Override
    public boolean upDate(Admin admin) {
        AdminExample adminExample = new AdminExample();
        adminExample.createCriteria().andAUsernameEqualTo(admin.getaUsername());
        int i = adminMapper.updateByExampleSelective(admin, adminExample);
        return i > 0 ? true : false;
    }

    @Override
    public Admin findByUsername(String username) {
        AdminExample adminExample = new AdminExample();
        adminExample.createCriteria().andAUsernameEqualTo(username);
        List<Admin> admins = adminMapper.selectByExample(adminExample);
        if(admins != null && admins.size() > 0){
            Admin admin = admins.get(0);
            //pid不为空设置站点对象
            if(admin.getaPid() != null) {
                admin.setPlace(placeMapper.selectByPrimaryKey(admin.getaPid()));
            }
            return admin;
        }
        return null;
    }

    @Override
    public Page<Admin> findAllToPage(Integer page, Integer rows) {

        Page<Admin> aPage = new Page<>();
        List<Admin> lists = adminMapper.findToPage((page-1)*rows,rows);
        aPage.setRows(lists);
        aPage.setTotal(adminMapper.countByExample(new AdminExample()));
        return aPage;
    }

    @Override
    public JsonResult deleteById(Integer aid) {
        if (aid == null) return new JsonResult(false, ResultEnum.SYSTEM_ERROR);
        int i = adminMapper.deleteByPrimaryKey(aid);
        return i > 0 ? new JsonResult(true, ResultEnum.DELETE_SUCCESS)
                : new JsonResult(false, ResultEnum.DELETE_FAIL);
    }

    @Override
    public List<Place> loadPlace() {
        List<Place> places = placeMapper.selectByExample(new PlaceExample());
        return places;
    }

    @Override
    public JsonResult add(MultipartFile adminIcon, Admin admin, HttpServletRequest request) {
        if(!adminIcon.isEmpty()){
            String path = FileUtil.uploadImage(adminIcon, "adminIcon", request);
            if(path == null) return new JsonResult(false, ResultEnum.UPLOAD_TYPE_ERROR);
            admin.setaIcon(path);
        }
        if(admin.getaRole() == null) admin.setaRole(RoleEnum.MANAGER.getMassage());
        admin.setaPassword(MD5Util.getMD5(admin.getaPassword()));
        admin.setaCreateTime(new Date());
        admin.setaUpdateTime(new Date());
        try{
            int  row = adminMapper.insertSelective(admin);
            return row > 0 ? new JsonResult(true, ResultEnum.ADD_SUCCESS)
                    : new JsonResult(false, ResultEnum.ADD_FAIL);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonResult(false, ResultEnum.REPEAT_ERROR);
        }
    }

    @Override
    public JsonResult update(MultipartFile adminIcon, Admin admin, HttpServletRequest request) {
        try{
            if(!adminIcon.isEmpty()){
                //更新首先要先删除原来的文件
                if(admin.getaIcon() != null){
                    File file = new File(request.getServletContext().getRealPath("/" + admin.getaIcon()));
                    if(file != null) file.delete();
                }
                String path = FileUtil.uploadImage(adminIcon, "adminIcon", request);
                if(path == null) return new JsonResult(false, ResultEnum.UPLOAD_TYPE_ERROR);
                admin.setaIcon(path);
            }
            admin.setaUpdateTime(new Date());
            int i = adminMapper.updateByPrimaryKeySelective(admin);
            return i > 0 ? new JsonResult(false, ResultEnum.UPDATE_SUCCESS)
                    : new JsonResult(false, ResultEnum.UPDATE_FAIL);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonResult(false, ResultEnum.SYSTEM_ERROR);
        }
    }


}
