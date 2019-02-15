package com.tyella.weibo.model;

import java.util.HashMap;
import java.util.Map;

public class ViewObject {
    private Map<String,Object> obj=new HashMap<>();

    public void set(String key,Object value){
        obj.put(key,value);
    }

    public Object get(String key){
        return obj.get(key);
    }
}
