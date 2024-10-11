package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.*;
import com.xuecheng.content.model.po.CourseBase;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @description 课程基本信息管理业务接口
 * @author Mr.M
 * @date 2022/9/6 21:42
 * @version 1.0
 */
public interface TeachplanService {

 /**
  * @param courseId 课程id
  * @return List<TeachplanDto>
  * @description 查询课程计划树型结构
  * @author Mr.M
  * @date 2022/9/9 11:13
  */
 public List<TeachplanDto> findTeachplanTree(long courseId);

 public void saveTeachplan(@RequestBody SaveTeachplanDto teachplan);
}