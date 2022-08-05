package com.mingmen.yanpro.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.common.Constants;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.controller.dto.SUserDTO;
import com.mingmen.yanpro.controller.dto.UserPasswordDTO;
import com.mingmen.yanpro.dao.SUser;
import com.mingmen.yanpro.dao.Validation;
import com.mingmen.yanpro.exception.ServiceException;
import com.mingmen.yanpro.service.IUserService;
import com.mingmen.yanpro.service.IValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/user")
public class SUserController {

    @Value("${files.upload.path}")
    private String filesUploadPath;

    //@Resource
    @Autowired
    private IUserService userService;

    @Resource
    private IValidationService validationService;

    @PostMapping("/loginAccount")
    public Result login(@RequestBody SUserDTO SUserDTO) {
        String username = SUserDTO.getUsername();
        String password = SUserDTO.getPassword();
        String email = SUserDTO.getEmail();
        System.out.println(username+"ADfsf");
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password) ) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        SUserDTO dto = userService.login(SUserDTO);
        return Result.success(dto);
    }


    @PostMapping("/loginEmail")
    public Result loginEmail(@RequestBody SUserDTO SUserDTO) {
        String email = SUserDTO.getEmail();
        String code = SUserDTO.getCode();
        if (StrUtil.isBlank(email) || StrUtil.isBlank(code)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        SUserDTO dto = userService.loginEmail(SUserDTO);
        return Result.success(dto);
    }

    @PostMapping("/register")
    public Result register(@RequestBody SUserDTO SUserDTO) {
        String username = SUserDTO.getUsername();
        String password = SUserDTO.getPassword();
        String email = SUserDTO.getEmail();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password) || StrUtil.isBlank(email)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return Result.success(userService.register(SUserDTO));
    }

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody SUser suser) {
        if (suser.getId() == null && suser.getPassword() == null) {  // 新增用户默认密码
            suser.setPassword( SecureUtil.md5("123"));
        }
        return Result.success(userService.saveOrUpdate(suser));
    }

    /**
     * 修改密码
     * @param userPasswordDTO
     * @return
     */
    @PostMapping("/password")
    public Result password(@RequestBody UserPasswordDTO userPasswordDTO) {
        userPasswordDTO.setPassword(SecureUtil.md5(userPasswordDTO.getPassword()));
        userPasswordDTO.setNewPassword(SecureUtil.md5(userPasswordDTO.getNewPassword()));
        userService.updatePassword(userPasswordDTO);
        return Result.success();
    }

    // 忘记密码 | 重置密码
    @PostMapping("/reset")
    public Result reset(@RequestBody UserPasswordDTO userPasswordDTO) {
        if (StrUtil.isBlank(userPasswordDTO.getEmail()) || StrUtil.isBlank(userPasswordDTO.getCode())) {
            throw new ServiceException("-1", "参数异常");
        }
       // System.out.printf(userPasswordDTO.getEmail());
        // 先查询 邮箱验证的表，看看之前有没有发送过  邮箱code，如果不存在，就重新获取
        QueryWrapper<Validation> validationQueryWrapper = new QueryWrapper<>();
        validationQueryWrapper.eq("email", userPasswordDTO.getEmail());
        validationQueryWrapper.eq("code", userPasswordDTO.getCode());
        validationQueryWrapper.ge("time", new Date());  // 查询数据库没过期的code, where time >= new Date()
        Validation one = validationService.getOne(validationQueryWrapper);
        if (one == null) {
            throw new ServiceException("-1", "验证码过期，请重新获取");
        }

        // 如果验证通过了，就查询要不过户的信息
        QueryWrapper<SUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("email", userPasswordDTO.getEmail());  //存根据email查询用户信息
        SUser suser = userService.getOne(userQueryWrapper);

        // 重置密码
        //userPasswordDTO.setPassword(SecureUtil.md5(userPasswordDTO.getPassword()));
       // userPasswordDTO.setNewPassword(SecureUtil.md5(userPasswordDTO.getNewPassword()));
       // userService.updatePassword(userPasswordDTO);
       // return Result.success();
        String newpassword = userPasswordDTO.getNewPassword();
        //System.out.printf(newpassword);
        suser.setPassword(SecureUtil.md5(newpassword));
        userService.updateById(suser);
         return Result.success();
    }


    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(userService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(userService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(userService.list());
    }

    // 发送邮箱验证码
    @GetMapping("/email/{email}/{type}")
    public Result sendEmailCode(@PathVariable String email,@PathVariable Integer type)  {
        if(StrUtil.isBlank(email)) {
            throw new ServiceException(Constants.CODE_400, "参数错误");
        }
        if(type==null) {
            throw new ServiceException(Constants.CODE_400, "参数错误");
        }
        userService.sendEmailCode(email,type);
        return Result.success();
    }


    @GetMapping("/role/{role}")
    public Result findUsersByRole(@PathVariable String role) {
        QueryWrapper<SUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", role);
        List<SUser> list = userService.list(queryWrapper);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    @GetMapping("/username/{username}")
    public Result findByUsername(@PathVariable String username) {
        QueryWrapper<SUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return Result.success(userService.getOne(queryWrapper));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam(defaultValue = "") String username,
                               @RequestParam(defaultValue = "") String email,
                               @RequestParam(defaultValue = "") String address) {

//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        if (!"".equals(username)) {
//            queryWrapper.like("username", username);
//        }
//        if (!"".equals(email)) {
//            queryWrapper.like("email", email);
//        }
//        if (!"".equals(address)) {
//            queryWrapper.like("address", address);
//        }

        return Result.success(userService.findPage(new Page<>(pageNum, pageSize), username, email, address));
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<SUser> list = userService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("nickname", "昵称");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("createTime", "创建时间");
        writer.addHeaderAlias("avatarUrl", "头像");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<User> list = reader.readAll(User.class);

        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<SUser> susers = CollUtil.newArrayList();
        for (List<Object> row : list) {
            SUser suser = new SUser();
            suser.setUsername(row.get(0).toString());
            suser.setPassword(row.get(1).toString());
            suser.setNickname(row.get(2).toString());
            suser.setEmail(row.get(3).toString());
            suser.setPhone(row.get(4).toString());
            suser.setAddress(row.get(5).toString());
            suser.setAvatarUrl(row.get(6).toString());
            susers.add(suser);
        }

        userService.saveBatch(susers);
        return Result.success(true);
    }

}

