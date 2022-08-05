package com.mingmen.yanpro.service;

import com.mingmen.yanpro.dao.PMFSDao;
import com.mingmen.yanpro.mapper.PMFSMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PMFSService {
    @Autowired
    private PMFSMapper pmfsMapper;
    //    选择全部数据
    public List<PMFSDao> selectAll(){
        return pmfsMapper.selectAll();
    }
    //    分页查找
    public Map<String, Object> findPage(Integer pageNum, Integer pageSize, String mc){
        pageNum = (pageNum - 1) * pageSize;
        List<PMFSDao> data = pmfsMapper.selectPage(pageNum,pageSize,mc);
        Integer total = pmfsMapper.selectTotal(mc);
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        return res;
    }
    //    插入数据
    public Integer insert(PMFSDao pmfsDao){return pmfsMapper.insert(pmfsDao);}

    public PMFSDao selectByPMFSDM(String PMFS){return pmfsMapper.selectByPMFSDM(PMFS);}

    public PMFSDao selectByPMFSMC(String PMFS){return pmfsMapper.selectByPMFSMC(PMFS);}

    public  Integer save(PMFSDao pmfsDao){
        if(pmfsDao.getID() == null){//user没有id，则表示是新增
            return pmfsMapper.insert(pmfsDao);
        }else{//否则为更新
            return pmfsMapper.update(pmfsDao);
        }
    }


    public Integer delete(Integer ID) {
        return pmfsMapper.delete(ID);
    }

    public void saveBatch(List<PMFSDao> list) {
        for(PMFSDao pmfsDao:list){
            pmfsMapper.insert(pmfsDao);
        }
    }

    public Integer deleteBatch(List<Integer> list) {
        int result = 1;
        for(int id:list){
            if(pmfsMapper.delete(id)==0){
                result = 0;
            }
        }
        return result;
    }


}
