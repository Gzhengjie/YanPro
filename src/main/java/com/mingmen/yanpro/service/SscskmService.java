package com.mingmen.yanpro.service;

import com.mingmen.yanpro.dao.SscskmDao;
import com.mingmen.yanpro.dao.YXSDao;
import com.mingmen.yanpro.mapper.SscskmMapper;
import com.mingmen.yanpro.mapper.YXSMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SscskmService {

    @Autowired
    private SscskmMapper sscskmMapper;

    public int save(SscskmDao sscskmDao){
        if(sscskmDao.getID() == null){//user没有id，则表示是新增
            return sscskmMapper.insert(sscskmDao);
        }else{//否则为更新
            return sscskmMapper.update(sscskmDao);
        }
    }

    public List<SscskmDao> selectAll(){
        return sscskmMapper.selectAll();
    }

    public List<SscskmDao> selectByMC(String KSKMMC){
        return sscskmMapper.selectByMC(KSKMMC);
    }

    public Integer deleteByID(String ID) {
        return sscskmMapper.deleteByID(ID);
    }

    public Integer deleteBatch(List<String> IDs) {
        Integer result = 1;
        for(int i=0; i< IDs.size(); i++){
            result = sscskmMapper.deleteByID(IDs.get(i));
            if(result==0){
                return result;
            }
        }
        return result;
    }

    public void saveBatch(List<SscskmDao> list) {
        for(SscskmDao sscskmDao:list){
            sscskmMapper.insert(sscskmDao);
        }
    }

    public List<SscskmDao> selectPage(Integer pageNum, Integer pageSize) {
        return sscskmMapper.selectPage(pageNum,pageSize);
    }

    public Integer selectTotal() {
        return sscskmMapper.selectTotal();
    }
}
