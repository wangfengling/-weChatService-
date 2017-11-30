package com.chuxin.util;

import java.util.Collections;
import java.util.List;

/**
 * Created by wangzhen on 2017/6/30.
 */
public class SortToString {
    public static String getSortToString(List<String> sourceString){
        if (sourceString.size() > 0){
            Collections.sort(sourceString);
            StringBuilder stringBuilder = new StringBuilder();
            for (String item : sourceString) {
                stringBuilder.append(item);
            }
            return stringBuilder.toString();
        }
        return "";
    }
}
