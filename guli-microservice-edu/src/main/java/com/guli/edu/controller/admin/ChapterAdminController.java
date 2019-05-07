package com.guli.edu.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.vo.R;
import com.guli.edu.entity.Chapter;
import com.guli.edu.entity.Course;
import com.guli.edu.entity.CourseQuery;
import com.guli.edu.service.ChapterService;
import com.guli.edu.service.CourseService;
import com.guli.edu.vo.ChapterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "课程章节管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/edu/chapter")
public class ChapterAdminController {

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private CourseService courseService;



    @GetMapping("{id}")
    public R getChapter(@PathVariable String id){
        Chapter chapterServiceById = chapterService.getById(id);
        return R.ok().data("items",chapterServiceById);
    }

    /**
     * 保存章节对象
     * @param chapter
     * @return
     */
    @ApiOperation(value = "新增章节")
    @PostMapping
    public R save(@ApiParam(name="chapter",value="章节对象",required = true)
                  @RequestBody Chapter chapter){
        chapterService.save(chapter);

        return R.ok();
    }

    /**
     * 修改章节
     * @param id
     * @param chapter
     * @return
     */
    @ApiOperation(value = "根据ID修改章节")
    @PutMapping("{id}")
    public R updateById(
            @ApiParam(name = "id", value = "章节ID", required = true)
            @PathVariable String id,

            @ApiParam(name = "chapter", value = "章节对象", required = true)
            @RequestBody Chapter chapter){

        chapter.setId(id);
        chapterService.updateById(chapter);
        return R.ok();
    }
    @ApiOperation(value = "根据ID删除章节")
    @DeleteMapping("{id}")
    public R removeById(
            @ApiParam(name = "id", value = "章节ID", required = true)
            @PathVariable String id){

        chapterService.removeChapterById(id);
        return R.ok();
    }

    @ApiOperation(value="嵌套章节数据列表")
    @GetMapping("/nested-list/{courseId}")
    public R nestedListByCourseId(@ApiParam(name = "courseId", value = "课程ID", required = true)
                                      @PathVariable String courseId){
        List<ChapterVo> list=chapterService.nestedList(courseId);
        return R.ok().data("items",list);
    }

}
