package com.mingmen.yanpro.service;

import com.mingmen.yanpro.dao.SsdsDao;
import com.mingmen.yanpro.dao.YXSDao;
import com.mingmen.yanpro.mapper.SsdsMapper;
import com.mingmen.yanpro.mapper.YXSMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SsdsService {

    @Autowired
    private SsdsMapper ssdsMapper;

    public int save(SsdsDao ssdsDao){
        if(ssdsDao.getID() == null){//user没有id，则表示是新增
            return ssdsMapper.insert(ssdsDao);
        }else{//否则为更新
            return ssdsMapper.update(ssdsDao);
        }
    }

    public List<SsdsDao> selectAll(){
        return ssdsMapper.selectAll();
    }

    public List<SsdsDao> selectByMC(String DSXM){
        return ssdsMapper.selectByMC(DSXM);
    }

    public Integer deleteByID(String ID) {
        return ssdsMapper.deleteByID(ID);
    }

    public Integer deleteBatch(List<String> IDs) {
        Integer result = 1;
        for(int i=0; i< IDs.size(); i++){
            result = ssdsMapper.deleteByID(IDs.get(i));
            if(result==0){
                return result;
            }
        }
        return result;
    }

    public void saveBatch(List<SsdsDao> list) {
        for(SsdsDao ssdsDao:list){
            ssdsMapper.insert(ssdsDao);
        }
    }

    public List<SsdsDao> selectPage(Integer pageNum, Integer pageSize) {
        return ssdsMapper.selectPage(pageNum,pageSize);
    }

    public Integer selectTotal() {
        return ssdsMapper.selectTotal();
    }
}
