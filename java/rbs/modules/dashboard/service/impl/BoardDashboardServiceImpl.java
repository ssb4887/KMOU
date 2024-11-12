package rbs.modules.dashboard.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.StringUtil;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.modules.dashboard.mapper.DashboardMapper;
import rbs.modules.dashboard.service.ModuleDashboardService;

@Service("boardDashboardService")
public class BoardDashboardServiceImpl extends EgovAbstractServiceImpl implements ModuleDashboardService {

	@Resource(name="dashboardMapper")
	private DashboardMapper dashboardDao;


	@Override
	public HashMap<String, Object> getModuleHashMap(String lang, String moduleId, JSONObject siteMenus, JSONObject dashboardObject, JSONObject moduleTotalObject){

		HashMap<String, Object> resultHashMap = new HashMap<String, Object>();
		JSONArray objNames = dashboardObject.names();
		JSONObject dashboardLObject = null;
		int dashboardMenuIdx = 0;
		JSONObject menuObj = null;
		String menuModuleId = null;
		int menuFnIdx = 0;
		String cateListColumnId = null;
		String cateListCode = null;
		int useDldate = 0;
		int useQna = 0;
		int limitNumber = 0;
		int isListimg = 0;
		int isContents = 0;
		String designType = null;
		for(Object obj:objNames) {
			String objName = (String)obj;
			
			dashboardLObject = JSONObjectUtil.getJSONObject(dashboardObject, objName);
			if(JSONObjectUtil.isEmpty(dashboardLObject)) continue;
			
			dashboardMenuIdx = JSONObjectUtil.getInt(dashboardLObject, "menu_idx");
			menuObj = JSONObjectUtil.getJSONObject(siteMenus, "menu" + dashboardMenuIdx);
			menuModuleId = JSONObjectUtil.getString(menuObj, "module_id");
			
			// 메뉴의 모듈과 일치하지 않는 경우 삭제
			if(!StringUtil.isEquals(moduleId, menuModuleId)) continue;

			// 메뉴명 setting
			dashboardLObject.put("menu_name", JSONObjectUtil.getString(menuObj, "menu_name"));
			
			menuFnIdx = JSONObjectUtil.getInt(menuObj, "fn_idx");
			
			// 모듈설정 목록정보
			JSONObject moduleObject = JSONObjectUtil.getJSONObject(moduleTotalObject, "item" + menuFnIdx);
			cateListColumnId = JSONObjectUtil.getString(moduleObject, "dset_cate_list_column_id");
			if(!StringUtil.isEmpty(cateListColumnId)) {
				cateListCode = StringUtil.getString(dashboardLObject.get("cate_list_code"));
			} else {
				cateListCode = null;
			}
			
			Map<String, Object> param = new HashMap<String, Object>();
			List<DTForm> searchList = new ArrayList<DTForm>();
			if(!StringUtil.isEmpty(cateListColumnId) && !StringUtil.isEmpty(cateListCode)) {
				searchList.add(new DTForm("A." + cateListColumnId, cateListCode));
			}
			
			useDldate = JSONObjectUtil.getInt(moduleObject, "use_dldate");
			useQna = JSONObjectUtil.getInt(moduleObject, "use_qna");
			limitNumber = JSONObjectUtil.getInt(dashboardLObject, "limit_number");
			isListimg = JSONObjectUtil.getInt(dashboardLObject, "islistimg");
			isContents = JSONObjectUtil.getInt(dashboardLObject, "iscontents");
			designType = JSONObjectUtil.getString(menuObj, "design_type");

			param.put("searchList", searchList);
    		param.put("firstIndex", 0);
    		param.put("lastIndex", limitNumber);
    		param.put("recordCountPerPage", limitNumber);
			if(useDldate == 1) param.put("useDldate", true);
			if(useQna == 1) param.put("useQna", true);
			if(isListimg == 1) param.put("useListimg", true);
			if(isContents == 1) param.put("useContents", true);
			if(StringUtil.isEquals(designType, "photo") || StringUtil.isEquals(designType, "movie")) param.put("useFile", true);
	    	param.put("fnIdx", menuFnIdx);
			resultHashMap.put(objName, dashboardDao.getBoardList(designType, param));
		}
		
		return resultHashMap;
	}
}