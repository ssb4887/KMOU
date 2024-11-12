package rbs.modules.member.service.impl;

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
import rbs.modules.member.mapper.MemberDprtMapper;
import rbs.modules.member.service.MemberDprtService;

/**
 * 기관정보관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 */
@Service("memberDprtService")
public class MemberDprtServiceImpl extends EgovAbstractServiceImpl implements MemberDprtService {

	@Value("${Globals.locale.lang.use}")
	protected int useLang;						// 언어사용여부
	
	@Resource(name="memberDprtMapper")
	private MemberDprtMapper memberDprtDAO;

	/**
	 * 전체 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getOptnList(Map<String, Object> param) {
		return memberDprtDAO.getOptnList(param);
	}

    /**
     * 전체 목록 수
	 * @param mapFlag
	 * @param param
	 * @return
	 */
    @Override
	public int getTotalCount(Map<String, Object> param) {
    	return memberDprtDAO.getTotalCount(param);
    }

	/**
	 * 전체 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(Map<String, Object> param) {
		return memberDprtDAO.getList(param);
	}
	

	@Override
	public List<Object> getSearchList(Map<String, Object> param) {
		return memberDprtDAO.getSearchList(param);
	}

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getModify(Map<String, Object> param) {
		DataMap viewDAO = memberDprtDAO.getModify(param);
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

		// 항목설정으로 저장항목 setting
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(parameterMap, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;

		// 저장
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		
		//고유아이디 셋팅
		Map<String, Object> nextIdParam = new HashMap<String, Object>();
		int departIdx = memberDprtDAO.getNextId(nextIdParam);

		// 순서 setting
		int ordType = StringUtil.getInt(parameterMap.get("ordType"));
		int targetClassIdx = StringUtil.getInt(parameterMap.get("ordIdx"));

		if(targetClassIdx <= 0) {
			int targetOrdIdx = 0;			// 이동될 ord_idx
			// 이동될 ord_idx select
			Map<String, Object> targetOrdParam = new HashMap<String, Object>();
			targetOrdIdx = memberDprtDAO.getMaxOrdIdx(targetOrdParam);
			
			dataList.add(new DTForm("ORDER_IDX", targetOrdIdx + 1));
			dataList.add(new DTForm("GROUP_DEPART_IDX", departIdx));
			dataList.add(new DTForm("DEPART_LEVEL", 1));
		} else {

			// target 순서 수정 data, 조건
			List<DTForm> targetDataList = null;
			List<DTForm> targetSearchList = null;
			targetSearchList = new ArrayList<DTForm>();

			//int targetPrtClassIdx = 0;		// 이동될 PARENT_DEPART_IDX 
			int targetOrdIdx = 0;			// 이동될 ord_idx
			// 이동될 ord_idx select
			Map<String, Object> targetOrdParam = new HashMap<String, Object>();
			List<DTForm> targetOrdSearchList = new ArrayList<DTForm>();
			
			if(ordType == 1) {
				// 위 : 선택한 단원의 첫번째 순서
				targetOrdSearchList.add(new DTForm("DEPART_IDX", targetClassIdx));
				targetOrdParam.put("searchList", targetOrdSearchList);
				targetOrdIdx = memberDprtDAO.getOrdIdx(targetOrdParam);
				
				targetSearchList.add(new DTForm("ORDER_IDX", targetOrdIdx, ">="));
				// ord_idx setting
				dataList.add(new DTForm("ORDER_IDX", targetOrdIdx));
			} else {
				// 아래/내부 : 선택한 단원의 마지막 순서
				targetOrdParam.put("searchList", targetOrdSearchList);
				targetOrdParam.put("departIdx", targetClassIdx);
				targetOrdIdx = memberDprtDAO.getNextOrdIdx(targetOrdParam);

				targetSearchList.add(new DTForm("ORDER_IDX", targetOrdIdx, ">"));
				// ord_idx setting
				dataList.add(new DTForm("ORDER_IDX", targetOrdIdx + 1));
			}
			targetDataList = new ArrayList<DTForm>();
			targetDataList.add(new DTForm("ORDER_IDX", "ORDER_IDX + 1", 1));
			int result1 =  0;
			// target 순서 수정
			if(targetDataList != null) {
				Map<String, Object> targetParam = new HashMap<String, Object>();
				targetParam.put("dataList", targetDataList);
				targetParam.put("searchList", targetSearchList);
				result1 = memberDprtDAO.update(targetParam);
			}
			
			// prt_unt_cd setting
			
			// 선택한 위치의 정보 얻기
			Map<String, Object> targetPrtParam = new HashMap<String, Object>();
			List<DTForm> targetPrtSearchList = new ArrayList<DTForm>();
			targetPrtSearchList.add(new DTForm("DEPART_IDX", targetClassIdx));
			targetPrtParam.put("searchList", targetPrtSearchList);
			DataMap targetClassDt = memberDprtDAO.getDepartKeyView(targetPrtParam);
			int targetGroupClassIdx = StringUtil.getInt(targetClassDt.get("GROUP_DEPART_IDX"));
			int targetClassLevel = StringUtil.getInt(targetClassDt.get("DEPART_LEVEL"));
			int departLevel = targetClassLevel;
			int groupClassIdx = departIdx;
			int parentClassIdx = 0;
			if(ordType == 3) {
				// 내부
				parentClassIdx = targetClassIdx;
				departLevel = targetClassLevel + 1;
				groupClassIdx = targetGroupClassIdx;
			} else {
				// 위/아래
				/*Map<String, Object> targetPrtParam = new HashMap<String, Object>();
				List<DTForm> targetPrtSearchList = new ArrayList<DTForm>();
				targetPrtSearchList.add(masterSearchForm);
				targetPrtSearchList.add(new DTForm("DEPART_IDX", targetClassIdx));
				targetPrtParam.put("searchList", targetPrtSearchList);
				targetPrtClassIdx = memberDprtDAO.getPrtClassIdx(targetPrtParam);*/
				parentClassIdx = StringUtil.getInt(targetClassDt.get("PARENT_DEPART_IDX"));
				if(targetClassLevel > 1) {
					groupClassIdx = targetGroupClassIdx;
					//departLevel = targetClassLevel;
				} else departLevel = 1;
			}
			dataList.add(new DTForm("PARENT_DEPART_IDX", parentClassIdx));
			dataList.add(new DTForm("GROUP_DEPART_IDX", groupClassIdx));
			dataList.add(new DTForm("DEPART_LEVEL", departLevel));
		}

		// insert 항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);

		dataList.add(new DTForm("DEPART_IDX", departIdx));

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
		int result = memberDprtDAO.insert(param);
				
		return result;
	}

	/**
	 * 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param param 수정정보
	 * @throws Exception
	 */
	@Override
	public int update(int sourceClassIdx, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		Map<String, Object> langParam = new HashMap<String, Object>();
		List<DTForm> langDataList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("DEPART_IDX", sourceClassIdx));
		
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
		int targetClassIdx = StringUtil.getInt(parameterMap.get("ordIdx"));
		if(targetClassIdx != sourceClassIdx) {
			int sourcePrtClassIdx = -1;		// 이동할 prt_unt_cd
			int targetPrtClassIdx = -1;		// 이동될 prt_unt_cd 
			int sourceClassLevel = 0;		// 이동할 depart_level
			int sourceOrdIdx = 0;			// 이동할 ord_idx (현재)
			int targetOrdIdx = 0;			// 이동될 ord_idx
			int gOrdCnt = 0;				// sourceOrdIdx - targetOrdIdx
			List<Object> sourceDprtList = null;		// 이동할 departIdx 목록
			int sourceOrdCnt = 0;
			
			// 하위단원으로 이동여부
			Map<String, Object> inChildParam = new HashMap<String, Object>();
			List<DTForm> inChildSearchList = new ArrayList<DTForm>();
			inChildParam.put("searchList", inChildSearchList);
			inChildParam.put("searchDepartIdx", targetClassIdx);
			inChildParam.put("departIdx", sourceClassIdx);
			int inChild = memberDprtDAO.getInChildCount(inChildParam);
			
			if(inChild > 0) {
				// 하위단원으로 이동하는 경우
				return -11;
			}

			
			// 선택한 위치의 정보 얻기
			Map<String, Object> targetPrtParam = new HashMap<String, Object>();
			List<DTForm> targetPrtSearchList = new ArrayList<DTForm>();
			targetPrtSearchList.add(new DTForm("DEPART_IDX", targetClassIdx));
			targetPrtParam.put("searchList", targetPrtSearchList);
			DataMap targetClassDt = memberDprtDAO.getDepartKeyView(targetPrtParam);
			int targetGroupClassIdx = StringUtil.getInt(targetClassDt.get("GROUP_DEPART_IDX"));
			int targetClassLevel = StringUtil.getInt(targetClassDt.get("DEPART_LEVEL"));
			targetOrdIdx = StringUtil.getInt(targetClassDt.get("ORDER_IDX"));
			
			// 이동할 정보 얻기
			Map<String, Object> sourceOrdParam = new HashMap<String, Object>();
			List<DTForm> sourceOrdSearchList = new ArrayList<DTForm>();
			sourceOrdSearchList.add(new DTForm("DEPART_IDX", sourceClassIdx));
			sourceOrdParam.put("searchList", sourceOrdSearchList);
			DataMap sourceClassDt = memberDprtDAO.getDepartKeyView(sourceOrdParam);
			sourceClassLevel = StringUtil.getInt(sourceClassDt.get("DEPART_LEVEL"));
			sourceOrdIdx = StringUtil.getInt(sourceClassDt.get("ORDER_IDX"));
			
			int groupClassIdx = StringUtil.getInt(sourceClassDt.get("GROUP_DEPART_IDX"));
			int parentClassIdx = 0;
			int addClassLevel = targetClassLevel - sourceClassLevel;
			
			// 이동될 prt_unt_cd select 
			if(ordType == 3) {
				// 내부
				targetPrtClassIdx = targetClassIdx;
				
				Map<String, Object> sourcePrtParam = new HashMap<String, Object>();
				List<DTForm> sourcePrtSearchList = new ArrayList<DTForm>();
				sourcePrtSearchList.add(new DTForm("DEPART_IDX", sourceClassIdx));
				sourcePrtParam.put("searchList", sourcePrtSearchList);
				sourcePrtClassIdx = memberDprtDAO.getPrtDepartIdx(sourcePrtParam);
				
				// 같은 상위단원의 내부로 선택된 경우
				if(sourcePrtClassIdx == targetPrtClassIdx) return -12;

				parentClassIdx = targetClassIdx;
				addClassLevel ++;
				groupClassIdx = targetGroupClassIdx;
			} else {
				// 위/아래
				parentClassIdx = StringUtil.getInt(targetClassDt.get("PARENT_DEPART_IDX"));
				if(targetClassLevel > 1) groupClassIdx = targetGroupClassIdx;
				else groupClassIdx = sourceClassIdx;
			}
			
			// 이동될 ord_idx select
			Map<String, Object> targetOrdParam = new HashMap<String, Object>();
			List<DTForm> targetOrdSearchList = new ArrayList<DTForm>();
			
			if(ordType == 1) {
				// 위 : 선택한 단원의 첫번째 순서
			} else {
				// 아래/내부 : 선택한 단원의 마지막 순서
				targetOrdParam.put("searchList", targetOrdSearchList);
				targetOrdParam.put("departIdx", targetClassIdx);
				targetOrdIdx = memberDprtDAO.getNextOrdIdx(targetOrdParam);
			}
			
			// 이동할 순서 - 이동될 순서
			gOrdCnt = sourceOrdIdx - targetOrdIdx;

			// 이동할 departIdx 목록
			Map<String, Object> sourceDprtParam = new HashMap<String, Object>();
			List<DTForm> sourceDprtSearchList = new ArrayList<DTForm>();
			sourceDprtParam.put("searchList", sourceDprtSearchList);
			sourceDprtParam.put("departIdx", sourceClassIdx);
			sourceDprtList = memberDprtDAO.getSourceList(sourceDprtParam);
			sourceOrdCnt = (sourceDprtList != null)? sourceDprtList.size():0;
			
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
			
				int result1 = 0;
				// target 순서 수정
				if(targetDataList != null) {
					Map<String, Object> targetParam = new HashMap<String, Object>();
					targetParam.put("dataList", targetDataList);
					targetParam.put("searchList", targetSearchList);
					result1 = memberDprtDAO.update(targetParam);
				}
			}
			
			// source 순서 수정
			if(addClassLevel != 0) {
				if(sourceDataList == null) sourceDataList = new ArrayList<DTForm>();
				sourceDataList.add(new DTForm("DEPART_LEVEL", "DEPART_LEVEL + (" + addClassLevel + ")", 1));
			}
			/*
			// source 순서 수정
			if(sourceDataList != null) {
				Map<String, Object> sourceParam = new HashMap<String, Object>();
				
				sourceDataList.add(new DTForm("GROUP_DEPART_IDX", groupClassIdx));
				sourceParam.put("dataList", sourceDataList);
				List<DTForm> sourceSearchList2 = new ArrayList<DTForm>();
				sourceSearchList2.add(new DTForm("DEPART_IDX", sourceClassIdx));
				sourceParam.put("searchList2", sourceSearchList2);
				memberDprtDAO.updateOrdIdx(sourceParam);
			}*/
			
			int result1 = 0;
			// 이동할 data update
			if(sourceDprtList != null && sourceDataList != null) {
				Map<String, Object> sourceParam = new HashMap<String, Object>();
				sourceDataList.add(new DTForm("GROUP_DEPART_IDX", groupClassIdx));
				sourceParam.put("dataList", sourceDataList);
				
				List<DTForm> sourceSearchList = new ArrayList<DTForm>();
				sourceSearchList.add(new DTForm("DEPART_IDX", sourceDprtList.toArray()));
				sourceParam.put("searchList", sourceSearchList);
				result1 = memberDprtDAO.updateOrdIdx(sourceParam);
			}
	
			dataList.add(new DTForm("PARENT_DEPART_IDX", parentClassIdx));
			//dataList.add(new DTForm("GROUP_DEPART_IDX", groupClassIdx));
			//dataList.add(new DTForm("DEPART_LEVEL", departLevel));
		}
		
    	param.put("dataList", dataList);
    	param.put("searchList", searchList);
    	
		int result = memberDprtDAO.update(param);	
		// 언어 사용하는 경우 : 언어 테이블에 저장
		if(useLang == 1 && result > 0) {
	    	langParam.put("dataList", langDataList);
	    	langParam.put("searchList", searchList);
			memberDprtDAO.updateLang(langParam);	
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
    	return memberDprtDAO.getDeleteCount(param);
    }

	/**
	 * 삭제 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDeleteList(Map<String, Object> param) {
		return memberDprtDAO.getDeleteList(param);
	}

	/**
	 * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
	 * @param param 삭제정보
	 * @throws Exception
	 */
	@Override
	public int delete(int[] deleteIdxs, String regiIp) throws Exception {
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
		int result1 = 0;
		try {
			for(int departIdx : deleteIdxs) {
				// 저장조건
				param.put("departIdx", departIdx);
				param.put("modiIdx", loginMemberIdx);
				param.put("modiId", loginMemberId);
				param.put("modiName", loginMemberName);
				param.put("modiIp", regiIp);
				
				result1 = memberDprtDAO.delete(param);
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
	public int restore(int[] restoreIdxs, String regiIp) throws Exception {
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
		int result1 = 0;
		try {
			for(int departIdx : restoreIdxs) {
				// 저장조건
				param.put("departIdx", departIdx);
				param.put("modiIdx", loginMemberIdx);
				param.put("modiId", loginMemberId);
				param.put("modiName", loginMemberName);
				param.put("modiIp", regiIp);
				
				result1 = memberDprtDAO.restore(param);
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
	public int cdelete(int[] deleteIdxs, JSONObject items, JSONArray itemOrder) throws Exception {
		if(deleteIdxs == null) return 0;

		Map<String, Object> param = new HashMap<String, Object>();
		int result = 1;
		int result1 = 0;
		try {
			for(int departIdx : deleteIdxs) {
				// 저장조건
				param.put("departIdx", departIdx);
				
				result1 = memberDprtDAO.cdelete(param);
			}
		} catch(Exception e) {
			result = 0;
		}
		
		if(result > 0) {
			
			// mst_cd에 해당하는 전체 순서 update
			param = new HashMap<String, Object>();
			
			result1 = memberDprtDAO.updateTotOrdIdx(param);
		}
		return result;
	}

}