# project-base

바른아이오 공통 프레임워크 

## 목차
- [버전 정보](#버전-정보)
- [의존성](#의존성)
- [라이브러리 생성](#라이브러리-생성)
- [프레임워크 모듈](#프레임워크-모듈)
  - [API](#api)
  - [공통](#공통)
  - [예외 처리](#예외-처리)
  - [파일 처리](#파일-처리)
  - [로깅](#로깅)
- [샘플 프로젝트](#샘플-프로젝트)
- [이슈](#이슈)

## 버전 정보 

```text
[자바] java : 1.8
[스프링 부트] org.springframework.boot : 2.7.12
[전자정부 프레임워크] org.egovframework : 4.2 
```

## 의존성

[`build.gradle`]

```groovy
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.springframework.boot:spring-boot-starter-webflux'
	implementation 'org.springframework.boot:spring-boot-starter-security'

    implementation("org.egovframe.rte:org.egovframe.rte.bat.core:4.2.0") {
		exclude group: 'org.egovframe.rte', module: 'org.egovframe.rte.fdl.logging'
	}

	implementation 'org.apache.commons:commons-collections4:4.4'
	implementation 'org.apache.commons:commons-text:1.10.0'
	implementation 'org.apache.poi:poi-ooxml:5.2.4'

	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testImplementation 'org.springframework.security:spring-security-test'
	testImplementation 'io.projectreactor:reactor-test'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

## 라이브러리 생성

**`Nexus`를 고려하고 있지만 현재는 `jar`로 제공**

[`build.gradle`]

```groovy
plugins {
	id 'java'
	id 'org.springframework.boot' version '2.7.12'
	id 'io.spring.dependency-management' version '1.0.15.RELEASE'
	id 'maven-publish'
	id 'com.github.johnrengelman.shadow' version '7.1.2'
}

...(중략)

shadowJar {
    archiveFileName.set('project-base-0.0.1-all.jar')
}

publishing {
	publications {
		mavenJava(MavenPublication) {
			artifact shadowJar
		}
	}
}
```

`com.github.johnrengelman.shadow` : `jar` 파일 생성 시 전체 의존성(`dependencies`) 포함

[`jar` 파일 생성 방법]  

```text
./gradlew shadowJar 
```

위의 명령어 실행 시, `build/libs` 경로에 `project-base-0.0.1-all.jar` 생성된다.


## 프레임워크 모듈

자세한 예제 코드는 `project-sample` 에서 설명한다. 

### API 

`io.bareun.base.api`

**외부 `API`를 호출하기 위한 모듈 (동기, 비동기 지원)**

```java
/**
 * {@link WebClient}를 사용하여 웹 API 호출을 수행하는 클라이언트입니다.
 */
@Slf4j
@Component
public class WebApiClient {

    private final WebClient webClient;
    
    ...(중략)

    /**
     * 주어진 API 요청을 동기적으로 호출하고 응답을 반환합니다.
     *
     * @param <T> 응답 본문의 타입
     * @param request 호출할 API 요청
     * @return API 요청의 응답 본문
     */
    public <T> T callReturn(ApiRequest<T> request) {
        return retrieve(request).block();
    }

    /**
     * 주어진 API 요청을 비동기적으로 호출합니다.
     *
     * @param <T> 응답 본문의 타입
     * @param request 호출할 API 요청
     */
    public <T> void call(ApiRequest<T> request) {
        retrieve(request).subscribe(request::subscribe, request::error);
    }
    
    ...(중략)
}
```

`WebClient`를 사용하여 `API`를 호출하며 `callReturn(ApiRequest<T> request)`과 `call(ApiRequest<T> request)` 는 각각 동기, 비동기를 호출하는 메서드이다.    


```java
/**
 * API 요청을 표현하는 인터페이스입니다.
 *
 * @param <T> 응답 타입
 */
public interface ApiRequest<T> {

    /**
     * HTTP 메서드를 반환합니다.
     *
     * @return 요청에 사용될 HTTP 메서드
     */
    HttpMethod getMethod();

    /**
     * 요청할 URL을 반환합니다.
     *
     * @return 요청할 URL
     */
    String getUrl();

    /**
     * 요청 본문을 반환합니다.
     *
     * @return 요청 본문
     */
    Object getBody();

    /**
     * 응답 타입을 반환합니다.
     *
     * @return 응답 타입 클래스
     */
    Class<T> getResponseType();
    
    ...(중략)
}

```

`API` 요청 인터페이스, 인터페이스 구현한 클래스로 `WebApiClient`의 메소드를 호출한다.

추가로, `Content-Type: application/json`를 지원하는 `JsonApiRequest` 인터페이스와 편리하게 인스턴스를 생성하는 빌더 클래스 `ApiRequestBuilder`도 제공한다. 

### 공통 

`io.bareun.base.common`

**공통으로 사용하는 `Map` 클래스와 공통 `Controller` 응답 클래스, 자주 사용하는 유틸 클래스**

```java
/**
 * 키-값 쌍을 보관하고 조작하는 데 사용되는 DTO 맵 클래스입니다.
 * 기본적으로 키를 camelCase로 변환합니다.
 */
public class BaseMap extends HashMap<String, Object> {
    ...(중략)
}
```

공통으로 사용하는 `Map` 클래스로, 키는 `camelCase`로 변환을 시킵니다. 편하게 사용 한 `org.apache.commons.collections4.MapUtils`에 의거한 메소드도 추가하였습니다. 

```java
/**
 * BaseMap 클래스에 대한 테스트 클래스입니다.
 */
class BaseMapTest {

    /**
     * JSON 문자열 파싱을 테스트하는 메서드입니다.
     * JSON 문자열을 BaseMap 객체로 변환하고,
     * 변환된 BaseMap 객체의 값을 확인합니다.
     */
    @Test
    void jsonParsing() {
        String json = "{\n" +
                "  \"search_map\": {\n" +
                "    \"string_value\": \"formal_contents\",\n" +
                "    \"long_value\": 1203405603,\n" +
                "    \"double_value\": 1.2,\n" +
                "    \"integer_value\": 10,\n" +
                "    \"map_value\" : {\n" +
                "      \"hello_world\" : \"I'm project base\"\n" +
                "    },\n" +
                "    \"list_string_value\": [\n" +
                "      \"one\",\n" +
                "      \"two\"\n" +
                "    ],\n" +
                "    \"list_map_value\": [\n" +
                "      {\n" +
                "        \"key\": \"value\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        BaseMap baseMap = BaseMap.of(json);

        System.out.println("baseMap = " + baseMap);

        assertThat(baseMap).isNotNull();
        assertThat(baseMap.getMap("searchMap")).isNotNull();
        assertThat(baseMap.getMap("searchMap").getString("stringValue")).isEqualTo("formal_contents");
        assertThat(baseMap.getMap("searchMap").getLong("longValue")).isEqualTo(1203405603);
        assertThat(baseMap.getMap("searchMap").getDouble("doubleValue")).isEqualTo(1.2);
        assertThat(baseMap.getMap("searchMap").getInteger("integerValue")).isEqualTo(10);
        assertThat(baseMap.getMapByKeys("searchMap", "mapValue").getString("helloWorld")).isEqualTo("I'm project base");
        assertThat(baseMap.getMap("searchMap").getList("listStringValue").get(0)).isEqualTo("one");

        List<BaseMap> list = baseMap.getMap("searchMap").getMapList("listMapValue");
        assertThat(list.size()).isEqualTo(1);

        BaseMap item = list.get(0);
        assertThat(item.getString("key")).isEqualTo("value");
    }
}
```

`json` 문자열을 `BaseMap`으로 변환하고 `Key`값을 이용하여 `Value`값을 찾는 테스트 코드 입니다. **사용 예제로 참고**

```java
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
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(result)
                .build();
    }

    /**
     * 결과 데이터 없는 성공 응답을 생성하는 메소드.
     */
    public static <T> ApiResponse<T> success() {
        return ApiResponse.<T>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .build();
    }
}
```

다음과 같은 `json` 결과 값을 공통 처리 한다. 정상 응답시 `success` 메소드를 이용하여 리턴하면 된다.

```json
{
  "code" : (int),
  "message" : (String),
  "result" : (T)
}
```

**유틸 클래스** 
- `ObjectMapperUtils` : `JSON` 객체를 변환하는 유틸 클래스 (`String`-`T` / `Object`-`T`)
- `RequestUtils` : `HttpServletRequest` 및 `HttpSession` 처리하는 유틸 클래스
- `ResponseUtils` : `HttpServletResponse` 유틸 클래스
- `SecurityUtils` : `Spring Security` 유틸 클래스

### 예외 처리 

`io.bareun.base.exception`

**기본적으로 예외 처리에 사용하는 에러 코드 및 `Exception`정의, 예외 핸들러 클래스가 제공**

```java
public interface ErrorCode {

    int getCode();

    String getMessage();
}
```

기본 에러 코드 인터페이스이다. 각 프로젝트에서 `ErrorCode`를 구현해서 사용하면 된다. 기본으로 `ErrorCode`를 구현한 에러코드는 다음과 같다.

```java
/**
 * ErrorCode는 예외 처리에 사용되는 오류 코드를 정의하는 열거형 클래스입니다.
 * 각 열거 상수는 오류 코드와 메시지를 가지고 있습니다.
 */
@Getter
@RequiredArgsConstructor
public enum BaseErrorCode implements ErrorCode {

    /**
     * HTTP 40x 코드 기반 5자리
     */
    BAD_REQUEST(40000, "잘못된 요청 값 입니다."),
    REQUIRED(40001, "%s 값은 필수입니다."),
    VALIDATE(40002, "%s 값이 올바르지 않습니다."),

    /**
     * HTTP 50x 코드 기반 5자리
     */
    UNKNOWN(50000, "알 수 없는 에러입니다."),
    ;

    private final int code;
    private final String message;
}
```

앞의 3자리는 [HTTP Status Code](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status) 코드로 구성하며, 뒤의 2자리는 커스텀 코드이다.

```java
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
```

위의 `ErrorCode`를 기반으로 하는 예외 클래스이다. 해당 예외 클래스는 업무관련된 사용자 에러메세지를 처리한다.

```java
/**
 * ApiExceptionHandler는 Spring Web MVC에서 발생하는 예외를 처리하는 클래스입니다.
 * RestControllerAdvice 애노테이션을 사용하여 모든 @RestController에서 발생하는 예외를 처리합니다.
 * 각 예외에 따라 적절한 HTTP 상태 코드와 메시지를 반환합니다.
 */
@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler {

    /**
     * BusinessException을 처리하는 메서드입니다.
     *
     * @param e 발생한 BusinessException 객체
     * @return ApiResponse 객체 (실패 응답)
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<?> buisnessException(BusinessException e) {
        log.error("ApiException buisnessException", e);
        return ApiResponse.fail(e.getErrorCode().getCode(), e.getMessage());
    }
    
    ...(중략)
}
```

위의 예외 클래스를 핸들러하는 클래스이다. `ApiResponse`의 `fail()` 메소드를 반환한다. 

### 파일 처리 

`io.bareun.base.file`

**첨부 파일 및 엑셀 파일 업로드, 다운로드 지원하는 모듈**

```java
/**
 * FileManager 인터페이스는 파일 관리 기능을 정의하는 인터페이스입니다.
 * 구현 클래스에서 파일 업로드, 다운로드, 파일명 생성 등의 기능을 제공합니다.
 */
public interface FileManager {

    /**
     * 파일이 저장될 디렉토리 경로를 반환합니다.
     *
     * @return 파일이 저장될 디렉토리 경로
     */
    String getDirectory();

    ...(중략)

    /**
     * 주어진 MultipartFile을 업로드하고 AttachUploadFile 객체로 반환합니다.
     *
     * @param file 업로드할 MultipartFile 객체
     * @return AttachUploadFile 객체
     */
    default AttachUploadFile upload(MultipartFile file) {
        validate(file);

        String originalFileName = file.getOriginalFilename();
        String storedFileName = createStoredFileName(originalFileName);

        FileUtils.upload(file, getFullPath(storedFileName));

        return AttachUploadFile.of(originalFileName, storedFileName);
    }

    /**
     * DownloadFile 객체를 사용하여 파일을 다운로드합니다.
     *
     * @param downloadFile 다운로드할 파일 정보를 포함한 DownloadFile 객체
     * @param <T>          응답 바디 타입
     * @return ResponseEntity 객체로 감싼 다운로드 결과
     */
    default <T> ResponseEntity<T> download(DownloadFile<T> downloadFile) {
        return ResponseEntity.ok().headers(downloadFile.getHeaders()).body(downloadFile.getBody());
    }
}
```

파일을 관리하는 인터페이스로, 저장 디렉토리를 가져오는 `getDirectory()`를 필수로 구현해야한다. 업로드와 다운로드 기능을 기본 제공한다. 

```java
/**
 * UploadFile 인터페이스는 업로드된 파일의 원본 파일명과 저장된 파일명을 제공하는 메서드를 정의합니다.
 */
public interface UploadFile {

    /**
     * 업로드된 파일의 원본 파일명을 반환합니다.
     *
     * @return 원본 파일명
     */
    String getOriginalFileName();

    /**
     * 업로드된 파일의 저장된 파일명을 반환합니다.
     *
     * @return 저장된 파일명
     */
    String getStoredFileName();
}
```

업로드 파일 인터페이스로, 인터페이스 구현체로 `AttachUploadFile`와 `ExcelUploadFile<T>`는 각각 첨부파일, 엑셀파일 업로드 클래스이다. 업로드한 정보로 `DB`에서 관리될 수 있다. 

```java
/**
 * DownloadFile 인터페이스는 파일 다운로드 정보를 정의하는 인터페이스입니다.
 * 구현 클래스에서는 다운로드할 파일명, HTTP 헤더, 응답 바디를 제공해야 합니다.
 *
 * @param <T> 다운로드할 데이터의 타입
 */
public interface DownloadFile<T> {

    /**
     * 다운로드할 파일명을 반환합니다.
     *
     * @return 다운로드할 파일명
     */
    String getDownloadFileName();

    /**
     * 다운로드할 파일에 대한 HTTP 헤더를 반환합니다.
     *
     * @return HTTP 헤더 객체
     */
    HttpHeaders getHeaders();

    /**
     * 다운로드할 데이터의 본문을 반환합니다.
     *
     * @return 다운로드할 데이터의 본문
     */
    T getBody();
}
```

다운로드 파일 인터페이스로, 인터페이스 구현체로 `AttachDownloadFile`와 `ExcelDownloadFile`는 각각 첨부파일, 엑셀파일 다운로드 클래스이다.

구현 클래스를 인스턴스하여 `FileManager`의 `download` 메소드를 호출하면 응답 값으로 파일 다운로드가 실행된다.  

```java
/**
 * ExcelWriter 인터페이스는 Excel 파일 작성을 위한 기능을 정의합니다.
 * <p>
 * 구현체는 Excel 파일의 헤더 스타일, 헤더 이름 및 값을 작성하기 위한 메서드를 포함합니다.
 * </p>
 *
 * @param <T> Excel 파일에 작성할 객체의 타입
 */
public interface ExcelWriter<T> {

    /**
     * Excel 파일에 작성할 데이터 리스트를 반환합니다.
     *
     * @return Excel 파일에 작성할 데이터 리스트
     */
    List<?> getList();

    /**
     * Excel 파일에 작성할 객체의 클래스 타입을 반환합니다.
     *
     * @return Excel 파일에 작성할 객체의 클래스 타입
     */
    Class<T> getType();
    
    ...(중략)
}
```

엑셀 다운로드 시, `List<?>`의 데이터를 엑셀로 쓰는 인터페이스이다. 기본 구현 클래스로 `DefaultExcelWriter`제공

**파일 유틸 클래스**
- `FileUtils` : 기본 첨부 파일 유틸 클래스 
- `ExcelFileUtils` : 엑셀 파일 관련 유틸 클레스 

### 로깅 

`io.bareun.base.log`

**`@RestController`에 대한 HTTP 로깅을 공통으로 제공한다.**

```java
/**
 * ApiLoggingAspect는 REST 컨트롤러의 HTTP 요청을 로깅하기 위한 Aspect입니다.
 * <p>
 * 이 클래스는 @RestController 어노테이션이 붙은 클래스 내에서 메서드 호출 전에 HTTP 요청을 로깅합니다.
 * 로깅할 정보로는 IP 주소, HTTP 메서드, 요청 URL, 쿼리 스트링, 요청 바디가 포함됩니다.
 */
@Slf4j
@Aspect
@Component
public class ApiLoggingAspect {

    /**
     * {@link org.springframework.web.bind.annotation.RestController} 어노테이션이 붙은 클래스
     * 내의 모든 메서드를 포인트컷으로 설정합니다.
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restController() {
    }

    /**
     * 포인트컷에서 지정한 메서드 호출 전에 HTTP 요청을 로깅하는 메서드입니다.
     *
     * @param joinPoint 조인 포인트 객체로, 호출된 메서드와 그 파라미터 등을 추출하는 데 사용됩니다.
     */
    @Before("restController()")
    public void httpLogging(JoinPoint joinPoint) {
        String remoteAddr = getRemoteAddr();
        String method = getMethod();
        String requestURL = getRequestURL();
        String queryString = getQueryString();

        String requestBody = getRequestBody(joinPoint);

        log.info("HTTP Logging IP : {} | Method : {} | URL : {} | Query : {} | Body {}",
                remoteAddr, method, requestURL, queryString, requestBody);
    }
    
    ...(중략)
}
```

`AOP`를 이용하여 `HTTP`의 정보를 로깅한다.

[로그 포맷] 
```text
HTTP Logging IP : {} | Method : {} | URL : {} | Query : {} | Body {}
```

## 샘플 프로젝트 

https://github.com/bareunio/project-sample

## 이슈 

**이슈 및 개선사항은 아래의 링크를 통해 등록 해주세요** 

https://github.com/bareunio/project-base/issues