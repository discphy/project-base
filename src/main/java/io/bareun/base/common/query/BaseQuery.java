package io.bareun.base.common.query;

import io.bareun.base.common.dto.map.BaseMap;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@ConditionalOnBean(SqlSession.class)
public class BaseQuery {

    private final SqlSession sqlSession;

    public int selectCount(String id) {
        return sqlSession.selectOne(id);
    }

    public int selectCount(String id, BaseMap parameters) {
        return sqlSession.selectOne(id, parameters);
    }

    public BaseMap selectOne(String id) {
        return sqlSession.selectOne(id);
    }

    public BaseMap selectOne(String id, BaseMap parameters) {
        return sqlSession.selectOne(id, parameters);
    }

    public List<BaseMap> selectList(String id) {
        return sqlSession.selectList(id);
    }

    public List<BaseMap> selectList(String id, BaseMap parameters) {
        return sqlSession.selectList(id, parameters);
    }

    public int insert(String id) {
        return sqlSession.insert(id);
    }

    public int insert(String id, BaseMap parameters) {
        return sqlSession.insert(id, parameters);
    }

    public int update(String id) {
        return sqlSession.update(id);
    }

    public int update(String id, BaseMap parameters) {
        return sqlSession.update(id, parameters);
    }

    public int delete(String id) {
        return sqlSession.delete(id);
    }

    public int delete(String id, BaseMap parameters) {
        return sqlSession.delete(id, parameters);
    }
}

