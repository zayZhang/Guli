package com.guli.vod.service;

import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;
import org.springframework.web.multipart.MultipartFile;

public interface VideoService {

    //上传视频到阿里云
    String uploadVideo(MultipartFile file);

    void removeVideo(String videoId);

    //获取上传凭证
    String getVideoPlayAuth(String videoId);
    /**
     * 获取上传凭证和地址
     * @param title
     * @param fileName
     * @return
     */
    CreateUploadVideoResponse getUploadAuthAndAddress(String title, String fileName);

    /**
     * 刷新上传凭证和地址
     * @param videoId
     * @return
     */
    RefreshUploadVideoResponse refreshUploadAuthAndAddress(String videoId);

}
