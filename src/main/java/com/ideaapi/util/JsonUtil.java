package com.ideaapi.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class JsonUtil {

    private JsonUtil() {
    }

    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.findAndRegisterModules();
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        MAPPER.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
    }

    public static String toJson(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("Error write json.", e);
            return null;
        }
    }

    public static <T> T toObject(String json, Class<T> objectType) {
        try {
            return MAPPER.readValue(json, objectType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("Error parsing json.", e);
            return null;
        }
    }

    public static <T> T toObject(Object json, Class<T> objectType) {
        if (json == null) {
            return null;
        }
        return toObject(json.toString(), objectType);
    }
}
