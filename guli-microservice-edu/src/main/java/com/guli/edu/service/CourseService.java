package com.guli.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.edu.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.edu.entity.CourseInfoForm;
import com.guli.edu.entity.CourseQuery;
import com.guli.edu.vo.CoursePublishVo;
import com.guli.edu.vo.CourseWebVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Helen
 * @since 2019-04-15
 */
public interface CourseService extends IService<Course> {
    /**
     * 保存课程和课程详情信息
     * @param courseInfoForm
     * @return 新生成的课程id
     */
    String saveCourseInfo(CourseInfoForm courseInfoForm);

    CourseInfoForm getCourseInfoFormById(String id);

    //  更新课程信息
    void updateCourseInfoById(CourseInfoForm courseInfoForm);

    void pageQuery(Page<Course> coursePage, CourseQuery courseQuery);

    //获取课程信息
    void publishCourseById(String id);

    CoursePublishVo getCoursePublishVoById(String id);

    //根据讲师ID查询讲师所讲课程的列表
    List<Course> selectByTeacherId(String TeacherId);

    Map<String,Object> pageListWeb(Page<Course> pageParam);

    /**
     * 关联获取讲师和课程信息
     * @param id
     * @return
     */
    CourseWebVo selectCourseWebVoById(String id);
}
