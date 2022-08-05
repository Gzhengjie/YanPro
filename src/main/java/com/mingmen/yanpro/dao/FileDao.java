package com.mingmen.yanpro.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@TableName("sys_file")
public class FileDao {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "type")
    private String type;
    @JsonProperty(value = "size")
    private Long size;
    @JsonProperty(value = "url")
    private String url;
    @JsonProperty(value = "isDelete")
    private Boolean isDelete;
    @JsonProperty(value = "enable")
    private Boolean enable;
    @JsonProperty(value = "md5")
    private String md5;
}
