package io.bareun.base.file.upload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ExcelUploadFile<T> implements UploadFile {

    private final String originalFileName;
    private final String storedFileName;
    private final List<T> list = new ArrayList<>();

    public static <T> ExcelUploadFile<T> of(AttachUploadFile upload) {
        return new ExcelUploadFile<>(upload.getOriginalFileName(), upload.getStoredFileName());
    }

    public ExcelUploadFile<T> addAll(List<T> list) {
        this.list.addAll(list);
        return this;
    }

    public ExcelUploadFile<T> add(T t) {
        list.add(t);
        return this;
    }
}
