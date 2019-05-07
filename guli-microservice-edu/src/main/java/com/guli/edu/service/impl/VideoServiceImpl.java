package com.guli.edu.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;
import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.edu.entity.Video;
import com.guli.edu.form.VideoInfoForm;
import com.guli.edu.mapper.VideoMapper;
import com.guli.edu.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.vod.utils.AliyunVodSDKUtils;
import com.guli.vod.utils.ConstantPropertiesUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2019-04-15
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Override
    public void saveVideoInfo(VideoInfoForm videoInfoForm) {
        Video video=new Video();
        BeanUtils.copyProperties(videoInfoForm,video);
        baseMapper.insert(video);
    }

    @Override
    public VideoInfoForm getVideoInfoFormById(String id) {
        Video video = baseMapper.selectById(id);
        VideoInfoForm videoInfoForm=new VideoInfoForm();
        BeanUtils.copyProperties(video,videoInfoForm);
        return videoInfoForm;
    }

    @Override
    public void updateVideoInfoById(VideoInfoForm videoInfoForm) {
        Video video=new Video();
        BeanUtils.copyProperties(videoInfoForm,video);
        baseMapper.updateById(video);
    }

    @Override
    public void removeVideoById(String id) {

        //删除视频资源 TODO

        baseMapper.deleteById(id);


    }

    @Override
    public CreateUploadVideoResponse getUploadAuthAndAddress(String title, String fileName) {
        try {
            //title是视频显示的名字，filename是视频文件名
            //初始化
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
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
            throw new GuliException(ResultCodeEnum.FETCH_VIDEO_PLAYAUTH_ERROR);
        }



    }

    /**
     * 和上面的获取凭证一样，只不过这个是通过视频id
     * @param videoId
     * @return1
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
    //https://github.com/zayZhang/guli.git
    //https://github.com/zayZhang/guli.git
}
