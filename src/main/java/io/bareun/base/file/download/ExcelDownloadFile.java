package io.bareun.base.file.download;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

/**
 * ExcelDownloadFile 클래스는 Excel 파일 다운로드를 위한 DownloadFile 인터페이스의 구현체입니다.
 * 다운로드할 파일명과 메타데이터(byte 배열)를 가지고 있으며, HTTP 헤더와 바이트 배열 데이터를 제공합니다.
 */
@Builder
@RequiredArgsConstructor
public class ExcelDownloadFile implements DownloadFile<byte[]> {

    /**
     * 다운로드할 파일명
     */
    private final String downloadFileName;
    /**
     * Excel 파일의 메타데이터 (바이트 배열 형식)
     */
    private final byte[] metaData;

    /**
     * UTF-8로 인코딩된 다운로드 파일명을 반환합니다.
     *
     * @return 인코딩된 파일명
     * @throws IllegalStateException 인코딩 지원되지 않는 경우
     */
    public String getDownloadFileName() {
        return URLEncoder.encode(downloadFileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    }

    /**
     * HTTP 다운로드 헤더를 설정하여 반환합니다.
     * 파일 형식은 APPLICATION_OCTET_STREAM으로 설정되며, 다운로드할 파일명도 포함됩니다.
     *
     * @return HTTP 헤더 객체
     */
    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
		headers.setContentType(APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", getDownloadFileName());

		return headers;
    }

    /**
     * Excel 파일의 메타데이터 (바이트 배열)을 반환합니다.
     *
     * @return Excel 파일의 바이트 배열 데이터
     */
    @Override
    public byte[] getBody() {
        return metaData;
    }
}
