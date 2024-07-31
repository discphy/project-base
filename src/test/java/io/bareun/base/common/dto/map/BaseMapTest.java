package io.bareun.base.common.dto.map;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * BaseMap 클래스에 대한 테스트 클래스입니다.
 */
class BaseMapTest {

    /**
     * JSON 문자열 파싱을 테스트하는 메서드입니다.
     * JSON 문자열을 BaseMap 객체로 변환하고,
     * 변환된 BaseMap 객체의 값을 확인합니다.
     */
    @Test
    void jsonParsing() {
        String json = "{\n" +
                "  \"search_map\": {\n" +
                "    \"string_value\": \"formal_contents\",\n" +
                "    \"long_value\": 1203405603,\n" +
                "    \"double_value\": 1.2,\n" +
                "    \"integer_value\": 10,\n" +
                "    \"map_value\" : {\n" +
                "      \"hello_world\" : \"I'm project base\"\n" +
                "    },\n" +
                "    \"list_string_value\": [\n" +
                "      \"one\",\n" +
                "      \"two\"\n" +
                "    ],\n" +
                "    \"LIST\": [\n" +
                "      {\n" +
                "        \"key\": \"value\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        BaseMap baseMap = BaseMap.of(json);

        System.out.println("baseMap = " + baseMap);

        assertThat(baseMap).isNotNull();
        assertThat(baseMap.getMap("searchMap")).isNotNull();
        assertThat(baseMap.getMap("searchMap").getString("stringValue")).isEqualTo("formal_contents");
        assertThat(baseMap.getMap("searchMap").getLong("longValue")).isEqualTo(1203405603);
        assertThat(baseMap.getMap("searchMap").getDouble("doubleValue")).isEqualTo(1.2);
        assertThat(baseMap.getMap("searchMap").getInteger("integerValue")).isEqualTo(10);
        assertThat(baseMap.getMapByKeys("searchMap", "mapValue").getString("helloWorld")).isEqualTo("I'm project base");
        assertThat(baseMap.getMap("searchMap").getList("listStringValue").get(0)).isEqualTo("one");

        List<BaseMap> list = baseMap.getMap("searchMap").getMapList("list");
        assertThat(list.size()).isEqualTo(1);

        BaseMap item = list.get(0);
        assertThat(item.getString("key")).isEqualTo("value");
    }
}