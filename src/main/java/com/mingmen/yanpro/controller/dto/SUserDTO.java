package com.mingmen.yanpro.controller.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 接受前端登录请求的参数
 */
@Data
@ToString
public class SUserDTO {
    private Integer id;
    private String username;
    private String email;
    private String code;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private String role;
    //private List<Menu> menus;
}
