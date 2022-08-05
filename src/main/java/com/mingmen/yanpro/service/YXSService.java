package com.mingmen.yanpro.service;

import com.mingmen.yanpro.dao.YXSDao;
import com.mingmen.yanpro.mapper.YXSMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YXSService {

    @Autowired
    private YXSMapper yxsMapper;

    public int save(YXSDao yxsDao){
        if(yxsDao.getID() == null){//user没有id，则表示是新增
            return yxsMapper.insert(yxsDao);
        }else{//否则为更新
            return yxsMapper.update(yxsDao);
        }
    }

    public List<YXSDao> selectAll(){
        return yxsMapper.selectAll();
    }

    public List<YXSDao> selectByMC(String YXSMC){
        return yxsMapper.selectByMC(YXSMC);
    }

    public Integer deleteByID(String ID) {
        return yxsMapper.deleteByID(ID);
    }

    public Integer deleteBatch(List<String> IDs) {
        Integer result = 1;
        for(int i=0; i< IDs.size(); i++){
            result = yxsMapper.deleteByID(IDs.get(i));
            if(result==0){
                return result;
            }
        }
        return result;
    }

    public void saveBatch(List<YXSDao> list) {
        for(YXSDao yxsDao:list){
            yxsMapper.insert(yxsDao);
        }
    }

    public List<YXSDao> selectPage(Integer pageNum, Integer pageSize) {
        return yxsMapper.selectPage(pageNum,pageSize);
    }

    public Integer selectTotal() {
        return yxsMapper.selectTotal();
    }
}
