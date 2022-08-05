package com.mingmen.yanpro.dao;

import lombok.Data;

@Data
public class    SsdsDao {
    private Integer ID;
    private String ZGH;
    //@JsonIgnore //忽略该字段，不给前端展示该字段
    private String DSXM;
    private String SZYXDM;
    private String SZYXMC;
    private String ZC;
    private String XSZSYXDM;
    private String XSZSYXMC;
    private String XSZSZYDM;
    private String XSZSZYMC;
    private String ZSZSYXDM;
    private String ZSZSYXMC;
    private String ZSZSZYDM;
    private String ZSZSZYMC;
    private String BZ;
    private String NF;
}
