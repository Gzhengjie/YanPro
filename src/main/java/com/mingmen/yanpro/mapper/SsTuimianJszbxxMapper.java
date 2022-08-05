package com.mingmen.yanpro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingmen.yanpro.dao.SsTuimianJszbxx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SsTuimianJszbxxMapper extends BaseMapper<SsTuimianJszbxx> {
    int insert(SsTuimianJszbxx ssTuimianJszbxx);

    int myUpdate1(SsTuimianJszbxx ssTuimianJszbxx);

    @Select("SELECT yxsmc from base_college order by yxsdm")
    List<String> alljsyxsdm();

    int totalSutShzt(@Param("jsyxsdm") String jsyxsdm, @Param("jsyxsmc") String jsyxsmc, @Param("shzt") int shzt, @Param("nf") int nf);

}
