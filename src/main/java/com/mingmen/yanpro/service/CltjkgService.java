package com.mingmen.yanpro.service;

import com.mingmen.yanpro.dao.CltjkgDao;
import com.mingmen.yanpro.dao.YXSDao;
import com.mingmen.yanpro.mapper.CltjkgMapper;
import com.mingmen.yanpro.mapper.YXSMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CltjkgService {

    @Autowired
    private CltjkgMapper cltjkgMapper;

    public int save(CltjkgDao cltjkgDao){
        if(cltjkgDao.getID() == null){//user没有id，则表示是新增
            return cltjkgMapper.insert(cltjkgDao);
        }else{//否则为更新
            return cltjkgMapper.update(cltjkgDao);
        }
    }

    public List<CltjkgDao> selectAll(){
        return cltjkgMapper.selectAll();
    }

    public List<CltjkgDao> selectByMC(String CLMC){
        return cltjkgMapper.selectByMC(CLMC);
    }

    public Integer deleteByID(String ID) {
        return cltjkgMapper.deleteByID(ID);
    }

    public Integer deleteBatch(List<String> IDs) {
        Integer result = 1;
        for(int i=0; i< IDs.size(); i++){
            result = cltjkgMapper.deleteByID(IDs.get(i));
            if(result==0){
                return result;
            }
        }
        return result;
    }

    public void saveBatch(List<CltjkgDao> list) {
        for(CltjkgDao cltjkgDao:list){
            cltjkgMapper.insert(cltjkgDao);
        }
    }

    public List<CltjkgDao> selectPage(Integer pageNum, Integer pageSize) {
        return cltjkgMapper.selectPage(pageNum,pageSize);
    }

    public Integer selectTotal() {
        return cltjkgMapper.selectTotal();
    }
}
