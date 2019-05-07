package com.guli.common.handller;

import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.common.util.ExceptionUtil;
import com.guli.common.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice   //异常通知类
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)   //拦截的异常处理为Exception
    @ResponseBody
    public R error(Exception e){
       // log.error(ExceptionUtil.getMessage(e));
        log.error("这是自己写的异常");
        //log.error(ExceptionUtil.getMessage(e));
        e.printStackTrace();
        ExceptionUtil.getMessage(e);
        return R.error();
    }

    //指定的异常处理配置
    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseBody
    public R error(BadSqlGrammarException e){
        e.printStackTrace();
        log.error(e.getMessage());
        //log.error(ExceptionUtil.getMessage(e));
        return R.setResult(ResultCodeEnum.BAD_SQL_GRAMMAR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public R error(JsonParseException e){
        e.printStackTrace();
        //log.error(ExceptionUtil.getMessage(e));
         log.error(e.getMessage());
        return R.setResult(ResultCodeEnum.JSON_PARSE_ERROR);
    }

    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public R error(GuliException e){
        e.printStackTrace();
        log.error("这是自己写的异常");
        //log.error(ExceptionUtil.getMessage(e));
        ExceptionUtil.getMessage(e);
        return R.error().message(e.getMessage()).code(e.getCode());
    }
}
