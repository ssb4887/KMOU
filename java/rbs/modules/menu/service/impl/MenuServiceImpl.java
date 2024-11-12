package rbs.modules.menu.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import rbs.egovframework.LoginVO;
import rbs.modules.contents.mapper.BranchMapper;
import rbs.modules.contents.mapper.ContentsMapper;
import rbs.modules.menu.mapper.MenuMapper;
import rbs.modules.menu.mapper.MenuMultiMapper;
import rbs.modules.menu.service.MenuService;
import rbs.modules.module.mapper.ModuleAuthMapper;
import rbs.modules.module.mapper.ModuleFnMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.JSONMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MenuUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import com.woowonsoft.egovframework.form.EgovMap;

@Service("menuService")
public class MenuServiceImpl extends EgovAbstractServiceImpl implements MenuService {
	
	@Resource(name="menuMapper")
	private MenuMapper menuDAO;
	
	@Resource(name="menuMultiMapper")
	private MenuMultiMapper menuMultiDAO;
	
	@Resource(name="moduleFnMapper")
	private ModuleFnMapper moduleFnDAO;
	
	@Resource(name="moduleAuthMapper")
	private ModuleAuthMapper moduleAuthDAO;
	
	@Resource(name="contentsMapper")
	private ContentsMapper contentsDAO;
	
	@Resource(name="branchMapper")
	private BranchMapper branchDAO;

	/**
	 * 전체 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getOptnList(Map<String, Object> param) {
		return menuDAO.getOptnList(param);
	}
	
	@Override
	public List<Object> getModuleFnJSONList(Map<String, Object> param) {
		return moduleFnDAO.getOptnJSONList(param);
	}
    @Override
	public List<Object> getContentsJSONList(String lang, Map<String, Object> param) {
		return contentsDAO.getOptnJSONList(lang, param);
	}
    @Override
	public List<Object> getBranchSONList(String lang, Map<String, Object> param) {
		return branchDAO.getOptnJSONList(lang, param);
	}
    @Override
	public List<Object> getModuleAuthJSONList(Map<String, Object> param) {
		return moduleAuthDAO.getOptnJSONList(param);
	}

    @Override
	public int getMaxMenuLevel(Map<String, Object> param){
		return menuDAO.getMaxMenuLevel(param);
	}
    @Override
	public List<Object> getExcelList(Map<String, Object> param) {
		return menuDAO.selectExcelList(param);
	}

    @Override
	public Map<String, List<Object>> getGroupMapList(Map<String, Object> param) {
		return menuMultiDAO.getGroupMapList(param);
	}

    @Override
	public Map<String, List<Object>> getDepartMapList(Map<String, Object> param) {
		return menuMultiDAO.getDepartMapList(param);
	}

    @Override
	public Map<String, List<Object>> getMemberMapList(Map<String, Object> param) {
		return menuMultiDAO.getMemberMapList(param);
	}

    /**
     * 전체 목록 수
	 * @param mapFlag
	 * @param param
	 * @return
	 */
    @Override
	public int getTotalCount(Map<String, Object> param) {
    	return menuDAO.getTotalCount(param);
    }

	/**
	 * 전체 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(Map<String, Object> param) {
		return menuDAO.getList(param);
	}

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getModify(Map<String, Object> param) {
		DataMap viewDAO = menuDAO.getModify(param);
		return viewDAO;
	}

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public EgovMap getJSONModify(Map<String, Object> param) {
		EgovMap viewDAO = menuDAO.getJSONModify(param);
		return viewDAO;
	}
	
	@Override
	public JSONObject getMenuApplyJSONObject(Map<String, Object> param) {
		
		List<Object> menuList = menuDAO.getApplyMenuList(param);
		
		if(menuList == null) return null;
		
		JSONObject urMenuTotalObject = null;

        JSONObject menus = new JSONObject();
        JSONObject menuObject = null;
        JSONMap menuDt = null;
        int menuSize = menuList.size();
        for(int i = 0 ; i < menuSize ; i ++){
        	menuDt = (JSONMap)menuList.get(i);
        	menuObject = new JSONObject();
        	menuObject.putAll((Map)menuDt);
        	menus.put("menu" + menuDt.get("menu_idx"), menuObject);
        }
        
		JSONArray menuListArray = MenuUtil.getListToJSON(0, 100, 0, 0, menuList, 0);
		int menusSize = menus.size();
		int menuListSize = 0;
		if(menuListArray != null && !menuListArray.isEmpty()) menuListSize = menuListArray.size();
		if(menusSize > 0 || menuListSize > 0) {
			urMenuTotalObject = new JSONObject(); 
	        if(menuSize > 0) urMenuTotalObject.put("menus", menus);
	        if(menuListSize > 0) urMenuTotalObject.put("menu-list", menuListArray);
		}
		return urMenuTotalObject;
	}

	/**
	 * 적용 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public EgovMap getJSONApplyView(Map<String, Object> param) {
		EgovMap viewDAO = menuDAO.getJSONApplyView(param);
		return viewDAO;
	}
    
	/**
	 * 기본정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param Map 등록정보
	 * @return result 등록결과
	 * @throws Exception
	 */
	@Override
	public int insert(String siteId, int verIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		// 항목설정으로 저장항목 setting
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;

		// 저장
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		List<Object> multiDataList = StringUtil.getList(dataMap.get("multiDataList"));
		List<DTForm> masterSearchList = new ArrayList<DTForm>();
		masterSearchList.add(new DTForm("SITE_ID", siteId));
		masterSearchList.add(new DTForm("VER_IDX", verIdx));

		//고유아이디 셋팅
		Map<String, Object> nextIdParam = new HashMap<String, Object>();
		List<DTForm> nextIdSearchList = new ArrayList<DTForm>();
		nextIdSearchList.addAll(masterSearchList);
		nextIdParam.put("searchList", nextIdSearchList);
		int menuIdx = menuDAO.getNextId(nextIdParam);

		// 순서 setting
		int ordType = StringUtil.getInt(parameterMap.get("ordType"));
		int targetMenuIdx = StringUtil.getInt(parameterMap.get("ordIdx"));
		int result1 = 0;
		if(targetMenuIdx <= 0) {
			int targetOrdIdx = 0;			// 이동될 ord_idx
			// 이동될 ord_idx select
			Map<String, Object> targetOrdParam = new HashMap<String, Object>();
			List<DTForm> targetOrdSearchList = new ArrayList<DTForm>();
			targetOrdSearchList.addAll(masterSearchList);
			targetOrdParam.put("searchList", targetOrdSearchList);
			targetOrdIdx = menuDAO.getMaxOrdIdx(targetOrdParam);
			
			dataList.add(new DTForm("ORDER_IDX", targetOrdIdx + 1));
			dataList.add(new DTForm("GROUP_MENU_IDX", menuIdx));
			dataList.add(new DTForm("MENU_LEVEL", 1));
		} else {

			// target 순서 수정 data, 조건
			List<DTForm> targetDataList = null;
			List<DTForm> targetSearchList = null;
			targetSearchList = new ArrayList<DTForm>();

			//int targetPrtMenuIdx = 0;		// 이동될 PARENT_MENU_IDX 
			int targetOrdIdx = 0;			// 이동될 ord_idx
			// 이동될 ord_idx select
			Map<String, Object> targetOrdParam = new HashMap<String, Object>();
			List<DTForm> targetOrdSearchList = new ArrayList<DTForm>();
			targetOrdSearchList.addAll(masterSearchList);;
			
			if(ordType == 1) {
				// 위 : 선택한 단원의 첫번째 순서
				targetOrdSearchList.add(new DTForm("MENU_IDX", targetMenuIdx));
				targetOrdParam.put("searchList", targetOrdSearchList);
				targetOrdIdx = menuDAO.getOrdIdx(targetOrdParam);
				
				targetSearchList.add(new DTForm("ORDER_IDX", targetOrdIdx, ">="));
				// ord_idx setting
				dataList.add(new DTForm("ORDER_IDX", targetOrdIdx));
			} else {
				// 아래/내부 : 선택한 단원의 마지막 순서
				//List<DTForm> targetOrdSearchList2 = new ArrayList<DTForm>();
				//targetOrdSearchList2.add(new DTForm("MENU_IDX", targetMenuIdx));
				targetOrdParam.put("searchList", targetOrdSearchList);
				//targetOrdParam.put("searchList2", targetOrdSearchList2);
				targetOrdParam.put("menuIdx", targetMenuIdx);
				targetOrdIdx = menuDAO.getNextOrdIdx(targetOrdParam);

				targetSearchList.add(new DTForm("ORDER_IDX", targetOrdIdx, ">"));
				// ord_idx setting
				dataList.add(new DTForm("ORDER_IDX", targetOrdIdx + 1));
			}
			targetDataList = new ArrayList<DTForm>();
			targetDataList.add(new DTForm("ORDER_IDX", "ORDER_IDX + 1", 1));
			
			// target 순서 수정
			if(targetDataList != null) {
				Map<String, Object> targetParam = new HashMap<String, Object>();
				targetParam.put("dataList", targetDataList);
				targetParam.put("searchList", targetSearchList);
				result1 = menuDAO.update(targetParam);
			}
			
			// prt_unt_cd setting
			
			// 선택한 위치의 정보 얻기
			Map<String, Object> targetPrtParam = new HashMap<String, Object>();
			List<DTForm> targetPrtSearchList = new ArrayList<DTForm>();
			targetPrtSearchList.addAll(masterSearchList);;
			targetPrtSearchList.add(new DTForm("MENU_IDX", targetMenuIdx));
			targetPrtParam.put("searchList", targetPrtSearchList);
			DataMap targetMenuDt = menuDAO.getMenuKeyView(targetPrtParam);
			int targetGroupMenuIdx = StringUtil.getInt(targetMenuDt.get("GROUP_MENU_IDX"));
			int targetMenuLevel = StringUtil.getInt(targetMenuDt.get("MENU_LEVEL"));
			int menuLevel = targetMenuLevel;
			int groupMenuIdx = menuIdx;
			int parentMenuIdx = 0;
			if(ordType == 3) {
				// 내부
				parentMenuIdx = targetMenuIdx;
				menuLevel = targetMenuLevel + 1;
				groupMenuIdx = targetGroupMenuIdx;
			} else {
				// 위/아래
				parentMenuIdx = StringUtil.getInt(targetMenuDt.get("PARENT_MENU_IDX"));
				if(targetMenuLevel > 1) {
					groupMenuIdx = targetGroupMenuIdx;
				} else menuLevel = 1;
			}
			dataList.add(new DTForm("PARENT_MENU_IDX", parentMenuIdx));
			dataList.add(new DTForm("GROUP_MENU_IDX", groupMenuIdx));
			dataList.add(new DTForm("MENU_LEVEL", menuLevel));
		}

		// insert 항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);

		dataList.add(new DTForm("MENU_IDX", menuIdx));
		dataList.addAll(masterSearchList);

		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
		    	
    	dataList.add(new DTForm("REGI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("REGI_ID", loginMemberId));
    	dataList.add(new DTForm("REGI_NAME", loginMemberName));
    	dataList.add(new DTForm("REGI_IP", regiIp));
    	
    	dataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));
    	
    	param.put("dataList", dataList);
    	
		// insert
		int result = menuDAO.insert(param);
		if(result > 0) {
			if(multiDataList != null) {
				int multiDataSize = multiDataList.size();
				for(int i = 0 ; i < multiDataSize ; i ++) {
					Map<String, Object> multiParam = (HashMap)multiDataList.get(i);
					multiParam.put("SITE_ID", siteId);
					multiParam.put("VER_IDX", verIdx);
			    	multiParam.put("MENU_IDX", menuIdx);
					result = menuMultiDAO.insert(multiParam);
				}
			}
		}
				
		return menuIdx;
	}

	/**
	 * 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param param 수정정보
	 * @throws Exception
	 */
	@Override
	public int update(String siteId, int verIdx, int sourceMenuIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		//Map<String, Object> langParam = new HashMap<String, Object>();
		//List<DTForm> langDataList = new ArrayList<DTForm>();
		List<DTForm> masterSearchList = new ArrayList<DTForm>();
		masterSearchList.add(new DTForm("SITE_ID", siteId));
		masterSearchList.add(new DTForm("VER_IDX", verIdx));
		
		searchList.addAll(masterSearchList);
		searchList.add(new DTForm("MENU_IDX", sourceMenuIdx));
		
		// 항목설정으로 저장항목 setting
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// 저장 항목
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
    	
    	dataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));

		// 순서 setting 
		// sourceUntCd != targetUntCd
		// 하위단원으로 이동하지 않는 경우
		// 내부이동인 경우 prt_unt_cd가 같지 않은 경우
		int ordType = StringUtil.getInt(parameterMap.get("ordType"));
		int targetMenuIdx = StringUtil.getInt(parameterMap.get("ordIdx"));
		if(targetMenuIdx != sourceMenuIdx) {
			int sourcePrtMenuIdx = -1;		// 이동할 prt_unt_cd
			int targetPrtMenuIdx = -1;		// 이동될 prt_unt_cd 
			int sourceMenuLevel = 0;		// 이동할 menu_level
			int sourceOrdIdx = 0;			// 이동할 ord_idx (현재)
			int targetOrdIdx = 0;			// 이동될 ord_idx
			int gOrdCnt = 0;				// sourceOrdIdx - targetOrdIdx
			List<Object> sourceClassList = null;	// 이동할 menuIdx 목록
			int sourceOrdCnt = 0;
			
			// 하위메뉴로 이동여부
			Map<String, Object> inChildParam = new HashMap<String, Object>();
			List<DTForm> inChildSearchList = new ArrayList<DTForm>();
			inChildSearchList.addAll(masterSearchList);
			inChildParam.put("searchList", inChildSearchList);
			inChildParam.put("searchMenuIdx", targetMenuIdx);
			inChildParam.put("menuIdx", sourceMenuIdx);
			int inChild = menuDAO.getInChildCount(inChildParam);
			
			if(inChild > 0) {
				// 하위메뉴로 이동하는 경우
				return -11;
			}
			
			// 선택한 위치의 정보 얻기
			Map<String, Object> targetPrtParam = new HashMap<String, Object>();
			List<DTForm> targetPrtSearchList = new ArrayList<DTForm>();
			targetPrtSearchList.addAll(masterSearchList);
			targetPrtSearchList.add(new DTForm("MENU_IDX", targetMenuIdx));
			targetPrtParam.put("searchList", targetPrtSearchList);
			DataMap targetMenuDt = menuDAO.getMenuKeyView(targetPrtParam);
			int targetGroupMenuIdx = StringUtil.getInt(targetMenuDt.get("GROUP_MENU_IDX"));
			int targetMenuLevel = StringUtil.getInt(targetMenuDt.get("MENU_LEVEL"));
			targetOrdIdx = StringUtil.getInt(targetMenuDt.get("ORDER_IDX"));
			
			// 이동할 정보 얻기
			Map<String, Object> sourceOrdParam = new HashMap<String, Object>();
			List<DTForm> sourceOrdSearchList = new ArrayList<DTForm>();
			sourceOrdSearchList.addAll(masterSearchList);
			sourceOrdSearchList.add(new DTForm("MENU_IDX", sourceMenuIdx));
			sourceOrdParam.put("searchList", sourceOrdSearchList);
			DataMap sourceMenuDt = menuDAO.getMenuKeyView(sourceOrdParam);
			sourceMenuLevel = StringUtil.getInt(sourceMenuDt.get("MENU_LEVEL"));
			sourceOrdIdx = StringUtil.getInt(sourceMenuDt.get("ORDER_IDX"));
			
			int groupMenuIdx = StringUtil.getInt(sourceMenuDt.get("GROUP_MENU_IDX"));
			int parentMenuIdx = 0;
			int addMenuLevel = targetMenuLevel - sourceMenuLevel;
			
			// 이동될 prt_unt_cd select 
			if(ordType == 3) {
				// 내부
				targetPrtMenuIdx = targetMenuIdx;
				
				Map<String, Object> sourcePrtParam = new HashMap<String, Object>();
				List<DTForm> sourcePrtSearchList = new ArrayList<DTForm>();
				sourcePrtSearchList.addAll(masterSearchList);
				sourcePrtSearchList.add(new DTForm("MENU_IDX", sourceMenuIdx));
				sourcePrtParam.put("searchList", sourcePrtSearchList);
				sourcePrtMenuIdx = menuDAO.getPrtMenuIdx(sourcePrtParam);
				
				// 같은 상위단원의 내부로 선택된 경우
				if(sourcePrtMenuIdx == targetPrtMenuIdx) return -12;

				parentMenuIdx = targetMenuIdx;
				addMenuLevel ++;
				groupMenuIdx = targetGroupMenuIdx;
			} else {
				// 위/아래
				parentMenuIdx = StringUtil.getInt(targetMenuDt.get("PARENT_MENU_IDX"));
				if(targetMenuLevel > 1) groupMenuIdx = targetGroupMenuIdx;
				else groupMenuIdx = sourceMenuIdx;
			}
			
			// 이동될 ord_idx select
			Map<String, Object> targetOrdParam = new HashMap<String, Object>();
			List<DTForm> targetOrdSearchList = new ArrayList<DTForm>();
			targetOrdSearchList.addAll(masterSearchList);
			
			if(ordType == 1) {
				// 위 : 선택한 단원의 첫번째 순서
			} else {
				// 아래/내부 : 선택한 단원의 마지막 순서
				targetOrdParam.put("searchList", targetOrdSearchList);
				targetOrdParam.put("menuIdx", targetMenuIdx);
				targetOrdIdx = menuDAO.getNextOrdIdx(targetOrdParam);
			}
			
			// 이동할 순서 - 이동될 순서
			gOrdCnt = sourceOrdIdx - targetOrdIdx;

			// 이동할 menuIdx 목록
			Map<String, Object> sourceMenuParam = new HashMap<String, Object>();
			List<DTForm> sourceMenuSearchList = new ArrayList<DTForm>();
			sourceMenuSearchList.addAll(masterSearchList);
			sourceMenuParam.put("searchList", sourceMenuSearchList);
			sourceMenuParam.put("menuIdx", sourceMenuIdx);
			sourceClassList = menuDAO.getSourceList(sourceMenuParam);
			sourceOrdCnt = (sourceClassList != null)? sourceClassList.size():0;
			
			List<DTForm> sourceDataList = null;
			List<DTForm> targetDataList = null;
			List<DTForm> targetSearchList = null;
			int result1 = 0;
			if(gOrdCnt != 0) {
				int gOrbCntAbs = Math.abs(gOrdCnt);
				
				if(gOrdCnt > 0) {
					// 이동할 ord_idx가 아래에 있는 경우 (sourceOrdIdx > targetOrdIdx)
					int addCnt = 0;
					if(ordType != 1) addCnt = 1;				// 아래/내부
					sourceDataList = new ArrayList<DTForm>();
					sourceDataList.add(new DTForm("ORDER_IDX", "ORDER_IDX - " + gOrbCntAbs + " + " + addCnt, 1));
					
					targetDataList = new ArrayList<DTForm>();
					targetDataList.add(new DTForm("ORDER_IDX", "ORDER_IDX + " + sourceOrdCnt, 1));
					
					targetSearchList = new ArrayList<DTForm>();
					targetSearchList.addAll(masterSearchList);
					if(ordType == 1) {
						// 위
						targetSearchList.add(new DTForm("ORDER_IDX", targetOrdIdx, ">="));
						targetSearchList.add(new DTForm("ORDER_IDX", sourceOrdIdx, "<"));
					} else {
						// 아래/내부
						targetSearchList.add(new DTForm("ORDER_IDX", targetOrdIdx, ">"));
						targetSearchList.add(new DTForm("ORDER_IDX", sourceOrdIdx, "<"));
					}
				} else if(gOrdCnt < 0) {
					// 이동할 ord_idx가 위에 있는 경우 (sourceOrdIdx > targetOrdIdx)
					int addCnt = 0;
					if(ordType != 1) addCnt = 1;				// 아래/내부
					sourceDataList = new ArrayList<DTForm>();
					sourceDataList.add(new DTForm("ORDER_IDX", "ORDER_IDX  + (" + (gOrbCntAbs - sourceOrdCnt + addCnt) + ")", 1));
					
					targetDataList = new ArrayList<DTForm>();
					targetDataList.add(new DTForm("ORDER_IDX", "ORDER_IDX - " + sourceOrdCnt, 1));
					
					targetSearchList = new ArrayList<DTForm>();
					targetSearchList.addAll(masterSearchList);
					if(ordType == 1) {
						// 위
						targetSearchList.add(new DTForm("ORDER_IDX", targetOrdIdx, "<"));
						targetSearchList.add(new DTForm("ORDER_IDX", (sourceOrdIdx + sourceOrdCnt), ">="));
					} else {
						// 아래/내부
						targetSearchList.add(new DTForm("ORDER_IDX", (sourceOrdIdx + sourceOrdCnt - 1), ">"));
						targetSearchList.add(new DTForm("ORDER_IDX", targetOrdIdx, "<="));
					}
				}
			
				// target 순서 수정
				if(targetDataList != null) {
					Map<String, Object> targetParam = new HashMap<String, Object>();
					targetParam.put("dataList", targetDataList);
					targetParam.put("searchList", targetSearchList);
					result1 = menuDAO.update(targetParam);
				}
			}
				
			// source 순서 수정
			if(addMenuLevel != 0) {
				if(sourceDataList == null) sourceDataList = new ArrayList<DTForm>();
				sourceDataList.add(new DTForm("MENU_LEVEL", "MENU_LEVEL + (" + addMenuLevel + ")", 1));
			}
			
			// 이동할 data update
			if(sourceClassList != null && sourceDataList != null) {
				Map<String, Object> sourceParam = new HashMap<String, Object>();
				sourceDataList.add(new DTForm("GROUP_MENU_IDX", groupMenuIdx));
				sourceParam.put("dataList", sourceDataList);
				
				List<DTForm> sourceSearchList = new ArrayList<DTForm>();
				sourceSearchList.addAll(masterSearchList);
				sourceSearchList.add(new DTForm("MENU_IDX", sourceClassList.toArray()));
				sourceParam.put("searchList", sourceSearchList);
				result1 = menuDAO.updateOrdIdx(sourceParam);
			}
			dataList.add(new DTForm("PARENT_MENU_IDX", parentMenuIdx));
		}
		
    	param.put("dataList", dataList);
    	param.put("searchList", searchList);
    	
		int result = menuDAO.update(param);	
		int result1 = 0;
		if(result > 0) {
			
			// 5. multi data 저장
			Map<String, Object> multiDelParam = new HashMap<String, Object>();
			multiDelParam.put("SITE_ID", siteId);
			multiDelParam.put("VER_IDX", verIdx);
			multiDelParam.put("MENU_IDX", sourceMenuIdx);
			result1 = menuMultiDAO.cdelete(multiDelParam);
			
			List multiDataList = StringUtil.getList(dataMap.get("multiDataList"));
			if(multiDataList != null) {
				int multiDataSize = multiDataList.size();
				for(int i = 0 ; i < multiDataSize ; i ++) {
					Map<String, Object> multiParam = (HashMap)multiDataList.get(i);
					multiParam.put("SITE_ID", siteId);
					multiParam.put("VER_IDX", verIdx);
			    	multiParam.put("MENU_IDX", sourceMenuIdx);
					result = menuMultiDAO.insert(multiParam);
				}
			}
		}
		
		return result;
	}
	
	public int mngUpdate(String siteId, int verIdx, int[] mngMenuIdxs, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		searchList.add(new DTForm("SITE_ID", siteId));
		searchList.add(new DTForm("VER_IDX", verIdx));
		searchList.add(new DTForm("MENU_IDX", mngMenuIdxs));
		
		// 항목설정으로 저장항목 setting
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// 저장 항목
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
    	
    	dataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));
		
    	param.put("dataList", dataList);
    	param.put("searchList", searchList);
    	
		int result = menuDAO.update(param);	
		
		return result;
	}

    /**
     * 삭제 목록 수
	 * @param mapFlag
	 * @param param
	 * @return
	 */
    @Override
	public int getDeleteCount(Map<String, Object> param) {
    	return menuDAO.getDeleteCount(param);
    }

	/**
	 * 삭제 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDeleteList(Map<String, Object> param) {
		return menuDAO.getDeleteList(param);
	}

	/**
	 * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
	 * @param param 삭제정보
	 * @throws Exception
	 */
	@Override
	public int delete(String siteId, int verIdx, int menuIdx, String regiIp) throws Exception {
		if(menuIdx <= 0) return 0;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
		
		int result = 1;
		int result1 = 0;
		try {
			// 저장조건
			param.put("menuIdx", menuIdx);
			param.put("siteId", siteId);
			param.put("verIdx", verIdx);
			param.put("modiIdx", loginMemberIdx);
			param.put("modiId", loginMemberId);
			param.put("modiName", loginMemberName);
			param.put("modiIp", regiIp);
			
			result1 = menuDAO.delete(param);
		} catch(Exception e) {
			result = 0;
		}
		return result;
	}

	/**
	 * 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
	 * @param param 복원정보
	 * @throws Exception
	 */
	@Override
	public int restore(String siteId, int verIdx, int[] restoreIdxs, String regiIp) throws Exception {
		if(restoreIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();

		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}

		int result = 1;
		int result1 = 0;
		try {
			for(int menuIdx : restoreIdxs) {
				// 저장조건
				param.put("menuIdx", menuIdx);
				param.put("siteId", siteId);
				param.put("verIdx", verIdx);
				param.put("modiIdx", loginMemberIdx);
				param.put("modiId", loginMemberId);
				param.put("modiName", loginMemberName);
				param.put("modiIp", regiIp);
				
				result1 = menuDAO.restore(param);
			}
		} catch(Exception e) {
			result = 0;
		}
		return result;
	}

	/**
	 * 화면에 조회된 정보를 데이터베이스에서 삭제
	 * @param param 완전삭제정보
	 * @throws Exception
	 */
	@Override
	public int cdelete(String siteId, int verIdx, int[] deleteIdxs, JSONObject items, JSONArray itemOrder) throws Exception {
		if(deleteIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		int result = 1;
		int result1 = 0;
		try {
			for(int menuIdx : deleteIdxs) {
				// 저장조건
				param.put("menuIdx", menuIdx);
				param.put("siteId", siteId);
				param.put("verIdx", verIdx);
				
				result1 = menuDAO.cdelete(param);
			}
		} catch(Exception e) {
			result = 0;
		}
		
		if(result > 0) {
			// mst_cd에 해당하는 전체 순서 update
			param = new HashMap<String, Object>();
			searchList = new ArrayList<DTForm>();
			// 저장조건
			searchList.add(new DTForm("SITE_ID", siteId));
			searchList.add(new DTForm("VER_IDX", verIdx));
			param.put("searchList", searchList);
			
			result1 = menuDAO.updateTotOrdIdx(param);
		}
		
		return result;
	}

	@Override
	public HashMap<String, Object> getJSONMultiHashMap(int menuIdx, int verIdx, String siteId, JSONObject items, JSONArray itemOrder) {
		HashMap<String, Object> resultHashMap = new HashMap<String, Object>();
		if(!JSONObjectUtil.isEmpty(itemOrder) && !JSONObjectUtil.isEmpty(items)) {
			int searchOrderSize = itemOrder.size();
			for(int i = 0 ; i < searchOrderSize ; i ++) {
				String itemId = JSONObjectUtil.getString(itemOrder, i);
				JSONObject item = JSONObjectUtil.getJSONObject(items, itemId);
				int formatType = JSONObjectUtil.getInt(item, "format_type");
				int objectType = JSONObjectUtil.getInt(item, "object_type");
				if(formatType == 0 && (objectType == 3 || objectType == 4 || objectType == 11)) {
					// mult file
					Map<String, Object> param = new HashMap<String, Object>();
					List<DTForm> searchList = new ArrayList<DTForm>();
			    	searchList.add(new DTForm("A.MENU_IDX", menuIdx));
			    	searchList.add(new DTForm("A.VER_IDX", verIdx));
			    	searchList.add(new DTForm("A.SITE_ID", siteId));
			    	searchList.add(new DTForm("A.ITEM_ID", itemId));
					param.put("searchList", searchList);
					resultHashMap.put(itemId, menuMultiDAO.getJSONList(param));
				}
			}
		}
		
		return resultHashMap;
	}

	@Override
	public HashMap<String, Object> getJSONApplyMultiHashMap(int menuIdx, int verIdx, String siteId, JSONObject items, JSONArray itemOrder) {
		HashMap<String, Object> resultHashMap = new HashMap<String, Object>();
		if(!JSONObjectUtil.isEmpty(itemOrder) && !JSONObjectUtil.isEmpty(items)) {
			int searchOrderSize = itemOrder.size();
			for(int i = 0 ; i < searchOrderSize ; i ++) {
				String itemId = JSONObjectUtil.getString(itemOrder, i);
				JSONObject item = JSONObjectUtil.getJSONObject(items, itemId);
				int formatType = JSONObjectUtil.getInt(item, "format_type");
				int objectType = JSONObjectUtil.getInt(item, "object_type");
				if(formatType == 0 && (objectType == 3 || objectType == 4 || objectType == 11)) {
					// mult file
					Map<String, Object> param = new HashMap<String, Object>();
					List<DTForm> searchList = new ArrayList<DTForm>();
			    	searchList.add(new DTForm("A.MENU_IDX", menuIdx));
			    	searchList.add(new DTForm("A.VER_IDX", verIdx));
			    	searchList.add(new DTForm("A.SITE_ID", siteId));
			    	searchList.add(new DTForm("A.ITEM_ID", itemId));
					param.put("searchList", searchList);
					resultHashMap.put(itemId, menuMultiDAO.getJSONApplyList(param));
				}
			}
		}
		
		return resultHashMap;
	}
}