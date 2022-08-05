package com.mingmen.yanpro.dao;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import lombok.Data;

@Data
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT,verticalAlignment = VerticalAlignmentEnum.CENTER)//水平居中,垂直居中
public class BsjianzhangDao {
//    private Integer ID;
    @ExcelProperty("年份")
    private String NF;
    @ExcelProperty("学院（所）")
    private String YXSMC;
    @ExcelProperty("专业")
    private String ZYMC;
    @ExcelProperty("研究方向")
    private String YJFXMC;
    @ExcelProperty("指导教师")
    private String ZDJS;
    @ExcelProperty("拟招生人数")
    private String NZSRS;
    @ExcelProperty("考核科目")
    private String KSKM;
    @ExcelProperty("联系电话")
    private String LXDH;
    @ExcelIgnore
    private String XXFS;
    @ExcelIgnore
    private String ZSLX;
}
