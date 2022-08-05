package com.mingmen.yanpro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.dao.Ksxxsh;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface KsxxshMapper extends BaseMapper<Ksxxsh> {

    @Select("SELECT * FROM ss_kscjsh")
    List<Ksxxsh> selectAll();

    @Select("SELECT ss_kscjsh.ID,KSBH, XM, ZJHM, DH, ZZLLM,t1.KSKMMC as ZZLLMC, YGYM,t2.KSKMMC as YGYMC,YWK1M,t3.KSKMMC as YWK1MC, YWK2M,t4.KSKMMC as YWK2MC, ZZLLS, YGYS, YWK1S, YWK2S, SQLY, SHJL, SHZT, ZZLLCJ, WGYCJ, YWK1CJ, YWK2CJ, SJSFNGSH, NF, SCR, SCSJ, SHR, SHSJ, GXSJ,ss_kscjsh.ZZLLM, ss_kscjsh.YGYM, ss_kscjsh.YWK1M, ss_kscjsh.YWK2M FROM ss_kscjsh LEFT JOIN base_kskm_ss t1 on ss_kscjsh.ZZLLM=t1.KSKMDM LEFT JOIN base_kskm_ss t2 on ss_kscjsh.YGYM=t2.KSKMDM LEFT JOIN base_kskm_ss t3 on ss_kscjsh.YWK1M=t3.KSKMDM LEFT JOIN base_kskm_ss t4 on ss_kscjsh.YWK2M=t4.KSKMDM where ss_kscjsh.ID = #{ID}")
    Ksxxsh selectById(@Param("ID") Integer ID);

    @Select("SELECT NF from ss_kscjsh GROUP BY NF")
    List<Integer> allNF();

    @Select("SELECT KSKMDM from base_kskm_ss GROUP BY KSKMDM")
    List<Integer> allKSKMDM();

    IPage<Ksxxsh> findPage(Page<Ksxxsh> page,@Param("NF") Integer NF,@Param("SHZT") Integer SHZT, @Param("KSBH") Long KSBH, @Param("XM") String XM, @Param("ZJHM") String ZJHM);

    @Select("SELECT * FROM ss_kscjsh where SHZT = 1")
    List<Ksxxsh> selectModified();

    @Select("SELECT * FROM ss_kscjsh where KSBH = #{KSBH} AND XM = #{XM} AND ZJHM = #{ZJHM}")
    Ksxxsh selectOne(@Param("KSBH") Long KSBH,@Param("XM") String XM,@Param("ZJHM") String ZJHM);

    @Select("SELECT * FROM ss_kscjsh where SHZT = 1 AND KSBH = #{KSBH}")
    List<Ksxxsh> selectModifiedByKsbh(@Param("KSBH") Long KSBH);

    @Insert("INSERT INTO ss_kscjsh(KSBH, XM, ZJHM, DH, ZZLLM, YGYM, YWK1M, YWK2M, SQLY, SHJL, SHZT, ZZLLCJ, WGYCJ, YWK1CJ, YWK2CJ, SJSFNGSH, NF, SCR,SCSJ,SHR,SHSJ) VALUES(#{KSBH},#{XM},#{ZJHM},#{DH},#{ZZLLM},#{YGYM},#{YWK1M},#{YWK2M},#{SQLY},#{SHJL},#{SHZT},#{ZZLLCJ},#{WGYCJ},#{YWK1CJ},#{YWK2CJ},#{SJSFNGSH},#{NF},#{SCR},#{SCSJ},#{SHR},#{SHSJ})")
    int insert(Ksxxsh ksxxsh);

    int update(Ksxxsh ksxxsh);

    @Delete("Delete from ss_kscjsh where KSBH = #{KSBH}")
    Integer deleteByKsbh(@Param("KSBH") Long KSBH);

    @Select("select SFYXTJ from base_cltjkg WHERE CLMC ='考生初试成绩' ")
    String getSFYXTJ();

}
