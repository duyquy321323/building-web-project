package com.buildingweb.utils;

public class UtilFunction {
    public static boolean checkString(String s){
        if(s != null && !s.equals("")) {
            return true;
        }
        return false;
    }

    public static boolean checkInteger(Integer i){
        if(i != null) {
            return true;
        }
        return false;
    }
}