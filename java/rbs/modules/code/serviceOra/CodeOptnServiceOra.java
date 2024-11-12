package rbs.modules.code.serviceOra;

import java.util.List;
import java.util.Map;

/**
 * 기관 정보 관리에 관한 인터페이스 클래스를 정의한다.
 * @author user
 *
 */
public interface CodeOptnServiceOra {
	/**
	 * 학사 정보 유틸
	 * @param param
	 * @return
	 */
	public List<Object> getHaksaCode(String upCode) throws Exception;
	public List<Object> getHaksaPartCode(String upCode) throws Exception;
	public List<Object> getHaksaAllCode(Map<String, Object> param) throws Exception;
	public List<Object> getHaksaAllContrCode(Map<String, Object> param) throws Exception;
	public List<Object> getHaksaAllColgCode(Map<String, Object> param) throws Exception;
	public List<Object> getHaksaAllContrColgCode(Map<String, Object> param) throws Exception;
	public List<Object> getHaksaAllClsfCode(Map<String, Object> param) throws Exception;
	public List<Object> getHaksaAllEtcCode(Map<String, Object> param) throws Exception;
	
	/**
	 * 대학 조회
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<Object> getDeptList(Map<String, Object> param) throws Exception;
	
	/**
	 * 학과 조회
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<Object> getMajorList(Map<String, Object> param) throws Exception;
	
	/**
	 * 이수 영역 유틸
	 * @param param
	 * @return
	 */
	public List<Object> getCompleteCode(Map<String, Object> param) throws Exception;
	
	/**
	 * 교과목 영역 유틸
	 * @param param
	 * @return
	 */
	public List<Object> getSubjectCode(Map<String, Object> param) throws Exception;
	
	/**
	 * 소속 분류 : 대학, 계약학과
	 * @param param
	 * @return
	 */
	public List<Object> getContractGbnCode(String upCode) throws Exception;

	/**
	 * 소속 분류 : 대학 > 단과대학
	 * @param param
	 * @return
	 */
	public List<Object> getCollegeCode(String upCode) throws Exception;

	/**
	 * 학과(부) : 대학 > 단과대학 > 학과/전공
	 * @param param
	 * @return
	 */
	public List<Object> getDepartmentCode(String upCode) throws Exception;

	/**
	 * 소속 분류 : 계약학과 > 단과대학
	 * @param param
	 * @return
	 */
	public List<Object> getContractCollegeCode(String upCode) throws Exception;

	/**
	 * 학과(부) : 계약학과 > 단과대학 > 학과/전공
	 * @param param
	 * @return
	 */
	public List<Object> getContractDepartCode(String upCode) throws Exception;

	/**
	 * 이수 구분 ex) 전공 필수, 교양 필수
	 * @param param
	 * @return
	 */
	public List<Object> getCptnCode() throws Exception;

	/**
	 * 과정 구분 ex) 학사, 석사
	 * @param param
	 * @return
	 */
	public List<Object> getCorsCode() throws Exception;

	/**
	 * 핵심 역량 ex) 지식 탐구, 의사소통
	 * @param param
	 * @return
	 */
	public List<Object> get6CoreAbtyCode() throws Exception;

	/**
	 * 연계 전공 ex) 나노디그리, 매트릭스
	 * @param param
	 * @return
	 */
	public List<Object> getTrackCode() throws Exception;
	
	/**
	 * 학년 구분
	 * @param param
	 * @return
	 */
	public List<Object> getHySeqGbn() throws Exception;

	/**
	 * 학기 구분
	 * @param param
	 * @return
	 */
	public List<Object> getTmGbnCode() throws Exception;
}