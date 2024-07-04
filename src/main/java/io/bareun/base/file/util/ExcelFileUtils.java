package io.bareun.base.file.util;

import io.bareun.base.file.writer.ExcelWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static io.bareun.base.common.util.ObjectMapperUtils.convert;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.rangeClosed;
import static org.apache.poi.ss.usermodel.BorderStyle.THIN;
import static org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND;
import static org.apache.poi.ss.usermodel.IndexedColors.BLACK;
import static org.apache.poi.ss.usermodel.IndexedColors.GREY_25_PERCENT;

/**
 * ExcelFileUtils 클래스는 엑셀 파일 관련 유틸리티 기능을 제공합니다.
 * 엑셀 파일의 읽기 및 쓰기 기능을 포함하며, 특정 포맷에 맞춰서 데이터를 읽고 쓸 수 있습니다.
 */
public class ExcelFileUtils {

    /**
     * 엑셀 파일의 헤더 인덱스
     */
    public static final int HEADER_INDEX = 0;

    /**
     * 엑셀 파일의 본문 시작 인덱스
     */
    public static final int BODY_START_INDEX = 1;

    /**
     * 엑셀 파일의 기본 헤더 스타일을 생성하여 반환합니다.
     *
     * @param workbook 헤더 스타일이 적용될 Workbook 인스턴스
     * @return 생성된 헤더 스타일
     */
    public static CellStyle getDefaultHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setBold(true); // bold 스타일 설정

        style.setFont(font);
        style.setFillForegroundColor(GREY_25_PERCENT.getIndex()); // 헤더 색상 설정
        style.setFillPattern(SOLID_FOREGROUND); // 색상 패턴 설정

        style.setBorderBottom(THIN);
        style.setBottomBorderColor(BLACK.getIndex());
        style.setBorderLeft(THIN);
        style.setLeftBorderColor(BLACK.getIndex());
        style.setBorderRight(THIN);
        style.setRightBorderColor(BLACK.getIndex());
        style.setBorderTop(THIN);
        style.setTopBorderColor(BLACK.getIndex());

        // 정렬 설정
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    /**
     * 엑셀 파일을 읽어 지정된 타입의 데이터 리스트로 반환합니다.
     *
     * @param file 엑셀 파일
     * @param type 반환할 데이터의 클래스 타입
     * @param <T>  반환할 데이터의 타입
     * @return 읽어온 데이터 리스트
     */
    public static <T> List<T> read(MultipartFile file, Class<T> type) {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            return readSheet(workbook.getSheetAt(0), type);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read excel file", e);
        }
    }

    /**
     * 엑셀 파일을 쓰기 위한 메타 데이터를 바이트 배열로 반환합니다.
     *
     * @param excelWriter Excel 파일 쓰기 작업을 수행하는 ExcelWriter 인스턴스
     * @return 생성된 엑셀 파일의 바이트 배열
     */
    public static byte[] write(ExcelWriter excelWriter) {
        try (Workbook workbook = new SXSSFWorkbook()) {
            Sheet sheet = workbook.createSheet();
            writeHeader(sheet, excelWriter);
            writeBody(sheet, excelWriter);
            return writeMetaData(workbook);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write excel file", e);
        }
    }

    /**
     * Workbook에서 생성된 엑셀 파일의 바이트 배열을 반환합니다.
     *
     * @param workbook Workbook 인스턴스
     * @return 생성된 엑셀 파일의 바이트 배열
     */
    private static byte[] writeMetaData(Workbook workbook) {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            workbook.write(stream);
            return stream.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write meta data", e);
        }
    }

    /**
     * Sheet에 헤더를 작성합니다.
     *
     * @param sheet       엑셀 시트
     * @param excelWriter Excel 파일 쓰기 작업을 수행하는 ExcelWriter 인스턴스
     */
    private static void writeHeader(Sheet sheet, ExcelWriter excelWriter) {
        Row row = sheet.createRow(HEADER_INDEX);

        for (int a = 0; a < excelWriter.getHeaderSize(); a++) {
            Cell cell = row.createCell(a);

            cell.setCellStyle(excelWriter.getHeaderStyle(sheet.getWorkbook()));
            cell.setCellValue(excelWriter.getHeaderName(a));
        }
    }

    /**
     * Sheet에 본문을 작성합니다.
     *
     * @param sheet       엑셀 시트
     * @param excelWriter Excel 파일 쓰기 작업을 수행하는 ExcelWriter 인스턴스
     */
    private static void writeBody(Sheet sheet, ExcelWriter excelWriter) {
        int index = BODY_START_INDEX;

        for (Map<String, ?> column : excelWriter.getList()) {
            writeRow(sheet.createRow(index++), column, excelWriter);
        }
    }

    /**
     * Row에 데이터를 작성합니다.
     *
     * @param row         엑셀 Row
     * @param column      작성할 데이터
     * @param excelWriter Excel 파일 쓰기 작업을 수행하는 ExcelWriter 인스턴스
     */
    private static void writeRow(Row row, Map<String, ?> column, ExcelWriter excelWriter) {
        for (int a = 0; a < excelWriter.getHeaderSize(); a++) {
            setCellValue(row.createCell(a), excelWriter.getFieldValue(column, a));
        }
    }

    /**
     * Sheet에서 데이터를 읽어와서 지정된 타입의 리스트로 반환합니다.
     *
     * @param sheet 시트
     * @param type  반환할 데이터의 클래스 타입
     * @param <T>   반환할 데이터의 타입
     * @return 읽어온 데이터 리스트
     */
    private static <T> List<T> readSheet(Sheet sheet, Class<T> type) {
        return rangeClosed(BODY_START_INDEX, sheet.getLastRowNum())
                .mapToObj(sheet::getRow)
                .map(ExcelFileUtils::readCell)
                .map(m -> convert(m, type))
                .collect(toList());
    }

    /**
     * Row의 셀 데이터를 Map 형태로 반환합니다.
     *
     * @param row 엑셀 Row
     * @return 셀 데이터를 담고 있는 Map
     */
    private static Map<String, Object> readCell(Row row) {
        Map<String, Object> map = new HashMap<>();
        Iterator<Cell> cellIterator = row.cellIterator();

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            map.put(getCellKey(cell), getCellValue(cell));
        }

        return map;
    }

    /**
     * 셀의 헤더 키 값을 반환합니다.
     *
     * @param cell 셀
     * @return 헤더 키 값
     */
    private static String getCellKey(Cell cell) {
        return cell.getSheet().getRow(HEADER_INDEX).getCell(cell.getColumnIndex()).getStringCellValue();
    }

    /**
     * 셀의 값에 따라 적절한 데이터 형식으로 반환합니다.
     *
     * @param cell 셀
     * @return 셀의 값
     */
    private static Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return cell.getNumericCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    /**
     * 셀에 값을 설정합니다.
     *
     * @param cell       셀
     * @param fieldValue 설정할 값
     */
    private static void setCellValue(Cell cell, Object fieldValue) {
        if (fieldValue == null) {
            cell.setCellValue("");
            return;
        }

        if (fieldValue instanceof Number) {
            cell.setCellValue(((Number) fieldValue).doubleValue());
        } else if (fieldValue instanceof Boolean) {
            cell.setCellValue((Boolean) fieldValue);
        } else if (fieldValue instanceof Date) {
            cell.setCellValue((Date) fieldValue);
        } else if (fieldValue instanceof LocalDateTime) {
            cell.setCellValue((LocalDateTime) fieldValue);
        } else if (fieldValue instanceof LocalDate) {
            cell.setCellValue((LocalDate) fieldValue);
        } else {
            cell.setCellValue(fieldValue.toString());
        }
    }
}
