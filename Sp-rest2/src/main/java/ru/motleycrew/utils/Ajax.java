package ru.motleycrew.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vas on 03.04.16.
 */
public class Ajax {

    public static Map<String, Object> successResponse(Object object) {
        Map<String, Object> responce = new HashMap<>();
        responce.put("result", "success");
        responce.put("data", object);
        return responce;
    }

    public static Map<String, Object> emptyResponce() {
        Map<String, Object> responce = new HashMap<>();
        responce.put("result", "success");
        return responce;
    }

    public static Map<String, Object> errorResponce(String errorMessage) {
        Map<String, Object> responce = new HashMap<>();
        responce.put("result", "error");
        responce.put("message", errorMessage);
        return responce;
    }
}
