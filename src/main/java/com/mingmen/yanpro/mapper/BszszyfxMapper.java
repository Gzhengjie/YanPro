package com.mingmen.yanpro.mapper;

import com.mingmen.yanpro.dao.BszszyDao;
import com.mingmen.yanpro.dao.BszszyfxDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BszszyfxMapper {

    @Select("SELECT * FROM base_yjfx_bs")
    List<BszszyfxDao> selectAll();

    @Select("SELECT * FROM base_yjfx_bs WHERE YJFXMC = #{YJFXMC}")
    List<BszszyfxDao> selectByMC(String YJFXMC);

    @Insert("INSERT INTO base_yjfx_bs(ZYDM,YJFXDM,YJFXMC,XXFS,ZSLX,BZ,NF,SHZT,SCR,SCSJ,XYSHR,XYSHSJ,YYSHR,YYSHSJ,GXSJ) " +
            "VALUES(#{ZYDM},#{YJFXDM},#{YJFXMC},#{XXFS},#{ZSLX},#{BZ},#{NF},#{SHZT},#{SCR},#{SCSJ},#{XYSHR},#{XYSHSJ},#{YYSHR},#{YYSHSJ},#{GXSJ})")
    int insert(BszszyfxDao bszszyfxDao);

    int update(BszszyfxDao bszszyfxDao);

    @Delete("Delete from base_yjfx_bs where ID = #{ID}")
    Integer deleteByYJFXDM(@Param("ID") String ID);


    @Select("select * from base_yjfx_bs limit #{pageNum}, #{pageSize}")
    List<BszszyfxDao> selectPage(Integer pageNum,Integer pageSize);

    @Select("select count(*) from base_yjfx_bs")
    Integer selectTotal();
}
