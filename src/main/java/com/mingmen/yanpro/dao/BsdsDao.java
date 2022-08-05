package com.mingmen.yanpro.dao;

import lombok.Data;

@Data
public class BsdsDao {
    private Integer ID;
    private String ZGH;
    //@JsonIgnore //忽略该字段，不给前端展示该字段
    private String DSXM;
    private String SZYXDM;
    private String SZYXMC;
    private String ZC;
    private String XBZSYXDM;
    private String XBZSYXMC;
    private String XBZSZYDM;
    private String XBZSZYMC;
    private String ZBZSYXDM;
    private String ZBZSYXMC;
    private String ZBZSZYDM;
    private String ZBZSZYMC;
    private String BZ;
    private String NF;
}
