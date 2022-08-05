package com.mingmen.yanpro.mapper;

import com.mingmen.yanpro.dao.SscskmDao;
import com.mingmen.yanpro.dao.YXSDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SscskmMapper {

    @Select("SELECT * FROM base_kskm_ss")
    List<SscskmDao> selectAll();

    @Select("SELECT * FROM base_kskm_ss WHERE KSKMMC = #{KSKMMC}")
    List<SscskmDao> selectByMC(String KSKMMC);

    @Insert("INSERT INTO base_kskm_ss(KSKMDM,KSKMMC,MTYXSDM,CKSM,KSFZ,KSSC,SFDHB,KSSM,SFTK,KSDY) VALUES(#{KSKMDM},#{KSKMMC},#{MTYXSDM},#{CKSM},#{KSFZ},#{KSSC},#{SFDHB},#{KSSM},#{SFTK},#{KSDY})")
    int insert(SscskmDao sscskmDao);

    int update(SscskmDao sscskmDao);

    @Delete("Delete from base_kskm_ss where ID = #{ID}")
    Integer deleteByID(@Param("ID") String ID);


    @Select("select * from base_kskm_ss limit #{pageNum}, #{pageSize}")
    List<SscskmDao> selectPage(Integer pageNum,Integer pageSize);

    @Select("select count(*) from base_kskm_ss")
    Integer selectTotal();
}
