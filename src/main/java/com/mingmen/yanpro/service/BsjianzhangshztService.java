package com.mingmen.yanpro.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingmen.yanpro.dao.BsjianzhangshztDao;
import com.mingmen.yanpro.mapper.BsjianzhangshztMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BsjianzhangshztService extends ServiceImpl<BsjianzhangshztMapper, BsjianzhangshztDao> {

    @Autowired
    private BsjianzhangshztMapper bsjianzhangshztMapper;

    public int saveBsshzt(BsjianzhangshztDao bsjianzhangshztDao) {
        if(bsjianzhangshztDao.getID()==null) { //审核状态没有id，则表示新增
            return bsjianzhangshztMapper.insert(bsjianzhangshztDao);
        } else { //否则为更新
            return bsjianzhangshztMapper.update(bsjianzhangshztDao);
        }
    }

    public List<BsjianzhangshztDao> selectAll(){
        return bsjianzhangshztMapper.selectAll();
    }

    public String minYear(String yxsdm) {
        return bsjianzhangshztMapper.minYear(yxsdm);
    }

    public Integer getSHZT(String yxsdm, String nf) {
        String s = bsjianzhangshztMapper.getSHZT(yxsdm,nf);
        if(s==null) {
            return 6;
        }
        Integer shzt =  Integer.parseInt(s);
        return shzt;
    }

    public IPage<BsjianzhangshztDao> findPage(Page<BsjianzhangshztDao> page, String yxsmc, String nf) {
        return bsjianzhangshztMapper.findPage(page,yxsmc,nf);
    }

    public boolean setXYMSshzt(String yxsdm, String nf) {
        QueryWrapper<BsjianzhangshztDao> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("YXSDM", yxsdm);
        queryWrapper.eq("NF", nf);

        if(getOne(queryWrapper)!= null){
            BsjianzhangshztDao bsjianzhangshztDao = new BsjianzhangshztDao();
            bsjianzhangshztDao.setID(getOne(queryWrapper).getID());
            bsjianzhangshztDao.setSHZT("1");
            saveBsshzt(bsjianzhangshztDao);
            return true;
        } else {
            return false;
        }
    }

    public boolean setXYLDYYshzt(String yxsdm, String nf, String shzt) {
        QueryWrapper<BsjianzhangshztDao> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("YXSDM", yxsdm);
        queryWrapper.eq("NF", nf);

        if(getOne(queryWrapper)!=null){
            BsjianzhangshztDao bsjianzhangshztDao = new BsjianzhangshztDao();
            bsjianzhangshztDao.setID(getOne(queryWrapper).getID());
            bsjianzhangshztDao.setSHZT(shzt);
            saveBsshzt(bsjianzhangshztDao);
            return true;
        }
        else{
            return false;
        }
    }

    public String allJZminyear() {
        return bsjianzhangshztMapper.allJZminyear();
    }
}
