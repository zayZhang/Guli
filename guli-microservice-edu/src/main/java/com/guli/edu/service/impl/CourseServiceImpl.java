package com.guli.edu.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.exception.GuliException;
import com.guli.edu.entity.Course;
import com.guli.edu.entity.CourseDescription;
import com.guli.edu.entity.CourseInfoForm;
import com.guli.edu.entity.CourseQuery;
import com.guli.edu.mapper.CourseDescriptionMapper;
import com.guli.edu.mapper.CourseMapper;
import com.guli.edu.service.CourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.edu.vo.CoursePublishVo;
import com.guli.edu.vo.CourseWebVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2019-04-15
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseDescriptionMapper courseDescriptionMapper;

    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {
        //保存课程基本信息
        Course course = new Course();
        course.setStatus(Course.COURSE_DRAFT);
        BeanUtils.copyProperties(courseInfoForm, course);
        baseMapper.insert(course);

        //保存课程详情信息
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());
        courseDescriptionMapper.insert(courseDescription);

        return course.getId();
    }

    /**
     * 获取刚添加的课程数据
     * @param id
     * @return
     */
    @Override
    public CourseInfoForm getCourseInfoFormById(String id) {

        //从course表中取数据
        Course course = baseMapper.selectById(id);
        if(course == null){
            throw new GuliException(20001, "数据不存在");
        }

        //从course_description表中取数据
        CourseDescription courseDescription = courseDescriptionMapper.selectById(id);
        if(courseDescription == null){
            throw new GuliException(20001, "数据不完整");
        }

        //创建courseInfoForm对象
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(course, courseInfoForm);
        BeanUtils.copyProperties(courseDescription, courseInfoForm);

        return courseInfoForm;
    }


    /**
     * 更新课程信息
     * @param courseInfoForm
     */
    @Transactional
    @Override
    public void updateCourseInfoById(CourseInfoForm courseInfoForm) {
        //保存课程基本信息
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm, course);
        baseMapper.updateById(course);

        //保存课程详情信息
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescriptionMapper.updateById(courseDescription);
    }

    @Override
    public void pageQuery(Page<Course> coursePage, CourseQuery courseQuery) {
        QueryWrapper<Course> query=new QueryWrapper<>();
        query.orderByDesc("gmt_create");
        if(courseQuery==null){
            //由于是使用的分页工具，这里查询了就不用返回了，查询了会直接装配到这个分页对象中的
            baseMapper.selectPage(coursePage,query);
            return;
        }

        String title=courseQuery.getTitle();
        String teacherId=courseQuery.getTeacherId();
        String subjectParentId=courseQuery.getSubjectParentId();
        String subjectId=courseQuery.getSubjectId();
        if(!StringUtils.isEmpty(title)){
            query.like("title",title);
        }
        if (!StringUtils.isEmpty(teacherId) ) {
            query.eq("teacher_id", teacherId);
        }

        if (!StringUtils.isEmpty(subjectParentId)) {
            query.ge("subject_parent_id", subjectParentId);
        }

        if (!StringUtils.isEmpty(subjectId)) {
            query.ge("subject_id", subjectId);
        }

        baseMapper.selectPage(coursePage, query);
    }

    @Override
    public void publishCourseById(String id) {
        Course course = new Course();
        course.setId(id);
        course.setStatus(Course.COURSE_NORMAL);
        baseMapper.updateById(course);
    }

    /**
     * 根据id，左外连接，获取页面课程信息
     * @param id
     * @return
     */
    @Override
    public CoursePublishVo getCoursePublishVoById(String id) {
        return baseMapper.selectCoursePublishVoById(id);
    }

    @Override
    public List<Course> selectByTeacherId(String TeacherId) {
        QueryWrapper<Course> query=new QueryWrapper<>();
        query.eq("teacher_id",TeacherId);
        query.orderByDesc("gmt_modified");
        List<Course> courses = baseMapper.selectList(query);
        return courses;

    }

    /**
     * 获取课程分页列表
     * @param pageParam
     * @return
     */
    @Override
    public Map<String, Object> pageListWeb(Page<Course> pageParam) {
        QueryWrapper<Course> query=new QueryWrapper<>();
        query.orderByDesc("gmt_modified");
        baseMapper.selectPage(pageParam,query);
        List<Course> list=pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", list);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }
    @Transactional
    @Override
    public CourseWebVo selectCourseWebVoById(String id) {
        Course course=baseMapper.selectById(id);
        //访问量+1
        course.setViewCount(course.getViewCount()+1);
        return  baseMapper.selectCourseWebVoById(id);
    }


}
