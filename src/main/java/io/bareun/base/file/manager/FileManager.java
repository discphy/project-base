package io.bareun.base.file.manager;

import io.bareun.base.file.download.DownloadFile;
import io.bareun.base.file.upload.AttachUploadFile;
import io.bareun.base.file.util.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.bareun.base.file.util.FileUtils.getExtension;
import static io.bareun.base.file.util.FileUtils.getOriginalFilename;
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

    /**
     * 임시 파일이 저장될 디렉토리 경로를 반환합니다.
     *
     * @return 임시 파일이 저장될 디렉토리 경로
     */
    String getTempDirectory();

    /**
     * 서브 디렉토리 경로를 반환합니다.
     * 기본값은 빈 문자열입니다.
     *
     * @return 서브 디렉토리 경로
     */
    default String getSubDirectory() {
        return "";
    }

    /**
     * 전체 파일 경로를 반환합니다.
     *
     * @return 전체 파일 경로
     */
    default String getPath() {
        return getDirectory() + (hasText(getSubDirectory()) ? "/" + getSubDirectory() : "");
    }

    /**
     * 임시 파일의 전체 경로를 반환합니다.
     *
     * @return 임시 파일의 전체 경로
     */
    default String getTempPath() {
        return getTempDirectory() + (hasText(getSubDirectory()) ? "/" + getSubDirectory() : "");
    }

    /**
     * 주어진 파일명을 포함한 전체 파일 경로를 반환합니다.
     *
     * @param fileName 파일명
     * @return 전체 파일 경로
     */
    default String getFullPath(String fileName) {
        return getPath() + "/" + fileName;
    }

    /**
     * 주어진 파일명을 포함한 임시 파일의 전체 경로를 반환합니다.
     *
     * @param fileName 파일명
     * @return 임시 파일의 전체 경로
     */
    default String getTempFullPath(String fileName) {
        return getTempPath() + "/" + fileName;
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
     * 파일 경로가 유효한지 검증합니다.
     * 파일 경로가 null이거나 존재하지 않을 경우 IllegalArgumentException을 발생시킵니다.
     *
     * @param filePath 검증할 파일 경로
     */
    default void validate(Path filePath) {
        if (filePath == null || !Files.exists(filePath)) {
            throw new IllegalArgumentException("Not found file: " +
                    (filePath == null ? "File is NULL" : filePath.toAbsolutePath()));
        }
    }

    /**
     * 확장자를 기반으로 저장될 파일명을 생성합니다.
     * 저장될 파일명은 랜덤 UUID와 확장자를 조합하여 생성됩니다.
     *
     * @param extension 파일 확장자
     * @return 생성된 저장 파일명
     */
    default String createStoredFileNameByExtension(String extension) {
        return randomUUID() + "." + extension;
    }

    /**
     * 주어진 MultipartFile을 업로드하고 AttachUploadFile 객체로 반환합니다.
     *
     * @param file 업로드할 MultipartFile 객체
     * @return AttachUploadFile 객체
     */
    default AttachUploadFile upload(MultipartFile file) {
        validate(file);

        String originalFileName = getOriginalFilename(file);
        String extension = getExtension(originalFileName);
        String storedFileName = createStoredFileNameByExtension(extension);
        String storedFilePath = getTempFullPath(storedFileName);
        String storedPath = getTempPath();

        FileUtils.upload(file, storedFilePath);

        return AttachUploadFile.builder()
                .originalFileName(originalFileName)
                .storedFileName(storedFileName)
                .storedFilePath(storedFilePath)
                .storedPath(storedPath)
                .size(file.getSize())
                .extension(extension)
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

    /**
     * 임시 경로의 파일을 실제 경로로 복사합니다.
     *
     * @param tempStoredPath   임시 파일 경로
     * @param tempStoredFileName 임시 파일 이름
     * @return AttachUploadFile 객체
     */
    default AttachUploadFile copy(String tempStoredPath, String tempStoredFileName) {
        Path tempFilePath = Paths.get(tempStoredPath, tempStoredFileName);

        validate(tempFilePath);

        String storedFilePath = getFullPath(tempStoredFileName);
        String storedPath = getPath();

        Path realFilePath = Paths.get(storedPath, tempStoredFileName);

        FileUtils.copy(tempFilePath, realFilePath, true);

        return AttachUploadFile.builder()
                .storedFileName(tempStoredFileName)
                .storedFilePath(storedFilePath)
                .storedPath(storedPath)
                .build();
    }

    /**
     * 주어진 경로의 파일을 삭제합니다.
     *
     * @param storedPath   파일이 저장된 경로
     * @param storedFileName 삭제할 파일의 이름
     */
    default void delete(String storedPath, String storedFileName) {
        Path filePath = Paths.get(storedPath, storedFileName);

        validate(filePath);

        FileUtils.delete(filePath);
    }
}
