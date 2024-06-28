package io.bareun.base.file.writer;

import io.bareun.base.file.annotation.ExcelHeader;
import io.bareun.base.file.util.ExcelFileUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static io.bareun.base.common.util.ObjectMapperUtils.convert;
import static java.util.Collections.addAll;
import static java.util.Comparator.comparingInt;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

public interface ExcelWriter<T> {

    List<?> getList();

    Class<T> getType();

    default Object getFieldValue(T column, int index) {
        Field field = getExcelFields().get(index);
        field.setAccessible(true);

        try {
            return field.get(column);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot access field " + field.getName(), e);
        }
    }

    default List<T> getExcelColumns() {
        return getList().stream()
                .map(v -> convert(v, getType()))
                .collect(toList());
    }

    default CellStyle getHeaderStyle(Workbook workbook) {
        return ExcelFileUtils.getDefaultHeaderStyle(workbook);
    }

    default String getHeaderName(int index) {
        return getHeaderNames().get(index);
    }

    default int getHeaderSize() {
        return getHeaderNames().size();
    }

    default LinkedList<String> getHeaderNames() {
        return getExcelFields().stream()
                .map(this::getName)
                .collect(toCollection(LinkedList::new));
    }

    default LinkedList<Field> getExcelFields() {
		return getFields().stream()
                .filter(this::hasAnnotation)
                .sorted(comparingInt(this::getOrder))
                .collect(toCollection(LinkedList::new));
	}

    default List<Field> getFields() {
        List<Field> fields = new ArrayList<>();

        if (nonNull(getType().getSuperclass())) {
            addAll(fields, getType().getSuperclass().getDeclaredFields());
        }

        addAll(fields, getType().getDeclaredFields());

        return fields;
    }

    default boolean hasAnnotation(Field field) {
        return field.isAnnotationPresent(ExcelHeader.class);
    }

    default int getOrder(Field field) {
        return field.getAnnotation(ExcelHeader.class).order();
    }

    default String getName(Field field) {
        return field.getAnnotation(ExcelHeader.class).value();
    }
}
