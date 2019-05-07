package com.guli.edu;


import com.guli.edu.util.AliyunVodAPIUtils;

import java.util.HashMap;
import java.util.Map;

public class VodMain {

    public static void main(String[] ags) throws Exception {
        Map<String,String> privateParams=generatePrivateParamters();
        Map<String,String> publicParams= AliyunVodAPIUtils.generatePublicParamters();
        String URL=AliyunVodAPIUtils.generateOpenAPIURL(publicParams,privateParams);
        String s = AliyunVodAPIUtils.httpGet(URL);
        System.out.println(s);
    }

    private static Map<String,String> generatePrivateParamters(){
        Map<String,String> privateParams=new HashMap();
        privateParams.put("VideoId","4552aaa7e99f4fe3aeea171c1ed03421");
        privateParams.put("Action","GetVideoPlayAuth");
        return privateParams;

    }
}
