package com.mingmen.yanpro.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingmen.yanpro.dao.SsJianzhangPinjie;
import com.mingmen.yanpro.dao.SsJianzhangZykskm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SsjianzhangZykskmMapper extends BaseMapper<SsJianzhangZykskm> {


    int insert(SsJianzhangZykskm ssJianzhangZykskm);

    int myUpdate(SsJianzhangZykskm ssJianzhangZykskm);

    @Select("SELECT KSKMMC FROM base_kskm_ss WHERE KSKMDM = #{kmdm}")
    String getKMMC(String kmdm);

    @Select("SELECT ZYDM FROM base_zhuanye_ss WHERE YXSDM = #{yxsdm} AND NF = #{nf}")
    List<String> getZYDM(String yxsdm, String nf);

    @Select("SELECT ZYMC FROM base_zhuanye_ss WHERE ZYDM = #{zydm} AND NF = #{nf} LIMIT 1")
    String getZYMC(String zydm, String nf);

    @Select("SELECT YJFXDM FROM base_yjfx_ss WHERE ZYMC = #{zymc} AND NF = #{nf}")
    List<String> getYJFXDM(String zymc, String nf);

    @Select("SELECT YJFXMC FROM base_yjfx_ss WHERE ZYMC = #{zymc} AND YJFXDM = #{yjfxdm} AND NF = #{nf}")
    String getYJFXMC(String yjfxdm, String zymc, String nf);

    @Select("SELECT KSKMDM FROM base_kskm_ss WHERE KSDY = #{ksdy}")
    List<String> getKSKMDM(String ksdy);

    @Select("SELECT YXSMC FROM base_college")
    List<String> getAllCollege();

    @Select("SELECT YXSMC FROM base_college WHERE YXSDM = #{yxsdm}")
    String yxsdm2yxsmc(String yxsdm);

    @Select("SELECT SFYXTJ FROM base_cltjkg WHERE CLMC = '硕士简章编制'")
    String getSFYXTJ();



    @Select("SELECT\n" +
            "\tconcat( ss_jianzhang_pinjie.YXSDM, ss_jianzhang_pinjie.YXSMC ) AS yxs, \n" +
            "\tconcat( ss_jianzhang_pinjie.ZYDM, ss_jianzhang_pinjie.ZYMC) AS zy, \n" +
            "\tconcat( ss_jianzhang_pinjie.YJFXDM, ss_jianzhang_pinjie.YJFXMC ) AS yjfx, \n" +
            "\tss_jianzhang_pinjie.ZDJS AS zdjs, \n" +
            "\tCONCAT(\n" +
            "\t\t'①',\n" +
            "\t\tss_jianzhang_pinjie.ZZLLM,\n" +
            "\t\tzzll.KSKMMC,\n" +
            "\t\t'\\n',\n" +
            "\t\t'②',\n" +
            "\t\tss_jianzhang_pinjie.WGYM,\n" +
            "\t\twgy.KSKMMC,\n" +
            "\t\t'\\n',\n" +
            "\t\t'③',\n" +
            "\t\tss_jianzhang_pinjie.YWK1M,\n" +
            "\t\tywk1.KSKMMC,\n" +
            "\t\t'\\n',\n" +
            "\t\t'④',\n" +
            "\t\tss_jianzhang_pinjie.YWK2M,\n" +
            "\t\tywk2.KSKMMC \n" +
            "\t) AS kskm, \n" +
            "\tss_jianzhang_pinjie.FSBSKM AS fsbskm, \n" +
            "\tss_jianzhang_pinjie.XXFS AS xxfs, \n" +
            "\tss_jianzhang_pinjie.ZSLX AS zslx, \n" +
            "\tss_jianzhang_pinjie.RSSM\n" +
            "FROM\n" +
            "\tss_jianzhang_pinjie\n" +
            "\tINNER JOIN\n" +
            "\tbase_kskm_ss AS zzll\n" +
            "\tON \n" +
            "\t\tss_jianzhang_pinjie.ZZLLM = zzll.KSKMDM\n" +
            "\tINNER JOIN\n" +
            "\tbase_kskm_ss AS wgy\n" +
            "\tON \n" +
            "\t\tss_jianzhang_pinjie.WGYM = wgy.KSKMDM\n" +
            "\tINNER JOIN\n" +
            "\tbase_kskm_ss AS ywk1\n" +
            "\tON \n" +
            "\t\tss_jianzhang_pinjie.YWK1M = ywk1.KSKMDM\n" +
            "\tINNER JOIN\n" +
            "\tbase_kskm_ss AS ywk2\n" +
            "\tON \n" +
            "\t\tss_jianzhang_pinjie.YWK2M = ywk2.KSKMDM\n" +
            "WHERE\n" +
            "\tss_jianzhang_pinjie.YXSDM = #{yxsdm} AND\n" +
            "\tss_jianzhang_pinjie.NF = #{nf}\n" +
            "ORDER BY\n" +
            "\tss_jianzhang_pinjie.ZYDM ASC, \n" +
            "\tss_jianzhang_pinjie.YJFXDM ASC")
    List<SsJianzhangPinjie> jianZhangPinJie(String yxsdm, String nf);

    @Select("SELECT YJFXDM FROM ss_zy_fx WHERE YXSDM = #{yxsdm} AND NF = #{nf}")
    List<String> getCollegeYJFXDM(String yxsdm, String nf);
}
