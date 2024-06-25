### Javadoc 작성 방법 요약

1. **클래스 Javadoc**
   ```java
   /**
    * 클래스에 대한 간결한 설명.
    * 
    * <p>상세한 설명 및 사용 예시 등을 포함할 수 있습니다.</p>
    */
   public class ClassName {
       // 클래스 내용
   }
   ```

2. **메서드 Javadoc**
   ```java
   /**
    * 메서드의 기능을 설명하는 주석.
    *
    * @param paramName 매개변수에 대한 설명
    * @return 반환값에 대한 설명
    * @throws Exception 예외 상황에 대한 설명
    */
   public ReturnType methodName(Type paramName) throws Exception {
       // 메서드 내용
   }
   ```

3. **필드 Javadoc**
   ```java
   /**
    * 필드에 대한 설명.
    */
   private Type fieldName;
   ```

4. **Javadoc 태그**
   - `@param`: 메서드의 매개변수 설명
   - `@return`: 메서드의 반환값 설명
   - `@throws` 또는 `@exception`: 메서드가 발생시킬 수 있는 예외 설명
   - `@see`: 다른 클래스나 메서드와의 관계를 명시
   - `@deprecated`: 사용하지 말아야 할 deprecated된 메서드나 클래스 표시
   - `@since`: 해당 요소가 도입된 버전 명시

5. **HTML 태그 사용**
   Javadoc 주석 내에서 HTML 태그를 사용하여 더 다양한 포맷과 내용을 표현할 수 있습니다. 예를 들어 `<p>`, `<ul>`, `<li>` 등을 사용하여 문단 나누기, 목록 생성 등을 할 수 있습니다.

6. **Javadoc 생성**
   Javadoc 주석이 작성된 소스 코드는 `javadoc` 명령어를 사용하여 HTML 포맷의 문서로 변환할 수 있습니다. 이를 통해 API 문서를 생성하고 공유할 수 있습니다.

### Javadoc 작성하기 (IntelliJ IDEA)

1. **메서드, 클래스, 필드에 Javadoc 추가**
   - 메서드나 클래스 선언 위에 커서를 위치시킨 후 `/**`와 Enter 키를 누르면 Javadoc 템플릿이 자동으로 생성됩니다.
   - 이 후에 나오는 각 줄에 필요한 설명을 추가하고, `@param`, `@return`, `@throws` 등의 태그를 사용하여 세부 정보를 기술할 수 있습니다.
   - `Alt` + `Enter` 를 활용한 주석 생성도 가능합니다. 

2. **HTML 사용**
   - Javadoc 주석 내에서 HTML 태그를 사용하여 본문을 포맷팅할 수 있습니다. 예를 들어 `<p>`, `<ul>`, `<li>` 등을 이용하여 문단을 구분하거나 목록을 작성할 수 있습니다.

### Javadoc 보기

1. **Javadoc 뷰어**
   - IntelliJ IDEA는 코드 내에서 Javadoc을 바로 보여주는 기능을 제공합니다. 메서드나 클래스 이름 위에 마우스를 올리면 Javadoc 설명이 툴팁 형태로 표시됩니다.

2. **외부 브라우저로 열기**
   - Javadoc을 HTML 문서로 변환하여 외부 브라우저에서 볼 수도 있습니다.
   - 프로젝트 폴더에서 `javadoc` 명령어를 사용하거나 IntelliJ IDEA에서 제공하는 Javadoc 생성 기능을 이용하여 HTML 문서를 생성할 수 있습니다.

### IntelliJ IDEA에서 Javadoc 생성하기

1. **프로젝트 설정**
   - IntelliJ IDEA에서는 프로젝트의 메뉴에서 `Tools` -> `Generate Javadoc...`을 선택하여 Javadoc을 생성할 수 있습니다.
   - 필요한 옵션을 설정한 후 `OK`를 클릭하면 Javadoc이 생성됩니다.

2. **Javadoc 패널**
   - IntelliJ IDEA의 하단에 있는 `Structure` 패널에서 `Javadoc` 탭을 선택하여 프로젝트의 Javadoc 문서를 볼 수 있습니다.
   - 이를 통해 프로젝트의 API 문서를 편리하게 확인할 수 있습니다.

