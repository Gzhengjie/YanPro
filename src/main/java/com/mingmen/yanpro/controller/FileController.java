package com.mingmen.yanpro.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mingmen.yanpro.dao.FileDao;
import com.mingmen.yanpro.mapper.FileMapper;
import com.mingmen.yanpro.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 文件上传
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private FileMapper fileMapper;


    /**
     * 文件上传的接口
     * @param file 前端传过来的文件
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        //定义一个文件的唯一标识符
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type;
        File uploadFile = new File(fileUploadPath + fileUUID);
        //判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        if(!uploadFile.getParentFile().exists()) {
            uploadFile.getParentFile().mkdirs();
        }

//        String url;
//        // 获取文件的md5
//        String md5 = SecureUtil.md5(file.getInputStream());
//        // 从数据库查询是否存在相同的记录
//        FileDao dbFiles = getFileByMd5(md5);
//        if (dbFiles != null) { // 文件已存在
//            url = dbFiles.getUrl();
//        } else {
//            // 上传文件到磁盘
//            file.transferTo(uploadFile);
//            // 数据库若不存在重复文件，则不删除刚才上传的文件
//            url = "http://localhost:9090/file/" + fileUUID;
//        }
        String url;
        //上传文件到磁盘
        file.transferTo(uploadFile);
        //获取文件的md5
        String md5 = SecureUtil.md5(uploadFile);
        //从数据库查询是否存在相同的记录
        FileDao dbFileDao = getFileByMd5(md5);
        if(dbFileDao != null) {
            url = dbFileDao.getUrl();
            //由于文件已存在，所以删除刚才上传的文件
            uploadFile.delete();
        } else {
            //数据库若不存在重复文件，则不删除刚才上传的文件
            url = "http://localhost:9090/file/" + fileUUID;
        }

        //存储数据库
        FileDao saveFile = new FileDao();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        fileMapper.insert(saveFile);
        return url;
    }

    /**
     * 文件下载接口 http://localhost:9090/file/{fileUUID}
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException{
        //根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        //设置输出流的格式
        ServletOutputStream outputStream = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");

        //设置文件的字节流
        outputStream.write(FileUtil.readBytes(uploadFile));
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 通过文件的md5查询文件
     * @param md5
     * @return
     */
    private FileDao getFileByMd5(String md5) {
        //查询文件的md5是否存在
        QueryWrapper<FileDao> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<FileDao> fileDaoList = fileMapper.selectList(queryWrapper);
        return fileDaoList.size() == 0 ? null : fileDaoList.get(0);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        FileDao fileDao = fileMapper.selectById(id);
        fileDao.setIsDelete(true);
        return Result.success(fileMapper.updateById(fileDao));
    }

    @PostMapping("/update")
    public Result update(@RequestBody FileDao fileDao) {
        return Result.success(fileMapper.updateById(fileDao));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        QueryWrapper<FileDao> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("ID", ids);
        List<FileDao> fileDaos = fileMapper.selectList(queryWrapper);
        for(FileDao fileDao : fileDaos) {
            fileDao.setIsDelete(true);
            fileMapper.updateById(fileDao);
        }
        return Result.success();
    }

    /**
     * 分页查询接口
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/findPage")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<FileDao> queryWrapper = new QueryWrapper<>();
        //查询未删除的记录
        queryWrapper.eq("is_Delete", false);
        queryWrapper.orderByDesc("ID");

        return Result.success(fileMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper));

    }
}
