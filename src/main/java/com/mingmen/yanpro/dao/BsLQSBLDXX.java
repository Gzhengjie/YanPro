package com.mingmen.yanpro.dao;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "bs_luqu_sbldxx")
public class BsLQSBLDXX {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @Alias("录取院系代码")
    private String yxsdm;

    @Alias("院系所名称")
    private String yxsmc;

    @Alias("专业代码")
    private String zydm;

    @Alias("专业名称")
    private String zymc;

    @Alias("学号")
    private String xh;

    @Alias("姓名")
    private String xm;

    @Alias("导师姓名")
    private String dsxm;

    @Alias("应完成学分数")
    private String ywcxfs;

    @Alias("已完成学分数")
    private String wcxfs;

    @Alias("单科最低成绩")
    private String dkzdcj;

    @Alias("有无重修")
    private String ywcx;

    @Alias("有无纪律处分")
    private String ywjlcf;

    @Alias("发表论文数")
    private String fblws;

    @Alias("外语考核成绩")
    private String wykhcj;

    @Alias("综合考核评价成绩")
    private String zhkhpjcj;

    @Alias("年份")
    private int nf;

    @Alias("备注")
    private String bz;

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
