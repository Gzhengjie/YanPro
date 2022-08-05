package com.mingmen.yanpro.dao;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@TableName(value = "tuimian_jszbxx")
@ToString

public class SsTuimianJszbxx {
    @TableId(type = IdType.AUTO)
    @Alias("自增ID")
    private Integer id;
    @Alias("接收院系所代码")
    private String jsyxsdm;
    @Alias("接收院系所名称")
    private String jsyxsmc;
    @Alias("拟攻读博士专业代码")
    private String ngdbszydm;
    @Alias("拟攻读博士专业名称")
    private String ngdbszymc;
    @Alias("学生姓名")
    private String xsxm;
    @Alias("导师姓名")
    private String dsxm;
    @Alias("本科阶段学分成绩")
    private Double bkxfcj;
    @Alias("本科专业学分成绩排名")
    private Integer bkxfcjpm;
    @Alias("本科专业人数")
    private Integer bkzyrs;
    @Alias("复试总成绩")
    private Double fszcj;
    @Alias("CTE-6或小语种四级成绩")
    private Integer cte;
    @Alias("联系电话")
    private String lxdh;
    @Alias("备注")
    private String bz;
    @Alias("年份")
    private Integer nf;
    @Alias("审核状态（0：学院秘书上传1：学院审核通过2：研究生院审核通过3：学院不通过4：研究生院不通过）")
    private Integer shzt;
    @Alias("上传人")
    private String scr;
    @Alias("上传时间")
    private java.sql.Timestamp scsj;
    @Alias("审核人")
    @TableField(value = "SHR")
    private String shenheren;
    @Alias("审核时间")
    private java.sql.Timestamp shsj;
    @Alias("更新时间")
    private java.sql.Timestamp gxsj;

    public static void main(String[] args) {
        Date date = new Date();
        java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());

        System.out.println(System.currentTimeMillis());
    }

}
