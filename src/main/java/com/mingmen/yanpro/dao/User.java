package com.mingmen.yanpro.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@TableName(value = "base_user")
public class User {
    @TableId(type = IdType.AUTO)
//    @JsonIgnore
    private Integer id;

    private String zgh;

    private String xm;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String mm;

    private String yxsdm;

    private String yhz;
}
