package com.mingmen.yanpro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingmen.yanpro.dao.SsJianzhangZyds;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SsJianzhangZydsMapper extends BaseMapper<SsJianzhangZyds> {
    int insert(SsJianzhangZyds ssJianzhangZyds);

    int myUpdate(SsJianzhangZyds ssJianzhangZyds);

    @Select("SELECT DSXM FROM base_daoshi_ss WHERE XSZSYXDM = #{zsyxsdm} AND XSZSZYDM = #{zszydm} AND NF = #{nf}")
    List<String> getXSDS(String zsyxsdm, String zszydm, String nf);

    @Select("SELECT DSXM FROM base_daoshi_ss WHERE ZSZSYXDM = #{zsyxsdm} AND ZSZSZYDM = #{zszydm} AND NF = #{nf}")
    List<String> getZSDS(String zsyxsdm, String zszydm, String nf);
}
