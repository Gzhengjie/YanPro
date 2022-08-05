package com.mingmen.yanpro.service;

import com.mingmen.yanpro.dao.BsdsDao;
import com.mingmen.yanpro.dao.SsdsDao;
import com.mingmen.yanpro.mapper.BsdsMapper;
import com.mingmen.yanpro.mapper.SsdsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BsdsService {

    @Autowired
    private BsdsMapper bsdsMapper;

    public int save(BsdsDao bsdsDao){
        if(bsdsDao.getID() == null){//user没有id，则表示是新增
            return bsdsMapper.insert(bsdsDao);
        }else{//否则为更新
            return bsdsMapper.update(bsdsDao);
        }
    }

    public List<BsdsDao> selectAll(){
        return bsdsMapper.selectAll();
    }

    public List<BsdsDao> selectByMC(String DSXM){
        return bsdsMapper.selectByMC(DSXM);
    }

    public Integer deleteByID(String ID) {
        return bsdsMapper.deleteByID(ID);
    }

    public Integer deleteBatch(List<String> IDs) {
        Integer result = 1;
        for(int i=0; i< IDs.size(); i++){
            result = bsdsMapper.deleteByID(IDs.get(i));
            if(result==0){
                return result;
            }
        }
        return result;
    }

    public void saveBatch(List<BsdsDao> list) {
        for(BsdsDao bsdsDao:list){
            bsdsMapper.insert(bsdsDao);
        }
    }

    public List<BsdsDao> selectPage(Integer pageNum, Integer pageSize) {
        return bsdsMapper.selectPage(pageNum,pageSize);
    }

    public Integer selectTotal() {
        return bsdsMapper.selectTotal();
    }
}
