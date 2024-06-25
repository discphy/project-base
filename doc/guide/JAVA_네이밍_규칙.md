### 패키지 네이밍 규칙

```
├── java
│ └── io 
│     └── bareun [도메인 예시 : bareun.io]
│         └── sample [프로젝트 명]
│             ├── common [공통 패키지]
│             │ ├── code : 코드
│             │ └── util : 유틸
│             ├── config [설정 패키지]
│             │ ├── SecurityConfig.java
│             │ └── exception : 예외
│             │     └── handler : 핸들러
│             │         └── ApiExceptionHandler.java
│             └── domain [도메인 패키지 - 기본 구조 : controller / service / dao]
│                 ├── item : 상품(예시)
│                 │ ├── controller : 컨트롤러
│                 │ │ └── ItemController.java
│                 │ ├── dao : DAO 
│                 │ │ └── ItemDao.java
│                 │ └── service : 서비스
│                 │     └── ItemService.java
│                 └── user : 사용자(예시)
│                     ├── controller : 컨트롤러
│                     │ └── UserController.java
│                     ├── dao : DAO
│                     │ └── UserDao.java
│                     └── service : 서비스
│                         └── UserService.java
└── resources
    ├── application.yml
    ├── mappers [MyBatis 쿼리 XML]
    │ ├── ItemDao.xml
    │ └── UserDao.xml
    ├── static
    └── templates
```

1. **메인 패키지**
    - `common` : 공통
    - `config` : 설정
    - `domain` : 도메인

2. **패키지 네이밍 규칙**
    - 단어 한개로 구성
    - 소문자로 구성
    - 직관적인 네이밍
    - 축약형 지양

3. **도메인 주도 설계 DDD(Domain-Driven Design) 구조**
    - `domain` 패키지 밑에 업무 별 패키지 구성 (철저한 분리)
    - 핵심 비즈니스 로직을 순수하게 유지하고 확장성을 위함

### 파일 네이밍 규칙

1. **파일 네이밍 규칙**
    - 단어 + 단어 결합
    - `CamelCase`로 네이밍
    - 직관적인 네이밍
    - 축약형 지양

2. **패키지 네이밍과 결합한 파일 네이밍** : 주요기능 및 도메인과 패키지명을 결합한 파일 네이밍
    - `ObjectMapperUtils.java` : `ObjectMapper`(주요기능) + `util` (`*.common.util`의 `util`)
    - `ApiExceptionHandler` : `API`(주요기능) + `ExceptionHandler` (`*.config.util`의 `exceptionHandler`)
    - `ItemService` : `Item`(도메인) + `Service` (`*.domain.item.service`의 `service`)

### 메서드 네이밍 규칙 

1. **메서드 기본 네이밍 규칙**
   - 단어 + 단어 결합
   - `CamelCase`로 네이밍
   - 직관적인 네이밍
   - 축약형 지양

2. **메서드 네이밍 규칙 - `controller` / `service`**   
   - `CRUD` 기준으로 작성 (단순 도메인)
     - C : `create`
     - R : `readAll` / `read`
     - U : `update`
     - D : `delete`
   - 참고 : [ItemController.java](..%2F..%2Fsrc%2Fmain%2Fjava%2Fio%2Fbareun%2Fsample%2Fdomain%2Fitem%2Fcontroller%2FItemController.java)
   - 복잡한 비지니스 로직인 경우, `CRUD`를 활용하여 네이밍한다.
     - C : `admin` 도메인에서 권한을 생성 시, `createAuthority` (`AdminController.java`)
     - R : `user` 도메인에서 사용자명 조회 시, `readUsername` (`UserController.java`)
     - U : `item` 도메인에서 재고수량 변경 시, `updateStockCount` (`ItemController.java`)
     - D : `order` 도메인에서 상품 ID로 삭제 시, `deleteByItemId` (`OrderController.java`)

3. **메서드 네이밍 규칙 - `dao`**

    메서드 이름은 "키워드 + 속성" 패턴을 따르며, 키워드는 쿼리 유형을 나타내고, 속성은 엔티티의 속성을 나타냅니다.
   
    [참고] [JPA 쿼리 메소드](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html)

    #### 기본 키워드
    
    1. **찾기(조회)**
       - `find...By` : 조회
       - `read...By` : 읽기
       - `query...By` : 조회
       - `get...By` : 가져오기
    
    2. **카운트**
       - `count...By` : 갯수 세기
    
    3. **삭제**
       - `delete...By` : 삭제
       - `remove...By` : 제거
    
    4. **존재 여부 확인**
       - `exists...By` : 존재 여부 확인
    
    #### 조건 키워드
    
    조건 키워드는 메서드 이름에 추가되어 조건을 나타냅니다. 예를 들어, `findByName`은 이름으로 조회하는 메서드입니다.
    
    - `And` : 그리고 (예: `findByLastnameAndFirstname`)
    - `Or` : 또는 (예: `findByLastnameOrFirstname`)
    - `Is` : 같다 (예: `findByNameIs`, `findByName`)
    - `Equals` : 같다 (예: `findByNameEquals`)
    - `Between` : 사이 (예: `findByStartDateBetween`)
    - `LessThan` : 보다 작다 (예: `findByAgeLessThan`)
    - `GreaterThan` : 보다 크다 (예: `findByAgeGreaterThan`)
    - `Like` : 유사하다 (예: `findByFirstnameLike`)
    - `NotLike` : 유사하지 않다 (예: `findByFirstnameNotLike`)
    - `In` : 포함된다 (예: `findByAgeIn(Collection<Integer> ages)`)
    - `NotIn` : 포함되지 않는다 (예: `findByAgeNotIn(Collection<Integer> ages)`)
    - `True` : 참이다 (예: `findByActiveTrue`)
    - `False` : 거짓이다 (예: `findByActiveFalse`)
    - `OrderBy` : 정렬 (예: `findByLastnameOrderByFirstnameAsc`)
    
    #### 예시
    
    다음은 `User` 엔티티에 대한 다양한 쿼리 메서드 예시입니다.
    
    ```java
    
    public interface UserRepository extends JpaRepository<User, Long> {
        // 이름으로 사용자 찾기
        User findByName(String name);
    
        // 이름이 포함된 사용자 찾기
        List<User> findByNameContaining(String name);
    
        // 나이가 특정 값 이하인 사용자 찾기
        List<User> findByAgeLessThan(int age);
    
        // 이메일로 사용자 찾기
        User findByEmail(String email);
    
        // 활성화된 사용자 찾기
        List<User> findByActiveTrue();
    
        // 이름으로 사용자 존재 여부 확인
        boolean existsByName(String name);
    
        // 이름과 이메일로 사용자 찾기
        User findByNameAndEmail(String name, String email);
    
        // 나이로 사용자 삭제
        void deleteByAge(int age);
    
        // 나이가 특정 범위 내에 있는 사용자 수
        long countByAgeBetween(int startAge, int endAge);
    
        // 이름으로 정렬하여 사용자 찾기
        List<User> findByOrderByNameAsc();
    }
    ```

### API URL 네이밍 규칙
    
REST API의 요청 매핑을 위한 가이드는 RESTful 원칙을 준수하면서 이해하기 쉽고 명확한 URL 구조를 만드는 데 도움이 됩니다. 다음은 REST API의 요청 매핑을 설계할 때 고려해야 할 주요 사항과 권장 사항입니다.

1. 자원(Resource) 명명

- **명사 사용**: 자원 이름은 명사로 작성합니다.
  - 예: `/user`, `/order`, `/product`
- **복수형 사용**: 자원 컬렉션을 나타낼 때는 복수형을 사용합니다.
  - 예: `/users`, `/orders`
- **하위 자원**: 하위 자원은 부모 자원의 ID를 포함합니다.
  - 예: `/users/{userId}/orders`, `/orders/{orderId}/items`

2. HTTP 메서드

- **GET**: 자원의 조회
  - 예: `GET /users`, `GET /users/{userId}`
- **POST**: 새로운 자원의 생성
  - 예: `POST /users`
- **PUT**: 기존 자원의 전체 업데이트
  - 예: `PUT /users/{userId}`
- **PATCH**: 기존 자원의 부분 업데이트
  - 예: `PATCH /users/{userId}`
- **DELETE**: 자원의 삭제
  - 예: `DELETE /users/{userId}`

3. 상태 코드

- **200 OK**: 요청이 성공적으로 처리됨
- **201 Created**: 새로운 자원이 성공적으로 생성됨
- **204 No Content**: 요청이 성공적으로 처리되었으나 반환할 콘텐츠가 없음
- **400 Bad Request**: 잘못된 요청
- **401 Unauthorized**: 인증 실패
- **403 Forbidden**: 권한 부족
- **404 Not Found**: 자원을 찾을 수 없음
- **500 Internal Server Error**: 서버 내부 오류

4. URL 설계

- **리소스 컬렉션**: `GET /users`
- **리소스 단일 항목**: `GET /users/{userId}`
- **리소스 하위 컬렉션**: `GET /users/{userId}/orders`
- **리소스 필터링**: `GET /users?status=active`
- **리소스 정렬**: `GET /users?sort=name,asc`
- **리소스 페이징**: `GET /users?page=2&size=10`

#### 5. 예시

다음은 `User` 자원에 대한 REST API 요청 매핑 예시입니다.

```http
# 모든 사용자 조회
GET /users

# 사용자 생성
POST /users
{
  "name": "John Doe",
  "email": "john.doe@example.com"
}

# 특정 사용자 조회
GET /users/{userId}

# 특정 사용자 전체 업데이트
PUT /users/{userId}
{
  "name": "John Smith",
  "email": "john.smith@example.com"
}

# 특정 사용자 부분 업데이트
PATCH /users/{userId}
{
  "email": "john.newemail@example.com"
}

# 특정 사용자 삭제
DELETE /users/{userId}
```
