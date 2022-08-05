package com.mingmen.yanpro.utils;

import java.util.*;

public class JianzhangUtils {


    public static String list2str(List<String> zdjsList){
        StringBuilder res = new StringBuilder();
        for(String zdjs : zdjsList){
            // 把前端发过来的所有的空格都去掉
            res.append(zdjs.replaceAll(" ","")).append("，");
        }
        // 删除最后一个逗号
        res.deleteCharAt(res.length()-1);
        return res.toString();
    }

    public static List<String> str2list(String zdjsStr){
        String[] zdjsList = zdjsStr.replaceAll(" ","")
                .replaceAll(",","，")
                .split("，");
        List<String> res = new ArrayList<>();
        for(String str : zdjsList){
            // 保证数据库里也是没有空格的
            res.add(str.replaceAll(" ","")+" ");
        }
        return res;
    }

    public static String change(String shztm){
        String res = "";
        switch (shztm) {
            case "0":
                res = "学院秘书未提交总表";
                break;
            case "1":
                res = "秘书已提交总表（学院领导待审核）";
                break;
            case "2":
                res = "学院领导审核通过总表（研究生院待审核）";
                break;
            case "3":
                res = "学院领导审核未通过总表";
                break;
            case "4":
                res = "研究生院审核通过总表";
                break;
            case "5":
                res = "研究生院审核不通过总表";
                break;
            case "Y0":
                res = "学院秘书未提交专业方向";
                break;
            case "Y1":
                res = "学院秘书已提交专业方向（学院领导待审核）";
                break;
            case "Y2":
                res = "学院领导审核通过专业方向（研究生院待审核）";
                break;
            case "Y3":
                res = "学院领导审核未通过专业方向";
                break;
            case "Y4":
                res = "研究生院审核通过专业方向";
                break;
            case "Y5":
                res = "研究生院审核未通过专业方向";
                break;
            default:
                res = "error:数据库错误";
                break;
        }

        return res;
    }

    public static boolean isDBData(String str, List<String> strList){
        if(strList.contains(str)){
            return true;
        } else {
            return false;
        }
    }
    public static void main(String[] args) {
        String a = "无";
        List<String> list = new ArrayList<>();
        list.add("你大爷");
        list.add("你二爷");
        list.add("你三爷");
        List<String> bigList = new ArrayList<>();
        bigList.add("你大爷");
        bigList.add("你二爷");
        bigList.add("你三爷");
        bigList.add("你四爷");
        System.out.println(new HashSet<>(bigList).containsAll(list));
    }
}
