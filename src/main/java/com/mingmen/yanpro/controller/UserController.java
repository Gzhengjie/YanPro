package com.mingmen.yanpro.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mingmen.yanpro.common.Constants;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.controller.dto.UserDTO;
import com.mingmen.yanpro.dao.UserDao;
import com.mingmen.yanpro.service.UserService;
import com.mingmen.yanpro.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("")
public class UserController {

    @Autowired
    private UserService userService;

    //登录接口
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String zgh = userDTO.getZgh();
        String mm = userDTO.getMm();
        if(StrUtil.isBlank(zgh) || StrUtil.isBlank(mm)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        UserDTO dto = userService.login(userDTO);
        return Result.success(dto);
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        String zgh = userDTO.getZgh();
        String mm = userDTO.getMm();
        if(StrUtil.isBlank(zgh) || StrUtil.isBlank(mm)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return Result.success(userService.register(userDTO));
    }

    @GetMapping("/user/zhg/{zgh}")
    public Result findOne(@PathVariable String zgh) {
        QueryWrapper<UserDao> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("zgh", zgh);
        return Result.success(userService.getOne(queryWrapper));
    }

    //新增和修改
    @PostMapping("/sys/roleAuth/save")
    public Result save(@RequestBody UserDao userDao) {
        //用户管理，新增或更新
        return Result.success(userService.save(userDao));
    }

    //角色设置
    @PostMapping("/role")
    public Result updateYHZ(@RequestBody UserDao userDao){
        return Result.success(userService.roleSetting(userDao));
    }

    //查询全部操作
    @GetMapping("/sys/roleAuth/selectAll")
    public Result index() {
        return Result.success(userService.selectAll());
    }

    //通过职工号进行查询
    @GetMapping("/sys/roleAuth/selectByZGH/{ZGH}")
    public Result selectByZGH(@PathVariable String ZGH) {return Result.success(userService.selectByZGH(ZGH));}

    //通过姓名进行查询
    @GetMapping("/sys/roleAuth/selectByXM/{XM}")
    public Result selectByXM(@PathVariable String XM) {return Result.success(userService.selectByXM(XM));}

    //删除操作
//    @DeleteMapping("/roleAuth/delete/{ZGH}")
//    public Result delete(@PathVariable String ZGH) {
//        return Result.success(userService.delete(ZGH));
//    }
    @DeleteMapping("/sys/roleAuth/delete/{ID}")
    public Result delete(@PathVariable Integer ID) {
        return Result.success(userService.deleteById(ID));
    }

    //批量删除
//    @PostMapping("/roleAuth/deleteBatch")
//    public Result deleteBatch(@RequestBody List<String> ZGHs) {
//        return Result.success(userService.deleteBatch(ZGHs));
//    }
    @PostMapping("/sys/roleAuth/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(userService.deleteBatch(ids));
    }


    /**
     * 分页查询
     * 接口路径：/roleAuth/findPage
     * @RequestParam 接收参数
     * limit 第一个参数 = （pageNum-1）*pageSize
     */
    @GetMapping("/sys/roleAuth/findPage")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String XM,
                           @RequestParam(defaultValue = "") String ZGH,
                           @RequestParam(defaultValue = "") String YXSDM,
                           @RequestParam(defaultValue = "") String YHZ){
        pageNum = (pageNum - 1) * pageSize;
        XM = "%" + XM + "%";
        ZGH = "%" + ZGH + "%";
        YXSDM = "%" + YXSDM + "%";
        YHZ = "%" + YHZ + "%";
        List<UserDao> data = userService.selectPage(pageNum,pageSize,XM,ZGH,YXSDM,YHZ);
        Integer total = userService.selectTotal(XM,ZGH,YXSDM,YHZ);
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        Result result = new Result();
        return result.success(res);
    }

    /**
     * 导出接口
     */
    @GetMapping("/sys/roleAuth/export")
    public void export(HttpServletResponse response) throws Exception {
        //从数据库查询出所有数据
        List<UserDao> list = userService.selectAll();
        //通过工具类创建writer写出到磁盘路径
        //ExcelWriter writer = ExcelUtil.getWriter(true);
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("ZGH", "职工号");
        writer.addHeaderAlias("XM", "姓名");
        writer.addHeaderAlias("YXSDM", "院系所代码");
        writer.addHeaderAlias("MM", "密码");
        writer.addHeaderAlias("YHZ", "用户组");
        writer.addHeaderAlias("CJR", "创建人");
        writer.addHeaderAlias("CJSJ", "创建时间");
        writer.addHeaderAlias("GXR", "更新人");
        writer.addHeaderAlias("GXSJ", "更新时间");

        //一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.setOnlyAlias(true);
        writer.write(list, true);

        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户表", "UTF-8");
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
    @PostMapping("/sys/roleAuth/import")
    public Boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //方式1：通过JavaBean的方式读取Excel内的对象，但是要求表头必须是英文，跟JavaBean的属性要对应起来
        //List<UserDao> list = reader.readAll(UserDao.class);
        //方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<UserDao> userDaos = CollUtil.newArrayList();
        for(List<Object> row:list) {
            UserDao userDao = new UserDao();
            userDao.setZGH(row.get(0).toString());
            userDao.setXM(row.get(1).toString());
            userDao.setYXSDM(row.get(2).toString());
            userDao.setMM(row.get(3).toString());
            userDao.setYHZ(row.get(4).toString());
            userDao.setCJR(row.get(5).toString());
//            userDao.setGXR(row.get(6).toString());
            userDaos.add(userDao);
        }

        userService.saveBatch(userDaos);
        return true;
    }

    @GetMapping("/sys/getuser")
    public Result getUser() {
        return Result.success(userService.getUser());
    }
}
