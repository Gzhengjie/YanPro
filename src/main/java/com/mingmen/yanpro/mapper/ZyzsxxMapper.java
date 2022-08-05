package com.mingmen.yanpro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.dao.Zyzsxx;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ZyzsxxMapper extends BaseMapper<Zyzsxx> {

    @Select("SELECT * FROM ss_fushi_zyxx")
    List<Zyzsxx> selectAll();

    @Select("SELECT * FROM ss_fushi_zyxx where ID = #{ID}")
    Zyzsxx selectById(@Param("ID") Integer ID);

    @Select("SELECT YXSMC from base_college GROUP BY YXSMC ")
    List<String> allYXSMC1();

    @Select("SELECT YXSMC from base_college WHERE YXSDM = #{YXSDM}")
    List<String> allYXSMC(@Param("YXSDM") String YXSDM);

    @Select("SELECT ZYMC from base_zhuanye_ss WHERE YXSMC = #{YXSMC} AND NF = #{NF}")
    List<String> allZYMC(@Param("YXSMC") String YXSMC,@Param("NF") Integer NF);

    @Select("SELECT ZYDM from base_zhuanye_ss WHERE ZYMC = #{ZYMC} AND NF = #{NF}")
    List<String> allZYDM(@Param("ZYMC") String ZYMC,@Param("NF") Integer NF);

    @Select("SELECT XXFS from base_yjfx_ss WHERE ZYMC = #{ZYMC} AND NF = #{NF} GROUP BY XXFS")
    List<String> allXXFS(@Param("ZYMC") String ZYMC,@Param("NF") Integer NF);

    @Select("SELECT CONCAT(YJFXDM,YJFXMC) AS YJFX from base_yjfx_ss WHERE ZYDM = #{ZYDM} AND NF = #{NF} ORDER BY YJFXDM")
    List<String> allYJFX(@Param("ZYDM") String ZYDM,@Param("NF") Integer NF);

    @Select("SELECT NF from ss_fushi_zyxx GROUP BY NF")
    List<Integer> allNF();

    IPage<Zyzsxx> findPage(Page<Zyzsxx> page,@Param("NF") Integer NF,@Param("SHZT") Integer SHZT,@Param("YXSDM") String YXSDM, @Param("YXSMC") String YXSMC, @Param("ZYMC") String ZYMC);

    @Select("SELECT * FROM ss_fushi_zyxx where YXSMC = #{YXSMC}")
    List<Zyzsxx> selectByYxsmc(@Param("YXSMC") String YXSMC);

    @Insert("INSERT INTO ss_fushi_zyxx(YXSDM,YXSMC,ZYDM,ZYMC,YJFX,XXFS,JHZS,YJSTMS,GKZKZS,FSBL,SFXYTJ,LXRJDH,DJYQ,BZ,SHZT,NF,SCR,SCSJ,XYSHR,XYSHSJ,YYSHR,YYSHSJ) VALUES(#{YXSDM},#{YXSMC},#{ZYDM},#{ZYMC},#{YJFX},#{XXFS},#{JHZS},#{YJSTMS},#{GKZKZS},#{FSBL},#{SFXYTJ},#{LXRJDH},#{DJYQ},#{BZ},#{SHZT},#{NF},#{SCR},#{SCSJ},#{XYSHR},#{XYSHSJ},#{YYSHR},#{YYSHSJ})")
    int insert(Zyzsxx zyzsxx);

    int update(Zyzsxx zyzsxx);

    @Delete("Delete from ss_fushi_zyxx where id = #{id}")
    Integer deleteById(@Param("id") Integer id);

    @Select("select * from ss_fushi_zyxx limit #{pageNum}, #{pageSize}")
    List<Zyzsxx> selectPage(Integer pageNum,Integer pageSize);

    @Select("select count(*) from ss_fushi_zyxx")
    Integer selectTotal();

    @Select("select SFYXTJ from base_cltjkg WHERE CLMC ='各专业复试信息' ")
    String getSFYXTJ();

    int totalSutShzt(@Param("YXSDM") String YXSDM,@Param("YXSMC") String YXSMC, @Param("SHZT") int SHZT, @Param("NF") int NF);
}
