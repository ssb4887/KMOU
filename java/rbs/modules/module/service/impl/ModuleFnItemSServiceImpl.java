package rbs.modules.module.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.egovframework.LoginVO;
import rbs.modules.module.mapper.ModuleFnItemMapper;
import rbs.modules.module.mapper.ModuleFnItemSMapper;
import rbs.modules.module.mapper.ModuleFnMapper;
import rbs.modules.module.service.ModuleFnItemSService;

@Service("moduleFnItemSService")
public class ModuleFnItemSServiceImpl extends EgovAbstractServiceImpl implements ModuleFnItemSService {

	@Resource(name="moduleFnItemMapper")
	private ModuleFnItemMapper moduleFnItemDAO;
	
	@Resource(name="moduleFnItemSMapper")
	private ModuleFnItemSMapper moduleFnItemSDAO;
	
	@Resource(name="moduleFnMapper")
	private ModuleFnMapper moduleFnDAO;
	
	/**
	 * 입력항목 검색에 사용할 기본항목정보
	 * @param param
	 * @return
	 */
	public List<Object> getDefaultSearchIList(Map<String, Object> param) {
		return moduleFnItemSDAO.getDefaultSearchIList(param);
	}
	
	/**
	 * 선택항목 검색에 사용할 기본항목정보
	 * @param param
	 * @return
	 */
	public List<Object> getDefaultSearchSList(Map<String, Object> param) {
		return moduleFnItemSDAO.getDefaultSearchSList(param);
	}
	
	/**
	 * 입력항목 검색에 사용할 항목정보
	 * @param param
	 * @return
	 */
	public List<Object> getSearchIList(Map<String, Object> param) {
		return moduleFnItemSDAO.getSearchIList(param);
	}
	
	/**
	 * 선택항목 검색에 사용할 항목정보
	 * @param param
	 * @return
	 */
	public List<Object> getSearchSList(Map<String, Object> param) {
		return moduleFnItemSDAO.getSearchSList(param);
	}

	/**
	 * 전체 목록 수
	 * @param param
	 * @return
	 */
	@Override
	public int getTotalCount(Map<String, Object> param) {
    	return moduleFnItemDAO.getTotalCount(param);
	}

	/**
	 * 전체 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(Map<String, Object> param) {
		return moduleFnItemDAO.getList(param);
	}

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getView(Map<String, Object> param) {
		return moduleFnItemDAO.getView(param);
	}

	/**
	 * 수정 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getModify(Map<String, Object> param) {
		return moduleFnItemDAO.getModify(param);
	}
	
	/**
	 * 등록처리 : 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장   <br/>
	 * 기능 정보 <br/>
	 * 설정 정보  <br/>
	 * 항목 정보  <br/>
	 * 검색 항목 정보  <br/>
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
	public int insert(String moduleId, int fnIdx, String regiIp,
			ParamForm parameterMap)
			throws Exception {
		int result = 0;
		String tableModuleId = moduleId.toUpperCase();
		//LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		
		String[] searchItemKey = StringUtil.getStringArray(parameterMap.get("searchItemKey"));				// 항목검색 항목
		String[] searchItemKeyField = StringUtil.getStringArray(parameterMap.get("searchItemKeyField"));	// 선택검색 항목
		
		// 1. 검색항목 삭제
		param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("FN_IDX", fnIdx));
		
		param.put("searchList", searchList);
		param.put("MODULE_ID", tableModuleId);
		result = moduleFnItemSDAO.cdelete(param);
		
		// 2. 검색항목 저장
		if(searchItemKey != null) {

			int idx = 1;
			for(String searchItemId:searchItemKey) {
				List<Object> dataList2 = new ArrayList<Object>();
				dataList2.add(new DTForm("FN_IDX", fnIdx));
				dataList2.add(new DTForm("ITEM_ID", searchItemId));
				dataList2.add(new DTForm("SEARCH_TYPE", "1"));
				dataList2.add(new DTForm("ORDER_IDX", idx));
				
				param = new HashMap<String, Object>();
				param.put("dataList", dataList2);
				param.put("MODULE_ID", tableModuleId);
				result = moduleFnItemSDAO.insert(param);
				idx ++;
			}
		}
		
		if(searchItemKeyField != null) {
			
			int idx = 1;
			for(String searchItemId:searchItemKeyField) {
				List<Object> dataList2 = new ArrayList<Object>();
				dataList2.add(new DTForm("FN_IDX", fnIdx));
				dataList2.add(new DTForm("ITEM_ID", searchItemId));
				dataList2.add(new DTForm("SEARCH_TYPE", "2"));
				dataList2.add(new DTForm("ORDER_IDX", idx));
				
				param = new HashMap<String, Object>();
				param.put("dataList", dataList2);
				param.put("MODULE_ID", tableModuleId);
				result = moduleFnItemSDAO.insert(param);
				idx ++;
			}
		}
			
		// 5. 기능 적용여부 저장
		result = updateFnApply(moduleId, fnIdx);
		return result;
	}

	@Override
	public int update(String uploadModulePath, String moduleId, int fnIdx, String itemId, String regiIp,
			ParamForm parameterMap, JSONObject items, JSONArray itemOrder)
			throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		return update(uploadModulePath, moduleId, fnIdx, itemId, regiIp, parameterMap, items, itemOrder, null);
	}

	@Override
	public int update(String uploadModulePath, String moduleId, int fnIdx, String itemId, String regiIp,
			ParamForm parameterMap, String ordItemId)
			throws Exception {
		if(StringUtil.isEmpty(ordItemId)) return -1;
		return update(uploadModulePath, moduleId, fnIdx, itemId, regiIp, parameterMap, null, null, ordItemId);
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
	public int update(String uploadModulePath, String moduleId, int fnIdx, String itemId, String regiIp,
			ParamForm parameterMap, JSONObject items, JSONArray itemOrder, String ordItemId)
			throws Exception {
		if(StringUtil.isEmpty(ordItemId) && (JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder))) return -1;

		String tableModuleId = moduleId.toUpperCase();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
		searchList.add(new DTForm("FN_IDX", fnIdx));
		searchList.add(new DTForm("ITEM_ID", itemId));
		
		// 순서 setting
		if(StringUtil.isEmpty(ordItemId)) ordItemId = StringUtil.getString(parameterMap.get("order_idx"));
		if(!StringUtil.isEquals(ordItemId, itemId)) {
			int preOrdIdx = 0;
			int ordIdx = 0;
			// 저장된 순서 select
			Map<String, Object> sparam2 = new HashMap<String, Object>();
			List<DTForm> searchList2 = new ArrayList<DTForm>();
			searchList2.add(new DTForm("A.FN_IDX", fnIdx));
			searchList2.add(new DTForm("A.ITEM_ID", itemId));
			sparam2.put("searchList", searchList2);
			sparam2.put("MODULE_ID", tableModuleId);
			preOrdIdx = moduleFnItemDAO.getOrdIdx(sparam2);
			
			// 선택한 순서 select
			sparam2 = new HashMap<String, Object>();
			searchList2 = new ArrayList<DTForm>();
			searchList2.add(new DTForm("A.FN_IDX", fnIdx));
			searchList2.add(new DTForm("A.ITEM_ID", ordItemId));
			sparam2.put("searchList", searchList2);
			sparam2.put("MODULE_ID", tableModuleId);
			ordIdx = moduleFnItemDAO.getOrdIdx(sparam2);
			
			// 선택한 순서 이상 증가
			Map<String, Object> sparam3 = new HashMap<String, Object>();
			List<DTForm> dataList3 = new ArrayList<DTForm>();
			List<DTForm> searchList3 = new ArrayList<DTForm>();
			searchList3.add(new DTForm("FN_IDX", fnIdx));

			if(preOrdIdx > ordIdx) {
				searchList3.add(new DTForm("ORDER_IDX", ordIdx, ">="));
				searchList3.add(new DTForm("ORDER_IDX", preOrdIdx, "<"));
				dataList3.add(new DTForm("ORDER_IDX", "ORDER_IDX + 1", 1));
			} else {
				searchList3.add(new DTForm("ORDER_IDX", ordIdx, "<="));
				searchList3.add(new DTForm("ORDER_IDX", preOrdIdx, ">"));
				dataList3.add(new DTForm("ORDER_IDX", "ORDER_IDX - 1", 1));
			}
			sparam3.put("dataList", dataList3);
			sparam3.put("searchList", searchList3);
			sparam3.put("MODULE_ID", tableModuleId);
			int result1 = moduleFnItemDAO.update(sparam3);

			dataList.add(new DTForm("ORDER_IDX", ordIdx));
		}
		
		if(StringUtil.isEmpty(ordItemId)) {
			// 항목설정으로 저장항목 setting
			String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
			HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, items, itemOrder);
			if(dataMap == null || dataMap.size() == 0) return -1;
			
			List itemDataList = StringUtil.getList(dataMap.get("dataList"));
			if(itemDataList != null) dataList.addAll(itemDataList);

			// 3.2 언어목록
			List<DTForm> langItemDataList = getLangItemDataList(parameterMap);
			if(langItemDataList != null) dataList.addAll(langItemDataList);
		}
		
		// 저장 항목
    	param.put("searchList", searchList);
    	param.put("MODULE_ID", tableModuleId);
		DataMap itemDt = getModify(param);
		int isDB = StringUtil.getInt(itemDt.get("ISDB"));
		// 컬럼 생성 항목 -> 컬럼 수정
		if(isDB == 1) dataList.add(new DTForm("ISDB", "2"));
		
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
    	param.put("MODULE_ID", tableModuleId);
    	
		int result = moduleFnItemDAO.update(param);
		if(result > 0) {
			// 5. 기능 적용여부 저장
			result = updateFnApply(moduleId, fnIdx);
		}
				
		/*
		// 3. file 삭제
		List deleteFileList = StringUtil.getList(dataMap.get("deleteFileList"));
		if(deleteFileList != null) {
			FileUtil.isDelete(fileRealPath, deleteFileList);
		}
		*/
		return result;
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
	 * 삭제 전체 목록 수
	 * @param param
	 * @return
	 */
	@Override
	public int getDeleteCount(Map<String, Object> param) {
    	return moduleFnItemDAO.getDeleteCount(param);
	}

	/**
	 * 삭제 전체 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDeleteList(Map<String, Object> param) {
		return moduleFnItemDAO.getDeleteList(param);
	}

	/**
	 * 삭제처리 : 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
	 * @param moduleId
	 * @param deleteIdxs
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	@Override
	public int delete(String moduleId, int fnIdx, String[] deleteIdxs, String regiIp) {
		if(deleteIdxs == null) return 0;
	
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		
		// 저장조건
		searchList.add(new DTForm("FN_IDX", fnIdx));
		searchList.add(new DTForm("ITEM_ID", deleteIdxs));
		
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
    	param.put("MODULE_ID", moduleId.toUpperCase());
		
		int result = moduleFnItemDAO.delete(param);
		if(result > 0) {
			// 5. 기능 적용여부 저장
			result = updateFnApply(moduleId, fnIdx);
		}
		
		return result;
	}

	/**
	 * 복원처리 : 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
	 * @param moduleId
	 * @param restoreIdxs
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	@Override
	public int restore(String moduleId, int fnIdx, String[] restoreIdxs, String regiIp)
			throws Exception {
		if(restoreIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		// 저장조건
		searchList.add(new DTForm("FN_IDX", fnIdx));
		searchList.add(new DTForm("ITEM_ID", restoreIdxs));
		
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
    	param.put("MODULE_ID", moduleId.toUpperCase());
		
    	int result = moduleFnItemDAO.restore(param);
		if(result > 0) {
			// 5. 기능 적용여부 저장
			result = updateFnApply(moduleId, fnIdx);
		}
		
		return result;
	}

	/**
	 * 완전삭제처리 : 화면에 조회된 정보를 데이터베이스에서 삭제
	 * @param moduleId
	 * @param uploadModulePath
	 * @param deleteIdxs
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@Override
	public int cdelete(String uploadModulePath, String moduleId, int fnIdx, String[] deleteIdxs, String regiIp, JSONObject items, JSONArray itemOrder) throws Exception {
		if(deleteIdxs == null) return 0;
	
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();

		// 1. DB 생성 안된 항목 완전삭제
		searchList.add(new DTForm("FN_IDX", fnIdx));
		searchList.add(new DTForm("ISDB", "0"));
		searchList.add(new DTForm("ITEM_ID", deleteIdxs));
		param.put("searchList", searchList);
    	param.put("MODULE_ID", moduleId.toUpperCase());
		
		int result = moduleFnItemDAO.cdelete(param);
		
		// 2. DB 생성 된 항목 임시 삭제 - 적용시 완전삭제
		List<DTForm> tmpSearchList = new ArrayList<DTForm>();
		tmpSearchList.add(new DTForm("FN_IDX", fnIdx));
		tmpSearchList.add(new DTForm("ISDB", "0", "<>"));
		tmpSearchList.add(new DTForm("ITEM_ID", deleteIdxs));
		
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

		param.put("searchList", tmpSearchList);
		param.put("dataList", dataList);
    	param.put("MODULE_ID", moduleId.toUpperCase());
    	
		result = moduleFnItemDAO.tmpCdelete(param);
		if(result > 0) {
			
			// 3. 순서 update
			param = new HashMap<String, Object>();
			searchList = new ArrayList<DTForm>();
			searchList.add(new DTForm("FN_IDX", fnIdx));
			searchList.add(new DTForm("ISDELETE", "2", "<>"));
			param.put("searchList", searchList);
	    	param.put("MODULE_ID", moduleId.toUpperCase());
			
	    	int result1 = moduleFnItemDAO.updateTotOrdIdx(param);
			
			// 4. 기능 적용여부 저장
			result = updateFnApply(moduleId, fnIdx);
		}
		
		return result;
	}
	
	/**
	 * 언어항목 저장
	 * @param parameterMap
	 * @return
	 */
	private List<DTForm> getLangItemDataList(ParamForm parameterMap) {
		List<DTForm> langItemDataList = null;
		List<Object> langList = null;
		String langItemId = null;
		String langColumnId = null;
		langList = CodeHelper.getOptnList("LOCALE");
		langItemDataList = (langList != null)? new ArrayList<DTForm>():null;
		
		if(langList != null){
			for(Object langObj:langList) {
				if(langObj instanceof DataMap) {
					DataMap langDt = (DataMap)langObj;
					
					langItemId = StringUtil.getString(langDt.get("OPTION_CODE"));
					langColumnId = langItemId.toUpperCase();
					langItemDataList.add(new DTForm("ITEM_NAME_" + langColumnId, parameterMap.get("item_name_" + langItemId)));
				}
			}
		}
		
		return langItemDataList;
	}
	
	/**
	 * 기능 미적용으로 update
	 * @param moduleId
	 * @param fnIdx
	 * @return
	 */
	private int updateFnApply(String moduleId, int fnIdx) {

		// 5. 기능 적용여부 저장
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("MODULE_ID", moduleId));
		searchList.add(new DTForm("FN_IDX", fnIdx));
		List<DTForm> fnDataList = new ArrayList<DTForm>();
		fnDataList.add(new DTForm("ISAPPLY", "0"));
    	param.put("dataList", fnDataList);
    	param.put("searchList", searchList);
		return moduleFnDAO.update(param);
	}

}
