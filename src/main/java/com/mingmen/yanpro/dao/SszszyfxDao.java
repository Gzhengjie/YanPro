package com.mingmen.yanpro.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("base_yjfx_ss")
public class SszszyfxDao {
    private Integer ID;
    private String ZYDM;
    private String ZYMC;
    private String YJFXDM;
    private String YJFXMC;
    private String XXFS;
    private String ZSLX;
    private String BZ;
    private String NF;
}
