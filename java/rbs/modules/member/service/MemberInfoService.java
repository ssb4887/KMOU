package rbs.modules.member.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import net.sf.json.JSONObject;

import com.woowonsoft.egovframework.form.DataMap;

/**
 * 기관정보관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
public interface MemberInfoService {
	
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
	 * 학점
	 * @param param
	 * @return
	 */
	public DataMap getMyCDT(Map<String, Object> param);
	
	/**
	 * 성적
	 * @param param
	 * @return
	 */
	public List<Object> getMyGPA(Map<String, Object> param);
	
	/**
	 * 학점 상세
	 * @param param
	 * @return
	 */
	public List<Object> getMyCdtDetail(Map<String, Object> param);
	
	/**
	 * 소요학점 조회
	 * @param param
	 * @return
	 */
	public List<Object> getMyReqCDT(Map<String, Object> param);
	
	/**
	 * 누적성적 조회
	 * @param param
	 * @return
	 */
	public List<Object> getMyCumCDT(Map<String, Object> param);
	
	
	/**
	 * 졸업인증자격 조회
	 * @param param
	 * @return
	 */
	public List<Object> getMyGradReq(Map<String, Object> param);
	
	/**
	 * 과목별성적 조회
	 * @param param
	 * @return
	 */
	public List<Object> getMySubjectCDT(Map<String, Object> param);
	
	/**
	 * 전공필수 이수현황
	 * @param param
	 * @return
	 */
	public List<Object> getMyMajorReq(Map<String, Object> param);
	
	/**
	 * 교양필수 이수현황 체크(교양교육원 교양교육이수체계 적용 관리 테이블의 row수)
	 * @param param
	 * @return int
	 */
	public int getChkMinorReq(Map<String, Object> param);
	/**
	 * 교양필수 이수현황
	 * @param param
	 * @return
	 */
	public List<Object> getMyMinorReq(Map<String, Object> param);
	
	/**
	 * 학적변경내역
	 * @param param
	 * @return
	 */
	public List<Object> getMyRecordHistory(Map<String, Object> param);
	
	/**
	 * 해시태그 조회
	 * @param param
	 * @return
	 */
	public List<Object> getMyHashtag(Map<String, Object> param);
	
	/**
     * 해시태그 등록
     * @param param
     * @return
     * @throws Exception
     */
	public int hashtagInsert(Map<String, Object> param) throws Exception;
	
	/**
     * 해시태그 삭제
     * @param param
     * @return
     * @throws Exception
     */
	public int hashtagDelete(Map<String, Object> param) throws Exception;


}