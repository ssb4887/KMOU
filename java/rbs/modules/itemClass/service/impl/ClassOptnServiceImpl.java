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
import rbs.modules.itemClass.mapper.ClassOptnMapper;
import rbs.modules.itemClass.service.ClassOptnService;

/**
 * 기관정보관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 */
@Service("classOptnService")
public class ClassOptnServiceImpl extends EgovAbstractServiceImpl implements ClassOptnService {

	@Value("${Globals.locale.lang.use}")
	protected int useLang;						// 언어사용여부
	
	@Resource(name="classOptnMapper")
	private ClassOptnMapper classOptnDAO;

	/**
	 * 전체 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getOptnList(Map<String, Object> param) {
		return classOptnDAO.getOptnList(param);
	}

    /**
     * 전체 목록 수
	 * @param mapFlag
	 * @param param
	 * @return
	 */
    @Override
	public int getTotalCount(String lang, Map<String, Object> param) {
    	return classOptnDAO.getTotalCount(lang, param);
    }

	/**
	 * 전체 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(String lang, Map<String, Object> param) {
		return classOptnDAO.getList(lang, param);
	}

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getModify(String lang, Map<String, Object> param) {
		DataMap viewDAO = classOptnDAO.getModify(lang, param);
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

		// 항목설정으로 저장항목 setting
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(parameterMap, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;

		// 저장
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		
		DTForm masterSearchForm = new DTForm("MASTER_CODE", mstCd);

		//고유아이디 셋팅
		/*Map<String, Object> nextIdParam = new HashMap<String, Object>();
		List<DTForm> nextIdSearchList = new ArrayList<DTForm>();
		nextIdSearchList.add(masterSearchForm);
		nextIdParam.put("searchList", nextIdSearchList);
		int classIdx = classOptnDAO.getNextId(nextIdParam);*/

		String classIdx = StringUtil.getString(parameterMap.get("optCd"));
		
		// key 중복확인
		Map<String, Object> sparam1 = new HashMap<String, Object>();
		List<DTForm> searchList1 = new ArrayList<DTForm>();
		searchList1.add(masterSearchForm);
		searchList1.add(new DTForm("A.CLASS_IDX", classIdx));
		sparam1.put("searchList", searchList1);
		int duplicate = classOptnDAO.getDuplicateCount(sparam1);
		if(duplicate > 0) {
			return -2;
		}

		// 순서 setting
		int ordType = StringUtil.getInt(parameterMap.get("ordType"));
		String targetClassIdx = StringUtil.getString(parameterMap.get("ordIdx"));

		if(StringUtil.isEmpty(targetClassIdx)) {
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
			targetSearchList.add(masterSearchForm);

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
				//List<DTForm> targetOrdSearchList2 = new ArrayList<DTForm>();
				//targetOrdSearchList2.add(new DTForm("CLASS_IDX", targetClassIdx));
				targetOrdParam.put("searchList", targetOrdSearchList);
				//targetOrdParam.put("searchList2", targetOrdSearchList2);
				targetOrdParam.put("classIdx", targetClassIdx);
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
			String targetGroupClassIdx = StringUtil.getString(targetClassDt.get("GROUP_CLASS_IDX"));
			int targetClassLevel = StringUtil.getInt(targetClassDt.get("CLASS_LEVEL"));
			int classLevel = targetClassLevel;
			String groupClassIdx = classIdx;
			String parentClassIdx = null;
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
				parentClassIdx = StringUtil.getString(targetClassDt.get("PARENT_CLASS_IDX"));
				if(targetClassLevel > 1) {
					groupClassIdx = targetGroupClassIdx;
					//classLevel = targetClassLevel;
				} else classLevel = 1;
			}
			dataList.add(new DTForm("PARENT_CLASS_IDX", parentClassIdx));
			dataList.add(new DTForm("GROUP_CLASS_IDX", groupClassIdx));
			dataList.add(new DTForm("CLASS_LEVEL", classLevel));
		}

		// insert 항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);

		//dataList.add(new DTForm("CLASS_IDX", classIdx));
		dataList.add(new DTForm("MASTER_CODE", mstCd));

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
    	
		// insert
		int result = classOptnDAO.insert(param);
				
		return result;
	}

	/**
	 * 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param param 수정정보
	 * @throws Exception
	 */
	@Override
	public int update(String lang, String mstCd, String sourceClassIdx, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		Map<String, Object> langParam = new HashMap<String, Object>();
		List<DTForm> langDataList = new ArrayList<DTForm>();

		DTForm masterSearchForm = new DTForm("MASTER_CODE", mstCd);
		
		searchList.add(masterSearchForm);
		searchList.add(new DTForm("CLASS_IDX", sourceClassIdx));
		
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
		// sourceUntCd != targetUntCd
		// 하위단원으로 이동하지 않는 경우
		// 내부이동인 경우 prt_unt_cd가 같지 않은 경우
		int ordType = StringUtil.getInt(parameterMap.get("ordType"));
		String targetClassIdx = StringUtil.getString(parameterMap.get("ordIdx"));
		if(!StringUtil.isEquals(targetClassIdx, sourceClassIdx)) {
			String sourcePrtClassIdx = "-1";		// 이동할 prt_unt_cd
			String targetPrtClassIdx = "-1";		// 이동될 prt_unt_cd 
			int sourceClassLevel = 0;		// 이동할 class_level
			int sourceOrdIdx = 0;			// 이동할 ord_idx (현재)
			int targetOrdIdx = 0;			// 이동될 ord_idx
			int gOrdCnt = 0;				// sourceOrdIdx - targetOrdIdx
			List<Object> sourceClassList = null;	// 이동할 classIdx 목록
			int sourceOrdCnt = 0;
			
			// 하위단원으로 이동여부
			Map<String, Object> inChildParam = new HashMap<String, Object>();
			List<DTForm> inChildSearchList = new ArrayList<DTForm>();
			inChildSearchList.add(masterSearchForm);
			inChildParam.put("searchList", inChildSearchList);
			inChildParam.put("searchClassIdx", targetClassIdx);
			inChildParam.put("classIdx", sourceClassIdx);
			int inChild = classOptnDAO.getInChildCount(inChildParam);
			
			if(inChild > 0) {
				// 하위단원으로 이동하는 경우
				return -11;
			}

			
			// 선택한 위치의 정보 얻기
			Map<String, Object> targetPrtParam = new HashMap<String, Object>();
			List<DTForm> targetPrtSearchList = new ArrayList<DTForm>();
			targetPrtSearchList.add(masterSearchForm);
			targetPrtSearchList.add(new DTForm("CLASS_IDX", targetClassIdx));
			targetPrtParam.put("searchList", targetPrtSearchList);
			DataMap targetClassDt = classOptnDAO.getClassKeyView(targetPrtParam);
			String targetGroupClassIdx = StringUtil.getString(targetClassDt.get("GROUP_CLASS_IDX"));
			int targetClassLevel = StringUtil.getInt(targetClassDt.get("CLASS_LEVEL"));
			targetOrdIdx = StringUtil.getInt(targetClassDt.get("ORDER_IDX"));
			
			// 이동할 정보 얻기
			Map<String, Object> sourceOrdParam = new HashMap<String, Object>();
			List<DTForm> sourceOrdSearchList = new ArrayList<DTForm>();
			sourceOrdSearchList.add(masterSearchForm);
			sourceOrdSearchList.add(new DTForm("CLASS_IDX", sourceClassIdx));
			sourceOrdParam.put("searchList", sourceOrdSearchList);
			DataMap sourceClassDt = classOptnDAO.getClassKeyView(sourceOrdParam);
			sourceClassLevel = StringUtil.getInt(sourceClassDt.get("CLASS_LEVEL"));
			sourceOrdIdx = StringUtil.getInt(sourceClassDt.get("ORDER_IDX"));
			
			String groupClassIdx = StringUtil.getString(sourceClassDt.get("GROUP_CLASS_IDX"));
			String parentClassIdx = null;
			int addClassLevel = targetClassLevel - sourceClassLevel;
			
			// 이동될 PARENT_CLASS_IDX select 
			if(ordType == 3) {
				// 내부
				targetPrtClassIdx = targetClassIdx;
				
				Map<String, Object> sourcePrtParam = new HashMap<String, Object>();
				List<DTForm> sourcePrtSearchList = new ArrayList<DTForm>();
				sourcePrtSearchList.add(masterSearchForm);
				sourcePrtSearchList.add(new DTForm("CLASS_IDX", sourceClassIdx));
				sourcePrtParam.put("searchList", sourcePrtSearchList);
				sourcePrtClassIdx = classOptnDAO.getPrtClassIdx(sourcePrtParam);
				
				// 같은 상위단원의 내부로 선택된 경우
				if(StringUtil.isEquals(sourcePrtClassIdx, targetPrtClassIdx)) return -12;

				parentClassIdx = targetClassIdx;
				addClassLevel ++;
				groupClassIdx = targetGroupClassIdx;
			} else {
				// 위/아래
				parentClassIdx = StringUtil.getString(targetClassDt.get("PARENT_CLASS_IDX"));
				if(targetClassLevel > 1) groupClassIdx = targetGroupClassIdx;
				else groupClassIdx = sourceClassIdx;
			}
			
			// 이동될 ord_idx select
			Map<String, Object> targetOrdParam = new HashMap<String, Object>();
			List<DTForm> targetOrdSearchList = new ArrayList<DTForm>();
			targetOrdSearchList.add(masterSearchForm);
			
			if(ordType == 1) {
				// 위 : 선택한 단원의 첫번째 순서
				/*targetOrdSearchList.add(new DTForm("CLASS_IDX", targetClassIdx));
				targetOrdParam.put("searchList", targetOrdSearchList);
				targetOrdIdx = classOptnDAO.getOrdIdx(targetOrdParam);*/
			} else {
				// 아래/내부 : 선택한 단원의 마지막 순서
				//List<DTForm> targetOrdSearchList2 = new ArrayList<DTForm>();
				//targetOrdSearchList2.add(new DTForm("CLASS_IDX", targetClassIdx));
				targetOrdParam.put("searchList", targetOrdSearchList);
				//targetOrdParam.put("searchList2", targetOrdSearchList2);

				targetOrdParam.put("classIdx", targetClassIdx);
				targetOrdIdx = classOptnDAO.getNextOrdIdx(targetOrdParam);
			}
			
			// 이동할 순서 - 이동될 순서
			gOrdCnt = sourceOrdIdx - targetOrdIdx;

			// 이동할 classIdx 목록
			Map<String, Object> sourceClassParam = new HashMap<String, Object>();
			List<DTForm> sourceClassSearchList = new ArrayList<DTForm>();
			sourceClassSearchList.add(masterSearchForm);
			//List<DTForm> sourceClassSearchList2 = new ArrayList<DTForm>();
			//sourceClassSearchList2.add(new DTForm("CLASS_IDX", sourceClassIdx));
			sourceClassParam.put("searchList", sourceClassSearchList);
			//sourceClassParam.put("searchList2", sourceClassSearchList2);
			sourceClassParam.put("classIdx", sourceClassIdx);
			sourceClassList = classOptnDAO.getSourceList(sourceClassParam);
			sourceOrdCnt = (sourceClassList != null)? sourceClassList.size():0;//classOptnDAO.getOrdCount(sourceCntParam);
			
			List<DTForm> sourceDataList = null;
			List<DTForm> targetDataList = null;
			List<DTForm> targetSearchList = null;
			if(gOrdCnt != 0) {
				int gOrbCntAbs = Math.abs(gOrdCnt);
				
				if(gOrdCnt > 0) {
					// 이동할 ord_idx가 아래에 있는 경우 (sourceOrdIdx > targetOrdIdx)
					int addCnt = 0;
					if(ordType != 1) addCnt = 1;				// 아래/내부
					sourceDataList = new ArrayList<DTForm>();
					sourceDataList.add(new DTForm("ORDER_IDX", "ORDER_IDX - " + gOrbCntAbs + " + " + addCnt, 1));
					
					targetDataList = new ArrayList<DTForm>();
					targetDataList.add(new DTForm("ORDER_IDX", "ORDER_IDX + " + sourceOrdCnt, 1));
					
					targetSearchList = new ArrayList<DTForm>();
					targetSearchList.add(masterSearchForm);
					if(ordType == 1) {
						// 위
						targetSearchList.add(new DTForm("ORDER_IDX", targetOrdIdx, ">="));
						targetSearchList.add(new DTForm("ORDER_IDX", sourceOrdIdx, "<"));
					} else {
						// 아래/내부
						targetSearchList.add(new DTForm("ORDER_IDX", targetOrdIdx, ">"));
						targetSearchList.add(new DTForm("ORDER_IDX", sourceOrdIdx, "<"));
					}
				} else if(gOrdCnt < 0) {
					// 이동할 ord_idx가 위에 있는 경우 (sourceOrdIdx > targetOrdIdx)
					int addCnt = 0;
					if(ordType != 1) addCnt = 1;				// 아래/내부
					sourceDataList = new ArrayList<DTForm>();
					sourceDataList.add(new DTForm("ORDER_IDX", "ORDER_IDX  + (" + (gOrbCntAbs - sourceOrdCnt + addCnt) + ")", 1));
					
					targetDataList = new ArrayList<DTForm>();
					targetDataList.add(new DTForm("ORDER_IDX", "ORDER_IDX - " + sourceOrdCnt, 1));
					
					targetSearchList = new ArrayList<DTForm>();
					targetSearchList.add(masterSearchForm);
					if(ordType == 1) {
						// 위
						targetSearchList.add(new DTForm("ORDER_IDX", targetOrdIdx, "<"));
						targetSearchList.add(new DTForm("ORDER_IDX", (sourceOrdIdx + sourceOrdCnt), ">="));
					} else {
						// 아래/내부
						targetSearchList.add(new DTForm("ORDER_IDX", (sourceOrdIdx + sourceOrdCnt - 1), ">"));
						targetSearchList.add(new DTForm("ORDER_IDX", targetOrdIdx, "<="));
					}
				}
			
				// target 순서 수정
				if(targetDataList != null) {
					Map<String, Object> targetParam = new HashMap<String, Object>();
					targetParam.put("dataList", targetDataList);
					targetParam.put("searchList", targetSearchList);
					int result1 = classOptnDAO.update(targetParam);
				}
			}
				
			// source 순서 수정
			if(addClassLevel != 0) {
				if(sourceDataList == null) sourceDataList = new ArrayList<DTForm>();				
				sourceDataList.add(new DTForm("CLASS_LEVEL", "CLASS_LEVEL + (" + addClassLevel + ")", 1));
			}
			
			/*if(sourceDataList != null) {
				Map<String, Object> sourceParam = new HashMap<String, Object>();
				sourceDataList.add(new DTForm("GROUP_CLASS_IDX", groupClassIdx));
				sourceParam.put("dataList", sourceDataList);
				List<DTForm> sourceSearchList = new ArrayList<DTForm>();
				sourceSearchList.add(masterSearchForm);
				//List<DTForm> sourceSearchList2 = new ArrayList<DTForm>();
				//sourceSearchList2.add(new DTForm("CLASS_IDX", sourceClassIdx));
				sourceParam.put("searchList", sourceSearchList);
				//sourceParam.put("searchList2", sourceSearchList2);
				sourceParam.put("classIdx", sourceClassIdx);
				classOptnDAO.updateOrdIdx(sourceParam);
			}*/
			
			// 이동할 data update
			if(sourceClassList != null && sourceDataList != null) {
				Map<String, Object> sourceParam = new HashMap<String, Object>();
				sourceDataList.add(new DTForm("GROUP_CLASS_IDX", groupClassIdx));
				sourceParam.put("dataList", sourceDataList);
				
				List<DTForm> sourceSearchList = new ArrayList<DTForm>();
				sourceSearchList.add(masterSearchForm);
				sourceSearchList.add(new DTForm("CLASS_IDX", sourceClassList.toArray()));
				sourceParam.put("searchList", sourceSearchList);
				int result1 = classOptnDAO.updateOrdIdx(sourceParam);
			}
			dataList.add(new DTForm("PARENT_CLASS_IDX", parentClassIdx));
			//dataList.add(new DTForm("GROUP_MENU_IDX", groupClassIdx));
		}
		
    	param.put("dataList", dataList);
    	param.put("searchList", searchList);
    	
		int result = classOptnDAO.update(param);	
		// 언어 사용하는 경우 : 언어 테이블에 저장
		if(useLang == 1 && result > 0) {
	    	langParam.put("dataList", langDataList);
	    	langParam.put("searchList", searchList);
			classOptnDAO.updateLang(lang, langParam);	
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
    	return classOptnDAO.getDeleteCount(lang, param);
    }

	/**
	 * 삭제 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDeleteList(String lang, Map<String, Object> param) {
		return classOptnDAO.getDeleteList(lang, param);
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
		
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}

		int result = 1;
		try {
			for(String classIdx : deleteIdxs) {
				// 저장조건
				param.put("masterCode", mstCd);
				param.put("classIdx", classIdx);
				param.put("modiIdx", loginMemberIdx);
				param.put("modiId", loginMemberId);
				param.put("modiName", loginMemberName);
				param.put("modiIp", regiIp);
				
				int result1 = classOptnDAO.delete(param);
			}
		} catch(Exception e) {
			result = 0;
		}
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

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();

		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
		
		int result = 1;
		try {
			for(String classIdx : restoreIdxs) {
				// 저장조건
				param.put("masterCode", mstCd);
				param.put("classIdx", classIdx);
				param.put("modiIdx", loginMemberIdx);
				param.put("modiId", loginMemberId);
				param.put("modiName", loginMemberName);
				param.put("modiIp", regiIp);
				
				int result1 = classOptnDAO.restore(param);
			}
		} catch(Exception e) {
			result = 0;
		}
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
		int result = 1;
		int result1 = 0;
		try {
			for(String classIdx : deleteIdxs) {
				// 저장조건
				param.put("masterCode", mstCd);
				param.put("classIdx", classIdx);
				
				result1 = classOptnDAO.cdelete(param);
			}
		} catch(Exception e) {
			result = 0;
		}
		
		if(result > 0) {
			
			// mst_cd에 해당하는 전체 순서 update
			param = new HashMap<String, Object>();
			List<DTForm> searchList = new ArrayList<DTForm>();
			// 저장조건
			searchList.add(new DTForm("MASTER_CODE", mstCd));
			param.put("searchList", searchList);
			
			result1 = classOptnDAO.updateTotOrdIdx(param);
		}
		
		return result;
	}

}