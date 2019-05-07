package com.guli.edu.controller.admin;

import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.guli.common.vo.R;
import com.guli.edu.form.VideoInfoForm;
import com.guli.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(description = "课时管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/edu/video")
public class VideoAdminController {
    @Autowired
    private VideoService videoService;

    @ApiOperation(value = "新增课时")
    @PostMapping("save-video-info")
    public R save(
            @ApiParam(name = "videoForm", value = "课时对象", required = true)
            @RequestBody VideoInfoForm videoInfoForm){

        videoService.saveVideoInfo(videoInfoForm);
        return R.ok();
    }

    @ApiOperation(value = "根据ID查询课时")
    @GetMapping("video-info/{id}")
    public R getVideInfoById(
            @ApiParam(name = "id", value = "课时ID", required = true)
            @PathVariable String id){

        VideoInfoForm videoInfoForm = videoService.getVideoInfoFormById(id);
        return R.ok().data("item", videoInfoForm);
    }

    /**
     * 更新视频信息，video表
     * @param videoInfoForm
     * @param id
     * @return
     */
    @ApiOperation(value = "更新课时")
    @PutMapping("update-video-info/{id}")
    public R updateCourseInfoById(
            @ApiParam(name = "VideoInfoForm", value = "课时基本信息", required = true)
            @RequestBody VideoInfoForm videoInfoForm,
            @ApiParam(name = "id", value = "课时ID", required = true)
            @PathVariable String id){
                System.out.println("更新课时");
        videoInfoForm.toString();
        videoService.updateVideoInfoById(videoInfoForm);
        return R.ok();
    }

    @ApiOperation(value = "根据ID删除课时")
    @DeleteMapping("{id}")
    public R removeById(
            @ApiParam(name = "id", value = "课时ID", required = true)
            @PathVariable String id){

        videoService.removeVideoById(id);
        return R.ok();
    }

    @ApiOperation(value="从阿里云服务器获取上传凭证")
    @GetMapping("get-upload-auth-and-address/{title}/{fileName}")
    public R getUploadAuthAndAddress( @ApiParam(name = "title", value = "视频标题", required = true)
                                          @PathVariable String title,
                                      @ApiParam(name = "fileName", value = "视频源文件名", required = true)
                                      @PathVariable String fileName){
        CreateUploadVideoResponse uploadAuthAndAddress = videoService.getUploadAuthAndAddress(title, fileName);
        return R.ok().message("获取视频上传地址和凭证成功").data("response", uploadAuthAndAddress);
    }

    /**
     * 刷新视频上传地址和凭证
     * @param videoId
     * @return
     */
    @ApiOperation(value="根据视频id刷新上传凭证")
    @GetMapping("refresh-upload-auth-and-address/{videoId}")
    public R refreshUploadAuthAndAddress(
            @ApiParam(name = "videoId", value = "云端视频id", required = true)
            @PathVariable String videoId){
        videoService.refreshUploadAuthAndAddress(videoId);
        return R.ok().message("刷新视频上传地址和凭证成功");
    }

}
