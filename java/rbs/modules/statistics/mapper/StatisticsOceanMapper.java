package rbs.modules.statistics.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.OceanAbstractMapper;

/**
 * 통계모듈에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("statisticsOceanMapper")
public class StatisticsOceanMapper extends OceanAbstractMapper{
	
	/**
	 * 찜 현황 > 비교과이름 가져오기
	 * 조회
	 */
	public Map<String, Object> getNonSbjtName(Map<String, Object> oraParam) {
		return selectOne("rbs.modules.statistics.statisticsOceanMapper.getNonSbjtName", oraParam);
	}
	    
}