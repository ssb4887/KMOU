package rbs.modules.menu.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;

/**
 * 사이트정보에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
public interface MenuVerService {
	
	/**
	 * 전체 목록
	 * @param param
	 * @return
	 */
	public List<Object> getOptnList(Map<String, Object> param);
	
	/**
	 * 전체 목록 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public int getTotalCount(Map<String, Object> param);
	
	/**
	 * 전체 목록
	 * @param param
	 * @return
	 */
	public List<Object> getList(Map<String, Object> param);

	/**
	 * 수정 상세조회
	 * @param param
	 * @return
	 */
	public DataMap getModify(Map<String, Object> param);
	
	/**
	 * 등록처리 : 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param uploadModulePath
	 * @param siteId
	 * @param regiIp
	 * @param parameterMap
	 * @param settingInfo
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	public int insert(String uploadModulePath, String siteId, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;
	
	/**
	 * 수정처리 : 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param uploadModulePath
	 * @param siteId
	 * @param verIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param settingInfo
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	public int update(String uploadModulePath, String siteId, int verIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;

	/**
	 * 적용처리
	 * @param siteId
	 * @param verIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param menuSettingInfo
	 * @param menuItems
	 * @param menuItemOrder
	 * @return
	 * @throws Exception
	 */
	public int apply(String siteId, int verIdx, String regiIp, ParamForm parameterMap, JSONObject menuSettingInfo, JSONObject menuItems, JSONArray menuItemOrder) throws Exception;
	
	/**
	 * 템플릿 사이트 생성
	 * @param siteType
	 * @param applyLogoSavedName
	 * @param localPath
	 * @param layoutTmp
	 * @param logoSavedName
	 * @throws Exception
	 */
	public void setLayoutTmp(String siteType, String applyLogoSavedName, String localPath, String layoutTmp, String logoSavedName) throws Exception;
	
	/**
	 * 삭제 전체 목록 수
	 * @param param
	 * @return
	 */
	public int getDeleteCount(Map<String, Object> param);
	
	/**
	 * 삭제 전체 목록
	 * @param param
	 * @return
	 */
	public List<Object> getDeleteList(Map<String, Object> param);
	
	/**
	 * 삭제처리 : 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
	 * @param deleteIdxs
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	public int delete(String siteId, String[] deleteIdxs, String regiIp) throws Exception;
	
	/**
	 * 복원처리 : 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
	 * @param restoreIdxs
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	public int restore(String siteId, String[] restoreIdxs, String regiIp) throws Exception;

	/**
	 * 완전삭제처리 : 화면에 조회된 정보를 데이터베이스에서 삭제
	 * @param uploadModulePath
	 * @param deleteIdxs
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	public int cdelete(String siteId, String[] deleteIdxs, JSONObject items, JSONArray itemOrder) throws Exception;
	
	public void setMenuManageAuthLog(String menuName, JSONObject menu, JSONObject applyMenu, String flag) throws Exception;
	
	public void setMenuManageAuthLog(String menuName, JSONObject menus, JSONObject applyMenus, JSONArray menuArray) throws Exception;

	public void setMenuManageAuthLog(Map<String, Object> webSiteMenuMbrMap, Map<String, Object> applyWebSiteMenuMbrMap) throws Exception;
	
	public Map<String, Object> setMenuMbrMap() throws Exception;
}