package com.pingan.haofang.network.converter.response;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Set;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

/**
 * 对 ReponseBody 通过 Gson 对 JSON 反序列化。
 * @param <T>
 */
public class GsonJsonReponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Type type;
    private Gson gson = new Gson();

    public GsonJsonReponseBodyConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        Rule rule1 = gson.fromJson("{\"avgPriceRange\":{}, \"valueSet\":[]}", Rule.class);
        BufferedSource bufferedSource = Okio.buffer(value.source());
        String tempStr = bufferedSource.readUtf8();
        bufferedSource.close();
        return gson.fromJson(tempStr, type);
    }
    class Rule{
        Integer id;
        IntegerRange priceRange;
        Set<String> valueSet;

    }   class IntegerRange {
        Integer min;
        Integer max;
    }

}
