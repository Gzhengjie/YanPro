package com.mingmen.yanpro.service;

import com.mingmen.yanpro.dao.BszszyfxDao;
import com.mingmen.yanpro.dao.SszszyfxDao;
import com.mingmen.yanpro.mapper.BszszyfxMapper;
import com.mingmen.yanpro.mapper.SszszyfxMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SszszyfxService {

    @Autowired
    private SszszyfxMapper sszszyfxMapper;

    public int save(SszszyfxDao sszszyfxDao){
        if(sszszyfxDao.getID() == null){//user没有id，则表示是新增
            return sszszyfxMapper.insert(sszszyfxDao);
        }else{//否则为更新
            return sszszyfxMapper.update(sszszyfxDao);
        }
    }

    public List<SszszyfxDao> selectAll(){
        return sszszyfxMapper.selectAll();
    }

    public List<SszszyfxDao> selectByMC(String YJFXMC){
        return sszszyfxMapper.selectByMC(YJFXMC);
    }

    public Integer deleteByID(String ID) {
        return sszszyfxMapper.deleteByID(ID);
    }

    public Integer deleteBatch(List<String> IDs) {
        Integer result = 1;
        for(int i=0; i< IDs.size(); i++){
            result = sszszyfxMapper.deleteByID(IDs.get(i));
            if(result==0){
                return result;
            }
        }
        return result;
    }

    public void saveBatch(List<SszszyfxDao> list) {
        for(SszszyfxDao sszszyfxDao:list){
            sszszyfxMapper.insert(sszszyfxDao);
        }
    }

    public List<SszszyfxDao> selectPage(Integer pageNum, Integer pageSize) {
        return sszszyfxMapper.selectPage(pageNum,pageSize);
    }

    public Integer selectTotal() {
        return sszszyfxMapper.selectTotal();
    }
}
