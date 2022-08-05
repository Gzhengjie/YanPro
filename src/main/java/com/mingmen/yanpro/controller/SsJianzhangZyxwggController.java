package com.mingmen.yanpro.controller;

import ch.qos.logback.core.util.FileSize;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.SsJianZhangZyxwgg;

import com.mingmen.yanpro.dao.UserFileReq;
import com.mingmen.yanpro.service.SsJianzhangZyxwggService;
import com.mingmen.yanpro.service.SsjianzhangZykskmService;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;



import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.List;

@RestController
@RequestMapping("/ss_jianzhang_zyxwgg")
public class SsJianzhangZyxwggController {

    @Value("${files.upload.path}" + "SsZyxwgg/")
    private String fileUploadPath;

    @Resource
    private SsJianzhangZyxwggService ssJianzhangZyxwggService;

    @Resource
    private SsjianzhangZykskmService ssjianzhangZykskmService;

    @Transactional
    // 专业学位公告上传接口
    @PostMapping("/upload")
    public Result upload(UserFileReq userFileReq) throws UnknownHostException {
        String originalFilename = "";

        originalFilename = userFileReq.getFile().getOriginalFilename();


        String type = FileUtil.extName(originalFilename);
        long size = userFileReq.getFile().getSize();

        // 定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type;
        File uploadFile = new File(fileUploadPath + fileUUID);

        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }

        //System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        String md5;
        String url;
        // 把获取到的文件存储到磁盘目录
        // 如果文件写失败抛出异常
        try{
            userFileReq.getFile().transferTo(uploadFile.getAbsoluteFile());
        } catch (IOException e){
            return Result.error("600","文件上传失败：" + e,null);
        }

        // 获取文件的md5
        md5 = SecureUtil.md5(uploadFile);
        // 从数据库查询是否存在相同的记录
        SsJianZhangZyxwgg dbFiles = getFileByMd5(md5);
        if(dbFiles != null){
            // 由于文件已存在，所以删除刚才上传的重复文件
            uploadFile.delete();
            return Result.error("600","文件已存在请勿重复上传",null);
        } else {
            // 数据库若不存在重复文件，则不删除刚才上传的文件
            url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":9090/ss_jianzhang_zyxwgg/" + fileUUID;
            // 存储数据库
            SsJianZhangZyxwgg ssJianZhangZyxwgg = new SsJianZhangZyxwgg();
            ssJianZhangZyxwgg.setWjmc(originalFilename);
            ssJianZhangZyxwgg.setType(type);
            ssJianZhangZyxwgg.setSize(size/1024);  //kb
            ssJianZhangZyxwgg.setUrl(url);
            ssJianZhangZyxwgg.setMd5(md5);
            ssJianZhangZyxwgg.setNf(userFileReq.getNf());
            ssJianZhangZyxwgg.setScr(userFileReq.getXm());
            ssJianZhangZyxwgg.setYxsdm(userFileReq.getYxsdm());
            ssJianZhangZyxwgg.setYsxmc(ssjianzhangZykskmService.yxsdm2yxsmc(userFileReq.getYxsdm()));

            ssJianzhangZyxwggService.saveZyxwgg(ssJianZhangZyxwgg);
            //return url;
            return Result.success(true);
        }

    }

    // 下载接口
    @GetMapping("/{fileUUID}")
    public Result download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        if(uploadFile.exists()){
            // 设置输出流的格式
            ServletOutputStream os = response.getOutputStream();
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
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

    // 真删除删了就没了
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        return Result.success(ssJianzhangZyxwggService.deleteFile(id, fileUploadPath));
    }

    // 批量真删除
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(ssJianzhangZyxwggService.deleteFiles(ids, fileUploadPath));
    }

    @PostMapping
    public Result save(@RequestBody SsJianZhangZyxwgg ssJianZhangZyxwgg){
        return Result.success(ssJianzhangZyxwggService.saveSsjianzhangZyxwgg(ssJianZhangZyxwgg));
    }

    //分页查询 - mybatis-plus的方式
    @GetMapping("/page")
    public IPage<SsJianZhangZyxwgg> findPage(@RequestParam Integer pageNum,
                                           @RequestParam Integer pageSize,
                                           @RequestParam String YXSDM,
                                           @RequestParam String WJMC, // 文件名称
                                           @RequestParam String TYPE,   // 文件类型
                                           @RequestParam String NF){       // 年份
        IPage<SsJianZhangZyxwgg> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SsJianZhangZyxwgg> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("WJMC", WJMC);
        queryWrapper.like("YXSDM", YXSDM);
        queryWrapper.like("TYPE",TYPE);
        queryWrapper.like("NF",NF);
        queryWrapper.orderByDesc("ID");
        return ssJianzhangZyxwggService.page(page, queryWrapper);
    }


    // 通过文件的md5查询文件
    private SsJianZhangZyxwgg getFileByMd5(String md5){
        // 查询文件的md5是否存在
        QueryWrapper<SsJianZhangZyxwgg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("MD5", md5);
        List<SsJianZhangZyxwgg> filesList = ssJianzhangZyxwggService.list(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

}
