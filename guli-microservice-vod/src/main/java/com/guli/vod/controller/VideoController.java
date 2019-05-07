package com.guli.vod.controller;

import com.guli.common.vo.R;
import com.guli.vod.service.VideoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(description = "阿里云视频云点播")
@CrossOrigin
@RestController
@RequestMapping("/vod/video")
public class VideoController {
    @Autowired
    private VideoService videoService;

    @GetMapping("get-play-auth/{videoId}")
    public R getVideoPlayAuth(@PathVariable("videoId")String videoId){
        String playAuth=videoService.getVideoPlayAuth(videoId);
        return R.ok().message("获取地址成功").data("playAuth", playAuth);
    }
}
