package rbs.modules.module.service.impl;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.FileUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.egovframework.LoginVO;
import rbs.egovframework.schema.mapper.SchemaMapper;
import rbs.modules.module.mapper.ModuleFnItemMapper;
import rbs.modules.module.mapper.ModuleFnItemSMapper;
import rbs.modules.module.mapper.ModuleFnMapper;
import rbs.modules.module.mapper.ModuleFnSetMapper;
import rbs.modules.module.mapper.ModuleMapper;
import rbs.modules.module.service.ModuleFnService;

@Service("moduleFnService")
public class ModuleFnServiceImpl extends EgovAbstractServiceImpl implements ModuleFnService {

	@Resource(name="moduleFnMapper")
	private ModuleFnMapper moduleFnDAO;
	
	@Resource(name="moduleMapper")
	private ModuleMapper moduleDAO;
	
	@Resource(name="moduleFnSetMapper")
	private ModuleFnSetMapper moduleFnSetDAO;
	
	@Resource(name="moduleFnItemMapper")
	private ModuleFnItemMapper moduleFnItemDAO;
	
	@Resource(name="moduleFnItemSMapper")
	private ModuleFnItemSMapper moduleFnItemSDAO;
	
	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;

	@Override
	public List<Object> getOptnList(Map<String, Object> param) {
		return moduleFnDAO.getOptnList(param);
	}
	/**
	 * 전체 목록 수
	 * @param param
	 * @return
	 */
	@Override
	public int getTotalCount(Map<String, Object> param) {
    	return moduleFnDAO.getTotalCount(param);
	}

	/**
	 * 전체 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(Map<String, Object> param) {
		return moduleFnDAO.getList(param);
	}

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getView(Map<String, Object> param) {
		return moduleFnDAO.getView(param);
	}

	/**
	 * 수정 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getModify(Map<String, Object> param) {
		return moduleFnDAO.getModify(param);
	}
	
	public void createModuleTable(String moduleId, int fnIdx, String fnName, String designType){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		ApplicationContext act = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
		SchemaMapper schemaMapper = (SchemaMapper)act.getBean(moduleId + "SchemaMapper");
		
		if(schemaMapper != null) {
			Map<String, Object> createParam = new HashMap<String, Object>();
			createParam.put("fnIdx", fnIdx);
			createParam.put("fnName", fnName);
			schemaMapper.createTable(designType, createParam);
		}
	}
	
	/**
	 * 등록처리 : 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장   <br/>
	 * 기능 정보 <br/>
	 * 설정 정보  <br/>
	 * 항목 정보  <br/>
	 * 검색 항목 정보  <br/>
	 * 1. 설정관리하는 모듈인 경우 설정 data setting <br/>
	 * 2. 항목관리하는 모듈인 경우 항목 data setting <br/>
	 * 3. 기능정보(FN_INFO) 저장 <br/>
	 * 4. 설정정보(FN_SETTING_INFO) 저장 <br/>
	 * 5. 항목정보([모듈ID]_ITEM_INFO) 저장 <br/>
	 * 6. 검색항목정보([모듈ID]_ITEM_SEARCH_INFO) 저장 <br/>
	 * 7. table 생성 <br/>
	 * 8. 설정파일생성 <br/>
	 * 9. 항목파일생성 <br/>
	 * @param uploadModulePath
	 * @param moduleId
	 * @param regiIp
	 * @param parameterMap
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@Override
	public int insert(String uploadModulePath, String moduleId, String regiIp,
			ParamForm parameterMap, JSONObject items, JSONArray itemOrder)
			throws Exception {
		return insert(true, uploadModulePath, moduleId, regiIp, parameterMap, items, itemOrder);
	}
	@Override
	public int insert(boolean isCreateTable, String uploadModulePath, String moduleId, String regiIp,
			ParamForm parameterMap, JSONObject items, JSONArray itemOrder)
			throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		String fnName = StringUtil.getString(parameterMap.get("fnName"));
		String designType = StringUtil.getString(parameterMap.get("designType"));
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		List<DTForm> searchList = new ArrayList<DTForm>();
    	
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
		
		// 모듈정보 얻기
		Map<String, Object> moduleParam = new HashMap<String, Object>();
		List<DTForm> moduleSearchList = new ArrayList<DTForm>();
		moduleSearchList.add(new DTForm("A.MODULE_ID", moduleId));
		moduleParam.put("searchList", moduleSearchList);
		
		DataMap moduleDt = moduleDAO.getManageView(moduleParam);
		
		if(moduleDt == null) return -1;

		//고유아이디 셋팅
		searchList.add(new DTForm("A.MODULE_ID", moduleId));
		param.put("searchList", searchList);
		int fnIdx = moduleFnDAO.getNextId(param);
		
		// 설정관리, 항목관리 유무
		// 언어목록
		List<DTForm> langItemDataList = null;
		List<Object> langList = null;
		String langName = null;
		
		List<DTForm> settingDataList = null;
		List<DTForm> setItemDataList = null;
		
		int isSetManager = StringUtil.getInt(moduleDt.get("ISSETMANAGER"));
		int isItemManager = StringUtil.getInt(moduleDt.get("ISITEMMANAGER"));
		
		String confModule = StringUtil.getString(moduleDt.get("CONF_MODULE"));
		//String confSModule = null;
		if(StringUtil.isEmpty(confModule)) confModule = moduleId;
		
		if(isSetManager == 1) {
			// 설정관리 하는 모듈인 경우 : 설정 data setting
			JSONObject settingManagerObject = ModuleUtil.getModuleSettingManagerObject(confModule, null, designType);
			settingDataList = new ArrayList<DTForm>();
			settingDataList.add(new DTForm("MODULE_ID", moduleId));
			settingDataList.add(new DTForm("FN_IDX", fnIdx));
			
			settingDataList.add(new DTForm("REGI_IDX", loginMemberIdx));
			settingDataList.add(new DTForm("REGI_ID", loginMemberId));
			settingDataList.add(new DTForm("REGI_NAME", loginMemberName));
			settingDataList.add(new DTForm("REGI_IP", regiIp));
	    	
			settingDataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
			settingDataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
			settingDataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
			settingDataList.add(new DTForm("LAST_MODI_IP", regiIp));
			

			settingDataList.add(new DTForm("DESIGN_TYPE", designType));
			
			boolean isMemo = false;
			if(StringUtil.isEquals(designType, RbsProperties.getProperty("Globals.design.NAME_DESIGN_TYPE_MEMO"))) isMemo = true;
			
			JSONArray settingItemOrder = JSONObjectUtil.getJSONArray(settingManagerObject, "modifyproc_order");
			JSONObject settingItems = JSONObjectUtil.getJSONObject(settingManagerObject, "items");
			JSONObject settingItem = null;
			String settingColulmnId = null;
			for(Object itemObj:settingItemOrder) {
				String itemId = StringUtil.getString(itemObj);
				if(StringUtil.isEmpty(itemId)) continue;
				
				settingItem = JSONObjectUtil.getJSONObject(settingItems, itemId);
				settingColulmnId = JSONObjectUtil.getString(settingItem, "column_id");
				
				// 항목정보에 column_Id 없는 경우 : 항목정보 없는 경우 포함
				if(StringUtil.isEmpty(settingColulmnId)) continue;
				
				if(isMemo && StringUtil.isEquals(itemId, "use_memo")) {
					// 한 줄형인 경우
					settingDataList.add(new DTForm(settingColulmnId, 1));
				} else settingDataList.add(new DTForm(settingColulmnId, settingColulmnId, 1));
			}
			/*
			String key = null;
			Set set = JSONObjectUtil.getJSONObject(settingManagerObject, "items").keySet();
			Iterator keys = set.iterator();
			while(keys.hasNext()) {
				key = StringUtil.getString(keys.next()).toUpperCase();
				if(isMemo && StringUtil.isEquals(key, "USE_MEMO")) {
					// 한 줄형인 경우
					settingDataList.add(new DTForm(key, 1));
				} else settingDataList.add(new DTForm(key, key, 1));
			}*/
		}

		if(isItemManager == 1) {
			// 항목관리 하는 모듈인 경우 : 설정 data setting
			JSONObject itemManagerObject = ModuleUtil.getModuleItemManagerObject(confModule, null);
			setItemDataList = new ArrayList<DTForm>();
			setItemDataList.add(new DTForm("FN_IDX", fnIdx));
	    	
			setItemDataList.add(new DTForm("REGI_IDX", loginMemberIdx));
			setItemDataList.add(new DTForm("REGI_ID", loginMemberId));
			setItemDataList.add(new DTForm("REGI_NAME", loginMemberName));
			setItemDataList.add(new DTForm("REGI_IP", regiIp));
	    	
			setItemDataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
			setItemDataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
			setItemDataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
			setItemDataList.add(new DTForm("LAST_MODI_IP", regiIp));

			setItemDataList.add(new DTForm("ORDER_IDX", "ORDER_IDX", 1));
			setItemDataList.add(new DTForm("ISDB", "ISDB", 1));
			setItemDataList.add(new DTForm("DEFAULT_ITEM", "DEFAULT_ITEM", 1));

			// 언어목록
			langList = CodeHelper.getOptnList("LOCALE");
			langItemDataList = (langList != null)? new ArrayList<DTForm>():null;

			JSONArray itemsItemOrder = JSONObjectUtil.getJSONArray(itemManagerObject, "writeproc_order");
			JSONObject itemsItems = JSONObjectUtil.getJSONObject(itemManagerObject, "items");
			JSONObject itemsItem = null;
			String itemsColulmnId = null;
			for(Object itemObj:itemsItemOrder) {
				String itemId = StringUtil.getString(itemObj);
				if(StringUtil.isEmpty(itemId)) continue;

				if(StringUtil.isEquals(itemId, "closed") || 
						StringUtil.isEquals(itemId, "con_item_ids") || 
						StringUtil.isEquals(itemId, "con_required_size")) continue;
				
				itemsItem = JSONObjectUtil.getJSONObject(itemsItems, itemId);
				itemsColulmnId = JSONObjectUtil.getString(itemsItem, "column_id");
				
				setItemDataList.add(new DTForm(itemsColulmnId, itemsColulmnId, 1));
				
				if(StringUtil.isEquals(itemId, "item_name")) {
					for(Object langObj:langList) {
						if(langObj instanceof DataMap) {
							DataMap langDt = (DataMap)langObj;
							
							langName = StringUtil.getString(langDt.get("OPTION_CODE")).toUpperCase();
							langItemDataList.add(new DTForm(itemsColulmnId + "_" + langName, itemsColulmnId + "_" + langName, 1));
						}
					}
					if(langItemDataList != null) setItemDataList.addAll(langItemDataList);
				}
			}
		}

		// 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, items, itemOrder);

		if(dataMap == null || dataMap.size() == 0) return -1;
		
		@SuppressWarnings("unchecked")
		List<DTForm> itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// 기능 저장 항목
		dataList.add(new DTForm("MODULE_ID", moduleId));
    	dataList.add(new DTForm("FN_IDX", fnIdx));
    	
    	dataList.add(new DTForm("REGI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("REGI_ID", loginMemberId));
    	dataList.add(new DTForm("REGI_NAME", loginMemberName));
    	dataList.add(new DTForm("REGI_IP", regiIp));
    	
    	dataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));
		
    	// 1. 기능정보 저장
    	param.put("dataList", dataList);
    	param.put("moduleId", moduleId);
    	
		int result = moduleFnDAO.insert(param);
		
		if(settingDataList != null) {
			// 2. 설정정보 저장
			Map<String, Object> setParam = new HashMap<String, Object>();
			setParam.put("dataList", settingDataList);
			setParam.put("moduleId", moduleId);
			result = moduleFnSetDAO.insertInit(setParam);
		}
		if(setItemDataList != null) {
			// 3. 항목정보 저장
			Map<String, Object> itemParam = new HashMap<String, Object>();
			itemParam.put("dataList", setItemDataList);
			itemParam.put("designType", designType);
			itemParam.put("MODULE_ID", moduleId.toUpperCase());
			result = moduleFnItemDAO.insertInit(itemParam);
			
			// 4. 검색 항목정보 저장
			Map<String, Object> itemSParam = new HashMap<String, Object>();
			List<DTForm> itemSearchDataList = new ArrayList<DTForm>();
			itemSearchDataList.add(new DTForm("FN_IDX", fnIdx));
			itemSearchDataList.add(new DTForm("ITEM_ID", "ITEM_ID", 1));
			itemSearchDataList.add(new DTForm("SEARCH_TYPE", "SEARCH_TYPE", 1));
			itemSearchDataList.add(new DTForm("ORDER_IDX", "ORDER_IDX", 1));
			
			if(langItemDataList != null) itemSearchDataList.addAll(langItemDataList);
			itemSParam.put("dataList", itemSearchDataList);
			itemSParam.put("designType", designType);
			itemSParam.put("MODULE_ID", moduleId.toUpperCase());
			result = moduleFnItemSDAO.insertInit(itemSParam);
		}
		
		if(isCreateTable) {
			// table 생성
			createModuleTable(moduleId, fnIdx, fnName, designType);
			/*HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
			ApplicationContext act = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
			SchemaMapper schemaMapper = (SchemaMapper)act.getBean(moduleId + "SchemaMapper");
			
			if(schemaMapper != null) {
				Map<String, Object> createParam = new HashMap<String, Object>();
				createParam.put("fnIdx", fnIdx);
				createParam.put("fnName", fnName);
				schemaMapper.createTable(designType, createParam);
			}*/
		}
		
		String defaultPathName = StringUtil.getString(designType, "default");
		//if(settingDataList != null) {
		// 설정파일 생성 : 기본 설정파일에 기능명 setting
		String settingName = "setting_info";
		JSONObject defaultSettingObject = ModuleUtil.getModuleDefaultSettingObject(confModule, defaultPathName);
		JSONObject defaultSettingInfo = JSONObjectUtil.getJSONObject(defaultSettingObject, settingName);
		
		if(defaultSettingInfo != null) {
			JSONObject settingObject = new JSONObject();
			settingObject.accumulateAll(defaultSettingObject);
			JSONObject settingInfo = JSONObjectUtil.getJSONObject(settingObject, settingName);
			settingInfo.put("list_title", MessageFormat.format(rbsMessageSource.getMessage("item.itemId.setting.list.title"), fnName));
			settingInfo.put("input_title", MessageFormat.format(rbsMessageSource.getMessage("item.itemId.setting.input.title"), fnName));
			settingInfo.put("deleteList_title", MessageFormat.format(rbsMessageSource.getMessage("item.itemId.setting.deleteList.title"), fnName));

			settingObject.put(settingName, settingInfo);
			
			ModuleUtil.writeModuleSettingObject(confModule, fnIdx, settingObject);
			
			// 모듈별 전체 설정 파일 저장 시작  ------------------------------------------------------------------------
			String totalSObjName = "item" + fnIdx;
			JSONObject totalSettingObject = ModuleUtil.getModuleTotalSettingObject(confModule, false);
			
			// Q&A사용여부 전체 저장
			boolean isSetObj = true;
			if(totalSettingObject == null || totalSettingObject.isEmpty()) {
				totalSettingObject = new JSONObject();
				isSetObj = false;
			}
			
			JSONObject fnSettingObject = JSONObjectUtil.getJSONObject(totalSettingObject, totalSObjName);
			if(fnSettingObject == null || fnSettingObject.isEmpty()) {
				fnSettingObject = new JSONObject();
				isSetObj = false;
			}

			int useDIDate = JSONObjectUtil.getInt(settingInfo, "use_dldate");
			int useQNA = JSONObjectUtil.getInt(settingInfo, "use_qna");
			int usePrivate = JSONObjectUtil.getInt(settingInfo, "use_private");
			int useContList = JSONObjectUtil.getInt(settingInfo, "use_cont_list");
			int usePartType = JSONObjectUtil.getInt(settingInfo, "use_part_type");
			
			fnSettingObject.put("idx_name", settingInfo.get("idx_name"));
			fnSettingObject.put("idx_column", settingInfo.get("idx_column"));
			fnSettingObject.put("use_qna", useQNA);
			fnSettingObject.put("use_dldate", useDIDate);
			fnSettingObject.put("use_private", usePrivate);
			fnSettingObject.put("use_cont_list", useContList);
			fnSettingObject.put("use_part_type", usePartType);
			
			/*
			// 대표분류 전체 저장
			String dsetCateListId = JSONObjectUtil.getString(settingInfo, "dset_cate_list_id");
			if(!StringUtil.isEmpty(dsetCateListId)) {
				JSONObject cateListObject = JSONObjectUtil.getJSONObject(targetItems, dsetCateListId);
				String dsetCateListColumnId = JSONObjectUtil.getString(cateListObject, "column_id");
				String dsetCateListMsterCode = JSONObjectUtil.getString(cateListObject, "master_code");
				fnSettingObject.put("dset_cate_list_column_id", dsetCateListColumnId);
				fnSettingObject.put("dset_cate_list_master_code", dsetCateListMsterCode);
			} else if(isSetObj) {
				fnSettingObject.remove("dset_cate_list_column_id");
				fnSettingObject.remove("dset_cate_list_master_code");
			}*/
			
			totalSettingObject.put(totalSObjName, fnSettingObject);

			// 모듈별 전체 설정 파일 저장
			ModuleUtil.writeModuleTotalSettingObject(confModule, totalSettingObject);
			// 모듈별 전체 설정 파일  저장 종료 ------------------------------------------------------------------------
		}
		
		// 항목파일 생성
		String defaultItemPath = ModuleUtil.getModuleItemDPath(confModule, defaultPathName);
		String itemPath = ModuleUtil.getModuleItemPath(confModule, fnIdx);
		FileUtil.copy(defaultItemPath, itemPath);
		return fnIdx;
	}

	/**
	 * 수정처리 : 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param uploadModulePath
	 * @param moduleId
	 * @param brdIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int update(String uploadModulePath, String moduleId, int fnIdx, String regiIp,
			ParamForm parameterMap, JSONObject items, JSONArray itemOrder)
			throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		searchList.add(new DTForm("MODULE_ID", moduleId));
		searchList.add(new DTForm("FN_IDX", fnIdx));
		
		// 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, items, itemOrder);
		
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
    	param.put("moduleId", moduleId);
    	
		int result = moduleFnDAO.update(param);
				
		// 3. file 삭제
		List deleteFileList = StringUtil.getList(dataMap.get("deleteFileList"));
		if(deleteFileList != null) {
			FileUtil.isDelete(fileRealPath, deleteFileList);
		}
		
		return result;
	}
	
	/**
	 * 적용처리<br/>
	 * 1. 모듈정보 select : 설정정보 관리여부, 항목정보 관리여부<br/>
	 * 2. 설정정보<br/>
	 * 3. 항목정보<br/>
	 * 3.1 기본항목정보<br/>
	 * 3.2 항목검색 항목정보<br/>
	 * 3.3 선택검색 항목정보<br/>
	 * 3.4 DB에서 완전삭제할 항목정보   : isdelete = '2' -> delete 및 drop<br/>
	 * 3.5 json파일에서 삭제할 항목정보 : isdelete <> '0' -> 삭제<br/>
	 * 4. DB data 저장<br/>
	 * 4.1 기능 적용여부 저장 : fn_info - isapply = '1'<br/>
	 * 5. 파일저장<br/>
	 * 5.1 항목정보 저장<br/>
	 * 5.1.1 항목검색항목 저장<br/>
	 * 5.1.2 선택검색항목 저장<br/>
	 * 5.1.3 기본항목저장<br/>
	 * 5.1.4 생성/수정 컬럼정보 저장<br/>
	 * 5.1.4 json파일에서 삭제할 항목 삭제<br/>
	 * 6. DB에서 완전삭제할 항목정보 삭제<br/>
	 * 6.1 data 삭제<br/>
	 * 6.2 DB 컬럼 add/modify/drop<br/>
	 * 6.3 json파일에서 삭제<br/>
	 * 7. 항목파일 저장<br/>
	 * 8. 설정파일 저장
	 * @param moduleId
	 * @param fnIdx
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	@Override
	public int apply(String moduleId, int mfIdx, String regiIp)
			throws Exception {
		
		// 1. 모듈정보 select : 설정정보 관리여부, 항목정보 관리여부
		DataMap moduleDt = getModuleInfo(moduleId);
		if(moduleDt == null) return -1;
		
		int result = 0;
		
		int isSetManager = StringUtil.getInt(moduleDt.get("ISSETMANAGER"));
		int isItemManager = StringUtil.getInt(moduleDt.get("ISITEMMANAGER"));
		String confModule = StringUtil.getString(moduleDt.get("CONF_MODULE"));
		if(StringUtil.isEmpty(confModule)) confModule = moduleId;

		DataMap settingDt = null;							// 설정정보
		List<Object> itemList = null;						// 기본 항목정보
		List<Object> searchSItemList = null;				// 선택검색 항목정보
		List<Object> searchIItemList = null;				// 항목검색 항목정보
		
		List<Object> dbDropItemList = null;					// DB에서 완전삭제할 항목정보     : isdelete = '2' -> delete 및 drop
		List<Object> fileDelItemList = null;				// json파일에서 삭제할 항목정보 : isdelete = '1' -> closed '1'로 setting

		List<String[]> addColumnList = new ArrayList<String[]>();	// 추가할 컬럼정보
		List<String[]> modifyColumnList = new ArrayList<String[]>();// 수정할 컬럼정보
		List<String[]> modifyCommentList = new ArrayList<String[]>();// comment만 수정할 컬럼정보

		// 설정관리 정보
		JSONObject settingManagerObject = null;				// 설정관리 관리항목정보
		JSONObject targetSetObject = null;					// 설정관리 정보
		// 항목관리 정보
		JSONObject itemManagerObject = null;					// 항목관리 관리항목정보
		JSONObject targetItemObject = null;					// 항목관리 정보
		JSONObject targetItemInfo = null;					// item_info
		JSONObject targetItems = null;						// item_info.items
		JSONArray targetListOrder = null;					// item_info.list_order
		JSONArray targetViewOrder = null;					// item_info.view_order
		JSONArray targetWriteOrder = null;					// item_info.write_order
		JSONArray targetWriteProcOrder = null;				// item_info.writeproc_order
		JSONArray targetModifyOrder = null;					// item_info.modify_order
		JSONArray targetModifyProcOrder = null;				// item_info.modifyproc_order
		JSONArray targetReplyOrder = null;					// item_info.reply_order
		JSONArray targetReplyProcOrder = null;				// item_info.replyproc_order
		
		// 검색항목 정보
		JSONObject targetListSearch = null;					// list_search
		
		JSONObject targetLSKeyField = null;					// list_search.keyField	
		JSONObject targetLSKeyFieldItems = null;			// list_search.keyField.items
		JSONArray targetLSKeyFieldOrder = null;				// list_search.keyField.search_order
		
		JSONObject targetLSKey = null;						// list_search.key
		JSONObject targetLSKeyItems = null;					// list_search.key.items
		
		JSONArray targetLSSearchFormOrder = null;			// list_search.searchForm_order
		JSONArray targetLSSearchOrder = null;				// list_search.search_order
		
		// 2. 설정정보
		if(isSetManager == 1) {
			// 설정관리하는 경우
			
			Map<String, Object> param = new HashMap<String, Object>();
			List<DTForm> searchList = new ArrayList<DTForm>();

			searchList.add(new DTForm("A.MODULE_ID", moduleId));
			searchList.add(new DTForm("A.FN_IDX", mfIdx));
			param.put("searchList", searchList);
			
			settingDt = getSettingModify(param);
		}
		
		// 3. 항목정보
		if(isItemManager == 1) {
			// 항목관리하는 경우
			
			// 3.1 기본 항목정보
			Map<String, Object> param = new HashMap<String, Object>();
			List<DTForm> searchList = new ArrayList<DTForm>();
			searchList.add(new DTForm("A.FN_IDX", mfIdx));
			searchList.add(new DTForm("A.ISDELETE", "0"));

	    	param.put("MODULE_ID", moduleId.toUpperCase());
			param.put("searchList", searchList);
			
			itemList = moduleFnItemDAO.getTotalList(param);
			
			// 3.2 항목검색 항목정보
			param = new HashMap<String, Object>();
			searchList = new ArrayList<DTForm>();
			searchList.add(new DTForm("ST.FN_IDX", mfIdx));
			searchList.add(new DTForm("ST.SEARCH_TYPE", "1"));

	    	param.put("MODULE_ID", moduleId.toUpperCase());
			param.put("searchList", searchList);
			
			searchIItemList = moduleFnItemSDAO.getList(param);
			
			// 3.3 선택검색 항목정보
			param = new HashMap<String, Object>();
			searchList = new ArrayList<DTForm>();
			searchList.add(new DTForm("ST.FN_IDX", mfIdx));
			searchList.add(new DTForm("ST.SEARCH_TYPE", "2"));

	    	param.put("MODULE_ID", moduleId.toUpperCase());
			param.put("searchList", searchList);
			
			searchSItemList = moduleFnItemSDAO.getList(param);
			
			// 3.4 DB에서 완전삭제할 항목정보     : isdelete = '2' -> delete 및 drop
			param = new HashMap<String, Object>();
			searchList = new ArrayList<DTForm>();
			searchList.add(new DTForm("A.FN_IDX", mfIdx));
			searchList.add(new DTForm("A.ISDELETE", "2"));

	    	param.put("MODULE_ID", moduleId.toUpperCase());
			param.put("searchList", searchList);
			
			dbDropItemList = moduleFnItemDAO.getTotalList(param);
			
			// 3.5 json파일에서 삭제할 항목정보 : isdelete <> '0' -> 삭제
			param = new HashMap<String, Object>();
			searchList = new ArrayList<DTForm>();
			searchList.add(new DTForm("A.FN_IDX", mfIdx));
			searchList.add(new DTForm("A.ISDELETE", "0", "<>"));

	    	param.put("MODULE_ID", moduleId.toUpperCase());
			param.put("searchList", searchList);
			
			fileDelItemList = moduleFnItemDAO.getTotalList(param);
		}

		// 4. DB data 저장
		// 4.1 기능 적용여부 저장 : fn_info - isapply = '1'
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("MODULE_ID", moduleId));
		searchList.add(new DTForm("FN_IDX", mfIdx));
		List<DTForm> fnDataList = new ArrayList<DTForm>();
		fnDataList.add(new DTForm("ISAPPLY", "1"));
    	param.put("dataList", fnDataList);
    	param.put("searchList", searchList);
    	result = moduleFnDAO.update(param);

		// 5. 파일저장
		// 5.1 항목정보 저장
		if(isItemManager == 1) {
			// 항목관리하는 경우
			
			// 항목정보 얻기 (설정파일)
			List<Object> langList = CodeHelper.getOptnList("LOCALE");			// 언어목록
			targetItemObject = ModuleUtil.getModuleItemObject(confModule, mfIdx, false);
			if(!JSONObjectUtil.isEmpty(targetItemObject)) {

				
				
				targetItemInfo = JSONObjectUtil.getJSONObject(targetItemObject, "item_info");
				if(!JSONObjectUtil.isEmpty(targetItemInfo)) {
					// 기본항목
					targetItems = JSONObjectUtil.getJSONObject(targetItemInfo, "items");
					
					// 검색항목
					targetListSearch = JSONObjectUtil.getJSONObject(targetItemInfo, "list_search");
					if(!JSONObjectUtil.isEmpty(targetListSearch)) {
						// 입력항목
						targetLSKey = JSONObjectUtil.getJSONObject(targetListSearch, "key");
						if(!JSONObjectUtil.isEmpty(targetLSKey)) {
							targetLSKeyItems = JSONObjectUtil.getJSONObject(targetLSKey, "items");
						}
						
						// 선택항목
						targetLSKeyField = JSONObjectUtil.getJSONObject(targetListSearch, "keyField");
						if(!JSONObjectUtil.isEmpty(targetLSKeyField)) {
							targetLSKeyFieldItems = JSONObjectUtil.getJSONObject(targetLSKeyField, "items");
						}
					}
				}
			}
			
			// 기본항목정보 생성
			if(JSONObjectUtil.isEmpty(targetItemObject)) targetItemObject = new JSONObject();
			if(JSONObjectUtil.isEmpty(targetItemInfo)) targetItemInfo = new JSONObject();
			if(JSONObjectUtil.isEmpty(targetItems)) targetItems = new JSONObject();

			// 검색항목 순서 생성
			targetLSSearchFormOrder = new JSONArray();
			targetLSSearchOrder = new JSONArray();
			
			if(searchIItemList != null || searchSItemList != null) {
				if(JSONObjectUtil.isEmpty(targetListSearch)) targetListSearch = new JSONObject();
			}

			// 5.1.1 항목검색항목 저장
			if(searchIItemList != null) {
				if(JSONObjectUtil.isEmpty(targetLSKey)) targetLSKey = new JSONObject();
				if(JSONObjectUtil.isEmpty(targetLSKeyItems)) targetLSKeyItems = new JSONObject();
				
				String itemId = null;
				String itemName = null;
				String columnId = null;
				String masterCode = null;
				int formatType = 0;
				int objectType = 0;
				String langName = null;
				//String styleWidth = null;
				DataMap seachItemDt = null;
				for(Object searchItemObj:searchIItemList) {
					if (searchItemObj instanceof DataMap) {
						seachItemDt = (DataMap) searchItemObj;
						itemId = StringUtil.getString(seachItemDt.get("ITEM_ID"));
						itemName = StringUtil.getString(seachItemDt.get("ITEM_NAME"));
						columnId = StringUtil.getString(seachItemDt.get("COLUMN_ID"));
						masterCode = StringUtil.getString(seachItemDt.get("MASTER_CODE"));
						formatType = StringUtil.getInt(seachItemDt.get("FORMAT_TYPE"));
						objectType = StringUtil.getInt(seachItemDt.get("OBJECT_TYPE"));
						
						JSONObject keyItem = new JSONObject();
						keyItem.put("item_id", itemId);
						keyItem.put("item_name", itemName);
						keyItem.put("column_id", columnId);
						keyItem.put("master_code", masterCode);
						keyItem.put("format_type", formatType);
						keyItem.put("object_type", objectType);
						keyItem.put("start_year", StringUtil.getInt(seachItemDt.get("START_YEAR")));
						keyItem.put("end_addcnt", StringUtil.getInt(seachItemDt.get("END_ADDCNT")));
						
						for(Object langObj:langList) {
							if(langObj instanceof DataMap) {
								DataMap langDt = (DataMap)langObj;
								
								langName = StringUtil.getString(langDt.get("OPTION_CODE")).toUpperCase();
								keyItem.put("item_name_" + StringUtil.getString(langDt.get("OPTION_CODE")), StringUtil.getString(seachItemDt.get("ITEM_NAME_" + langName)));
							}
						}
						targetLSKeyItems.put(itemId, keyItem);

						// 검색항목 순서에 저장 
						targetLSSearchFormOrder.add(itemId);
						targetLSSearchOrder.add(itemId);
					}
				}

				// 항목검색항목 정보 저장
				targetLSKey.put("items", targetLSKeyItems);
			}

			// 5.1.2 선택검색항목 저장
			if(searchSItemList != null) {
				if(JSONObjectUtil.isEmpty(targetLSKeyField)) targetLSKeyField = new JSONObject();
				if(JSONObjectUtil.isEmpty(targetLSKeyFieldItems)) targetLSKeyFieldItems = new JSONObject();
				targetLSKeyFieldOrder = new JSONArray();
				
				String itemId = null;
				String itemName = null;
				String columnId = null;
				/*String masterCode = null;
				int formatType = 0;
				int objectType = 0;*/
				String langName = null;
				//String styleWidth = null;
				DataMap seachItemDt = null;
				for(Object searchItemObj:searchSItemList) {
					if (searchItemObj instanceof DataMap) {
						seachItemDt = (DataMap) searchItemObj;
						itemId = StringUtil.getString(seachItemDt.get("ITEM_ID"));
						itemName = StringUtil.getString(seachItemDt.get("ITEM_NAME"));
						columnId = StringUtil.getString(seachItemDt.get("COLUMN_ID"));
						/*masterCode = StringUtil.getString(seachItemDt.get("MASTER_CODE"));
						formatType = StringUtil.getInt(seachItemDt.get("FORMAT_TYPE"));
						objectType = StringUtil.getInt(seachItemDt.get("OBJECT_TYPE"));*/
						
						JSONObject keyItem = new JSONObject();
						keyItem.put("item_id", itemId);
						keyItem.put("item_name", itemName);
						keyItem.put("column_id", columnId);
						
						for(Object langObj:langList) {
							if(langObj instanceof DataMap) {
								DataMap langDt = (DataMap)langObj;
								
								langName = StringUtil.getString(langDt.get("OPTION_CODE")).toUpperCase();
								keyItem.put("item_name_" + StringUtil.getString(langDt.get("OPTION_CODE")), StringUtil.getString(seachItemDt.get("ITEM_NAME_" + langName)));
							}
						}
						targetLSKeyFieldItems.put(itemId, keyItem);
						targetLSKeyFieldOrder.add(itemId);
					}
				}
				
				// 검색항목 순서에 저장 
				targetLSSearchFormOrder.add("keyField");
				targetLSSearchOrder.add("keyField");

				// 선택검색항목 정보 저장
				targetLSKeyField.put("items", targetLSKeyFieldItems);
				// 선택검색항목 순서 저장
				targetLSKeyField.put("select_order", targetLSKeyFieldOrder);
			}

			if(searchIItemList != null || searchSItemList != null) {
				// 항목검색항목 저장
				targetListSearch.put("key",  targetLSKey);
				// 선택검색항목 저장
				targetListSearch.put("keyField",  targetLSKeyField);
				
				// 검색항목 순서 저장
				targetListSearch.put("searchForm_order", targetLSSearchFormOrder);
				targetListSearch.put("search_order", targetLSSearchOrder);
			}
			
			// 5.1.3 기본항목저장
			if(itemList != null) {

				itemManagerObject = ModuleUtil.getModuleItemManagerObject(confModule, null);
				if(!JSONObjectUtil.isEmpty(itemManagerObject)) {
					// 항목관리 관리항목정보가 있는 경우
					JSONObject manageItems = JSONObjectUtil.getJSONObject(itemManagerObject, "items");
					JSONArray manageItemOrder = JSONObjectUtil.getJSONArray(itemManagerObject, "writeproc_order");
					JSONObject manageItem = null;
					String manageItemId = null;
					String manageColumnId = null;
					String manageItemType = null;
					
					int useList = 0;
					int useView = 0;
					int useWrite = 0;
					int useModify = 0;
					int useReply = 0;
					
					int isDB = 0;
					
					targetListOrder = new JSONArray();
					targetViewOrder = new JSONArray();
					targetWriteOrder = new JSONArray();
					targetWriteProcOrder = new JSONArray();
					targetModifyOrder = new JSONArray();
					targetModifyProcOrder = new JSONArray();
					targetReplyOrder = new JSONArray();
					targetReplyProcOrder = new JSONArray();
					
					String itemId = null;
					String itemName = null;
					String columnId = null;
					int maximum = 0;
					String itemType = null;
					String defaultValue = null;
					//String masterCode = null;
					int formatType = 0;
					int objectType = 0;
					int writeType = 0;
					int modifyType = 0;
					String langName = null;
					DataMap itemDt = null;
					for(Object itemObj:itemList) {
						if (itemObj instanceof DataMap) {
							itemDt = (DataMap) itemObj;
							itemId = StringUtil.getString(itemDt.get("ITEM_ID"));
							itemName = StringUtil.getString(itemDt.get("ITEM_NAME"));
							columnId = StringUtil.getString(itemDt.get("COLUMN_ID"));
							maximum = StringUtil.getInt(itemDt.get("MAXIMUM"));
							//masterCode = StringUtil.getString(itemDt.get("MASTER_CODE"));
							formatType = StringUtil.getInt(itemDt.get("FORMAT_TYPE"));
							objectType = StringUtil.getInt(itemDt.get("OBJECT_TYPE"));
							itemType = StringUtil.getString(itemDt.get("ITEM_TYPE"));
							defaultValue = StringUtil.getString(itemDt.get("DEFAULT_VALUE"));
							
							// 항목관리 관리항목정보에 따른 항목저장
							JSONObject item = JSONObjectUtil.getJSONObject(targetItems, itemId);
							if(JSONObjectUtil.isEmpty(item)) item = new JSONObject();
							for(Object itemIdObj:manageItemOrder) {
								if (itemIdObj instanceof String) {
									manageItemId = (String) itemIdObj;
									manageItem = JSONObjectUtil.getJSONObject(manageItems, manageItemId);
									manageColumnId = JSONObjectUtil.getString(manageItem, "column_id");
									manageItemType = JSONObjectUtil.getString(manageItem, "item_type");
									
									if(StringUtil.isEquals(manageItemType, "1")) item.put(manageItemId, StringUtil.getInt(itemDt.get(manageColumnId)));
									else item.put(manageItemId, itemDt.get(manageColumnId));
								}
							}
							
							// 언어별 항목명 저장
							for(Object langObj:langList) {
								if(langObj instanceof DataMap) {
									DataMap langDt = (DataMap)langObj;
									
									langName = StringUtil.getString(langDt.get("OPTION_CODE")).toUpperCase();
									item.put("item_name_" + StringUtil.getString(langDt.get("OPTION_CODE")), StringUtil.getString(itemDt.get("ITEM_NAME_" + langName)));
								}
							}
							
							// 항목정보 저장
							targetItems.put(itemId, item);
	
							// 항목순서 저장
							useList = StringUtil.getInt(itemDt.get("USE_LIST"));
							useView = StringUtil.getInt(itemDt.get("USE_VIEW"));
							useWrite = StringUtil.getInt(itemDt.get("USE_WRITE"));
							useModify = StringUtil.getInt(itemDt.get("USE_MODIFY"));
							useReply = StringUtil.getInt(itemDt.get("USE_REPLY"));
							
							if(useList == 1) targetListOrder.add(itemId);
							if(useView == 1) targetViewOrder.add(itemId);
							if(useWrite == 1) {
								targetWriteOrder.add(itemId);
								targetWriteProcOrder.add(itemId);
							}
							if(useModify == 1) {
								targetModifyOrder.add(itemId);
								if(modifyType != 10) targetModifyProcOrder.add(itemId);
							}
							if(useReply == 1) {
								targetReplyOrder.add(itemId);
								targetReplyProcOrder.add(itemId);
							}
							
							// DB컬럼 add/modify할 목록 setting
							isDB = StringUtil.getInt(itemDt.get("ISDB"));
							if(isDB == 0) {
								// DB항목생성
								addColumnList.addAll(ModuleUtil.getAddColumnList(columnId, itemName, maximum, formatType, objectType, itemType, 0, defaultValue));
								//addColumnList.addAll(ModuleUtil.getAddColumnList(itemDt.getString("column_id"), itemDt.getInt("maximum"), formatType, objectType, itemDt.getInt("option_type"), itemDt.getString("default_value")));
							} else if(isDB == 2 && formatType == 0 && (objectType == 0 || objectType == 1 || objectType == 7)) {
								// DB항목수정
								if(formatType == 0 && (objectType == 0 || objectType == 1 || objectType == 101)) {
									modifyColumnList.addAll(ModuleUtil.getAddColumnList(columnId, itemName, maximum, formatType, objectType, itemType, 0, defaultValue));
									//modifyColumnList.addAll(ModuleUtil.getAddColumnList(itemDt.getString("column_id"), itemDt.getInt("maximum"), formatType, objectType, itemDt.getInt("option_type"), itemDt.getString("default_value")));
								} else {
									modifyCommentList.addAll(ModuleUtil.getAddColumnList(columnId, itemName, maximum, formatType, objectType, itemType, 0, defaultValue));
								}
							}
						}
					}
				}
				
				// 5.1.4 json파일에서 삭제할 항목 삭제
				if(fileDelItemList != null) {
					String delItemId = null;
					DataMap delItemDt = null;
					for(Object delItemObj:fileDelItemList) {
						if (delItemObj instanceof DataMap) {
							delItemDt = (DataMap) delItemObj;
							delItemId = StringUtil.getString(delItemDt.get("ITEM_ID"));
							
							targetItems.remove(delItemId);
						}
					}
				}
			}

			// 6. DB 정보 처리
			String tableModuleId = moduleId.toUpperCase();
			param = new HashMap<String, Object>();									// mapper parameter 데이터
			searchList = new ArrayList<DTForm>();									// 검색조건
			List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
			searchList.add(new DTForm("FN_IDX", mfIdx));
			searchList.add(new DTForm("ISDELETE", "0"));
			searchList.add(new DTForm("ISDB", "1", "<>"));
			
			dataList.add(new DTForm("ISDB", "1"));

	    	param.put("dataList", dataList);
	    	param.put("searchList", searchList);
	    	param.put("MODULE_ID", tableModuleId);
			result = moduleFnItemDAO.update(param);
			
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
			ApplicationContext act = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
			SchemaMapper schemaMapper = (SchemaMapper)act.getBean(moduleId + "SchemaMapper");
			
			if(dbDropItemList != null) {
				
				// 6.1 DB에서 완전삭제할 항목정보 data 삭제
				param = new HashMap<String, Object>();
				searchList = new ArrayList<DTForm>();
				searchList.add(new DTForm("FN_IDX", mfIdx));
				searchList.add(new DTForm("ISDELETE", "2"));

		    	param.put("MODULE_ID", moduleId.toUpperCase());
				param.put("searchList", searchList);
				
				result = moduleFnItemDAO.cdelete(param);
				

				// 6.2 DB 컬럼 drop
				int result1 = 0;
				if(schemaMapper != null && dbDropItemList != null) {
		
					// 2 컬럼 drop
					List<String> dropColumnList = ModuleUtil.getDropColumnList(dbDropItemList);
					for(String dropColumnId : dropColumnList) {
						Map<String, Object> dropParam = new HashMap<String, Object>();
						dropParam.put("fnIdx", mfIdx);
						dropParam.put("columnName", dropColumnId);
						result1 = schemaMapper.dropColumn(dropParam);
					}
		
					// 6.3 json파일에서 삭제
					DataMap dropItemDt = null;
					for(Object dropItemObj : dbDropItemList) {
						if(dropItemObj instanceof DataMap) {
							dropItemDt = (DataMap) dropItemObj;
							targetItems.remove(StringUtil.getString(dropItemDt.get("ITEM_ID")));
						}
					}
				}
			}
			
			// 6.3 DB 컬럼 add/modify
			// add
			int result1 = 0;
			if(addColumnList != null) {
				for(String[] columnInfo : addColumnList) {
					Map<String, Object> addParam = new HashMap<String, Object>();
					addParam.put("fnIdx", mfIdx);
					addParam.put("columnName", columnInfo[0]);
					addParam.put("dataType", columnInfo[1]);
					if(!StringUtil.isEmpty(columnInfo[2]) && !StringUtil.isEquals(columnInfo[2], "0")) addParam.put("dataLength", columnInfo[2]);
					//if(!StringUtil.isEmpty(columnInfo[2]) && !StringUtil.isEquals(columnInfo[2], "0")) addParam.put("dataLength", Integer.parseInt(columnInfo[2]));
					if(!StringUtil.isEmpty(columnInfo[3])) addParam.put("defaultValue", columnInfo[3]);
					addParam.put("comment", columnInfo[4]);
					result1 = schemaMapper.addColumn(addParam);
				}
			}

			// modify
			if(modifyColumnList != null) {
				for(String[] columnInfo : modifyColumnList) {
					Map<String, Object> addParam = new HashMap<String, Object>();
					addParam.put("fnIdx", mfIdx);
					addParam.put("columnName", columnInfo[0]);
					addParam.put("dataType", columnInfo[1]);
					if(!StringUtil.isEmpty(columnInfo[2]) && !StringUtil.isEquals(columnInfo[2], "0")) addParam.put("dataLength", Integer.parseInt(columnInfo[2]));
					if(!StringUtil.isEmpty(columnInfo[3])) addParam.put("defaultValue", columnInfo[3]);
					addParam.put("comment", columnInfo[4]);
					result1 = schemaMapper.modifyColumn(addParam);
				}
			}

			// comment 수정
			if(modifyCommentList != null) {
				for(String[] columnInfo : modifyCommentList) {
					Map<String, Object> addParam = new HashMap<String, Object>();
					addParam.put("fnIdx", mfIdx);
					addParam.put("columnName", columnInfo[0]);
					addParam.put("comment", columnInfo[4]);
					result1 = schemaMapper.modifyComment(addParam);
				}
			}
			
			// 7. 항목파일 저장
			targetItemInfo.put("items", targetItems);
			if(targetListOrder != null) targetItemInfo.put("list_order", targetListOrder);
			else targetItemInfo.remove("list_order");
			if(targetViewOrder != null) targetItemInfo.put("view_order", targetViewOrder);
			else targetItemInfo.remove("view_order");
			if(targetWriteOrder != null) targetItemInfo.put("write_order", targetWriteOrder);
			else targetItemInfo.remove("write_order");
			if(targetWriteProcOrder != null) targetItemInfo.put("writeproc_order", targetWriteProcOrder);
			else targetItemInfo.remove("writeproc_order");
			if(targetModifyOrder != null) targetItemInfo.put("modify_order", targetModifyOrder);
			else targetItemInfo.remove("modify_order");
			if(targetModifyProcOrder != null) targetItemInfo.put("modifyproc_order", targetModifyProcOrder);
			else targetItemInfo.remove("modifyproc_order");
			if(targetReplyOrder != null) targetItemInfo.put("reply_order", targetReplyOrder);
			else targetItemInfo.remove("reply_order");
			if(targetReplyProcOrder != null) targetItemInfo.put("replyproc_order", targetReplyProcOrder);
			else targetItemInfo.remove("replyproc_order");

			if(targetListSearch != null) targetItemInfo.put("list_search", targetListSearch);
			
			targetItemObject.put("item_info", targetItemInfo);

			// 항목정보 파일 저장
			ModuleUtil.writeModuleItemObject(confModule, mfIdx, targetItemObject);
		} else {
			// 항목관리 하지 않는 모듈인 경우
			String defaultCateId = null;
			if(StringUtil.isEquals(moduleId, RbsProperties.getProperty("Globals.design.NAME_MODULE_ID_TREBOOK"))) defaultCateId = "category";
			int useMultiCate = StringUtil.getInt(settingDt.get("USE_MULTI_CATE"));
			String dsetCateId = StringUtil.getString(settingDt.get("DSET_CATE_ID"), defaultCateId);

			String dsetCateMasterCode = StringUtil.getString(settingDt.get("DSET_CATE_MASTER_CODE"));

			String dsetCateTabId = StringUtil.getString(settingDt.get("DSET_CATE_TAB_ID"));
			if(!StringUtil.isEmpty(dsetCateId) && !StringUtil.isEmpty(dsetCateTabId)) {
				useMultiCate = 1;
				dsetCateMasterCode = dsetCateTabId;
			}
			
			if(useMultiCate == 1 && !StringUtil.isEmpty(dsetCateId) && !StringUtil.isEmpty(dsetCateMasterCode)) {
				// 3차분류 사용하는 경우
				
				// 항목정보 얻기 (설정파일)
				targetItemObject = ModuleUtil.getModuleItemObject(confModule, mfIdx, false);
				if(!JSONObjectUtil.isEmpty(targetItemObject)) {

					targetItemInfo = JSONObjectUtil.getJSONObject(targetItemObject, "item_info");
					if(!JSONObjectUtil.isEmpty(targetItemInfo)) {
						// 기본항목
						targetItems = JSONObjectUtil.getJSONObject(targetItemInfo, "items");
						// 검색항목
						targetListSearch = JSONObjectUtil.getJSONObject(targetItemInfo, "list_search");
						if(!JSONObjectUtil.isEmpty(targetListSearch)) {
							// 입력항목
							targetLSKey = JSONObjectUtil.getJSONObject(targetListSearch, "key");
							if(!JSONObjectUtil.isEmpty(targetLSKey)) {
								targetLSKeyItems = JSONObjectUtil.getJSONObject(targetLSKey, "items");
							}
						}
				
						// 기본항목정보 3차 분류 master_code setting
						if(!JSONObjectUtil.isEmpty(targetItems)) {
							JSONObject targetItem = JSONObjectUtil.getJSONObject(targetItems, dsetCateId);
							if(targetItem != null) {
								targetItem.put("master_code", dsetCateMasterCode);
								targetItems.put(dsetCateId, targetItem);
								targetItemInfo.put("items", targetItems);
							}
							
						}
				
						// 검색항목 3차 분류 master_code setting
						if(!JSONObjectUtil.isEmpty(targetLSKeyItems)) {
							JSONObject targetKeyItem = null;	// search 항목
							targetKeyItem = JSONObjectUtil.getJSONObject(targetLSKeyItems, dsetCateId);
							if(!JSONObjectUtil.isEmpty(targetKeyItem)) {
								targetKeyItem.put("master_code", dsetCateMasterCode);
								targetLSKeyItems.put(dsetCateId, targetKeyItem);
								targetLSKey.put("items", targetLSKeyItems);
								// 항목검색항목 저장
								targetListSearch.put("key",  targetLSKey);
								targetItemInfo.put("list_search", targetListSearch);
							}
						}
						targetItemObject.put("item_info", targetItemInfo);
	
						// 항목정보 파일 저장
						ModuleUtil.writeModuleItemObject(confModule, mfIdx, targetItemObject);
					}
				}
			}
		}
		

		// 설정정보
		if(isSetManager == 1) {
			// 설정관리하는 경우
			String designType = StringUtil.getString(settingDt.get("DESIGN_TYPE"));
			settingManagerObject = ModuleUtil.getModuleSettingManagerObject(confModule, null, designType);
			if(!JSONObjectUtil.isEmpty(settingManagerObject)) {
				JSONObject manageItems = JSONObjectUtil.getJSONObject(settingManagerObject, "items");
				JSONArray manageItemOrder = JSONObjectUtil.getJSONArray(settingManagerObject, "modifyproc_order");
				targetSetObject = ModuleUtil.getModuleSettingObject(confModule, mfIdx, false);
				JSONObject targetSettingInfo = null;
				if(!JSONObjectUtil.isEmpty(targetSetObject)) targetSettingInfo = JSONObjectUtil.getJSONObject(targetSetObject, "setting_info");
				else {
					targetSetObject = new JSONObject();
					targetSettingInfo = new JSONObject();
				}
				//if(settingDt != null) {
					JSONObject manageItem = null;
					String manageColumnId = null;
					String manageItemType = null;
					
					for(Object itemIdObj:manageItemOrder) {
						if (itemIdObj instanceof String) {
							String manageItemId = (String) itemIdObj;
							manageItem = JSONObjectUtil.getJSONObject(manageItems, manageItemId);
							manageColumnId = JSONObjectUtil.getString(manageItem, "column_id");
							manageItemType = JSONObjectUtil.getString(manageItem, "item_type");
							
							if(StringUtil.isEquals(manageItemType, "1")) targetSettingInfo.put(manageItemId, StringUtil.getInt(settingDt.get(manageColumnId)));
							else targetSettingInfo.put(manageItemId, settingDt.get(manageColumnId));
						}
					}
					
					String fnName = StringUtil.getString(settingDt.get("FN_NAME"));
					targetSettingInfo.put("list_title", MessageFormat.format(rbsMessageSource.getMessage("item.itemId.setting.list.title"), fnName));
					targetSettingInfo.put("input_title", MessageFormat.format(rbsMessageSource.getMessage("item.itemId.setting.input.title"), fnName));
					targetSettingInfo.put("deleteList_title", MessageFormat.format(rbsMessageSource.getMessage("item.itemId.setting.deleteList.title"), fnName));
					
					
					targetSetObject.put("setting_info", targetSettingInfo);
					
					// 모듈별 전체 설정 파일
					String totalSObjName = "item" + mfIdx;
					JSONObject totalSettingObject = ModuleUtil.getModuleTotalSettingObject(confModule, false);
					
					// Q&A사용여부 전체 저장
					boolean isSetObj = true;
					if(totalSettingObject == null || totalSettingObject.isEmpty()) {
						totalSettingObject = new JSONObject();
						isSetObj = false;
					}
					
					JSONObject fnSettingObject = JSONObjectUtil.getJSONObject(totalSettingObject, totalSObjName);
					if(fnSettingObject == null || fnSettingObject.isEmpty()) {
						fnSettingObject = new JSONObject();
						isSetObj = false;
					}

					int useDIDate = StringUtil.getInt(settingDt.get("USE_DLDATE"));
					int useQNA = StringUtil.getInt(settingDt.get("USE_QNA"));
					int usePrivate = StringUtil.getInt(settingDt.get("USE_PRIVATE"));
					int useContList = StringUtil.getInt(settingDt.get("USE_CONT_LIST"));
					int usePartType = StringUtil.getInt(settingDt.get("USE_PART_TYPE"));
					
					fnSettingObject.put("idx_name", targetSettingInfo.get("idx_name"));
					fnSettingObject.put("idx_column", targetSettingInfo.get("idx_column"));
					fnSettingObject.put("use_qna", useQNA);
					fnSettingObject.put("use_dldate", useDIDate);
					fnSettingObject.put("use_private", usePrivate);
					fnSettingObject.put("use_cont_list", useContList);
					fnSettingObject.put("use_part_type", usePartType);
					
					
					// 대표분류 전체 저장
					String dsetCateListId = StringUtil.getString(settingDt.get("DSET_CATE_LIST_ID"));
					if(!StringUtil.isEmpty(dsetCateListId)) {
						JSONObject cateListObject = JSONObjectUtil.getJSONObject(targetItems, dsetCateListId);
						String dsetCateListColumnId = JSONObjectUtil.getString(cateListObject, "column_id");
						String dsetCateListMsterCode = JSONObjectUtil.getString(cateListObject, "master_code");
						fnSettingObject.put("dset_cate_list_column_id", dsetCateListColumnId);
						fnSettingObject.put("dset_cate_list_master_code", dsetCateListMsterCode);
					} else if(isSetObj) {
						fnSettingObject.remove("dset_cate_list_column_id");
						fnSettingObject.remove("dset_cate_list_master_code");
					}
					totalSettingObject.put(totalSObjName, fnSettingObject);

					// 모듈별 전체 설정 파일 저장
					ModuleUtil.writeModuleTotalSettingObject(confModule, totalSettingObject);
				//}
				
				// 설정정보 파일 저장
				//if(targetSetObject != null && !targetSetObject.isEmpty()) {
					ModuleUtil.writeModuleSettingObject(confModule, mfIdx, targetSetObject);
				//}
			}
		}
		
		return 1;
	}

	/**
	 * 항목 코드정보 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getItemOptionList(Map<String, Object> param) {
		return moduleFnItemDAO.getOptionList(param);
	}
	
	/**
	 * 설정정보 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getSettingModify(Map<String, Object> param) {
		return moduleFnSetDAO.getModify(param);
	}

	/**
	 * 수정처리 : 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param uploadModulePath
	 * @param moduleId
	 * @param brdIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int settingUpdate(String uploadModulePath, String moduleId, int fnIdx, String regiIp,
			ParamForm parameterMap, JSONObject items, JSONArray itemOrder)
			throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		searchList.add(new DTForm("MODULE_ID", moduleId));
		searchList.add(new DTForm("FN_IDX", fnIdx));
		
		// 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, items, itemOrder);
		
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
    	param.put("moduleId", moduleId);
    	
		int result = moduleFnSetDAO.update(param);
		if(result > 0) {
			List<DTForm> fnDataList = new ArrayList<DTForm>();
			fnDataList.add(new DTForm("ISAPPLY", "0"));
	    	param.put("dataList", fnDataList);
	    	param.put("searchList", searchList);
			result = moduleFnDAO.update(param);
		}
		
		// 3. file 삭제
		List deleteFileList = StringUtil.getList(dataMap.get("deleteFileList"));
		if(deleteFileList != null) {
			FileUtil.isDelete(fileRealPath, deleteFileList);
		}
		
		return result;
	}

	/**
	 * 삭제 전체 목록 수
	 * @param param
	 * @return
	 */
	@Override
	public int getDeleteCount(Map<String, Object> param) {
    	return moduleFnDAO.getDeleteCount(param);
	}

	/**
	 * 삭제 전체 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDeleteList(Map<String, Object> param) {
		return moduleFnDAO.getDeleteList(param);
	}

	/**
	 * 삭제처리 : 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영<br/>
	 * 1. DB삭제 플래그 처리<br/>
	 * 2. 설정경로명(경로명 '[기능일련번호]'-> [기능일련번호]_del) 수정<br/>
	 * @param moduleId
	 * @param deleteIdxs
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	@Override
	public int delete(String moduleId, String[] deleteIdxs, String regiIp) {
		if(deleteIdxs == null) return 0;
	
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		
		// 저장조건
		searchList.add(new DTForm("MODULE_ID", moduleId));
		searchList.add(new DTForm("FN_IDX", deleteIdxs));
		
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
	
		param.put("searchList", searchList);
		param.put("dataList", dataList);
    	param.put("moduleId", moduleId);
		
		int result = moduleFnDAO.delete(param);
		if(result > 0) {
			String confModulePath = getConfModulePath(moduleId);
			//System.out.println("-confModulePath:" + confModulePath);
			if(!StringUtil.isEmpty(confModulePath)) {
				String filePath = null;
				for(String deleteIdx : deleteIdxs) {
					filePath = confModulePath + "/" + deleteIdx;
					//System.out.println("-filePath:" + filePath);
					File file = new File(filePath);
					if(file.isDirectory()) {
						File deleteFile = new File(filePath + "_del");
						file.renameTo(deleteFile);
					}
				}
			}
		}
		
		return result;
	}

	/**
	 * 복원처리 : 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영<br/>
	 * 1. DB복원 플래그 처리<br/>
	 * 2. 설정경로명(경로명 '[기능일련번호]_del'-> [기능일련번호]) 수정<br/>
	 * @param moduleId
	 * @param restoreIdxs
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	@Override
	public int restore(String moduleId, String[] restoreIdxs, String regiIp)
			throws Exception {
		if(restoreIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		// 저장조건
		searchList.add(new DTForm("MODULE_ID", moduleId));
		searchList.add(new DTForm("FN_IDX", restoreIdxs));
		
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

		param.put("searchList", searchList);
		param.put("dataList", dataList);
    	param.put("moduleId", moduleId);
		
		int result = moduleFnDAO.restore(param);

		if(result > 0) {
			String confModulePath = getConfModulePath(moduleId);
			if(!StringUtil.isEmpty(confModulePath)) {
				String filePath = null;
				for(String deleteIdx : restoreIdxs) {
					filePath = confModulePath + "/" + deleteIdx;
					File deleteFile = new File(filePath + "_del");
					if(deleteFile.isDirectory()) {
						File file = new File(filePath);
						deleteFile.renameTo(file);
					}
				}
			}
		}
		
		return result;
	}

	/**
	 * 완전삭제처리 : 화면에 조회된 정보를 데이터베이스에서 삭제
	 * 1. DB삭제 처리<br/>
	 * 2. 항목 DB 삭제 처리<br/>
	 * 3. 기능 테이블 삭제 처리<br/>
	 * 4. 설정파일 삭제 처리<br/>
	 * @param moduleId
	 * @param deleteIdxs
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@Override
	public int cdelete(String moduleId, String[] deleteIdxs, JSONObject items, JSONArray itemOrder) throws Exception {
		if(deleteIdxs == null) return 0;
	
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		// 1. 저장조건
		searchList.add(new DTForm("MODULE_ID", moduleId));
		searchList.add(new DTForm("FN_IDX", deleteIdxs));
		
		param.put("searchList", searchList);
    	param.put("moduleId", moduleId);

		// 2. delete (FN_INFO)
		int result = moduleFnDAO.cdelete(param);
		if(result > 0) {
			// 관리모듈 정보
			DataMap moduleDt = getModuleInfo(moduleId);
			if(moduleDt == null) {
				// 관리할 모듈(기능)이 없는 경우
				return -1;
			}

			int isItemManager = StringUtil.getInt(moduleDt.get("ISITEMMANAGER"));
			if(isItemManager == 1) {
				// 3. 항목정보 삭제 (ITEM_INFO, ITEM_DEL_INFO)
				Map<String, Object> itemParam = new HashMap<String, Object>();
				List<DTForm> itemSearchList = new ArrayList<DTForm>();
	
				// 1. 저장조건
				itemSearchList.add(new DTForm("FN_IDX", deleteIdxs));
				
				itemParam.put("searchList", itemSearchList);
				itemParam.put("MODULE_ID", moduleId.toUpperCase());
	
				// 2. delete
				result = moduleFnItemDAO.cdelete(itemParam);		// ITEM_INFO
				//result = moduleFnItemDAO.delCdelete(itemParam);		// ITEM_DEL_INFO
			}

			// 설정파일 경로
			String confModule = StringUtil.getString(moduleDt.get("CONF_MODULE"));
			if(StringUtil.isEmpty(confModule)) confModule = moduleId;
			String confModulePath = RbsProperties.getProperty("Globals.module.path") + "/" + confModule;

			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
			ApplicationContext act = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
			SchemaMapper schemaMapper = (SchemaMapper)act.getBean(moduleId + "SchemaMapper");
			
			String filePath = null;
			int result1 = 0;
			for(String deleteIdx : deleteIdxs) {
				// 4. 테이블 삭제
				if(schemaMapper != null) {
					Map<String, Object> createParam = new HashMap<String, Object>();
					createParam.put("fnIdx", deleteIdx);
					result1 = schemaMapper.dropTable(createParam);
				}

				// 5. 설정파일 삭제
				if(!StringUtil.isEmpty(confModulePath)) {
					filePath = confModulePath + "/" + deleteIdx;
					FileUtil.isDelete(filePath + "_del");
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 관리모듈정보
	 * @param moduleId
	 * @return
	 */
	private DataMap getModuleInfo(String moduleId) {
		Map<String, Object> moduleParam = new HashMap<String, Object>();
		List<DTForm> moduleSearchList = new ArrayList<DTForm>();
		moduleSearchList.add(new DTForm("A.MODULE_ID", moduleId));
		moduleParam.put("searchList", moduleSearchList);
		DataMap moduleDt = moduleDAO.getManageView(moduleParam);
		
		return moduleDt;
	}
	
	/**
	 * 관리모듈 설정파일경로
	 * @param moduleId
	 * @return
	 */
	private String getConfModulePath(String moduleId) {

		// 관리모듈 정보
		DataMap moduleDt = getModuleInfo(moduleId);
		if(moduleDt == null) {
			// 관리할 모듈(기능)이 없는 경우
			return null;
		}

		// 2. 설정파일 경로
		String confModule = StringUtil.getString(moduleDt.get("CONF_MODULE"));
		if(StringUtil.isEmpty(confModule)) confModule = moduleId;
		
		return RbsProperties.getProperty("Globals.module.path") + "/" + confModule;
	}

}
