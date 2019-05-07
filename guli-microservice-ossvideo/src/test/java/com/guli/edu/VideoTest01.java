package com.guli.edu;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import org.junit.Test;

public class VideoTest01 {

    private static String accesKeyId="LTAICSHSwYoetimr";
    private static final String accessKeySecret="cyyeDGQk4NGIVGDx9pOBdAsme2b8PD";

    /**
     * 视频上传：本地文件上传
     */
    @Test
    public void testUploadVideo(){

        //视频标题(必选)
        String title = "3 - How Does Project Submission Work - upload by sdk";
        //文件名必须包含扩展名
        String fileName = "F:/尚硅谷在线教育/data_bank/共享-在线教育-v3/06.资源/课程视频/3 - How Does Project Submission Work.mp4";
        //本地文件上传
        UploadVideoRequest request = new UploadVideoRequest(accesKeyId, accessKeySecret, title, fileName);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
        /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
        //f517319f3a504063b9fd74fe2b8dc0a4
        //f517319f3a504063b9fd74fe2b8dc0a4
    }
}
