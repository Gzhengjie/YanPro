package com.mingmen.yanpro.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingmen.yanpro.dao.BsjianzhangDao;
import com.mingmen.yanpro.dao.BszsjzDao;
import com.mingmen.yanpro.dao.BszyzsrsDao;
import com.mingmen.yanpro.mapper.BszsjzMapper;
import com.mingmen.yanpro.mapper.BszyzsrsMapper;
import com.mingmen.yanpro.mapper.UserMapper;
import com.mingmen.yanpro.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Service
public class BszsjzService extends ServiceImpl<BszsjzMapper, BszsjzDao> {

    @Autowired
    private BszsjzMapper bszsjzMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BszyzsrsMapper bszyzsrsMapper;

    public int saveBszsjz(BszsjzDao bszsjzDao) {
        if(bszsjzDao.getID()==null) { //bszsjz没有id，则表示新增
            String yxsdm = TokenUtils.getCurrentUser().getYXSDM();
            String yxsmc = userMapper.selectYxsmc(TokenUtils.getCurrentUser());
            String xm = TokenUtils.getCurrentUser().getXM();
            bszsjzDao.setYXSDM(yxsdm);
            bszsjzDao.setYXSMC(yxsmc);
            bszsjzDao.setSCR(xm);
            return bszsjzMapper.insert(bszsjzDao);
        } else { //否则为更新
            return bszsjzMapper.update(bszsjzDao);
        }
    }

    public List<BszsjzDao> selectAll(){
        return bszsjzMapper.selectAll();
    }

    public Integer delete(String YXSDM, String YJFXDM) {
        return bszsjzMapper.delete(YXSDM, YJFXDM);
    }

    public List<BszsjzDao> selectPage(Integer pageNum, Integer pageSize) {
        return bszsjzMapper.selectPage(pageNum,pageSize);
    }

    public Integer selectTotal() {
        return bszsjzMapper.selectTotal();
    }

    public BszsjzDao selectByYJFXDM(String YJFXDM) {
        return bszsjzMapper.selectByYJFXDM(YJFXDM);
    }

    public List<BszsjzDao> saveBatch(List<BszsjzDao> bszsjzDaos) {
        List<BszsjzDao> faillist = new ArrayList<>();
        List<BszsjzDao> list = new ArrayList<>();
        List<BszsjzDao> slist = new ArrayList<>();
        List<BszsjzDao> successlist = new ArrayList<>();
        for (int i = 0; i<bszsjzDaos.size(); i++){
            BszsjzDao bszsjzDao = bszsjzDaos.get(i);
            BszyzsrsDao bszyzsrsDao = new BszyzsrsDao();
            bszyzsrsDao.setYXSDM(bszsjzDao.getYXSDM());
            bszyzsrsDao.setZYDM(bszsjzDao.getZYDM());
            bszyzsrsDao.setZYMC(bszsjzDao.getZYMC());
            Integer match = bszsjzMapper.match(bszsjzDao);
            Integer matchzydm = bszyzsrsMapper.matchZYDM(bszyzsrsDao);
            if(match==0){
                bszsjzDao.setBZ("专业代码和专业名称不匹配，请检查是否正确填写");
                faillist.add(bszsjzDao);
            }
            else if(matchzydm==0){
                bszsjzDao.setBZ("研究方向代码和研究方向名称不匹配，请检查是否正确填写");
                faillist.add(bszsjzDao);
            }
            else {
                list.add(bszsjzDao);
            }
        }
        for (int i = 0; i<list.size(); i++){
            BszsjzDao bszsjzDao = list.get(i);
            List<String> zdjs = this.getAllZDJS(bszsjzDao.getZYDM(),bszsjzDao.getYXSDM());
            String js =  bszsjzDao.getZDJS();
            List<String> zdjslist = Arrays.asList(js.split(" "));
            if(zdjs.containsAll(zdjslist)==true){
                slist.add(bszsjzDao);
            }else{
                bszsjzDao.setBZ("指导教师不匹配，请检查是否正确填写");
                faillist.add(bszsjzDao);
            }
        }
        for (int i = 0; i<slist.size(); i++){
            BszsjzDao bszsjzDao = slist.get(i);
            Integer exist = bszsjzMapper.exist(bszsjzDao);
            if(exist==1){
                bszsjzDao.setBZ("数据重复");
                faillist.add(bszsjzDao);
            }
            else{
                successlist.add(bszsjzDao);
            }
        }
        for (int i = 0; i<successlist.size(); i++){
            BszsjzDao bszsjzDao = successlist.get(i);
            bszsjzMapper.insert(bszsjzDao);
        }
        return faillist;
    }

    public IPage<BszsjzDao> findPage(Page<BszsjzDao> page, String yxsdm, String yxsmc, String zydm, String zymc,String yjfxdm, String yjfxmc, String zdjs, String kskm, String nf) {
        return bszsjzMapper.findPage(page, yxsdm, yxsmc, zydm, zymc, yjfxdm, yjfxmc, zdjs, kskm, nf);
    }

    public Integer deleteById(Integer id) {
        return bszsjzMapper.deleteById(id);
    }

    public Integer deleteBatch(List<Integer> ids) {
        Integer result = 1;
        for(Integer id:ids){
            result = bszsjzMapper.deleteById(id);
        }
        return result;
    }

    public List<String> yjfxlist(String zymc,String yxsdm) {
        return bszsjzMapper.yjfxlist(zymc,yxsdm);
    }

    public String minYear(String yxsdm) {
        return bszsjzMapper.minYear(yxsdm);
    }

    public List<String> allZydm(String yxsdm) {
        return bszsjzMapper.allZydm(yxsdm);
    }

    public String getZYMC(String zydm) {
        return bszsjzMapper.getZYMC(zydm);
    }

    public List<String> allZymc(String yxsdm) {
        return bszsjzMapper.allZymc(yxsdm);
    }

    public List<String> getYJFXDM(String zydm) {
        return bszsjzMapper.getYJFXDM(zydm);
    }

    public String getYJFXMC(String yjfxdm, String zydm) {
        return bszsjzMapper.getYJFXMC(yjfxdm,zydm);
    }

    public String getSFYXTJ() {
        return bszsjzMapper.getSFYXTJ();
    }

    public int getNFCZ(Integer id) {
        String shzt = bszsjzMapper.getshzt(id);
        String nf = bszsjzMapper.getnf(id);
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
        }
        else {
            return 0;
        }
    }

    public List<String> getAllYJFXMC(String yxsdm) {
        return bszsjzMapper.getAllYJFXMC(yxsdm);
    }

    public List<String> getAllZDJS(String zydm,String yxsdm) {
        return bszsjzMapper.getAllZDJS(zydm,yxsdm);
    }

    public List<String> getyjfxdmandmc(String yxsdm, String nf) {
        return bszsjzMapper.getyjfxdmandmc(yxsdm, nf);
    }

    public int getxyldNFCZ(Integer id) {
        String shzt = bszsjzMapper.getshzt(id);
        String nf = bszsjzMapper.getnf(id);
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR) + 1);
        if(nf.equals(year)){
            if(shzt!=null){
                if(shzt.equals("1")) {
                    return 1;
                }
                else if(shzt.equals("0")){
                    return 0;
                }
            }
            else{
                return 2;
            }
        }else {
            return 3;
        }
        return 1;
    }

    public void updateShzt(String yxsdm, String nf, String yhz, String shr) {
        List<Integer> ids = bszsjzMapper.selectIds(yxsdm,nf);
        BszyzsrsDao bszyzsrsDao = new BszyzsrsDao();
        if(yhz.equals("2-1")) {
            bszyzsrsDao.setXYSHR(shr);
            for(Integer id:ids){
                bszyzsrsDao.setID(id);
                bszsjzMapper.updateXYShzt(bszyzsrsDao);
            }
        }
        else if(yhz.equals("1-1")) {
            bszyzsrsDao.setYYSHR(shr);
            for(Integer id:ids){
                bszyzsrsDao.setID(id);
                bszsjzMapper.updateYYShzt(bszyzsrsDao);
            }
        }
    }

    public String getyxsdmandmc(String yxsdm) {
        return  bszsjzMapper.getyxsdmandmc(yxsdm);
    }

    public List<String> getAllYXSMC() {
        return bszsjzMapper.getAllYXSMC();
    }

    public List<BsjianzhangDao> pjJZBZ(String yxsdm, String nf) {
        return bszsjzMapper.pjJZBZ(yxsdm,nf);
    }

    public int getNFXZ(String yxsdm, String nf) {
        String shzt = bszsjzMapper.getXZshzt(yxsdm,nf);
        if(shzt!=null) {
            if(shzt.equals("1")){
                return 1; //学院秘书已提交
            }
            if(shzt.equals("2")){
                return 2; //领导审核通过
            }
            if(shzt.equals("4")){
                return 4; //研究生院
            }
            else{
                return 3;//可以
            }
        }
        else{
            return 5;
        }
    }

//    public List<String> getzydmandmc(String yxsdm, String nf) {
//        return bszsjzMapper.getzydmandmc(yxsdm, nf);
//    }
}
