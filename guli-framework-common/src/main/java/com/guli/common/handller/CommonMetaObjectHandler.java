package com.guli.common.handller;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class CommonMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("添加的时候开始自动填充的时候打印");
        this.setFieldValByName("gmtCreate",new Date(),metaObject);
        this.setFieldValByName("gmtModified",new Date(),metaObject);
    }

    @Override
        public void updateFill(MetaObject metaObject) {
            log.info("开始修改的时候自动填充的时候打印...");

            this.setFieldValByName("gmtModified", new Date(), metaObject);
        }
}
