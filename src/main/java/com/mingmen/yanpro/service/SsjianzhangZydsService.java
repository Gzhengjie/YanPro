package com.mingmen.yanpro.service;


import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingmen.yanpro.dao.SsJianzhangZyds;

import com.mingmen.yanpro.mapper.SsJianzhangZydsMapper;
import com.mingmen.yanpro.utils.JianzhangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mingmen.yanpro.utils.JianzhangUtils.list2str;

@Service
public class SsjianzhangZydsService extends ServiceImpl<SsJianzhangZydsMapper, SsJianzhangZyds> {
    @Autowired
    private SsJianzhangZydsMapper ssJianzhangZydsMapper;

    // 借用一下zykskmservice里的查询基础表的接口
    @Resource
    private SsjianzhangZykskmService ssjianzhangZykskmService;

    public int saveSsJianzhangZyds(SsJianzhangZyds ssJianzhangZyds){
        if(ssJianzhangZyds.getId() == null){
            //前端传过来的是个list类型，但数据库里存储的是string类型，所以要转化一下。
            ssJianzhangZyds.setZdjs(list2str(ssJianzhangZyds.getZdjslist()));

            return ssJianzhangZydsMapper.insert(ssJianzhangZyds);
        } else {
//            // 如果指导教师里面是空的话，我要在这里设置一个“无”
//            if(ssJianzhangZyds.getZdjslist().size() < 1){
//                List<String> zdjsList = new ArrayList<>();
//                zdjsList.add("无 ");
//                ssJianzhangZyds.setZdjslist(zdjsList);
//            }
            //前端传过来的是个list类型，但数据库里存储的是string类型，所以要转化一下。
            ssJianzhangZyds.setZdjs(list2str(ssJianzhangZyds.getZdjslist()));
            return ssJianzhangZydsMapper.myUpdate(ssJianzhangZyds);
        }
    }

    public List<String> getDS(String zsyxsdm, String zszydm, String nf) {
        //获取学硕导师
        List<String> xsdsTempList = ssJianzhangZydsMapper.getXSDS(zsyxsdm, zszydm, nf);
        //获取专硕导师
        List<String> zsdsTempList = ssJianzhangZydsMapper.getZSDS(zsyxsdm, zszydm, nf);

        List<String> DS_list_temp = new ArrayList<>(xsdsTempList);
        List<String> DS_list = new ArrayList<>();
        DS_list_temp.addAll(zsdsTempList);
        DS_list_temp.add("无");
        for(String ds : DS_list_temp){
            DS_list.add(ds+" ");
        }

        LinkedHashSet<String> hashSet = new LinkedHashSet<>(DS_list);

        return new ArrayList<>(hashSet);
    }


    public void updateSHR_SHSJ(String yxsdm, String nf, String shenheren, String yhz) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        UpdateWrapper<SsJianzhangZyds> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("YXSDM", yxsdm);
        updateWrapper.eq("NF", nf);

        SsJianzhangZyds ssJianzhangZyds = new SsJianzhangZyds();
        if (yhz.equals("1-1")){
            ssJianzhangZyds.setYyshr(shenheren);
            ssJianzhangZyds.setYyshsj(formatter.format(calendar.getTime()));
        } else if (yhz.equals("2-1")) {
            ssJianzhangZyds.setXyshr(shenheren);
            ssJianzhangZyds.setXyshsj(formatter.format(calendar.getTime()));
        }

        ssJianzhangZydsMapper.update(ssJianzhangZyds, updateWrapper);
    }

    public List<SsJianzhangZyds> saveWihtCheck(List<SsJianzhangZyds> ssJianzhangZydsList) {
        List<SsJianzhangZyds> failList = new ArrayList<>();
        for(SsJianzhangZyds temp : ssJianzhangZydsList){
            // 校验是否为空，即null或空字符
            if(StrUtil.isBlank(temp.getZydm()) || StrUtil.isBlank(temp.getYjfxdm())
            || StrUtil.isBlank(temp.getZdjs())){
                temp.setFalseReason("专业代码、研究方向代码或指导教师不能为空，如无指导教师请填写“无”");
                failList.add(temp);
                continue;
            } else if(!JianzhangUtils.isDBData(temp.getZydm(), ssjianzhangZykskmService.getZYDM(temp.getYxsdm(), temp.getNf()))
                    || !JianzhangUtils.isDBData(temp.getYjfxdm(), ssjianzhangZykskmService.getCollegeYJFXDM(temp.getYxsdm(), temp.getNf()))
                    ){
                // 校验数据是否合法，即是否在数据库基础表里
                // 研究方向代码，先在这里校验，有就行
                temp.setFalseReason("专业代码或研究方向代码错误，请检查上传信息或维护基础表");
                failList.add(temp);
                continue;
            }
            else {
                List<String> dsList = JianzhangUtils.str2list(temp.getZdjs());
                List<String> dsListFromDB = this.getDS(temp.getYxsdm(), temp.getZydm(), temp.getNf());
                StringBuilder dsNotFromDB = new StringBuilder();
                // 如果上传的文件存在某些导师不在导师表里，则进行下面的操作
                if (! new HashSet<>(dsListFromDB).containsAll(dsList)){
                    // 找出来具体哪些导师不在导师表里
                    for(String ds : dsList){
                        if (!JianzhangUtils.isDBData(ds, dsListFromDB)){
                            dsNotFromDB.append(ds).append("、");
                        }
                        // 删除最后一个逗号
                        dsNotFromDB.deleteCharAt(dsNotFromDB.length()-1);
                    }
                    temp.setFalseReason("导师“" + dsNotFromDB + "”不在硕士招生导师表中，请检查上传信息或维护基础表");
                    failList.add(temp);
                    continue;
                }

            }

            temp.setZymc(ssjianzhangZykskmService.getZYMC(temp.getZydm(), temp.getNf()));
            //在这里根据研究方向代码和专业名称查询研究方向名称，如果没查出来，说明缺少研究方向代码
            String yjfxmc = ssjianzhangZykskmService.getYJFXMC(temp.getYjfxdm(), ssjianzhangZykskmService.getZYMC(temp.getZydm(), temp.getNf()), temp.getNf());
            if(StrUtil.isBlank(yjfxmc)){
                temp.setFalseReason("研究方向表中“"+ ssjianzhangZykskmService.getZYMC(temp.getZydm(), temp.getNf()) + "”专业无相关方向，请先维护基础表");
                failList.add(temp);
            } else {
                temp.setYjfxmc(yjfxmc);
                if(ssJianzhangZydsMapper.insert(temp) < 1){
                    temp.setFalseReason("不能导入重复数据");
                    failList.add(temp);
                }
            }
        }
        return failList;
    }
}
