package com.mingmen.yanpro.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.SsJianZhangZyxwgg;

import com.mingmen.yanpro.dao.SsJianzhangZykskm;
import com.mingmen.yanpro.mapper.SsJianzhangZyxwggMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Service
public class SsJianzhangZyxwggService extends ServiceImpl<SsJianzhangZyxwggMapper, SsJianZhangZyxwgg> {



    @Autowired
    private SsJianzhangZyxwggMapper ssJianzhangZyxwggMapper;

    public void saveZyxwgg(SsJianZhangZyxwgg ssJianZhangZyxwgg){
        ssJianzhangZyxwggMapper.insert(ssJianZhangZyxwgg);
    }

    public void updateSHR_SHSJ(String yxsdm, String nf, String shenheren, String yhz) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        UpdateWrapper<SsJianZhangZyxwgg> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("YXSDM", yxsdm);
        updateWrapper.eq("NF", nf);

        SsJianZhangZyxwgg ssJianZhangZyxwgg = new SsJianZhangZyxwgg();
        if (yhz.equals("1-1")){
            ssJianZhangZyxwgg.setYyshr(shenheren);
            ssJianZhangZyxwgg.setYyshsj(formatter.format(calendar.getTime()));
        } else if (yhz.equals("2-1")) {
            ssJianZhangZyxwgg.setXyshr(shenheren);
            ssJianZhangZyxwgg.setXyshsj(formatter.format(calendar.getTime()));
        }

        ssJianzhangZyxwggMapper.update(ssJianZhangZyxwgg, updateWrapper);
    }

    public int saveSsjianzhangZyxwgg(SsJianZhangZyxwgg ssJianZhangZyxwgg) {
        if (ssJianZhangZyxwgg.getId() == null){
            return 0;
        } else {
            return ssJianzhangZyxwggMapper.myUpdate(ssJianZhangZyxwgg.getBz(), ssJianZhangZyxwgg.getId());
        }
    }

    @Transactional
    public Object deleteFile(Integer id, String fileUploadPath) {
        SsJianZhangZyxwgg ssJianZhangZyxwgg = ssJianzhangZyxwggMapper.selectById(id);
        List<String> fileUUID_list = Arrays.asList(ssJianZhangZyxwgg.getUrl().split("/"));
        String fileUUID = fileUUID_list.get(fileUUID_list.size() - 1);
        File file = new File(fileUploadPath + fileUUID);
        // 确保文件存在才能执行删除操作
        if(file.exists()){
            file.delete();
        }
        return ssJianzhangZyxwggMapper.deleteById(id);
    }

    @Transactional
    public Object deleteFiles(List<Integer> ids, String fileUploadPath) {
        QueryWrapper<SsJianZhangZyxwgg> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("ID", ids);
        List<String> fileUUID_list;
        String fileUUID;
        File file;
        List<SsJianZhangZyxwgg> ssJianZhangZyxwggs = ssJianzhangZyxwggMapper.selectList(queryWrapper);
        for(SsJianZhangZyxwgg ssJianZhangZyxwgg : ssJianZhangZyxwggs){
            fileUUID_list = Arrays.asList(ssJianZhangZyxwgg.getUrl().split("/"));
            fileUUID = fileUUID_list.get(fileUUID_list.size() - 1);
            file = new File(fileUploadPath + fileUUID);
            // 确保文件存在才能执行删除操作
            if(file.exists()){
                file.delete();
            }

            ssJianzhangZyxwggMapper.deleteById(ssJianZhangZyxwgg.getId());
        }

        return true;
    }
}
