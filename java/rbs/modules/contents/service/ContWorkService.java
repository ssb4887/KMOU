package rbs.modules.contents.service;

import java.util.List;
import java.util.Map;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 기관정보관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
public interface ContWorkService {

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	public DataMap getModify(String contentsType, String lang, Map<String, Object> param);
	public DataMap getApplyView(String contentsType, String lang, Map<String, Object> param);
	
	/**
	 * 수정
	 * @param param
	 * @return
	 */
	public int update(String lang, String contCd, int branchIdx, int versionIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject moduleItem) throws Exception;
	
	public int apply(String lang, String contCd, int branchIdx, int versionIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject moduleItem) throws Exception;

}