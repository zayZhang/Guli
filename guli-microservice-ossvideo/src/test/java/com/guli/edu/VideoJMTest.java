package com.guli.edu;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class VideoJMTest {

    private static String key="LTAICSHSwYoetimr";
    private static String value="cyyeDGQk4NGIVGDx9pOBdAsme2b8PD";

    @Test
    public void test1() throws Exception {
        Map<String,String> map=new HashMap<>();
        map.put("VideoId","f517319f3a504063b9fd74fe2b8dc0a4");
        map.put("Action","GetVideoPlayAuth");
        
        Map<String,String> publicMap=AliyunVodAPIUtils.generatePublicParamters();
        String url = AliyunVodAPIUtils.generateOpenAPIURL(publicMap, map);
        System.out.println("___________________________________________________");
        System.out.println(url);  //http://vod.cn-shanghai.aliyuncs.com/?Action=GetVideoPlayAuth&VideoId=f517319f3a504063b9fd74fe2b8dc0a4&Signature=cn%2FAUlrY1cdi19aHf8CCoNAO7Iw%3D
        AliyunVodAPIUtils.httpGet(url);
    }

    @Test
    public void test2() throws Exception {
        Map<String,String> map=new HashMap<>();
        map.put("VideoId","f517319f3a504063b9fd74fe2b8dc0a4");
        map.put("Action","GetPlayInfo");

        Map<String,String> publicMap=AliyunVodAPIUtils.generatePublicParamters();
        String url = AliyunVodAPIUtils.generateOpenAPIURL(publicMap, map);
        System.out.println("___________________________________________________");
        System.out.println(url);  //http://vod.cn-shanghai.aliyuncs.com/?Action=GetVideoPlayAuth&VideoId=f517319f3a504063b9fd74fe2b8dc0a4&Signature=cn%2FAUlrY1cdi19aHf8CCoNAO7Iw%3D
        AliyunVodAPIUtils.httpGet(url);  //coverURL是播放的显示的头图片，playauth是播放凭证，playinfo是播放地址
    }
}
