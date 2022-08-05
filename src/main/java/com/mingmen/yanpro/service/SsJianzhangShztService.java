package com.mingmen.yanpro.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingmen.yanpro.dao.SsJianzhangShzt;
import com.mingmen.yanpro.mapper.SsJianzhangShztMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;


@Service
public class SsJianzhangShztService extends ServiceImpl<SsJianzhangShztMapper, SsJianzhangShzt>  {

    @Autowired
    private SsJianzhangShztMapper ssJianzhangShztMapper;

    @Resource
    private SsjianzhangZykskmService ssjianzhangZykskmService;



    public int saveSsJianzhangShzt(SsJianzhangShzt ssJianzhangShzt){
        if(ssJianzhangShzt.getId() == null){
            return ssJianzhangShztMapper.insert(ssJianzhangShzt);
        } else {
            //更新操作，mybatis-plus方式
            SsJianzhangShzt ssJianzhangShzt1 = new SsJianzhangShzt();
            ssJianzhangShzt1.setId(ssJianzhangShzt.getId());
            ssJianzhangShzt1.setShzt(ssJianzhangShzt.getShzt());
            return ssJianzhangShztMapper.updateById(ssJianzhangShzt1);
        }
    }

    public String getShzt(String YXSDM, String NF){

        //int len = NF.length();
        //String onlyNF = NF.substring(11,15);
        QueryWrapper<SsJianzhangShzt> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("YXSDM", YXSDM);
        queryWrapper.eq("NF", NF);

        return this.getOne(queryWrapper).getShzt();
    }

    public boolean setShztXyms(String yxsdm, String nf) {
        QueryWrapper<SsJianzhangShzt> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("YXSDM", yxsdm);
        queryWrapper.eq("NF", nf);

        if (this.getOne(queryWrapper) !=null){
            // 只要是学院秘书点提交按钮，那状态肯定变为“秘书已提交”
            SsJianzhangShzt ssJianzhangShzt = new SsJianzhangShzt();
            ssJianzhangShzt.setId(this.getOne(queryWrapper).getId());
            ssJianzhangShzt.setShzt("1");
            this.saveSsJianzhangShzt(ssJianzhangShzt);
            return true;
        }else{
            return false;
        }


    }

    public boolean setShztLdOrYy(String yxsdm, String nf, String shzt) {
        //String onlyNF = nf.substring(11,15);
        QueryWrapper<SsJianzhangShzt> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("YXSDM", yxsdm);
        queryWrapper.eq("NF", nf);

        if (this.getOne(queryWrapper) !=null){
            SsJianzhangShzt ssJianzhangShzt = new SsJianzhangShzt();
            ssJianzhangShzt.setId(this.getOne(queryWrapper).getId());
            ssJianzhangShzt.setShzt(shzt);
            this.saveSsJianzhangShzt(ssJianzhangShzt);
            return true;
        }else{
            return false;
        }
    }

    public void nextYearShzt(String yxsdm) {
        String yxsmc = ssjianzhangZykskmService.yxsdm2yxsmc(yxsdm);
        Calendar calendar = Calendar.getInstance();
        int nf = calendar.get(Calendar.YEAR)+1;
        String shzt = "0";
        ssJianzhangShztMapper.nextYearShzt(yxsdm, yxsmc, Integer.toString(nf), shzt);
    }

    public String  getShztYy(String yxsmc, String nf) {
        QueryWrapper<SsJianzhangShzt> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("YXSMC", yxsmc);
        queryWrapper.eq("NF", nf);

        return this.getOne(queryWrapper).getShzt();
    }
}
