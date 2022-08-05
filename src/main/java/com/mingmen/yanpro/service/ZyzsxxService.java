package com.mingmen.yanpro.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingmen.yanpro.dao.SsJianzhangZyds;
import com.mingmen.yanpro.dao.Zyzsxx;
import com.mingmen.yanpro.mapper.ZyzsxxMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.mingmen.yanpro.utils.JianzhangUtils.list2str;
import static com.mingmen.yanpro.utils.JianzhangUtils.str2list;

/**
 * @author Colin
 */

@Service
public class ZyzsxxService extends ServiceImpl<ZyzsxxMapper, Zyzsxx> {

    @Autowired
    private ZyzsxxMapper zyzsxxMapper;

    public Map<String,Object> saveZyzsxx(Zyzsxx zyzsxx){
        Map<String,Object> ruslt = new HashMap<>();

        if(zyzsxx.getID() == null){//user没有id，则表示是新增
            //前端传过来的是个list类型，但数据库里存储的是string类型，所以要转化一下。
            zyzsxx.setYJFX(list2str(zyzsxx.getYJFXLIST()));
            if (zyzsxxMapper.insert(zyzsxx)!=0){
                ruslt.put("code","200");
                ruslt.put("message","修改成功");
                return ruslt;
            }
            else {
                ruslt.put("code","0");
                ruslt.put("message","修改失败");
                return ruslt;
            }
        }else{//否则为更新
            if (zyzsxxMapper.update(zyzsxx)!=0){
                ruslt.put("code","200");
                ruslt.put("message","修改成功");
                return ruslt;
            }
            else {
                ruslt.put("code","0");
                ruslt.put("message","修改失败");
                return ruslt;
            }
        }
    }

    public List<Zyzsxx> selectAll(){ return zyzsxxMapper.selectAll(); }

    public Zyzsxx selectById(Integer ID){ return zyzsxxMapper.selectById(ID); }


    public IPage<Zyzsxx> findPage(Page<Zyzsxx> page,Integer NF,Integer SHZT ,String YXSDM,String YXSMC, String ZYMC) {
        if (NF == 0) {
            NF = DateUtil.year(DateUtil.date());
        }
        // 数据库里是string，前端要求是list，所以要转化一下才可以
        List<Zyzsxx> zyzsxxList = new ArrayList<>();
        for (Zyzsxx zyzsxx : zyzsxxMapper.findPage(page,NF,SHZT,YXSDM,YXSMC,ZYMC).getRecords()){
            if (zyzsxx.getYJFX()!=null) {
                zyzsxx.setYJFXLIST(str2list(zyzsxx.getYJFX()));
            }
            zyzsxxList.add(zyzsxx);
        }
            return zyzsxxMapper.findPage(page,NF,SHZT,YXSDM,YXSMC,ZYMC).setRecords(zyzsxxList);
    }

    public JSONArray findallYXSMC1() {
        JSONArray jsonArray = new JSONArray();
        List<String> allname = zyzsxxMapper.allYXSMC1();
        for(String name : allname){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("value", name);
            jsonObject.putOpt("label", name);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONArray findallYXSMC(String YXSDM) {
        JSONArray jsonArray = new JSONArray();
        List<String> allname = zyzsxxMapper.allYXSMC(YXSDM);
        for(String name : allname){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("value", name);
            jsonObject.putOpt("label", name);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }



    public JSONArray findallNF() {
        JSONArray jsonArray = new JSONArray();
        List<Integer> allNF = zyzsxxMapper.allNF();
        for(Integer NF : allNF){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("value", NF);
            jsonObject.putOpt("label", NF);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONArray findallZYMC(String YXSMC,Integer NF) {
        if (NF == 0) {
            NF = DateUtil.year(DateUtil.date());
        }
        JSONArray jsonArray = new JSONArray();
        List<String> allname = zyzsxxMapper.allZYMC(YXSMC,NF);
        for(String name : allname){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("value", name);
            jsonObject.putOpt("label", name);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONArray findallZYDM(String ZYMC,Integer NF) {
        if (NF == 0) {
            NF = DateUtil.year(DateUtil.date());
        }
        JSONArray jsonArray = new JSONArray();
        List<String> allname = zyzsxxMapper.allZYDM(ZYMC,NF);
        for(String name : allname){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("value", name);
            jsonObject.putOpt("label", name);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONArray findallXXFS(String ZYMC,Integer NF) {
        if (NF == 0) {
            NF = DateUtil.year(DateUtil.date());
        }
        JSONArray jsonArray = new JSONArray();
        List<String> allname = zyzsxxMapper.allXXFS(ZYMC,NF);
        for(String name : allname){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("value", name);
            jsonObject.putOpt("label", name);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONArray tongJi(int nf) {
        if (nf == 0) {
            nf = DateUtil.year(DateUtil.date());
        }
        JSONArray jsonArray = new JSONArray();
        List<String> allname = zyzsxxMapper.allYXSMC1();
        for (String name : allname) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("yxsmc", name);
            jsonObject.putOpt("dshrs", zyzsxxMapper.totalSutShzt("", name, 2, nf));
            jsonObject.putOpt("shtgrs", zyzsxxMapper.totalSutShzt("", name, 4, nf));
            jsonObject.putOpt("shbtgrs", zyzsxxMapper.totalSutShzt("", name, 5, nf));
            jsonObject.putOpt("nf", nf);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 获取研究方向
     * @param ZYDM
     * @param NF
     * @return
     */
    public List<String> getYJFX(String ZYDM,Integer NF) {

        //获取研究方向
        List<String> YJFXTempList = zyzsxxMapper.allYJFX(ZYDM, NF);

        List<String> FX_list_temp = new ArrayList<>(YJFXTempList);
        List<String> DS_list = new ArrayList<>();
        FX_list_temp.addAll(YJFXTempList);
        FX_list_temp.add("无");
        for(String FX : FX_list_temp){
            DS_list.add(FX+" ");
        }

        LinkedHashSet<String> hashSet = new LinkedHashSet<>(DS_list);

        return new ArrayList<>(hashSet);
    }

    public List<Zyzsxx> selectByYxsmc(String YXSMC){
        return zyzsxxMapper.selectByYxsmc(YXSMC);
    }

    public Map<String,Object> deleteById(Integer id) {
//        return zyzsxxMapper.deleteById(id);
        Map<String,Object> ruslt = new HashMap<>();
        if (zyzsxxMapper.deleteById(id)!=0){
            ruslt.put("code","200");
            ruslt.put("message","删除成功");
            return ruslt;
        }
        else {
            ruslt.put("code","0");
            ruslt.put("message","删除失败");
            return ruslt;
        }

    }

    public List<Zyzsxx> selectPage(Integer pageNum, Integer pageSize) { return zyzsxxMapper.selectPage(pageNum,pageSize);
    }

    public Integer selectTotal() {
        return zyzsxxMapper.selectTotal();
        }

    public String getSFYXTJ() {
        return zyzsxxMapper.getSFYXTJ();
    }

    /**
     * 读取excl并插入到数据中
     * @param file
     * @return
     */
//    public Map<String,Object> uploadExcl(MultipartFile file) {
//        Map<String,Object> ruslt = new HashMap<>();
//        try {
//            String fileName = file.getOriginalFilename();
//            //判断文件格式并获取工作簿
//            Workbook workbook;
//            if(fileName.endsWith("xls")){
//                workbook = new HSSFWorkbook(file.getInputStream());
//            }else if(fileName.endsWith("xlsx")){
//                workbook = new XSSFWorkbook(file.getInputStream());
//            } else {
//                ruslt.put("code","1");
//                ruslt.put("message","文件格式非excl");
//                return ruslt;
//            }
//            //判断第一页不为空
//            if(null != workbook.getSheetAt(0)){
//                //读取excl第二行，从1开始
//                for(int rowNumofSheet = 1;rowNumofSheet <=workbook.getSheetAt(0).getLastRowNum();rowNumofSheet++){
//                    if (null != workbook.getSheetAt(0).getRow(rowNumofSheet)) {
//                        //定义行，并赋值
//                        Row aRow = workbook.getSheetAt(0).getRow(rowNumofSheet);
//                         Zyzsxx zyzsxx = new Zyzsxx();
//                        System.out.println(aRow.getLastCellNum());
//                        for(int cellNumofRow=0;cellNumofRow<aRow.getLastCellNum();cellNumofRow++){
//                            //读取rowNumOfSheet值所对应行的数据
//                            //获得行的列数
//                            Cell xCell = aRow.getCell(cellNumofRow);
//                            xCell.setCellType(Cell.CELL_TYPE_STRING);
//                            Object cell_val;
//                            if(cellNumofRow == 0){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null){
//                                        Integer temp = Integer.valueOf(cell_val.toString());
//                                        zyzsxx.setYXSDM(temp);
//                                    }
//                                }
//                            }
//                            if(cellNumofRow == 1){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        String temp = (String) cell_val;
//
//                                        zyzsxx.setYXSMC(temp);
//
//                                    }
//                                }
//
//                            }
//                            if(cellNumofRow == 2){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        String temp = (String)cell_val;
//                                        zyzsxx.setZYDM(temp);
//
//                                    }
//                                }
//                            }
//                            if(cellNumofRow == 3){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        String temp = (String)cell_val;
//                                        zyzsxx.setZYMC(temp);
//
//                                    }
//                                }
//                            }
//                            if(cellNumofRow == 4){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        String temp = (String)cell_val;
//                                        zyzsxx.setXXFS(temp);
//
//                                    }
//                                }
//                                else {
//                                    zyzsxx.setXXFS(null);
//                                }
//                            }
//                            if(cellNumofRow == 5){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Integer temp = Integer.valueOf(cell_val.toString());
//                                        zyzsxx.setJHZS(temp);
//
//                                    }
//                                }
//                                else {
//                                    zyzsxx.setJHZS(null);
//                                }
//                            }
//                            if(cellNumofRow == 6){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Integer temp = Integer.valueOf(cell_val.toString());
//                                        zyzsxx.setYJSTMS(temp);
//
//                                    }
//                                }
//                                else {
//                                    zyzsxx.setYJSTMS(null);
//                                }
//                            }
//                            if(cellNumofRow == 7){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Integer temp = Integer.valueOf(cell_val.toString());
//                                        zyzsxx.setGKZKZS(temp);
//                                    }
//                                }
//                                else {
//                                    zyzsxx.setGKZKZS(null);
//                                }
//                            }
//                            if(cellNumofRow == 8){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Float temp = Float.valueOf(cell_val.toString());
//                                        zyzsxx.setFSBL(temp);
//                                    }
//                                }
//                                else {
//                                    zyzsxx.setFSBL(null);
//                                }
//                            }
//                            if(cellNumofRow == 9){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        String temp = (String)cell_val;
//                                        zyzsxx.setSFXYTJ(temp);
//
//                                    }
//                                }
//                                else {
//                                    zyzsxx.setSFXYTJ(null);
//                                }
//                            }
//                            if(cellNumofRow == 10){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        String temp = (String)cell_val;
//                                        zyzsxx.setLXRJDH(temp);
//
//                                    }
//                                }
//                                else {
//                                    zyzsxx.setLXRJDH(null);;
//                                }
//                            }
//                            if(cellNumofRow == 11){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        String temp = (String)cell_val;
//                                        zyzsxx.setDJYQ(temp);
//
//                                    }
//                                }
//                                else {
//                                    zyzsxx.setDJYQ(null);
//                                }
//                            }
//                            if(cellNumofRow == 12){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        String temp = (String)cell_val;
//                                        zyzsxx.setBZ(temp);
//
//                                    }
//                                }
//                                else {
//                                    zyzsxx.setBZ(null);
//                                }
//                            }
//                            if(cellNumofRow == 13){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Integer temp = Integer.valueOf(cell_val.toString());
//                                        zyzsxx.setSHZT(temp);
//
//                                    }
//                                }
//                                else {
//                                    zyzsxx.setSHZT(null);
//                                }
//                            }
//                            if(cellNumofRow == 14){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Integer temp = Integer.valueOf(cell_val.toString());
//                                        zyzsxx.setNF(temp);
//
//                                    }
//                                }
//                                else {
//                                    zyzsxx.setNF(null);
//                                }
//                            }
//                            if(cellNumofRow == 15){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        String temp = (String)cell_val;
//                                        zyzsxx.setSCR(temp);
//
//                                    }
//                                }
//                                else {
//                                    zyzsxx.setSCR(null);
//                                }
//                            }
//
//                            if(cellNumofRow == 16){
//                                Calendar calendar= Calendar.getInstance();
//                                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
//                                zyzsxx.setSCSJ(calendar.getTime());
//                                    }
//
//                            if(cellNumofRow == 17){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        String temp = (String)cell_val;
//                                        zyzsxx.setSHR(temp);
//
//                                    }
//                                }
//                                else {
//                                    zyzsxx.setSHR(null);
//                                }
//                            }
//
//                            if(cellNumofRow == 18){
//                                Calendar calendar= Calendar.getInstance();
//                                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
//                                zyzsxx.setSHSJ(calendar.getTime());
//                            }
//
//                            if(cellNumofRow == 19){
//                                Calendar calendar= Calendar.getInstance();
//                                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
//                                zyzsxx.setGXSJ(calendar.getTime());
//                            }
//                        }
//                        zyzsxxMapper.insert(zyzsxx);
//                    }
//                }
//                ruslt.put("code","0");
//                ruslt.put("message","成功插入数据库！");
//            }else {
//                ruslt.put("code","1");
//                ruslt.put("message","第一页EXCL无数据！");
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            ruslt.put("code","1");
//            ruslt.put("message",e.getMessage());
//        }
//        return ruslt;
//    }

}

