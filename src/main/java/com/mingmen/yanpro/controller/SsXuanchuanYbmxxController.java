package com.mingmen.yanpro.controller;

import com.mingmen.yanpro.dao.SsXuanchuanYbmxx;
import com.mingmen.yanpro.mapper.SsXuanchuanYbmxxMapper;
import com.mingmen.yanpro.service.SsXuanchuanYbmxxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ss_xuanchuan_ybmxx")

public class SsXuanchuanYbmxxController {

    @Autowired
    private SsXuanchuanYbmxxMapper ssXuanchuanYbmxxMapper;

    @Autowired
    private SsXuanchuanYbmxxService ssXuanchuanYbmxxService;

    // 新增和修改
    @PostMapping
    public Integer save(@RequestBody SsXuanchuanYbmxx ssXuanchuanYbmxx){
        return ssXuanchuanYbmxxService.save(ssXuanchuanYbmxx);
    }

    // 查询所有数据
    @GetMapping
    public List<SsXuanchuanYbmxx> index(){
        List<SsXuanchuanYbmxx> all=ssXuanchuanYbmxxMapper.findAll();
        return all;
    }

    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id){
        return ssXuanchuanYbmxxMapper.deleteById(id);
    }
}
