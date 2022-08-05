package com.mingmen.yanpro.service;
import com.mingmen.yanpro.dao.PMFSDao;
import com.mingmen.yanpro.dao.TJDWDao;
import com.mingmen.yanpro.mapper.TJDWMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TJDWService {
    @Autowired
    TJDWMapper tjdwMapper;
    //    选择全部数据
    public List<TJDWDao> selectAll(){
        return tjdwMapper.selectAll();
    }
    //    分页查找
    public Map<String, Object> findPage(Integer pageNum, Integer pageSize, String mc){
        pageNum = (pageNum - 1) * pageSize;
        List<TJDWDao> data = tjdwMapper.selectPage(pageNum,pageSize,mc);
        Integer total = tjdwMapper.selectTotal(mc);
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        return res;
    }
    //    插入数据
    public Integer insert(TJDWDao tjdwDao){return tjdwMapper.insert(tjdwDao);}

    //    搜索
    public TJDWDao selectByTJDWDM(String tjdwdm){return tjdwMapper.selectByTJDWDM(tjdwdm);}

    //    搜索
    public TJDWDao selectByTJDWMC(String tjdwmc){return tjdwMapper.selectByTJDWMC(tjdwmc);}

    public Integer save(TJDWDao tjdwDao){
        if(tjdwDao.getID() == null){//user没有id，则表示是新增
            return tjdwMapper.insert(tjdwDao);
        }else{//否则为更新
            return tjdwMapper.update(tjdwDao);
        }
    }
    public Integer delete(Integer ID) {
        return tjdwMapper.delete(ID);
    }

    public void saveBatch(List<TJDWDao> list) {
        for(TJDWDao tjdwDao:list){
            tjdwMapper.insert(tjdwDao);
        }
    }

    public Integer deleteBatch(List<Integer> list) {
        int result = 1;
        for(int id:list){
            if(tjdwMapper.delete(id)==0){
                result = 0;
            }
        }
        return result;
    }


}
