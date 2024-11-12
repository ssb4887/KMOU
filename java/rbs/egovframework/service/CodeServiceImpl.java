package rbs.egovframework.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.service.CodeService;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.StringUtil;

import rbs.egovframework.mapper.CodeOptnMapper;

/**
 * 공통코드에 관한 클래스를 정의한다.
 * @author user
 */
public class CodeServiceImpl extends EgovAbstractServiceImpl implements CodeService {

	@Resource(name="rbsCodeOptnMapper")
	private CodeOptnMapper codeOptnDAO;

	/**
	 * 기능분류 master목록
	 * @return
	 */
	@Override
	public List<Object> getClassMstrList() {

		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
    	searchList.add(new DTForm("A.ISHIDDEN", "0"));
		param.put("searchList", searchList);
		
		return codeOptnDAO.getClassMstrList(param);
	}

	/**
	 * 기능분류 목록
	 * @param masterCode
	 * @param maxLevel
	 * @return
	 */
	@Override
	public List<Object> getClassOptnList(String masterCode, int maxLevel) {

		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
    	searchList.add(new DTForm("A.MASTER_CODE", masterCode));
		if(maxLevel > 0) searchList.add(new DTForm("A.CLASS_LEVEL", maxLevel, "<="));
		param.put("searchList", searchList);
		
		return codeOptnDAO.getClassOptnList(param);
	}

	/**
	 * 기능분류 목록
	 * @param masterCode
	 * @return
	 */
	@Override
	public List<Object> getClassOptnList(String masterCode) {

		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
    	searchList.add(new DTForm("A.MASTER_CODE", masterCode));
		param.put("searchList", searchList);
		
		return codeOptnDAO.getClassOptnList(param);
	}

	/**
	 * 기능코드 master목록
	 * @return
	 */
	@Override
	public List<Object> getMstrList() {

		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
    	searchList.add(new DTForm("A.ISHIDDEN", "0"));
		param.put("searchList", searchList);
		
		return codeOptnDAO.getMstrList(param);
	}

	/**
	 * 기능코드명
	 * @param masterCode
	 * @param optnCode
	 * @return
	 */
	@Override
	public String getOptnName(String masterCode, String optnCode){

		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
    	searchList.add(new DTForm("A.MASTER_CODE", masterCode));
    	searchList.add(new DTForm("A.OPTION_CODE", optnCode));
		param.put("searchList", searchList);
		
		return codeOptnDAO.getOptnName(param);
	}

	/**
	 * 사용자유형명
	 * @param utpIdx
	 * @return
	 */
	@Override
	public String getMemberUstpName(int utpIdx){

		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.USERTYPE_IDX", utpIdx));
		param.put("searchList", searchList);
		
		return codeOptnDAO.getUstpName(param);
	}

	/**
	 * 공통코드 - resultType="dataMap"
	 * @param masterCode
	 * @return
	 */
	@Override
	public List<Object> getOptnList(String masterCode) {

		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
    	searchList.add(new DTForm("A.MASTER_CODE", masterCode));
		param.put("searchList", searchList);
		
		return codeOptnDAO.getOptnList(param);
	}

	/**
	 * 공통코드 - resultType="egovMap"
	 * @param masterCode
	 * @return
	 */
	@Override
	public List<Object> getOptnJSONList(String masterCode) {

		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
    	searchList.add(new DTForm("A.MASTER_CODE", masterCode));
		param.put("searchList", searchList);
		
		return codeOptnDAO.getOptnJSONList(param);
	}

	/**
	 * 회원목록 - resultType="dataMap"
	 * @param memberIdxs
	 * @return
	 */
	@Override
	public List<Object> getMemberList(String[] memberIdxs) {
		if(memberIdxs == null) return null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A.MEMBER_IDX", memberIdxs));
		param.put("searchList", searchList);
		
		return codeOptnDAO.getMemberList(param);
		
	}

	/**
	 * 회원목록 - resultType="dataMap"
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getMemberList(Map<String, Object> param) {
		return codeOptnDAO.getMemberList(param);
	}

	/**
	 * 회원목록 - resultType="egovMap"
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getMemberJSONList(Map<String, Object> param) {
		return codeOptnDAO.getMemberJSONList(param);
	}

	/**
	 * 사용자유형
	 * @param utpType adm, usr
	 * @return
	 */
	@Override
	public List<Object> getMemberUstpList(String utpType) {

		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
    	//searchList.add(new DTForm("A.UTP_TYPE", utpType));
		String admMode = RbsProperties.getProperty("Globals.site.mode.adm");
		String usrMode = RbsProperties.getProperty("Globals.site.mode.usr");
    	int admMbrType = RbsProperties.getPropertyInt("Globals.code.USERTYPE_ADMIN");
		if(StringUtil.isEquals(utpType, admMode)) {
			// 관리자
			searchList.add(new DTForm("A.USERTYPE_IDX", admMbrType/*Defines.CODE_USERTYPE_ADMIN*/, ">="));
		} else if(StringUtil.isEquals(utpType, usrMode)) {
			// 일반회원
			searchList.add(new DTForm("A.USERTYPE_IDX", admMbrType/*Defines.CODE_USERTYPE_ADMIN*/, "<"));
		}
		param.put("searchList", searchList);
		
		return codeOptnDAO.getUstpList(param);
	}

	/**
	 * 사용자그룹목록
	 * @return
	 */
	@Override
	public List<Object> getMemberGrupList() {
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
    	//searchList.add(new DTForm("A.GRP_TYPE", grpType));
		param.put("searchList", searchList);
		return codeOptnDAO.getGrupList(param);
	}

	/**
	 * 마스터코드별 공통코드
	 * @param listSearch		: 검색항목 설정정보
	 * @return
	 */
	@Override
	public HashMap<String, Object> getHashMapList(JSONObject listSearch) {
		JSONArray tmpOrder = JSONObjectUtil.getJSONArray(listSearch, "search_order");
		JSONArray addOrder = JSONObjectUtil.getJSONArray(listSearch, "searchAddOptn_order");
		JSONObject keyInfo = JSONObjectUtil.getJSONObject(listSearch, "key");
		JSONObject tmpItems = JSONObjectUtil.getJSONObject(keyInfo, "items");
		JSONArray searchOrder = new JSONArray();
		if(tmpOrder != null) searchOrder.addAll(tmpOrder);
		if(addOrder != null) searchOrder.addAll(addOrder);
		JSONObject keyItems = new JSONObject();
		if(tmpItems != null) keyItems.accumulateAll(tmpItems);
		return getHashMapList(true, keyItems, searchOrder);
	}

	/**
	 * 마스터코드별 공통코드
	 * @param listSearch		: 검색항목 설정정보
	 * @param tabCateId         : 탭코드항목id
	 * @param tabCateColumn     : 탭코드컬럼id
	 * @param tabCateObjectType : 탭코드 object종류
	 * @return
	 */
	@Override
	public HashMap<String, Object> getHashMapList(JSONObject listSearch, String tabCateId, String tabCateColumn, int tabCateObjectType, String tabCateMasterCode) {
		JSONArray tmpOrder = JSONObjectUtil.getJSONArray(listSearch, "search_order");
		JSONArray addOrder = JSONObjectUtil.getJSONArray(listSearch, "searchAddOptn_order");
		JSONObject keyInfo = JSONObjectUtil.getJSONObject(listSearch, "key");
		JSONObject tmpItems = JSONObjectUtil.getJSONObject(keyInfo, "items");
		JSONArray searchOrder = new JSONArray();
		if(tmpOrder != null) searchOrder.addAll(tmpOrder);
		if(addOrder != null) searchOrder.addAll(addOrder);
		JSONObject keyItems = new JSONObject();
		if(tmpItems != null) keyItems.accumulateAll(tmpItems);

		// 탭항목 추가
		JSONObject tabCateItem = JSONObjectUtil.getJSONObject(keyItems, tabCateId);
		boolean isItemEmpty = JSONObjectUtil.isEmpty(tabCateItem);
		boolean isOrderEmpty = (JSONObjectUtil.indexOf(searchOrder, tabCateId) == -1);
		if((isItemEmpty || isOrderEmpty) && !StringUtil.isEmpty(tabCateId) && !StringUtil.isEmpty(tabCateColumn)) {
			if(isOrderEmpty) searchOrder.add(tabCateId);
			if(isItemEmpty) {
				tabCateItem = new JSONObject();
				tabCateItem.put("item_id", tabCateId);
				tabCateItem.put("column_id", tabCateColumn);
				tabCateItem.put("object_type", (tabCateObjectType == 22)?tabCateObjectType:2);
				tabCateItem.put("master_code", tabCateMasterCode);
				keyItems.put(tabCateId, tabCateItem);
			}
		}
		return getHashMapList(true, keyItems, searchOrder);
	}
	
	/**
	 * 마스터코드별 공통코드
	 * @param listSearch		: 검색항목 설정정보
	 * @param optnHashMap		: 이미 select한 마스터코드별 공통코드
	 * @return
	 */
	@Override
	public HashMap<String, Object> getHashMapList(JSONObject listSearch, HashMap<String, Object> optnHashMap) {
		JSONArray tmpOrder = JSONObjectUtil.getJSONArray(listSearch, "search_order");
		JSONArray addOrder = JSONObjectUtil.getJSONArray(listSearch, "searchAddOptn_order");
		JSONArray searchOrder = new JSONArray();
		if(tmpOrder != null) searchOrder.addAll(tmpOrder);
		if(addOrder != null) searchOrder.addAll(addOrder);
		JSONObject keyInfo = JSONObjectUtil.getJSONObject(listSearch, "key");
		JSONObject keyItems = JSONObjectUtil.getJSONObject(keyInfo, "items");
		return getHashMapList(true, keyItems, searchOrder, optnHashMap);
	}

	/**
	 * 마스터코드별 공통코드
	 * @param items				: 항목 설정 정보
	 * @param itemOrder			: 항목 목록
	 * @return
	 */
	@Override
	public HashMap<String, Object> getHashMapList(JSONObject items, JSONArray itemOrder) {
		return getHashMapList(true, items, itemOrder);
	}

	/**
	 * 마스터코드별 공통코드
	 * @param items				: 항목 설정 정보
	 * @param itemOrder			: 항목 목록
	 * @param optnHashMap		: 이미 select한 마스터코드별 공통코드
	 * @return
	 */
	@Override
	public HashMap<String, Object> getHashMapList(JSONObject items, JSONArray itemOrder, HashMap<String, Object> optnHashMap) {
		return getHashMapList(true, items, itemOrder, optnHashMap);
	}

	/**
	 * 마스터코드별 공통코드
	 * @param isMultiSBOptn		: object_type = 11 (multi select + button) && option 필요한 경우
	 * @param items				: 항목 설정 정보
	 * @param itemOrder			: 항목 목록
	 * @return
	 */
	@Override
	public HashMap<String, Object> getHashMapList(boolean isMultiSBOptn, JSONObject items, JSONArray itemOrder) {
		return getHashMapList(isMultiSBOptn, items, itemOrder, null);
	}

	/**
	 * 마스터코드별 공통코드
	 * @param isMultiSBOptn		: object_type = 11 (multi select + button) && option 필요한 경우
	 * @param items				: 항목 설정 정보
	 * @param itemOrder			: 항목 목록
	 * @param optnHashMap		: 이미 select한 마스터코드별 공통코드
	 * @return
	 */
	@Override
	public HashMap<String, Object> getHashMapList(boolean isMultiSBOptn, JSONObject items, JSONArray itemOrder, HashMap<String, Object> optnHashMap) {
		HashMap<String, Object> resultHashMap = new HashMap<String, Object>();
		if(!JSONObjectUtil.isEmpty(itemOrder) && !JSONObjectUtil.isEmpty(items)) {
			String admMode = RbsProperties.getProperty("Globals.site.mode.adm");
			String usrMode = RbsProperties.getProperty("Globals.site.mode.usr");
			
			// optionList masterCode
			List<String> optnMasterCodeList = new ArrayList<String>();
			// classOptionList searchList
			List<DTForm> classOptnSearchList = new ArrayList<DTForm>();
			
			int searchOrderSize = itemOrder.size();
			for(int i = 0 ; i < searchOrderSize ; i ++) {
				String itemId = JSONObjectUtil.getString(itemOrder, i);
				JSONObject item = JSONObjectUtil.getJSONObject(items, itemId);
				//String columnId = JSONObjectUtil.getString(item, "column_id");
				int formatType = JSONObjectUtil.getInt(item, "format_type");
				int objectType = JSONObjectUtil.getInt(item, "object_type");
				int optionType = JSONObjectUtil.getInt(item, "option_type");
				String masterCode = JSONObjectUtil.getString(item, "master_code");
				if(formatType == 21 || objectType >= 2 && objectType <= 5 || objectType == 11 || objectType == 14) {
					// multi select + button && option 필요없는 경우
					if(objectType == 11 && !isMultiSBOptn) continue;

					if(optionType == 0 && !StringUtil.isEmpty(masterCode) && optnMasterCodeList.indexOf(masterCode) == -1) {
						if(optnHashMap != null && optnHashMap.get(masterCode) != null) {
							resultHashMap.put(masterCode, optnHashMap.get(masterCode));
							continue;
						}
						optnMasterCodeList.add(masterCode);
					} else if(optionType == 1) {
						// 사용자유형
						if(optnHashMap != null && optnHashMap.get(itemId) != null) {
							resultHashMap.put(itemId, optnHashMap.get(itemId));
							continue;
						}
						Map<String, Object> param = new HashMap<String, Object>();
						List<DTForm> searchList = new ArrayList<DTForm>();
						int admMbrType = RbsProperties.getPropertyInt("Globals.code.USERTYPE_ADMIN");
						if(StringUtil.isEquals(masterCode, admMode)) {
							// 관리자
							searchList.add(new DTForm("A.USERTYPE_IDX", admMbrType/*Defines.CODE_USERTYPE_ADMIN*/, ">="));
						} else if(StringUtil.isEquals(masterCode, usrMode)) {
							// 일반회원
							searchList.add(new DTForm("A.USERTYPE_IDX", admMbrType/*Defines.CODE_USERTYPE_ADMIN*/, "<"));
						} else if(StringUtil.isEquals(masterCode, "org")) {
							// 기관회원
							int orgMMbrType = RbsProperties.getPropertyInt("Globals.code.USERTYPE_OMMEMBER");
							int orgAMbrType = RbsProperties.getPropertyInt("Globals.code.USERTYPE_OAMEMBER");
							searchList.add(new DTForm("A.USERTYPE_IDX", orgMMbrType, ">="));
							searchList.add(new DTForm("A.USERTYPE_IDX", orgAMbrType, "<="));
						}
						param.put("searchList", searchList);
						resultHashMap.put(itemId, codeOptnDAO.getUstpList(param));
					} else if(optionType == 2) {
						// 사용자그룹
						if(optnHashMap != null && optnHashMap.get(itemId) != null) {
							resultHashMap.put(itemId, optnHashMap.get(itemId));
							continue;
						}
						Map<String, Object> param = new HashMap<String, Object>();
						List<DTForm> searchList = new ArrayList<DTForm>();
						String areaGroupCode = RbsProperties.getProperty("Globals.code.GROUP_AREAMG");
						String[] masterCodes = StringUtil.stringToArray(masterCode, ",");
						if(StringUtil.indexOf(masterCodes, "exc_area") != -1) {
							if(!StringUtil.isEmpty(areaGroupCode)) {
								// 기관회원 제외
								searchList.add(new DTForm("A.GROUP_CODE", areaGroupCode, "<>"));
							}
							String areaMbGroupCode = RbsProperties.getProperty("Globals.code.GROUP_AREAMB");
							if(!StringUtil.isEmpty(areaGroupCode)) {
								// 매니저회원 제외
								searchList.add(new DTForm("A.GROUP_CODE", areaMbGroupCode, "<>"));
							}
						}
						param.put("searchList", searchList);
						resultHashMap.put(itemId, codeOptnDAO.getGrupList(param));
					} else if(optionType == 3) {
						// 사용자부서
						if(optnHashMap != null && optnHashMap.get(itemId) != null) {
							resultHashMap.put(itemId, optnHashMap.get(itemId));
							continue;
						}
						Map<String, Object> param = new HashMap<String, Object>();
						List<DTForm> searchList = new ArrayList<DTForm>();
						param.put("searchList", searchList);
						resultHashMap.put(itemId, codeOptnDAO.getDprtList(param));
					} else if(optionType == 4) {
						// 메뉴에서 사용하는 모듈
						if(optnHashMap != null && optnHashMap.get(itemId) != null) {
							resultHashMap.put(itemId, optnHashMap.get(itemId));
							continue;
						}
						Map<String, Object> param = new HashMap<String, Object>();
						List<DTForm> searchList = new ArrayList<DTForm>();
						param.put("searchList", searchList);
						resultHashMap.put(itemId, codeOptnDAO.getMenuModuleList(param));
					}
				} else if(objectType == 22 && resultHashMap.get("_class_" + masterCode) == null) {
					// 3차 select
					if(!StringUtil.isEmpty(masterCode)) {
						if(optnHashMap != null && optnHashMap.get("_class_" + masterCode) != null) {
							int maxLevel = JSONObjectUtil.getInt(item, "max_level");
							DataMap classMaxLevelDt = (DataMap)optnHashMap.get("_class_" + masterCode + "_max_level");
							if(maxLevel <= 0 || maxLevel == StringUtil.getInt(classMaxLevelDt.get("MAX_LEVEL"))) {
								resultHashMap.put("_class_" + masterCode + "_max_level", classMaxLevelDt);
								resultHashMap.put("_class_" + masterCode, optnHashMap.get("_class_" + masterCode));
								continue;
							}
						}
						int optnSearchSize = classOptnSearchList.size();
						if(optnSearchSize == 0) {
							classOptnSearchList.add(new DTForm("A.MASTER_CODE", masterCode, 0, null, "AND", "(", null));
						} else {
							classOptnSearchList.add(new DTForm("A.MASTER_CODE", masterCode, 0, null, "OR"));
						}
						int maxLevel = JSONObjectUtil.getInt(item, "max_level");
						if(maxLevel > 0) classOptnSearchList.add(new DTForm("A.CLASS_LEVEL", maxLevel, "<="));
				    	
						/*Map<String, Object> param = new HashMap<String, Object>();
						List<DTForm> searchList = new ArrayList<DTForm>();
				    	searchList.add(new DTForm("A.MASTER_CODE", masterCode));

						int maxLevel = JSONObjectUtil.getInt(item, "max_level");
						if(maxLevel > 0) searchList.add(new DTForm("A.CLASS_LEVEL", maxLevel, "<="));
						param.put("searchList", searchList);
						param.put("MASTER_CODE", masterCode);

						resultHashMap.put("_class_" + masterCode + "_max_level", codeOptnDAO.getClassMaxLevel(param));
						resultHashMap.put("_class_" + masterCode, codeOptnDAO.getClassOptnList(param));*/
					}
				}
			}
			
			// optionList setting
			int optnMasterCodeSize = optnMasterCodeList.size();
			if(optnMasterCodeSize > 0) {
				Map<String, Object> param = new HashMap<String, Object>();
				List<DTForm> searchList = new ArrayList<DTForm>();
		    	searchList.add(new DTForm("A.MASTER_CODE", optnMasterCodeList.toArray()));
				param.put("searchList", searchList);
				resultHashMap.putAll(codeOptnDAO.getOptnMapList(param));
			}
			
			// classOptionList setting
			int classOptnSearchSize = classOptnSearchList.size();
			if(classOptnSearchSize > 0) {
				Map<String, Object> param = new HashMap<String, Object>();
				DTForm dtForm = classOptnSearchList.get(classOptnSearchSize - 1);
				dtForm.put("columnPost", ")");
				param.put("searchList", classOptnSearchList);
				resultHashMap.putAll(codeOptnDAO.getClassMaxLevelOptnMap(param));
				resultHashMap.putAll(codeOptnDAO.getClassOptnMapList(param));
			}
		}
		
		return resultHashMap;
	}

	/**
	 * 마스터코드별 공통코드
	 * @param dt				: 공통코드 사용할 DataMap - 상세조회에서 사용
	 * @param items				: 항목 설정 정보
	 * @param itemOrder			: 항목 목록
	 * @return
	 */
	@Override
	public HashMap<String, Object> getHashMapList(DataMap dt, JSONObject items, JSONArray itemOrder) {
		if(dt == null) return null;
		HashMap<String, Object> resultHashMap = new HashMap<String, Object>();
		if(!JSONObjectUtil.isEmpty(itemOrder) && !JSONObjectUtil.isEmpty(items)) {
			String admMode = RbsProperties.getProperty("Globals.site.mode.adm");
			String usrMode = RbsProperties.getProperty("Globals.site.mode.usr");
			
			// optionList searchList
			List<DTForm> optnSearchList = new ArrayList<DTForm>();
			// classOptionList searchList
			List<DTForm> classOptnSearchList = new ArrayList<DTForm>();
			
			int itemOrderSize = itemOrder.size();
			for(int i = 0 ; i < itemOrderSize ; i ++) {
				String itemId = JSONObjectUtil.getString(itemOrder, i);
				JSONObject item = JSONObjectUtil.getJSONObject(items, itemId);
				int formatType = JSONObjectUtil.getInt(item, "format_type");
				int objectType = JSONObjectUtil.getInt(item, "object_type");
				int optionType = JSONObjectUtil.getInt(item, "option_type");
				String columnId = JSONObjectUtil.getString(item, "column_id");
				Object value = dt.get(columnId);
				if(StringUtil.isEmpty(value)) continue;

				String masterCode = JSONObjectUtil.getString(item, "master_code");
				if(formatType == 21 || objectType >= 2 && objectType <= 5 || objectType == 11 || objectType == 14) {
					// 년도 분기의 분기 / select / select multi /checkbox / radio / multi select +button / checkbox(단일)
					if(optionType == 0 && !StringUtil.isEmpty(masterCode) && resultHashMap.get(masterCode) == null) {
						if(formatType == 21) value = dt.get(columnId + "2");
						/*Map<String, Object> param = new HashMap<String, Object>();
						List<DTForm> searchList = new ArrayList<DTForm>();*/
						
						int optnSearchSize = optnSearchList.size();
						if(optnSearchSize == 0) optnSearchList.add(new DTForm("A.MASTER_CODE", masterCode, 0, null, "AND", "(", null));
						else optnSearchList.add(new DTForm("A.MASTER_CODE", masterCode, 0, null, "OR"));
				    	// select multi /checkbox / multi select +button 가 아닌 경우
				    	if(objectType != 3 && objectType != 4 && objectType != 11) optnSearchList.add(new DTForm("A.OPTION_CODE", value));
						/*param.put("searchList", searchList);
						resultHashMap.put(masterCode, codeOptnDAO.getOptnList(param));*/
					} else if(optionType == 1) {
						// 사용자유형
						Map<String, Object> param = new HashMap<String, Object>();
						List<DTForm> searchList = new ArrayList<DTForm>();
						int admMbrType = RbsProperties.getPropertyInt("Globals.code.USERTYPE_ADMIN");
						if(StringUtil.isEquals(masterCode, admMode)) {
							// 관리자
							searchList.add(new DTForm("A.USERTYPE_IDX", admMbrType/*Defines.CODE_USERTYPE_ADMIN*/, ">="));
						} else if(StringUtil.isEquals(masterCode, usrMode)) {
							// 일반회원
							searchList.add(new DTForm("A.USERTYPE_IDX", admMbrType/*Defines.CODE_USERTYPE_ADMIN*/, "<"));
						}
						param.put("searchList", searchList);
						resultHashMap.put(itemId, codeOptnDAO.getUstpList(param));
					} else if(optionType == 2) {
						// 사용자그룹
						Map<String, Object> param = new HashMap<String, Object>();
						List<DTForm> searchList = new ArrayList<DTForm>();
				    	//searchList.add(new DTForm("A.GRP_TYPE", masterCode));
						param.put("searchList", searchList);
						resultHashMap.put(itemId, codeOptnDAO.getGrupList(param));
					}
				} else if(objectType == 22 && resultHashMap.get("_class_" + masterCode) == null) {
					// 3차 select
					if(!StringUtil.isEmpty(masterCode)) {
						int optnSearchSize = classOptnSearchList.size();
						if(optnSearchSize == 0) {
							classOptnSearchList.add(new DTForm("A.MASTER_CODE", masterCode, 0, null, "AND", "(", null));
						} else {
							classOptnSearchList.add(new DTForm("A.MASTER_CODE", masterCode, 0, null, "OR"));
						}
						int maxLevel = JSONObjectUtil.getInt(item, "max_level");
						if(maxLevel > 0) classOptnSearchList.add(new DTForm("A.CLASS_LEVEL", maxLevel, "<="));
						/*
						Map<String, Object> param = new HashMap<String, Object>();
						List<DTForm> searchList = new ArrayList<DTForm>();
				    	searchList.add(new DTForm("A.MASTER_CODE", masterCode));
				    	searchList.add(new DTForm("A.CLASS_IDX", value));

						int maxLevel = JSONObjectUtil.getInt(item, "max_level");
						if(maxLevel > 0) searchList.add(new DTForm("A.CLASS_LEVEL", maxLevel, "<="));
						param.put("searchList", searchList);
						param.put("MASTER_CODE", masterCode);
						resultHashMap.put("_class_" + masterCode, codeOptnDAO.getClassOptnList(param));
						*/
					}
				}
			}
			
			// optionList setting
			int optnSearchSize = optnSearchList.size();
			if(optnSearchSize > 0) {
				Map<String, Object> param = new HashMap<String, Object>();
				DTForm dtForm = optnSearchList.get(optnSearchSize - 1);
				dtForm.put("columnPost", ")");
				param.put("searchList", optnSearchList);
				resultHashMap.putAll(codeOptnDAO.getOptnMapList(param));
			}
			
			// classOptionList setting
			int classOptnSearchSize = classOptnSearchList.size();
			if(classOptnSearchSize > 0) {
				Map<String, Object> param = new HashMap<String, Object>();
				DTForm dtForm = classOptnSearchList.get(classOptnSearchSize - 1);
				dtForm.put("columnPost", ")");
				param.put("searchList", classOptnSearchList);
				resultHashMap.putAll(codeOptnDAO.getClassMaxLevelOptnMap(param));
				resultHashMap.putAll(codeOptnDAO.getClassOptnMapList(param));
			}
		}
		
		return resultHashMap;
	}
}