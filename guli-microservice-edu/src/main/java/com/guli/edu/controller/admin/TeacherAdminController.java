package com.guli.edu.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.common.vo.R;
import com.guli.edu.entity.Teacher;
import com.guli.edu.query.TeacherQuery;
import com.guli.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "admin handler类")
@CrossOrigin // 处理跨域问题
@RestController
@RequestMapping("/admin/edu/teacher")
public class TeacherAdminController {

    @Autowired
    private TeacherService  teacherService;

    @ApiOperation(value = "获取全部teacher")
   @GetMapping
    public R list(){

        List<Teacher> list = teacherService.list(null);
        return   R.ok().data("items",list);
    }

    @ApiOperation(value ="根据教师ID删除" )
    @DeleteMapping("{id}")
    public R remove(
            @ApiParam(name = "id",value ="教师ID",required = true)
            @PathVariable String id){
        boolean result = teacherService.removeById(id);

        if(result){
            return R.ok();
        }
        return R.error().message("删除失败");
    }

    @ApiOperation(value = "分页方法")
    @GetMapping("{page}/{limit}")
    public R page(
            @ApiParam(name = "page",value = "当前页码",required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit",value = "每页显示数量",required = true)
            @PathVariable Long limit,

            @ApiParam(name = "teacherQuery",value = "查询对象",required = false)
             TeacherQuery teacherQuery){
        if(page <= 0 || limit <= 0){
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }


        Page<Teacher> teacherPage = new Page<>(page, limit);

        teacherService.pageQuery(teacherPage,teacherQuery);

        List<Teacher> records = teacherPage.getRecords(); //每页的数量
        long total = teacherPage.getTotal(); // 查询的总记录数
        return R.ok().data("total",total).data("rows",records);

    }

    @ApiOperation(value = "添加方法")
    @PostMapping
    public R save(
            @ApiParam(name = "teacher",value = "教师对象")
            @RequestBody Teacher teacher){

        teacherService.save(teacher);
        return R.ok().message("成功");

    }

    @GetMapping("{id}")
    @ApiOperation(value = "根据id查询讲师信息")
    public R selectById(
                @ApiParam(name = "id",value = "讲师id",required = true)
                @PathVariable String id){
            Teacher teacher = teacherService.getById(id);
        return R.ok().data("item",teacher);

    }



    @ApiOperation(value = "更新方法")
    @PutMapping("{id}")
    public R update(
            @ApiParam(name = "id",value = "讲师id",required = true)
            @PathVariable  String id,
            @ApiParam(name = "teacher",value = "要更新的教师对象",required = true)
            @RequestBody Teacher teacher){
        teacher.setId(id);
        teacherService.updateById(teacher);
        return R.ok().message("更新成功");
    }




}
