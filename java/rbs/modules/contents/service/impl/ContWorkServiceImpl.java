package rbs.modules.contents.service.impl;

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
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import rbs.egovframework.LoginVO;
import rbs.egovframework.util.LogHelper;
import rbs.modules.contents.mapper.ContVersionMapper;
import rbs.modules.contents.mapper.ContWorkMapper;
import rbs.modules.contents.mapper.ContentsMapper;
import rbs.modules.contents.service.ContWorkService;

/**
 * 기관정보관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 */
@Service("contWorkService")
public class ContWorkServiceImpl extends EgovAbstractServiceImpl implements ContWorkService {

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

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getModify(String contentsType, String lang, Map<String, Object> param) {
		DataMap viewDAO = contWorkDAO.getModify(contentsType, lang, param);
		return viewDAO;
	}
	@Override
	public DataMap getApplyView(String contentsType, String lang, Map<String, Object> param) {
		DataMap viewDAO = contWorkDAO.getApplyView(contentsType, lang, param);
		return viewDAO;
	}

	/**
	 * 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param param 수정정보
	 * @throws Exception
	 */
	@Override
	public int update(String lang, String contCd, int branchIdx, int verIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject moduleItem) throws Exception {
		if(JSONObjectUtil.isEmpty(moduleItem)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		Map<String, Object> workParam = new HashMap<String, Object>();
		List<DTForm> workDataList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A.CONTENTS_CODE", contCd));
		searchList.add(new DTForm("A.BRANCH_IDX", branchIdx));
		searchList.add(new DTForm("A.VER_IDX", verIdx));
		
		param.put("searchList", searchList);
    	
		// 버전정보 select
		DataMap verDt = contVersionDAO.getModify(lang, param);

		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
		
		int workType = StringUtil.getInt(verDt.get("WORK_TYPE"));
		if(workType == 0) {
			// 작업전 : 작업 등록 정보

	    	dataList.add(new DTForm("WORK_IDX", loginMemberIdx));
	    	dataList.add(new DTForm("WORK_ID", loginMemberId));
	    	dataList.add(new DTForm("WORK_NAME", loginMemberName));
	    	dataList.add(new DTForm("WORK_IP", regiIp));
	    	dataList.add(new DTForm("WORK_TYPE", "01"));						// 작업전 -> 작업중
		}
		
		// 저장 항목
		/*String inputWorkType = null;
		if(workType == 0) inputWorkType = "1";				// 작업전 -> 작업중
		else if(workType == 11) inputWorkType = "1";		// 적용완료
		dataList.add(new DTForm("WORK_TYPE", inputWorkType));*/

    	dataList.add(new DTForm("LAST_WORK_IDX", loginMemberIdx));
    	dataList.add(new DTForm("LAST_WORK_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_WORK_NAME", loginMemberName));
    	dataList.add(new DTForm("LAST_WORK_IP", regiIp));
		
    	searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("CONTENTS_CODE", contCd));
		searchList.add(new DTForm("BRANCH_IDX", branchIdx));
		searchList.add(new DTForm("VER_IDX", verIdx));
		
    	param.put("dataList", dataList);
    	param.put("searchList", searchList);
    	
		int result = -1;
		if(workType == 0) result = contVersionDAO.workInsertLang(lang, param);
		else result = contVersionDAO.workUpdateLang(lang, param);
		if(result > 0) {
			
			// 작업 테이블 저장
			String contType = StringUtil.getString(verDt.get("CONTENTS_TYPE"));

			// 항목설정으로 저장항목 setting
			String submitType = "modify";
			JSONObject itemInfo = JSONObjectUtil.getJSONObject(moduleItem, "work_" + contType + "_item_info");
			JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
			JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
			HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(parameterMap, settingInfo, items, itemOrder);
			
			if(dataMap == null || dataMap.size() == 0) return -1;
			
			List itemDataList = StringUtil.getList(dataMap.get("dataList"));
			if(itemDataList != null) workDataList.addAll(itemDataList);
			
	    	workParam.put("dataList", workDataList);
	    	workParam.put("searchList", searchList);
			contWorkDAO.update(contType, lang, workParam);	
    		int result2 = LogHelper.insert("버전내용", contCd + "," + branchIdx, 2, verIdx);
		}
		
		return result;
	}

	/**
	 * 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param param 수정정보
	 * @throws Exception
	 */
	@Override
	public int apply(String lang, String contCd, int branchIdx, int verIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject moduleItem) throws Exception {
		if(JSONObjectUtil.isEmpty(moduleItem)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		Map<String, Object> workParam = new HashMap<String, Object>();
		List<DTForm> workDataList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A.CONTENTS_CODE", contCd));
		searchList.add(new DTForm("A.BRANCH_IDX", branchIdx));
		searchList.add(new DTForm("A.VER_IDX", verIdx));
		
		param.put("searchList", searchList);
    	
		// 1. 버전정보 select
		DataMap verDt = contVersionDAO.getModify(lang, param);
		
		// 2. 작업 상태 update
		int workType = StringUtil.getInt(parameterMap.get("workType"));
		if(workType == 10) {
	    	
	    	// 2.1 '적용'상태의 버전 '적용완료'상태로 수정
			Map<String, Object> preAWorkParam = new HashMap<String, Object>();
			List<DTForm> preAWorkSearchList = new ArrayList<DTForm>();
			List<DTForm> preAWorkDataList = new ArrayList<DTForm>();
			
			preAWorkSearchList.add(new DTForm("CONTENTS_CODE", contCd));
			preAWorkSearchList.add(new DTForm("BRANCH_IDX", branchIdx));
			preAWorkSearchList.add(new DTForm("WORK_TYPE", workType + ""));
			
			preAWorkDataList.add(new DTForm("WORK_TYPE", "11"));

			preAWorkParam.put("dataList", preAWorkDataList);
			preAWorkParam.put("searchList", preAWorkSearchList);
			contVersionDAO.updateLang(lang, preAWorkParam);
			
	    	dataList.add(new DTForm("APPLY_IDX", loginMemberIdx));
	    	dataList.add(new DTForm("APPLY_ID", loginMemberId));
	    	dataList.add(new DTForm("APPLY_NAME", loginMemberName));
	    	dataList.add(new DTForm("APPLY_IP", regiIp));
		}
    	dataList.add(new DTForm("WORK_TYPE", parameterMap.get("workType")));				// 작업상태
		
    	searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("CONTENTS_CODE", contCd));
		searchList.add(new DTForm("BRANCH_IDX", branchIdx));
		searchList.add(new DTForm("VER_IDX", verIdx));
		
    	param.put("dataList", dataList);
    	param.put("searchList", searchList);
    	
    	// 2.2 작업 상태 update
		int result = contVersionDAO.updateLang(lang, param);
		if(result > 0) {
			
			// 3. 작업 적용 테이블 저장
			String contType = StringUtil.getString(verDt.get("CONTENTS_TYPE"));

			workDataList.add(new DTForm("REGI_IDX", loginMemberIdx));
			workDataList.add(new DTForm("REGI_ID", loginMemberId));
			workDataList.add(new DTForm("REGI_NAME", loginMemberName));
			workDataList.add(new DTForm("REGI_IP", regiIp));

			// 항목설정으로 저장항목 setting
			String submitType = "modify";
			JSONObject itemInfo = JSONObjectUtil.getJSONObject(moduleItem, "work_" + contType + "_item_info");
			JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
			JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
			List<DTForm> itemWorkDataList = ModuleUtil.getItemInfoColumnList(items, itemOrder);
			
			if(itemWorkDataList == null || itemWorkDataList.size() == 0) return -1;
			
			if(itemWorkDataList != null) workDataList.addAll(itemWorkDataList);

			// 3.1 적용 삭제
	    	Map<String, Object> deleteApplyParam = new HashMap<String, Object>();
	    	List<DTForm> deleteSearchList = new ArrayList<DTForm>();
	    	deleteSearchList.add(new DTForm("CONTENTS_CODE", contCd));
	    	deleteSearchList.add(new DTForm("BRANCH_IDX", branchIdx));
	    	deleteApplyParam.put("searchList", deleteSearchList);
	    	result = contWorkDAO.applyCdelete(contType, lang, deleteApplyParam);
			
	    	// 3.2 적용 등록
	    	workParam.put("dataList", workDataList);
	    	workParam.put("searchList", searchList);
			result = contWorkDAO.apply(contType, lang, workParam);	
			if(result > 0) {
				int result2 = LogHelper.insert("버전", contCd + "," + branchIdx, 9, verIdx);
			}
		}
		
		return result;
	}
}