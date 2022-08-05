package com.mingmen.yanpro.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.College;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.mingmen.yanpro.service.ICollegeService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 院系所表 前端控制器
 * </p>
 *
 * @author gzj
 * @since 2022-07-07
 */
@RestController
@RequestMapping("/college")
public class CollegeController {

    @Resource
    private ICollegeService collegeService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody College college) {
        collegeService.saveOrUpdate(college);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        collegeService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        collegeService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(collegeService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(collegeService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {
        QueryWrapper<College> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(collegeService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/yxsmc")
    public Result findYxsmc(@RequestParam String yxsdm) {
        QueryWrapper<College> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("yxsdm", yxsdm);
        return Result.success(collegeService.getOne(queryWrapper).getYxsmc());
    }


}

