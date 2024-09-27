package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseBaseInfoService;
import com.xuecheng.content.service.CourseCategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CourseCategoryController {
    @Autowired
    private CourseCategoryService courseCategoryService;
    @Autowired
    private CourseBaseInfoService courseBaseInfoService;
    @RequestMapping("course-category/tree-nodes")
    public List<CourseCategoryTreeDto> queryTreeNodes() {
        List<CourseCategoryTreeDto> list = courseCategoryService.queryTreeNodes("1");
        return list;
    }

    @ApiOperation("新增课程基础信息")
    @RequestMapping("course")
    public CourseBaseInfoDto createCourseBase(@RequestBody AddCourseDto addCourseDto) {
        Long id = 1234L;
        CourseBaseInfoDto courseBase = courseBaseInfoService.createCourseBase(id, addCourseDto);
        return courseBase;
    }
}
