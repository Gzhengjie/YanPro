package com.mingmen.yanpro.service;

import com.mingmen.yanpro.dao.JSLBDao;
import com.mingmen.yanpro.dao.PMFSDao;
import com.mingmen.yanpro.mapper.JSLBMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JSLBService {
    @Autowired
    JSLBMapper jslbMapper;
    //    选择全部数据
    public List<JSLBDao> selectAll(){
        return jslbMapper.selectAll();
    }
    //    分页查找
    public Map<String, Object> findPage(Integer pageNum, Integer pageSize, String mc){
        pageNum = (pageNum - 1) * pageSize;
        List<JSLBDao> data = jslbMapper.selectPage(pageNum,pageSize,mc);
        Integer total = jslbMapper.selectTotal(mc);
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        return res;
    }

    //    插入数据
    public Integer insert(JSLBDao jslbDao){return jslbMapper.insert(jslbDao);}

    //    搜索
    public JSLBDao selectByJSLBDM(String jslbdm){return jslbMapper.selectByJSLBDM(jslbdm);}

    //    搜索
    public JSLBDao selectByJSLBMC(String jslbmc){return jslbMapper.selectByJSLBMC(jslbmc);}


    public Integer save(JSLBDao jslbDao){
        if(jslbDao.getID() == null){//user没有id，则表示是新增
            return jslbMapper.insert(jslbDao);
        }else{//否则为更新
            return jslbMapper.update(jslbDao);
        }
    }

    public Integer delete(Integer ID) {
        return jslbMapper.delete(ID);
    }

    public void saveBatch(List<JSLBDao> list) {
        for(JSLBDao jslbDao:list){
            jslbMapper.insert(jslbDao);
        }
    }

    public Integer deleteBatch(List<Integer> list) {
        int result = 1;
        for(int id:list){
            if(jslbMapper.delete(id)==0){
                result = 0;
            }
        }
        return result;
    }

}
