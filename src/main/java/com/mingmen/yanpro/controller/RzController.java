package com.mingmen.yanpro.controller;

import com.mingmen.yanpro.dao.RzDao;
import com.mingmen.yanpro.service.RzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/log")
public class RzController {

    @Autowired
    private RzService rzService;

    //新增和修改
    @PostMapping("/save")
    public Integer save(@RequestBody RzDao rzDao) {
        //新增或更新
        return rzService.save(rzDao);
    }

    //查询全部操作
    @GetMapping("/selectAll")
    public List<RzDao> index() {
        return rzService.selectAll();
    }

    //通过操作人员姓名实现查询
    @GetMapping("/selectByCZRYXM/{CZRYXM}")
    public RzDao selectByCZRYXM(@PathVariable String CZRYXM) {return rzService.selectByCZRYXM(CZRYXM);}

    //删除操作
    @DeleteMapping("/delete/{CZRYXM}")
    public Integer delete(@PathVariable String CZRYXM, String CZLX) {
        return rzService.delete(CZRYXM, CZLX);
    }

    /**
     * 分页查询
     * 接口路径：/user/page?pageNum=1&pageSize=5
     * @RequestParam 接收参数
     * limit 第一个参数 = （pageNum-1）*pageSize
     */
    @GetMapping("/findPage")
    public Map<String,Object> findPage(@RequestParam Integer pageNum,
                                       @RequestParam Integer pageSize,
                                       @RequestParam String CZRYXM){
        pageNum = (pageNum - 1) * pageSize;
        CZRYXM = "%" + CZRYXM + "%";
        List<RzDao> data = rzService.selectPage(pageNum,pageSize,CZRYXM);
        Integer total = rzService.selectTotal(CZRYXM);
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        return res;
    }
}
