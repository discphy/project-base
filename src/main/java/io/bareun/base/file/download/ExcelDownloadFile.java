package io.bareun.base.file.download;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

@Builder
@RequiredArgsConstructor
public class ExcelDownloadFile implements DownloadFile<byte[]> {

    private final String downloadFileName;
    private final byte[] metaData;

    public String getDownloadFileName() {
        try {
            return URLEncoder.encode(downloadFileName, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Unsupported encoding", e);
        }
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
		headers.setContentType(APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", getDownloadFileName());

		return headers;
    }

    @Override
    public byte[] getBody() {
        return metaData;
    }
}
