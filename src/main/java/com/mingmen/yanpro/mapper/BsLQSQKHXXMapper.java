package com.mingmen.yanpro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingmen.yanpro.dao.BsLQSQKHXX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BsLQSQKHXXMapper extends BaseMapper<BsLQSQKHXX> {
    int totalSutShzt(@Param("lqyxdm") String lqyxdm, @Param("lqyxmc") String lqyxmc, @Param("shzt") int shzt, @Param("nf") int nf);

    @Select("select sfyxtj from base_cltjkg where clmc = #{clmc}")
    boolean allow(@Param("clmc") String clmc);
}
