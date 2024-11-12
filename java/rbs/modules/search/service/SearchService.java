package rbs.modules.search.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 통합검색 모듈에 관한 인터페이스 클래스를 정의한다.
 * @author 석수빈, 이동근
 *
 */
public interface SearchService {

	/**
	 * 만족도 조사 insert
	 * @param reqJsonObj
	 * @param param
	 * @return
	 */
	public int insertPoint(JSONObject reqJsonObj, HttpServletRequest request);

	/**
	 * 검색 로그
	 * @param endpoint
	 * @param reqJsonObj
	 * @param result
	 * @return
	 */
	public int insertSearchLog(String endpoint, JSONObject reqJsonObj, String response, HttpServletRequest request);
}