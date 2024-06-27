package io.bareun.base.file.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UploadFile {

    private final String originalFileName;
    private final String storedFileName;

    public static UploadFile of(String originalFileName, String storedFileName) {
        return new UploadFile(originalFileName, storedFileName);
    }
}
