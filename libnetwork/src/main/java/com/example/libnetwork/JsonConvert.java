package com.example.libnetwork;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Type;

public class JsonConvert implements Convert {
    @Override
    public Object convert(String response, Type type) {
        JSONObject jsonObject = JSON.parseObject(response);
        JSONObject dataObject = jsonObject.getJSONObject("data");
        if (dataObject != null) {
            String data = dataObject.getString("data");
            return JSON.parseObject(data, type);
        }
        return null;
    }

    @Override
    public Object convert(String response, Class clazz) {
        JSONObject jsonObject = JSON.parseObject(response);
        JSONObject dataObject = jsonObject.getJSONObject("data");
        if (dataObject != null) {
            String data = dataObject.getString("data");
            return JSON.parseObject(data, clazz);
        }
        return null;
    }
}
