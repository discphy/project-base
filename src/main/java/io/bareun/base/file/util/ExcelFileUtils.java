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

public class ExcelFileUtils {

    public static final int HEADER_INDEX = 0, BODY_START_INDEX = 1;

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

        // Set alignment
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    public static <T> List<T> read(MultipartFile file, Class<T> type) {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            return readSheet(workbook.getSheetAt(0), type);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read excel file", e);
        }
    }

    public static <T> byte[] write(ExcelWriter<T> excelWriter) {
        try (Workbook workbook = new SXSSFWorkbook()) {
            Sheet sheet = workbook.createSheet();
            writeHeader(sheet, excelWriter);
            writeBody(sheet, excelWriter);
            return writeMetaData(workbook);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write excel file", e);
        }
    }

    private static byte[] writeMetaData(Workbook workbook) {
		try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
			workbook.write(stream);
			return stream.toByteArray();
		} catch (IOException e) {
			throw new IllegalStateException("Failed to write meta data", e);
		}
	}

    private static <T> void writeHeader(Sheet sheet, ExcelWriter<T> excelWriter) {
        Row row = sheet.createRow(HEADER_INDEX);

        for (int a = 0; a < excelWriter.getHeaderSize(); a++) {
            Cell cell = row.createCell(a);

            cell.setCellStyle(excelWriter.getHeaderStyle(sheet.getWorkbook()));
            cell.setCellValue(excelWriter.getHeaderName(a));
		}
    }

    private static <T> void writeBody(Sheet sheet, ExcelWriter<T> excelWriter) {
        int index = BODY_START_INDEX;

        for (T column : excelWriter.getExcelColumns()) {
            writeRow(sheet.createRow(index++), column, excelWriter);
        }
    }

    private static <T> void writeRow(Row row, T column, ExcelWriter<T> excelWriter) {
        for (int a = 0; a < excelWriter.getHeaderSize(); a++) {
            setCellValue(row.createCell(a), excelWriter.getFieldValue(column, a));
        }
    }

    private static <T> List<T> readSheet(Sheet sheet, Class<T> type) {
        return rangeClosed(BODY_START_INDEX, sheet.getLastRowNum())
                .mapToObj(sheet::getRow)
                .map(ExcelFileUtils::readCell)
                .map(m -> convert(m, type))
                .collect(toList());
    }

    private static Map<String, Object> readCell(Row row) {
        Map<String, Object> map = new HashMap<>();
        Iterator<Cell> cellIterator = row.cellIterator();

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            map.put(getCellKey(cell), getCellValue(cell));
        }

        return map;
    }

    private static String getCellKey(Cell cell) {
        return cell.getSheet().getRow(0).getCell(cell.getColumnIndex()).getStringCellValue();
    }

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

    private static void setCellValue(Cell cell, Object fieldValue) {
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
