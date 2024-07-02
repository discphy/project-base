package io.bareun.base.file.download;

import org.springframework.http.HttpHeaders;

/**
 * DownloadFile 인터페이스는 파일 다운로드 정보를 정의하는 인터페이스입니다.
 * 구현 클래스에서는 다운로드할 파일명, HTTP 헤더, 응답 바디를 제공해야 합니다.
 *
 * @param <T> 다운로드할 데이터의 타입
 */
public interface DownloadFile<T> {

    /**
     * 다운로드할 파일명을 반환합니다.
     *
     * @return 다운로드할 파일명
     */
    String getDownloadFileName();

    /**
     * 다운로드할 파일에 대한 HTTP 헤더를 반환합니다.
     *
     * @return HTTP 헤더 객체
     */
    HttpHeaders getHeaders();

    /**
     * 다운로드할 데이터의 본문을 반환합니다.
     *
     * @return 다운로드할 데이터의 본문
     */
    T getBody();
}
