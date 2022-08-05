package com.mingmen.yanpro.mapper;


import com.mingmen.yanpro.dao.SsXuanchuanYbmxx;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SsXuanchuanYbmxxMapper {

    @Select("SELECT * from ss_xuanchuan_ybmxx")
    List<SsXuanchuanYbmxx> findAll();



    @Insert("INSERT INTO `yandatabase`.`ss_xuanchuan_ybmxx` (`XM`, `XB`, `CSNY`, `MZ`, `ZZMM`, `YCZP`, `ZJHM`, " +
            "`LXDH`, `BYYX`, `BYYXS`, `BYZY`, `NSBZYDM`, `NSBZYMC`, `ZYDM` , `ZYMC`, `SGHZJS`, `QSNXJF`, `WYYZ`, `WYDJ`, `GRJS`,`SCCL`,`BZ`, `MM`) VALUES " +
            "(#{XM}, #{XB},#{CSNY},#{MZ},#{ZZMM},#{YCZP},#{ZJHM},#{LXDH},#{BYYX},#{BYYXS}, " +
            "#{BYZY},#{NSBZYDM},#{NSBZYMC},#{ZYDM},#{ZYMC},#{SGHZJS},#{QSNXJF},#{WYYZ},#{WYDJ},#{GRJS},#{SCCL},#{BZ},#{MM})")
    int insert(SsXuanchuanYbmxx ssXuanchuanYbmxx);


    int update(SsXuanchuanYbmxx ssXuanchuanYbmxx);

    @Delete("DELETE FROM ss_xuanchuan_ybmxx WHERE id = #{id}")
    Integer deleteById(@Param("id") Integer id);
}
