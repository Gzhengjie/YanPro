package com.mingmen.yanpro.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@TableName(value = "bs_jianzhang_zyzsrs")
public class BszyzsrsDao {
    @TableField(value = "ID")
    private Integer ID;
    @TableField(value = "YXSDM")
    private String YXSDM;
    @TableField(value = "YXSMC")
    private String YXSMC;
    @TableField(value = "ZYDM")
    private String ZYDM;
    @TableField(value = "ZYMC")
    private String ZYMC;
    @TableField(value = "NZSRS")
    private Integer NZSRS;
    @TableField(value = "NF")
    private String NF;
    @TableField(value = "BZ")
    private String BZ;
    @TableField(value = "SCR")
    private String SCR;
    @TableField(value = "SCSJ")
    private String SCSJ;
    @TableField(value = "XYSHR")
    private String XYSHR;
    @TableField(value = "XYSHSJ")
    private String XYSHSJ;
    @TableField(value = "YYSHR")
    private String YYSHR;
    @TableField(value = "YYSHSJ")
    private String YYSHSJ;
    @TableField(value = "GXSJ")
    private String GXSJ;
}
