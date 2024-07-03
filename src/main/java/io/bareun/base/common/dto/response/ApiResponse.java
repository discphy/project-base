package io.bareun.base.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * API 응답을 위한 클래스입니다.
 * <p>
 * 이 클래스는 API 요청에 대해 반환할 데이터 규격을 정의합니다.
 */
@Data
@Builder
@JsonInclude(NON_NULL)
public class ApiResponse<T> {

    /**
     * 응답 코드
     */
    private final int code;

    /**
     * 응답 메시지
     */
    private final String message;

    /**
     * API 응답의 결과 데이터를 포함
     */
    private final T result;

    /**
     * 실패 응답을 생성하는 메소드.
     *
     * @param code    응답 코드
     * @param message 응답 메시지
     */
    public static ApiResponse<?> fail(int code, String message) {
        return ApiResponse.builder()
                .code(code)
                .message(message)
                .build();
    }

    /**
     * 성공 응답을 생성하는 메소드.
     *
     * @param result 결과 데이터
     */
    public static <T> ApiResponse<T> success(T result) {
        return ApiResponse.<T>builder()
                .code(0)
                .message(HttpStatus.OK.getReasonPhrase())
                .result(result)
                .build();
    }

    /**
     * 결과 데이터 없는 성공 응답을 생성하는 메소드.
     */
    public static <T> ApiResponse<T> success() {
        return ApiResponse.<T>builder()
                .code(0)
                .message(HttpStatus.OK.getReasonPhrase())
                .build();
    }
}
