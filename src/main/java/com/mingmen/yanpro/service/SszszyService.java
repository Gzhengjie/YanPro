package com.mingmen.yanpro.service;

import com.mingmen.yanpro.dao.BszszyDao;
import com.mingmen.yanpro.dao.SszszyDao;
import com.mingmen.yanpro.mapper.BszszyMapper;
import com.mingmen.yanpro.mapper.SszszyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SszszyService {

    @Autowired
    private SszszyMapper sszszyMapper;

    public int save(SszszyDao sszszyDao){
        if(sszszyDao.getID() == null){//user没有id，则表示是新增
            return sszszyMapper.insert(sszszyDao);
        }else{//否则为更新
            return sszszyMapper.update(sszszyDao);
        }
    }

    public List<SszszyDao> selectAll(){
        return sszszyMapper.selectAll();
    }

    public List<SszszyDao> selectByMC(String ZYMC){
        return sszszyMapper.selectByMC(ZYMC);
    }

    public Integer deleteByID(String ID) {
        return sszszyMapper.deleteByID(ID);
    }

    public Integer deleteBatch(List<String> IDs) {
        Integer result = 1;
        for(int i=0; i< IDs.size(); i++){
            result = sszszyMapper.deleteByID(IDs.get(i));
            if(result==0){
                return result;
            }
        }
        return result;
    }

    public void saveBatch(List<SszszyDao> list) {
        for(SszszyDao sszszyDao:list){
            sszszyMapper.insert(sszszyDao);
        }
    }

    public List<SszszyDao> selectPage(Integer pageNum, Integer pageSize) {
        return sszszyMapper.selectPage(pageNum,pageSize);
    }

    public Integer selectTotal() {
        return sszszyMapper.selectTotal();
    }
}
