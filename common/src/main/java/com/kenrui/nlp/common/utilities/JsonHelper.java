package com.kenrui.nlp.common.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {
    public static ObjectMapper mapper;

    // Thread safe double checked locking lazy initialization
    public static ObjectMapper getMapper() {
        if (mapper == null) {
            synchronized (JsonHelper.class) {
                if (mapper == null) {
                    mapper = new ObjectMapper();
                }
            }
        }
        return mapper;
    }

    public static String write(Object object) {
        String output = null;
        try {
            output = JsonHelper.getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return output;
    }
}
