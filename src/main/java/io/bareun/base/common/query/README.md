# BaseQuery

## Bean 생성 

```java
@Bean
public BaseQuery baseQuery(SqlSession sqlSession) {
    return new BaseQuery(sqlSession);
}
```