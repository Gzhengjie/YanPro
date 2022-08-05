package com.mingmen.yanpro.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingmen.yanpro.dao.Fsfa;
import com.mingmen.yanpro.mapper.FsfaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Colin
 */

@Service
public class FsfaService extends ServiceImpl <FsfaMapper,Fsfa> {

    @Autowired
    private FsfaMapper fsfaMapper;

    public Map<String,Object> saveFsfa(Fsfa fsfa){
        Map<String,Object> ruslt = new HashMap<>();
        if(fsfa.getID() == null){//user没有id，则表示是新增
            if (fsfaMapper.insert(fsfa)!=0){
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
            if (fsfaMapper.update(fsfa)!=0){
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

//    public boolean saveFsfa(Fsfa fsfa){
//        return saveOrUpdate(fsfa); //Mybatis-Plus提供的方法
//    }

    //搜索除了id的所有信息
    public List<Fsfa> selectAllNoId(){ return fsfaMapper.selectAllNoId(); }

    public List<Fsfa> selectAll(){ return fsfaMapper.selectAll(); }

    public Fsfa selectById(Integer ID){ return fsfaMapper.selectById(ID); }


    public List<Fsfa> selectByWjm(String WJM){
        return fsfaMapper.selectByWjm(WJM);
    }

    public JSONArray findallYXSMC1() {
        JSONArray jsonArray = new JSONArray();
        List<String> allname = fsfaMapper.allYXSMC1();
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
        List<String> allname = fsfaMapper.allYXSMC(YXSDM);
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
        List<Integer> allNF = fsfaMapper.allNF();
        for(Integer NF : allNF){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("value", NF);
            jsonObject.putOpt("label", NF);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONArray tongJi(int nf) {
        if (nf == 0) {
            nf = DateUtil.year(DateUtil.date());
        }
        JSONArray jsonArray = new JSONArray();
        List<String> allname = fsfaMapper.allYXSMC1();
        for (String name : allname) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("yxsmc", name);
            jsonObject.putOpt("dshrs", fsfaMapper.totalSutShzt("", name, 2, nf));
            jsonObject.putOpt("shtgrs", fsfaMapper.totalSutShzt("", name, 4, nf));
            jsonObject.putOpt("shbtgrs", fsfaMapper.totalSutShzt("", name, 5, nf));
            jsonObject.putOpt("nf", nf);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public Map<String,Object> deleteById(Integer id) {
//        return fsfaMapper.deleteById(id);
        Map<String,Object> ruslt = new HashMap<>();
        if (fsfaMapper.deleteById(id)!=0){
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

//    public List<Fsfa> selectPage(Integer pageNum, Integer pageSize) {
//        return fsfaMapper.selectPage(pageNum,pageSize);
//    }

    public IPage<Fsfa> findPage(Page<Fsfa> page,Integer NF, Integer SHZT,String WJM, String YXSDM,String YXSMC) {
        if (NF == 0) {
            NF = DateUtil.year(DateUtil.date());
        }
        return fsfaMapper.findPage(page,NF,SHZT, WJM,YXSDM,YXSMC);
    }

    public String getSFYXTJ() {
        return fsfaMapper.getSFYXTJ();
    }

    public Integer selectTotal() {
        return fsfaMapper.selectTotal();
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
//                        Fsfa fsfa = new Fsfa();
//                        System.out.println(aRow.getLastCellNum());
//                        for(int cellNumofRow=0;cellNumofRow<aRow.getLastCellNum();cellNumofRow++) {
//                            //读取rowNumOfSheet值所对应行的数据
//                            //获得行的列数
//                            Cell xCell = aRow.getCell(cellNumofRow);
//                            xCell.setCellType(Cell.CELL_TYPE_STRING);
//                            Object cell_val;
//                            if (cellNumofRow == 0) {
//                                if (xCell != null && !xCell.toString().trim().isEmpty()) {
//                                    cell_val = xCell.getStringCellValue();
//                                    if (cell_val != null) {
//                                        String temp = (String) cell_val;
//                                        fsfa.setWJM(temp);
//                                    }
//                                }
//                            }
//                            if (cellNumofRow == 1) {
//                                if (xCell != null && !xCell.toString().trim().isEmpty()) {
//                                    cell_val = xCell.getStringCellValue();
//                                    if (cell_val != null) {
//                                        Integer temp = Integer.valueOf(cell_val.toString());
//                                        fsfa.setYXDM(temp);
//
//                                    }
//                                }
//
//                            }
//                            if (cellNumofRow == 2) {
//                                if (xCell != null && !xCell.toString().trim().isEmpty()) {
//                                    cell_val = xCell.getStringCellValue();
//                                    if (cell_val != null) {
//                                        String temp = (String) cell_val;
//                                        fsfa.setYXSMC(temp);
//                                    }
//                                }
//                            }
//                            if (cellNumofRow == 3) {
//                                if (xCell != null && !xCell.toString().trim().isEmpty()) {
//                                    cell_val = xCell.getStringCellValue();
//                                    if (cell_val != null) {
//                                        Integer temp = Integer.valueOf(cell_val.toString());
//                                        fsfa.setNF(temp);
//
//                                    }
//                                } else {
//                                    fsfa.setNF(null);
//                                }
//                            }
//                            if (cellNumofRow == 4) {
//                                if (xCell != null && !xCell.toString().trim().isEmpty()) {
//                                    cell_val = xCell.getStringCellValue();
//                                    if (cell_val != null) {
//                                        String temp = (String) cell_val;
//                                        fsfa.setFSFA(temp);
//
//                                    }
//                                } else {
//                                    fsfa.setFSFA(null);
//                                }
//                            }
//                            if (cellNumofRow == 5) {
//                                if (xCell != null && !xCell.toString().trim().isEmpty()) {
//                                    cell_val = xCell.getStringCellValue();
//                                    if (cell_val != null) {
//                                        Integer temp = Integer.valueOf(cell_val.toString());
//                                        fsfa.setSHZT(temp);
//
//                                    }
//                                } else {
//                                    fsfa.setSHZT(null);
//                                }
//                            }
//
//                            if (cellNumofRow == 6) {
//                                if (xCell != null && !xCell.toString().trim().isEmpty()) {
//                                    cell_val = xCell.getStringCellValue();
//                                    if (cell_val != null) {
//                                        String temp = (String) cell_val;
//                                        fsfa.setSCR(temp);
//
//                                    }
//                                } else {
//                                    fsfa.setSCR(null);
//                                }
//                            }
//
//                            if (cellNumofRow == 7) {
//                                Calendar calendar = Calendar.getInstance();
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
//                                fsfa.setSCSJ(calendar.getTime());
//                            }
//
//                            if (cellNumofRow == 8) {
//                                if (xCell != null && !xCell.toString().trim().isEmpty()) {
//                                    cell_val = xCell.getStringCellValue();
//                                    if (cell_val != null) {
//                                        String temp = (String) cell_val;
//                                        fsfa.setSHR(temp);
//                                    }
//                                } else {
//                                    fsfa.setSHR(null);
//                                }
//                            }
//
//                            if (cellNumofRow == 9) {
//                                Calendar calendar = Calendar.getInstance();
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
//                                fsfa.setSHSJ(calendar.getTime());
//                            }
//
//                            if (cellNumofRow == 10) {
//                                Calendar calendar = Calendar.getInstance();
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
//                                fsfa.setGXSJ(calendar.getTime());
//                            }
//                        }
//                        fsfaMapper.insert(fsfa);
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

