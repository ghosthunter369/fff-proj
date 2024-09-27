package com.xuecheng.content;

import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CourseCatoryTest {
    @Autowired
    private CourseCategoryMapper courseCategoryMapper;
    @Autowired
    private CourseCategoryService  courseCategoryService;
    @Test
    public void testSelectTreeNodes() {
        String id = "1";
        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryMapper.selectTreeNodes(id);
        System.out.println(courseCategoryTreeDtos);
    }
    @Test
    public void testCourseCategoryService() {
        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryService.queryTreeNodes("1");
        System.out.println( courseCategoryTreeDtos);
    }
}
