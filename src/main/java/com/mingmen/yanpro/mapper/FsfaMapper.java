package com.mingmen.yanpro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.dao.Fsfa;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//@Mapper
public interface FsfaMapper extends BaseMapper<Fsfa> {

    @Select("SELECT * FROM ss_fushi_fangan")
    List<Fsfa> selectAll();

    @Select("SELECT WJM,YXDM,YXSMC,NF,SHZT,SCR,SCSJ,XYSHR,XYSHSJ,YYSHR,YYSHSJ,GXSJ FROM ss_fushi_fangan where ID = #{ID}")
    Fsfa selectById(@Param("ID") Integer ID);

    @Select("SELECT WJM,YXDM,YXSMC,NF,FSFA,SHZT,SCR,SCSJ,SHR,SHSJ,GXSJ FROM ss_fushi_fangan")
    List<Fsfa> selectAllNoId();

    @Select("SELECT * FROM ss_fushi_fangan where WJM = #{WJM}")
    List<Fsfa> selectByWjm(@Param("WJM") String WJM);

    @Insert("INSERT INTO ss_fushi_fangan(WJM,YXDM,YXSMC,NF,FSFA,SHZT,SCR,SCSJ,XYSHR,XYSHSJ,YYSHR,YYSHSJ) VALUES(#{WJM},#{YXDM},#{YXSMC},#{NF},#{FSFA},#{SHZT},#{SCR},#{SCSJ},#{XYSHR},#{XYSHSJ},#{YYSHR},#{YYSHSJ})")
    int insert(Fsfa fsfa);

    int update(Fsfa fsfa);

    @Delete("Delete from ss_fushi_fangan where id = #{id}")
    Integer deleteById(@Param("id") Integer id);

    @Select("SELECT YXSMC from base_college GROUP BY YXSMC ")
    List<String> allYXSMC1();

    @Select("SELECT YXSMC from base_college WHERE YXSDM = #{YXSDM}")
    List<String> allYXSMC(@Param("YXSDM") String YXSDM);

    @Select("SELECT NF from ss_fushi_fangan GROUP BY NF")
    List<Integer> allNF();

//    @Select("select * from ss_fushi_fangan limit #{pageNum}, #{pageSize}")
//    List<Fsfa> selectPage(Integer pageNum,Integer pageSize);

    IPage<Fsfa> findPage(Page<Fsfa> page,@Param("NF") Integer NF,@Param("SHZT") Integer SHZT, @Param("WJM") String WJM, @Param("YXSDM") String YXSDM,@Param("YXSMC") String YXSMC);

    @Select("select count(*) from ss_fushi_fangan")
    Integer selectTotal();

    @Select("select SFYXTJ from base_cltjkg WHERE CLMC ='复试方案' ")
    String getSFYXTJ();

    int totalSutShzt(@Param("YXSDM") String YXSDM,@Param("YXSMC") String YXSMC, @Param("SHZT") int SHZT, @Param("NF") int NF);
}
