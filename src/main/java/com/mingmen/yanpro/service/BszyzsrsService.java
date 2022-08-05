package com.mingmen.yanpro.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingmen.yanpro.dao.BszyzsrsDao;
import com.mingmen.yanpro.mapper.BszyzsrsMapper;
import com.mingmen.yanpro.mapper.UserMapper;
import com.mingmen.yanpro.utils.TokenUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BszyzsrsService extends ServiceImpl<BszyzsrsMapper, BszyzsrsDao> {

    @Autowired
    private BszyzsrsMapper bszyzsrsMapper;

    @Autowired
    private UserMapper userMapper;

    public int saveBszyzsrs(BszyzsrsDao bszyzsrsDao) {
        if(bszyzsrsDao.getID()==null) { //BSZYZSRS没有id，则表示新增
            String yxsdm = TokenUtils.getCurrentUser().getYXSDM();
            String yxsmc = userMapper.selectYxsmc(TokenUtils.getCurrentUser());
            String xm = TokenUtils.getCurrentUser().getXM();
            bszyzsrsDao.setYXSDM(yxsdm);
            bszyzsrsDao.setYXSMC(yxsmc);
            bszyzsrsDao.setSCR(xm);
            return bszyzsrsMapper.insert(bszyzsrsDao);
        } else { //否则为更新
            return bszyzsrsMapper.update(bszyzsrsDao);
        }
    }

    public List<BszyzsrsDao> selectAll(){
        return bszyzsrsMapper.selectAll();
    }

    public Integer delete(String YXSDM, String ZYDM) {
        return bszyzsrsMapper.delete(YXSDM, ZYDM);
    }

    public List<BszyzsrsDao> selectPage(Integer pageNum, Integer pageSize) {
        return bszyzsrsMapper.selectPage(pageNum,pageSize);
    }

    public Integer selectTotal() {
        return bszyzsrsMapper.selectTotal();
    }

    public String selectByYXSMC(String YXSMC) {
        return bszyzsrsMapper.selectByYXSMC(YXSMC);
    }

    public BszyzsrsDao selectByZYMC(String ZYMC) {
        return bszyzsrsMapper.selectByZYMC(ZYMC);
    }

    public List<BszyzsrsDao> saveBatch(List<BszyzsrsDao> bszyzsrsDaos) {
        List<BszyzsrsDao> faillist = new ArrayList<>();
        List<BszyzsrsDao> list = new ArrayList<>();
        List<BszyzsrsDao> blist = new ArrayList<>();
        List<BszyzsrsDao> successlist = new ArrayList<>();
        for (int i = 0; i<bszyzsrsDaos.size(); i++){
            BszyzsrsDao bszyzsrsDao = bszyzsrsDaos.get(i);
            Integer match = bszyzsrsMapper.matchZYDM(bszyzsrsDao);
            if(match==1){
                list.add(bszyzsrsDao);
            }
            else{
                bszyzsrsDao.setBZ("专业代码和专业名称不匹配");
                faillist.add(bszyzsrsDao);
            }
        }
        for (int i = 0; i<list.size(); i++){
            BszyzsrsDao bszyzsrsDao = list.get(i);
            Integer match = bszyzsrsMapper.matchNZSRS(bszyzsrsDao);
            if(match==1){
                blist.add(bszyzsrsDao);
            }
            else{
                bszyzsrsDao.setBZ("拟招生人数和总指标不匹配");
                faillist.add(bszyzsrsDao);
            }
        }
        for (int i = 0; i<blist.size(); i++){
            BszyzsrsDao bszyzsrsDao = blist.get(i);
            Integer exist = bszyzsrsMapper.exist(bszyzsrsDao);
            if(exist==1){
                bszyzsrsDao.setBZ("数据重复");
                faillist.add(bszyzsrsDao);
            }
            else{
                successlist.add(bszyzsrsDao);
            }
        }
        for (int i = 0; i<successlist.size(); i++){
            BszyzsrsDao bszyzsrsDao = successlist.get(i);
            bszyzsrsMapper.insert(bszyzsrsDao);
        }
        return faillist;
    }

    public Integer deleteBatch(List<Integer> ids) {
        Integer result = 1;
        for(Integer id:ids){
            result = bszyzsrsMapper.deleteById(id);
        }
        return result;
    }

    public Integer deleteById(Integer id) {
        return bszyzsrsMapper.deleteById(id);
    }

    public IPage<BszyzsrsDao> findPage(Page<BszyzsrsDao> page, String yxsdm, String yxsmc, String zydm, String zymc, String nf) {
        return bszyzsrsMapper.findPage(page, yxsdm, yxsmc, zydm, zymc, nf);
    }

    public int getNFCZ(Integer id) {
        String shzt = bszyzsrsMapper.getshzt(id);
        String nf = bszyzsrsMapper.getnf(id);
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR) + 1);
        if(nf.equals(year)){
            if(shzt!=null){
                if(shzt.equals("0")||shzt.equals("3")||shzt.equals("5")) {
                    return 1;
                }
                else if(shzt.equals("1")) {
                    return 2;
                }
                else if(shzt.equals("2")) {
                    return 3;
                }
                else{
                    return 4;
                }
            }
            else{
                return 5;
            }
        }else {
            return 0;
        }
    }

    public boolean getNFSC(String yxsdm,String nf) {
        String shzt = bszyzsrsMapper.getSCshzt(yxsdm,nf);
        if(shzt.equals("0")) {
            return true;
        }
        else {
            return false;
        }
    }

    public String minYear(String yxsdm) {
        return bszyzsrsMapper.minYear(yxsdm);
    }

    public void updateShzt(String yxsdm, String nf, String yhz, String shr) {
        List<Integer> ids = bszyzsrsMapper.selectIds(yxsdm,nf);
        BszyzsrsDao bszyzsrsDao = new BszyzsrsDao();
        if(yhz.equals("2-1")) {
            bszyzsrsDao.setXYSHR(shr);
            for(Integer id:ids){
                bszyzsrsDao.setID(id);
                bszyzsrsMapper.updateXYShzt(bszyzsrsDao);
            }
        }
        else if(yhz.equals("1-1")) {
            bszyzsrsDao.setYYSHR(shr);
            for(Integer id:ids){
                bszyzsrsDao.setID(id);
                bszyzsrsMapper.updateYYShzt(bszyzsrsDao);
            }
        }
    }

    public int getxyldNFCZ(Integer id) {
        String shzt = bszyzsrsMapper.getshzt(id);
        String nf = bszyzsrsMapper.getnf(id);
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR) + 1);
        if(nf.equals(year)) {
            if (shzt != null) {
                if (shzt.equals("1")) {
                    return 1;
                } else if (shzt.equals("0")) {
                    return 0;
                } else {
                    return 2;
                }
            }
            else{
                return 4;
            }
        }
        else {
            return 3;
        }
    }

    public Integer getCollegeRs(String yxsdm, String nf) {
        Integer rs = bszyzsrsMapper.getZyzsrs(yxsdm, nf);
        return rs;
    }

    public List<String> getzydmandmc(String yxsdm, String nf) {
        return bszyzsrsMapper.getzydmandmc(yxsdm,nf);
    }
}
