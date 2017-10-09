package com.jmm.csg.parser;


import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.jmm.csg.bean.BaseResp;
import com.jmm.csg.http.HttpApiException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public final class BaseResponseConverter<T> implements Converter<ResponseBody, T> {

    private Gson gson;
    private TypeAdapter<T> adapter;

    public BaseResponseConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        Charset UTF8 = Charset.forName("UTF-8");
        String resp = value.string();
        if (resp.startsWith("{") || resp.startsWith("[")) {

        } else {
            String statusCode = "";
            switch (resp) {
                case "success":
                    statusCode = "0";
                    break;
                case "error/-1":
                case "error/0":
                case "error/1":
                case "error/2":
                case "error/3":
                case "error/4":
                case "error/5":
                case "error/6":
                    statusCode = "2";
                    throw new HttpApiException("");
//                    break;
                default:
                    statusCode = "0";
                    break;
            }
            resp = gson.toJson(new BaseResp(statusCode, resp));
        }
//        LogUtils.printJson("BaseResponseConverter", resp, "");
        InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(resp.getBytes()), UTF8);
        JsonReader jsonReader = gson.newJsonReader(reader);
        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }

}
