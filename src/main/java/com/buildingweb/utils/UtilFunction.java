package com.buildingweb.utils;

import java.util.List;

public class UtilFunction {
    public static <T> String arrayToString(List<T> array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(array.get(i).toString());
        }
        return sb.toString();
    }

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