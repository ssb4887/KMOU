package rbs.modules.poll.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.AuthHelper;
import com.woowonsoft.egovframework.util.FileUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.egovframework.LoginVO;
import rbs.modules.poll.mapper.RespMapper;
import rbs.modules.poll.mapper.ResultMapper;
import rbs.modules.poll.service.RespService;

/**
 * 설문관리에 관한 구현클래스를 정의한다.
 * @author user
 *
 */
@Service("pollRespService")
public class RespServiceImpl extends EgovAbstractServiceImpl implements RespService {

	@Resource(name="pollRespMapper")
	private RespMapper respDAO;
	@Resource(name="pollResultMapper")
	private ResultMapper resultDAO;
	
	@Override
	public DataMap getPollView(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return respDAO.getPollView(param);
	}

    @Override
	public int getRespIdx(int fnIdx, Map<String, Object> param){
    	param.put("fnIdx", fnIdx);
		return respDAO.getId(null, param);
	}
	
    @Override
	public int getTmpRespIdx(int fnIdx, Map<String, Object> param){
    	param.put("fnIdx", fnIdx);
		return respDAO.getId("t", param);
	}

    @Override
	public int getRespCnt(int fnIdx, Map<String, Object> param){
    	param.put("fnIdx", fnIdx);
		return respDAO.getTotalCount(null, param);
	}
	
	/**
	 * 전체 목록 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
    @Override
	public int getTotalCount(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	return respDAO.getTotalCount(null, param);
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
		return respDAO.getList(param);
	}

	public List<DTForm> getRespDataList(){
    	LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = request.getSession(true);
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
		String sDupInfo = null;//StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));
		String sAuthType = null;
		boolean isPageNMMember = AuthHelper.isAuthenticated("NMMEMBER_RWT");

		List<DTForm> dataList = new ArrayList<DTForm>();
		if(isPageNMMember) {
			// 본인인증회원 참여 권한
			sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));
			sAuthType = StringUtil.getString(session.getAttribute("sAuthType"), StringUtil.getString(session.getAttribute("iSAuthType")));;
			
			/*if(!StringUtil.isEmpty(sDupInfo)) {
				dataList.add(new DTForm("CHECK_TYPE", sAuthType));
				dataList.add(new DTForm("MEMBER_DUP", sDupInfo));
			}*/
		} else {
			// 비회원 참여권한
			boolean isPageNOMember = AuthHelper.isAuthenticated("NOMEMBER_RWT");
			if(isPageNOMember && StringUtil.isEmpty(loginMemberIdx)) {
				loginMemberIdx = session.getId();
				//dataList.add(new DTForm("REGI_IP", request.getRemoteAddr()));
			}
		}
		dataList.add(new DTForm("CHECK_TYPE", sAuthType));
		dataList.add(new DTForm("MEMBER_DUP", sDupInfo));
		dataList.add(new DTForm("REGI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("REGI_ID", loginMemberId));
    	dataList.add(new DTForm("REGI_NAME", loginMemberName));
		dataList.add(new DTForm("REGI_IP", request.getRemoteAddr()));
		
		return dataList;
	}

	@Override
	public List<DTForm> getRespSearchList(int pollIdx){
    	return getRespSearchList(pollIdx, null);
	}

	@Override
	public List<DTForm> getRespSearchList(int pollIdx, String alias){
    	LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = request.getSession(true);
		String regiIdx = (loginVO != null)? loginVO.getMemberIdx():null;
		boolean isPageNMMember = AuthHelper.isAuthenticated("NMMEMBER_RWT");
		if(alias == null) alias = "";
		else alias += ".";

		List<DTForm> searchList = new ArrayList<DTForm>();
		if(isPageNMMember) {
			// 본인인증회원 참여 권한
			String sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));
			String sAuthType = StringUtil.getString(session.getAttribute("sAuthType"), StringUtil.getString(session.getAttribute("iSAuthType")));;
			
			if(!StringUtil.isEmpty(sDupInfo)) {
				//regiIdx = sDupInfo;
				searchList.add(new DTForm(alias + "CHECK_TYPE", sAuthType));
				searchList.add(new DTForm(alias + "MEMBER_DUP", sDupInfo));
			}
		} else {
			// 비회원 참여권한
			boolean isPageNOMember = AuthHelper.isAuthenticated("NOMEMBER_RWT");
			if(isPageNOMember && StringUtil.isEmpty(regiIdx)) {
				regiIdx = session.getId();
				searchList.add(new DTForm(alias + "REGI_IP", request.getRemoteAddr()));
			}
		}
		if(!StringUtil.isEmpty(regiIdx)) searchList.add(new DTForm(alias + "REGI_IDX", regiIdx));
		
		if(searchList.size() == 0) return null;
		
		if(pollIdx != -1) searchList.add(new DTForm(alias + "POLL_IDX", pollIdx));
		return searchList;
	}

	@Override
	public Map<Object, Object> getResultMap(int fnIdx, Map<String, Object> param){
    	param.put("fnIdx", fnIdx);
		return resultDAO.getResultMap(null, param);
	}

	@Override
	public Map<Object, Object> getTmpResultMap(int fnIdx, Map<String, Object> param){
    	param.put("fnIdx", fnIdx);
		return resultDAO.getResultMap("t", param);
	}

	@Override
	public Map<String, List<Object>> getResultListMap(int fnIdx, Map<String, Object> param){
    	param.put("fnIdx", fnIdx);
		return resultDAO.getResultMap(param);
	}
	
	/**
	 * 전체 목록 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
    @Override
	public int getResultTotalCount(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	return resultDAO.getTotalCount(null, param);
    }
	
	@Override
	public List<Object> getResultList(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return resultDAO.getList(param);
	}

	@Override
	public List<Object> getQuestypeResultList(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return resultDAO.getQuestypeResultList(false, param);
	}

	@Override
	public List<Object> getQuestypePResultList(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return resultDAO.getQuestypeResultList(true, param);
	}
    
	/**
	 * 등록처리 : 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@Override
	public int insert(int fnIdx, int pollIdx, String regiIp, ParamForm parameterMap) throws Exception  {
		String[] quesIdxs = StringUtil.getStringArray(parameterMap.get("questionIdx")); 
		if(pollIdx <= 0 || quesIdxs == null) return -1;

		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
		
		int isComplete = StringUtil.getInt(parameterMap.get("iscomp"));
		
		int tmpModifyRespIdx = 0;
		int result = 0;
		String tmpFlag = (isComplete != 1)?"t":null;

		int result1 = 0;
		// 1. key 얻기
    	List<DTForm> searchList = new ArrayList<DTForm>();
    	searchList.add(new DTForm("A.POLL_IDX", pollIdx));
    	param.put("fnIdx", fnIdx);
    	param.put("searchList", searchList);
		int respIdx = respDAO.getNextId(tmpFlag, param);

		dataList.addAll(getRespDataList());
		//if(isComplete != 1) {
			// 임시저장 key
	    	searchList = getRespSearchList(pollIdx);
	    	if(searchList == null) return -1;
			
	    	param.put("fnIdx", fnIdx);
	    	param.put("searchList", searchList);
	    	tmpModifyRespIdx = respDAO.getId("t", param);
		//}
		
		if(isComplete != 1 && tmpModifyRespIdx > 0) {
			// 임시저장 수정
			respIdx = tmpModifyRespIdx;
			searchList = new ArrayList<DTForm>();
	    	searchList.add(new DTForm("POLL_IDX", pollIdx));
	    	searchList.add(new DTForm("RESP_IDX", respIdx));
	    	param.put("searchList", searchList);
	    	param.put("dataList", dataList);
			result = respDAO.update("t", param);
			
			if(result > 0) {
				// 참여결과 삭제
				result1 = resultDAO.cdelete("t", param);
			}
		} else {
			// 등록
			if(tmpModifyRespIdx > 0) {
				// 임시저장 삭제
				searchList = new ArrayList<DTForm>();
		    	searchList.add(new DTForm("POLL_IDX", pollIdx));
		    	searchList.add(new DTForm("RESP_IDX", tmpModifyRespIdx));
		    	param.put("searchList", searchList);
				result = respDAO.cdelete("t", param);
			}
			dataList.add(new DTForm("POLL_IDX", pollIdx));
			dataList.add(new DTForm("RESP_IDX", respIdx));
	    	param.put("dataList", dataList);
	    	
			result = respDAO.insert(tmpFlag, param);
		}
		
		if(result > 0) {
			// 참여결과 등록
			String[] itemIdxs = null;				// 답안 선택 항목 (다중)
			String resultContents = null;			// 기타 입력내용
			List<DTForm> resultDataList = null;
			for(String quesIdx:quesIdxs) {
				itemIdxs = StringUtil.getStringArray(parameterMap.get("itemIdx_" + quesIdx));
	            if(itemIdxs == null) continue;
	            
	            int itemI = 0;
	            for(String itemIdx:itemIdxs) {
	            	resultContents = StringUtil.getString(parameterMap.get("istext_contents_" + quesIdx + "_" + itemIdx));
	    			resultDataList = new ArrayList<DTForm>();
	    			resultDataList.add(new DTForm("POLL_IDX", pollIdx));
	    			resultDataList.add(new DTForm("QUESTION_IDX", quesIdx));
	    			resultDataList.add(new DTForm("RESP_IDX", respIdx));
	    			resultDataList.add(new DTForm("RESULT_IDX", ++ itemI));
	    			resultDataList.add(new DTForm("ITEM_IDX", itemIdx));
	    			resultDataList.add(new DTForm("CONTENTS", resultContents));
	    			
	    			param.put("dataList", resultDataList);
	    			result1 = resultDAO.insert(tmpFlag, param);
	            }
			}
		}
		
		return result;
	}
	
	public int resultUpdate(int fnIdx, int pollIdx, int respIdx, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception{
		if(pollIdx <= 0 || respIdx <= 0) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	param.put("fnIdx", fnIdx);												// table flag
		
		// 2. 항목설정으로 저장항목 setting
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(null, parameterMap, null, items, itemOrder);
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
    	
    	dataList.add(new DTForm("MNGC_MODI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("MNGC_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("MNGC_MODI_NAME", loginMemberName));
    	dataList.add(new DTForm("MNGC_MODI_IP", regiIp));

    	param.put("dataList", dataList);
    	param.put("searchList", searchList);
    	
		// 3. DB 저장
		int result = respDAO.resultUpdate(param);
				
		return result;
	}
}
