package com.mingmen.yanpro.service;

import com.mingmen.yanpro.dao.BszzbfpDao;
import com.mingmen.yanpro.dao.SszzbfpDao;
import com.mingmen.yanpro.mapper.BszzbfpMapper;
import com.mingmen.yanpro.mapper.SszzbfpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SszzbfpService {

    @Autowired
    private SszzbfpMapper sszzbfpMapper;

    public int save(SszzbfpDao sszzbfpDao){
        if(sszzbfpDao.getID() == null){//user没有id，则表示是新增
            return sszzbfpMapper.insert(sszzbfpDao);
        }else{//否则为更新
            return sszzbfpMapper.update(sszzbfpDao);
        }
    }

    public List<SszzbfpDao> selectAll(){
        return sszzbfpMapper.selectAll();
    }

    public List<SszzbfpDao> selectByMC(String YXSMC){
        return sszzbfpMapper.selectByMC(YXSMC);
    }

    public Integer deleteByID(String ID) {
        return sszzbfpMapper.deleteByID(ID);
    }

    public Integer deleteBatch(List<String> IDs) {
        Integer result = 1;
        for(int i=0; i< IDs.size(); i++){
            result = sszzbfpMapper.deleteByID(IDs.get(i));
            if(result==0){
                return result;
            }
        }
        return result;
    }

    public void saveBatch(List<SszzbfpDao> list) {
        for(SszzbfpDao sszzbfpDao:list){
            sszzbfpMapper.insert(sszzbfpDao);
        }
    }

    public List<SszzbfpDao> selectPage(Integer pageNum, Integer pageSize) {
        return sszzbfpMapper.selectPage(pageNum,pageSize);
    }

    public Integer selectTotal() {
        return sszzbfpMapper.selectTotal();
    }
}
