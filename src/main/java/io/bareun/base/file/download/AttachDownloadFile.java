package io.bareun.base.file.download;

import io.bareun.base.file.util.FileUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

@Getter
@Builder
@RequiredArgsConstructor
public class AttachDownloadFile implements DownloadFile<Resource> {

    private final String downloadFileName;
    private final String storedFilePath;

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentDispositionFormData("attachment", getDownloadFileName());

        return headers;
    }

    @Override
    public Resource getBody() {
        return FileUtils.getResource(storedFilePath);
    }
}
