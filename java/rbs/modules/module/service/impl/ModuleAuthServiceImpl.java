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
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.FileUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.egovframework.LoginVO;
import rbs.modules.module.mapper.ModuleAuthMapper;
import rbs.modules.module.mapper.ModuleAuthMultiMapper;
import rbs.modules.module.mapper.ModuleMapper;
import rbs.modules.module.service.ModuleAuthService;

@Service("moduleAuthService")
public class ModuleAuthServiceImpl extends EgovAbstractServiceImpl implements ModuleAuthService {

	@Resource(name="moduleAuthMapper")
	private ModuleAuthMapper moduleAuthDAO;

	@Resource(name="moduleAuthMultiMapper")
	private ModuleAuthMultiMapper moduleAuthMultiDAO;
	
	@Resource(name="moduleMapper")
	private ModuleMapper moduleDAO;
	
	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;

	/**
	 * 전체 목록 수
	 * @param param
	 * @return
	 */
	@Override
	public int getTotalCount(Map<String, Object> param) {
    	return moduleAuthDAO.getTotalCount(param);
	}

	/**
	 * 전체 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(Map<String, Object> param) {
		return moduleAuthDAO.getList(param);
	}

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getView(Map<String, Object> param) {
		return moduleAuthDAO.getView(param);
	}

	/**
	 * 수정 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getModify(Map<String, Object> param) {
		return moduleAuthDAO.getModify(param);
	}
	
	/**
	 * 등록처리 : 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장   <br/>
	 * 기능 정보 <br/>
	 * 설정파일생성 <br/>
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
	public int insert(String moduleId, String regiIp,
			ParamForm parameterMap, JSONObject items, JSONArray itemOrder)
			throws Exception {
		return insert(moduleId, -1, regiIp, parameterMap, items, itemOrder);
	}
	@Override
	public int insert(String moduleId, int authIdx, String regiIp,
			ParamForm parameterMap, JSONObject items, JSONArray itemOrder)
			throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<HashMap<String, Object>> multiDataList = null;
		JSONObject authObject = null;
		
		// 모듈정보 얻기
		Map<String, Object> moduleParam = new HashMap<String, Object>();
		List<DTForm> moduleSearchList = new ArrayList<DTForm>();
		moduleSearchList.add(new DTForm("A.MODULE_ID", moduleId));
		moduleParam.put("searchList", moduleSearchList);
		
		DataMap moduleDt = moduleDAO.getManageView(moduleParam);
		if(moduleDt == null) return -1;
		
		/*String confModule = StringUtil.getString(moduleDt.get("CONF_MODULE"));
		if(StringUtil.isEmpty(confModule)) confModule = moduleId;
		
		// 권한 설정 정보
		JSONArray authManagerArray = ModuleUtil.getModuleAuthManagerArray(confModule, null);*/
		// 권한 설정 정보
		JSONArray authManagerArray = ModuleUtil.getModuleAuthManagerArray(moduleId);
		if(JSONObjectUtil.isEmpty(authManagerArray)) {
			return -2;
		}
    	
    	// 권한별 사용자유형 setting
    	/*String authItemId = null;
    	String authColumnId = null;
    	String utpItemIdName = null;
    	String utpColumnIdName = null;
    	int utpVal = 0;
    	String multiItemId = null;
    	String[] multiItemIdNames = {"Grp", "Dpt", "Mbr"};
    	String[] multiColulmnNames = {"GRP", "DPT", "MBR"};
    	Object multiObjVals = null;
    	String[] multiVals = null;*/

    	authObject = new JSONObject();
    	HashMap<String, Object> authDataObjects = getAuthDataObjects(parameterMap, authManagerArray, authObject, dataList, multiDataList);
		if(authDataObjects != null) {
			authObject = (JSONObject)authDataObjects.get("authObject");
			dataList = (List)authDataObjects.get("dataList");
			multiDataList = (List)authDataObjects.get("multiDataList");
		}
		/*
    	for(Object authManagerObj:authManagerArray) {
    		if(authManagerObj instanceof JSONObject) {
    			JSONObject authManagerJSONObj = (JSONObject)authManagerObj;
    			authItemId = JSONObjectUtil.getString(authManagerJSONObj, "item_id");
    			authColumnId = JSONObjectUtil.getString(authManagerJSONObj, "column_id");
    			
    			utpItemIdName = authItemId + "Utp";
    			utpColumnIdName = authColumnId + "_UTP";
    			
    	    	// 사용자유형
    			utpVal = StringUtil.getInt(parameterMap.get(utpItemIdName));
    			dataList.add(new DTForm(authColumnId, StringUtil.getInt(parameterMap.get(utpItemIdName))));
    			
    			authObject.put(utpColumnIdName, utpVal);

    			// multi 사용자권한 : 그룹,부서,회원
    			int mIdx = 0;
    			for(String multiItemIdName:multiItemIdNames) {
    				multiItemId = authItemId + multiItemIdName;
    				multiObjVals = parameterMap.get(multiItemId);
    				if(multiObjVals != null) {
    					// 값 있는 경우
						if(multiObjVals instanceof String[]) multiVals = (String[])multiObjVals;
						else if(multiObjVals instanceof String){
							multiVals = new String[1];
							multiVals[0] = (String)multiObjVals;
						}
						for(String multiVal:multiVals) {
			    			List<DTForm> multiDataList2 = new ArrayList<DTForm>();
							multiDataList2.add(new DTForm("ITEM_ID", multiItemId));											// item_id
							multiDataList2.add(new DTForm("ITEM_KEY", multiVal));								// item_key
			
							HashMap<String, Object> dataMap2 = new HashMap<String, Object>();
							dataMap2.put("dataList", multiDataList2);
							
							if(multiDataList == null) multiDataList = new ArrayList<HashMap<String,Object>>();
							multiDataList.add(dataMap2);
						}
						
						authObject.put(authColumnId + "_" + multiColulmnNames[mIdx], StringUtil.getString(multiVals, ","));
    				} else authObject.remove(authColumnId + "_" + multiColulmnNames[mIdx]);
					
					mIdx ++;
    			}
    		}
    	}*/

		if(authIdx < 0) {
			//고유아이디 셋팅
			searchList.add(new DTForm("A.MODULE_ID", moduleId));
			param.put("searchList", searchList);
			authIdx = moduleAuthDAO.getNextId(param);
		}
		
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(parameterMap, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// 기능 저장 항목
		dataList.add(new DTForm("MODULE_ID", moduleId));
    	dataList.add(new DTForm("AUTH_IDX", authIdx));
    	
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

		
    	// 1. 기능정보 저장
    	param.put("dataList", dataList);
    	param.put("moduleId", moduleId);
    	
		int result = moduleAuthDAO.insert(param);
		
		if(result > 0) {
			// 5. multi data 저장
			/*Map<String, Object> multiDelParam = new HashMap<String, Object>();
			multiDelParam.put("MODULE_ID", moduleId);
			multiDelParam.put("AUTH_IDX", authIdx);
			multiDelParam.put("moduleId", moduleId);
			result = moduleAuthMultiDAO.cdelete(multiDelParam);*/
			
			if(multiDataList != null) {
				int multiDataSize = multiDataList.size();
				for(int i = 0 ; i < multiDataSize ; i ++) {
					Map<String, Object> multiParam = (HashMap)multiDataList.get(i);
					multiParam.put("MODULE_ID", moduleId);
					multiParam.put("AUTH_IDX", authIdx);
			    	multiParam.put("moduleId", moduleId);
					result = moduleAuthMultiDAO.insert(multiParam);
				}
			}
			
			// 권한 파일 생성
			//ModuleUtil.writeModuleAuthObject(confModule, authIdx, authObject);
			ModuleUtil.writeModuleAuthObject(moduleId, authIdx, authObject);
		}
		
		return result;
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
	public int update(String moduleId, int authIdx, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		List<HashMap<String, Object>> multiDataList = null;
		JSONObject authObject = null;
		
		// 모듈정보 얻기
		Map<String, Object> moduleParam = new HashMap<String, Object>();
		List<DTForm> moduleSearchList = new ArrayList<DTForm>();
		moduleSearchList.add(new DTForm("A.MODULE_ID", moduleId));
		moduleParam.put("searchList", moduleSearchList);
		
		DataMap moduleDt = moduleDAO.getManageView(moduleParam);
		if(moduleDt == null) return -1;
		
		/*String confModule = StringUtil.getString(moduleDt.get("CONF_MODULE"));
		if(StringUtil.isEmpty(confModule)) confModule = moduleId;
		
		// 권한 설정 정보
		JSONArray authManagerArray = ModuleUtil.getModuleAuthManagerArray(confModule, null);*/
		JSONArray authManagerArray = ModuleUtil.getModuleAuthManagerArray(moduleId);
		if(JSONObjectUtil.isEmpty(authManagerArray)) {
			return -2;
		}
    	
    	// 권한별 사용자유형 setting
		//authObject = ModuleUtil.getModuleAuthObject(confModule, authIdx);
		authObject = ModuleUtil.getModuleAuthObject(moduleId, authIdx);
		HashMap<String, Object> authDataObjects = getAuthDataObjects(parameterMap, authManagerArray, authObject, dataList, multiDataList);
		if(authDataObjects != null) {
			authObject = (JSONObject)authDataObjects.get("authObject");
			dataList = (List)authDataObjects.get("dataList");
			multiDataList = (List)authDataObjects.get("multiDataList");
		}
		
		searchList.add(new DTForm("MODULE_ID", moduleId));
		searchList.add(new DTForm("AUTH_IDX", authIdx));
		
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(parameterMap, items, itemOrder);
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
    	
		int result = moduleAuthDAO.update(param);
		
		if(result > 0) {
			// 5. multi data 저장
			Map<String, Object> multiDelParam = new HashMap<String, Object>();
			multiDelParam.put("MODULE_ID", moduleId);
			multiDelParam.put("AUTH_IDX", authIdx);
			multiDelParam.put("moduleId", moduleId);
			int result1 = moduleAuthMultiDAO.cdelete(multiDelParam);
			
			if(multiDataList != null) {
				int multiDataSize = multiDataList.size();
				for(int i = 0 ; i < multiDataSize ; i ++) {
					Map<String, Object> multiParam = (HashMap)multiDataList.get(i);
					multiParam.put("MODULE_ID", moduleId);
					multiParam.put("AUTH_IDX", authIdx);
			    	multiParam.put("moduleId", moduleId);
					result = moduleAuthMultiDAO.insert(multiParam);
				}
			}
			
			// 권한 파일 생성
			//ModuleUtil.writeModuleAuthObject(confModule, authIdx, authObject);
			ModuleUtil.writeModuleAuthObject(moduleId, authIdx, authObject);
		}
				
		return result;
	}
	
	/**
	 * 모듈저장항목 setting
	 * @param parameterMap
	 * @param authManagerArray
	 * @param authObject
	 * @param dataList
	 * @param multiDataList
	 * @return
	 */
	private HashMap<String, Object> getAuthDataObjects(ParamForm parameterMap, JSONArray authManagerArray, JSONObject authObject, List<DTForm> dataList, List<HashMap<String, Object>> multiDataList) {
		HashMap<String, Object> authDataObjects = new HashMap<String, Object>();
    	String authItemId = null;
    	String authColumnId = null;
    	String utpItemIdName = null;
    	String utpColumnIdName = null;
    	int utpVal = 0;
    	String multiItemId = null;
    	String[] multiItemIdNames = {"Grp", "Dpt", "Mbr"};
    	String[] multiColulmnNames = {"GRP", "DPT", "MBR"};
    	Object multiObjVals = null;
    	String[] multiVals = null;
    	
		for(Object authManagerObj:authManagerArray) {
    		if(authManagerObj instanceof JSONObject) {
    			JSONObject authManagerJSONObj = (JSONObject)authManagerObj;
    			authItemId = JSONObjectUtil.getString(authManagerJSONObj, "item_id");
    			authColumnId = JSONObjectUtil.getString(authManagerJSONObj, "column_id");
    			
    			utpItemIdName = authItemId + "Utp";
    			utpColumnIdName = authColumnId + "_UTP";
    			
    	    	// 사용자유형
    			utpVal = StringUtil.getInt(parameterMap.get(utpItemIdName));
    			dataList.add(new DTForm(utpColumnIdName, StringUtil.getInt(parameterMap.get(utpItemIdName))));
    			
    			authObject.put(utpColumnIdName, utpVal);

    			// multi 사용자권한 : 그룹,부서,회원
    			int mIdx = 0;
    			for(String multiItemIdName:multiItemIdNames) {
    				multiItemId = authItemId + multiItemIdName;
    				multiObjVals = parameterMap.get(multiItemId);
    				if(multiObjVals != null) {
    					// 값 있는 경우
						if(multiObjVals instanceof String[]) multiVals = (String[])multiObjVals;
						else if(multiObjVals instanceof String){
							multiVals = new String[1];
							multiVals[0] = (String)multiObjVals;
						}
						for(String multiVal:multiVals) {
			    			List<DTForm> multiDataList2 = new ArrayList<DTForm>();
							multiDataList2.add(new DTForm("ITEM_ID", multiItemId));											// item_id
							multiDataList2.add(new DTForm("ITEM_KEY", multiVal));								// item_key
			
							HashMap<String, Object> dataMap2 = new HashMap<String, Object>();
							dataMap2.put("dataList", multiDataList2);
							
							if(multiDataList == null) multiDataList = new ArrayList<HashMap<String,Object>>();
							multiDataList.add(dataMap2);
						}
						
						authObject.put(authColumnId + "_" + multiColulmnNames[mIdx], StringUtil.getString(multiVals, ","));
    				} else authObject.remove(authColumnId + "_" + multiColulmnNames[mIdx]);
					
					mIdx ++;
    			}
    		}
    	}

		authDataObjects.put("authObject", authObject);
		authDataObjects.put("dataList", dataList);
		authDataObjects.put("multiDataList", multiDataList);
		
		return authDataObjects;
	}

	/**
	 * 삭제 전체 목록 수
	 * @param param
	 * @return
	 */
	@Override
	public int getDeleteCount(Map<String, Object> param) {
    	return moduleAuthDAO.getDeleteCount(param);
	}

	/**
	 * 삭제 전체 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDeleteList(Map<String, Object> param) {
		return moduleAuthDAO.getDeleteList(param);
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
		searchList.add(new DTForm("AUTH_IDX", deleteIdxs));
		
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
		
		int result = moduleAuthDAO.delete(param);
		if(result > 0) {
			String confModulePath = getConfModulePath(moduleId) + "/" + RbsProperties.getProperty("Globals.auth.path");
			System.out.println("-confModulePath:" + confModulePath);
			if(!StringUtil.isEmpty(confModulePath)) {
				String filePath = null;
				for(String deleteIdx : deleteIdxs) {
					filePath = confModulePath + "/" + deleteIdx;
					System.out.println("-filePath:" + filePath);
					File file = new File(filePath + RbsProperties.getProperty("Globals.module.ext"));
					if(file.isFile()) {
						File deleteFile = new File(filePath + "_del" + RbsProperties.getProperty("Globals.module.ext"));
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
		searchList.add(new DTForm("AUTH_IDX", restoreIdxs));
		
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
		
		int result = moduleAuthDAO.restore(param);

		if(result > 0) {
			String confModulePath = getConfModulePath(moduleId) + "/" + RbsProperties.getProperty("Globals.auth.path");;
			if(!StringUtil.isEmpty(confModulePath)) {
				String filePath = null;
				for(String deleteIdx : restoreIdxs) {
					filePath = confModulePath + "/" + deleteIdx;
					File deleteFile = new File(filePath + "_del" + RbsProperties.getProperty("Globals.module.ext"));
					if(deleteFile.isFile()) {
						File file = new File(filePath + RbsProperties.getProperty("Globals.module.ext"));
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
		searchList.add(new DTForm("AUTH_IDX", deleteIdxs));
		
		param.put("searchList", searchList);
    	param.put("moduleId", moduleId);

		// 2. delete (FN_INFO)
		int result = moduleAuthDAO.cdelete(param);
		if(result > 0) {
			// 관리모듈 정보
			DataMap moduleDt = getModuleInfo(moduleId);
			if(moduleDt == null) {
				// 관리할 모듈(기능)이 없는 경우
				return -1;
			}

			// 설정파일 경로
			String confModule = StringUtil.getString(moduleDt.get("CONF_MODULE"));
			if(StringUtil.isEmpty(confModule)) confModule = moduleId;
			String confModulePath = RbsProperties.getProperty("Globals.module.path") + "/" + confModule + "/" + RbsProperties.getProperty("Globals.auth.path");

			String filePath = null;
			for(String deleteIdx : deleteIdxs) {

				// 5. 설정파일 삭제
				if(!StringUtil.isEmpty(confModulePath)) {
					filePath = confModulePath + "/" + deleteIdx;
					FileUtil.isDelete(filePath + "_del" + RbsProperties.getProperty("Globals.module.ext"));
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
