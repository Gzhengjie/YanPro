package com.mingmen.yanpro.dao;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;



@Data
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT,verticalAlignment = VerticalAlignmentEnum.CENTER)//水平居中,垂直居中
public class SsJianzhangPinjie {
    @ExcelProperty("学院（所）")
    private String yxs;
    @ExcelProperty("专业")
    private String zy;
    @ExcelProperty("公开招考人数")
    @TableField(exist = false)
    private int zkrs;
    @ExcelProperty("接收推免人数")
    @TableField(exist = false)
    private int tmrs;
    @ExcelProperty("研究方向")
    private String yjfx;
    @ExcelProperty("指导教师")
    private String zdjs;
    @ExcelProperty("学习方式")
    private String xxfs;
    @ExcelProperty("招生类型")
    private String zslx;
    @ExcelProperty("考试科目")
    private String kskm;
    @ExcelProperty("复试笔试科目")
    private String fsbskm;
    @ExcelProperty("招生人数说明")
    private String rssm;
    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private String ignore;
}
