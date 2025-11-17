package com.helpysoft.mima_api.config;

import java.util.HashMap;
import java.util.Map;

public class Helper {
    public static Map<String, Object> responseFormat(boolean error, String message, Object data, String exception) {
        Map<String, Object> json = new HashMap<>();
        json.put("error", error);
        json.put("message", message);
        json.put("data", data);
        json.put("exception", exception);
        return json;
    }
}
