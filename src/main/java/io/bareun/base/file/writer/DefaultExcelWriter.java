package io.bareun.base.file.writer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class DefaultExcelWriter implements ExcelWriter {

    private final List<? extends Map<String, ?>> list;
    private final LinkedList<ExcelHeader> headers = new LinkedList<>();

    public static DefaultExcelWriter of(List<? extends Map<String, ?>> list) {
        return new DefaultExcelWriter(list);
    }

    public DefaultExcelWriter header(String key, String name) {
        headers.add(new DefaultExcelHeader(key, name));
        return this;
    }
}
