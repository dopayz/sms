package com.yudu.sms.util;

public class JsonStringUtils {
    public static String stringConvert(String oldString){
        String newResult = "[";
        oldString = oldString.replaceAll(" ", "");
        oldString = trimStart(oldString,"[");
        oldString = trimEnd(oldString,"]");
        oldString = oldString.replace("},{","}=={");
        String[] r1  = oldString.split("==");
        for (int i = 0; i <r1.length ; i++) {
            newResult+="{";
            r1[i] = trimStart(r1[i],"{");
            r1[i] = trimEnd(r1[i],"}");
            String[] r2  =r1[i].split(",");
            for (int j = 0; j < r2.length; j++) {
                if(r2[j].split("=").length == 2){
                    String s1 = r2[j].split("=")[0];
                    String s2 = r2[j].split("=")[1];
                    newResult+="\""+s1+"\""+":"+ "\""+s2+"\""+",";
                }else{
                    String s1 = r2[j].split("=")[0];
                    newResult+="\""+s1+"\""+":"+ "\""+""+"\""+",";
                }
            }
            newResult = newResult.substring(0,newResult.length()-1);
            newResult+="},";
        }
        newResult = newResult.substring(0,newResult.length()-1);
        newResult += "]";
        return newResult;
    }

    /*
     * 删除开头字符串
     */
    public static String trimStart(String inStr, String prefix) {
        if (inStr.startsWith(prefix)) {
            return (inStr.substring(prefix.length()));
        }
        return inStr;
    }
    /*
     * 删除末尾字符串
     */
    public static String trimEnd(String inStr, String suffix) {
        if (inStr.endsWith(suffix)) {
            return (inStr.substring(0,inStr.length()-suffix.length()));
        }
        return inStr;
    }
}
