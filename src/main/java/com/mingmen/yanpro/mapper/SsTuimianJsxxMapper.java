package com.mingmen.yanpro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.dao.SsTuimianJsxx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SsTuimianJsxxMapper extends BaseMapper<SsTuimianJsxx> {
//    @Select("SELECT * from tuimian_jsxx")
//    List<SsTuimianJsxx> findAll();

//    @Insert("INSERT INTO `yandatabase`.`tuimian_jsxx` (`XM`, `XB`, `TJYXSDM`, `TJYXSMC`, `BYZYDM`, `BYZYMC`, " +
//            "`JSYXSDM`, `JSYXSMC`, `NGDZYDM`, `NGDZYMC`, `DSXM`, `DSSFBD`, `JSLXDM`, `JSLXMC`, `FSZCJ`, `FSZCJPM`, " +
//            "`WYDJ`, `JSLBDM`, `JSLBMC`, `TMSLY`, `CJXLY`, `BZ`, `NF`, `SHZT`, `SCR`, `SCSJ`, `SHR`, `SHSJ`, `GXSJ`) " +
//            "VALUES (#{XM},#{XB},#{TJYXSDM}, #{TJYXSMC}, #{BYZYDM}, #{BYZYMC}, #{JSYXSDM}, #{JSYXSMC}, #{NGDZYDM}, " +
//            "#{NGDZYMC}, #{DSXM},#{DSSFBD} , #{JSLXDM},#{JSLXMC},#{FSZCJ}, #{FSZCJPM}, #{WYDJ}, #{JSLBDM}, #{JSLBMC}, #{TMSLY}, #{CJXLY},#{BZ}," +
//            "#{NF},#{SHZT}, #{SCR},#{SCSJ}, #{SHR},#{SHSJ} ,#{GXSJ})")
    int insert(SsTuimianJsxx ssTuimianJsxx);

    int myUpdate(SsTuimianJsxx ssTuimianJsxx);

    List<SsTuimianJsxx> selectAll();

    IPage<SsTuimianJsxx> findPage(Page<SsTuimianJsxx> page,@Param("tjyxsdm") String tjyxsdm,
                                  @Param("tjyxsmc")String tjyxsmc, @Param("dsxm") String dsxm,
                                  @Param("nf") int nf, @Param("shzt") int shzt);

    @Select("SELECT yxsmc from base_college order by yxsdm")
    List<String> alltjyxsmc();

    int totalSutShzt(@Param("tjyxsdm") String tjyxsdm,@Param("tjyxsmc") String tjyxsmc, @Param("shzt") int shzt, @Param("nf") int nf);
}
