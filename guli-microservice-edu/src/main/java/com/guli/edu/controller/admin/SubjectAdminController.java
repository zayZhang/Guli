package com.guli.edu.controller.admin;


import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.common.vo.R;
import com.guli.edu.service.SubjectService;
import com.guli.edu.vo.SubjectNestedVO;
import com.guli.edu.vo.SubjectVO2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@Api(description = "文件处理类")
@CrossOrigin
@RequestMapping("/admin/edu/subject")
public class SubjectAdminController {

    @Autowired
    private SubjectService  subjectService;


    @PostMapping("import")
    @ApiOperation(value = "Excel上传方法")
    public R upload(
            @ApiParam(name = "file",value = "Excel 文件" ,required = true)
            @RequestParam  MultipartFile file){

        try {
            List<String> msgList  = subjectService.batchImport(file);
            if(msgList.size() > 0){
                return R.error().message("文件上传失败").data("errorMsgList",msgList);
            }else {
                return R.ok().message("文件上传成功");
            }
        } catch (Exception e) {
            throw  new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }
    }


    /**
     * 返回所有一级目录以及其子目录
     * @return
     */
    @GetMapping
    @ApiOperation(value = "嵌套数据列表方法")
    public R getSubject(){

        List<SubjectNestedVO> subjectNestedList = subjectService.getSubjectNestedList();

        return R.ok().data("items",subjectNestedList);
    }

    @ApiOperation(value = "嵌套数据列表")
    @GetMapping("list2")
    public R getSubject2(){

        List<SubjectVO2> subjectVO2s = subjectService.nestedList2();
        return R.ok().data("items",subjectVO2s);

    }


}
