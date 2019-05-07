package com.guli.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.util.ExcelImportUtil;
import com.guli.edu.entity.Subject;
import com.guli.edu.mapper.SubjectMapper;
import com.guli.edu.service.SubjectService;
import com.guli.edu.vo.SubjectNestedVO;
import com.guli.edu.vo.SubjectVO;
import com.guli.edu.vo.SubjectVO2;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程科目 服务实现类
 *
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Transactional
    @Override
    public List<String> batchImport(MultipartFile file) throws Exception {

        List<String> msgList = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        ExcelImportUtil excelImportUtil = new ExcelImportUtil(inputStream);

        // 获取sheet
        HSSFSheet sheet = excelImportUtil.getSheet();
        int rows = sheet.getPhysicalNumberOfRows();
        // 判断用户时候输入有效数据
        if(rows <= 1){
            msgList.add("请输入数据");
            return msgList;
        }
        String cellValue = "";
        // 获取第一列的信息
        for (int rowNum = 1; rowNum < rows; rowNum++) {
            HSSFRow row = sheet.getRow(rowNum);
            if(row != null){
                HSSFCell cell = row.getCell(0);
                if(cell != null){
                     cellValue = excelImportUtil.getCellValue(cell).trim();
                     if(StringUtils.isEmpty(cellValue)){
                         msgList.add("第"+rowNum+"行一级标题为空");
                         continue;
                     }
                }
            }
            Subject subject = selectTitle(cellValue);
            String parentId =null;
            if(subject == null){
                Subject subject1 = new Subject();
                subject1.setTitle(cellValue);
                subject1.setParentId("0");
                subject1.setSort(rowNum);
                baseMapper.insert(subject1);
                parentId = subject1.getId();
            } else {
                 parentId = subject.getId();
            }
            // 获取第二列信息
            HSSFCell cell = row.getCell(1);
            if(cell != null){
                String cellOneValue = excelImportUtil.getCellValue(cell).trim();
                if (StringUtils.isEmpty(cellOneValue)) {
                    msgList.add("第" + rowNum + "行二级分类为空");
                    continue;
                }
                Subject subject1 = SubjectSubByTitle(cellOneValue, parentId);
                if(subject1 == null){
                    Subject subject2 = new Subject();
                    subject2.setTitle(cellOneValue);
                    subject2.setParentId(parentId);
                    subject2.setSort(rowNum);
                    baseMapper.insert(subject2);
                }
            }
        }
        return msgList;
    }

    @Override
    public List<SubjectNestedVO> getSubjectNestedList() {
        // 创建一级分类数组
        ArrayList<SubjectNestedVO> subjectNestedVOList = new ArrayList<>();
        // 获取所有的一级分类
        QueryWrapper<Subject> subjectNestedVOQueryWrapper = new QueryWrapper<>();
        subjectNestedVOQueryWrapper.eq("parent_id",0);
        subjectNestedVOQueryWrapper.orderByAsc("sort", "id");
        List<Subject> subjectList = baseMapper.selectList(subjectNestedVOQueryWrapper);

        // 获取所有的二级分类
        QueryWrapper<Subject> subjectVOQueryWrapper = new QueryWrapper<>();
        subjectVOQueryWrapper.ne("parent_id", 0);
        subjectVOQueryWrapper.orderByAsc("sort","id");
        List<Subject> subSubjectList = baseMapper.selectList(subjectVOQueryWrapper);

        for (int i = 0; i < subjectList.size(); i++) {

            SubjectNestedVO subjectNestedVO = new SubjectNestedVO();
            Subject subject = subjectList.get(i);
            BeanUtils.copyProperties(subject,subjectNestedVO);
            // 填充一级分类数组
            subjectNestedVOList.add(subjectNestedVO);

            // 创建二级分类数组
            ArrayList<SubjectVO> subjectVOList = new ArrayList<>();
            // 遍历所有的二级分类
            for (int j = 0; j < subSubjectList.size(); j++) {
                Subject subSubject = subSubjectList.get(j);
                // 填充分类
                if(subject.getId().equals(subSubject.getParentId())){
                    SubjectVO subjectVO = new SubjectVO();
                    BeanUtils.copyProperties(subSubject,subjectVO);
                    subjectVOList.add(subjectVO);
                }
            }
            // 填充一级分类
            subjectNestedVO.setChildren(subjectVOList);
        }
        // 返回结果
        return subjectNestedVOList;
    }

    @Override
    public List<SubjectVO2> nestedList2() {


        return baseMapper.selectNestedListByParentId("0");
    }

    // 去重的工具方法只希望在此类中使用所以使用private修饰
    /**
     * 根据分类名称查询这个一级分类中否存在
     * @param title
     * @return
     */
    private Subject selectTitle(String title){
        QueryWrapper<Subject> subjectQueryWrapper = new QueryWrapper<>();

        subjectQueryWrapper.eq("parent_id","0");
        subjectQueryWrapper.eq("title",title);

        return  baseMapper.selectOne(subjectQueryWrapper);
    }


    /**
     * 根据分类名称和父id查询这个二级分类中否存在
     * @param title
     * @return
     */
    private Subject SubjectSubByTitle(String title,String parentId){
        QueryWrapper<Subject> subjectQueryWrapper = new QueryWrapper<>();
        subjectQueryWrapper.eq("parent_id",parentId);
        subjectQueryWrapper.eq("title",title);
        return baseMapper.selectOne(subjectQueryWrapper);

    }
}
