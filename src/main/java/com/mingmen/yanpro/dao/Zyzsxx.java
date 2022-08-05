package com.mingmen.yanpro.dao;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName(value = "ss_fushi_zyxx")
public class Zyzsxx {
    @Alias("ID")
    private Integer ID;
    @Alias("院系所代码")
    private String YXSDM;
    @Alias("院系所名称")
    private String  YXSMC;
    @Alias("专业代码")
    private String  ZYDM;
    @Alias("专业名称")
    private String  ZYMC;

    @Alias("研究方向")
    private String  YJFX;

    @Alias("学习方式")
    private String  XXFS;
    @Alias("计划总数")
    private Integer JHZS;
    @Alias("已接收推免生数")
    private Integer YJSTMS;
    @Alias("最终公开招考总数")
    private Integer GKZKZS;
    @Alias("复试比例")
    private Float   FSBL;
    @Alias("是否需要调剂")
    private String  SFXYTJ;
    @Alias("联系人及电话")
    private String  LXRJDH;
    @Alias("调剂要求")
    private String  DJYQ;
    @Alias("备注")
    private String  BZ;
    @Alias("审核状态")
    private Integer SHZT;
    @Alias("年份")
    private Integer NF;
    @Alias("上传人")
    private String  SCR;
    @Alias("上传时间")
    private String  SCSJ;
    @Alias("学院审核人")
    private String XYSHR;
    @Alias("学院审核时间")
    private String XYSHSJ;
    @Alias("研院审核人")
    private String YYSHR;
    @Alias("研院审核时间")
    private String YYSHSJ;
    @Alias("更新时间")
    private String GXSJ;

    @TableField(exist = false)
    private List<String> YJFXLIST;

}
