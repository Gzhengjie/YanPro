package com.mingmen.yanpro.dao;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@TableName(value = "ss_jianzhang_shzt")
@ToString
public class SsJianzhangShzt {
    @TableId(type = IdType.AUTO)
    @Alias("自增ID（请忽略）")
    private Integer id;
    @Alias("院系所代码")
    private String yxsdm;
    @Alias("院系所名称")
    private String yxsmc;
    @Alias("审核状态")
    private String shzt;
    @Alias("年份")
    private String nf;
}
