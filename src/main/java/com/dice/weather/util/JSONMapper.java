package com.dice.weather.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * This is a utility class that provides a support over the ObjectMapper.
 * It acts as a common class to convert Objects to JSON and vice-versa
 */
@Slf4j
@UtilityClass
public class JSONMapper {
    private static final String ERROR_CONVERT_TO_OBJECT = "Unable to convert String:{}, to class:{} due to:{}";
    public static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .setFilterProvider(new SimpleFilterProvider()
                    .setFailOnUnknownId(false)
            )
            .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

    public static String toString(Object object) {
        if (object instanceof String str) {
            return str;
        } else {
            try {
                return MAPPER.writeValueAsString(object);
            } catch (Exception e) {
                log.error("error in json convert. class {} trace {}",
                        object.getClass().getCanonicalName(), ExceptionUtils.getStackTrace(e));
                return ToStringBuilder.reflectionToString(object);
            }
        }
    }

    /**
     * method to convert the string to given ObjectType
     *
     * @param object
     * @param valueType
     * @param <T>
     * @return the converted Json
     */
    public static <T> T toObject(String object, Class<T> valueType) {
        try {
            return MAPPER.readValue(object, valueType);
        } catch (Exception e) {
            log.error(ERROR_CONVERT_TO_OBJECT, object, valueType, e.getMessage(), e);
            return null;
        }
    }

    /**
     * method to conver the byte[] to given ObjectType
     *
     * @param bytes
     * @param valueType
     * @param <T>
     * @return the converted Json
     */
    public static <T> T toObject(byte[] bytes, Class<T> valueType) {
        try {
            return MAPPER.readValue(bytes, valueType);
        } catch (Exception e) {
            log.error(ERROR_CONVERT_TO_OBJECT, new String(bytes), valueType, e.getMessage(), e);
            return null;
        }
    }

    /**
     * method to convert the string to given ObjectType
     *
     * @param object
     * @param valueType
     * @param <T>
     * @return the converted Json
     */
    public static <T> T toObject(String object, TypeReference<T> valueType) {
        try {
            return MAPPER.readValue(object, valueType);
        } catch (IOException e) {
            log.error(ERROR_CONVERT_TO_OBJECT, object, valueType, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Utility to exclude json fields while serialization...
     *
     * @param filterName
     * @param data
     * @param fields
     * @return
     */
    public static MappingJacksonValue filterJsonFields(String filterName, Object data, String... fields) {
        SimpleBeanPropertyFilter beanPropertyFilter = SimpleBeanPropertyFilter.serializeAllExcept(fields);
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter(filterName, beanPropertyFilter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(data);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    /**
     * Utility to include only specific fields during serialization...
     *
     * @param filterName
     * @param data
     * @param fields
     * @return
     */
    public static MappingJacksonValue filterOutExcept(String filterName, Object data, String... fields) {
        SimpleBeanPropertyFilter beanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept(fields);
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter(filterName, beanPropertyFilter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(data);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    public static String parseBytesToString(Object obj) {
        try {
            byte[] bytes = (byte[]) obj;
            return StringUtils.toEncodedString(bytes, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            if (obj instanceof String str) {
                return str;
            } else {
                return JSONMapper.toString(obj);
            }
        }
    }

}