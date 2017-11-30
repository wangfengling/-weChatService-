package com.chuxin.pojo;

/**
 * Created by wangzhen on 2017/7/13.
 */
public class KeyValues {
    private String key;
    private String values;
    private int level;

    public KeyValues(String key, String values, int level) {
        this.key = key;
        this.values = values;
        this.level = level;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
