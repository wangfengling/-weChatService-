package com.chuxin.util;

import com.chuxin.pojo.KeyValues;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * Created by wangzhen on 2017/7/13.
 * 传入List集合转化成JSON字符串
 */
public class KeyValuesToJSON {

    private final static int[]  level = {0,1,2,3,4,5};

    public static String getJsonFromKeyValues(List<KeyValues> keyValuesList){
        JSONObject[] parent = new JSONObject[6];
        if (keyValuesList == null){
            return "keyValuesList 值不能为空!";
        }
        for(int i = keyValuesList.size() - 1; i >= 0; i--) {
            if (keyValuesList.get(i).getValues() == null) {
                if (parent[keyValuesList.get(i).getLevel()] == null) {
                    parent[keyValuesList.get(i).getLevel()] = new JSONObject();
                }
                parent[keyValuesList.get(i).getLevel()].put(keyValuesList.get(i).getKey(), parent[keyValuesList.get(i).getLevel()+1]);
                parent[keyValuesList.get(i).getLevel()+1].clear();
            } else {
                if (parent[keyValuesList.get(i).getLevel()] == null) {
                    parent[keyValuesList.get(i).getLevel()] = new JSONObject();
                }
                parent[keyValuesList.get(i).getLevel()].put(keyValuesList.get(i).getKey(), keyValuesList.get(i).getValues());
            }
        }
        return parent[0].toString();
    }
}
