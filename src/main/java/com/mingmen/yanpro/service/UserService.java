package com.mingmen.yanpro.service;

import cn.hutool.log.Log;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingmen.yanpro.common.Constants;
import com.mingmen.yanpro.controller.dto.UserDTO;
import com.mingmen.yanpro.dao.UserDao;
import com.mingmen.yanpro.exception.ServiceException;
import com.mingmen.yanpro.mapper.UserMapper;
import com.mingmen.yanpro.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, UserDao> {

    @Autowired
    private UserMapper userMapper;
    private static final Log LOG = Log.get();

    public boolean save(UserDao userDao) {
        Boolean b;
        if(userDao.getID()==null) { //user没有id，则表示新增
            return b = userMapper.insert(userDao)!=0;
        } else { //否则为更新
            return b = userMapper.update(userDao)!=0;
        }
    }

    public List<UserDao> selectAll(){
        return userMapper.selectAll();
    }

    public Integer deleteById(Integer ID) {
        return userMapper.deleteById(ID);
    }

    public List<UserDao> selectPage(Integer pageNum, Integer pageSize, String XM, String ZGH, String YXSDM, String YHZ) {
        return userMapper.selectPage(pageNum,pageSize,XM,ZGH,YXSDM,YHZ);
    }

    public Integer selectTotal(String XM, String ZGH, String YXSDM, String YHZ) {
        return userMapper.selectTotal(XM,ZGH,YXSDM,YHZ);
    }

    public UserDao selectByXM(String XM) {
        return userMapper.selectByXM(XM);
    }

    public Integer roleSetting(UserDao userDao) {
        return userMapper.updateYHZ(userDao);
    }

    public void saveBatch(List<UserDao> userDaos) {
        for (int i = 0; i<userDaos.size(); i++){
            UserDao userDao = userDaos.get(i);
            userMapper.insert(userDao);
        }
    }

    public Integer deleteBatch(List<Integer> ids) {
        Integer result = 1;
        for(int i=0; i< ids.size(); i++){
            result = userMapper.deleteById(ids.get(i));
            if(result==0){
                return result;
            }
        }
        return result;
    }

    public UserDao selectByZGH(String ZGH) {
        return userMapper.selectByZGH(ZGH);
    }

    public UserDTO login(UserDTO userDTO) {
        UserDao one = getUserInfo(userDTO);
        if(one != null) {
            BeanUtil.copyProperties(one, userDTO, true);
            //设置token
            String token = TokenUtils.genToken(one.getID().toString(), one.getMM());
            userDTO.setToken(token);
            String yxsmc = userMapper.selectYxsmc(one);
            userDTO.setYxsmc(yxsmc);
            return userDTO;
        } else {
            throw new ServiceException(Constants.CODE_600, "职工号或密码错误");
        }
    }

    public UserDao getUserInfo(UserDTO userDTO) {
        QueryWrapper<UserDao> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ZGH", userDTO.getZgh());
        queryWrapper.eq("MM", userDTO.getMm());
        UserDao one;
        //处理异常情况
        try {
            one = getOne(queryWrapper);  //从数据库查询用户信息
//            return one;
        } catch(Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }

    public UserDao getUser() {
        UserDao user = TokenUtils.getCurrentUser();
        return user;
    }

    public UserDao register(UserDTO userDTO) {
        UserDao one = getUserInfo(userDTO);
        if(one == null) {
            one = new UserDao();
            BeanUtil.copyProperties(userDTO, one, true);
            one.setYXSDM("001");
            save(one);
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return one;
    }
}
