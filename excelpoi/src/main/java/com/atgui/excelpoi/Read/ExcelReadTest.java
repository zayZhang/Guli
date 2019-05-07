package com.atgui.excelpoi.Read;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;

public class ExcelReadTest {

    @Test
    public void TtestRead() throws Exception{
        InputStream is =new FileInputStream("d:/excel-poi/test.xls");
        Workbook work=new HSSFWorkbook(is);
        Sheet sheet=work.getSheetAt(0);
        Row row=sheet.getRow(0);
        Cell cell=row.getCell(0);
        System.out.println(cell);
        is.close();
    }

    @Test
    public void testCellType()throws Exception{
        InputStream is=new FileInputStream("d:/excel-poi/test.xls");
        Workbook work=new HSSFWorkbook(is);
        Sheet sheet = work.getSheetAt(0);
//        Row row = sheet.getRow(2);
//        Cell cell = row.getCell(1);
        Row rowTitle=sheet.getRow(0);
        if(rowTitle!=null){
            int cellCount = rowTitle.getPhysicalNumberOfCells();
            for(int cellNum=0;cellNum<cellCount;cellNum++){
                Cell cell=rowTitle.getCell(cellNum);
                if(cell!=null){
                    int cellType=cell.getCellType();
                    String cellValue=cell.getStringCellValue();
                    System.out.print(cellValue);
                }
            }
            System.out.println();
        }
        //读取其他收据
        System.out.println("读取其他数据");

        // 读取第一页的数据
        int rowCount=sheet.getPhysicalNumberOfRows();

        //开始遍历第一页的行
        for(int rowNum=0;rowNum<rowCount;rowNum++){
            Row rowData=sheet.getRow(rowNum);
            if(rowData!=null){
                int cellcount=rowTitle.getPhysicalNumberOfCells();
                // 开始遍历每一行的每一列
                for (int cellNum=0;cellNum<cellcount;cellNum++){
                    System.out.print("【第" + (rowNum + 1) + "行-第" + (cellNum + 1) + "列】");
                    Cell cell=rowData.getCell(cellNum);
                    if(cell!=null){
                        int cellType=cell.getCellType();
                        String cellValue="";
                        switch(cellType){
                            case HSSFCell.CELL_TYPE_STRING://字符串
                                System.out.print("【STRING】");
                                cellValue = cell.getStringCellValue();
                                break;

                            case HSSFCell.CELL_TYPE_BOOLEAN://布尔
                                System.out.print("【BOOLEAN】");
                                cellValue = String.valueOf(cell.getBooleanCellValue());
                                break;

                            case HSSFCell.CELL_TYPE_BLANK://空
                                System.out.print("【BLANK】");
                                break;

                            case HSSFCell.CELL_TYPE_NUMERIC:
                                System.out.print("【NUMERIC】");
                                //cellValue = String.valueOf(cell.getNumericCellValue());

                                if (HSSFDateUtil.isCellDateFormatted(cell)) {//日期
                                    System.out.print("【日期】");
                                    Date date = cell.getDateCellValue();
                                    cellValue = new DateTime(date).toString("yyyy-MM-dd");
                                } else {
                                    // 不是日期格式，则防止当数字过长时以科学计数法显示
                                    System.out.print("【转换成字符串】");
                                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                    cellValue = cell.toString();
                                }
                                break;

                            case Cell.CELL_TYPE_ERROR:
                                System.out.print("【数据类型错误】");
                                break;
                        }
                        System.out.println(cellValue);
                    }
                }
            }

        }
    is.close();

    }
    @Test
    public void testFormula() throws Exception{

        InputStream is = new FileInputStream("d:/excel-poi/计算公式.xls");

        Workbook workbook = new HSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);

        // 读取第五行第一列
        Row row = sheet.getRow(4);
        Cell cell = row.getCell(0);

        //公式计算器
        FormulaEvaluator formulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);

        // 输出单元内容
        int cellType = cell.getCellType();
        switch (cellType) {
            case Cell.CELL_TYPE_FORMULA://2

                //得到公式
                String formula = cell.getCellFormula();
                System.out.print(formula);

                CellValue evaluate = formulaEvaluator.evaluate(cell);
                //String cellValue = String.valueOf(evaluate.getNumberValue());
                String cellValue = evaluate.formatAsString();
                System.out.println(cellValue);

                break;
        }
    }
}
