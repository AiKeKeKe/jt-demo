package com.aike.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ObjectMapperUtil {
    private final static ObjectMapper mapper = new ObjectMapper();

    public static String toJSON(Object target) {
        String result = null;
        try {
            result = mapper.writeValueAsString(target);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    public static <T> T toObject(String json, Class<T> targetClass) {
        T t = null;
        try {
            t = mapper.readValue(json, targetClass);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return t;
    }


}
