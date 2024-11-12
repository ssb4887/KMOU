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
import rbs.modules.contents.mapper.BranchMapper;
import rbs.modules.contents.mapper.ContVersionMapper;
import rbs.modules.contents.mapper.ContWorkMapper;
import rbs.modules.contents.mapper.ContentsMapper;
import rbs.modules.contents.service.BranchService;

/**
 * 기관정보관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 */
@Service("branchService")
public class BranchServiceImpl extends EgovAbstractServiceImpl implements BranchService {

	@Value("${Globals.locale.lang.use}")
	protected int useLang;						// 언어사용여부

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	@Resource(name="contentsMapper")
	private ContentsMapper contentsDAO;
	
	@Resource(name="branchMapper")
	private BranchMapper branchDAO;
	
	@Resource(name="contVersionMapper")
	private ContVersionMapper contVersionDAO;
	
	@Resource(name="contWorkMapper")
	private ContWorkMapper contWorkDAO;

    /**
     * 전체 목록 수
	 * @param mapFlag
	 * @param param
	 * @return
	 */
    @Override
	public int getTotalCount(Map<String, Object> param) {
    	return branchDAO.getTotalCount(param);
    }

	/**
	 * 전체 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(String lang, Map<String, Object> param) {
		return branchDAO.getList(lang, param);
	}

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getModify(String lang, Map<String, Object> param) {
		DataMap viewDAO = branchDAO.getModify(lang, param);
		return viewDAO;
	}
    
	/**
	 * 기본정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param Map 등록정보
	 * @return result 등록결과
	 * @throws Exception
	 */
	@Override
	public int insert(String contCd, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		//고유아이디 셋팅
		Map<String, Object> sparam1 = new HashMap<String, Object>();
		List<DTForm> searchList1 = new ArrayList<DTForm>();
		searchList1.add(new DTForm("A.CONTENTS_CODE", contCd));
		sparam1.put("searchList", searchList1);
		int branchIdx = branchDAO.getNextId(sparam1);
    	
		// 항목설정으로 저장항목 setting
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(parameterMap, settingInfo, items, itemOrder);

		if(dataMap == null || dataMap.size() == 0) return -1;

		// 저장
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		Map<String, Object> versionParam = new HashMap<String, Object>();
		List<DTForm> versionDataList = new ArrayList<DTForm>();
		Map<String, Object> langVersionParam = new HashMap<String, Object>();
		List<DTForm> langVersionDataList = new ArrayList<DTForm>();
		Map<String, Object> workParam = new HashMap<String, Object>();
		List<DTForm> workDataList = new ArrayList<DTForm>();
		
		dataList.add(new DTForm("CONTENTS_CODE", contCd));
		dataList.add(new DTForm("BRANCH_IDX", branchIdx));

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
		
		versionDataList.addAll(dataList);
		langVersionDataList.addAll(dataList);

		// insert 항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
    	param.put("dataList", dataList);
    	
		// insert
		int result = branchDAO.insert(param);
		
		if(result > 0) {
			int versionIdx = 1;

			// 2. Version 저장
			String orgLang = StringUtil.getString(parameterMap.get("orgLang"), rbsMessageSource.getLocaleLang());
			versionDataList.add(new DTForm("VER_IDX", versionIdx));
			versionDataList.add(new DTForm("ORG_LANG", orgLang));
			versionParam.put("dataList", versionDataList);
			result = contVersionDAO.insert(versionParam);
			
			// 2.2 Version lang 저장
			langVersionDataList.add(new DTForm("VER_IDX", versionIdx));
			langVersionDataList.add(new DTForm("TORG_LANG", orgLang));
			langVersionParam.put("dataList", langVersionDataList);
			result = contVersionDAO.allInsertLang(langVersionParam);
			
			// 3. Work 저장 : contType에 따른 table
			Map<String, Object> contParam = new HashMap<String, Object>();
			List<DTForm> contSearchList = new ArrayList<DTForm>();
			contSearchList.add(new DTForm("CONTENTS_CODE", contCd));
			contParam.put("searchList", contSearchList);
			String contentsType = contentsDAO.getContentsType(contParam);
			
			workDataList.add(new DTForm("CONTENTS_CODE", contCd));
	    	workDataList.add(new DTForm("BRANCH_IDX", branchIdx));
			workDataList.add(new DTForm("VER_IDX", versionIdx));
			workParam.put("dataList", workDataList);
			result = contWorkDAO.allInsert(contentsType, workParam);

			int result2 = LogHelper.insert("Branch", contCd, 1, branchIdx);
		}
		return result;
	}

	/**
	 * 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param param 수정정보
	 * @throws Exception
	 */
	@Override
	public int update(String lang, String contCd, int branchIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		Map<String, Object> langParam = new HashMap<String, Object>();
		List<DTForm> langDataList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("CONTENTS_CODE", contCd));
		searchList.add(new DTForm("BRANCH_IDX", branchIdx));
		
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
    	
		int result = branchDAO.update(param);	
		// 언어 사용하는 경우 : 언어 테이블에 저장
		if(useLang == 1 && result > 0) {
			
			// 언어 사용하는 경우 : 언어 테이블에 저장
			langDataList.add(new DTForm("BRANCH_NAME", parameterMap.get("branchName")));
			langDataList.add(new DTForm("REMARKS", parameterMap.get("remarks")));
			
	    	langParam.put("dataList", langDataList);
	    	langParam.put("searchList", searchList);
			branchDAO.updateLang(lang, langParam);	
		}

		if(result > 0) {
			int result2 = LogHelper.insert("Branch", contCd, 2, branchIdx);
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
	public int getDeleteCount(Map<String, Object> param) {
    	return branchDAO.getDeleteCount(param);
    }

	/**
	 * 삭제 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDeleteList(Map<String, Object> param) {
		return branchDAO.getDeleteList(param);
	}

	/**
	 * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
	 * @param param 삭제정보
	 * @throws Exception
	 */
	@Override
	public int delete(String contCd, String[] deleteIdxs, String regiIp) throws Exception {
		if(deleteIdxs == null) return 0;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		
		// 저장조건
		
		searchList.add(new DTForm("CONTENTS_CODE", contCd));
		searchList.add(new DTForm("BRANCH_IDX", deleteIdxs));
		/*
		String keyColumnId = "BRANCH_IDX";
		int deleteIdxsLen = deleteIdxs.length;
		int lastIdx = deleteIdxsLen - 1;

		if(deleteIdxsLen == 1) {
			searchList.add(new DTForm(keyColumnId, deleteIdxs[0], 0, "IN (", "AND", null, ")"));
		} else {
    		for(int x = 0 ; x < deleteIdxsLen ; x ++) {
    			if(x == 0) searchList.add(new DTForm(keyColumnId, deleteIdxs[x], 0, "IN (", "AND", null, null));
    			else if(x == lastIdx) searchList.add(new DTForm(null, deleteIdxs[x], 0, "", ", ", null, ")"));
    			else searchList.add(new DTForm(null, deleteIdxs[x], 0, "", ", "));
    		}
		}*/
		
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
		
		int result = branchDAO.delete(param);

		if(result > 0) {
			int result2 = LogHelper.insert("Branch", contCd, 3, deleteIdxs);
		}
		return result;
	}

	/**
	 * 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
	 * @param param 복원정보
	 * @throws Exception
	 */
	@Override
	public int restore(String contCd, String[] restoreIdxs, String regiIp) throws Exception {
		if(restoreIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		// 저장조건
		
		searchList.add(new DTForm("CONTENTS_CODE", contCd));
		searchList.add(new DTForm("BRANCH_IDX", restoreIdxs));
		/*
		String keyColumnId = "BRANCH_IDX";
		int restoreIdxsLen = restoreIdxs.length;
		int lastIdx = restoreIdxsLen - 1;

		if(restoreIdxsLen == 1) {
			searchList.add(new DTForm(keyColumnId, restoreIdxs[0], 0, "IN (", "AND", null, ")"));
		} else {
    		for(int x = 0 ; x < restoreIdxsLen ; x ++) {
    			if(x == 0) searchList.add(new DTForm(keyColumnId, restoreIdxs[x], 0, "IN (", "AND", null, null));
    			else if(x == lastIdx) searchList.add(new DTForm(null, restoreIdxs[x], 0, "", ", ", null, ")"));
    			else searchList.add(new DTForm(null, restoreIdxs[x], 0, "", ", "));
    		}
		}
		*/
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
		
		int result = branchDAO.restore(param);

		if(result > 0) {
			int result2 = LogHelper.insert("Branch", contCd, 4, restoreIdxs);
		}
		return result;
	}

	/**
	 * 화면에 조회된 정보를 데이터베이스에서 삭제
	 * @param param 완전삭제정보
	 * @throws Exception
	 */
	@Override
	public int cdelete(String uploadModulePath, String contCd, String[] deleteIdxs, JSONObject items, JSONArray itemOrder) throws Exception {
		if(deleteIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		// 저장조건
		searchList.add(new DTForm("CONTENTS_CODE", contCd));
		searchList.add(new DTForm("BRANCH_IDX", deleteIdxs));
		param.put("searchList", searchList);
		
		// 2. delete
		int result = branchDAO.cdelete(param);
		
		if(result > 0) {
			// 파일 삭제
			String filePath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath + File.separator + contCd + File.separator;
			String fileRealPath = null;
			for(String deleteIdx : deleteIdxs) {
				fileRealPath = filePath + deleteIdx;
				FileUtil.isDelete(fileRealPath);
			}

			int result2 = LogHelper.insert("Branch", contCd, 5, deleteIdxs);
		}
		return result;
	}

}