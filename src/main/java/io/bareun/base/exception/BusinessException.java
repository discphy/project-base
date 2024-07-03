package io.bareun.base.exception;

import io.bareun.base.exception.code.BaseErrorCode;
import io.bareun.base.exception.code.ErrorCode;
import lombok.Getter;

/**
 * BusinessException은 비즈니스 로직에서 발생할 수 있는 예외를 나타내는 클래스입니다.
 * RuntimeException을 상속받아 unchecked 예외로 정의되어 있습니다.
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    /**
     * 주어진 메시지를 가지고 기본적인 UNKNOWN 에러 코드로 BusinessException을 생성합니다.
     *
     * @param message 예외 메시지
     */
    public BusinessException(String message) {
        super(message);
        this.errorCode = BaseErrorCode.UNKNOWN;
    }

    /**
     * 주어진 에러 코드로 BusinessException을 생성합니다.
     *
     * @param errorCode 에러 코드
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * 주어진 에러 코드와 추가적인 인자를 사용하여 BusinessException을 생성합니다.
     * 에러 메시지는 포맷팅된 형태로 제공됩니다.
     *
     * @param errorCode 에러 코드
     * @param args      포맷팅에 사용될 인자들
     */
    public BusinessException(ErrorCode errorCode, Object... args) {
        super(String.format(errorCode.getMessage(), args));
        this.errorCode = errorCode;
    }
}
