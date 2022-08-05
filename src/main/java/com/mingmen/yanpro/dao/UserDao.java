package com.mingmen.yanpro.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@TableName(value = "base_user")
public class UserDao {
    @JsonProperty(value = "ID")
    private Integer ID;
    @JsonProperty(value = "ZGH")
    @TableField(value = "ZGH")
    private String ZGH;
    @JsonProperty(value = "XM")
    @TableField(value = "XM")
    private String XM;
    @JsonProperty(value = "YXSDM")
    @TableField(value = "YXSDM")
    private String YXSDM;
    @JsonProperty(value = "MM")
    @TableField(value = "MM")
    private String MM;
    @JsonProperty(value = "YHZ")
    @TableField(value = "YHZ")
    private String YHZ;
    @JsonProperty(value = "CJR")
    @TableField(value = "CJR")
    private String CJR;
    @JsonProperty(value = "CJSJ")
    @TableField(value = "CJSJ")
    private String CJSJ;
    @JsonProperty(value = "GXR")
    @TableField(value = "GXR")
    private String GXR;
    @JsonProperty(value = "GXSJ")
    @TableField(value = "GXSJ")
    private String GXSJ;
}
