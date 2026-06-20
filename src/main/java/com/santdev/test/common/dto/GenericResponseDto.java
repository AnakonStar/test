package com.santdev.test.common.dto;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GenericResponseDto extends LinkedHashMap<String, Object> {

    public GenericResponseDto(Map<String, Object> values) {
        super(values);
    }

    public static GenericResponseDto fromEntity(Object entity, List<String> keys) {
        return fromEntity(entity, keys, Map.of());
    }

    public static GenericResponseDto fromEntity(
            Object entity,
            List<String> keys,
            Map<String, Function<Object, Object>> transforms
    ) {
        Map<String, Object> pickedEntity = new LinkedHashMap<>();

        for (String key : keys) {
            Object value = readValue(entity, key);

            if (value == null) {
                continue;
            }

            Function<Object, Object> transform = transforms.get(key);
            pickedEntity.put(key, transform != null ? transform.apply(value) : normalize(value));
        }

        return new GenericResponseDto(pickedEntity);
    }

    private static Object normalize(Object value) {
        if (value instanceof BigDecimal decimal) {
            return decimal.doubleValue();
        }

        return value;
    }

    private static Object readValue(Object entity, String key) {
        if (entity == null || key == null || key.isBlank()) {
            return null;
        }

        if (entity instanceof Map<?, ?> map) {
            return map.get(key);
        }

        String suffix = key.substring(0, 1).toUpperCase() + key.substring(1);

        try {
            Method getter = entity.getClass().getMethod("get" + suffix);
            return getter.invoke(entity);
        } catch (Exception ignored) {
            try {
                Method getter = entity.getClass().getMethod("is" + suffix);
                return getter.invoke(entity);
            } catch (Exception ignoredAgain) {
                return null;
            }
        }
    }
}
