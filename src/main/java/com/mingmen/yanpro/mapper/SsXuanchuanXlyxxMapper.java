package com.mingmen.yanpro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.dao.Ksxxsh;
import com.mingmen.yanpro.dao.SsXuanchuanXlyxx;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SsXuanchuanXlyxxMapper extends BaseMapper<SsXuanchuanXlyxx> {

    @Select("SELECT * from ss_xuanchuan_xlyxx WHERE zjhm= #{zjhm}")
    List<SsXuanchuanXlyxx> findAll(@Param("zjhm") String zjhm);

    @Select("SELECT * FROM ss_xuanchuan_xlyxx where ID = #{ID}")
    SsXuanchuanXlyxx selectById(@Param("ID") Integer ID);

    int insert(SsXuanchuanXlyxx ssXuanchuanXlyxx);

    int update(SsXuanchuanXlyxx ssXuanchuanXlyxx);

    @Delete("DELETE FROM ss_xuanchuan_xlyxx WHERE id = #{id}")
    Integer deleteById(@Param("id") Integer id);

    @Select("SELECT yxsmc from base_college order by yxsdm")
    List<String> allyxsmc();

    int totalSutShzt(@Param("yxsdm") String yxsdm,@Param("yxsmc") String yxsmc, @Param("shzt") int shzt, @Param("nf") int nf);

    @Select("SELECT * from ss_xuanchuan_xlyxx")
    List<SsXuanchuanXlyxx> selectAll();

    IPage<SsXuanchuanXlyxx> findPage(Page<SsXuanchuanXlyxx> page,@Param("EMAIL") String EMALI,@Param("NF") Integer NF,@Param("YXSDM") String YXSDM,@Param("SHZT") Integer SHZT,@Param("XM") String XM,@Param("ZJHM") String ZJHM);
}
