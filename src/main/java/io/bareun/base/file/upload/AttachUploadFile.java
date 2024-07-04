package io.bareun.base.file.upload;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * AttachUploadFile 클래스는 업로드된 파일의 원본 파일명과 저장된 파일명을 가지고 있는 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class AttachUploadFile implements UploadFile {

    /**
     * 업로드된 파일의 원본 파일명
     */
    private final String originalFileName;

    /**
     * 업로드된 파일의 저장된 파일명
     */
    private final String storedFileName;

    private final String storedFilePath;

    private final String extension;

    private final long size;
}
