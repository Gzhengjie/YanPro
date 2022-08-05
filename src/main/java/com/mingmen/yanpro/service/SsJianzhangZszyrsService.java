package com.mingmen.yanpro.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.util.IntUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingmen.yanpro.dao.SsJianzhangZszyrs;
import com.mingmen.yanpro.mapper.SsJianzhangZszyrsMapper;
import com.mingmen.yanpro.utils.JianzhangUtils;
import io.swagger.models.auth.In;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class SsJianzhangZszyrsService extends ServiceImpl<SsJianzhangZszyrsMapper, SsJianzhangZszyrs> {

    @Autowired
    private SsJianzhangZszyrsMapper ssJianzhangZszyrsMapper;

    @Resource
    private SsjianzhangZykskmService ssjianzhangZykskmService;

    public int saveSsJianzhangZszyrs(SsJianzhangZszyrs ssJianzhangZszyrs){
        if(ssJianzhangZszyrs.getId() == null){
            return ssJianzhangZszyrsMapper.insert(ssJianzhangZszyrs);
        } else {
            return ssJianzhangZszyrsMapper.myUpdate(ssJianzhangZszyrs);
        }
    }

    public void updateSHR_SHSJ(String yxsdm, String nf, String shenheren, String yhz) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        UpdateWrapper<SsJianzhangZszyrs> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("YXSDM", yxsdm);
        updateWrapper.eq("NF", nf);

        SsJianzhangZszyrs ssJianzhangZszyrs = new SsJianzhangZszyrs();
        if (yhz.equals("1-1")){
            ssJianzhangZszyrs.setYyshr(shenheren);
            ssJianzhangZszyrs.setYyshsj(formatter.format(calendar.getTime()));
        } else if (yhz.equals("2-1")) {
            ssJianzhangZszyrs.setXyshr(shenheren);
            ssJianzhangZszyrs.setXyshsj(formatter.format(calendar.getTime()));
        }

        ssJianzhangZszyrsMapper.update(ssJianzhangZszyrs, updateWrapper);
    }

    public List<SsJianzhangZszyrs> saveWihtCheck(List<SsJianzhangZszyrs> ssJianzhangZszyrsList) {
        List<SsJianzhangZszyrs> failList = new ArrayList<>();
        for(SsJianzhangZszyrs temp : ssJianzhangZszyrsList){
            // 校验是否为空，即null或空字符
            if(StrUtil.isBlank(temp.getZydm()) || StrUtil.isBlank(temp.getYjfxdm())
                    || StrUtil.isBlank(temp.getNzsrs().toString()) || StrUtil.isBlank(temp.getJstmrs().toString())
                    || StrUtil.isBlank(temp.getGkzkrs().toString()) || StrUtil.isBlank(temp.getRssm())){
                temp.setFalseReason("专业代码、研究方向代码或考试科目代码不能为空；“招生人数说明”不能为空，如果没有请写“无”");
                failList.add(temp);
            } else if(!JianzhangUtils.isDBData(temp.getZydm(), ssjianzhangZykskmService.getZYDM(temp.getYxsdm(), temp.getNf()))
                    || !JianzhangUtils.isDBData(temp.getYjfxdm(), ssjianzhangZykskmService.getCollegeYJFXDM(temp.getYxsdm(), temp.getNf()))){
                // 校验数据是否合法，即是否在数据库基础表里
                // 研究方向代码，先在这里校验，有就行
                temp.setFalseReason("专业代码或研究方向代码错误，请检查上传信息或维护基础表");
                failList.add(temp);
            } else {
                temp.setZymc(ssjianzhangZykskmService.getZYMC(temp.getZydm(), temp.getNf()));
                //在这里根据研究方向代码和专业名称查询研究方向名称，如果没查出来，说明缺少研究方向代码
                String yjfxmc = ssjianzhangZykskmService.getYJFXMC(temp.getYjfxdm(), ssjianzhangZykskmService.getZYMC(temp.getZydm(), temp.getNf()), temp.getNf());
                if(StrUtil.isBlank(yjfxmc)){
                    temp.setFalseReason("研究方向表中“"+ ssjianzhangZykskmService.getZYMC(temp.getZydm(), temp.getNf()) + "”专业无相关方向，请先维护基础表");
                    failList.add(temp);
                } else {
                    temp.setYjfxmc(yjfxmc);
                    if(ssJianzhangZszyrsMapper.insert(temp) < 1){
                        temp.setFalseReason("不能导入重复数据");

                        failList.add(temp);
                    }
                }
            }
        }
        return failList;
    }

    public String jianZhangRsJiaoYan(String yxsdm, String nf) {
        Object yyZrsTemp = ssJianzhangZszyrsMapper.getZszrs(yxsdm, nf);
        int yyZszrs;  // 研院下发的招生总人数
        if (yyZrsTemp != null) {
            yyZszrs = Integer.parseInt(yyZrsTemp.toString());
        } else {
            return "1";
        }

        Object xyZszrsTemp = ssJianzhangZszyrsMapper.getXyZszrs(yxsdm, nf);
        int xyNzsrs;  // 学院填报的总人数
        if (xyZszrsTemp != null) {
            xyNzsrs = Integer.parseInt(xyZszrsTemp.toString());
        } else {
            return "5";
        }
        if (xyNzsrs != yyZszrs) {
            return "2";
        }

        Object yyTmrsTemp = ssJianzhangZszyrsMapper.getYyTmrs(yxsdm, nf);
        int yyTmrs; // 研院下发的接收推免人数
        if (yyTmrsTemp != null){
            yyTmrs = Integer.parseInt(yyTmrsTemp.toString());
        } else {
            return "1";
        }
        Object xyTmrsTemp = ssJianzhangZszyrsMapper.getXyTmrs(yxsdm, nf);
        int xyTmrs; // 学院提交的接收推免生人数
        if (xyTmrsTemp != null){
            xyTmrs = Integer.parseInt(xyTmrsTemp.toString());
        } else {
            return "5";
        }
        if (xyTmrs < yyTmrs){
            return "3";
        }

        Object xyGkzkrsTemp = ssJianzhangZszyrsMapper.getXyGkzkrs(yxsdm, nf);
        int xyGkzkrs; // 学院提交的公开招考人数
        if (xyGkzkrsTemp != null){
            xyGkzkrs = Integer.parseInt(xyGkzkrsTemp.toString());
        } else {
            return "5";
        }

        if((xyGkzkrs + xyTmrs) != xyNzsrs){
            return "4";
        }

        return "0";
    }
}
