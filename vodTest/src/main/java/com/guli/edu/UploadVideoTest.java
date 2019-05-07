package com.guli.edu;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import org.junit.Test;

public class UploadVideoTest {

    private final static String accessKeyId="LTAIrqGntiJOzAhB";
    private final static String accessKeySecret="TCBmjvGE3NMViRPimKbElRETiBZRQd";

    @Test
    public void testUploadVideo(){
        String fileName="F:/尚硅谷在线教育/data_bank/共享-在线教育-v3/06.资源/课程视频/2 - Projects and Progress.mp4";
        String title="Marvel_video_err.mp4";
        UploadVideoRequest request=new UploadVideoRequest(accessKeyId,accessKeySecret,title,fileName);
        UploadVideoImpl uploadVideo=new UploadVideoImpl();
        UploadVideoResponse response = uploadVideo.uploadVideo(request);
        String videoId = response.getVideoId();
        System.out.println(response.getCode());
        System.out.println(videoId);
    }

}
