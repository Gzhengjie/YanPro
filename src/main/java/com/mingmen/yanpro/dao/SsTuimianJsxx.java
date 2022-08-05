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
@TableName(value = "tuimian_jsxx")
@ToString
public class SsTuimianJsxx {
    @TableId(type = IdType.AUTO)
    @Alias("自增ID（请忽略）")
    private Integer id;
    @Alias("姓名")
    private String xm;
    @Alias("性别")
    private String xb;
    @Alias("推荐院系所代码")
    private String tjyxsdm;
    @Alias("推荐院系所名称")
    private String tjyxsmc;
    @Alias("本科毕业专业代码") ///////
    private String bkzydm;
    @Alias("本科毕业专业名称")
    private String bkzymc;
    @Alias("接收院系所代码")
    private String jsyxsdm;
    @Alias("接收院系所名称")
    private String jsyxsmc;
    @Alias("拟攻读专业代码")
    private String ngdzydm;
    @Alias("拟攻读专业名称")
    private String ngdzymc;
    @Alias("导师姓名")
    private String dsxm;
    @Alias("导师是否博导")
    private String dssfbd;
    @Alias("接收类型代码")
    private String jslxdm;
    @Alias("接收类型名称")
    private String jslxmc;
    @Alias("复试总成绩")
    private Float fszcj;
    @Alias("复试总成绩排名")
    private Integer fszcjpm;
    @Alias("外语等级")
    private String wydj;
    @Alias("接收类别代码")
    private String jslbdm;
    @Alias("接收类别名称")
    private String jslbmc;
    @Alias("推免生来源")
    private String tmsly;
    @Alias("是否参加过我校夏令营")
    private String cjxly;
    @Alias("备注")
    private String bz;
    @Alias("年份")
    private Integer nf;
    @Alias("审核状态（0：学院秘书上传1：学院审核通过2：研究生院审核通过3：学院不通过4：研究生院不通过）")
    private Integer shzt;
    @Alias("上传人")
    private String scr;
    @Alias("上传时间")
    private String scsj;
    @Alias("学院审核人")
    private String xyshr;
    @Alias("学院审核时间")
    private String xyshsj;
    @Alias("更新时间")
    private String gxsj;
    @Alias("研院审核人")
    private String yyshr;
    @Alias("研院审核时间")
    private String yyshsj;


    public static void main(String[] args) {
        Date date = new Date();
        java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());

        System.out.println(System.currentTimeMillis());
    }
}
