package io.bareun.base.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // HTTP 40x
    BAD_REQUEST(40000, "잘못된 요청 값 입니다."),
    REQUIRED(40001, "%s 값은 필수입니다."),
    VALIDATE(40002, "%s 값이 올바르지 않습니다."),

    // HTTP 50x
    UNKNOWN(50000, "알 수 없는 에러입니다."),
    ;

    private final int code;
    private final String message;
}
