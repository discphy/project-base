package io.bareun.base.file.upload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * AttachUploadFile 클래스는 업로드된 파일의 원본 파일명과 저장된 파일명을 가지고 있는 클래스입니다.
 */
@Getter
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

    /**
     * 주어진 원본 파일명과 저장된 파일명으로 AttachUploadFile 인스턴스를 생성하여 반환합니다.
     *
     * @param originalFileName 원본 파일명
     * @param storedFileName   저장된 파일명
     * @return AttachUploadFile 인스턴스
     */
    public static AttachUploadFile of(String originalFileName, String storedFileName) {
        return new AttachUploadFile(originalFileName, storedFileName);
    }
}
