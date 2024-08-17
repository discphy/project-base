package io.bareun.base.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code QueryParam}은 메소드 파라미터에서 HTTP 요청의 쿼리 파라미터를 바인딩하기 위해 사용되는
 * 커스텀 어노테이션입니다.
 * <p>
 * 이 어노테이션은 컨트롤러 메소드에서 특정 파라미터가 요청의 쿼리 파라미터로부터 값을 주입받도록
 * 명시하는 데 사용됩니다. 이 어노테이션은 {@link io.bareun.base.web.support.QueryParamArgumentResolver}
 * 에 의해 처리됩니다.
 * </p>
 *
 * <p><b>예시:</b></p>
 * <pre>
 * public String exampleMethod(@QueryParam BaseMap queryParams) {
 *     // queryParams에 HTTP 쿼리 파라미터가 매핑됩니다.
 * }
 * </pre>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryParam {
}
