package com.guli.edu.server;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.guli.edu.AliyunVodSDKUtils;
import org.junit.Test;

import java.util.List;

public class testVod {

    private static String key="LTAICSHSwYoetimr";
    private static String value="cyyeDGQk4NGIVGDx9pOBdAsme2b8PD";

    @Test
    public void testPlayAuto() throws Exception{
        //初始化客户端
        DefaultAcsClient client= AliyunVodSDKUtils.initVodClient(key,value);
        //初始化请求对象
        GetVideoPlayAuthRequest request=new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response=new GetVideoPlayAuthResponse();
        try{
            request.setVideoId("26ba66178098408b9fcd2be358ac008e");
           // request.setActionName("GetVideoPlayAuth"); //这个是默认会获取的,可以说基本上我们需要的餐数据，都会默认获取
            response=client.getAcsResponse(request);
            System.out.println("response:   "+response.getPlayAuth());

        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("requestID:  "+response.getRequestId());
    }

    //获取视频播放地址
    @Test
    public void testInfo() throws Exception{
        DefaultAcsClient client=AliyunVodSDKUtils.initVodClient(key,value);
        GetPlayInfoRequest request=new GetPlayInfoRequest();
        GetPlayInfoResponse response=new GetPlayInfoResponse();
        try{
            request.setVideoId("26ba66178098408b9fcd2be358ac008e");
            response=client.getAcsResponse(request);
            List<GetPlayInfoResponse.PlayInfo> list = response.getPlayInfoList();
            for(GetPlayInfoResponse.PlayInfo r:list){
                System.out.println("URL:"+r.getPlayURL());
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
