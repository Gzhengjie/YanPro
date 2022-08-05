package com.mingmen.yanpro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingmen.yanpro.dao.BsLQSBLDXX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BsLQSBLDXXMapper extends BaseMapper<BsLQSBLDXX> {

    @Select("SELECT * from ss_luqu_xxhuizong")
    List<BsLQSBLDXX> selectAll();

    @Select("SELECT yxsmc from base_college order by yxsdm")
    List<String> alllqyxmc();

    List<String> alllqzymc(@Param("yxsdm") String yxsdm, @Param("nf") int nf);

    @Select("SELECT XXFS from base_yjfx_bs where zymc = #{zymc} group by xxfs")
    List<String> allxxfs(@Param("zymc") String zymc);

    int totalSutShzt(@Param("lqyxdm") String lqyxdm,@Param("lqyxmc") String lqyxmc, @Param("shzt") int shzt, @Param("nf") int nf);

    @Select("select sfyxtj from base_cltjkg where clmc = #{clmc}")
    boolean allow(@Param("clmc") String clmc);
}
