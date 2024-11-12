package rbs.modules.adminAuth.service;

import com.woowonsoft.egovframework.form.ParamForm;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 기관정보관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
public interface AdminAuthService {
	

	/**
	 * 선택 메뉴정보
	 * @return
	 */
	public JSONObject getMenuInfo(int mnuCd);
	public int update(String siteMode, int mnuCd, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception;

}