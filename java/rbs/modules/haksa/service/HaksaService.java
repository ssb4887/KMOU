package rbs.modules.haksa.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 교수에 관한 기능 인터페이스클래스를 정의한다.
 * @author user
 *
 */
public interface HaksaService {
	
	/**
	 * 관심교과목 북마크 등록 리스트
	 * @param docId
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getHaksaBk(List<Object> arr) throws Exception;
	
	/**
	 *  관심교과목 북마크 등록 여부
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public int bkHaksaCount(Map<String, Object> param) throws Exception;
	
	/**
	 * 등록처리 : 관심교과목 북마크 등록
	 * @return
	 * @throws Exception
	 */
	public int insertBkHaksa(Map<String, Object> param, HttpServletRequest request) throws Exception;
	
	/**
	 * 등록처리 : 관심교과목 북마크 업데이트
	 * @return
	 * @throws Exception
	 */
	public int updateBkHaksa(Map<String, Object> param, HttpServletRequest request) throws Exception;

	
}