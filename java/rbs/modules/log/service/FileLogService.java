package rbs.modules.log.service;

import java.util.List;
import java.util.Map;


/**
 * 관리 로그에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
public interface FileLogService {

	/**
	 * 전체 목록 수
	 * @param param
	 * @return
	 */
	public int getTotalCount(Map<String, Object> param);
	
	/**
	 * 전체 목록
	 * @param param
	 * @return
	 */
	public List<Object> getList(Map<String, Object> param);
}