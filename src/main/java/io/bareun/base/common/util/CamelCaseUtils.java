package io.bareun.base.common.util;

import org.apache.commons.text.WordUtils;

import static org.springframework.util.StringUtils.hasText;

/**
 * 주어진 문자열을 CamelCase로 변환해주는 유틸 클래스
 */
public class CamelCaseUtils {

    /**
     * 주어진 문자열을 CamelCase로 변환하는 메소드
     * @param input 변환할 문자열
     * @return CamelCase로 변환된 문자열
     */
    public static String toCamelCase(String input) {
        if (!hasText(input) || isCamelCase(input)) {
            return input;
        }

        String camelCaseStr = WordUtils.capitalizeFully(input, ' ', '_', '-');
        camelCaseStr = camelCaseStr.replaceAll("[\\s_-]", "");
        return Character.toLowerCase(camelCaseStr.charAt(0)) + camelCaseStr.substring(1);
    }

    /**
     * 주어진 문자열이 CamelCase인지 확인하는 메소드
     * @param input 확인할 문자열
     * @return CamelCase이면 true, 아니면 false
     */
    public static boolean isCamelCase(String input) {
        if (!hasText(input) || !Character.isLowerCase(input.charAt(0))) {
            return false;
        }

        boolean hasUpperCase = false;

        for (int i = 1; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (!Character.isLetterOrDigit(c)) {
                return false; // 비문자나 비숫자가 발견되면 false 반환
            }
        }

        return hasUpperCase;
    }
}
