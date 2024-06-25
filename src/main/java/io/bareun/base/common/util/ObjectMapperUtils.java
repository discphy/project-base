package io.bareun.base.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ObjectMapperUtils는 JSON 객체를 변환하기 위한 유틸리티 클래스입니다.
 * <p>
 * 이 클래스는 Jackson 라이브러리를 사용하여 JSON 객체를 Java 객체나 리스트로 변환하는 기능을 제공합니다.
 * </p>
 */
@Component
public class ObjectMapperUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 주어진 JSON 객체를 Map으로 변환합니다.
     * <p>
     * 이 메서드는 JSON 객체를 Java Map 객체로 변환합니다.
     * </p>
     *
     * @param json 변환할 JSON 객체
     * @param <T>  변환할 Map의 타입
     * @return 변환된 Map 객체
     */
    public static <T> T toMap(Object json) {
        return mapper.convertValue(json, new TypeReference<T>() {});
    }

    /**
     * 주어진 JSON 객체를 리스트로 변환합니다.
     * <p>
     * 이 메서드는 JSON 객체를 Java List 객체로 변환합니다.
     * </p>
     *
     * @param json 변환할 JSON 객체
     * @param <T>  변환할 리스트의 타입
     * @return 변환된 리스트 객체
     */
    public static <T> List<T> toList(Object json) {
        return mapper.convertValue(json, new TypeReference<List<T>>() {});
    }
}
