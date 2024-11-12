package rbs.modules.code.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rbs.egovframework.LoginVO;
import rbs.modules.code.mapper.CodeOptnMapper;
import rbs.modules.code.service.CodeOptnService;

/**
 * 기관정보관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 */
@Service("codeOptnService")
public class CodeOptnServiceImpl extends EgovAbstractServiceImpl implements CodeOptnService {

	@Value("${Globals.locale.lang.use}")
	protected int useLang;						// 언어사용여부
	
	@Resource(name="codeOptnMapper")
	private CodeOptnMapper codeOptnDAO;

	/**
	 * 전체 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getOrderList(Map<String, Object> param) {
		return codeOptnDAO.getOrderList(param);
	}

    /**
     * 전체 목록 수
	 * @param mapFlag
	 * @param param
	 * @return
	 */
    @Override
	public int getTotalCount(String lang, Map<String, Object> param) {
    	return codeOptnDAO.getTotalCount(lang, param);
    }

	/**
	 * 전체 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(String lang, Map<String, Object> param) {
		return codeOptnDAO.getList(lang, param);
	}

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getModify(String lang, Map<String, Object> param) {
		DataMap viewDAO = codeOptnDAO.getModify(lang, param);
		return viewDAO;
	}
    
	/**
	 * 기본정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param Map 등록정보
	 * @return result 등록결과
	 * @throws Exception
	 */
	@Override
	public int insert(String mstCd, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		String optCd = StringUtil.getString(parameterMap.get("optCd"));
		// key 중복확인
		Map<String, Object> sparam1 = new HashMap<String, Object>();
		List<DTForm> searchList1 = new ArrayList<DTForm>();
		searchList1.add(new DTForm("A.MASTER_CODE", mstCd));
		searchList1.add(new DTForm("A.OPTION_CODE", optCd));
		sparam1.put("searchList", searchList1);
		int duplicate = codeOptnDAO.getDuplicateCount(sparam1);
		if(duplicate > 0) {
			return -2;
		}
    	
		// 항목설정으로 저장항목 setting
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(parameterMap, items, itemOrder);

		if(dataMap == null || dataMap.size() == 0) return -1;

		// 저장
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		Map<String, Object> langParam = new HashMap<String, Object>();
		List<DTForm> langDataList = new ArrayList<DTForm>();

		// 순서 setting
		int ordIdx = 0;
		int result1 = 0;
		String ordOptCd = StringUtil.getString(parameterMap.get("ordIdx"));
		Map<String, Object> sparam2 = new HashMap<String, Object>();
		List<DTForm> searchList2 = new ArrayList<DTForm>();
		searchList2.add(new DTForm("A.MASTER_CODE", mstCd));
		if(StringUtil.isEmpty(ordOptCd)) {
			// 순서 최대값 select
			sparam2.put("searchList", searchList2);
			ordIdx = codeOptnDAO.getMaxOrdIdx(sparam2) + 1;
		} else/* if(!StringUtil.isEquals(ordOptCd, optCd))*/ {
			// 선택한 순서 select
			searchList2.add(new DTForm("A.OPTION_CODE", ordOptCd));
			sparam2.put("searchList", searchList2);
			ordIdx = codeOptnDAO.getOrdIdx(sparam2);

			// 선택한 순서 이상 증가
			Map<String, Object> sparam3 = new HashMap<String, Object>();
			List<DTForm> dataList3 = new ArrayList<DTForm>();
			List<DTForm> searchList3 = new ArrayList<DTForm>();
			searchList3.add(new DTForm("MASTER_CODE", mstCd));
			searchList3.add(new DTForm("ORDER_IDX", ordIdx, ">="));
			dataList3.add(new DTForm("ORDER_IDX", "ORDER_IDX + 1", 1));

			sparam3.put("dataList", dataList3);
			sparam3.put("searchList", searchList3);
			result1 = codeOptnDAO.update(sparam3);
		}

		// insert 항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);

		dataList.add(new DTForm("MASTER_CODE", mstCd));
		
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

		dataList.add(new DTForm("REGI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("REGI_ID", loginMemberId));
    	dataList.add(new DTForm("REGI_NAME", loginMemberName));
    	dataList.add(new DTForm("REGI_IP", regiIp));
    	
		dataList.add(new DTForm("ORDER_IDX", ordIdx));
		
    	param.put("dataList", dataList);
    	
		// insert
		int result = codeOptnDAO.insert(param);
		
		// 언어 사용하는 경우 : 언어 테이블에 저장
		if(useLang == 1 && result > 0) {
			langParam.put("dataList", langDataList);
			result = codeOptnDAO.insertLang(langParam);
		}
		
		return result;
	}

	/**
	 * 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param param 수정정보
	 * @throws Exception
	 */
	@Override
	public int update(String lang, String mstCd, String optCd, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		Map<String, Object> langParam = new HashMap<String, Object>();
		List<DTForm> langDataList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("MASTER_CODE", mstCd));
		searchList.add(new DTForm("OPTION_CODE", optCd));
		
		// 항목설정으로 저장항목 setting
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

		langDataList.addAll(dataList);

		// 순서 setting
		String ordOptCd = StringUtil.getString(parameterMap.get("ordIdx"));
		if(!StringUtil.isEquals(ordOptCd, optCd)) {
			int preOrdIdx = 0;
			int ordIdx = 0;
			int result1 = 0;
			// 저장된 순서 select
			Map<String, Object> sparam2 = new HashMap<String, Object>();
			List<DTForm> searchList2 = new ArrayList<DTForm>();
			searchList2.add(new DTForm("A.MASTER_CODE", mstCd));
			searchList2.add(new DTForm("A.OPTION_CODE", optCd));
			sparam2.put("searchList", searchList2);
			preOrdIdx = codeOptnDAO.getOrdIdx(sparam2);
			
			// 선택한 순서 select
			sparam2 = new HashMap<String, Object>();
			searchList2 = new ArrayList<DTForm>();
			searchList2.add(new DTForm("A.MASTER_CODE", mstCd));
			searchList2.add(new DTForm("A.OPTION_CODE", ordOptCd));
			sparam2.put("searchList", searchList2);
			ordIdx = codeOptnDAO.getOrdIdx(sparam2);
			
			// 선택한 순서 이상 증가
			Map<String, Object> sparam3 = new HashMap<String, Object>();
			List<DTForm> dataList3 = new ArrayList<DTForm>();
			List<DTForm> searchList3 = new ArrayList<DTForm>();
			searchList3.add(new DTForm("MASTER_CODE", mstCd));

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
			result1 = codeOptnDAO.update(sparam3);

			dataList.add(new DTForm("ORDER_IDX", ordIdx));
		}
		
    	param.put("dataList", dataList);
    	param.put("searchList", searchList);
    	
		int result = codeOptnDAO.update(param);	
		// 언어 사용하는 경우 : 언어 테이블에 저장
		if(useLang == 1 && result > 0) {
	    	langParam.put("dataList", langDataList);
	    	langParam.put("searchList", searchList);
			codeOptnDAO.updateLang(lang, langParam);	
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
    	return codeOptnDAO.getDeleteCount(lang, param);
    }

	/**
	 * 삭제 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDeleteList(String lang, Map<String, Object> param) {
		return codeOptnDAO.getDeleteList(lang, param);
	}

	/**
	 * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
	 * @param param 삭제정보
	 * @throws Exception
	 */
	@Override
	public int delete(String mstCd, String[] deleteIdxs, String regiIp) throws Exception {
		if(deleteIdxs == null) return 0;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		
		// 저장조건
		
		searchList.add(new DTForm("MASTER_CODE", mstCd));
		searchList.add(new DTForm("OPTION_CODE", deleteIdxs));
		/*
		String keyColumnId = "OPTION_CODE";
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
		
		int result = codeOptnDAO.delete(param);
		return result;
	}

	/**
	 * 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
	 * @param param 복원정보
	 * @throws Exception
	 */
	@Override
	public int restore(String mstCd, String[] restoreIdxs, String regiIp) throws Exception {
		if(restoreIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		// 저장조건
		
		searchList.add(new DTForm("MASTER_CODE", mstCd));
		searchList.add(new DTForm("OPTION_CODE", restoreIdxs));
		
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
		
		int result = codeOptnDAO.restore(param);
		return result;
	}

	/**
	 * 화면에 조회된 정보를 데이터베이스에서 삭제
	 * @param param 완전삭제정보
	 * @throws Exception
	 */
	@Override
	public int cdelete(String mstCd, String[] deleteIdxs, JSONObject items, JSONArray itemOrder) throws Exception {
		if(deleteIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		// 저장조건
		
		searchList.add(new DTForm("MASTER_CODE", mstCd));
		searchList.add(new DTForm("OPTION_CODE", deleteIdxs));
		/*
		String keyColumnId = "OPTION_CODE";
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
		}
		*/
		param.put("searchList", searchList);
		
		// 2. delete
		int result = codeOptnDAO.cdelete(param);
		int result1 = 0;
		if(result > 0) {
			
			// mst_cd에 해당하는 전체 순서 update
			param = new HashMap<String, Object>();
			searchList = new ArrayList<DTForm>();
			// 저장조건
			searchList.add(new DTForm("MASTER_CODE", mstCd));
			param.put("searchList", searchList);
			
			result1 = codeOptnDAO.updateTotOrdIdx(param);
		}
		
		return result;
	}
	
	@Override
	public List<?> getMajorOptnHashMap(Map<String, Object> param) {
		return  codeOptnDAO.getMajorOptnHashMap(param);
	}

	@Override
	public String getNextSptCode(String majorCd) {
		// TODO Auto-generated method stub
		return codeOptnDAO.getNextSptCode(majorCd);
		

	}

	@Override
	public int deleteAndInsert(HttpServletRequest request, ParamForm parameterMap) {
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();		// 로그인 사용자 정보
		String loginMemberId = null;
		if(loginVO != null) {
			loginMemberId = loginVO.getMemberId();
		}
		
		// HRD담당자 정보는 수정이 될 때 기존 데이터를 update 하는 것이 아니라 기존 데이터를 delete하고 새로 insert 한다.
		
		List<DTForm> dataList;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("SUBSTRING_INDEX(OPTION_CODE, \"-\", 1)", request.getParameter("majorCd")));
		searchList.add(new DTForm("MASTER_CODE", "FIELD"));
		
		param.put("searchList", searchList);					
				
		
		int insertAbility = 0;
		
		param = new HashMap<String, Object>();					// mapper parameter 데이터		
		param.put("searchList", searchList);
		
		codeOptnDAO.deleteOptnInfo(param);
		codeOptnDAO.deleteOptnInfoLang(param);
		
		dataList = new ArrayList<DTForm>();							// 데이터 항목		
		
		int sptPsnRowLength = StringUtil.getInt((String) parameterMap.get("sptPsnRowLength"));

		for(int i = 0; i < sptPsnRowLength; i++) {
			
			if(parameterMap.get("ord" + i) != null) {
				String optionCode = (String) parameterMap.get("optionCode" + i);
				String optionName = (String) parameterMap.get("optionName" + i);				
				int orderIdx =  StringUtil.getInt(parameterMap.get("ordIdx" + i));			
				String regiId = (String) parameterMap.get("regiId" + i);				
				String regiIp = (String) parameterMap.get("regiIp" + i);				
				
				// REGI, LAST_MODI 항목 setting
				if(regiId == null || regiIp == null) {
					dataList.add(new DTForm("REGI_ID", loginMemberId));
					dataList.add(new DTForm("REGI_IP", request.getRemoteAddr()));
				}else {
					dataList.add(new DTForm("REGI_ID", regiId));
					dataList.add(new DTForm("REGI_IP", regiIp));
				}
				
				dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
				dataList.add(new DTForm("LAST_MODI_IP", request.getRemoteAddr()));
								
				dataList.add(new DTForm("MASTER_CODE", "FIELD"));
				dataList.add(new DTForm("OPTION_CODE", optionCode));
				dataList.add(new DTForm("OPTION_NAME", optionName));
				dataList.add(new DTForm("ORDER_IDX", orderIdx));
				dataList.add(new DTForm("ISDELETE", 0));
				
				param.put("dataList", dataList);
				insertAbility += codeOptnDAO.insertOptnInfo(param);
				dataList.clear();				
				
				dataList.add(new DTForm("LOCALE_LANG", "ko"));	
				dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
				dataList.add(new DTForm("LAST_MODI_IP", request.getRemoteAddr()));
								
				dataList.add(new DTForm("MASTER_CODE", "FIELD"));
				dataList.add(new DTForm("OPTION_CODE", optionCode));
				dataList.add(new DTForm("OPTION_NAME", optionName));
				
				param.put("dataList", dataList);
				insertAbility += codeOptnDAO.insertOptnInfoLang(param);
				
				dataList.clear();
			}
			
		}
		
		return insertAbility;
	}

}