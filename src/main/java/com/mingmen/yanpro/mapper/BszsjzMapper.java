package com.mingmen.yanpro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.dao.BsjianzhangDao;
import com.mingmen.yanpro.dao.BszsjzDao;
import com.mingmen.yanpro.dao.BszyzsrsDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BszsjzMapper extends BaseMapper<BszsjzDao> {

    @Select("select * from bs_jianzhang_zsjz")
    List<BszsjzDao> selectAll();

    int insert(BszsjzDao bszsjzDao);

    int update(BszsjzDao bszsjzDao);

    @Delete("delete from bs_jianzhang_zsjz where YXSDM = #{YXSDM} and YJFXDM = #{YJFXDM}")
    Integer delete(@Param("YXSDM") String YXSDM, @Param("YJFXDM") String YJFXDM);

    @Select("select * from bs_jianzhang_zsjz limit #{pageNum}, #{pageSize}")
    List<BszsjzDao> selectPage(Integer pageNum, Integer pageSize);

    @Select("select count(*) from bs_jianzhang_zsjz")
    Integer selectTotal();

    @Select("select YJFXDM,YJFXMC,ZDJS,KSKM,LXDH,NF,BZ from bs_jianzhang_zsjz where YJFXDM = #{YJFXDM}")
    BszsjzDao selectByYJFXDM(@Param("YJFXDM") String YJFXDM);

    IPage<BszsjzDao> findPage(Page<BszsjzDao> page, @Param("YXSDM") String yxsdm, @Param("YXSMC") String yxsmc, @Param("ZYDM") String zydm, @Param("ZYMC") String zymc, @Param("YJFXDM") String yjfxdm, @Param("YJFXMC") String yjfxmc, @Param("ZDJS") String zdjs, @Param("KSKM") String kskm, @Param("NF") String nf);

    @Delete("delete from bs_jianzhang_zsjz where ID = #{ID}")
    Integer deleteById(@Param("ID") Integer id);

    @Select("select base_yjfx_bs.YJFXMC FROM base_zhuanye_bs,base_yjfx_bs WHERE base_zhuanye_bs.YXSDM = #{YXSDM} AND base_zhuanye_bs.ZYDM = base_yjfx_bs.ZYDM AND base_zhuanye_bs.ZYMC = #{ZYMC}")
    List<String> yjfxlist(@Param("ZYMC") String zymc,@Param("YXSDM") String yxsdm);

    @Select("select MIN(NF) from bs_jianzhang_zsjz where YXSDM = #{YXSDM}")
    String minYear(@Param("YXSDM") String yxsdm);

    @Select("select ZYDM from base_zhuanye_bs where YXSDM = #{YXSDM}")
    List<String> allZydm(@Param("YXSDM") String yxsdm);

    @Select("select ZYMC from base_zhuanye_bs where ZYDM = #{ZYDM}")
    String getZYMC(@Param("ZYDM") String zydm);

    @Select("select ZYMC from base_zhuanye_bs where YXSDM = #{YXSDM}")
    List<String> allZymc(@Param("YXSDM") String yxsdm);

    @Select("select YJFXDM FROM base_yjfx_bs where ZYDM = #{ZYDM}")
    List<String> getYJFXDM(@Param("ZYDM") String zydm);

    @Select("select YJFXMC from base_yjfx_bs where YJFXDM = #{YJFXDM} and ZYDM = #{ZYDM}")
    String getYJFXMC(@Param("YJFXDM") String yjfxdm,@Param("ZYDM") String zydm);

    @Select("select SFYXTJ from base_cltjkg where CLMC = '博士简章编制'")
    String getSFYXTJ();

    @Select("select bs_jianzhang_shzt.SHZT from bs_jianzhang_shzt,bs_jianzhang_zsjz where bs_jianzhang_zsjz.ID = #{ID} and bs_jianzhang_zsjz.NF = bs_jianzhang_shzt.NF and bs_jianzhang_zsjz.YXSDM = bs_jianzhang_shzt.YXSDM")
    String getshzt(@Param("ID") Integer id);

    @Select("select nf from bs_jianzhang_zsjz where ID = #{ID}")
    String getnf(@Param("ID") Integer id);

    @Select("select base_yjfx_bs.YJFXMC from base_yjfx_bs,base_zhuanye_bs where base_zhuanye_bs.YXSDM = #{YXSDM} and base_yjfx_bs.ZYDM = base_zhuanye_bs.ZYDM")
    List<String> getAllYJFXMC(@Param("YXSDM") String yxsdm);

    @Select("select DSXM from base_daoshi_bs where SZYXDM = #{YXSDM} and (XBZSZYDM = #{ZYDM} or ZBZSZYDM = #{ZYDM})")
    List<String> getAllZDJS(@Param("ZYDM") String zydm,@Param("YXSDM") String yxsdm);

    @Select("select concat(YJFXDM,YJFXMC) from bs_jianzhang_zsjz where YXSDM = #{YXSDM} and NF = #{NF}")
    List<String> getyjfxdmandmc(@Param("YXSDM") String yxsdm, @Param("NF") String nf);

    void updateXYShzt(BszyzsrsDao bszyzsrsDao);

    void updateYYShzt(BszyzsrsDao bszyzsrsDao);

    @Select("select ID from bs_jianzhang_zsjz where YXSDM = #{YXSDM} and NF = #{NF}")
    List<Integer> selectIds(@Param("YXSDM") String yxsdm, @Param("NF") String nf);

    @Select("select YXSMC from base_college where YXSDM = #{YXSDM}")
    String getyxsdmandmc(@Param("YXSDM") String yxsdm);

    @Select("select YXSMC from base_college")
    List<String> getAllYXSMC();

    List<BsjianzhangDao> pjJZBZ(@Param("YXSDM") String yxsdm, @Param("NF") String nf);

    @Select("select SHZT from bs_jianzhang_shzt where YXSDM = #{YXSDM} and NF = #{NF}")
    String getXZshzt(@Param("YXSDM") String yxsdm, @Param("NF") String nf);

    @Select("select count(*) from bs_jianzhang_zsjz where YXSDM = #{YXSDM} and ZYDM = #{ZYDM} and YJFXDM = #{YJFXDM} and NF = #{NF}")
    Integer exist(BszsjzDao bszsjzDao);

    @Select("select count(*) from base_yjfx_bs where YJFXDM = #{YJFXDM} and YJFXMC = #{YJFXMC}")
    Integer match(BszsjzDao bszsjzDao);

//    @Select("select ")
//    List<String> getzydmandmc(String yxsdm, String nf);
}
