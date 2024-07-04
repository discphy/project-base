package io.bareun.base.file.manager;

import io.bareun.base.file.download.DownloadFile;
import io.bareun.base.file.upload.AttachUploadFile;
import io.bareun.base.file.util.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static io.bareun.base.file.util.FileUtils.getExtension;
import static java.util.UUID.randomUUID;
import static org.springframework.util.StringUtils.hasText;

/**
 * FileManager 인터페이스는 파일 관리 기능을 정의하는 인터페이스입니다.
 * 구현 클래스에서 파일 업로드, 다운로드, 파일명 생성 등의 기능을 제공합니다.
 */
public interface FileManager {

    /**
     * 파일이 저장될 디렉토리 경로를 반환합니다.
     *
     * @return 파일이 저장될 디렉토리 경로
     */
    String getDirectory();

    default String getSubDirectory() {
        return "";
    }

    /**
     * 주어진 파일명을 포함한 전체 파일 경로를 반환합니다.
     *
     * @param fileName 파일명
     * @return 전체 파일 경로
     */
    default String getFullPath(String fileName) {
        return getDirectory() + "/" + (hasText(getSubDirectory()) ? getSubDirectory() + "/" : "")  + fileName;
    }

    /**
     * MultipartFile이 유효한지 검증합니다.
     * 파일이 null이거나 비어있을 경우 IllegalStateException을 발생시킵니다.
     *
     * @param file 검증할 MultipartFile 객체
     */
    default void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalStateException("Invalid file");
        }
    }

    /**
     * 원본 파일명을 기반으로 저장될 파일명을 생성합니다.
     * 저장될 파일명은 랜덤 UUID와 원본 파일의 확장자를 조합하여 생성됩니다.
     *
     * @param originalFileName 원본 파일명
     * @return 생성된 저장 파일명
     */
    default String createStoredFileName(String originalFileName) {
        return randomUUID() + "." + getExtension(originalFileName);
    }

    /**
     * 주어진 MultipartFile을 업로드하고 AttachUploadFile 객체로 반환합니다.
     *
     * @param file 업로드할 MultipartFile 객체
     * @return AttachUploadFile 객체
     */
    default AttachUploadFile upload(MultipartFile file) {
        validate(file);

        String originalFileName = file.getOriginalFilename();
        String storedFileName = createStoredFileName(originalFileName);
        String storedFilePath = getFullPath(storedFileName);

        FileUtils.upload(file, storedFilePath);

        return AttachUploadFile.builder()
                .originalFileName(originalFileName)
                .storedFileName(storedFileName)
                .storedFilePath(storedFilePath)
                .size(file.getSize())
                .extension(getExtension(originalFileName))
                .build();
    }

    /**
     * DownloadFile 객체를 사용하여 파일을 다운로드합니다.
     *
     * @param downloadFile 다운로드할 파일 정보를 포함한 DownloadFile 객체
     * @param <T>          응답 바디 타입
     * @return ResponseEntity 객체로 감싼 다운로드 결과
     */
    default <T> ResponseEntity<T> download(DownloadFile<T> downloadFile) {
        return ResponseEntity.ok().headers(downloadFile.getHeaders()).body(downloadFile.getBody());
    }
}
