package com.mingmen.yanpro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.dao.BszyzsrsDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BszyzsrsMapper extends BaseMapper<BszyzsrsDao> {

    @Select("select * from bs_jianzhang_zyzsrs")
    List<BszyzsrsDao> selectAll();

    int insert(BszyzsrsDao bszyzsrsDao);

    int update(BszyzsrsDao bszyzsrsDao);

    @Delete("delete from bs_jianzhang_zyzsrs where YXSDM = #{YXSDM} and ZYDM = #{ZYDM}")
    Integer delete(@Param("YXSDM") String YXSDM, @Param("ZYDM") String ZYDM);

    @Select("select * from bs_jianzhang_zyzsrs limit #{pageNum}, #{pageSize}")
    List<BszyzsrsDao> selectPage(Integer pageNum, Integer pageSize);

    @Select("select count(*) from bs_jianzhang_zyzsrs")
    Integer selectTotal();

    @Select("select NZSRS,NF from bs_jianzhang_zyzsrs where ZYMC = #{ZYMC}")
    BszyzsrsDao selectByZYMC(@Param("ZYMC") String ZYMC);

    @Select("select YXSDM from base_college where YXSMC = #{YXSMC}")
    String selectByYXSMC(@Param("YXSMC") String YXSMC);

    @Delete("delete from bs_jianzhang_zyzsrs where ID = #{ID}")
    Integer deleteById(@Param("ID") Integer ID);

    IPage<BszyzsrsDao> findPage(Page<BszyzsrsDao> page, @Param("YXSDM") String yxsdm, @Param("YXSMC") String yxsmc, @Param("ZYDM") String zydm, @Param("ZYMC") String zymc, @Param("NF") String nf);

    int updateZyzsrsSHZT(BszyzsrsDao bszyzsrsDao);

    @Select("select bs_jianzhang_shzt.SHZT from bs_jianzhang_shzt,bs_jianzhang_zyzsrs where bs_jianzhang_zyzsrs.ID = #{ID} and bs_jianzhang_zyzsrs.NF = bs_jianzhang_shzt.NF and bs_jianzhang_zyzsrs.YXSDM = bs_jianzhang_shzt.YXSDM")
    String getshzt(@Param("ID") Integer id);

    @Select("select nf from bs_jianzhang_zyzsrs where ID = #{ID}")
    String getnf(@Param("ID") Integer id);

    @Select("select SHZT from bs_jianzhang_shzt where NF = #{nf} and YXSDM = #{yxsdm}")
    String getSCshzt(String yxsdm, String nf);

    @Select("select MIN(NF) from bs_jianzhang_zyzsrs where YXSDM = #{YXSDM}")
    String minYear(@Param("YXSDM") String yxsdm);

    @Select("select ID from bs_jianzhang_zyzsrs where YXSDM = #{YXSDM} and NF = #{NF}")
    List<Integer> selectIds(@Param("YXSDM")String yxsdm, @Param("NF")String nf);
    
    void updateXYShzt(BszyzsrsDao bszyzsrsDao);

    void updateYYShzt(BszyzsrsDao bszyzsrsDao);

    @Select("select ZSSL from bs_jianzhang_zzbfp where YXSDM = #{YXSDM} and NF = #{NF}")
    Integer getZyzsrs(@Param("YXSDM") String yxsdm, @Param("NF") String nf);

    @Select("select concat(ZYDM, ZYMC) from bs_jianzhang_zyzsrs where YXSDM = #{YXSDM} and NF = #{NF}")
    List<String> getzydmandmc(@Param("YXSDM") String yxsdm, @Param("NF") String nf);

    @Select("select count(*) from bs_jianzhang_zyzsrs where YXSDM = #{YXSDM} AND ZYDM = #{ZYDM} AND NF = #{NF}")
    Integer exist(BszyzsrsDao bszyzsrsDao);

    @Select("select count(*) from base_zhuanye_bs where YXSDM = #{YXSDM} and ZYDM = #{ZYDM} and ZYMC=#{ZYMC}")
    Integer matchZYDM(BszyzsrsDao bszyzsrsDao);

    @Select("select count(*) from bs_jianzhang_zzbfp where YXSDM = #{YXSDM} and ZSSL = #{NZSRS} and NF = #{NF}")
    Integer matchNZSRS(BszyzsrsDao bszyzsrsDao);
}
