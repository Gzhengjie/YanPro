package com.mingmen.yanpro.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingmen.yanpro.dao.GjzxjhDao;
import com.mingmen.yanpro.mapper.GjzxjhMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GjzxjhService extends ServiceImpl<GjzxjhMapper, GjzxjhDao> {

    @Autowired
    private GjzxjhMapper gjzxjhMapper;

    public boolean save(GjzxjhDao gjzxjhDao) {
        boolean b;
        if(gjzxjhDao.getID()==null) { //gjzxjh没有ID，则表示新增
            return b = gjzxjhMapper.insert(gjzxjhDao)!=0;
        } else { //否则为更新
            return b = gjzxjhMapper.update(gjzxjhDao)!=0;
        }
//        return saveOrUpdate(gjzxjhDao);
    }

    public List<GjzxjhDao> selectAll(){
        return gjzxjhMapper.selectAll();
    }

    public Integer deleteById(Integer id) {
        return gjzxjhMapper.deleteById(id);
    }

//    public List<GjzxjhDao> selectPage(Integer pageNum, Integer pageSize, String ZXJHDM, String ZXJHMC) {
//        return gjzxjhMapper.selectPage(pageNum,pageSize,ZXJHDM,ZXJHMC);
//    }

    public List<GjzxjhDao> selectPage(Integer pageNum, Integer pageSize, String ZXJHDM, String ZXJHMC) {
        return gjzxjhMapper.selectPage(pageNum,pageSize, ZXJHDM, ZXJHMC);
    }

    public Integer selectTotal(String ZXJHDM, String ZXJHMC) {
        return gjzxjhMapper.selectTotal(ZXJHDM, ZXJHMC);
    }

    public GjzxjhDao selectByZxjhdm(String ZXJHDM) {
        return gjzxjhMapper.selectByZxjhdm(ZXJHDM);
    }

    public void saveBatch(List<GjzxjhDao> gjzxjhDaos) {
        for (int i = 0; i<gjzxjhDaos.size(); i++){
            GjzxjhDao gjzxjhDao = gjzxjhDaos.get(i);
            gjzxjhMapper.insert(gjzxjhDao);
        }
    }

    public Integer deleteBatch(List<Integer> ids) {
        Integer result = 1;
        for(int i=0; i< ids.size(); i++){
            result = gjzxjhMapper.deleteById(ids.get(i));
            if(result==0){
                return result;
            }
        }
        return result;
    }

    public GjzxjhDao selectByZxjhmc(String ZXJHMC) {
        return gjzxjhMapper.selectByZxjhmc(ZXJHMC);
    }

}
