package com.mingmen.yanpro.service;

import com.mingmen.yanpro.dao.JSLBDao;
import com.mingmen.yanpro.dao.WYDJDao;
import com.mingmen.yanpro.mapper.WYDJMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WYDJService {
    @Autowired
    WYDJMapper wydjMapper;
    //    选择全部数据
    public List<WYDJDao> selectAll(){
        return wydjMapper.selectAll();
    }
    //    分页查找
    public Map<String, Object> findPage(Integer pageNum, Integer pageSize, String mc){
        pageNum = (pageNum - 1) * pageSize;
        List<WYDJDao> data = wydjMapper.selectPage(pageNum,pageSize,mc);
        Integer total = wydjMapper.selectTotal(mc);
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        return res;
    }
    //    插入数据
    public Integer insert(WYDJDao wydjDao){return wydjMapper.insert(wydjDao);}


    public WYDJDao selectByTGXMDM(String tgxmdm){return wydjMapper.selectByTGXMDM(tgxmdm);}

    public WYDJDao selectByTGXMMC(String tgxmmc){return wydjMapper.selectByTGXMMC(tgxmmc);}

    public  Integer save(WYDJDao wydjDao){
        if(wydjDao.getID()==null){
            return wydjMapper.insert(wydjDao);
        }
        else{
            return wydjMapper.update(wydjDao);
        }
    }

    public Integer delete(Integer ID) {
        return wydjMapper.delete(ID);
    }


    public void saveBatch(List<WYDJDao> list) {
        for(WYDJDao wydjDao:list){
            wydjMapper.insert(wydjDao);
        }
    }

    public Integer deleteBatch(List<Integer> list) {
        int result = 1;
        for(int id:list){
            if(wydjMapper.delete(id)==0){
                result = 0;
            }
        }
        return result;
    }
}
