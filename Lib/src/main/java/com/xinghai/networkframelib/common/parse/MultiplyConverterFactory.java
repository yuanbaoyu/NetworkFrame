package com.xinghai.networkframelib.common.parse;

import com.xinghai.networkframelib.common.annotations.Json;
import com.xinghai.networkframelib.common.annotations.Xml;
import com.xinghai.networkframelib.common.parse.gson.GsonConverterFactory;
import com.xinghai.networkframelib.common.parse.xml.SimpleXmlConverterFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created on 16/12/12.
 * author: yuanbaoyu
 */

public class MultiplyConverterFactory extends Converter.Factory {

    private final Converter.Factory jsonFactory;
    private final Converter.Factory xmlFactory;

    public static MultiplyConverterFactory create(){
        return new MultiplyConverterFactory(GsonConverterFactory.create(), SimpleXmlConverterFactory.create());
    }

    public static MultiplyConverterFactory create(Converter.Factory jsonFactory, Converter.Factory xmlFactory){
        return new MultiplyConverterFactory(jsonFactory, xmlFactory);
    }

    public MultiplyConverterFactory(Converter.Factory jsonFactory, Converter.Factory xmlFactory) {

        if(jsonFactory == null && xmlFactory == null){
            throw new IllegalArgumentException("你至少需要支持json或xml解析中的一种");
        }

        this.jsonFactory = jsonFactory;
        this.xmlFactory = xmlFactory;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Json) {
                return jsonFactory.responseBodyConverter(type, annotations, retrofit);
            }
            if (annotation instanceof Xml) {
                return xmlFactory.responseBodyConverter(type, annotations, retrofit);
            }
        }
        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        for (Annotation annotation : parameterAnnotations) {
            if (annotation instanceof Json) {
                return jsonFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations,
                        retrofit);
            }
            if (annotation instanceof Xml) {
                return xmlFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations,
                        retrofit);
            }
        }
        return null;
    }
}
