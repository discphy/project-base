package io.bareun.base.file.manager;

import io.bareun.base.file.upload.AttachUploadFile;
import io.bareun.base.file.util.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import static io.bareun.base.file.util.FileUtils.getExtension;
import static io.bareun.base.file.util.FileUtils.getOriginalFilename;

public interface FileWithoutTempManager extends FileManager {

    @Override
    default String getTempDirectory() {
        throw new UnsupportedOperationException("getTempDirectory not implemented");
    }

    @Override
    default String getTempPath() {
        throw new UnsupportedOperationException("getTempPath not implemented");
    }

    /**
     * Temp 경로를 사용하지 않습니다.
     *
     * @param fileName 파일명
     */
    @Override
    default String getTempFullPath(String fileName) {
        throw new UnsupportedOperationException("getTempFullPath not implemented");
    }

    /**
     * 주어진 MultipartFile을 업로드하고 AttachUploadFile 객체로 반환합니다.
     *
     * @param file 업로드할 MultipartFile 객체
     * @return AttachUploadFile 객체
     */
    default AttachUploadFile upload(MultipartFile file) {
        validate(file);

        String originalFileName = getOriginalFilename(file);
        String extension = getExtension(originalFileName);
        String storedFileName = createStoredFileNameByExtension(extension);
        String storedFilePath = getFullPath(storedFileName);
        String storedPath = getPath();

        FileUtils.upload(file, storedFilePath);

        return AttachUploadFile.builder()
                .originalFileName(originalFileName)
                .storedFileName(storedFileName)
                .storedFilePath(storedFilePath)
                .storedPath(storedPath)
                .size(file.getSize())
                .extension(extension)
                .build();
    }
}
