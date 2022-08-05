package com.mingmen.yanpro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.dao.BsjianzhangshztDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BsjianzhangshztMapper extends BaseMapper<BsjianzhangshztDao> {

    @Select("select * from bs_jianzhang_shzt")
    List<BsjianzhangshztDao> selectAll();

    int insert(BsjianzhangshztDao bsjianzhangshztDao);

    int update(BsjianzhangshztDao bsjianzhangshztDao);

    IPage<BsjianzhangshztDao> findPage(Page<BsjianzhangshztDao> page, @Param("YXSMC") String yxsmc, @Param("NF") String nf);

    @Select("select SHZT from bs_jianzhang_shzt where YXSDM = #{YXSDM} and NF = #{NF}")
    String getSHZT(@Param("YXSDM") String yxsdm, @Param("NF") String nf);

    @Select("select MIN(NF) from bs_jianzhang_shzt where YXSDM = #{YXSDM}")
    String minYear(@Param("YXSDM") String yxsdm);

    @Select("select MIN(NF) from bs_jianzhang_shzt")
    String allJZminyear();
}
