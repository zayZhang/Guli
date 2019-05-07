package com.guli.vod.controller;

import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;
import com.guli.common.vo.R;
import com.guli.vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@Api(description = "阿里云视频点播微服务")
@RequestMapping("/admin/vod/video")
public class VidelAdminController {

    @Autowired
    private VideoService videoService;

    /**
     * 将视频上传进阿里云，并返回阿里云视频id
     * @param file
     * @return
     */
    @PostMapping("upload")
    public R uploadVideo(@ApiParam(name = "file", value = "文件", required = true)
                         @RequestParam("file") MultipartFile file){

        String s = videoService.uploadVideo(file);
        return R.ok().data("videoId",s);
    }

    /**
     * 去阿里云中删除
     * @param videoId
     * @return
     */
    @DeleteMapping("{videoId}")
    public R RemoveVideoById(
            @ApiParam(name="videoId", value="阿里云视频id", required = true)
            @PathVariable String videoId
    ){
        System.out.println("执行删除！    "+videoId);
        videoService.removeVideo(videoId);
        return R.ok().message("视频删除成功");
    }


    /**
     * 获取视频上传地址和凭证
     * @param title
     * @param fileName
     * @return
     */
    @GetMapping("get-upload-auth-and-address/{title}/{fileName}")
    public R getUploadAuthAndAddress(
            @ApiParam(name = "title", value = "视频标题", required = true)
            @PathVariable String title,

            @ApiParam(name = "fileName", value = "视频源文件名", required = true)
            @PathVariable String fileName){

        CreateUploadVideoResponse response = videoService.getUploadAuthAndAddress(title, fileName);
        return R.ok().message("获取视频上传地址和凭证成功").data("response", response);
    }

    /**
     * 刷新视频上传地址和凭证
     * @param videoId
     * @return
     */
    @GetMapping("refresh-upload-auth-and-address/{videoId}")
    public R refreshUploadAuthAndAddress(
            @ApiParam(name = "videoId", value = "云端视频id", required = true)
            @PathVariable String videoId){
        RefreshUploadVideoResponse response = videoService.refreshUploadAuthAndAddress(videoId);
        return R.ok().message("刷新视频上传地址和凭证成功").data("response", response);
    }
}
