package com.mingmen.yanpro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingmen.yanpro.dao.BszszyfxDao;
import com.mingmen.yanpro.dao.SszszyfxDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SszszyfxMapper extends BaseMapper<SszszyfxDao> {

    @Select("SELECT * FROM base_yjfx_ss")
    List<SszszyfxDao> selectAll();

    @Select("SELECT * FROM base_yjfx_ss WHERE YJFXMC = #{YJFXMC}")
    List<SszszyfxDao> selectByMC(String YJFXMC);

    @Insert("INSERT INTO base_yjfx_ss(ZYDM,ZYMC,YJFXDM,YJFXMC,XXFS,ZSLX,BZ,NF) VALUES(#{ZYDM},#{ZYMC},#{YJFXDM},#{YJFXMC},#{XXFS},#{ZSLX},#{BZ},#{NF})")
    int insert(SszszyfxDao sszszyfxDao);

    int update(SszszyfxDao sszszyfxDao);

    @Delete("Delete from base_yjfx_ss where ID = #{ID}")
    Integer deleteByID(@Param("ID") String ID);


    @Select("select * from base_yjfx_ss limit #{pageNum}, #{pageSize}")
    List<SszszyfxDao> selectPage(Integer pageNum,Integer pageSize);

    @Select("select count(*) from base_yjfx_ss")
    Integer selectTotal();
}
