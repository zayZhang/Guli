package com.guli.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.edu.entity.Subject;
import com.guli.edu.vo.SubjectNestedVO;
import com.guli.edu.vo.SubjectVO2;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author Helen
 * @since 2019-04-15
 */
public interface SubjectService extends IService<Subject> {

    List<String> batchImport(MultipartFile multipartFile) throws Exception;

    List<SubjectNestedVO> getSubjectNestedList();


    List<SubjectVO2> nestedList2();

}
