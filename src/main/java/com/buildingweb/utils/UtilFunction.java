package com.buildingweb.utils;

import java.util.ArrayList;
import java.util.List;

public class UtilFunction {
    public static boolean checkString(String s) {
        if (s != null && !s.equals("")) {
            return true;
        }
        return false;
    }

    public static boolean checkLong(Long i) {
        if (i != null) {
            return true;
        }
        return false;
    }

    public static List<Long> stringToListNumber(String s) {
        List<Long> numbers = new ArrayList<>();
        String[] numString = s.split(",");
        for (String n : numString) {
            String numTrim = n.trim();
            Long numConvert = Long.parseLong(numTrim);
            numbers.add(numConvert);
        }
        return numbers;
    }
}