package com.mingmen.yanpro.dao;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString
@Getter
@Setter
@TableName(value = "ss_xuanchuan_xlyxx")
public class SsXuanchuanXlyxx {
    @TableId(type = IdType.AUTO)
    private Integer ID;
    @Alias("学号")
    private String XH;
    @Alias("姓名")
    private String XM;
    @Alias("民族")
    private String MZ;
    @Alias("政治面貌")
    private String ZZMM;
    @Alias("一寸照片")
    private String YCZP;
    @Alias("证件号码")
    private String ZJHM;
    @Alias("性别")
    private String XB;
    @Alias("申请类别")
    private String SQLB;
    @Alias("出生日期")
    private String BIRTHDAY;
    @Alias("地址邮编")
    private String DZYB;
    @Alias("入学时间")
    private String RXSJ;
    @Alias("毕业时间")
    private String BYSJ;
    @Alias("电话")
    private String DH;
    @Alias("邮箱")
    private String EMAIL;
    @Alias("院系所代码")
    private String YXSDM;
    @Alias("院系所名称")
    private String YXSMC;
    @Alias("专业代码")
    private String ZYDM;
    @Alias("专业名称")
    private String ZYMC;
    @Alias("本科生专业人数")
    private Integer ZYRS;
    @Alias("学分成绩")
    private Float XFCJ;
    @Alias("前三年专业排名")
    private Integer QSNZYPM;
    @Alias("GPA")
    private Float GPA;
    @Alias("外语等级")
    private String WYDJ;
    @Alias("上传材料连接")
    private String SCCL;
    @Alias("毕业学校")
    private String BYXX;
    @Alias("毕业院系所")
    private String BYYXS;
    @Alias("毕业专业")
    private String BYZY;
    @Alias("获奖情况")
    private String HJQK;
    @Alias("科研成果")
    private String KYCG;
    @Alias("个人简介")
    private String GRJJ;
    @Alias("审核状态")
    private Integer SHZT;
    @Alias("年份")
    private String NF;
    @Alias("备注")
    private String BZ;
    @Alias("填报时间")
    private String TBSJ;
    @Alias("更新时间")
    private String GXSJ;
    @Alias("学院审核人")
    private String XYSHR;
    @Alias("学院审核时间")
    private String XYSHSJ;

    /*
    private String XH;
    private String XM;
    private String ZJHM;
    private String XB;
    private String YXSDM;
    private String YXSMC;
    private String ZYDM;
    private String ZYMC;
    private Integer ZYRS;
    private Float XFCJ;
    private Integer QSNZYPM;
    private Float GPA;
    private String WYYZ;
    private String WYDJ;
    private Integer NF;
    private String MM;
    private String SCCL_path;
    private String BZ;
    private Integer SHZT;
    private String GRZP; //个人照片

     */


}
