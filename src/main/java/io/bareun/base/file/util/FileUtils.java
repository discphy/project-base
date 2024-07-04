package io.bareun.base.file.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import static org.springframework.util.StringUtils.hasText;

/**
 * FileUtils 클래스는 파일 관련 유틸리티 기능을 제공합니다.
 * 파일 확장자 추출 및 업로드, 리소스 획득 기능을 포함합니다.
 */
@Component
public class FileUtils {

    /**
     * 파일 이름에서 확장자를 추출하여 반환합니다.
     *
     * @param fileName 파일 이름
     * @return 추출된 확장자
     */
    public static String getExtension(String fileName) {
        if (!hasText(fileName)) {
            throw new IllegalArgumentException("fileName must not be empty");
        }

        return fileName.substring(fileName.lastIndexOf(".") + 1);
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
            throw new IllegalStateException("Fail upload ", e);
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
            throw new IllegalStateException("Fail get resource ", e);
        }
    }
}
