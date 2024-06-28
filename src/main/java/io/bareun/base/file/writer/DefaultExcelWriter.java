package io.bareun.base.file.writer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class DefaultExcelWriter<T> implements ExcelWriter<T> {

    private final List<?> list;
    private final Class<T> type;

    public static <T> DefaultExcelWriter<T> of(List<?> list, Class<T> type) {
        return new DefaultExcelWriter<>(list, type);
    }
}
