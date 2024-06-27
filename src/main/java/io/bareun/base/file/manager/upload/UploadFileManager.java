package io.bareun.base.file.manager.upload;

import io.bareun.base.file.dto.UploadFile;
import io.bareun.base.file.manager.FileManager;
import io.bareun.base.file.util.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import static io.bareun.base.file.util.FileUtils.getExtension;
import static java.util.UUID.randomUUID;

public interface UploadFileManager extends FileManager {

    default void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalStateException("Invalid file");
        }
    }

    default String createStoredFileName(String originalFileName) {
        return randomUUID() + "." + getExtension(originalFileName);
    }

    default UploadFile upload(MultipartFile file) {
        validate(file);

        String originalFileName = file.getOriginalFilename();
        String storedFileName = createStoredFileName(originalFileName);

        FileUtils.upload(file, getFullPath(storedFileName));

        return UploadFile.of(originalFileName, storedFileName);
    }
}
