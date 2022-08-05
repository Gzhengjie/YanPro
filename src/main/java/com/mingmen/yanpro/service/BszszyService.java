package com.mingmen.yanpro.service;

import com.mingmen.yanpro.dao.BszszyDao;
import com.mingmen.yanpro.dao.YXSDao;
import com.mingmen.yanpro.mapper.BszszyMapper;
import com.mingmen.yanpro.mapper.YXSMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BszszyService {

    @Autowired
    private BszszyMapper bszszyMapper;

    public int save(BszszyDao bszszyDao){
        if(bszszyDao.getID() == null){//user没有id，则表示是新增
            return bszszyMapper.insert(bszszyDao);
        }else{//否则为更新
            return bszszyMapper.update(bszszyDao);
        }
    }

    public List<BszszyDao> selectAll(){
        return bszszyMapper.selectAll();
    }

    public List<BszszyDao> selectByMC(String ZYMC){
        return bszszyMapper.selectByMC(ZYMC);
    }

    public Integer deleteByID(String ID) {
        return bszszyMapper.deleteByID(ID);
    }

    public Integer deleteBatch(List<String> IDs) {
        Integer result = 1;
        for(int i=0; i< IDs.size(); i++){
            result = bszszyMapper.deleteByID(IDs.get(i));
            if(result==0){
                return result;
            }
        }
        return result;
    }

    public void saveBatch(List<BszszyDao> list) {
        for(BszszyDao bszszyDao:list){
            bszszyMapper.insert(bszszyDao);
        }
    }

    public List<BszszyDao> selectPage(Integer pageNum, Integer pageSize) {
        return bszszyMapper.selectPage(pageNum,pageSize);
    }

    public Integer selectTotal() {
        return bszszyMapper.selectTotal();
    }
}
