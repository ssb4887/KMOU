package rbs.modules.poll.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.DateUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.egovframework.LoginVO;
import rbs.modules.poll.mapper.ItemMapper;
import rbs.modules.poll.mapper.PollMapper;
import rbs.modules.poll.mapper.QuesMapper;
import rbs.modules.poll.mapper.RespCntMapper;
import rbs.modules.poll.mapper.RespMapper;
import rbs.modules.poll.service.QuesService;

/**
 * 설문관리에 관한 구현클래스를 정의한다.
 * @author user
 *
 */
@Service("pollQuesService")
public class QuesServiceImpl extends EgovAbstractServiceImpl implements QuesService {

	@Resource(name="pollMapper")
	private PollMapper pollDAO;
	
	@Resource(name="respCntMapper")
	private RespCntMapper respCntDAO;
	
	@Resource(name="pollQuesMapper")
	private QuesMapper quesDAO;
	
	@Resource(name="pollItemMapper")
	private ItemMapper itemDAO;
	
	@Resource(name="pollRespMapper")
	private RespMapper respDAO;
	
	/**
	 * 설문정보
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public DataMap getPollInfo(int fnIdx, Map<String, Object> param){
    	param.put("fnIdx", fnIdx);
		return pollDAO.getPollInfo(param);
	}
	
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
    	return quesDAO.getTotalCount(param);
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
		return quesDAO.getList(param);
	}

    /**
	 * 전체 목록
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getRespCntList(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return respCntDAO.getQuesList(param);
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
    	DataMap viewDAO = quesDAO.getFileView(param);
		return viewDAO;
	}
	
	@Override
	public List<Object> getQuesList(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return quesDAO.getQuesList(param);
	}
	
	@Override
	public List<Object> getInclassList(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return quesDAO.getInclassList(param);
	}
	
	@Override
	public Map<String, List<Object>> getInquesMap(int fnIdx, Map<String, Object> param){
    	param.put("fnIdx", fnIdx);
		return quesDAO.getInquesList(param);
	}
	
	@Override
	public List<Object> getItemList(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return itemDAO.getList(param);
	}
	
	@Override
	public List<Object> getItemJSONList(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return itemDAO.getItemJSONList(param);
	}
	
	public DataMap getItemContView(int fnIdx, Map<String, Object> param){
    	param.put("fnIdx", fnIdx);
		return itemDAO.getItemContView(param);
		
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
    	DataMap viewDAO = quesDAO.getView(param);
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
    	DataMap viewDAO = quesDAO.getModify(param);
		return viewDAO;
	}

	@Override
	public int pollMng(boolean isPollIngChk, int fnIdx, int pollIdx) {
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.POLL_IDX", pollIdx));
		param.put("searchList", searchList);
		DataMap dt = getPollInfo(fnIdx, param);
		if(dt == null) {
			// 해당글이 없는 경우
			return -1;
		}
		
		int respCnt = getRespCnt(fnIdx, param);
		
		if(isPollIngChk) {
			// 설문진행 체크
			String slimitDate = StringUtil.getString(dt.get("LIMIT_DATE11"));
			if(!StringUtil.isEmpty(slimitDate)) {
				//int pollState = StringUtil.getInt(dt.get("ISSTOP"));
				slimitDate = dt.get("LIMIT_DATE11") + " " + StringUtil.getString(dt.get("LIMIT_DATE12"), "00")  + ":" + StringUtil.getString(dt.get("LIMIT_DATE13"), "00") + ":00";
				//int limitState = DateUtil.getDateTimeState(slimitDate);
				
				if(/*pollState != 2 && limitState <= 0 && respCnt > 0 || */respCnt > 0) {
					// 설문진행중인 경우
					return -2;
				}
			}
		}
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		request.setAttribute("ISQUESTYPE", (StringUtil.getInt(dt.get("ISQUESTYPE")) == 1)?true:false);
		request.setAttribute("ISPOLLITEM", (StringUtil.getInt(dt.get("ISPOLLITEM")) == 1)?true:false);
		
		request.setAttribute("pollDt", dt);
		request.setAttribute("respCnt", respCnt);
		
		return 1;
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int insert(String uploadModulePath, int fnIdx, int pollIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		int result = pollMng(true, fnIdx, pollIdx);
		if(result < 0) return result - 10;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	
		// 1. key 얻기
    	List<DTForm> searchList = new ArrayList<DTForm>();
    	searchList.add(new DTForm("A.POLL_IDX", pollIdx));
    	param.put("fnIdx", fnIdx);
    	param.put("searchList", searchList);
		int quesIdx = quesDAO.getNextId(param);

		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;

    	
    	int targetQuesIdx = StringUtil.getInt(parameterMap.get("ordIdx"));
    	int ordType = StringUtil.getInt(parameterMap.get("ordType"));
    	// 순서 setting
		int targetOrdIdx = 0;
		Map<String, Object> sparam2 = new HashMap<String, Object>();
		List<DTForm> searchList2 = new ArrayList<DTForm>();
		searchList2.add(new DTForm("A.POLL_IDX", pollIdx));
		sparam2.put("fnIdx", fnIdx);
		if(targetQuesIdx <= 0) {
			// 순서 최대값 select
			sparam2.put("searchList", searchList2);
			targetOrdIdx = quesDAO.getMaxOrdIdx(sparam2) + 1;
		} else/* if(!StringUtil.isEquals(ordOptCd, optCd))*/ {
			// 선택한 순서 select
			searchList2.add(new DTForm("A.QUESTION_IDX", targetQuesIdx));
			sparam2.put("searchList", searchList2);
			targetOrdIdx = quesDAO.getOrdIdx(sparam2);

			// 선택한 순서 이상 증가
			String whereFlag = null;
			if(ordType == 1) {
				// 위
				whereFlag = ">=";
			} else {
				// 아래
				whereFlag = ">";
			}
			int result1 = 0;
			Map<String, Object> sparam3 = new HashMap<String, Object>();
			List<DTForm> dataList3 = new ArrayList<DTForm>();
			List<DTForm> searchList3 = new ArrayList<DTForm>();
			searchList3.add(new DTForm("POLL_IDX", pollIdx));
			searchList3.add(new DTForm("QUESTION_ORDER", targetOrdIdx, whereFlag));
			dataList3.add(new DTForm("QUESTION_ORDER", "QUESTION_ORDER + 1", 1));

			sparam3.put("fnIdx", fnIdx);
			sparam3.put("dataList", dataList3);
			sparam3.put("searchList", searchList3);
			result1 = quesDAO.update(sparam3);
			
			if(ordType == 2) {
				// 아래
				targetOrdIdx ++;
			}
		}
		
		// 2.1 저장항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);

    	dataList.add(new DTForm("POLL_IDX", pollIdx));
    	dataList.add(new DTForm("QUESTION_IDX", quesIdx));
    	
    	
    	int relQuesIdx = StringUtil.getInt(parameterMap.get("relQuesIdx"));
    	String relItemIdxs = StringUtil.getString(parameterMap.get("relItemIdx"));
		
    	dataList.add(new DTForm("QUESTION_ORDER", targetOrdIdx));
    	dataList.add(new DTForm("REL_QUES_IDX", relQuesIdx));
    	dataList.add(new DTForm("REL_ITEM_IDX", relItemIdxs));
    	
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

    	param.put("fnIdx", fnIdx);
    	param.put("dataList", dataList);
    	
    	// 4. DB 저장
		result = quesDAO.insert(param);
		int result1 = 0;
		if(result > 0) {

			String[] inclassIdxs = StringUtil.stringToArray(parameterMap.get("inclassIdxs"), ",");
			String[] inquesIdxs = StringUtil.stringToArray(parameterMap.get("inquesIdxs"), ",");
			String[] itemIdxs = StringUtil.stringToArray(parameterMap.get("itemIdxs"), ",");

	    	// 분류
			if(inclassIdxs != null) {
				String inclassName = null;
				List<DTForm> inclassDataList = null;
				for(String inclassIdx:inclassIdxs){	    	    		    	
					inclassName = StringUtil.getString(parameterMap.get("inclassContents" + inclassIdx));
		            
		    		inclassDataList = new ArrayList();
		    		inclassDataList.add(new DTForm("POLL_IDX", pollIdx));
		    		inclassDataList.add(new DTForm("QUESTION_IDX", quesIdx));
		    		inclassDataList.add(new DTForm("CLASS_IDX", inclassIdx));
		    		inclassDataList.add(new DTForm("CLASS_NAME", inclassName));
		    		
					Map<String, Object> sparam = new HashMap<String, Object>();
					sparam.put("fnIdx", fnIdx);
					sparam.put("dataList", inclassDataList);
					result1 = quesDAO.inclassInsert(sparam);
				}
			}
			
	    	// 내부문항
			if(inquesIdxs != null) {
				String[] inClassQuesIdxs = null;
				int inclassIdx = 0;
				int inquesOrder = 0;
				int inquesAddIdx = 0;
				String inquesContents = null;
				List<DTForm> inquesDataList = null;
				for(String inClassQuesIdx:inquesIdxs){
					inClassQuesIdxs = StringUtil.stringToArray(inClassQuesIdx, ":");
					inclassIdx = StringUtil.getInt(inClassQuesIdxs[0]);
					inquesOrder = StringUtil.getInt(inClassQuesIdxs[1]);
					inquesAddIdx ++;
					inquesContents = StringUtil.getString(parameterMap.get("inquesContents" + inclassIdx + "_" + inquesOrder));
		            
		    		inquesDataList = new ArrayList();
		    		inquesDataList.add(new DTForm("POLL_IDX", pollIdx));
		    		inquesDataList.add(new DTForm("CLASS_IDX", inclassIdx));
		    		inquesDataList.add(new DTForm("INQUES_ORDER", inquesOrder));
		    		inquesDataList.add(new DTForm("CONTENTS", inquesContents));
		    		
		    		inquesDataList.add(new DTForm("QUESTION_IDX", quesIdx + inquesAddIdx));
		    		inquesDataList.add(new DTForm("QUESTION_ORDER", targetOrdIdx));
		    		inquesDataList.add(new DTForm("PAR_QUES_IDX", quesIdx));
		    		
					Map<String, Object> sparam = new HashMap<String, Object>();
					sparam.put("fnIdx", fnIdx);
					sparam.put("dataList", inquesDataList);
					result1 = quesDAO.insert(sparam);
				}
			}
			// 항목
			if(itemIdxs != null) {
				int answerType = StringUtil.getInt(parameterMap.get("answerType"));
				int inputTextDVal = (answerType == 1) ? 2 : 1;
				int inputText = 0;
				String itemContents = null;
				int itemPoints = 0;
				List<DTForm> pollItemDataList = null;
				int itemIdxOrder = 0;
				for(String itemIdx:itemIdxs){	    	    		    	
		    		itemContents = StringUtil.getString(parameterMap.get("itemContents" + itemIdx));
		            itemPoints = StringUtil.getInt(parameterMap.get("itemPoints" + itemIdx));
		            inputText = StringUtil.getInt(parameterMap.get("isInputText" + itemIdx), inputTextDVal);
		            
		    		pollItemDataList = new ArrayList();
		    		pollItemDataList.add(new DTForm("POLL_IDX", pollIdx));
		    		pollItemDataList.add(new DTForm("QUESTION_IDX", quesIdx));
		    		pollItemDataList.add(new DTForm("ITEM_IDX", itemIdx));
		    		pollItemDataList.add(new DTForm("CONTENTS", itemContents));
		    		pollItemDataList.add(new DTForm("POINTS", itemPoints));
		    		pollItemDataList.add(new DTForm("ISTEXT", inputText));
		    		pollItemDataList.add(new DTForm("ITEM_ORDER", ++ itemIdxOrder));

					Map<String, Object> sparam = new HashMap<String, Object>();
					sparam.put("fnIdx", fnIdx);
					sparam.put("dataList", pollItemDataList);
					result1 = itemDAO.insert(sparam);
				}
			}
		}
		
		return result;
	}

	/**
	 * 수정처리 : 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param brdIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int update(String uploadModulePath, int fnIdx, int pollIdx, int quesIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	param.put("fnIdx", fnIdx);												// table flag

    	// 1. 검색조건 setting
    	searchList.add(new DTForm("POLL_IDX", pollIdx));
		searchList.add(new DTForm("QUESTION_IDX", quesIdx));
		
		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		int targetQuesIdx = StringUtil.getInt(parameterMap.get("ordIdx"));
    	int ordType = StringUtil.getInt(parameterMap.get("ordType"));
    	// 순서 setting
		int targetOrdIdx = 0;
		int sourceOrdIdx = 0;
		if(targetQuesIdx > 0 && targetQuesIdx != quesIdx && ordType > 0) {
			// 선택한 순서 select
			Map<String, Object> sparam2 = new HashMap<String, Object>();
			List<DTForm> searchList2 = new ArrayList<DTForm>();
			searchList2.add(new DTForm("A.POLL_IDX", pollIdx));
			searchList2.add(new DTForm("A.QUESTION_IDX", quesIdx));
			sparam2.put("fnIdx", fnIdx);
			sparam2.put("searchList", searchList2);
			sourceOrdIdx = quesDAO.getOrdIdx(sparam2);
			
			sparam2 = new HashMap<String, Object>();
			searchList2 = new ArrayList<DTForm>();
			searchList2.add(new DTForm("A.POLL_IDX", pollIdx));
			searchList2.add(new DTForm("A.QUESTION_IDX", targetQuesIdx));
			sparam2.put("fnIdx", fnIdx);
			sparam2.put("searchList", searchList2);
			targetOrdIdx = quesDAO.getOrdIdx(sparam2);

			// 선택한 순서 이상 증가
			String sourceWFlag = null;
			String targetWFlag = null;
			String orderFlag = null;
			if(sourceOrdIdx > targetOrdIdx) { 
				if(ordType == 1) {
					// 위
					targetWFlag = ">=";
				} else {
					// 아래
					targetWFlag = ">";
				}
				sourceWFlag = "<";
				orderFlag = "+";
			} else {
				if(ordType == 1) {
					// 위
					targetWFlag = "<";
				} else {
					// 아래
					targetWFlag = "<=";
				}
				sourceWFlag = ">";
				orderFlag = "-";
			}
			int result1 = 0;
			Map<String, Object> sparam3 = new HashMap<String, Object>();
			List<DTForm> dataList3 = new ArrayList<DTForm>();
			List<DTForm> searchList3 = new ArrayList<DTForm>();
			searchList3.add(new DTForm("POLL_IDX", pollIdx));
			searchList3.add(new DTForm("QUESTION_ORDER", targetOrdIdx, targetWFlag));
			searchList3.add(new DTForm("QUESTION_ORDER", sourceOrdIdx, sourceWFlag));
			dataList3.add(new DTForm("QUESTION_ORDER", "QUESTION_ORDER " + orderFlag + " 1", 1));

			sparam3.put("fnIdx", fnIdx);
			sparam3.put("dataList", dataList3);
			sparam3.put("searchList", searchList3);
			result1 = quesDAO.update(sparam3);
			
			if(sourceOrdIdx > targetOrdIdx && ordType == 2) {
				// 현재 순서 > 이동할 순서 && 아래
				targetOrdIdx ++;
			} else if(sourceOrdIdx < targetOrdIdx && ordType == 1) {
				// 현재 순서 < 이동할 순서 && 위
				targetOrdIdx --;
			}
	    	dataList.add(new DTForm("QUESTION_ORDER", targetOrdIdx));
		}
		
		// 2.1 저장항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		int relQuesIdx = StringUtil.getInt(parameterMap.get("relQuesIdx"));
    	String relItemIdxs = StringUtil.getString(parameterMap.get("relItemIdx"));
		
    	dataList.add(new DTForm("REL_QUES_IDX", relQuesIdx));
    	dataList.add(new DTForm("REL_ITEM_IDX", relItemIdxs));
		
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

    	param.put("fnIdx", fnIdx);
    	param.put("dataList", dataList);
    	param.put("searchList", searchList);
    	
		// 3. DB 저장
		int result = quesDAO.update(param);
		int result1 = 0;
		if(result > 0) {
			result1 = quesDAO.inclassCdelete(param);
			result1 = itemDAO.cdelete(param);
			
			searchList = new ArrayList<DTForm>();
	    	searchList.add(new DTForm("POLL_IDX", pollIdx));
			searchList.add(new DTForm("PAR_QUES_IDX", quesIdx));
	    	param.put("fnIdx", fnIdx);
	    	param.put("searchList", searchList);
	    	result1 = quesDAO.cdelete(param);

			String[] inclassIdxs = StringUtil.stringToArray(parameterMap.get("inclassIdxs"), ",");
			String[] inquesIdxs = StringUtil.stringToArray(parameterMap.get("inquesIdxs"), ",");
			String[] itemIdxs = StringUtil.stringToArray(parameterMap.get("itemIdxs"), ",");

	    	// 분류
			if(inclassIdxs != null) {
				String inclassName = null;
				List<DTForm> inclassDataList = null;
				for(String inclassIdx:inclassIdxs){	    	    		    	
					inclassName = StringUtil.getString(parameterMap.get("inclassContents" + inclassIdx));
		            
		    		inclassDataList = new ArrayList();
		    		inclassDataList.add(new DTForm("POLL_IDX", pollIdx));
		    		inclassDataList.add(new DTForm("QUESTION_IDX", quesIdx));
		    		inclassDataList.add(new DTForm("CLASS_IDX", inclassIdx));
		    		inclassDataList.add(new DTForm("CLASS_NAME", inclassName));
		    		
					Map<String, Object> sparam = new HashMap<String, Object>();
					sparam.put("fnIdx", fnIdx);
					sparam.put("dataList", inclassDataList);
					result1 = quesDAO.inclassInsert(sparam);
				}
			}
			
	    	// 내부문항
			if(inquesIdxs != null) {
				String[] inClassQuesIdxs = null;
				int inclassIdx = 0;
				int inquesOrder = 0;
				int inquesAddIdx = 0;
				String inquesContents = null;
				List<DTForm> inquesDataList = null;
				for(String inClassQuesIdx:inquesIdxs){
					inClassQuesIdxs = StringUtil.stringToArray(inClassQuesIdx, ":");
					inclassIdx = StringUtil.getInt(inClassQuesIdxs[0]);
					inquesOrder = StringUtil.getInt(inClassQuesIdxs[1]);
					inquesAddIdx ++;
					inquesContents = StringUtil.getString(parameterMap.get("inquesContents" + inclassIdx + "_" + inquesOrder));
		            
		    		inquesDataList = new ArrayList();
		    		inquesDataList.add(new DTForm("POLL_IDX", pollIdx));
		    		inquesDataList.add(new DTForm("CLASS_IDX", inclassIdx));
		    		inquesDataList.add(new DTForm("INQUES_ORDER", inquesOrder));
		    		inquesDataList.add(new DTForm("CONTENTS", inquesContents));
		    		
		    		inquesDataList.add(new DTForm("QUESTION_IDX", quesIdx + inquesAddIdx));
		    		inquesDataList.add(new DTForm("QUESTION_ORDER", targetOrdIdx));
		    		inquesDataList.add(new DTForm("PAR_QUES_IDX", quesIdx));
		    		
					Map<String, Object> sparam = new HashMap<String, Object>();
					sparam.put("fnIdx", fnIdx);
					sparam.put("dataList", inquesDataList);
					result1 = quesDAO.insert(sparam);
				}
			}

			// 항목
			if(itemIdxs != null) {
				int answerType = StringUtil.getInt(parameterMap.get("answerType"));
				int inputTextDVal = (answerType == 1) ? 2 : 1;
				int inputText = 0;
				String itemContents = null;
				int itemPoints = 0;
				List<DTForm> pollItemDataList = null;
				int itemIdxOrder = 0;
				for(String itemIdx:itemIdxs){	    	    		    	
		    		itemContents = StringUtil.getString(parameterMap.get("itemContents" + itemIdx));
		            itemPoints = StringUtil.getInt(parameterMap.get("itemPoints" + itemIdx));
		            inputText = StringUtil.getInt(parameterMap.get("isInputText" + itemIdx), inputTextDVal);
		            
		    		pollItemDataList = new ArrayList();
		    		pollItemDataList.add(new DTForm("POLL_IDX", pollIdx));
		    		pollItemDataList.add(new DTForm("QUESTION_IDX", quesIdx));
		    		pollItemDataList.add(new DTForm("ITEM_IDX", itemIdx));
		    		pollItemDataList.add(new DTForm("CONTENTS", itemContents));
		    		pollItemDataList.add(new DTForm("POINTS", itemPoints));
		    		pollItemDataList.add(new DTForm("ISTEXT", inputText));
		    		pollItemDataList.add(new DTForm("ITEM_ORDER", ++ itemIdxOrder));

					Map<String, Object> sparam = new HashMap<String, Object>();
					sparam.put("fnIdx", fnIdx);
					sparam.put("dataList", pollItemDataList);
					result1 = itemDAO.insert(sparam);
				}
			}
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
    	return quesDAO.getDeleteCount(param);
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
		return quesDAO.getDeleteList(param);
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
	public int delete(int fnIdx, int pollIdx, String[] deleteIdxs, String regiIp) throws Exception {
		if(deleteIdxs == null) return 0;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	param.put("fnIdx", fnIdx);												// table flag
		
		// 1. 저장조건
    	searchList.add(new DTForm("POLL_IDX", pollIdx));
		searchList.add(new DTForm("QUESTION_IDX", deleteIdxs));
		searchList.add(new DTForm("PAR_QUES_IDX", deleteIdxs, "IN", "OR"));
		
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
		return quesDAO.delete(param);
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
	public int restore(int fnIdx, int pollIdx, String[] restoreIdxs, String regiIp) throws Exception {
		if(restoreIdxs == null) return 0;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	param.put("fnIdx", fnIdx);												// table flag

		// 1. 저장조건
    	searchList.add(new DTForm("POLL_IDX", pollIdx));
		//searchList.add(new DTForm("QUESTION_IDX", restoreIdxs));
		searchList.add(new DTForm("QUESTION_IDX", restoreIdxs));
		searchList.add(new DTForm("PAR_QUES_IDX", restoreIdxs, "IN", "OR"));

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
		return quesDAO.restore(param);
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
	public int cdelete(String uploadModulePath, int fnIdx, int pollIdx, String[] deleteIdxs, JSONObject items, JSONArray itemOrder) throws Exception {
		if(deleteIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
    	param.put("fnIdx", fnIdx);												// table flag
		
		// 1. 저장조건
    	searchList.add(new DTForm("POLL_IDX", pollIdx));
		//searchList.add(new DTForm("QUESTION_IDX", deleteIdxs));
		searchList.add(new DTForm("QUESTION_IDX", deleteIdxs));
		searchList.add(new DTForm("PAR_QUES_IDX", deleteIdxs, "IN", "OR"));
		param.put("searchList", searchList);
		
		// 3. delete
		int result = quesDAO.cdelete(param);
		int result1 = 0;
		if(result > 0) {
			
			// mst_cd에 해당하는 전체 순서 update
			param = new HashMap<String, Object>();
			searchList = new ArrayList<DTForm>();
			// 저장조건
			searchList.add(new DTForm("POLL_IDX", pollIdx));
			param.put("fnIdx", fnIdx);
			param.put("searchList", searchList);
			
			result1 = quesDAO.updateTotOrdIdx(param);
		}
		
		return result;
	}
}
