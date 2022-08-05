package com.mingmen.yanpro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingmen.yanpro.dao.SsJianzhangZszyrs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SsJianzhangZszyrsMapper extends BaseMapper<SsJianzhangZszyrs> {
    int insert(SsJianzhangZszyrs ssJianzhangZszyrs);

    int myUpdate(SsJianzhangZszyrs ssJianzhangZszyrs);


    @Select("SELECT CONCAT(ZYDM, ZYMC, '（', NZSRS, '人）') FROM ss_jianzhang_zszyrs WHERE YXSDM = #{yxsdm} AND NF = #{nf} AND YJFXDM = 'JH'")
    List<String> get_zydm_zymc_rs(String yxsdm, String nf);

    @Select("SELECT ZSZRS FROM ss_jianzhang_zzbfp WHERE YXSDM = #{yxsdm} AND NF = #{nf}")
    Object getZszrs(String yxsdm, String nf);

    @Select("SELECT SUM(NZSRS) FROM ss_jianzhang_zszyrs WHERE YXSDM = #{yxsdm} AND NF = #{nf}")
    Object getXyZszrs(String yxsdm, String nf);

    @Select("SELECT TMSL FROM ss_jianzhang_zzbfp WHERE YXSDM = #{yxsdm} AND NF = #{nf}")
    Object getYyTmrs(String yxsdm, String nf);

    @Select("SELECT SUM(JSTMRS) FROM ss_jianzhang_zszyrs WHERE YXSDM = #{yxsdm} AND NF = #{nf}")
    Object getXyTmrs(String yxsdm, String nf);

    @Select("SELECT SUM(GKZKRS) FROM ss_jianzhang_zszyrs WHERE YXSDM = #{yxsdm} AND NF = #{nf}")
    Object getXyGkzkrs(String yxsdm, String nf);



    @Select("SELECT SUM(JSTMRS) FROM ss_jianzhang_zszyrs WHERE YXSDM = #{yxsdm} AND ZYDM = #{zydm} AND NF = #{nf}")
    Object getZytmrs(String yxsdm, String zydm, String nf);

    @Select("SELECT SUM(GKZKRS) FROM ss_jianzhang_zszyrs WHERE YXSDM = #{yxsdm} AND ZYDM = #{zydm} AND NF = #{nf}")
    Object getZyzkrs(String yxsdm, String zydm, String nf);
}
