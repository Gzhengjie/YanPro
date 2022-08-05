package com.mingmen.yanpro.dao;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SscskmDao {
    private Integer ID;
    private String KSKMDM;
    //@JsonIgnore //忽略该字段，不给前端展示该字段
    private String KSKMMC;
    private String MTYXSDM;
    private String CKSM;
    private String KSFZ;
    private String KSSC;
    private String SFDHB;
    private String KSSM;
    private String SFTK;
    private String KSDY;
}
