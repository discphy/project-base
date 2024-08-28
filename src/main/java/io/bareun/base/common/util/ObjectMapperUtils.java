package io.bareun.base.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ObjectMapperUtils는 JSON 객체를 변환하기 위한 유틸리티 클래스입니다.
 * <p>
 * 이 클래스는 Jackson 라이브러리를 사용하여 JSON 객체를 Java 객체나 리스트로 변환하는 기능을 제공합니다.
 */
@Slf4j
@Component
public class ObjectMapperUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 주어진 JSON 문자열을 지정된 타입의 Java 객체로 변환합니다.
     *
     * @param json 변환할 JSON 문자열
     * @param <T>  변환할 타겟 객체의 타입
     * @return 변환된 Java 객체
     * @throws RuntimeException JSON 처리 중 오류가 발생한 경우
     */
    public static <T> T convert(String json) {
        try {
            return mapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 주어진 JSON 객체를 Map으로 변환합니다.
     *
     * @param object 변환할 JSON 객체
     * @param <T>    변환할 타겟 클래스의 타입
     * @return 변환된 Map 객체
     */
    public static <T> T convert(Object object) {
        return mapper.convertValue(object, new TypeReference<>() {
        });
    }

    /**
     * 주어진 JSON 객체를 지정된 클래스 타입의 객체로 변환합니다.
     *
     * @param object 변환할 JSON 객체
     * @param type   변환할 클래스 타입
     * @param <T>    변환할 타겟 클래스의 타입
     * @return 변환된 객체
     */
    public static <T> T convert(Object object, Class<T> type) {
        return mapper.convertValue(object, type);
    }

    /**
     * 주어진 JSON 배열을 지정된 타입의 리스트로 변환합니다.
     *
     * @param object 변환할 JSON 배열
     * @param type   리스트에 포함될 객체의 클래스 타입
     * @param <T>    리스트에 포함될 객체의 타입
     * @return 변환된 리스트 객체
     */
    public static <T> List<T> convertList(Object object, Class<T> type) {
        return mapper.convertValue(object, mapper.getTypeFactory().constructCollectionType(List.class, type));
    }

    /**
     * 주어진 객체를 JSON 문자열로 변환합니다.
     *
     * @param object 변환할 객체
     * @return JSON 문자열
     */
    public static String toString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("convert object to string error", e);
            return "";
        }
    }
}
