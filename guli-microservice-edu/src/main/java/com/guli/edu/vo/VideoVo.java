package com.guli.edu.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 与页面交互的视频对象
 * 页面中只需要这些数据，所以只定义这些
 */

@ApiModel(value = "章节信息")
@Data
public class VideoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //视频id
    private String id;
    //title名
    private String title;
    //是否免费
    private Boolean free;
    //视频Id
    private String videoSourceId;

}