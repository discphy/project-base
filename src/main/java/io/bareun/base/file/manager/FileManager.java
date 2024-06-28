package io.bareun.base.file.manager;

import io.bareun.base.file.download.DownloadFile;
import io.bareun.base.file.upload.AttachUploadFile;
import io.bareun.base.file.util.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static io.bareun.base.file.util.FileUtils.getExtension;
import static java.util.UUID.randomUUID;

public interface FileManager {

    String getDirectory();

    default String getFullPath(String fileName) {
        return getDirectory() + "/" + fileName;
    }

    default void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalStateException("Invalid file");
        }
    }

    default String createStoredFileName(String originalFileName) {
        return randomUUID() + "." + getExtension(originalFileName);
    }

    default AttachUploadFile upload(MultipartFile file) {
        validate(file);

        String originalFileName = file.getOriginalFilename();
        String storedFileName = createStoredFileName(originalFileName);

        FileUtils.upload(file, getFullPath(storedFileName));

        return AttachUploadFile.of(originalFileName, storedFileName);
    }

    default <T> ResponseEntity<T> download(DownloadFile<T> downloadFile) {
        return ResponseEntity.ok().headers(downloadFile.getHeaders()).body(downloadFile.getBody());
    }
}
