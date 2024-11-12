package rbs.modules.log.service;

import java.util.List;
import java.util.Map;

import com.woowonsoft.egovframework.form.DataMap;


/**
 * 관리 로그에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
public interface MngLogService {

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
	
	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	public DataMap getView(Map<String, Object> param);

	/**
	 * 등록
	 * @param logType		로그구분(1:등록,2:수정,3:삭제,4:복원,5:완전삭제, 9:적용)
	 * @param confSModule	내부모듈경로
	 * @param preKeyCode	keyCode앞에 추가할 key값
	 * @param keyCodes		key값
	 * @return
	 * @throws Exception
	 */
	public int insert(String confSModule, String preKeyCode, int logType, Object ... keyCodes) throws Exception;

	/**
	 * 등록
	 * @param logType		로그구분(1:등록,2:수정,3:삭제,4:복원,5:완전삭제, 9:적용)
	 * @param confSModule	내부모듈경로
	 * @param keyCodes		key값
	 * @return
	 * @throws Exception
	 */
	public int insert(String confSModule,  int logType, Object ... keyCodes) throws Exception;
}