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
 * [관리자페이지]교수에 관한 기능 인터페이스클래스를 정의한다.
 * @author LDG
 *
 */
public interface HaksaAdmService {
	
	/**
	 * 교수정보 리스트(count)
	 * @param param
	 * @return
	 */
	public int getTotalCount(Map<String, Object> param) throws Exception;

	/**
	 * 교수정보 리스트
	 * @param param
	 * @return
	 */
	public List<?> getList(Map<String, Object> param) throws Exception;

	
	/**
	 * 교수 상세정보(수정용)
	 * @param param
	 * @return
	 */
	public DataMap getModify(int fnIdx, Map<String, Object> param) throws Exception;

	/**
	 * 교수 상세정보 update
	 * @param param
	 * @return
	 */
	public int update(String uploadModulePath, int fnIdx, Map<String, Object> param, String remoteAddr, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;
	
}