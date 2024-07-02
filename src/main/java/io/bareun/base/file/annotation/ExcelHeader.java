package io.bareun.base.file.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * ExcelHeader는 엑셀 파일의 헤더 정보를 나타내기 위한 커스텀 어노테이션입니다.
 * <p>
 * 이 어노테이션은 필드에 적용되며, value 속성을 통해 헤더의 이름을 설정할 수 있습니다.
 * order 속성은 헤더의 순서를 지정합니다. 기본값은 1입니다.
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface ExcelHeader {

	String value() default "";
	int order() default 1;
}
