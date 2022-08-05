package com.mingmen.yanpro.dao;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 申请考核博士生信息类
 */
@Data
@TableName(value = "bs_luqu_sqkhxx")
public class BsLQSQKHXX {
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

    @Alias("导师职工号")
    private String dszgh;

    @Alias("导师姓名")
    private String dsxm;

    @Alias("性别")
    private String xb;

    @Alias("证件号")
    private String zjhm;

    @Alias("考生姓名")
    private String ksxm;

    @Alias("外国语")
    private String wgy;

    @Alias("业务课一")
    private String ywk1;

    @Alias("业务课二")
    private String ywk2;

    @Alias("面试")
    private String ms;

    @Alias("考核总成绩")
    private String khzcj;

    @Alias("政审结果")
    private String zsjg;

    @Alias("心里测试结果")
    private String xlcsjg;

    @Alias("录取类别代码")
    private String lqlbdm;

    @Alias("录取类别名称")
    private String lqlbmc;

    @Alias("考生档案所在单位")
    private String daszdw;

    @Alias("是否调取档案")
    private String sfdqda;

    @Alias("应届硕士生注册学号")
    private String yjssxh;

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
