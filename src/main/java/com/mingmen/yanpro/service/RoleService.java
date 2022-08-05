package com.mingmen.yanpro.service;

import com.mingmen.yanpro.dao.GjzxjhDao;
import com.mingmen.yanpro.dao.RoleDao;
import com.mingmen.yanpro.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    public int save(RoleDao roleDao) {
        if(roleDao.getID()==null) { //rz没有id，则表示新增
            return roleMapper.insert(roleDao);
        } else { //否则为更新
            return roleMapper.update(roleDao);
        }
    }

    public List<RoleDao> selectAll(){
        return roleMapper.selectAll();
    }

    public Integer delete(Integer id) {
        return roleMapper.delete(id);
    }

    public List<RoleDao> selectPage(Integer pageNum, Integer pageSize, String name) {
        return roleMapper.selectPage(pageNum,pageSize, name);
    }

    public Integer selectTotal(String name) {
        return roleMapper.selectTotal(name);
    }

    public void saveBatch(List<RoleDao> roleDaos) {
        for (RoleDao roleDao:roleDaos) {
            roleMapper.insert(roleDao);
        }
    }

    public Integer deleteBatch(List<Integer> ids) {
        Integer result = 1;
        for(int i=0; i< ids.size(); i++){
            result = roleMapper.delete(ids.get(i));
            if(result==0){
                return result;
            }
        }
        return result;
    }
}
