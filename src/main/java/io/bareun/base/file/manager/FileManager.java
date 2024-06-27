package io.bareun.base.file.manager;

public interface FileManager {

    String getDirectory();

    default String getFullPath(String fileName) {
        return getDirectory() + "/" + fileName;
    }
}
