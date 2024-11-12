package rbs.modules.itemClass.service.impl;

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
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import rbs.egovframework.LoginVO;
import rbs.modules.itemClass.mapper.ClassMstrMapper;
import rbs.modules.itemClass.service.ClassMstrService;

/**
 * 기관정보관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 */
@Service("classMstrService")
public class ClassMstrServiceImpl extends EgovAbstractServiceImpl implements ClassMstrService {

	@Value("${Globals.locale.lang.use}")
	protected int useLang;						// 언어사용여부
	
	@Resource(name="classMstrMapper")
	private ClassMstrMapper classMstrDAO;

    /**
     * 전체 목록 수
	 * @param mapFlag
	 * @param param
	 * @return
	 */
    @Override
	public int getTotalCount(Map<String, Object> param) {
    	return classMstrDAO.getTotalCount(param);
    }

	/**
	 * 전체 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(Map<String, Object> param) {
		return classMstrDAO.getList(param);
	}

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getView(Map<String, Object> param) {
		DataMap viewDAO = classMstrDAO.getView(param);
		return viewDAO;
	}

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getModify(Map<String, Object> param) {
		DataMap viewDAO = classMstrDAO.getModify(param);
		return viewDAO;
	}
    
	/**
	 * 기본정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param Map 등록정보
	 * @return result 등록결과
	 * @throws Exception
	 */
	@Override
	public int insert(String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		String mstCd = StringUtil.getString(parameterMap.get("mstCd"));
		// key 중복확인
		Map<String, Object> sparam1 = new HashMap<String, Object>();
		List<DTForm> searchList1 = new ArrayList<DTForm>();
		searchList1.add(new DTForm("A.MASTER_CODE", mstCd));
		sparam1.put("searchList", searchList1);
		int duplicate = classMstrDAO.getDuplicateCount(sparam1);
		if(duplicate > 0) {
			return -2;
		}
    	
		// 항목설정으로 저장항목 setting
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(parameterMap, items, itemOrder);

		if(dataMap == null || dataMap.size() == 0) return -1;


		int ordIdx = classMstrDAO.getMaxOrdIdx(null) + 1;
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// 저장 항목
    	dataList.add(new DTForm("ORDER_IDX", ordIdx));

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
    	
		int result = classMstrDAO.insert(param);
		
		return result;
	}

	/**
	 * 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param param 수정정보
	 * @throws Exception
	 */
	@Override
	public int update(String mstCd, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		searchList.add(new DTForm("MASTER_CODE", mstCd));
		
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
    	
    	param.put("dataList", dataList);
    	param.put("searchList", searchList);
    	
    	// 저장 : 언어테이블도 저장
		int result = classMstrDAO.update(param);
		// 언어 사용하는 경우 : 언어 테이블에 저장
		if(useLang == 1 && result > 0) result = classMstrDAO.updateLang(param);
		
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
    	return classMstrDAO.getDeleteCount(param);
    }

	/**
	 * 삭제 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDeleteList(Map<String, Object> param) {
		return classMstrDAO.getDeleteList(param);
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
		searchList.add(new DTForm("MASTER_CODE", deleteIdxs));
		/*
		String keyColumnId = "MASTER_CODE";
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
		
		int result = classMstrDAO.delete(param);
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
		searchList.add(new DTForm("MASTER_CODE", restoreIdxs));
		/*
		String keyColumnId = "MASTER_CODE";
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
		
		int result = classMstrDAO.restore(param);
		return result;
	}

	/**
	 * 화면에 조회된 정보를 데이터베이스에서 삭제
	 * @param param 완전삭제정보
	 * @throws Exception
	 */
	@Override
	public int cdelete(String[] deleteIdxs, JSONObject items, JSONArray itemOrder) throws Exception {
		if(deleteIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		// 저장조건
		searchList.add(new DTForm("MASTER_CODE", deleteIdxs));
		/*
		String keyColumnId = "MASTER_CODE";
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
		
		param.put("searchList", searchList);
		
		// 2. delete
		int result = classMstrDAO.cdelete(param);		
		return result;
	}

}