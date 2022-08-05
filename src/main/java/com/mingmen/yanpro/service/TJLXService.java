package com.mingmen.yanpro.service;

import com.mingmen.yanpro.dao.TJLXDao;
import com.mingmen.yanpro.mapper.TJLXMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TJLXService {

    @Autowired
    private TJLXMapper tjlxMapper;
//    选择全部数据
    public List<TJLXDao> selectAll(){
        return tjlxMapper.selectAll();
    }
//    分页查找
    public Map<String, Object> findPage(Integer pageNum, Integer pageSize, String mc){
        pageNum = (pageNum - 1) * pageSize;
        List<TJLXDao> data = tjlxMapper.selectPage(pageNum,pageSize,mc);
        Integer total = tjlxMapper.selectTotal(mc);
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        return res;
    }
//    插入数据
    public Integer insert(TJLXDao tjlxDao){return tjlxMapper.insert(tjlxDao);}

//    按推荐类型码搜索
    public TJLXDao selectByTJLXM(String TJLXM){return tjlxMapper.selectByTJLXM(TJLXM);}

    public TJLXDao selectByTJLXMC(String TJLXMC){return tjlxMapper.selectByTJLXMC(TJLXMC);}

//    按推荐类型码进行save,如果推荐类型码存在则更新表,如果推荐类型码不存在则插入数据
    public  Integer save(TJLXDao tjlxDao){
        if(tjlxDao.getID()==null){
            return tjlxMapper.insert(tjlxDao);
        }
        else{
            return tjlxMapper.update(tjlxDao);
        }
    }

    public Integer delete(Integer ID) {
        return tjlxMapper.delete(ID);
    }

    public void saveBatch(List<TJLXDao> list) {
        for(TJLXDao tjlxDao:list){
            tjlxMapper.insert(tjlxDao);
        }
    }

    public Integer deleteBatch(List<Integer> list) {
        int result = 1;
        for(int id:list){
            if(tjlxMapper.delete(id)==0){
                result = 0;
            }
        }
        return result;
    }

}
