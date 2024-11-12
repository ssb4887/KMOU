package rbs.adm.dashboard.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.woowonsoft.egovframework.form.DataMap;

import rbs.egovframework.mapper.RbsAbstractMapper;

/**
 * 다기능게시판에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("admDashboardMapper")
public class DashboardMapper extends RbsAbstractMapper{

	/**
	 * 1번 차트 (검색 건수 추이)
	 * 조회
	 */
	public List<Object> selectChart1Data(Map<String, Object> param){
        return (List<Object>) selectList("rbs.adm.dashboard.dashboardMapper.selectChart1Data", param);
    }

	/**
	 * 2번 차트 (검색어 통계)
	 * 조회
	 */
	public List<Object> selectChart2Data(Map<String, Object> param){
		return (List<Object>) selectList("rbs.adm.dashboard.dashboardMapper.selectChart2Data", param);
	}

	/**
	 * 3번 차트  (로그인 통계)
	 * 조회
	 */
	public List<Object> selectChart3Data(Map<String, Object> param){
        return (List<Object>) selectList("rbs.adm.dashboard.dashboardMapper.selectChart3Data", param);
    }

	/**
	 * 4번 차트(메뉴별 접속자 수)
	 * 조회
	 */
	public List<Object> selectChart4Data(Map<String, Object> param){
		return (List<Object>) selectList("rbs.adm.dashboard.dashboardMapper.selectChart4Data", param);
	}

	/**
	 * 5번 차트 (찜현황 통계)
	 * 조회
	 */
	public List<Object> selectChart5Data(Map<String, Object> param){
        return (List<Object>) selectList("rbs.adm.dashboard.dashboardMapper.selectChart5Data", param);
    }

	/**
	 * 6번 차트 (해시태그 키워트 통계)
	 * 조회
	 */
	public List<Object> selectChart6Data(Map<String, Object> param){
		return (List<Object>) selectList("rbs.adm.dashboard.dashboardMapper.selectChart6Data", param);
	}
}