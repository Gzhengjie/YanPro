package com.mingmen.yanpro.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@TableName(value = "base_gjzxjh")
public class GjzxjhDao {
    @JsonProperty(value = "ID")
    private Integer ID;
    @JsonProperty(value = "ZXJHDM")
    @TableField(value = "ZXJHDM")
    private String ZXJHDM;
    @JsonProperty(value = "ZXJHMC")
    @TableField(value = "ZXJHMC")
    private String ZXJHMC;
    @JsonProperty(value = "BZ")
    @TableField(value = "BZ")
    private String BZ;
}
