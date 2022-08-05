package com.mingmen.yanpro.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("base_zhuanye_ss")
public class SszszyDao {
    private Integer ID;
    private String YXSDM;
    private String YXSMC;
    //@JsonIgnore //忽略该字段，不给前端展示该字段
    private String ZYDM;
    private String ZYMC;
    private String BZ;
    private String NF;
}
