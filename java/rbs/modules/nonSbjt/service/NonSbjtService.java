package rbs.modules.nonSbjt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 비교과에 관한 인터페이스클래스를 정의한다.
 * @author 이동근
 *
 */
public interface NonSbjtService {	
	
	/**
	 * 최초 default 비교과 조회
	 * @return List<Object>
	 */
	public List<Object> getInitNonSbjtList(Map<String, Object> param) throws Exception;
	
	/**
	 * 최초 default 비교과 총 건 수
	 * @param param 
	 * @return int
	 */
	public DataMap getInitNonSbjtListCount(Map<String, Object> param) throws Exception;
	
	/**
	 * 비교과 ELSearch 결과의 키값으로 표시할 리스트(카드) 정보
	 * @param idList
	 * @return
	 */
	public List<Object> getNonSbjtList(Map<String, Object> param) throws Exception;	

	/**
	 * 비교과 핵심역량 분류
	 * @param parent
	 * @return
	 */
	public List<Object> getCategory(Map<String, Object> param) throws Exception;
	
	/**
	 * 비교과 전체 태그
	 * @return
	 */
	public List<Object> getTag() throws Exception;
	
	/**
	 * 비교과 프로그램 상세조회
	 * @param idx
	 * @param tidx
	 * @return
	 */
	public DataMap getNonsbjtInfo(Map<String, Object> param) throws Exception;

	/**
	 * 비교과 프로그램 태그
	 * @return idx
	 */
	public List<Object> getNonSbjtTag(Map<String, Object> param) throws Exception;

	/**
	 * 비교과 프로그램 핵심역량 지수
	 * @return idx
	 */
	public List<Object> getNonSbjtEssential(Map<String, Object> param) throws Exception;

	/**
	 * 비교과 프로그램 첨부파일
	 * @return filesList
	 */
	public List<Object> getNonSbjtAttachmentFile(Map<String, Object> param) throws Exception;

	/**
	 * 비교과 프로그램이력 조회
	 * @param idx
	 * @return
	 */
	public List<Object> getNonSbjtHist(Map<String, Object> param) throws Exception;

	public List<Object> getMyNonSbjtSigninHist(Map<String, Object> param)  throws Exception;
	
	/**
	 * 비교과 교육형식
	 * @return
	 */
	public List<Object> getProgramType() throws Exception;

	/**
	 * 비교과 운영방식
	 * @return
	 */
	public List<Object> getMethod() throws Exception;

	


	

	



	}