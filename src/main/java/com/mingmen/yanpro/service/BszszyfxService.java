package com.mingmen.yanpro.service;

import com.mingmen.yanpro.dao.BszszyDao;
import com.mingmen.yanpro.dao.BszszyfxDao;
import com.mingmen.yanpro.mapper.BszszyMapper;
import com.mingmen.yanpro.mapper.BszszyfxMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BszszyfxService {

    @Autowired
    private BszszyfxMapper bszszyfxMapper;

    public int save(BszszyfxDao bszszyfxDao){
        if(bszszyfxDao.getID() == null){//user没有id，则表示是新增
            return bszszyfxMapper.insert(bszszyfxDao);
        }else{//否则为更新
            return bszszyfxMapper.update(bszszyfxDao);
        }
    }

    public List<BszszyfxDao> selectAll(){
        return bszszyfxMapper.selectAll();
    }

    public List<BszszyfxDao> selectByMC(String YJFXMC){
        return bszszyfxMapper.selectByMC(YJFXMC);
    }

    public Integer deleteByID(String ID) {
        return bszszyfxMapper.deleteByYJFXDM(ID);
    }

    public Integer deleteBatch(List<String> IDs) {
        Integer result = 1;
        for(int i=0; i< IDs.size(); i++){
            result = bszszyfxMapper.deleteByYJFXDM(IDs.get(i));
            if(result==0){
                return result;
            }
        }
        return result;
    }

    public void saveBatch(List<BszszyfxDao> list) {
        for(BszszyfxDao bszszyfxDao:list){
            bszszyfxMapper.insert(bszszyfxDao);
        }
    }

    public List<BszszyfxDao> selectPage(Integer pageNum, Integer pageSize) {
        return bszszyfxMapper.selectPage(pageNum,pageSize);
    }

    public Integer selectTotal() {
        return bszszyfxMapper.selectTotal();
    }
}
