package com.mingmen.yanpro.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.common.Constants;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.*;
import com.mingmen.yanpro.mapper.SsXuanchuanXlyxxMapper;
import com.mingmen.yanpro.service.SsXuanchuanXlyxxService;
import com.mingmen.yanpro.utils.TokenUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/SsXuanchuanXlyxx")
public class SsXuanchuanXlyxxController {

    @Value("${files.upload.path}" + "Xly/")
    private String fileUploadPath;

    @Autowired
    private SsXuanchuanXlyxxService ssXuanchuanXlyxxService;

    /**
     * 学生填报个人夏令营信息
     * @param ssXuanchuanXlyxx
     * @return
     */
    @Transactional
    @PostMapping("/save")
    public Result save(@RequestBody SsXuanchuanXlyxx ssXuanchuanXlyxx) {
        //新增或者更新
        ssXuanchuanXlyxx.setTBSJ(DateUtil.now());
        return Result.success(ssXuanchuanXlyxxService.saveSsXuanchuanXlyxx(ssXuanchuanXlyxx));
    }

    /**
     * 查询所有学生信息
     */
    @GetMapping("/selectAll")
    public List<SsXuanchuanXlyxx> selectAll(){
        return ssXuanchuanXlyxxService.selectAll();
    }


    /**
     * 根据id删除学生
     * @param ID
     * @return
     */
    @DeleteMapping("/del/{ID}")
    public boolean delete(@PathVariable Integer ID){
        return ssXuanchuanXlyxxService.removeById(ID);
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestParam List<Integer> ids){
        if (ssXuanchuanXlyxxService.removeByIds(ids)) {
            return Result.success();
        }
        else {
            return Result.error(Constants.CODE_500, "系统错误");
        }
    }

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @param EMAIL
     * @param NF
     * @param YXSDM
     * @param SHZT
     * @return
     */
    @GetMapping("/page")
    public IPage<SsXuanchuanXlyxx> findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String EMAIL,
                           @RequestParam(defaultValue = "0") Integer NF,
                           @RequestParam(defaultValue = "") String YXSDM,
                           @RequestParam(defaultValue = "") Integer SHZT,
                           @RequestParam(defaultValue = "") String XM,
                           @RequestParam(defaultValue = "") String ZJHM
    ){
        IPage<SsXuanchuanXlyxx> page = new Page<>(pageNum,pageSize);
        QueryWrapper<SsXuanchuanXlyxx> queryWrapper = new QueryWrapper<>();
        return ssXuanchuanXlyxxService.findPage(new Page<>(pageNum,pageSize),EMAIL,NF,YXSDM,SHZT,XM,ZJHM);
    }

    /**
     * 单个审核
     * @param ID
     * @param SHZT
     * @return
     */
    @RequestMapping("/sh")  //单个审核
    public Result xxsh(@RequestParam Integer ID,
                       @RequestParam Integer SHZT){
        SsXuanchuanXlyxx ssXuanchuanXlyxx = new SsXuanchuanXlyxx();
        ssXuanchuanXlyxx.setID(ID);
        ssXuanchuanXlyxx.setSHZT(SHZT);
        ssXuanchuanXlyxx.setXYSHR(TokenUtils.getCurrentUser().getXM());
        ssXuanchuanXlyxx.setXYSHSJ(DateUtil.now());
        if (!"2-2".equals(TokenUtils.getCurrentUser().getYHZ())) {
            return Result.error(Constants.CODE_600,"您没有操作权限");
        }else{
            return Result.success(ssXuanchuanXlyxxService.saveSsXuanchuanXlyxx(ssXuanchuanXlyxx));
        }
    }

    /**
     * 批量审核
     * @param IDs
     * @param SHZT
     * @return
     */
    @PostMapping("/sh/batch")
    public Result xxbatchsh(@RequestParam List<Integer> IDs, @RequestParam Integer SHZT) {
        Integer numSuccess = 0;
        for(Integer ID : IDs){
            SsXuanchuanXlyxx ssXuanchuanXlyxx = new SsXuanchuanXlyxx();
            ssXuanchuanXlyxx.setID(ID);
            ssXuanchuanXlyxx.setSHZT(SHZT);
            ssXuanchuanXlyxx.setXYSHR(TokenUtils.getCurrentUser().getXM());
            ssXuanchuanXlyxx.setXYSHSJ(DateUtil.now());
            if (!"2-2".equals(TokenUtils.getCurrentUser().getYHZ())) {
                return Result.error(Constants.CODE_600,"您没有操作权限");
            }else{
                ssXuanchuanXlyxxService.saveSsXuanchuanXlyxx(ssXuanchuanXlyxx);
            }
            numSuccess+=1;
        }
        return Result.success(numSuccess);
    }

    @Transactional
    /**
     * 学生端上传证明材料
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file,
                        SsXuanchuanXlyxx ssXuanchuanXlyxx) throws UnknownHostException {

        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename); // 获取拓展名
        long size = file.getSize();

        // 定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type;
        File uploadFile = new File(fileUploadPath + "SupportingFiles/" + ssXuanchuanXlyxx.getYXSMC() + ssXuanchuanXlyxx.getZYMC() + fileUUID);

        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }

        String md5;
        String url;
        // 把获取到的文件存储到磁盘目录
        // 如果文件写失败抛出异常
        try{
            file.transferTo(uploadFile.getAbsoluteFile());
        } catch (IOException e){
            return Result.error("600","文件上传失败：" + e,null);
        }

        url = ssXuanchuanXlyxx.getYXSMC() + ssXuanchuanXlyxx.getZYMC() + fileUUID;
        SsXuanchuanXlyxx newSsXuanchuanXlyxx = new SsXuanchuanXlyxx();
        newSsXuanchuanXlyxx.setID(ssXuanchuanXlyxx.getID());
        newSsXuanchuanXlyxx.setSCCL(url);
        return Result.success(ssXuanchuanXlyxxService.saveSsXuanchuanXlyxx(newSsXuanchuanXlyxx));
//        // 获取文件的md5
//        md5 = SecureUtil.md5(uploadFile);
//        // 从数据库查询是否存在相同的记录
//        SsJianZhangZyxwgg dbFiles = getFileByMd5(md5);
//        if(dbFiles != null){
//            // 由于文件已存在，所以删除刚才上传的重复文件
//            uploadFile.delete();
//            return Result.error("600","文件已存在请勿重复上传",null);
//        } else {
//            // 数据库若不存在重复文件，则不删除刚才上传的文件
//            url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":9090/ss_jianzhang_zyxwgg/" + fileUUID;
//            // 存储数据库
//            SsJianZhangZyxwgg ssJianZhangZyxwgg = new SsJianZhangZyxwgg();
//            ssJianZhangZyxwgg.setWjmc(originalFilename);
//            ssJianZhangZyxwgg.setType(type);
//            ssJianZhangZyxwgg.setSize(size/1024);  //kb
//            ssJianZhangZyxwgg.setUrl(url);
//            ssJianZhangZyxwgg.setMd5(md5);
//            ssJianZhangZyxwgg.setNf(userFileReq.getNf());
//            ssJianZhangZyxwgg.setScr(userFileReq.getXm());
//            ssJianZhangZyxwgg.setYxsdm(userFileReq.getYxsdm());
//            ssJianZhangZyxwgg.setYsxmc(ssjianzhangZykskmService.yxsdm2yxsmc(userFileReq.getYxsdm()));
//
//            ssJianzhangZyxwggService.saveZyxwgg(ssJianZhangZyxwgg);
//            //return url;
//            return Result.success(true);
        }

    /**
     * 通过url下载文件
     * @param SCCL
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping("/{SCCL}")
    public Result download(@PathVariable String SCCL, HttpServletResponse response) throws IOException {
        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + "SupportingFiles/" + SCCL);
        System.out.println(uploadFile);
        if(uploadFile.exists()){
            // 设置输出流的格式
            ServletOutputStream os = response.getOutputStream();
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(SCCL, "UTF-8"));
            response.setContentType("application/octet-stream");

            // 读取文件的字节流
            os.write(FileUtil.readBytes(uploadFile));
            os.flush();
            os.close();
            return Result.success(null);
        } else {
            return Result.error("600","文件不存在，无法下载！", null);
        }

    }


    /**
     *
     *
     * 分页查询
     *
     *
     */
    @GetMapping("/self")
    public Result findSelf(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String zjhm
    ){
        IPage<SsXuanchuanXlyxx> page = new Page<>(pageNum,pageSize);
        QueryWrapper<SsXuanchuanXlyxx> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("zjhm",zjhm);
        return Result.success(ssXuanchuanXlyxxService.page(page, queryWrapper));
    }

    /**
     * 选择导出
     * @param response
     * @param IDs
     * @throws Exception
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response,
                        @RequestParam List<Integer> IDs
    ) throws Exception{
        //从数据库查询出所有的数据

        List<SsXuanchuanXlyxx> list = new ArrayList<>();
        for (Integer ID : IDs){
            SsXuanchuanXlyxx newSsXuanchuanXlyxx = ssXuanchuanXlyxxService.selectById(ID);
            list.add(newSsXuanchuanXlyxx);
        }
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        writer.addHeaderAlias("学号", "学号");
        writer.addHeaderAlias("姓名", "姓名");
        writer.addHeaderAlias("民族", "民族");
        writer.addHeaderAlias("政治面貌", "政治面貌");
        writer.addHeaderAlias("证件号码", "证件号码");
        writer.addHeaderAlias("性别", "性别");
        writer.addHeaderAlias("申请类别", "申请类别");
        writer.addHeaderAlias("出生日期", "出生日期");
        writer.addHeaderAlias("地址邮编", "地址邮编");
        writer.addHeaderAlias("入学时间", "入学时间");
        writer.addHeaderAlias("毕业时间", "毕业时间");
        writer.addHeaderAlias("电话", "电话");
        writer.addHeaderAlias("邮箱", "邮箱");
        writer.addHeaderAlias("院系所代码", "院系所代码");
        writer.addHeaderAlias("院系所名称", "院系所名称");
        writer.addHeaderAlias("专业代码", "专业代码");
        writer.addHeaderAlias("专业名称", "专业名称");
        writer.addHeaderAlias("本科生专业人数", "本科生专业人数");
        writer.addHeaderAlias("学分成绩", "学分成绩");
        writer.addHeaderAlias("前三年专业排名", "前三年专业排名");
        writer.addHeaderAlias("GPA", "GPA");
        writer.addHeaderAlias("外语等级", "外语等级");
        writer.addHeaderAlias("毕业学校", "毕业学校");
        writer.addHeaderAlias("毕业院系所", "毕业院系所");
        writer.addHeaderAlias("毕业专业", "毕业专业");
        writer.addHeaderAlias("获奖情况", "获奖情况");
        writer.addHeaderAlias("科研成果", "科研成果");
        writer.addHeaderAlias("个人简介", "个人简介");
        writer.addHeaderAlias("年份", "年份");
        writer.addHeaderAlias("备注", "备注");
        writer.addHeaderAlias("填报时间", "填报时间");
        writer.addHeaderAlias("更新时间", "更新时间");
        writer.addHeaderAlias("学院审核人", "学院审核人");
        writer.addHeaderAlias("学院审核时间", "学院审核时间");

        writer.setOnlyAlias(true);

        // 一次性写出list内的对象到Excel，使用默认样式，强制输出标题
        writer.write(list,true);
        // 自动适应列宽
        writer.autoSizeColumnAll();
        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("夏令营学生信息表", "utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    /**
     *
     *
     * excel 导入
     *
     *
     *
     */
    @PostMapping("/import")
    public Boolean imp(@RequestParam MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
         List<SsXuanchuanXlyxx> list = reader.readAll(SsXuanchuanXlyxx.class);

        inputStream.close();
        ssXuanchuanXlyxxService.saveBatch(list);
        return true;
    }

    @GetMapping("/tongji")
    public Result tongJi(@RequestParam(defaultValue = "0") int nf) {
        return Result.success(ssXuanchuanXlyxxService.tongJi(nf));
    }

}

