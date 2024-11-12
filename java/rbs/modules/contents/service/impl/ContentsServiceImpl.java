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
import rbs.modules.contents.service.ContentsService;

/**
 * 기관정보관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 */
@Service("contentsService")
public class ContentsServiceImpl extends EgovAbstractServiceImpl implements ContentsService {
	
	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;

	@Value("${Globals.locale.lang.use}")
	protected int useLang;						// 언어사용여부
	
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
	public int getTotalCount(String lang, Map<String, Object> param) {
    	return contentsDAO.getTotalCount(lang, param);
    }

	/**
	 * 전체 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(String lang, Map<String, Object> param) {
		return contentsDAO.getList(lang, param);
	}

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getView(String lang, Map<String, Object> param) {
		DataMap viewDAO = contentsDAO.getView(lang, param);
		return viewDAO;
	}

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getModify(String lang, Map<String, Object> param) {
		DataMap viewDAO = contentsDAO.getModify(lang, param);
		return viewDAO;
	}
    
	/**
	 * 기본정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param Map 등록정보
	 * @return result 등록결과
	 * @throws Exception
	 */
	@Override
	public int insert(String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		//고유아이디 셋팅
		String contCd = contentsDAO.getNextId(null);
    	
		// 항목설정으로 저장항목 setting
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(parameterMap, settingInfo, items, itemOrder);

		if(dataMap == null || dataMap.size() == 0) return -1;


		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		Map<String, Object> branchParam = new HashMap<String, Object>();
		List<DTForm> branchDataList = new ArrayList<DTForm>();
		Map<String, Object> versionParam = new HashMap<String, Object>();
		List<DTForm> versionDataList = new ArrayList<DTForm>();
		Map<String, Object> langVersionParam = new HashMap<String, Object>();
		List<DTForm> langVersionDataList = new ArrayList<DTForm>();
		Map<String, Object> workParam = new HashMap<String, Object>();
		List<DTForm> workDataList = new ArrayList<DTForm>();
		
		// 저장 항목
    	dataList.add(new DTForm("CONTENTS_CODE", contCd));
    	
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
    	
    	branchDataList.addAll(dataList);
		
		// 1. Content 저장
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
    	param.put("dataList", dataList);
    	
		int result = contentsDAO.insert(param);
		
		if(result > 0) {
			int branchIdx = 1;
			int versionIdx = 1;
			// 2. Branch 저장
			branchDataList.add(new DTForm("BRANCH_IDX", branchIdx));
	    	versionDataList.addAll(branchDataList);
			langVersionDataList.addAll(branchDataList);
	    	
			branchDataList.add(new DTForm("BRANCH_TYPE", StringUtil.getString(parameterMap.get("branchType"), RbsProperties.getProperty("Globals.contents.default.branchType"))));
			branchDataList.add(new DTForm("BRANCH_NAME", rbsMessageSource.getMessage("branch.default.name")));
			branchParam.put("dataList", branchDataList);
			result = branchDAO.insert(branchParam);

			// 3. Version 저장
			String orgLang = StringUtil.getString(parameterMap.get("orgLang"), rbsMessageSource.getLocaleLang());
			versionDataList.add(new DTForm("VER_IDX", versionIdx));
			versionDataList.add(new DTForm("ORG_LANG", orgLang));
			versionParam.put("dataList", versionDataList);
			result = contVersionDAO.insert(versionParam);
			
			// 3.2 Version lang 저장
			langVersionDataList.add(new DTForm("VER_IDX", versionIdx));
			langVersionDataList.add(new DTForm("TORG_LANG", orgLang));
			langVersionParam.put("dataList", langVersionDataList);
			result = contVersionDAO.allInsertLang(langVersionParam);
			
			// 4. Work 저장 : contType에 따른 table
			workDataList.add(new DTForm("CONTENTS_CODE", contCd));
	    	workDataList.add(new DTForm("BRANCH_IDX", branchIdx));
			workDataList.add(new DTForm("VER_IDX", versionIdx));
			
			String contentsType = StringUtil.getString(parameterMap.get("contType"), RbsProperties.getProperty("Globals.contents.default.contType"));
			workParam.put("dataList", workDataList);
			result = contWorkDAO.allInsert(contentsType, workParam);
    		int result2 = LogHelper.insert(1, contCd);
		}
		
		return result;
	}

	/**
	 * 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param param 수정정보
	 * @throws Exception
	 */
	@Override
	public int update(String lang, String contCd, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		List<DTForm> langDataList = new ArrayList<DTForm>();
		searchList.add(new DTForm("CONTENTS_CODE", contCd));
		
		// 항목설정으로 저장항목 setting
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(parameterMap, settingInfo, items, itemOrder);
		
		if(dataMap == null || dataMap.size() == 0) return -1;
		
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
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
    	
    	param.put("dataList", dataList);
    	param.put("searchList", searchList);
    	
    	// 저장 : 언어테이블도 저장
		int result = contentsDAO.update(param);
		
		// 언어 사용하는 경우 : 언어 테이블에 저장
		langDataList.add(new DTForm("CONTENTS_NAME", parameterMap.get("contName")));
		langDataList.add(new DTForm("REMARKS", parameterMap.get("remarks")));

		Map<String, Object> langParam = new HashMap<String, Object>();
		langParam.put("dataList", langDataList);
		langParam.put("searchList", searchList);
		if(useLang == 1 && result > 0) result = contentsDAO.updateLang(lang, langParam);

		if(result > 0) {
    		int result2 = LogHelper.insert(2, contCd);
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
    	return contentsDAO.getDeleteCount(lang, param);
    }

	/**
	 * 삭제 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDeleteList(String lang, Map<String, Object> param) {
		return contentsDAO.getDeleteList(lang, param);
	}

	/**
	 * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
	 * @param param 삭제정보
	 * @throws Exception
	 */
	@Override
	public int delete(String[] deleteIdxs, String regiIp) throws Exception {
		if(deleteIdxs == null) return 0;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		
		// 저장조건
		searchList.add(new DTForm("CONTENTS_CODE", deleteIdxs));
		
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
		
		int result = contentsDAO.delete(param);
		if(result > 0) {
    		int result2 = LogHelper.insert(3, deleteIdxs);
		}
		return result;
	}

	/**
	 * 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
	 * @param param 복원정보
	 * @throws Exception
	 */
	@Override
	public int restore(String[] restoreIdxs, String regiIp) throws Exception {
		if(restoreIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		// 저장조건
		searchList.add(new DTForm("CONTENTS_CODE", restoreIdxs));
		
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
		
		int result = contentsDAO.restore(param);
		if(result > 0) {
    		int result2 = LogHelper.insert(4, restoreIdxs);
		}
		return result;
	}

	/**
	 * 화면에 조회된 정보를 데이터베이스에서 삭제
	 * @param param 완전삭제정보
	 * @throws Exception
	 */
	@Override
	public int cdelete(String uploadModulePath, String[] deleteIdxs, JSONObject items, JSONArray itemOrder) throws Exception {
		if(deleteIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		// 저장조건
		searchList.add(new DTForm("CONTENTS_CODE", deleteIdxs));
		
		param.put("searchList", searchList);
		
		// 2. delete
		int result = contentsDAO.cdelete(param);	
		
		if(result > 0) {
			// 파일 삭제
			String filePath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath + File.separator;
			String fileRealPath = null;
			for(String deleteIdx : deleteIdxs) {
				fileRealPath = filePath + deleteIdx;
				FileUtil.isDelete(fileRealPath);
			}
    		int result2 = LogHelper.insert(5, deleteIdxs);
		}	
		return result;
	}

}