package rbs.modules.studPlan.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.woowonsoft.egovframework.form.DataMap;

import net.sf.json.JSONObject;


/**
 * 학생설계융합전공 service
 * @author 이동근
 *
 */
public interface StudPlanAdmService {
	
	/**
	 * 승인
	 * @param rawJsonObj
	 * @param request
	 * @return
	 */
	public int judgApprove(JSONObject rawJsonObj, HttpServletRequest request) throws Exception;
	/**
	 * 반려
	 * @param rawJsonObj
	 * @param request
	 * @return
	 */
	public int judgReject(JSONObject rawJsonObj, HttpServletRequest request) throws Exception;
	

	/**
	 * 창의융합교육센터 심의할 학생설계전공 조회
	 * @param param
	 * @return
	 */
	public int getEduCenterJudgListCount(Map<String, Object> param) throws Exception;
	public List<Object> getEduCenterJudgList(Map<String, Object> param) throws Exception;
	
	/**
	 * 학사과 심의할 학생설계전공 조회
	 * @param param
	 * @return
	 */
	public int getDepartJudgListCount(Map<String, Object> param) throws Exception;
	public List<Object> getDepartJudgList(Map<String, Object> param) throws Exception;
	
	/**
	 * 교무회 심의할 학생설계전공 조회
	 * @param param
	 * @return
	 */
	public int getAffairCommitteeJudgListCount(Map<String, Object> param) throws Exception;
	public List<Object> getAffairCommitteeJudgList(Map<String, Object> param) throws Exception;
	
	/**
	 * 최종 수정보완할 학생설계전공 조회
	 * @param param
	 * @return
	 */
	public int getLastSupplementListCount(Map<String, Object> param) throws Exception;
	public List<Object> getLastSupplementList(Map<String, Object> param) throws Exception;
	
	/**
	 * 수정보완 저장
	 * @param param
	 * @return
	 */
	public int lastSupplementSave(JSONObject rawJsonObj, HttpServletRequest request) throws Exception;
	/**
	 * 수정보완 마감
	 * @param param
	 * @return
	 */
	public int lastSupplementComplete(JSONObject rawJsonObj, HttpServletRequest request) throws Exception;
	/**
	 * 종합정보 시스템 연계(데이터 전송)
	 * @param param
	 * @return
	 */
	public int sendStudPlanToAHS010TB(JSONObject rawJsonObj) throws Exception;

	

}