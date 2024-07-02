package io.bareun.base.file.writer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class DefaultExcelWriter<T> implements ExcelWriter<T> {

    private final List<?> list;
    private final Class<T> type;

    /**
     * 주어진 리스트와 클래스 타입으로 DefaultExcelWriter 객체를 생성하여 반환합니다.
     *
     * @param list 데이터 리스트
     * @param type 데이터 객체의 클래스 타입
     * @param <T>  데이터 객체의 타입
     * @return DefaultExcelWriter 객체
     */
    public static <T> DefaultExcelWriter<T> of(List<?> list, Class<T> type) {
        return new DefaultExcelWriter<>(list, type);
    }
}
