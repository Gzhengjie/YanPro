package com.mingmen.yanpro.service;

import com.mingmen.yanpro.dao.SljtgqkDao;
import com.mingmen.yanpro.mapper.SljtgqkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class SljtgqkService {

    @Autowired
    private SljtgqkMapper sljtgqkMapper;

    public int save(SljtgqkDao sljtgqkDao) {
        if(sljtgqkDao.getID()==null) { //sljtgqk中没有id，则表示插入
            return sljtgqkMapper.insert(sljtgqkDao);
        } else { //否则为更新
            return sljtgqkMapper.update(sljtgqkDao);
        }
    }

    public List<SljtgqkDao> findAll(){
        return sljtgqkMapper.findAll();
    }

    public Integer deleteByTgxmdm(String TGXMDM) {
        return sljtgqkMapper.deleteByTgxmdm(TGXMDM);
    }

    public Integer deleteById(Integer ID) {
        return sljtgqkMapper.deleteById(ID);
    }

    public List<SljtgqkDao> selectPage(Integer pageNum, Integer pageSize, String TGXMDM, String TGXMMC) {
        return sljtgqkMapper.selectPage(pageNum,pageSize,TGXMDM,TGXMMC);
    }

    public Integer selectTotal(String TGXMDM, String TGXMMC) {
        return sljtgqkMapper.selectTotal(TGXMDM,TGXMMC);
    }

    public void saveBatch(List<SljtgqkDao> sljtgqkDaos) {
        for (int i = 0; i<sljtgqkDaos.size(); i++){
            SljtgqkDao sljtgqkDao = sljtgqkDaos.get(i);
            sljtgqkMapper.insert(sljtgqkDao);
        }
    }

    public Integer deleteBatch(List<Integer> ids) {
        Integer result = 1;
        for(int i=0; i< ids.size(); i++){
            result = sljtgqkMapper.deleteById(ids.get(i));
            if(result==0){
                return result;
            }
        }
        return result;
    }

    public SljtgqkDao selectByTgxmmc(String TGXMMC) {
        return sljtgqkMapper.selectByTgxmmc(TGXMMC);
    }
}
