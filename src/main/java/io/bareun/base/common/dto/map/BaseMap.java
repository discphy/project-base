package io.bareun.base.common.dto.map;

import lombok.NoArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.map.ListOrderedMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.bareun.base.common.util.ObjectMapperUtils.convert;
import static io.bareun.base.common.util.ObjectMapperUtils.convertList;
import static org.apache.commons.text.CaseUtils.toCamelCase;

/**
 * 키-값 쌍을 보관하고 조작하는 데 사용되는 DTO 맵 클래스입니다.
 * 기본적으로 키를 camelCase로 변환합니다.
 *
 *
 * @see BaseMapTest#jsonParsing()
 */
@NoArgsConstructor
public class BaseMap extends ListOrderedMap<String, Object> {

    /**
     * 주어진 맵으로 초기화합니다.
     *
     * @param map 초기화에 사용할 맵
     */
    public BaseMap(Map<String, Object> map) {
        putAll(map);
    }

    /**
     * JSON 객체로부터 맵을 생성합니다.
     *
     * @param json JSON 객체
     */
    public BaseMap(Object json) {
        putAll(toMap(json));
    }

    /**
     * JSON 객체로부터 맵을 생성합니다.
     *
     * @param json JSON 문자열
     */
    public BaseMap(String json) {
        putAll(convert(json));
    }

    /**
     * 주어진 맵으로 BaseMap을 생성합니다.
     *
     * @param map 초기화에 사용할 맵
     * @return BaseMap 인스턴스
     */
    public static BaseMap of(Object map) {
        return new BaseMap(map);
    }

    /**
     * 주어진 맵으로 BaseMap을 생성합니다.
     *
     * @param json 초기화에 사용할 json 문자열
     * @return BaseMap 인스턴스
     */
    public static BaseMap of(String json) {
        return new BaseMap(json);
    }

    /**
     * 주어진 키와 값을 camelCase 키로 변환하여 맵에 추가합니다.
     *
     * @param key   맵에 추가할 키
     * @param value 맵에 추가할 값
     * @return 이전 키와 연관된 값
     */
    @Override
    public Object put(String key, Object value) {
        return super.put(camelCase(key), value);
    }

    /**
     * 주어진 키와 값을 맵에 추가하고, BaseMap을 반환합니다.
     *
     * @param key   맵에 추가할 키
     * @param value 맵에 추가할 값
     * @return BaseMap 인스턴스
     */
    public BaseMap set(String key, Object value) {
        put(key, value);
        return this;
    }

    /**
     * 주어진 키에 대한 문자열 값을 반환합니다. 기본값을 제공할 수 있습니다.
     *
     * @param key          검색할 키
     * @param defaultValue 기본값
     * @return 문자열 값
     */
    public String getString(String key, String defaultValue) {
        return MapUtils.getString(this, key, defaultValue);
    }

    /**
     * 주어진 키에 대한 문자열 값을 반환합니다.
     *
     * @param key 검색할 키
     * @return 문자열 값
     */
    public String getString(String key) {
        return MapUtils.getString(this, key);
    }

    /**
     * 주어진 키에 대한 불리언 값을 반환합니다. 기본값을 제공할 수 있습니다.
     *
     * @param key          검색할 키
     * @param defaultValue 기본값
     * @return 불리언 값
     */
    public Boolean getBoolean(String key, boolean defaultValue) {
        return MapUtils.getBoolean(this, key, defaultValue);
    }

    /**
     * 주어진 키에 대한 불리언 값을 반환합니다.
     *
     * @param key 검색할 키
     * @return 불리언 값
     */
    public Boolean getBoolean(String key) {
        return MapUtils.getBoolean(this, key);
    }

    /**
     * 주어진 키에 대한 Long 값을 반환합니다. 기본값을 제공할 수 있습니다.
     *
     * @param key          검색할 키
     * @param defaultValue 기본값
     * @return Long 값
     */
    public Long getLong(String key, Long defaultValue) {
        return MapUtils.getLong(this, key, defaultValue);
    }

    /**
     * 주어진 키에 대한 Long 값을 반환합니다.
     *
     * @param key 검색할 키
     * @return Long 값
     */
    public Long getLong(String key) {
        return MapUtils.getLong(this, key);
    }

    /**
     * 주어진 키에 대한 Double 값을 반환합니다. 기본값을 제공할 수 있습니다.
     *
     * @param key          검색할 키
     * @param defaultValue 기본값
     * @return Double 값
     */
    public Double getDouble(String key, Double defaultValue) {
        return MapUtils.getDouble(this, key, defaultValue);
    }

    /**
     * 주어진 키에 대한 Double 값을 반환합니다.
     *
     * @param key 검색할 키
     * @return Double 값
     */
    public Double getDouble(String key) {
        return MapUtils.getDouble(this, key);
    }

    /**
     * 주어진 키에 대한 Integer 값을 반환합니다. 기본값을 제공할 수 있습니다.
     *
     * @param key          검색할 키
     * @param defaultValue 기본값
     * @return Integer 값
     */
    public Integer getInteger(String key, int defaultValue) {
        return MapUtils.getInteger(this, key, defaultValue);
    }

    /**
     * 주어진 키에 대한 Integer 값을 반환합니다.
     *
     * @param key 검색할 키
     * @return Integer 값
     */
    public Integer getInteger(String key) {
        return MapUtils.getInteger(this, key);
    }

    /**
     * 주어진 키에 대한 맵을 반환합니다.
     *
     * @param key 검색할 키
     * @return BaseMap 객체 또는 null
     */
    public BaseMap getMap(String key) {
        return get(key) != null ? toMap(get(key)) : null;
    }

    /**
     * 주어진 키에 대한 리스트를 반환합니다.
     *
     * @param key 검색할 키
     * @return BaseMap의 리스트 또는 null
     */
    public <T> List<T> getList(String key) {
        return get(key) != null ? convert(get(key)) : null;
    }

    /**
     * 주어진 키에 해당하는 값의 JSON 배열을 BaseMap 객체의 리스트로 변환합니다.
     * 만약 값이 null이면 null을 반환합니다.
     *
     * @param key 변환할 JSON 배열이 포함된 맵의 키
     * @return BaseMap 객체의 리스트
     */
    public List<BaseMap> getMapList(String key) {
        return get(key) != null ? convertList(get(key), BaseMap.class) : null;
    }

    /**
     * 주어진 키 목록에 따라 중첩된 맵을 반환합니다.
     *
     * @param keys 검색할 키 목록
     * @return BaseMap 객체
     * @throws IllegalArgumentException 키가 존재하지 않으면 발생
     */
    public BaseMap getMapByKeys(List<String> keys) {
        BaseMap map = this;

        for (String key : keys) {
            map = map.getMap(key);
            if (map == null) {
                throw new IllegalArgumentException("key:" + key + " not exist");
            }
        }

        return map;
    }

    /**
     * 주어진 키 목록에 따라 중첩된 맵을 반환합니다.
     *
     * @param keys 검색할 키 목록
     * @return BaseMap 객체
     * @throws IllegalArgumentException 키가 존재하지 않으면 발생
     */
    public BaseMap getMapByKeys(String... keys) {
        return getMapByKeys(Arrays.asList(keys));
    }

    /**
     * 주어진 객체를 BaseMap 클래스로 변환
     *
     * @param object 변환할 객체
     * @return BaseMap 객체
     */
    private BaseMap toMap(Object object) {
        return convert(object, BaseMap.class);
    }

    /**
     * 주어진 키를 camelCase 형식으로 변환합니다.
     *
     * @param key 변환할 키
     * @return camelCase 형식의 키
     */
    private String camelCase(String key) {
        return key.contains("_") ? toCamelCase(key, false, '_') : key;
    }
}

