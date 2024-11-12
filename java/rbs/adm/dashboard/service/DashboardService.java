package rbs.adm.dashboard.service;

import java.util.List;
import java.util.Map;

import com.woowonsoft.egovframework.form.DataMap;


public interface DashboardService {
	

	/**
	 * 1번 차트 (검색 건수 추이)
	 * 조회
	 */
	public List<Object> selectChart1Data(Map<String, Object> param);

	/**
	 * 2번 차트 (검색어 통계)
	 * 조회
	 */
	public List<Object> selectChart2Data(Map<String, Object> param);

	/**
	 * 3번 차트 (로그인 통계)
	 * 조회
	 */
	public List<Object> selectChart3Data(Map<String, Object> param);

	/**
	 * 4번 차트 (메뉴별 접속자 수)
	 * 조회
	 */
	public List<Object> selectChart4Data(Map<String, Object> param);

	/**
	 * 5번 차트 (찜현황 통계)
	 * 조회
	 */
	public List<Object> selectChart5Data(Map<String, Object> param);

	/**
	 * 6번 차트 (해시태그 키워트 통계)
	 * 조회
	 */
	public List<Object> selectChart6Data(Map<String, Object> param);
}