package com.mingmen.yanpro.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class YXSDao {
    private Integer ID;
    private String YXSDM;
    //@JsonIgnore //忽略该字段，不给前端展示该字段
    private String YXSMC;
    private String BZ;
}
