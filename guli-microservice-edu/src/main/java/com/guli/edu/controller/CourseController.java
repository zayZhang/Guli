package com.guli.edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.vo.R;
import com.guli.edu.entity.Chapter;
import com.guli.edu.entity.Course;
import com.guli.edu.service.ChapterService;
import com.guli.edu.service.CourseService;
import com.guli.edu.vo.ChapterVo;
import com.guli.edu.vo.CourseWebVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2019-04-15
 */
@CrossOrigin //跨域
@Api(description="课程模块")
@RestController
@RequestMapping("/edu/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ChapterService chapterService;

    @ApiOperation(value = "根据ID查询课程")
    @GetMapping(value = "{courseId}")
    public R getTeacherCourseById( @ApiParam(name = "courseId", value = "课程ID", required = true)
                                   @PathVariable String courseId){

        CourseWebVo courseWebVo = courseService.selectCourseWebVoById(courseId);

        //包含视频的课程信息页面对象
        List<ChapterVo> list=chapterService.nestedList(courseId);

        return R.ok().data("course", courseWebVo).data("chapterVoList", list);
    }

    @ApiOperation(value = "分页课程列表")
    @GetMapping(value = "{page}/{limit}")
    public R pageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit){

        Page<Course> pageParam = new Page<Course>(page, limit);

        Map<String, Object> map = courseService.pageListWeb(pageParam);

        return  R.ok().data(map);
    }
}

