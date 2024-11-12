package rbs.modules.tabContents.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.woowonsoft.egovframework.util.FileUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.egovframework.LoginVO;
import rbs.modules.itemClass.mapper.ClassOptnMapper;
import rbs.modules.tabContents.mapper.TabContentsDataMapper;
import rbs.modules.tabContents.mapper.TabContentsFileMapper;
import rbs.modules.tabContents.mapper.TabContentsMapper;
import rbs.modules.tabContents.service.TabContentsService;

/**
 * 배너관리에 관한 구현클래스를 정의한다.
 * @author user
 *
 */
@Service("tabContentsService")
public class TabContentsServiceImpl extends EgovAbstractServiceImpl implements TabContentsService {

	@Resource(name="tabContentsMapper")
	private TabContentsMapper tabcontentsDAO;
	
	
	@Resource(name="classOptnMapper")
	private ClassOptnMapper classOptnDAO;
	
	@Resource(name="tabContentsFileMapper")
	private TabContentsFileMapper tabcontentsFileDAO;
	
	@Resource(name="tabContentsDataMapper")
	private TabContentsDataMapper tabcontentsDataDAO;
	
	
	
	/**
	 * 전체 목록 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
    @Override
	public int getTotalCount(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	return tabcontentsDAO.getTotalCount(param);
    }

    /**
	 * 전체 목록
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return tabcontentsDAO.getList(param);
	}
	/**
	 * multi파일 상세조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getMultiFileView(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		DataMap viewDAO = tabcontentsDAO.getFileView(param);
		return viewDAO;
	}
	/**
	 * 파일 상세조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getFileView(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	DataMap viewDAO = tabcontentsDAO.getFileView(param);
		return viewDAO;
	}

	/**
	 * 상세조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getView(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	DataMap viewDAO = tabcontentsDAO.getView(param);
		return viewDAO;
	}

	/**
	 * 수정 상세조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getModify(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	DataMap viewDAO = tabcontentsDAO.getModify(param);
    	
    	
		return viewDAO;
	}
    

	/**
	 * 등록처리 : 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param siteMode
	 * @param regiIp
	 * @param parameterMap
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int insert(String uploadModulePath, int fnIdx, String siteMode, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	param.put("fnIdx", fnIdx);												// table flag
    	String masterCode = JSONObjectUtil.getString(settingInfo, "dset_cate_master_code");
		
    	DTForm masterSearchForm = new DTForm("MASTER_CODE", masterCode);
    	
    	// 1. key 얻기
		int classIdx = tabcontentsDAO.getNextId(param);
		
		int ordType = StringUtil.getInt(parameterMap.get("ordType"));
		int targetClassIdx = StringUtil.getInt(parameterMap.get("ordIdx"));

		
		Object[] conIdxs = StringUtil.getStringArray(parameterMap.get("con_idx"));
		int conIdxLen = 0;
		if(conIdxs != null) conIdxLen = conIdxs.length;
		
		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
	
		// 2.1 저장항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
    	dataList.add(new DTForm("CLASS_IDX", classIdx));
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
    	// 4. DB 저장
		int result = tabcontentsDAO.insert(param);
		// rbs_tabcont_data_info 테이블 등록, 수정, 삭제
    	if(conIdxLen > 0)
    	{
    		
    		Object[] url;
			String conSavedFileVal = null;
			Object[] url1 = StringUtil.getStringArray(parameterMap.get("url"));
			Object[] url_con = StringUtil.getStringArray(parameterMap.get("url_con"));
			Map<String, Object> param1 = new HashMap<String, Object>();	
	    	for(int i = 0 ; i < conIdxLen ; i ++)
	    	{
	    		url = StringUtil.getStringArray(parameterMap.get("con_idx"));
    			// 등록 : con_idx 없는 경우
    			if(url != null)
    			{
    				param1 = new HashMap<String, Object>();
					dataList = new ArrayList<DTForm>();
		    		dataList.add(new DTForm("CLASS_IDX", classIdx));
		    		dataList.add(new DTForm("CON_IDX",i+1 ));
		    		dataList.add(new DTForm("URL",url1[i] ));
		    		dataList.add(new DTForm("URL_CON",url_con[i] ));
		    		dataList.add(new DTForm("ORDER_IDX",i+1 ));
		    		param1.put("dataList",dataList);
		    		param1.put("fnIdx", fnIdx);	
	    			result = tabcontentsDataDAO.insert(param1);
    			}
	    	}
	    	System.out.println(param1);
    	}
	
		//iclass 저장
		if(result > 0){
			// 4.1  class 저장 조건
			String[] clsIdxs= StringUtil.getStringArray(parameterMap.get("classIdx"));
			if(clsIdxs != null) {
				int clsLen = clsIdxs.length;
				for(int i = 0 ; i < clsLen ; i ++) {
					param = new HashMap<String, Object>();
					dataList = new ArrayList<DTForm>();
					
			    	dataList.add(new DTForm("CLASS_IDX", classIdx));
			    	dataList.add(new DTForm("MASTER_CODE", masterCode));
			    	dataList.add(new DTForm("DMNG_LDATE", parameterMap.get("dmgLdate")));
			    	dataList.add(new DTForm("DMNG_NAME", parameterMap.get("dmgName")));
			    	dataList.add(new DTForm("DMNG_DEPART", parameterMap.get("dmgDepart")));
			    	dataList.add(new DTForm("DMNG_PHONE", parameterMap.get("dmgPhone")));
			    	dataList.add(new DTForm("CLASS_NAME", parameterMap.get("clName")));
			    	param.put("dataList", dataList);
			    	
			    	if(targetClassIdx <= 0) {
						int targetOrdIdx = 0;			// 이동될 ord_idx
						// 이동될 ord_idx select
						Map<String, Object> targetOrdParam = new HashMap<String, Object>();
						List<DTForm> targetOrdSearchList = new ArrayList<DTForm>();
						targetOrdSearchList.add(masterSearchForm);
						targetOrdParam.put("searchList", targetOrdSearchList);
						targetOrdIdx = classOptnDAO.getMaxOrdIdx(targetOrdParam);
						
						dataList.add(new DTForm("ORDER_IDX", targetOrdIdx + 1));
						dataList.add(new DTForm("GROUP_CLASS_IDX", classIdx));
						dataList.add(new DTForm("CLASS_LEVEL", 1));
					} else {

						// target 순서 수정 data, 조건
						List<DTForm> targetDataList = null;
						List<DTForm> targetSearchList = null;
						targetSearchList = new ArrayList<DTForm>();

						//int targetPrtClassIdx = 0;		// 이동될 PARENT_CLASS_IDX 
						int targetOrdIdx = 0;			// 이동될 ord_idx
						// 이동될 ord_idx select
						Map<String, Object> targetOrdParam = new HashMap<String, Object>();
						List<DTForm> targetOrdSearchList = new ArrayList<DTForm>();
						targetOrdSearchList.add(masterSearchForm);
						
						if(ordType == 1) {
							// 위 : 선택한 단원의 첫번째 순서
							targetOrdSearchList.add(new DTForm("CLASS_IDX", targetClassIdx));
							targetOrdParam.put("searchList", targetOrdSearchList);
							targetOrdIdx = classOptnDAO.getOrdIdx(targetOrdParam);
							
							targetSearchList.add(new DTForm("ORDER_IDX", targetOrdIdx, ">="));
							// ord_idx setting
							dataList.add(new DTForm("ORDER_IDX", targetOrdIdx));
						} else {
							// 아래/내부 : 선택한 단원의 마지막 순서
							List<DTForm> targetOrdSearchList2 = new ArrayList<DTForm>();
							targetOrdSearchList2.add(new DTForm("CLASS_IDX", targetClassIdx));
							targetOrdParam.put("searchList", targetOrdSearchList);
							targetOrdParam.put("searchList2", targetOrdSearchList2);
							targetOrdIdx = classOptnDAO.getNextOrdIdx(targetOrdParam);

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
							int result1 = classOptnDAO.update(targetParam);
						}
						
						// prt_unt_cd setting
						
						// 선택한 위치의 정보 얻기
						Map<String, Object> targetPrtParam = new HashMap<String, Object>();
						List<DTForm> targetPrtSearchList = new ArrayList<DTForm>();
						targetPrtSearchList.add(masterSearchForm);
						targetPrtSearchList.add(new DTForm("CLASS_IDX", targetClassIdx));
						targetPrtParam.put("searchList", targetPrtSearchList);
						DataMap targetClassDt = classOptnDAO.getClassKeyView(targetPrtParam);
						int targetGroupClassIdx = StringUtil.getInt(targetClassDt.get("GROUP_CLASS_IDX"));
						int targetClassLevel = StringUtil.getInt(targetClassDt.get("CLASS_LEVEL"));
						int classLevel = targetClassLevel;
						int groupClassIdx = classIdx;
						int parentClassIdx = 0;
						if(ordType == 3) {
							// 내부
							parentClassIdx = targetClassIdx;
							classLevel = targetClassLevel + 1;
							groupClassIdx = targetGroupClassIdx;
						} else {
							// 위/아래
							/*Map<String, Object> targetPrtParam = new HashMap<String, Object>();
							List<DTForm> targetPrtSearchList = new ArrayList<DTForm>();
							targetPrtSearchList.add(masterSearchForm);
							targetPrtSearchList.add(new DTForm("CLASS_IDX", targetClassIdx));
							targetPrtParam.put("searchList", targetPrtSearchList);
							targetPrtClassIdx = classOptnDAO.getPrtClassIdx(targetPrtParam);*/
							parentClassIdx = StringUtil.getInt(targetClassDt.get("PARENT_CLASS_IDX"));
							if(targetClassLevel > 1) {
								groupClassIdx = targetGroupClassIdx;
								//classLevel = targetClassLevel;
							} else classLevel = 1;
						}
						dataList.add(new DTForm("PARENT_CLASS_IDX", parentClassIdx));
						dataList.add(new DTForm("GROUP_CLASS_IDX", groupClassIdx));
						dataList.add(new DTForm("CLASS_LEVEL", classLevel));
					}
					result += classOptnDAO.insert(param);
			    	
					          					   }
						 		 }
					   }

		// 5. multi file 저장
		List fileDataList = StringUtil.getList(dataMap.get("fileDataList"));
		if(fileDataList != null) {
			int fileDataSize = fileDataList.size();
			for(int i = 0 ; i < fileDataSize ; i ++) {
				Map<String, Object> fileParam = (HashMap)fileDataList.get(i);
				fileParam.put("fnIdx", fnIdx);
				fileParam.put("CLASS_IDX", classIdx);
				fileParam.put("FLE_IDX", i + 1);
				result = tabcontentsFileDAO.insert(fileParam);
			                                         }
								  }
		
		
		
		return result;
	}


	/**
	 * 수정처리 : 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param hisIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int update(String uploadModulePath, int fnIdx, int classIdx, String siteMode, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		String masterCode = JSONObjectUtil.getString(settingInfo, "dset_cate_master_code");
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	param.put("fnIdx", fnIdx);												// table flag
    	// 1. 검색조건 setting
		searchList.add(new DTForm("CLASS_IDX", classIdx));
		
		DTForm masterSearchForm = new DTForm("MASTER_CODE", masterCode);
		
		
		Object[] conIdxs = StringUtil.getStringArray(parameterMap.get("con_idx"));
		int conIdxLen = 0;
		if(conIdxs != null) conIdxLen = conIdxs.length;
		
		
		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		
		
		if(itemDataList != null) dataList.addAll(itemDataList);
		
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
    	
    	// 3. DB 저장
		int result = tabcontentsDAO.update(param);
		
		// rbs_tabcont_data_info 테이블 등록, 수정, 삭제
    	if(conIdxLen > 0)
    	{
    		
    		Object[] url;
			Object[] url1 = StringUtil.getStringArray(parameterMap.get("url"));
			Object[] url_con = StringUtil.getStringArray(parameterMap.get("url_con"));
			Map<String, Object> param1 = new HashMap<String, Object>();	
			Map<String, Object> param3 = new HashMap<String, Object>();	
			List<DTForm> searchList3 = new ArrayList<DTForm>();
			searchList3.add(new DTForm("CLASS_IDX", classIdx));
    		param3.put("fnIdx", fnIdx);
    		param3.put("searchList", searchList3);
			result = tabcontentsDataDAO.delete(param3);
			for(int i = 0 ; i < conIdxLen ; i ++)
	    	{
	    		url = StringUtil.getStringArray(parameterMap.get("con_idx"));
    			// 등록 : con_idx 없는 경우
    			if(url != null)
    			{
    				param1 = new HashMap<String, Object>();
					dataList = new ArrayList<DTForm>();
		    		dataList.add(new DTForm("CLASS_IDX", classIdx));
		    		dataList.add(new DTForm("CON_IDX",i+1 ));
		    		dataList.add(new DTForm("URL",url1[i] ));
		    		dataList.add(new DTForm("URL_CON",url_con[i] ));
		    		dataList.add(new DTForm("ORDER_IDX",i+1 ));
		    		param1.put("dataList",dataList);
		    		param1.put("fnIdx", fnIdx);	
	    			result = tabcontentsDataDAO.insert(param1);
    			}
	    	}
	    	
    	}
		
		
		// 순서 setting
		int ordType = StringUtil.getInt(parameterMap.get("ordType"));
		int targetClassIdx = StringUtil.getInt(parameterMap.get("ordIdx"));
		//iclass 저장
				if(result > 0){
					// 4.1  class 저장 조건
					String[] clsIdxs= StringUtil.getStringArray(parameterMap.get("classIdx"));
					if(clsIdxs != null) {
						int clsLen = clsIdxs.length;
						for(int i = 0 ; i < clsLen ; i ++) {
							param = new HashMap<String, Object>();
							dataList = new ArrayList<DTForm>();
					    	dataList.add(new DTForm("CLASS_IDX", classIdx));
					    	dataList.add(new DTForm("MASTER_CODE", masterCode));
					    	dataList.add(new DTForm("DMNG_LDATE", parameterMap.get("dmgLdate")));
					    	dataList.add(new DTForm("DMNG_NAME", parameterMap.get("dmgName")));
					    	dataList.add(new DTForm("DMNG_DEPART", parameterMap.get("dmgDepart")));
					    	dataList.add(new DTForm("DMNG_PHONE", parameterMap.get("dmgPhone")));
					    	dataList.add(new DTForm("CLASS_NAME", parameterMap.get("clName")));
					    	searchList.add(new DTForm("MASTER_CODE", masterCode));
					    	param.put("dataList", dataList);
					    	param.put("searchList", searchList);
					    	
					    	if(targetClassIdx <= 0) {
								int targetOrdIdx = 0;			// 이동될 ord_idx
								// 이동될 ord_idx select
								Map<String, Object> targetOrdParam = new HashMap<String, Object>();
								List<DTForm> targetOrdSearchList = new ArrayList<DTForm>();
								targetOrdSearchList.add(masterSearchForm);
								targetOrdParam.put("searchList", targetOrdSearchList);
								targetOrdIdx = classOptnDAO.getMaxOrdIdx(targetOrdParam);
								
								dataList.add(new DTForm("ORDER_IDX", targetOrdIdx + 1));
								dataList.add(new DTForm("GROUP_CLASS_IDX", classIdx));
								dataList.add(new DTForm("CLASS_LEVEL", 1));
							} else {

								// target 순서 수정 data, 조건
								List<DTForm> targetDataList = null;
								List<DTForm> targetSearchList = null;
								targetSearchList = new ArrayList<DTForm>();

								//int targetPrtClassIdx = 0;		// 이동될 PARENT_CLASS_IDX 
								int targetOrdIdx = 0;			// 이동될 ord_idx
								// 이동될 ord_idx select
								Map<String, Object> targetOrdParam = new HashMap<String, Object>();
								List<DTForm> targetOrdSearchList = new ArrayList<DTForm>();
								targetOrdSearchList.add(masterSearchForm);
								
								if(ordType == 1) {
									// 위 : 선택한 단원의 첫번째 순서
									targetOrdSearchList.add(new DTForm("CLASS_IDX", targetClassIdx));
									targetOrdParam.put("searchList", targetOrdSearchList);
									targetOrdIdx = classOptnDAO.getOrdIdx(targetOrdParam);
									
									targetSearchList.add(new DTForm("ORDER_IDX", targetOrdIdx, ">="));
									// ord_idx setting
									dataList.add(new DTForm("ORDER_IDX", targetOrdIdx));
								} else {
									// 아래/내부 : 선택한 단원의 마지막 순서
									List<DTForm> targetOrdSearchList2 = new ArrayList<DTForm>();
									targetOrdSearchList2.add(new DTForm("CLASS_IDX", targetClassIdx));
									targetOrdParam.put("searchList", targetOrdSearchList);
									targetOrdParam.put("searchList2", targetOrdSearchList2);
									targetOrdIdx = classOptnDAO.getNextOrdIdx(targetOrdParam);

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
									int result1 = classOptnDAO.update(targetParam);
								}
								
								// prt_unt_cd setting
								
								// 선택한 위치의 정보 얻기
								Map<String, Object> targetPrtParam = new HashMap<String, Object>();
								List<DTForm> targetPrtSearchList = new ArrayList<DTForm>();
								targetPrtSearchList.add(masterSearchForm);
								targetPrtSearchList.add(new DTForm("CLASS_IDX", targetClassIdx));
								targetPrtParam.put("searchList", targetPrtSearchList);
								DataMap targetClassDt = classOptnDAO.getClassKeyView(targetPrtParam);
								int targetGroupClassIdx = StringUtil.getInt(targetClassDt.get("GROUP_CLASS_IDX"));
								int targetClassLevel = StringUtil.getInt(targetClassDt.get("CLASS_LEVEL"));
								int classLevel = targetClassLevel;
								int groupClassIdx = classIdx;
								int parentClassIdx = 0;
								if(ordType == 3) {
									// 내부
									parentClassIdx = targetClassIdx;
									classLevel = targetClassLevel + 1;
									groupClassIdx = targetGroupClassIdx;
								} else {
									// 위/아래
									/*Map<String, Object> targetPrtParam = new HashMap<String, Object>();
									List<DTForm> targetPrtSearchList = new ArrayList<DTForm>();
									targetPrtSearchList.add(masterSearchForm);
									targetPrtSearchList.add(new DTForm("CLASS_IDX", targetClassIdx));
									targetPrtParam.put("searchList", targetPrtSearchList);
									targetPrtClassIdx = classOptnDAO.getPrtClassIdx(targetPrtParam);*/
									parentClassIdx = StringUtil.getInt(targetClassDt.get("PARENT_CLASS_IDX"));
									if(targetClassLevel > 1) {
										groupClassIdx = targetGroupClassIdx;
										//classLevel = targetClassLevel;
									} else classLevel = 1;
								}
								dataList.add(new DTForm("PARENT_CLASS_IDX", parentClassIdx));
								dataList.add(new DTForm("GROUP_CLASS_IDX", groupClassIdx));
								dataList.add(new DTForm("CLASS_LEVEL", classLevel));
							}
					    	result += classOptnDAO.update(param);
							          					   }
								 		 }
							   }


		// 4. multi file 신규 저장
		List fileDataList = StringUtil.getList(dataMap.get("fileDataList"));
		if(fileDataList != null) {

			// 4.1 key 얻기
			Map<String, Object> fileParam1 = new HashMap<String, Object>();
			fileParam1.put("fnIdx", fnIdx);
			fileParam1.put("CLASS_IDX", classIdx);
			int fleIdx = tabcontentsFileDAO.getNextId(fileParam1);
			
			// 4.2 DB 저장
			int fileDataSize = fileDataList.size();
			for(int i = 0 ; i < fileDataSize ; i ++) {
				Map<String, Object> fileParam = (HashMap)fileDataList.get(i);
				fileParam.put("fnIdx", fnIdx);
				fileParam.put("CLASS_IDX", classIdx);
				fileParam.put("FLE_IDX", fleIdx + i);
				result = tabcontentsFileDAO.insert(fileParam);
			}
		}

		// 5 multi file 수정
		List fileModifyDataList = StringUtil.getList(dataMap.get("fileModifyDataList"));
		if(fileModifyDataList != null) {
			int fileDataSize = fileModifyDataList.size();
			for(int i = 0 ; i < fileDataSize ; i ++) {
				Map<String, Object> fileParam = (HashMap)fileModifyDataList.get(i);
				fileParam.put("fnIdx", fnIdx);
				fileParam.put("CLASS_IDX", classIdx);
				result = tabcontentsFileDAO.update(fileParam);
			}
		}
		
		
		
		// 6. multi file 삭제
		List fileDeleteSearchList = StringUtil.getList(dataMap.get("fileDeleteSearchList"));
		if(fileDeleteSearchList != null) {

			List<Object> deleteMultiFileList = new ArrayList<Object>();
			int fileDataSize = fileDeleteSearchList.size();
			for(int i = 0 ; i < fileDataSize ; i ++) {
				Map<String, Object> fileParam = (HashMap)fileDeleteSearchList.get(i);
				fileParam.put("fnIdx", fnIdx);
				fileParam.put("CLASS_IDX", classIdx);
				// 6.1 삭제목록 select
				List<Object> deleteFileList2 = tabcontentsFileDAO.getList(fileParam);
				if(deleteFileList2 != null) deleteMultiFileList.addAll(deleteFileList2);
				// 6.2 DB 삭제
				result = tabcontentsFileDAO.cdelete(fileParam);
			}
			
			// 6.3 multi file 삭제
			FileUtil.isKeyDelete(fileRealPath, deleteMultiFileList, "FILE_SAVED_NAME");
			/*
			int deleteFileListSize = deleteFileList.size();
			if(deleteFileListSize > 0) {
				for(int i = 0 ; i < deleteFileListSize ; i ++) {
					DataMap listDt = (DataMap)deleteFileList.get(i);
					FileUtil.isDelete(fileRealPath, StringUtil.getString(listDt.get("FILE_SAVED_NAME")));
				}
			}*/
		}
		
		// 7. file(단일항목) 삭제
		List deleteFileList = StringUtil.getList(dataMap.get("deleteFileList"));
		if(deleteFileList != null) {
			FileUtil.isDelete(fileRealPath, deleteFileList);
			/*
			int deleteFileListSize = deleteFileList.size();
			if(deleteFileListSize > 0) {
				String fileName = null;
				for(int i = 0 ; i < deleteFileListSize ; i ++) {
					fileName = StringUtil.getString(deleteFileList.get(i));
					FileUtil.isDelete(fileRealPath, fileName);
				}
			}*/
		}
		
		
		return result;
	}

	/**
	 * 삭제 전체 목록 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
    @Override
	public int getDeleteCount(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	return tabcontentsDAO.getDeleteCount(param);
    }

    /**
	 * 삭제 전체 목록
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDeleteList(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return tabcontentsDAO.getDeleteList(param);
	}

	/**
	 * 삭제처리 : 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
	 * @param fnIdx
	 * @param deleteIdxs
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	@Override
	public int delete(int fnIdx, String[] deleteIdxs, String regiIp,String masterCode) throws Exception {
		
		if(deleteIdxs == null) return 0;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	param.put("fnIdx", fnIdx);												// table flag
		
		// 1. 저장조건
		searchList.add(new DTForm("CLASS_IDX", deleteIdxs));
		
		// 2. 저장 항목
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
		
		// 3. DB 저장
		int result = tabcontentsDAO.delete(param);
		if(result > 0)
		{
			Map<String, Object> param1 = new HashMap<String, Object>();
			List<DTForm> searchList1 = new ArrayList<DTForm>();
			List<DTForm> dataList1 = new ArrayList<DTForm>();
			// 저장조건
			
			searchList1.add(new DTForm("MASTER_CODE", masterCode));
			searchList1.add(new DTForm("CLASS_IDX", deleteIdxs));
			
			// 저장 항목
	    	dataList1.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
	    	dataList1.add(new DTForm("LAST_MODI_ID", loginMemberId));
	    	dataList1.add(new DTForm("LAST_MODI_NAME", loginMemberName));
	    	dataList1.add(new DTForm("LAST_MODI_IP", regiIp));

			param1.put("searchList", searchList1);
			param1.put("dataList", dataList1);
			result=classOptnDAO.delete(param1);
		}
		return result;
	}

	/**
	 * 복원처리 : 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
	 * @param fnIdx
	 * @param restoreIdxs
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	@Override
	public int restore(int fnIdx, String[] restoreIdxs, String regiIp, String masterCode) throws Exception {
		if(restoreIdxs == null) return 0;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	param.put("fnIdx", fnIdx);												// table flag

		// 1. 저장조건
		searchList.add(new DTForm("CLASS_IDX", restoreIdxs));

		// 2. 저장 항목
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
		
		// 3. DB 저장
		int result = tabcontentsDAO.restore(param);
		if(result > 0)
		{
			Map<String, Object> param1 = new HashMap<String, Object>();
			List<DTForm> searchList1 = new ArrayList<DTForm>();
			List<DTForm> dataList1 = new ArrayList<DTForm>();

			searchList1.add(new DTForm("MASTER_CODE", masterCode));
			searchList1.add(new DTForm("CLASS_IDX", restoreIdxs));
			
			// 저장 항목
	    	dataList1.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
	    	dataList1.add(new DTForm("LAST_MODI_ID", loginMemberId));
	    	dataList1.add(new DTForm("LAST_MODI_NAME", loginMemberName));
	    	dataList1.add(new DTForm("LAST_MODI_IP", regiIp));

			param1.put("searchList", searchList1);
			param1.put("dataList", dataList1);
			
			result = classOptnDAO.restore(param1);
		}
		return result;
	}

	/**
	 * 완전삭제처리 : 화면에 조회된 정보를 데이터베이스에서 삭제
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param deleteIdxs
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@Override
	public int cdelete(String uploadModulePath, int fnIdx, String[] deleteIdxs, JSONObject items, JSONArray itemOrder, String masterCode ) throws Exception {
		if(deleteIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
    	param.put("fnIdx", fnIdx);												// table flag
		
		// 1. 저장조건
		searchList.add(new DTForm("CLASS_IDX", deleteIdxs));
		param.put("searchList", searchList);
		
		// 2. 삭제할 파일 select
		List<Object> deleteFileList = null;
		List deleteFileColumnList = ModuleUtil.getFileObjectList(items, itemOrder);
		if(deleteFileColumnList != null) {
			param.put("columnList", deleteFileColumnList);
			
			deleteFileList = tabcontentsDAO.getDeleteFileList(param);
		}
		
		// 3. delete
		int result = tabcontentsDAO.cdelete(param);
		if(result > 0) {
			// 4. 파일(단일항목) 삭제
			if(deleteFileList != null) {
				String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
				FileUtil.isKeyDelete(fileRealPath, deleteFileList);
			}
			
		}
	
			Map<String, Object> param1 = new HashMap<String, Object>();
			List<DTForm> searchList1 = new ArrayList<DTForm>();
			
			// 저장조건
			
			searchList1.add(new DTForm("MASTER_CODE", masterCode));
			searchList1.add(new DTForm("CLASS_IDX", deleteIdxs));
			
			param1.put("searchList", searchList1);
			
			// 2. delete
			result = classOptnDAO.cdelete(param1);
	
			if(result > 0)
			{
			// mst_cd에 해당하는 전체 순서 update
			param1 = new HashMap<String, Object>();
			searchList1 = new ArrayList<DTForm>();
			// 저장조건
			searchList1.add(new DTForm("MASTER_CODE", masterCode));
			param1.put("searchList", searchList);
			
			result = classOptnDAO.updateTotOrdIdx(param);
			}
		
		
		
		return result;
	}

	/**
	 * mult file 전체 목록 : 항목ID에 대한 HashMap
	 * @param fnIdx
	 * @param classIdx
	 * @param items
	 * @param itemOrder
	 * @return
	 */
	public HashMap<String, Object> getMultiFileHashMap(int fnIdx, int classIdx, JSONObject items, JSONArray itemOrder) {
		HashMap<String, Object> resultHashMap = new HashMap<String, Object>();
		if(!JSONObjectUtil.isEmpty(itemOrder) && !JSONObjectUtil.isEmpty(items)) {
			int searchOrderSize = itemOrder.size();
			for(int i = 0 ; i < searchOrderSize ; i ++) {
				String itemId = JSONObjectUtil.getString(itemOrder, i);
				JSONObject item = JSONObjectUtil.getJSONObject(items, itemId);
				int formatType = JSONObjectUtil.getInt(item, "format_type");
				int objectType = JSONObjectUtil.getInt(item, "object_type");
				if(formatType == 0 && objectType == 9) {
					// mult file
					Map<String, Object> param = new HashMap<String, Object>();
					List<DTForm> searchList = new ArrayList<DTForm>();
			    	searchList.add(new DTForm("A.CLASS_IDX", classIdx));
			    	searchList.add(new DTForm("A.ITEM_ID", itemId));
					param.put("searchList", searchList);
			    	param.put("fnIdx", fnIdx);
					resultHashMap.put(itemId, tabcontentsFileDAO.getList(param));
				}
			}
		}
		
		return resultHashMap;
	}
	
	
}
