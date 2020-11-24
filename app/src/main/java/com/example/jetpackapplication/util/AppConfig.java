package com.example.jetpackapplication.util;

import android.content.res.AssetManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.jetpackapplication.model.BottomBar;
import com.example.jetpackapplication.model.Destination;
import com.example.libcommon.AppGlobals;
import com.example.libcommon.ToolUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class AppConfig {

    private Map<String, Destination> mDestConfig;
    private BottomBar mBottomBar;

    public static AppConfig getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final AppConfig INSTANCE = new AppConfig();
    }

    private AppConfig() {
    }

    public Map<String, Destination> getDestConfig() {
        if (ToolUtil.isEmptyMap(mDestConfig)) {
            String destination = parseFile("destination.json");
            mDestConfig = JSON.parseObject(destination, new TypeReference<Map<String, Destination>>() {
            }.getType());
        }
        return mDestConfig;
    }

    public BottomBar getBottomBar() {
        if (mBottomBar == null) {
            String bottomBar = parseFile("main_tabs_config.json");
            mBottomBar = JSON.parseObject(bottomBar, BottomBar.class);
        }
        return mBottomBar;
    }

    private String parseFile(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }

        AssetManager assets = AppGlobals.getApplication().getResources().getAssets();
        InputStream input = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            input = assets.open(fileName);
            reader = new BufferedReader(new InputStreamReader(input));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ToolUtil.closeStream(input);
            ToolUtil.closeStream(reader);
        }
        return builder.toString();
    }
}
