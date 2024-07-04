package io.bareun.base.file.writer;

import io.bareun.base.file.util.ExcelFileUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * ExcelWriter 인터페이스는 Excel 파일 작성을 위한 기능을 정의합니다.
 * <p>
 * 구현체는 Excel 파일의 헤더 스타일, 헤더 이름 및 값을 작성하기 위한 메서드를 포함합니다.
 */
public interface ExcelWriter {

    /**
     * Excel 파일에 작성할 데이터 리스트를 반환합니다.
     *
     * @return Excel 파일에 작성할 데이터 리스트
     */
    List<? extends Map<String, ?>> getList();

    LinkedList<ExcelHeader> getHeaders();

    /**
     * 지정된 객체의 특정 인덱스에 해당하는 필드 값을 반환합니다.
     *
     * @param column 객체
     * @param index  필드 인덱스
     * @return 필드 값
     */
    default Object getFieldValue(Map<String, ?> column, int index) {
        String key = getHeaderKey(index);

        return column.get(key);
    }

    /**
     * Excel 헤더의 스타일을 반환합니다.
     *
     * @param workbook Excel 워크북
     * @return 헤더의 스타일
     */
    default CellStyle getHeaderStyle(Workbook workbook) {
        return ExcelFileUtils.getDefaultHeaderStyle(workbook);
    }


    default String getHeaderKey(int index) {
        return getHeaders().get(index).getKey();
    }

    /**
     * 주어진 인덱스에 해당하는 헤더의 이름을 반환합니다.
     *
     * @param index 헤더 인덱스
     * @return 헤더의 이름
     */
    default String getHeaderName(int index) {
        return getHeaders().get(index).getName();
    }

    /**
     * 헤더의 총 개수를 반환합니다.
     *
     * @return 헤더의 총 개수
     */
    default int getHeaderSize() {
        return getHeaders().size();
    }
}
