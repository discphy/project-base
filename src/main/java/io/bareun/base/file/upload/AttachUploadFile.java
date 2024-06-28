package io.bareun.base.file.upload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AttachUploadFile implements UploadFile {

    private final String originalFileName;
    private final String storedFileName;

    public static AttachUploadFile of(String originalFileName, String storedFileName) {
        return new AttachUploadFile(originalFileName, storedFileName);
    }
}
