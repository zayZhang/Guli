package com.guli.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.utils.StringUtils;
import com.aliyuncs.vod.model.v20170321.*;
import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.vod.service.VideoService;
import com.guli.vod.utils.AliyunVodSDKUtils;
import com.guli.vod.utils.ConstantPropertiesUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class VideoServiceImpl implements VideoService {

    //上传课程视频到阿里云视频点播
    @Override
    public String uploadVideo(MultipartFile file) {
        //文件名
        String fileName=file.getOriginalFilename();
        // 视频标题
        String title=fileName.substring(0,fileName.lastIndexOf("."));
        InputStream inputStream = null;

        try{
            inputStream=file.getInputStream();
        }catch(Exception e){
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_TOMCAT_ERROR);
        }
        //准备上传的请求
        UploadStreamRequest request = new UploadStreamRequest(
                ConstantPropertiesUtil.ACCESS_KEY_ID,
                ConstantPropertiesUtil.ACCESS_KEY_SECRET,
                title,
                fileName,
                inputStream);
        UploadVideoImpl uploader=new UploadVideoImpl();
        //开始上传，并返回响应，
        UploadStreamResponse response=uploader.uploadStream(request);
        String VideoId=response.getVideoId();
        if(response.isSuccess()){
            if(StringUtils.isEmpty(VideoId)){
                throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
            }
        }
        //将视频的id返回，供后面保存进数据库
        return VideoId;
    }

    /**
     * 根据id删除视频
     * @param videoId
     */
    @Override
    public void removeVideo(String videoId) {
        DefaultAcsClient client=null;
        try{
            client=AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET
            );
            DeleteVideoRequest request=new DeleteVideoRequest();
            request.setVideoIds(videoId);
            DeleteVideoResponse response = client.getAcsResponse(request);

        }catch(ClientException e){
            throw new GuliException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }

    /**
     * 获取播放凭证
     * @param videoId
     * @return
     */
    @Override
    public String getVideoPlayAuth(String videoId) {
        DefaultAcsClient client = null;
        System.out.println(videoId);
        try {
            client = AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);

            //请求
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(videoId);
            //响应
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            //得到播放凭证
            String playAuth = response.getPlayAuth();
            System.out.println(playAuth);
            return playAuth;
        } catch (ClientException e) {
            throw new GuliException(ResultCodeEnum.FETCH_PLAYAUTH_ERROR);
        }
    }

    /**
     * 获取上传视频凭证
     * @param title
     * @param fileName
     * @return
     */
    @Override
    public CreateUploadVideoResponse getUploadAuthAndAddress(String title, String fileName) {
        DefaultAcsClient client = null;
        try {
            //初始化
            client = AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);

            //创建请求对象
            CreateUploadVideoRequest request = new CreateUploadVideoRequest();
            request.setTitle(title);
            request.setFileName(fileName);

            //获取响应
            CreateUploadVideoResponse response = client.getAcsResponse(request);

            return response;
        } catch (ClientException e) {
            throw new GuliException(ResultCodeEnum.FETCH_PLAYAUTH_ERROR);
        }
    }

    /**
     * 刷新视频上传凭证
     * @param videoId
     * @return
     */
    @Override
    public RefreshUploadVideoResponse refreshUploadAuthAndAddress(String videoId) {
        try {
            //初始化
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);

            //创建请求对象
            RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();
            request.setVideoId(videoId);

            //获取响应
            RefreshUploadVideoResponse response = client.getAcsResponse(request);

            return response;

        } catch (ClientException e) {
            throw new GuliException(ResultCodeEnum.REFRESH_VIDEO_PLAYAUTH_ERROR);
        }
    }
}
