package io.bareun.base.file.upload;

/**
 * UploadFile 인터페이스는 업로드된 파일의 원본 파일명과 저장된 파일명을 제공하는 메서드를 정의합니다.
 */
public interface UploadFile {

    /**
     * 업로드된 파일의 원본 파일명을 반환합니다.
     *
     * @return 원본 파일명
     */
    String getOriginalFileName();

    /**
     * 업로드된 파일의 저장된 파일명을 반환합니다.
     *
     * @return 저장된 파일명
     */
    String getStoredFileName();

    String getStoredFilePath();

    String getExtension();

    long getSize();
}
