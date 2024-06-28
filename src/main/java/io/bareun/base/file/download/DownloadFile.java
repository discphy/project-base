package io.bareun.base.file.download;

import org.springframework.http.HttpHeaders;

public interface DownloadFile<T> {

    String getDownloadFileName();

    HttpHeaders getHeaders();

    T getBody();
}
