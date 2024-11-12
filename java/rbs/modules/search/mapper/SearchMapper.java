package rbs.modules.search.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.RbsAbstractMapper;

/**
 * 통합검색 모듈에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("searchMapper")
public class SearchMapper extends RbsAbstractMapper {

	/**
	 * 만족도 insert
	 * @param param
	 * @return
	 */
	public int insertPoint(Map<String, Object> param) {
		return insert("rbs.modules.search.searchMapper.insertPoint", param);
	}

	public int insertSearchLog(Map<String, Object> param) {
		return insert("rbs.modules.search.searchMapper.insertSearchLog", param);
	}
}