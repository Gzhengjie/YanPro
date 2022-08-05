package com.mingmen.yanpro.controller.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserPasswordDTO {
    private String username;
    private String password;
    private String newPassword;
    private String email;
    private String code;
}
