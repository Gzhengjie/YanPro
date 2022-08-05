package com.mingmen.yanpro.controller.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 接受前端登录请求的参数
 */
@Data
@TableName(value = "base_user")
public class UserDTO {
//    @JsonProperty(value = "ZGH")
    private String zgh;
//    @JsonProperty(value = "MM")
    private String mm;
    private String xm;
    private String yhz;
    private String yxsdm;
    private String yxsmc;
    @JsonProperty(value = "token")
    private String token;
}