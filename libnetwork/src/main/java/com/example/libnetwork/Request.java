package com.example.libnetwork;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;

import com.example.libnetwork.cache.CacheManager;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class Request<T, R extends Request> implements Cloneable {
    //只访问本地缓存，即便本地缓存不存在，也不会发起网络请求
    private static final int CACHE_ONLY = 1;
    //先访问缓存，同时发起网络的请求，成功后缓存到本地
    private static final int CACHE_FIRST = 2;
    //只访问服务器，不做任何存储
    private static final int NET_ONLY = 3;
    //先访问网络，成功后缓存到本地
    private static final int NET_CACHE = 4;

    protected String mUrl;
    private Map<String, String> headers = new HashMap<>();
    protected Map<String, Object> params = new HashMap<>();
    private String mCacheKey;
    private Type mType;
    private Class mClass;
    private int mCacheStrategy = NET_ONLY;

    @IntDef({CACHE_ONLY, CACHE_FIRST, NET_ONLY, NET_CACHE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CacheStrategy {

    }

    public Request(String url) {
        mUrl = url;
    }

    public R addHeader(String key, String value) {
        headers.put(key, value);
        return (R) this;
    }

    public R addParams(String key, Object value) {
        if (value == null) {
            return (R) this;
        }

        try {
            if (value instanceof String) {
                params.put(key, value);
            } else {
                Field field = value.getClass().getField("TYPE");
                Class clazz = (Class) field.get(null);
                if (clazz != null && clazz.isPrimitive()) {
                    params.put(key, value);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return (R) this;
    }

    public R cacheKey(String key) {
        mCacheKey = key;
        return (R) this;
    }

    public R responseType(Type type) {
        mType = type;
        return (R) this;
    }

    public R setCacheStrategy(@CacheStrategy int cacheStrategy) {
        mCacheStrategy = cacheStrategy;
        return (R) this;
    }

    public R responseType(Class clazz) {
        mClass = clazz;
        return (R) this;
    }

    public Call getCall() {
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        addHeaders(builder);
        okhttp3.Request request = generateRequest(builder);
        return ApiService.sOkHttpClient.newCall(request);
    }

    public void addHeaders(okhttp3.Request.Builder builder) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
    }

    protected abstract okhttp3.Request generateRequest(okhttp3.Request.Builder builder);

    @SuppressLint("RestrictedApi")
    public void execute(final JsonCallback<T> callback) {
        if (mCacheStrategy != NET_ONLY) {
            ArchTaskExecutor.getIOThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    ApiResponse<T> cacheData = readCache();
                    callback.onCacheSuccess(cacheData);
                }
            });
        }

        if (mCacheStrategy == CACHE_ONLY) {
            return;
        }

        getCall().enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ApiResponse<T> response = new ApiResponse<>();
                response.success = false;
                response.message = e.getMessage();
                callback.onError(response);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ApiResponse<T> result = parseResponse(response, callback);
                if (result.success) {
                    callback.onSuccess(result);
                } else {
                    callback.onError(result);
                }
            }
        });
    }

    public ApiResponse<T> execute() {
        if (mCacheStrategy == CACHE_ONLY) {
            return readCache();
        }

        try {
            Response response = getCall().execute();
            return parseResponse(response, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ApiResponse<T> parseResponse(Response response, JsonCallback<T> callback) {
        boolean success = response.isSuccessful();
        int status = response.code();
        String message;
        ApiResponse<T> result = new ApiResponse<>();
        Convert convert = ApiService.sConvert;
        try {
            String content = response.body().string();
            if (success) {
                if (callback != null) {
                    ParameterizedType type = (ParameterizedType) callback.getClass().getGenericSuperclass();
                    Type argument = type.getActualTypeArguments()[0];
                    result.body = (T) convert.convert(content, argument);
                } else if (mType != null) {
                    result.body = (T) convert.convert(content, mType);
                } else if (mClass != null) {
                    result.body = (T) convert.convert(content, mClass);
                } else {
                    Log.e("request", "parseResponse: 无法解析, 无数据类型");
                }
            }
            message = content;
        } catch (IOException e) {
            success = false;
            message = e.getMessage();
            e.printStackTrace();
        }
        result.success = success;
        result.status = status;
        result.message = message;

        if (mCacheStrategy != NET_ONLY && result.success
                && result.body instanceof Serializable) {
            saveCache(result.body);
        }

        return result;
    }

    private ApiResponse<T> readCache() {
        String key = TextUtils.isEmpty(mCacheKey) ? generateCacheKey() : mCacheKey;
        Object cache = CacheManager.getCache(key);
        ApiResponse<T> result = new ApiResponse<>();
        result.status = 304;
        result.message = "缓存获取成功";
        result.body = (T) cache;
        result.success = true;
        return result;
    }

    private void saveCache(T body) {
        String key = TextUtils.isEmpty(mCacheKey) ? generateCacheKey() : mCacheKey;
        CacheManager.saveCache(key, body);
    }

    private String generateCacheKey() {
        mCacheKey = UrlCreator.createUrlFromParams(mUrl, params);
        return mCacheKey;
    }

    @NonNull
    @Override
    protected Request<T, R> clone() throws CloneNotSupportedException {
        return (Request<T, R>) super.clone();
    }
}
