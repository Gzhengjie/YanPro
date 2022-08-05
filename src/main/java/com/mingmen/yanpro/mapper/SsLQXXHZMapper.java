package com.mingmen.yanpro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.dao.SsLQXXHZ;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
@Mapper
public interface SsLQXXHZMapper extends BaseMapper<SsLQXXHZ> {

    List<SsLQXXHZ> selectAll(@Param("lqyxdm") String lqyxdm, @Param("lqyxmc") String lqyxmc, @Param("lqzymc") String lqzymc, @Param("nf") int nf, @Param("shzt") int shzt);

    @Select("SELECT yxsmc from base_college order by yxsdm")
    List<String> alllqyxmc();

    @Select("SELECT zymc from base_zhuanye_ss where yxsdm = #{lqyxdm} and nf = #{nf} GROUP by zymc")
    List<String> alllqzymc(@Param("lqyxdm") String lqyxdm, @Param("nf") int nf);

    IPage<SsLQXXHZ> findPage(Page<SsLQXXHZ> page,@Param("lqyxdm") String lqyxdm, @Param("lqyxmc") String lqyxmc, @Param("lqzymc") String lqzymc, @Param("xxfs") String xxfs, @Param("zxjh") String zxjh, @Param("ksbh") String ksbh, @Param("ksxm") String ksxm, @Param("nf") int nf, @Param("shzt") int shzt);

    @Select("SELECT tgxmmc from base_wydj where tgxmdm = #{tgxmdm}")
    String findtgxmmc(@Param("tgxmdm") String tgxmdm);

    @Select("SELECT lbmc from base_nlqlb where lbdm = #{nlqlb}")
    String findnlqlmc(@Param("nlqlb") String nlqlb);

    @Select("SELECT zxjhmc from base_gjzxjh where zxjhdm = #{zxjhdm}")
    String findzxjhmc(@Param("zxjhdm") String zxjhdm);

    @Select("SELECT xmmc from base_zyxwzx where xmdm = #{xmdm}")
    String findxmc(@Param("xmdm") String xmdm);


    @Select("SELECT tgxmdm from base_wydj where tgxmmc = #{tgxmmc}")
    String findtgxmdm(@Param("tgxmmc") String tgxmmc);

    @Select("SELECT lbdm from base_nlqlb where lbmc = #{nlqlb}")
    String findnlqlbm(@Param("nlqlb") String nlqlb);

    @Select("SELECT zxjhdm from base_gjzxjh where zxjhmc = #{zxjhmc}")
    String findzxjh(@Param("zxjhmc") String zxjhmc);

    @Select("SELECT xmdm from base_zyxwzx where xmmc = #{xmmc}")
    String findxxm(@Param("xmmc") String xmmc);

    @Select("SELECT XXFS from base_yjfx_ss where zymc = #{zymc} group by xxfs")
    List<String> allxxfs(@Param("zymc") String zymc);

    @Select("SELECT zxjhdm,zxjhmc from base_gjzxjh")
    List<HashMap<String, String>> allZxjh();

    int totalStudent(@Param("lqyxdm") String lqyxdm);

    int totalSutShzt(@Param("lqyxdm") String lqyxdm,@Param("lqyxmc") String lqyxmc, @Param("shzt") int shzt, @Param("nf") int nf);

    @Select("select tgxmmc from base_wydj")
    List<String> allTgxmmc();

    @Select("select xmmc from base_zyxwzx")
    List<String> allXmm();

    @Select("select lbmc from base_nlqlb")
    List<String> allNlqlb();

    @Select("select zydm from base_zhuanye_ss where zymc = #{lqzymc}")
    String findLqzydm(String lqzymc);

    @Select("select sfyxtj from base_cltjkg where clmc = #{clmc}")
    boolean allow(@Param("clmc") String clmc);
//    void saveOne(SsLQXXHZ one);

//
//    @Insert("INSERT into sys_user(lqyxdm, lqyxmc,  lqzydm, lqzymc, xxfs, ksxm,ksbh,dsxm,tjbj,cszcj,bscj,mscj, fscj,zcj,cjpm,tgxmdm, nlqlbm,zxjh,xmm,dxjydwdm,szdm,sfdd,bz) " +
//            "VALUES (#{lqyxdm},#{lqyxmc},#{lqzydm},#{lqzymc},#{xxfs},#{ksxm},#{ksbh},#{dsxm},#{tjbj},#{cszcj},#{bscj},#{mscj},#{fscj},#{zcj},#{cjpm},#{tgxmdm},#{nlqlbm},#{zxjh},#{xmm},#{dxjydwdm},#{szdm},#{sfdd},#{bz})")
//    int insert( SsLQXXHZ user);
//
//    int update(SsLQXXHZ user);
//
//    @Delete("delete from sys_user where id = #{id}}")
//    int deleteById(@Param("id") Integer id);
//
//    @Select(" SELECT * from sys_user where lqyxmc like #{lqyxmc} limit #{pageNum},#{pageSize}")
//    List<SsLQXXHZ> selectPage(Integer pageNum, Integer pageSize,String lqyxmc);
//
//    @Select("SELECT count(*) from sys_user where lqyxmc like #{lqyxmc}")
//    int selectTotal(String lqyxmc);
}
