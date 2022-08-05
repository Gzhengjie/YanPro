package com.mingmen.yanpro.dao;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CltjkgDao {
    private Integer ID;
    private String CLMC;
    //@JsonIgnore //忽略该字段，不给前端展示该字段
    private String SFYXTJ;
}
