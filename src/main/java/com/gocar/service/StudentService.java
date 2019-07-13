package com.gocar.service;

import com.gocar.dto.JsonResult;
import com.gocar.dto.Page;
import com.gocar.pojo.Student;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


public interface StudentService {

    Student findBySno(String sno);

    Page findAllToPage(Integer page, Integer rows);

    JsonResult removeById(Integer sid);

    Student findById(Integer sid);

    JsonResult add(MultipartFile studentIcon, Student student, HttpServletRequest request);

    JsonResult update(MultipartFile multipartFile,Student student,HttpServletRequest request);
}
