package rbs.usr.main.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rbs.egovframework.LoginVO;

public interface MainService {
	public List<Object> getModuleList(String moduleId, int fnIdx, int limitNumber, String itemMasterCode);
	public Map<Object, List<Object>> getBoardListHashMap(DataForm queryString, int lastIndex, JSONArray menuArray, JSONObject boardTotalSetting);
	public Map<Object, List<Object>> getContentsListHashMap(DataForm queryString, JSONArray menuArray);
	
	/**
	 * 교육과정
	 * @param param
	 * @return
	 */
	public DataMap getMyCurriculum(Map<String, Object> param);
	
	/**
	 * 학생 기본정보
	 * @param param
	 * @return
	 */
	public DataMap getMyInfomation(Map<String, Object> param);
	
	/**
	 * 졸업기준학점(학과별/전공별)
	 * @param param
	 * @return
	 */
	public List<Object> getMyGoalCDT(Map<String, Object> param);
	
	/**
	 * 취득 학점
	 * @param param
	 * @return
	 */
	public DataMap getMyCDT(Map<String, Object> param);
	
	/**
	 * 평균 성적
	 * @param param
	 * @return
	 */
	public List<Object> getMyGPA(Map<String, Object> param);
	
	/**
	 * 학점상세
	 * @param param
	 * @return
	 */
	public List<Object> getMyCdtDetail(Map<String, Object> param);
	
	/**
	 * 장학 내역
	 * @param param
	 * @return
	 */
	public List<Object> getMyScholAmt(Map<String, Object> param);
	
	/**
	 * 전공필수 이수현황
	 * @param param
	 * @return
	 */
	public List<Object> getMyMajorReq(Map<String, Object> param);
	
	/**
	 * 교양필수 이수현황
	 * @param param
	 * @return
	 */
	public List<Object> getMyMinorReq(Map<String, Object> param);
	
	/**
	 * 교육과정 내 교양교과목 
	 * @param param
	 * @return
	 */
	public List<Object> getMyCurrMinor(Map<String, Object> param);
	
	/**
	 * 교과목 추천
	 * @param param
	 * @return
	 */
	public List<Object> getAiMajorSubject(List<Map<String, Object>> paramsList);
	
	/**
	 * 비교과 추천
	 * @param param
	 * @return
	 */
	public List<Object> getAiNonCourse(List<Map<String, Object>> paramsList);
	
	/**
	 * 핵심역량
	 * @param param
	 * @return
	 */
	public List<Object> getCoreCompDiagnosis(Map<String, Object> param);
	
	/**
	 * 전공능력
	 * @param param
	 * @return
	 */
	public List<Object> getMajorCompDiagnosis(Map<String, Object> param);
	
	/**
	 * 학사일정
	 * @param param
	 * @return
	 */
	public List<Object> getAcademicCalendar(Map<String, Object> param);
	
    /**
	 * 해양대학교 종합정보시스템 로그인 처리
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
    public String loginTis(String siteMode, String regiIp, ParamForm parameterMap, JSONObject itemInfo, JSONObject settingInfo, JSONObject memberItemInfo) throws Exception;
    
    /**
	 * 해양대학교 종합정보시스템 회원 정보 조회(VO세팅)
	 * @param parameterMap
     * @param request 
	 * @return
	 * @throws Exception
	 */
	public LoginVO setUser(ParamForm parameterMap, HttpServletRequest request) throws Exception;
	
	/**
	 * 취업률
	 * @return
	 */
	public List<Object> getEmploymentRate() throws Exception;
	
	/**
	 * 정보제공동의 여부 체크
	 * @param memberId
	 * @return
	 */
	public String getIsPrivacyPolicy(String memberId) throws Exception;
	
	/**
	 * 정보제공동의
	 * @param memberId
	 * @return
	 */		
	public int insertPrivacyPolicy() throws Exception;
	
}