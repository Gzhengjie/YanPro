package com.mingmen.yanpro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingmen.yanpro.dao.SsJianzhangShzt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SsJianzhangShztMapper extends BaseMapper<SsJianzhangShzt> {

    //int insert(SsJianzhangShzt ssJianzhangShzt);

//    @Select("SELECT YXSMC FROM base_college WHERE YXSDM = #{yxsdm} LIMIT 1")
//    String getYXSMCByYXSDM(String yxsdm);

    void nextYearShzt(String yxsdm, String yxsmc, String nf, String shzt);
}
