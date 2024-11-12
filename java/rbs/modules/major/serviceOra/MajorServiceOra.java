package rbs.modules.major.serviceOra;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 샘플모듈에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
public interface MajorServiceOra {

	/**
	 * 추가 생성 전공 조회
	 * @return List<Object>
	 */
	public List<Map<String, Object>> getNewMajorList() throws Exception;
		
	/**
	 * 교과목 - 상세조회
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	public DataMap getView(Map<String, Object> param) throws Exception;
	
	/**
	 * 교과목 상세-직업/직무
	 * @param docId
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getJobCd(String docId) throws Exception;
	
	/**
	 * 교과목 상세-해시태그
	 * @param docId
	 * @param param
	 * @return
	 */
	public Map<String, Object> getPlanInfo(String docId) throws Exception;
	
	/**
	 * 나노디그리&매트릭스 상단정보
	 * @param 교과목코드
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getTrackList(Map<String, Object> param) throws Exception;
	
	/**
	 * 개설강좌-강의실 정보
	 * @param docId
	 * @param param
	 * @return
	 */
	public List<Object> getRoomInfo(List<Object> arr) throws Exception;
	
	
	/**
	 * 개설강좌-강의계획서
	 * @param docId
	 * @param param
	 * @return
	 */
	public Map<String, Object> getLessonPlan(String docId) throws Exception;
	
	/**
	 * 수강생통계
	 * @param 학번
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getStatList(Map<String, Object> param) throws Exception;

}