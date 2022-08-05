package com.mingmen.yanpro.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingmen.yanpro.common.Constants;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.*;
import com.mingmen.yanpro.exception.ServiceException;
import com.mingmen.yanpro.mapper.KsxxshMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Colin
 */



@Service
public class KsxxshService extends ServiceImpl<KsxxshMapper,Ksxxsh> {
    @Autowired
    private KsxxshMapper ksxxshMapper;

    public static boolean isIDNumber(String IDNumber) {
        if (IDNumber == null || "".equals(IDNumber)) {
            return false;
        }
        // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
        String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))"
                + "(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|"
                + "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))"
                + "(([0-2][1-9])|10|20|30|31)\\d{3}$)";
        boolean matches = IDNumber.matches(regularExpression);
        //判断第18位校验值
        if (matches) {
            if (IDNumber.length() == 18) {
                try {
                    char[] charArray = IDNumber.toCharArray();
                    //前十七位加权因子
                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6,
                            3, 7, 9, 10, 5, 8, 4, 2};
                    //这是除以11后，可能产生的11位余数对应的验证码
                    String[] idCardY = {"1", "0", "X", "9", "8",
                            "7", "6", "5", "4", "3", "2"};
                    int sum = 0;
                    for (int i = 0; i < idCardWi.length; i++) {
                        int current = Integer.parseInt(String.valueOf(charArray[i]));
                        int count = current * idCardWi[i];
                        sum += count;
                    }
                    char idCardLast = charArray[17];
                    int idCardMod = sum % 11;
                    if (idCardY[idCardMod].toUpperCase().
                            equals(String.valueOf(idCardLast).toUpperCase())) {
                        return true;
                    } else {
                        System.out.println("身份证最后一位:" + String.valueOf(idCardLast).toUpperCase() +
                                "错误,正确的应该是:" + idCardY[idCardMod].toUpperCase());
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("异常:" + IDNumber);
                    return false;
                }
            }
        }
        return matches;
    }

    public Map<String,Object> saveKsxxsh(Ksxxsh ksxxsh){
        Map<String,Object> ruslt = new HashMap<>();
        if(ksxxsh.getID() == null){//user没有id，则表示是新增
//            return ksxxshMapper.insert(ksxxsh);
            if (ksxxshMapper.insert(ksxxsh)!=0){
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
//            return ksxxshMapper.update(ksxxsh);
            if (ksxxshMapper.update(ksxxsh)!=0){
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

    public Result saveBatch(List<Ksxxsh> list) {
        List<Ksxxsh> err = new ArrayList<>();
        int i = 0;
        Pattern pattern = Pattern.compile("[0-9]*");

        for(Ksxxsh one : list) {
            int flag = 0;

            //校验是否存在重复数据
            QueryWrapper<Ksxxsh> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("KSBH", one.getKSBH());
            queryWrapper2.eq("ZJHM", one.getZJHM());
            if (ksxxshMapper.selectCount(queryWrapper2) != 0)
            {
                one.setBZ("错误信息：该考生已存在");
                flag = 1;
            }

            //校验考生编号
            if (!pattern.matcher(one.getKSBH().toString()).matches() || one.getKSBH().toString().length() != 15){
                one.setBZ(one.getBZ() + " " + "错误信息：考生编号位数错误或有非法字符");
                flag = 1;
            }

            //校验身份证号码
            if (!isIDNumber(one.getZJHM())){
                one.setBZ(one.getBZ() + " " + "错误信息：证件号码位数错误或有非法字符");
                flag = 1;
            }


            if (flag == 0){
                saveOrUpdate(one);
                i++;
            }
            else {
                err.add(one);
            }
        }
        JSONObject jsonObject = new JSONObject();
        if (err.size() == 0)
            jsonObject.putOpt("all", true);
        else
            jsonObject.putOpt("all", false);
        jsonObject.putOpt("success", i);
        jsonObject.putOpt("error", err);
        jsonObject.putOpt("errorcount", err.size());
        return Result.success(jsonObject);
    }

    public List<Ksxxsh> selectAll(){ return ksxxshMapper.selectAll(); }

    public JSONArray findallNF() {
        JSONArray jsonArray = new JSONArray();
        List<Integer> allNF = ksxxshMapper.allNF();
        for(Integer NF : allNF){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("value", NF);
            jsonObject.putOpt("label", NF);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONArray findallKSKMDM() {
        JSONArray jsonArray = new JSONArray();
        List<Integer> allKSKMDM = ksxxshMapper.allKSKMDM();
        for(Integer KSKMDM : allKSKMDM){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("value", KSKMDM);
            jsonObject.putOpt("label", KSKMDM);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public Ksxxsh selectById(Integer ID){ return ksxxshMapper.selectById(ID); }

    public IPage<Ksxxsh> findPage(Page<Ksxxsh> page,Integer NF,Integer SHZT, Long KSBH,String XM, String ZJHM) {
        return ksxxshMapper.findPage(page,NF,SHZT, KSBH,XM,ZJHM);
    }

    public Ksxxsh selectOne(Long KSBH,String XM,String ZJHM){
        Ksxxsh ksxxsh = new Ksxxsh();
        try {
            ksxxsh = ksxxshMapper.selectOne(KSBH,XM,ZJHM);
        } catch (Exception e){
            throw new ServiceException(Constants.CODE_600,"无记录");
        }
        if (ksxxsh !=null){
            return ksxxsh;
        }else {
           throw new ServiceException(Constants.CODE_600,"无记录");
        }


    }

    public List<Ksxxsh> selectModified(){ return ksxxshMapper.selectModified(); }

    public List<Ksxxsh> selectModifiedByKsbh(Long KSBH){
        return ksxxshMapper.selectModifiedByKsbh(KSBH);
    }

    public  Map<String,Object> deleteByKsbh(Long KSBH) {
//        return ksxxshMapper.deleteByKsbh(KSBH);
        Map<String,Object> ruslt = new HashMap<>();
        if (ksxxshMapper.deleteByKsbh(KSBH)!=0){
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

    public String getSFYXTJ() {
        return ksxxshMapper.getSFYXTJ();
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
//                        Ksxxsh ksxxsh = new Ksxxsh();
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
//                                        Long temp = Long.valueOf(cell_val.toString());
//                                        ksxxsh.setKSBH(temp);
//                                    }
//                                }
//                            }
//                            if(cellNumofRow == 1){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        String temp = (String) cell_val;
//
//                                        ksxxsh.setXM(temp);
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
//                                        ksxxsh.setZJHM(temp);
//
//                                    }
//                                }
//                            }
//                            if(cellNumofRow == 3){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Long temp = Long.valueOf(cell_val.toString());
//                                        ksxxsh.setDH(temp);
//
//                                    }
//                                }
//                                else {
//                                    ksxxsh.setDH(null);
//                                }
//                            }
//                            if(cellNumofRow == 4){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Integer temp = Integer.valueOf(cell_val.toString());
//                                        ksxxsh.setZZLLM(temp);
//
//                                    }
//                                }
//                                else {
//                                    ksxxsh.setZZLLM(null);
//                                }
//                            }
//                            if(cellNumofRow == 5){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Integer temp = Integer.valueOf(cell_val.toString());
//                                        ksxxsh.setYGYM(temp);
//
//                                    }
//                                }
//                                else {
//                                    ksxxsh.setYGYM(null);
//                                }
//                            }
//                            if(cellNumofRow == 6){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Integer temp = Integer.valueOf(cell_val.toString());
//                                        ksxxsh.setYWK1M(temp);
//
//                                    }
//                                }
//                                else {
//                                    ksxxsh.setYWK1M(null);
//                                }
//                            }
//                            if(cellNumofRow == 7){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Integer temp = Integer.valueOf(cell_val.toString());
//                                        ksxxsh.setYWK2M(temp);
//                                    }
//                                }
//                                else {
//                                    ksxxsh.setYWK2M(null);
//                                }
//                            }
//                            if(cellNumofRow == 8){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Boolean temp = Boolean.valueOf(cell_val.toString());
//                                        ksxxsh.setZZLLS(temp);
//                                    }
//                                }
//                                else {
//                                    ksxxsh.setZZLLS(null);
//                                }
//                            }
//                            if(cellNumofRow == 9){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Boolean temp = Boolean.valueOf(cell_val.toString());
//                                        ksxxsh.setYGYS(temp);
//
//                                    }
//                                }
//                                else {
//                                    ksxxsh.setYGYS(null);
//                                }
//                            }
//                            if(cellNumofRow == 10){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Boolean temp = Boolean.valueOf(cell_val.toString());
//                                        ksxxsh.setYWK1S(temp);
//
//                                    }
//                                }
//                                else {
//                                    ksxxsh.setYWK1S(null);
//                                }
//                            }
//                            if(cellNumofRow == 11){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Boolean temp = Boolean.valueOf(cell_val.toString());
//                                        ksxxsh.setYWK2S(temp);
//
//                                    }
//                                }
//                                else {
//                                    ksxxsh.setYWK2S(null);
//                                }
//                            }
//                            if(cellNumofRow == 12){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        String temp = (String)cell_val;
//                                        ksxxsh.setSQLY(temp);
//
//                                    }
//                                }
//                                else {
//                                    ksxxsh.setSQLY(null);
//                                }
//                            }
//                            if(cellNumofRow == 13){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        String temp = (String)cell_val;
//                                        ksxxsh.setSHJL(temp);
//
//                                    }
//                                }
//                                else {
//                                    ksxxsh.setSHJL(null);
//                                }
//                            }
//                            if(cellNumofRow == 14){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Integer temp = Integer.valueOf(cell_val.toString());
//                                        ksxxsh.setSHZT(temp);
//
//                                    }
//                                }
//                                else {
//                                    ksxxsh.setSHZT(null);
//                                }
//                            }
//                            if(cellNumofRow == 15){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Integer temp = Integer.valueOf(cell_val.toString());
//                                        ksxxsh.setZZLLCJ(temp);
//
//                                    }
//                                }
//                                else {
//                                    ksxxsh.setZZLLCJ(null);
//                                }
//                            }
//                            if(cellNumofRow == 16){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Integer temp = Integer.valueOf(cell_val.toString());
//                                        ksxxsh.setWGYCJ(temp);
//
//                                    }
//                                }
//                                else {
//                                    ksxxsh.setWGYCJ(null);
//                                }
//                            }
//                            if(cellNumofRow == 17){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Integer temp = Integer.valueOf(cell_val.toString());
//                                        ksxxsh.setYWK1CJ(temp);
//
//                                    }
//                                }
//                                else {
//                                    ksxxsh.setYWK1CJ(null);
//                                }
//                            }
//                            if(cellNumofRow == 18){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Integer temp = Integer.valueOf(cell_val.toString());
//                                        ksxxsh.setYWK2CJ(temp);
//
//                                    }
//                                }
//                                else {
//                                    ksxxsh.setYWK2CJ(null);
//                                }
//                            }
//                            if(cellNumofRow == 19){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Boolean temp = Boolean.valueOf(cell_val.toString());
//                                        ksxxsh.setSJSFNGSH(temp);
//
//                                    }
//                                }
//                                else {
//                                    ksxxsh.setSJSFNGSH(null);
//                                }
//                            }
//                            if(cellNumofRow == 20){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        Integer temp = Integer.valueOf(cell_val.toString());
//                                        ksxxsh.setNF(temp);
//
//                                    }
//                                }
//                                else {
//                                    ksxxsh.setNF(null);
//                                }
//                            }
//                            if(cellNumofRow == 21){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        String temp = (String)cell_val;
//                                        ksxxsh.setSCR(temp);
//
//                                    }
//                                }
//                                else {
//                                    ksxxsh.setSCR(null);
//                                }
//                            }
//                            if(cellNumofRow == 22){
//                                Calendar calendar= Calendar.getInstance();
//                                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
//                                ksxxsh.setSCSJ(calendar.getTime());
//                            }
//                            if(cellNumofRow == 23){
//                                if(xCell != null && !xCell.toString().trim().isEmpty()){
//                                    cell_val = xCell.getStringCellValue();
//                                    if(cell_val != null) {
//                                        String temp = (String)cell_val;
//                                        ksxxsh.setSHR(temp);
//                                    }
//                                }
//                                else {
//                                    ksxxsh.setSHR(null);
//                                }
//                            }
//                            if(cellNumofRow == 24){
//                                Calendar calendar= Calendar.getInstance();
//                                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
//                                ksxxsh.setSHSJ(calendar.getTime());
//                            }
//                            if(cellNumofRow == 25){
//                                Calendar calendar= Calendar.getInstance();
//                                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
//                                ksxxsh.setGXSJ(calendar.getTime());
//                            }
//                        }
//                        ksxxshMapper.insert(ksxxsh);
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

