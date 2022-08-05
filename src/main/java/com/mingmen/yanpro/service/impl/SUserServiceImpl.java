package com.mingmen.yanpro.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingmen.yanpro.common.Constants;
import com.mingmen.yanpro.common.RoleEnum;
import com.mingmen.yanpro.common.ValidationEnum;
import com.mingmen.yanpro.controller.dto.SUserDTO;
import com.mingmen.yanpro.controller.dto.UserPasswordDTO;
import com.mingmen.yanpro.dao.SUser;
import com.mingmen.yanpro.dao.Validation;
import com.mingmen.yanpro.exception.ServiceException;
import com.mingmen.yanpro.mapper.SUserMapper;
import com.mingmen.yanpro.service.IUserService;
import com.mingmen.yanpro.service.IValidationService;
import com.mingmen.yanpro.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
@Transactional
public class SUserServiceImpl extends ServiceImpl<SUserMapper,SUser> implements IUserService{

    @Autowired(required = false)
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    private static final Log LOG = Log.get();

    @Resource
    private SUserMapper SUserMapper;
/*
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

 */

    @Resource
    private IValidationService validationService;

    @Override
    public SUserDTO login(SUserDTO SUserDTO) {
        // 用户密码 md5加密
        SUserDTO.setPassword(SecureUtil.md5(SUserDTO.getPassword()));

        /**
         * 检验用户是否存在
         */
        SUser user = getUserInfo2(SUserDTO);
        if(user!=null){
            /**
             * 检验用户存在之后
             * 检验用户名和密码对应起来
             */
            SUser sone = getUserInfo(SUserDTO);
            if (sone != null) {
                BeanUtil.copyProperties(sone, SUserDTO, true);
                // 设置token
                String token = TokenUtils.genToken(sone.getId().toString(), sone.getPassword());
                SUserDTO.setToken(token);

                String role = sone.getRole(); // ROLE_ADMIN
                // 设置用户的菜单列表
                //List<Menu> roleMenus = getRoleMenus(role);
                //userDTO.setMenus(roleMenus);
                return SUserDTO;
            } else {
                throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
            }
        }else {
            throw new ServiceException(Constants.CODE_600, "用户不存在");
        }


    }

    @Override
    public SUser register(SUserDTO SUserDTO) {
        // 用户密码 md5加密
        SUserDTO.setPassword(SecureUtil.md5(SUserDTO.getPassword()));

        /**
         * 先检验用户是否存在
         */
        SUser sone = getUserInfo2(SUserDTO);
        if (sone == null) {
            sone = new SUser();
            BeanUtil.copyProperties(SUserDTO, sone, true);
            // 默认一个普通用户的角色
            sone.setRole(RoleEnum.ROLE_STUDENT.toString());
            if (sone.getNickname() == null) {
                sone.setNickname(sone.getUsername());
            }
            save(sone);  // 把 copy完之后的用户对象存储到数据库
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return sone;
    }

    @Override
    public void updatePassword(UserPasswordDTO userPasswordDTO) {
        int update = SUserMapper.updatePassword(userPasswordDTO);
        if (update < 1) {
            throw new ServiceException(Constants.CODE_600, "密码错误");
        }
    }

    @Override
    public Page<SUser> findPage(Page<SUser> page, String username, String email, String address) {
        return SUserMapper.findPage(page, username, email, address);
    }

    @Override
    public SUserDTO loginEmail(SUserDTO SUserDTO) {
        String email = SUserDTO.getEmail();
        String code = SUserDTO.getCode();

        // 如果验证通过了，就查询有无用户的信息
        QueryWrapper<SUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("email", email);  //存根据email查询用户信息
        SUser suser = getOne(userQueryWrapper);

        if (suser == null) {
            throw new ServiceException("-1", "未找到用户");
        }

        // 先查询 邮箱验证的表，看看之前有没有发送过  邮箱code，如果不存在，就重新获取
        QueryWrapper<Validation> validationQueryWrapper = new QueryWrapper<>();
        validationQueryWrapper.eq("email", email);
        validationQueryWrapper.eq("code", code);
        validationQueryWrapper.ge("time", new Date());  // 查询数据库没过期的code, where time >= new Date()
        Validation one = validationService.getOne(validationQueryWrapper);
        if (one == null) {
            throw new ServiceException("-1", "验证码过期，请重新获取");
        }


        BeanUtil.copyProperties(suser, SUserDTO, true);
        // 设置token
        String token = TokenUtils.genToken(suser.getId().toString(), suser.getPassword());
        SUserDTO.setToken(token);

        String role = suser.getRole();
        // 设置用户的菜单列表
       // List<Menu> roleMenus = getRoleMenus(role);
        //userDTO.setMenus(roleMenus);
        return SUserDTO;

    }

    @Override
    public void sendEmailCode(String email, Integer type)  {
        Date now = new Date();

        // 先查询同类型code
        QueryWrapper<Validation> validationQueryWrapper = new QueryWrapper<>();
        validationQueryWrapper.eq("email", email);
        validationQueryWrapper.eq("type", type);
        validationQueryWrapper.ge("time", now);  // 查询数据库没过期的code
        Validation validation = validationService.getOne(validationQueryWrapper);
        if (validation != null) {
            throw new ServiceException("-1", "当前您的验证码仍然有效，请不要重复发送");
        }

        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(from);  // 发送人
        message.setTo(email);
        message.setSentDate(now);
        String code = RandomUtil.randomNumbers(4); // 随机一个 4位长度的验证码

        if (ValidationEnum.LOGIN.getCode().equals(type)) {

            message.setSubject("【西北农林科技大学】登录邮箱验证");
            message.setText("您本次登录的验证码是：" + code + "，有效期5分钟。请妥善保管，切勿泄露");

        }
        else if (ValidationEnum.FORGET_PASS.getCode().equals(type)){
            message.setSubject("【西北农林科技大学】忘记密码验证");
            message.setText("您的验证码是"+code);
        }
        try{
            javaMailSender.send(message);
        }catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_600, "邮件发送失败");
        }
        // 发送成功之后，把验证码存到数据库
        validationService.saveCode(email, code, type, DateUtil.offsetMinute(now, 5));

    }

    private SUser getUserInfo(SUserDTO SUserDTO) {
        QueryWrapper<SUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", SUserDTO.getEmail());
        queryWrapper.eq("password", SUserDTO.getPassword());
        SUser sone;
        try {
            sone = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return sone;
    }

    private SUser getUserInfo2(SUserDTO SUserDTO) {
        QueryWrapper<SUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", SUserDTO.getEmail());
        SUser sone;
        try {
            sone = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return sone;
    }

    /**
     * 获取当前角色的菜单列表
     */
    /*
    private List<Menu> getRoleMenus(String roleFlag) {
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        // 当前角色的所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

        // 查出系统所有的菜单(树形)
        List<Menu> menus = menuService.findMenus("");
        // new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf()  移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
*/
}
