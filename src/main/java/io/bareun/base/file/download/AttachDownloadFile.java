package io.bareun.base.file.download;

import io.bareun.base.file.util.FileUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * AttachDownloadFile 클래스는 첨부 파일 다운로드를 위한 DownloadFile 인터페이스의 구현체입니다.
 * 다운로드할 파일의 이름과 저장된 파일 경로를 가지고 있으며, HTTP 헤더와 실제 리소스를 제공합니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class AttachDownloadFile implements DownloadFile<Resource> {

    /**
     * 다운로드할 파일명
     */
    private final String downloadFileName;

    /**
     * 저장된 파일 경로
     */
    private final String storedFilePath;

    /**
     * UTF-8로 인코딩된 다운로드 파일명을 반환합니다.
     *
     * @return 인코딩된 파일명
     * @throws IllegalStateException 인코딩 지원되지 않는 경우
     */
    @Override
    public String getDownloadFileName() {
        return URLEncoder.encode(downloadFileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    }

    /**
     * HTTP 다운로드 헤더를 설정하여 반환합니다.
     *
     * @return HTTP 헤더 객체
     */
    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", getDownloadFileName());

        return headers;
    }

    /**
     * 저장된 파일 경로로부터 리소스를 읽어와 반환합니다.
     *
     * @return 다운로드할 리소스 객체
     */
    @Override
    public Resource getBody() {
        return FileUtils.getResource(storedFilePath);
    }
}
