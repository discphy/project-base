package io.bareun.base.file.writer;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class DefaultExcelHeader implements ExcelHeader {

    private final String key;
    private final String name;
}
