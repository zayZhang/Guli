package com.guli.edu.mapper;

import com.guli.edu.entity.Subject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guli.edu.vo.SubjectVO2;

import java.util.List;

/**
 * <p>
 * 课程科目 Mapper 接口
 * </p>
 *
 * @author Helen
 * @since 2019-04-15
 */
public interface SubjectMapper extends BaseMapper<Subject> {
    List<SubjectVO2> selectNestedListByParentId(String parentId);

}
