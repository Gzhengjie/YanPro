package com.mingmen.yanpro.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.RoleDao;
import com.mingmen.yanpro.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    //新增和修改
    @PostMapping("/save")
    public Result save(@RequestBody RoleDao roleDao) {
        //新增或更新
        return Result.success(roleService.save(roleDao));
    }

    //查询全部操作
    @GetMapping("/selectAll")
    public Result index() {
        return Result.success(roleService.selectAll());
    }

    //删除操作
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(roleService.delete(id));
    }

    //批量删除
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(roleService.deleteBatch(ids));
    }

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/findPage")
    public Result findPage(@RequestParam Integer pageNum,
                                       @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name){
        pageNum = (pageNum - 1) * pageSize;
        name = "%" + name + "%";
        List<RoleDao> data = roleService.selectPage(pageNum,pageSize,name);
        Integer total = roleService.selectTotal(name);
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        return Result.success(res);
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        //从数据库查询出所有数据
        List<RoleDao> list = roleService.selectAll();
        //通过工具类创建writer写出到磁盘路径
        //ExcelWriter writer = ExcelUtil.getWriter(true);
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("name", "名称");
        writer.addHeaderAlias("description", "描述");
        writer.addHeaderAlias("flag", "用户组");

        //一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.setOnlyAlias(true);
        writer.write(list, true);

        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("角色表", "UTF-8");
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
    public Boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //方式1：通过JavaBean的方式读取Excel内的对象，但是要求表头必须是英文，跟JavaBean的属性要对应起来
        //List<SljtgqkDao> list = reader.readAll(SljtgqkDao.class);
        //方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<RoleDao> roleDaos = CollUtil.newArrayList();
        for(List<Object> row:list) {
            RoleDao roleDao = new RoleDao();
            roleDao.setName(row.get(0).toString());
            roleDao.setDescription(row.get(1).toString());
            roleDao.setFlag(row.get(2).toString());
            roleDaos.add(roleDao);
        }

        roleService.saveBatch(roleDaos);
        return true;
    }
}
