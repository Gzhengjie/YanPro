package com.mingmen.yanpro.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;

import com.alibaba.excel.write.metadata.WriteSheet;
import com.mingmen.yanpro.dao.BsjianzhangDao;
import com.mingmen.yanpro.dao.SsJianzhangPinjie;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


// 自定义合并策略 该类继承了AbstractMergeStrategy抽象合并策略，需要重写merge()方法
public class CustomMergeStrategy extends AbstractMergeStrategy {

    /**
     * 分组，每几行合并一次
     */
    private final List<Integer> exportFieldGroupCountList;

    /**
     * 目标合并列index
     */
    private final Integer targetColumnIndex;

    // 需要开始合并单元格的首行index
    private Integer rowIndex;

    // exportDataList为待合并目标列的值
    public CustomMergeStrategy(List<String> exportDataList, Integer targetColumnIndex) {
        this.exportFieldGroupCountList = getGroupCountList(exportDataList);
        this.targetColumnIndex = targetColumnIndex;
    }

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {

        if (null == rowIndex) {
            rowIndex = cell.getRowIndex();
        }
        // 仅从首行以及目标列的单元格开始合并，忽略其他
        if (cell.getRowIndex() == rowIndex && cell.getColumnIndex() == targetColumnIndex) {
            mergeGroupColumn(sheet);
        }
    }

    private void mergeGroupColumn(Sheet sheet) {
        int rowCount = rowIndex;
        for (Integer count : exportFieldGroupCountList) {
            if(count == 1) {
                rowCount += count;
                continue ;
            }
            // 合并单元格
            CellRangeAddress cellRangeAddress = new CellRangeAddress(rowCount, rowCount + count - 1, targetColumnIndex, targetColumnIndex);
            sheet.addMergedRegionUnsafe(cellRangeAddress);
            rowCount += count;
        }
    }

    // 该方法将目标列根据值是否相同连续可合并，存储可合并的行数
    private List<Integer> getGroupCountList(List<String> exportDataList){
        if (CollectionUtils.isEmpty(exportDataList)) {
            return new ArrayList<>();
        }

        List<Integer> groupCountList = new ArrayList<>();
        int count = 1;

        for (int i = 1; i < exportDataList.size(); i++) {
            if (exportDataList.get(i).equals(exportDataList.get(i - 1))) {
                count++;
            } else {
                groupCountList.add(count);
                count = 1;
            }
        }
        // 处理完最后一条后
        groupCountList.add(count);
        return groupCountList;
    }

    private static String getPath() {
        return System.getProperty("user.dir") + "/" + System.currentTimeMillis() + ".xlsx";
    }




    // 修改WriteSheet的代码如下
    public static byte[] writeExcel(List<SsJianzhangPinjie> list) {
        //String fileName = getPath();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(os).excelType(ExcelTypeEnum.XLSX).build();
//
        //List<DemoData> demoDataList = data1();
        WriteSheet writeSheet = EasyExcel.writerSheet("模板1").head(SsJianzhangPinjie.class)
                //DemoData::getString
                .registerWriteHandler(new CustomMergeStrategy(list.stream().map(SsJianzhangPinjie::getYxs).collect(Collectors.toList()), 0))
                .registerWriteHandler(new CustomMergeStrategy(list.stream().map(SsJianzhangPinjie::getZy).collect(Collectors.toList()), 1))
                .registerWriteHandler(new CustomMergeStrategy(list.stream().map(SsJianzhangPinjie::getZy).collect(Collectors.toList()), 2))
                .registerWriteHandler(new CustomMergeStrategy(list.stream().map(SsJianzhangPinjie::getZy).collect(Collectors.toList()), 3))
                .registerWriteHandler(new CustomMergeStrategy(list.stream().map(SsJianzhangPinjie::getYjfx).collect(Collectors.toList()), 4))
                .registerWriteHandler(new CustomMergeStrategy(list.stream().map(SsJianzhangPinjie::getZdjs).collect(Collectors.toList()), 5))
                .registerWriteHandler(new CustomMergeStrategy(list.stream().map(SsJianzhangPinjie::getXxfs).collect(Collectors.toList()), 6))
                .registerWriteHandler(new CustomMergeStrategy(list.stream().map(SsJianzhangPinjie::getZslx).collect(Collectors.toList()), 7))
                .registerWriteHandler(new CustomMergeStrategy(list.stream().map(SsJianzhangPinjie::getKskm).collect(Collectors.toList()), 8))
                .registerWriteHandler(new CustomMergeStrategy(list.stream().map(SsJianzhangPinjie::getFsbskm).collect(Collectors.toList()), 9))
                .registerWriteHandler(new CustomMergeStrategy(list.stream().map(SsJianzhangPinjie::getRssm).collect(Collectors.toList()), 10))
                .build();
        excelWriter.write(list, writeSheet);
        excelWriter.finish();
        //OutputStream outputStream = excelWriter.writeContext().writeWorkbookHolder().getOutputStream();
        return os.toByteArray();
    }

    // 修改WriteSheet的代码如下
    public static byte[] writetoExcel(List<BsjianzhangDao> list) {
        //String fileName = getPath();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(os).excelType(ExcelTypeEnum.XLSX).build();
//
        //List<DemoData> demoDataList = data1();
        WriteSheet writeSheet = EasyExcel.writerSheet("模板1").head(BsjianzhangDao.class)
                //DemoData::getString
                .registerWriteHandler(new CustomMergeStrategy(list.stream().map(BsjianzhangDao::getNF).collect(Collectors.toList()), 0))
                .registerWriteHandler(new CustomMergeStrategy(list.stream().map(BsjianzhangDao::getYXSMC).collect(Collectors.toList()), 1))
                .registerWriteHandler(new CustomMergeStrategy(list.stream().map(BsjianzhangDao::getZYMC).collect(Collectors.toList()), 2))
                .registerWriteHandler(new CustomMergeStrategy(list.stream().map(BsjianzhangDao::getYJFXMC).collect(Collectors.toList()), 3))
                .registerWriteHandler(new CustomMergeStrategy(list.stream().map(BsjianzhangDao::getZDJS).collect(Collectors.toList()), 4))
                .registerWriteHandler(new CustomMergeStrategy(list.stream().map(BsjianzhangDao::getNZSRS).collect(Collectors.toList()), 5))
                .registerWriteHandler(new CustomMergeStrategy(list.stream().map(BsjianzhangDao::getKSKM).collect(Collectors.toList()), 6))
                .registerWriteHandler(new CustomMergeStrategy(list.stream().map(BsjianzhangDao::getLXDH).collect(Collectors.toList()), 7))
                .build();
        excelWriter.write(list, writeSheet);
        excelWriter.finish();
        //OutputStream outputStream = excelWriter.writeContext().writeWorkbookHolder().getOutputStream();
        return os.toByteArray();
    }

    public static void main(String[] args) {
        //writeExcel();
    }

}
