package com.mingmen.yanpro.service;

import com.mingmen.yanpro.dao.BszszyDao;
import com.mingmen.yanpro.dao.BszzbfpDao;
import com.mingmen.yanpro.mapper.BszszyMapper;
import com.mingmen.yanpro.mapper.BszzbfpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BszzbfpService {

    @Autowired
    private BszzbfpMapper bszzbfpMapper;

    public int save(BszzbfpDao bszzbfpDao){
        if(bszzbfpDao.getID() == null){//user没有id，则表示是新增
            return bszzbfpMapper.insert(bszzbfpDao);
        }else{//否则为更新
            return bszzbfpMapper.update(bszzbfpDao);
        }
    }

    public List<BszzbfpDao> selectAll(){
        return bszzbfpMapper.selectAll();
    }

    public List<BszzbfpDao> selectByMC(String YXSMC){
        return bszzbfpMapper.selectByMC(YXSMC);
    }

    public Integer deleteByID(String ID) {
        return bszzbfpMapper.deleteByID(ID);
    }

    public Integer deleteBatch(List<String> IDs) {
        Integer result = 1;
        for(int i=0; i< IDs.size(); i++){
            result = bszzbfpMapper.deleteByID(IDs.get(i));
            if(result==0){
                return result;
            }
        }
        return result;
    }

    public void saveBatch(List<BszzbfpDao> list) {
        for(BszzbfpDao bszzbfpDao:list){
            bszzbfpMapper.insert(bszzbfpDao);
        }
    }

    public List<BszzbfpDao> selectPage(Integer pageNum, Integer pageSize) {
        return bszzbfpMapper.selectPage(pageNum,pageSize);
    }

    public Integer selectTotal() {
        return bszzbfpMapper.selectTotal();
    }
}
