package io.bareun.base.common.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CamelCaseUtilsTest {

    @Test
    void camelCase() {
        String test1 = "hello world";
        String test2 = "hello World";
        String test3 = "hello_world";
        String test4 = "hello-World";
        String test5 = "HELLO-WORLD";
        String test6 = "HELLOWORLD";
        String test7 = "helloWorld";
        String test8 = "LEVEL";

        assertThat(CamelCaseUtils.toCamelCase(test1)).isEqualTo("helloWorld");
        assertThat(CamelCaseUtils.toCamelCase(test2)).isEqualTo("helloWorld");
        assertThat(CamelCaseUtils.toCamelCase(test3)).isEqualTo("helloWorld");
        assertThat(CamelCaseUtils.toCamelCase(test4)).isEqualTo("helloWorld");
        assertThat(CamelCaseUtils.toCamelCase(test5)).isEqualTo("helloWorld");
        assertThat(CamelCaseUtils.toCamelCase(test6)).isEqualTo("helloworld");
        assertThat(CamelCaseUtils.toCamelCase(test7)).isEqualTo("helloWorld");
        assertThat(CamelCaseUtils.toCamelCase(test8)).isEqualTo("level");
    }
}