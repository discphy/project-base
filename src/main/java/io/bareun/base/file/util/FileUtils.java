package io.bareun.base.file.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

/**
 * FileUtils 클래스는 파일 관련 유틸리티 기능을 제공합니다.
 * 파일 확장자 추출 및 업로드, 리소스 획득 기능을 포함합니다.
 */
@Component
public class FileUtils {

    /**
     * 허용된 파일 확장자 목록입니다.
     */
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
        "txt", "csv", "log", // 텍스트 파일
        "jpg", "jpeg", "png", "gif", "bmp", "tiff", // 이미지 파일
        "mp4", "avi", "mov", "wmv", // 비디오 파일
        "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx" // 문서 파일
    );

    /**
     * MultipartFile 객체에서 원본 파일명을 가져옵니다.
     * 파일명이 비어 있으면 예외를 던집니다.
     * macOS에서는 파일명을 NFC 형식으로 정규화합니다.
     *
     * @param file MultipartFile 객체
     * @return 원본 파일명
     * @throws IllegalArgumentException 파일명이 비어 있는 경우 발생
     */
    public static String getOriginalFilename(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        if (!hasText(fileName)) {
            throw new IllegalArgumentException("fileName must not be empty");
        }

        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("mac")) {
            fileName = Normalizer.normalize(fileName, Normalizer.Form.NFC);
        }

        return fileName;
    }

    /**
     * 파일 이름에서 확장자를 추출하여 반환합니다.
     *
     * @param fileName 파일 이름
     * @return 추출된 확장자
     * @throws IllegalArgumentException 파일 이름이 비어 있거나 확장자가 허용된 목록에 없는 경우
     */
    public static String getExtension(String fileName) {
        if (!hasText(fileName)) {
            throw new IllegalArgumentException("fileName must not be empty");
        }

        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("extension must be one of " + ALLOWED_EXTENSIONS + ", " + extension);
        }

        return extension;
    }

    /**
     * MultipartFile을 지정된 경로에 업로드합니다.
     *
     * @param file    업로드할 파일
     * @param fullPath 저장할 전체 경로
     */
    public static void upload(MultipartFile file, String fullPath) {
        File destFile = new File(fullPath);
        File parentDirectory = destFile.getParentFile();

        if (parentDirectory != null && !parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }

        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            throw new IllegalStateException("Upload failed", e);
        }
    }

    /**
     * 소스 경로에서 대상 경로로 파일을 복사합니다. 선택적으로 복사 후 소스 파일을 삭제할 수 있습니다.
     *
     * @param source   소스 파일의 경로
     * @param target   대상 파일의 경로
     * @param isDelete true인 경우, 복사 후 소스 파일을 삭제합니다
     * @throws IllegalStateException 복사 또는 삭제 작업 중 I/O 오류가 발생한 경우
     */
    public static void copy(Path source, Path target, boolean isDelete) {
        try {
            if (Files.notExists(target.getParent())) {
                Files.createDirectories(target.getParent());
            }

            Files.copy(source, target);

            if (isDelete) {
                Files.delete(source);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Copy failed", e);
        }
    }

    /**
     * 지정된 경로의 파일을 삭제합니다.
     *
     * @param source 삭제할 파일의 경로
     * @throws IllegalStateException 삭제 작업 중 I/O 오류가 발생한 경우
     */
    public static void delete(Path source) {
        try {
            Files.delete(source);
        } catch (IOException e) {
            throw new IllegalStateException("Delete failed", e);
        }
    }

    /**
     * 지정된 파일 경로의 리소스를 반환합니다.
     *
     * @param filePath 파일 경로
     * @return 파일 리소스
     */
    public static Resource getResource(String filePath) {
        try {
            return new UrlResource("file:" + filePath);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Failed get resource ", e);
        }
    }
}
