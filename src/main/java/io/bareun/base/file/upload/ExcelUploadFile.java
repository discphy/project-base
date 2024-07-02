package io.bareun.base.file.upload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * ExcelUploadFile 클래스는 엑셀 파일 업로드 시 사용되는 클래스입니다.
 * 업로드된 파일의 원본 파일명과 저장된 파일명을 가지며, 엑셀 파일에서 읽어온 데이터 리스트를 관리합니다.
 *
 * @param <T> 엑셀 파일에서 읽어온 데이터의 타입
 */
@Getter
@RequiredArgsConstructor
public class ExcelUploadFile<T> implements UploadFile {

    /**
     * 업로드된 파일의 원본 파일명
     */
    private final String originalFileName;

    /**
     * 업로드된 파일의 저장된 파일명
     */
    private final String storedFileName;

    /**
     * 엑셀 파일에서 읽어온 데이터를 저장하는 리스트
     */
    private final List<T> list = new ArrayList<>();

    /**
     * AttachUploadFile 인스턴스에서 ExcelUploadFile 인스턴스를 생성하여 반환합니다.
     *
     * @param upload 업로드된 파일 정보를 담고 있는 AttachUploadFile 인스턴스
     * @param <T>    엑셀 파일에서 읽어온 데이터의 타입
     * @return ExcelUploadFile 인스턴스
     */
    public static <T> ExcelUploadFile<T> of(AttachUploadFile upload) {
        return new ExcelUploadFile<>(upload.getOriginalFileName(), upload.getStoredFileName());
    }

    /**
     * 엑셀 파일에서 읽어온 데이터 리스트를 추가합니다.
     *
     * @param list 추가할 데이터 리스트
     * @return ExcelUploadFile 인스턴스
     */
    public ExcelUploadFile<T> addAll(List<T> list) {
        this.list.addAll(list);
        return this;
    }

    /**
     * 엑셀 파일에서 읽어온 데이터를 추가합니다.
     *
     * @param t 추가할 데이터
     * @return ExcelUploadFile 인스턴스
     */
    public ExcelUploadFile<T> add(T t) {
        list.add(t);
        return this;
    }
}
