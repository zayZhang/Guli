package com.guli.common.constants;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {

    SUCCESS(true, 20000,"成功"),
    UNKNOWN_REASON(false, 20001, "未知错误"),
    BAD_SQL_GRAMMAR(false, 21001, "sql语法错误"),
    JSON_PARSE_ERROR(false, 21002, "json解析异常"),
    PARAM_ERROR(false, 21003, "参数不正确"),
    FILE_UPLOAD_ERROR(false, 21004, "文件上传错误"),
    EXCEL_DATA_IMPORT_ERROR(false, 21005, "Excel数据导入错误"),
    VIDEO_UPLOAD_TOMCAT_ERROR(false,21006,"前端获取文件失败！！"),
    VIDEO_UPLOAD_ALIYUN_ERROR(false,21007 ,"上传文件失败" ),
    VIDEO_DELETE_ALIYUN_ERROR(false,21008 ,"删除视频失败!!"),
    FETCH_VIDEO_PLAYAUTH_ERROR(false,21009 ,"请求凭证失败!"),
    REFRESH_VIDEO_PLAYAUTH_ERROR(false,21010 , "刷新凭证失败！"),
    FETCH_PLAYAUTH_ERROR(false,201011 ,"获取上传凭证失败" ),
    URL_ENCODE_ERROR(false,201012 , "获取业务服务器重定向地址失败"),
    error_get_state(false,201013 , "获取state值失败"),
    FETCH_ACCESSTOKEN_FAILD(false,201014 ,"获取accessToken失败！" ),
    FETCH_USERINFO_ERROR(false,201015 ,"获取用户信息失败！" );
    private Boolean success;

    private Integer code;

    private String message;

    private ResultCodeEnum(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

}
