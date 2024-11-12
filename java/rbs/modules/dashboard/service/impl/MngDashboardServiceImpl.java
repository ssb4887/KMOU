package rbs.modules.dashboard.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MenuUtil;
import com.woowonsoft.egovframework.util.StringUtil;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.modules.dashboard.service.MngDashboardService;

@Service("mngDashboardService")
public class MngDashboardServiceImpl extends EgovAbstractServiceImpl implements MngDashboardService {
	
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
	@Override
	public int update(String siteId, int verIdx, String moduleId, ParamForm parameterMap, JSONObject moduleSetting, JSONObject moduleItem) throws Exception {
		if(JSONObjectUtil.isEmpty(moduleSetting) || JSONObjectUtil.isEmpty(moduleItem)) return -1;
		
		String siteMode = RbsProperties.getProperty("Globals.site.mode.usr");
		String siteORPath = "/" + siteMode + "/" + siteId;
		JSONObject dashboardObjects = MenuUtil.getDashboardObject(siteORPath, verIdx, moduleId);
		if(dashboardObjects == null) dashboardObjects = new JSONObject();

		String submitType = "modify";
		Set<?> set = moduleSetting.keySet();
	    Iterator<?> keys = set.iterator();
	    String key = null;
	    String setModuleId = null;

	    JSONObject moduleSettingInfo = null;
	    JSONObject moduleItemInfo = null;
	    JSONObject moduleItems = null;
	    JSONObject moduleItemObj = null;
	    JSONArray moduleItemOrder = null;
	    JSONObject dashboardObject = null;
	    JSONObject dashboardLObject = null;
	    String itemId = null;
	    String columnId = null;
	    int objectType = 0;
	    String itemType = null;
	    String itemValue = null;
	    int itemNumber = 0;				// 여러개 사용하는 경우 (게시판, 콘텐츠 등)
	    String itemMasterCode = null;	// 게시위치 master_code (배너)
	    String checkIdxName = null;
	    Object checkValue = null;
	    while(keys.hasNext()){
	    	// 모듈별 설정정보
			key = (String)keys.next();
			moduleSettingInfo = JSONObjectUtil.getJSONObject(moduleSetting, key);
			if(StringUtil.isEmpty(moduleSettingInfo)) continue;
			setModuleId = JSONObjectUtil.getString(moduleSettingInfo, "module_id");
			if(StringUtil.isEmpty(setModuleId)) continue;
			
			checkIdxName = JSONObjectUtil.getString(moduleSettingInfo, "idx_name");
			itemNumber = JSONObjectUtil.getInt(moduleSettingInfo, "item_number");
			itemMasterCode = JSONObjectUtil.getString(moduleSettingInfo, "item_master_code");
			moduleItemInfo = JSONObjectUtil.getJSONObject(moduleItem, setModuleId + "_item_info");
			moduleItems = JSONObjectUtil.getJSONObject(moduleItemInfo, "items");
			moduleItemOrder = JSONObjectUtil.getJSONArray(moduleItemInfo, submitType + "_order");

			//dashboardObject = new JSONObject();
			dashboardObject = JSONObjectUtil.getJSONObject(dashboardObjects, key);
			if(dashboardObject == null) dashboardObject = new JSONObject();
			
			if(itemNumber > 0) {
				// 다수 입력
				//int setItemIdx = 1;
				for(int itemIdx = 1;itemIdx <= itemNumber;itemIdx ++) {
					/*checkValue = parameterMap.get(setModuleId + checkIdxName + itemIdx);
					if(StringUtil.isEmpty(checkValue)) continue;
					
					dashboardLObject = new JSONObject();*/
					checkValue = parameterMap.get(setModuleId + checkIdxName + itemIdx);
					if(StringUtil.isEmpty(checkValue)) {
						//dashboardLObject = new JSONObject();

						dashboardObject.remove(setModuleId + itemIdx);
						continue;
					}
					else {
						dashboardLObject = JSONObjectUtil.getJSONObject(dashboardObject, key + itemIdx);
						if(dashboardLObject == null) dashboardLObject = new JSONObject();
					}

					// 모듈 항목정보
					for(Object itemIdObj:moduleItemOrder){
						itemId = StringUtil.getString(itemIdObj);
						moduleItemObj = JSONObjectUtil.getJSONObject(moduleItems, itemId);
						if(JSONObjectUtil.isEmpty(moduleItemObj)) continue;

						columnId = JSONObjectUtil.getString(moduleItemObj, "column_id");
						if(columnId != null) columnId = columnId.toLowerCase();
						else columnId = itemId;
						objectType = JSONObjectUtil.getInt(moduleItemObj, "object_type");
						itemType = JSONObjectUtil.getString(moduleItemObj, "item_type");
						
						itemValue = StringUtil.getString(parameterMap.get(setModuleId + itemId + itemIdx));
						if(StringUtil.isEquals(itemType, "1")) {
							// 숫자형식
							dashboardLObject.put(columnId, StringUtil.getInt(itemValue));
						} else {
							// 문자형식
							dashboardLObject.put(columnId, itemValue);
						}
						
						if(objectType == 8) {
							// text + button
							itemValue = StringUtil.getString(parameterMap.get(setModuleId + itemId + itemIdx + "Name"));
							dashboardLObject.put(columnId + "_name", itemValue);
							itemValue = StringUtil.getString(parameterMap.get(setModuleId + itemId + itemIdx + "OName"));
							dashboardLObject.put(columnId + "_oname", itemValue);
						}
					}
					//dashboardObject.put(setModuleId + setItemIdx, dashboardLObject);
					dashboardObject.put(setModuleId + itemIdx, dashboardLObject);
					//setItemIdx ++;
				}
			} else if(!StringUtil.isEmpty(itemMasterCode)) {
				// 게시위치 사용하는 경우 (banner)
				List<Object> bannerItemOptnList = CodeHelper.getOptnList(itemMasterCode);
				for(Object itemOptionObj:bannerItemOptnList) {
					if (itemOptionObj instanceof DataMap) {
						DataMap itemOptionDt = (DataMap) itemOptionObj;
						String itemIdx = StringUtil.getString(itemOptionDt.get("OPTION_CODE"));
						dashboardLObject = JSONObjectUtil.getJSONObject(dashboardObject, key + itemIdx);
						if(dashboardLObject == null) dashboardLObject = new JSONObject();
						// 모듈 항목정보
						for(Object itemIdObj:moduleItemOrder){
							itemId = StringUtil.getString(itemIdObj);
							moduleItemObj = JSONObjectUtil.getJSONObject(moduleItems, itemId);
							if(JSONObjectUtil.isEmpty(moduleItemObj)) continue;

							columnId = JSONObjectUtil.getString(moduleItemObj, "column_id");
							if(columnId != null) columnId = columnId.toLowerCase();
							else columnId = itemId;
							itemType = JSONObjectUtil.getString(moduleItemObj, "item_type");
							
							itemValue = StringUtil.getString(parameterMap.get(setModuleId + itemId + itemIdx));
							if(StringUtil.isEquals(itemType, "1")) {
								// 숫자형식
								dashboardLObject.put(columnId, StringUtil.getInt(itemValue));
							} else {
								// 문자형식
								dashboardLObject.put(columnId, itemValue);
							}
						}
						dashboardObject.put(setModuleId + itemIdx, dashboardLObject);
					}
				}
				
			} else {
				// 단일 입력
				// 모듈 항목정보
				for(Object itemIdObj:moduleItemOrder){
					itemId = StringUtil.getString(itemIdObj);
					moduleItemObj = JSONObjectUtil.getJSONObject(moduleItems, itemId);
					if(JSONObjectUtil.isEmpty(moduleItemObj)) continue;
					
					columnId = JSONObjectUtil.getString(moduleItemObj, "column_id");
					if(columnId != null) columnId = columnId.toLowerCase();
					else columnId = itemId;
					itemType = JSONObjectUtil.getString(moduleItemObj, "item_type");
					itemValue = StringUtil.getString(parameterMap.get(setModuleId + itemId));
					if(StringUtil.isEquals(itemType, "1")) {
						// 숫자형식
						dashboardObject.put(columnId, StringUtil.getInt(itemValue));
					} else {
						// 문자형식
						dashboardObject.put(columnId, itemValue);
					}
				}
				
			}			
			dashboardObjects.put(setModuleId, dashboardObject);
	    }
	    
	    boolean isResult = MenuUtil.writeDashboardObject(siteORPath, verIdx, moduleId, dashboardObjects);
	    if(isResult) return 1;
	    return 0;
	}
}