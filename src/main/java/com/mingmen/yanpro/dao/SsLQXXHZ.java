package com.mingmen.yanpro.dao;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigInteger;

@Data
@TableName(value = "ss_luqu_xxhuizong")
public class SsLQXXHZ {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @Alias("录取院系代码")
    private String lqyxdm;

    @Alias("录取院系名称")
    private String lqyxmc;

    @Alias("录取专业代码")
    private String lqzydm;

    @Alias("录取专业名称")
    private String lqzymc;

    @Alias("学习方式")
    private String xxfs;

    @Alias("考生姓名")
    private String ksxm;

    @Alias("考生编号")
    private String ksbh;

    @Alias("导师姓名")
    private String dsxm;

    @Alias("调剂标志")
    private String tjbj;

    @Alias("初试总成绩")
    private  Float cszcj;

    @Alias("笔试成绩")
    private Float bscj;

    @Alias("面试成绩")
    private Float mscj;

    @Alias("复试成绩")
    private Float fscj;

    @Alias("总成绩")
    private Float zcj;

    @Alias("成绩排名")
    private Integer cjpm;
    @JsonIgnore
    private String tgxmdm;

    @TableField(exist = false)
    @Alias("四六级通过项目名称")
    private String tgxmmc;

    @JsonIgnore
    private String nlqlbm;

    @TableField(exist = false)
    @Alias("拟录取类别")
    private String nlqlb;

    @JsonIgnore
    private String zxjh;

    @TableField(exist = false)
    @Alias("专项计划计划码")
    private String zxjhmc;

    @JsonIgnore
    private String xmm;

    @TableField(exist = false)
    @Alias("专业学位项目专项项目")
    private String xmmc;

    @JsonIgnore
    private String dxjydwdm;

    @TableField(exist = false)
    @Alias("定向就业单位")
    private String  dxjydw;

    @Alias("所在单位")
    private String  szdw;

    @Alias("是否调档")
    private String sfdd;

    @Alias("备注")
    private String bz;

    @Alias("年份")
    private int nf;

    private int shzt;

    @Alias("上传人")
    private String scr;

    @Alias("上传时间")
    private String scsj;

    @Alias("学院审核人")
    private String xyshr;

    @Alias("学院审核时间")
    private String xyshsj;

    @Alias("研院审核人")
    private String yyshr;

    @Alias("研院审核时间")
    private String yyshsj;

    @Alias("更新时间")
    private String gxsj;
}
