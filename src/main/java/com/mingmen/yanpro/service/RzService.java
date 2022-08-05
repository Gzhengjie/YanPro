package com.mingmen.yanpro.service;

import com.mingmen.yanpro.dao.RzDao;
import com.mingmen.yanpro.mapper.RzMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RzService {

    @Autowired
    private RzMapper rzMapper;

    public int save(RzDao rzDao) {
        if(rzDao.getID()==null) { //rz没有id，则表示新增
            return rzMapper.insert(rzDao);
        } else { //否则为更新
            return rzMapper.update(rzDao);
        }
    }

    public List<RzDao> selectAll(){
        return rzMapper.selectAll();
    }

    public Integer delete(String CZRYXM, String CZLX) {
        return rzMapper.delete(CZRYXM, CZLX);
    }

    public List<RzDao> selectPage(Integer pageNum, Integer pageSize, String ZXJHMC) {
        return rzMapper.selectPage(pageNum,pageSize,ZXJHMC);
    }

    public Integer selectTotal(String ZXJHMC) {
        return rzMapper.selectTotal(ZXJHMC);
    }

    public RzDao selectByCZRYXM(String CZRYXM) {
        return rzMapper.selectByCZRYXM(CZRYXM);
    }
}
