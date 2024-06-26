package com.buildingweb.ultil;

import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UltilFunction<T> {
    public String arrayToString(List<T> array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(array.get(i).toString());
        }
        return sb.toString();
    }
}