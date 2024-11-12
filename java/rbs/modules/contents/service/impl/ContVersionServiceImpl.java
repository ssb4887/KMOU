package rbs.modules.contents.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

import rbs.egovframework.LoginVO;
import rbs.egovframework.util.LogHelper;
import rbs.modules.contents.mapper.ContVersionMapper;
import rbs.modules.contents.mapper.ContWorkMapper;
import rbs.modules.contents.mapper.ContentsMapper;
import rbs.modules.contents.service.ContVersionService;

/**
 * 기관정보관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 */
@Service("contVersionService")
public class ContVersionServiceImpl extends EgovAbstractServiceImpl implements ContVersionService {

	@Value("${Globals.locale.lang.use}")
	protected int useLang;						// 언어사용여부

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	@Resource(name="contentsMapper")
	private ContentsMapper contentsDAO;
	
	@Resource(name="contVersionMapper")
	private ContVersionMapper contVersionDAO;
	
	@Resource(name="contWorkMapper")
	private ContWorkMapper contWorkDAO;
	

	@Override
	public List<Object> getCopyVerList(String lang, Map<String, Object> param) {
		return contVersionDAO.getOptnList(lang, param);
	}

    /**
     * 전체 목록 수
	 * @param mapFlag
	 * @param param
	 * @return
    @Override
	public int getTotalCount(Map<String, Object> param) {
    	return contVersionDAO.getTotalCount(param);
    }
	 */

	/**
	 * 전체 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(String lang, Map<String, Object> param) {
		return contVersionDAO.getList(lang, param);
	}

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getModify(String lang, Map<String, Object> param) {
		DataMap viewDAO = contVersionDAO.getModify(lang, param);
		return viewDAO;
	}
    
	/**
	 * 기본정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param Map 등록정보
	 * @return result 등록결과
	 * @throws Exception
	 */
	@Override
	public int insert(String uploadModulePath, String lang, String contCd, int branchIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, JSONObject moduleItem) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		//고유아이디 셋팅
		Map<String, Object> langSparam1 = new HashMap<String, Object>();
		List<DTForm> langSearchList1 = new ArrayList<DTForm>();
		langSearchList1.add(new DTForm("A.CONTENTS_CODE", contCd));
		langSearchList1.add(new DTForm("A.BRANCH_IDX", branchIdx));
		langSparam1.put("searchList", langSearchList1);
		int langVersionIdx = contVersionDAO.getNextId(lang, langSparam1);
		
		// version table에 같은 버전 유무 체크
		Map<String, Object> sparam1 = new HashMap<String, Object>();
		List<DTForm> searchList1 = new ArrayList<DTForm>();
		searchList1.add(new DTForm("A.CONTENTS_CODE", contCd));
		searchList1.add(new DTForm("A.BRANCH_IDX", branchIdx));
		searchList1.add(new DTForm("A.VER_IDX", langVersionIdx));
		sparam1.put("searchList", searchList1);
		int versionCnt = contVersionDAO.getCount(sparam1);
    	
		// 항목설정으로 저장항목 setting
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(parameterMap, settingInfo, items, itemOrder);

		if(dataMap == null || dataMap.size() == 0) return -1;

		// 저장
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		Map<String, Object> workParam = new HashMap<String, Object>();
		List<DTForm> workDataList = new ArrayList<DTForm>();
		
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
		
		// 복제여부
		int copyVerIdx = StringUtil.getInt(parameterMap.get("copyVerIdx"));
		
		int result = 0;
		if(versionCnt == 0) {
			// version table에 같은 버전이 없는 경우 : version table 등록
			Map<String, Object> sparam2 = new HashMap<String, Object>();
			List<DTForm> searchList2 = new ArrayList<DTForm>();
			searchList2.add(new DTForm("A.CONTENTS_CODE", contCd));
			searchList2.add(new DTForm("A.BRANCH_IDX", branchIdx));
			sparam2.put("searchList", searchList2);
			int versionIdx = contVersionDAO.getNextId(sparam2);
			/*
			if(copyVerIdx > 0) {
				// 복제등록
				dataList.add(new DTForm("COPY_CONTENTS_CODE", contCd));
				dataList.add(new DTForm("COPY_BRANCH_IDX", branchIdx));
				
			} else {
				// 신규등록
			}*/
			
			dataList.add(new DTForm("CONTENTS_CODE", contCd));
			dataList.add(new DTForm("BRANCH_IDX", branchIdx));
			dataList.add(new DTForm("VER_IDX", versionIdx));
			
			// insert 항목
			List itemDataList = StringUtil.getList(dataMap.get("dataList"));
			if(itemDataList != null) dataList.addAll(itemDataList);
			    	
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
			result = contVersionDAO.insert(param);
		} else {
			// version table에 같은 버전이 있는 경우
			result = 1;
		}
		
		if(result > 0) {

			// 2. 언어, 작업 저장 : contType에 따른 table
			// 언어데이터
			Map<String, Object> langParam = new HashMap<String, Object>();
			List<DTForm> langDataList = new ArrayList<DTForm>();
			langDataList.add(new DTForm("CONTENTS_CODE", contCd));
			langDataList.add(new DTForm("BRANCH_IDX", branchIdx));
			langDataList.add(new DTForm("VER_IDX", langVersionIdx));
			
			    	
			langDataList.add(new DTForm("REGI_IDX", loginMemberIdx));
			langDataList.add(new DTForm("REGI_ID", loginMemberId));
			langDataList.add(new DTForm("REGI_NAME", loginMemberName));
			langDataList.add(new DTForm("REGI_IP", regiIp));
	    	
			langDataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
			langDataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
			langDataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
			langDataList.add(new DTForm("LAST_MODI_IP", regiIp));
			
			// 작업 데이터
			Map<String, Object> contParam = new HashMap<String, Object>();
			List<DTForm> contSearchList = new ArrayList<DTForm>();
			contSearchList.add(new DTForm("CONTENTS_CODE", contCd));
			contParam.put("searchList", contSearchList);
			String contentsType = contentsDAO.getContentsType(contParam);
			
			workDataList.add(new DTForm("CONTENTS_CODE", contCd));
	    	workDataList.add(new DTForm("BRANCH_IDX", branchIdx));
			workDataList.add(new DTForm("VER_IDX", langVersionIdx));
			
			if(copyVerIdx > 0) {
				// 복제등록
				// 언어 테이블 등록
				JSONObject langItemInfo = JSONObjectUtil.getJSONObject(moduleItem, "versionlang_item_info");
				JSONObject langItems = JSONObjectUtil.getJSONObject(langItemInfo, "items");
				JSONArray langItemOrder = JSONObjectUtil.getJSONArray(langItemInfo, "writeproc_order");
				
				List<DTForm> langSearchList = new ArrayList<DTForm>();
				List<DTForm> langCopyDataList = ModuleUtil.getItemInfoColumnList(langItems, langItemOrder);
				if(langCopyDataList != null) langDataList.addAll(langCopyDataList);

				langSearchList.add(new DTForm("CONTENTS_CODE", contCd));
				langSearchList.add(new DTForm("BRANCH_IDX", branchIdx));
				langSearchList.add(new DTForm("VER_IDX", copyVerIdx));
				
				langParam.put("dataList", langDataList);
				langParam.put("searchList", langSearchList);
				result = contVersionDAO.copyInsertLang(lang, langParam);
				
				
				// 작업 테이블 등록
				JSONObject workItemInfo = JSONObjectUtil.getJSONObject(moduleItem, "work_" + contentsType + "_item_info");
				JSONObject workItems = JSONObjectUtil.getJSONObject(workItemInfo, "items");
				JSONArray workItemOrder = JSONObjectUtil.getJSONArray(workItemInfo, "writeproc_order");
				
				List<DTForm> workSearchList = new ArrayList<DTForm>();
				List<DTForm> workCopyDataList = ModuleUtil.getItemInfoColumnList(workItems, workItemOrder);
				if(workCopyDataList != null) workDataList.addAll(workCopyDataList);
				//workParam.put("CONTENTS_TYPE", contentsType);

				workSearchList.add(new DTForm("CONTENTS_CODE", contCd));
				workSearchList.add(new DTForm("BRANCH_IDX", branchIdx));
				workSearchList.add(new DTForm("VER_IDX", copyVerIdx));
				
				workParam.put("dataList", workDataList);
				workParam.put("searchList", workSearchList);
				result = contWorkDAO.copyInsert(lang, contentsType, workParam);
				
				if(result > 0) {
					// 업로드 파일 복제
					String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath + File.separator + contCd + File.separator + branchIdx + File.separator;
					String sourceFilePath = fileRealPath + copyVerIdx;
					String targetFilePath = fileRealPath + langVersionIdx;
					
					FileUtil.copy(sourceFilePath, targetFilePath);
				}
			} else {
				// 신규등록
				
				// 언어 테이블 등록
				langDataList.add(new DTForm("LOCALE_LANG", lang));
				langParam.put("dataList", langDataList);
				result = contVersionDAO.insertLang(lang, langParam);
				
				// 작업 테이블 등록
				workDataList.add(new DTForm("LOCALE_LANG", lang));
				workParam.put("dataList", workDataList);
				result = contWorkDAO.insert(contentsType, workParam);
			}
    		int result2 = LogHelper.insert("버전", contCd + "," + branchIdx, 1, langVersionIdx);
		}
		return result;
	}

	/**
	 * 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param param 수정정보
	 * @throws Exception
	 */
	@Override
	public int update(String lang, String contCd, int branchIdx, int versionIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		Map<String, Object> langParam = new HashMap<String, Object>();
		List<DTForm> langDataList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("CONTENTS_CODE", contCd));
		searchList.add(new DTForm("BRANCH_IDX", branchIdx));
		searchList.add(new DTForm("VER_IDX", versionIdx));
		
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


		langDataList.addAll(dataList);
		
		// 항목설정으로 저장항목 setting
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(parameterMap, settingInfo, items, itemOrder);
		
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
    	param.put("dataList", dataList);
    	param.put("searchList", searchList);
    	
		int result = contVersionDAO.update(param);	
		// 언어 사용하는 경우 : 언어 테이블에 저장
		if(useLang == 1 && result > 0) {
			
			// 언어 사용하는 경우 : 언어 테이블에 저장
			langDataList.add(new DTForm("REMARKS", parameterMap.get("remarks")));
			
	    	langParam.put("dataList", langDataList);
	    	langParam.put("searchList", searchList);
			contVersionDAO.updateLang(lang, langParam);	
		}
		if(result > 0) {
    		int result2 = LogHelper.insert("버전", contCd + "," + branchIdx, 2, versionIdx);
		}
		
		return result;
	}

    /**
     * 삭제 목록 수
	 * @param mapFlag
	 * @param param
	 * @return
	 */
    @Override
	public int getDeleteCount(String lang, Map<String, Object> param) {
    	return contVersionDAO.getDeleteCount(lang, param);
    }

	/**
	 * 삭제 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDeleteList(String lang, Map<String, Object> param) {
		return contVersionDAO.getDeleteList(lang, param);
	}

	/**
	 * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
	 * @param param 삭제정보
	 * @throws Exception
	 */
	@Override
	public int delete(String lang, String contCd, int branchIdx, int[] deleteIdxs, String regiIp) throws Exception {
		if(deleteIdxs == null) return 0;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		
		// 저장조건
		searchList.add(new DTForm("CONTENTS_CODE", contCd));
		searchList.add(new DTForm("BRANCH_IDX", branchIdx));
		searchList.add(new DTForm("VER_IDX", deleteIdxs));
		
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
		int result = contVersionDAO.deleteLang(lang, param);
		
		//int result = contVersionDAO.delete(param);
		if(result > 0) {
			int result2 = LogHelper.insert("버전", contCd + "," + branchIdx, 3, deleteIdxs);
		}
		return result;
	}

	/**
	 * 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
	 * @param param 복원정보
	 * @throws Exception
	 */
	@Override
	public int restore(String lang, String contCd, int branchIdx, int[] restoreIdxs, String regiIp) throws Exception {
		if(restoreIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		// 저장조건
		
		searchList.add(new DTForm("CONTENTS_CODE", contCd));
		searchList.add(new DTForm("BRANCH_IDX", branchIdx));
		searchList.add(new DTForm("VER_IDX", restoreIdxs));
		
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
		
		int result = contVersionDAO.restoreLang(lang, param);
		if(result > 0) {
			int result2 = LogHelper.insert("버전", contCd + "," + branchIdx, 4, restoreIdxs);
		}
		return result;
	}

	/**
	 * 화면에 조회된 정보를 데이터베이스에서 삭제
	 * @param param 완전삭제정보
	 * @throws Exception
	 */
	@Override
	public int cdelete(String uploadModulePath, String lang, String contCd, int branchIdx, int[] deleteIdxs, JSONObject items, JSONArray itemOrder) throws Exception {
		if(deleteIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		// 저장조건
		
		searchList.add(new DTForm("CONTENTS_CODE", contCd));
		searchList.add(new DTForm("BRANCH_IDX", branchIdx));
		searchList.add(new DTForm("VER_IDX", deleteIdxs));
		
		param.put("searchList", searchList);
		
		// 2. delete
		int result = contVersionDAO.cdeleteLang(lang, param);
		
		if(result > 0) {
			// 버전 삭제 : 등록된 언어 버전이 없는 경우 버전 페이블의 데이터도 삭제 
			int versionCnt = contVersionDAO.getTotalLangCount(param);
			if(versionCnt == 0) {
				result = contVersionDAO.cdelete(param);
				
				// 파일 삭제
				String filePath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath + File.separator + contCd + File.separator + branchIdx + File.separator;
				String fileRealPath = null;
				for(int deleteIdx : deleteIdxs) {
					fileRealPath = filePath + deleteIdx;
					FileUtil.isDelete(fileRealPath);
				}
			}
			int result2 = LogHelper.insert("버전", contCd + "," + branchIdx, 5, deleteIdxs);
		}
		
		return result;
	}

}