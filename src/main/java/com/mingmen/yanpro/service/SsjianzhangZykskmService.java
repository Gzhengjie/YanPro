package com.mingmen.yanpro.service;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingmen.yanpro.dao.SsJianzhangPinjie;
import com.mingmen.yanpro.dao.SsJianzhangZykskm;
import com.mingmen.yanpro.mapper.SsJianzhangZszyrsMapper;
import com.mingmen.yanpro.mapper.SsjianzhangZykskmMapper;
import com.mingmen.yanpro.utils.JianzhangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class SsjianzhangZykskmService extends ServiceImpl<SsjianzhangZykskmMapper, SsJianzhangZykskm> {

    @Autowired
    private SsjianzhangZykskmMapper ssjianzhangZykskmMapper;

    @Resource
    private SsJianzhangZszyrsMapper ssJianzhangZszyrsMapper;

    public int saveSsjianzhangZykskm(SsJianzhangZykskm ssJianzhangZykskm){
        if (ssJianzhangZykskm.getId() == null){
            return ssjianzhangZykskmMapper.insert(ssJianzhangZykskm);
        } else {
            return ssjianzhangZykskmMapper.myUpdate(ssJianzhangZykskm);
        }
    }

    // 根据考试科目代码从基础表里查询考试科目名称
    public String getKMbyDM(String kmdm){
        return ssjianzhangZykskmMapper.getKMMC(kmdm);
    }

    public List<String> getZYDM(String yxsdm, String nf) {
        return ssjianzhangZykskmMapper.getZYDM(yxsdm, nf);
    }

    public String getZYMC(String zydm, String nf) {
        return ssjianzhangZykskmMapper.getZYMC(zydm, nf);
    }

    public List<String> getYJFXDM(String zymc, String nf) {
        return ssjianzhangZykskmMapper.getYJFXDM(zymc, nf);
    }

    //获取某学院的研究方向代码
    public List<String> getCollegeYJFXDM(String yxsdm, String nf){
        return ssjianzhangZykskmMapper.getCollegeYJFXDM(yxsdm, nf);
    }

    public String getYJFXMC(String yjfxdm, String zymc, String nf) {
        return ssjianzhangZykskmMapper.getYJFXMC(yjfxdm, zymc, nf);
    }

    public List<String> getKSKMDM(String ksdy) {
        return ssjianzhangZykskmMapper.getKSKMDM(ksdy);
    }

    public List<String> getAllCollege() {
        return ssjianzhangZykskmMapper.getAllCollege();
    }

    public String yxsdm2yxsmc(String yxsdm) {
        return ssjianzhangZykskmMapper.yxsdm2yxsmc(yxsdm);
    }

    public String getSFYXTJ() {
        return ssjianzhangZykskmMapper.getSFYXTJ();
    }


    public List<SsJianzhangPinjie> jianZhangPinJie(String yxsdm, String nf) {
        List<SsJianzhangPinjie> list = ssjianzhangZykskmMapper.jianZhangPinJie(yxsdm, nf);
        List<SsJianzhangPinjie> pinjieList = new ArrayList<>();
        for(SsJianzhangPinjie ssJianzhangPinjie : list){
            String zydm = ssJianzhangPinjie.getZy().substring(0,6);

            Object zytmrs = ssJianzhangZszyrsMapper.getZytmrs(yxsdm, zydm, nf);
            Object zyzkrs = ssJianzhangZszyrsMapper.getZyzkrs(yxsdm, zydm, nf);

//            ssJianzhangPinjie.setZy(ssJianzhangPinjie.getZy() + "\n公开招考人数：" + zyzkrs + "人\n"
//            + "接收推免人数：" + zytmrs + "人");
            ssJianzhangPinjie.setTmrs(Integer.parseInt(zytmrs.toString()));
            ssJianzhangPinjie.setZkrs(Integer.parseInt(zyzkrs.toString()));
            pinjieList.add(ssJianzhangPinjie);
        }
        return pinjieList;
    }



    public void updateSHR_SHSJ(String yxsdm, String nf, String shenheren, String yhz) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        UpdateWrapper<SsJianzhangZykskm> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("YXSDM", yxsdm);
        updateWrapper.eq("NF", nf);

        SsJianzhangZykskm ssJianzhangZykskm = new SsJianzhangZykskm();
        if (yhz.equals("1-1")){
            ssJianzhangZykskm.setYyshr(shenheren);
            ssJianzhangZykskm.setYyshsj(formatter.format(calendar.getTime()));
        } else if (yhz.equals("2-1")) {
            ssJianzhangZykskm.setXyshr(shenheren);
            ssJianzhangZykskm.setXyshsj(formatter.format(calendar.getTime()));
        }

        ssjianzhangZykskmMapper.update(ssJianzhangZykskm, updateWrapper);
    }

    public List<SsJianzhangZykskm> saveWihtCheck(List<SsJianzhangZykskm> list) {
        List<SsJianzhangZykskm> failList = new ArrayList<>();
        for(SsJianzhangZykskm temp : list){
            // 校验是否为空，即null或空字符
            if(StrUtil.isBlank(temp.getZydm()) || StrUtil.isBlank(temp.getYjfxdm())
            || StrUtil.isBlank(temp.getZzllm()) || StrUtil.isBlank(temp.getYwk1m())
            || StrUtil.isBlank(temp.getYwk2m()) || StrUtil.isBlank(temp.getWgym())){
                temp.setFalseReason("专业代码、研究方向代码或考试科目代码不能为空");
                failList.add(temp);
            } else if(!JianzhangUtils.isDBData(temp.getZydm(), this.getZYDM(temp.getYxsdm(), temp.getNf()))
            || !JianzhangUtils.isDBData(temp.getYjfxdm(), this.getCollegeYJFXDM(temp.getYxsdm(), temp.getNf()))
            || !JianzhangUtils.isDBData(temp.getZzllm(), this.getKSKMDM("1"))
            || !JianzhangUtils.isDBData(temp.getWgym(), this.getKSKMDM("2"))
            || !JianzhangUtils.isDBData(temp.getYwk1m(), this.getKSKMDM("3"))
            || !JianzhangUtils.isDBData(temp.getYwk2m(), this.getKSKMDM("4"))){
                // 校验数据是否合法，即是否在数据库基础表里
                // 研究方向代码，先在这里校验，有就行
                temp.setFalseReason("专业代码、研究方向代码或考试科目代码错误，请检查上传信息或维护基础表");
                failList.add(temp);
            } else {
                temp.setZymc(this.getZYMC(temp.getZydm(), temp.getNf()));
                //在这里根据研究方向代码和专业名称查询研究方向名称，如果没查出来，说明缺少研究方向代码
                String yjfxmc = this.getYJFXMC(temp.getYjfxdm(), this.getZYMC(temp.getZydm(), temp.getNf()), temp.getNf());
                if(StrUtil.isBlank(yjfxmc)){
                    temp.setFalseReason("研究方向表中“"+ this.getZYMC(temp.getZydm(), temp.getNf()) + "”专业无相关方向，请先维护基础表");
                    failList.add(temp);
                } else {
                    temp.setYjfxmc(yjfxmc);
                    if(ssjianzhangZykskmMapper.insert(temp) < 1){
                        temp.setFalseReason("不能导入重复数据");
                        failList.add(temp);
                    }
                }
            }
        }
        return failList;
    }

//    public static void main(String[] args) {
//        String a = "010010aaaaaa";
//        System.out.println(a.substring(0,6));
//    }

}
