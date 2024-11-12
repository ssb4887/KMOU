package rbs.modules.dashboard.service;

import net.sf.json.JSONObject;

import com.woowonsoft.egovframework.form.ParamForm;

/**
 * 사이트정보에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
public interface MngDashboardService {
	/**
	 * 수정처리 : 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 파일에 반영
	 * @param siteId
	 * @param verIdx
	 * @param moduleId
	 * @param parameterMap
	 * @param moduleSetting
	 * @param moduleItem
	 * @return
	 * @throws Exception
	 */
	public int update(String siteId, int verIdx, String moduleId, ParamForm parameterMap, JSONObject moduleSetting, JSONObject moduleItem) throws Exception;

}