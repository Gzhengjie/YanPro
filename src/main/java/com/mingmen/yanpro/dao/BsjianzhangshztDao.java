package com.mingmen.yanpro.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Value;

@Data
@TableName(value = "bs_jianzhang_shzt")
public class BsjianzhangshztDao {
    @TableField(value = "ID")
    private Integer ID;
    @TableField(value = "YXSDM")
    private String YXSDM;
    @TableField(value = "YXSMC")
    private String YXSMC;
    @TableField(value = "SHZT")
    private String SHZT;
    @TableField(value = "NF")
    private String NF;
    @TableField(exist = false)
    private String code;
}
