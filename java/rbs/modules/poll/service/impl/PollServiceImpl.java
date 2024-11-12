package rbs.modules.poll.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.FileUtil;
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
import rbs.modules.poll.service.PollService;

/**
 * 설문관리에 관한 구현클래스를 정의한다.
 * @author user
 *
 */
@Service("pollService")
public class PollServiceImpl extends EgovAbstractServiceImpl implements PollService {

	@Resource(name="pollMapper")
	private PollMapper pollDAO;
	
	@Resource(name="respCntMapper")
	private RespCntMapper respCntDAO;
	
	@Resource(name="pollQuesMapper")
	private QuesMapper quesDAO;
	
	@Resource(name="pollItemMapper")
	private ItemMapper itemDAO;
	
	/**
	 * 전체 목록 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
    @Override
	public int getTotalCount(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	return pollDAO.getTotalCount(param);
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
		return pollDAO.getList(param);
	}
	
	@Override
	public List<Object> getRespCntList(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return respCntDAO.getPollList(param);
	}

	@Override
	public DataMap getRespCntView(int fnIdx, Map<String, Object> param) {
    	if(param == null) param = new HashMap<String, Object>();
    	param.put("fnIdx", fnIdx);
    	DataMap viewDAO = respCntDAO.getPollView(param);
		return viewDAO;
	}
	
	/**
	 * 진행중인 설문 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
    @Override
	public int getIngCount(int fnIdx, Map<String, Object> param) {
    	if(param == null) param = new HashMap<String, Object>();
    	param.put("fnIdx", fnIdx);
    	return pollDAO.getIngCount(param);
    }

	/**
	 * 진행중인 설문
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getIngView(int fnIdx, Map<String, Object> param) {
    	if(param == null) param = new HashMap<String, Object>();
    	param.put("fnIdx", fnIdx);
    	param.put("recordCountPerPage", 1);
    	param.put("firstIndex", 0);
    	DataMap viewDAO = pollDAO.getIngView(param);
		return viewDAO;
	}
	
	@Override
	public Map<String, List<Object>> getInclassMap(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return quesDAO.getInclassMap(param);
	}
	
	@Override
	public Map<String, List<Object>> getInquesMap(int fnIdx, Map<String, Object> param){
    	param.put("fnIdx", fnIdx);
		return quesDAO.getInquesList(param);
	}
	
	@Override
	public Map<String, List<Object>> getItemMap(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return itemDAO.getItemMap(param);
	}
	
	@Override
	public Map<String, List<Object>> getRespCntItemMap(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return respCntDAO.getItemMap(param);
	}
	
	@Override
	public Map<String, List<Object>> getRespCntPItemMap(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return respCntDAO.getItemMap(true, param);
	}
	/*
	@Override
	public Map<String, List<Object>> getItemTResultMap(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return itemDAO.getItemTResultMap(param);
	}
	*/
	/**
	 * 파일 상세조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getFileView(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	DataMap viewDAO = pollDAO.getFileView(param);
		return viewDAO;
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
    	DataMap viewDAO = pollDAO.getView(param);
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
    	DataMap viewDAO = pollDAO.getModify(param);
		return viewDAO;
	}
	
	@Override
	public List<Object> getPItemList(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return itemDAO.getList(true, param);
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
	public int insert(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	param.put("fnIdx", fnIdx);												// table flag
    	
		// 1. key 얻기
		int pollIdx = pollDAO.getNextId(param);

		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;

		// 2.1 저장항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
    	dataList.add(new DTForm("POLL_IDX", pollIdx));
    	
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
    	
    	// 4. DB 저장
		int result = pollDAO.insert(param);
		int result1 = 0;
		if(result > 0) {
			// pitem 항목 삭제
			List searchList = new ArrayList<DTForm>();
	    	searchList.add(new DTForm("POLL_IDX", pollIdx));
	    	param.put("fnIdx", fnIdx);
	    	param.put("searchList", searchList);
	    	
	    	result1 = itemDAO.cdelete(true, param);

			String[] itemIdxs = StringUtil.stringToArray(parameterMap.get("itemIdxs"), ",");
			
			// pitem 항목 등록
			if(itemIdxs != null) {
				int answerType = 1;
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
		    		pollItemDataList.add(new DTForm("ITEM_IDX", itemIdx));
		    		pollItemDataList.add(new DTForm("CONTENTS", itemContents));
		    		pollItemDataList.add(new DTForm("POINTS", itemPoints));
		    		pollItemDataList.add(new DTForm("ISTEXT", inputText));
		    		pollItemDataList.add(new DTForm("ITEM_ORDER", ++ itemIdxOrder));

					Map<String, Object> sparam = new HashMap<String, Object>();
					sparam.put("fnIdx", fnIdx);
					sparam.put("dataList", pollItemDataList);
					result1 = itemDAO.insert(true, sparam);
				}
			}

			int useOnePoll = JSONObjectUtil.getInt(settingInfo, "use_list_noreply");
			if(useOnePoll == 1) {
				// 진행설문 1개 사용 : '진행'인 경우 다른 '진행'설문 '중지'로 update
				int isstop = StringUtil.getInt(parameterMap.get("isstop"));
				if(isstop == 1) {
					// 다른 '진행'설문 '중지'로 update
					Map<String, Object> updateParam = new HashMap<String, Object>();		// mapper parameter 데이터
					dataList = new ArrayList<DTForm>();										// 저장항목
					searchList = new ArrayList<DTForm>();						// 조건
					searchList.add(new DTForm("POLL_IDX", pollIdx, "<>"));
					searchList.add(new DTForm("ISSTOP", "1"));
			    	dataList.add(new DTForm("ISSTOP", "2"));
			    	updateParam.put("fnIdx", fnIdx);
					updateParam.put("dataList", dataList);
					updateParam.put("searchList", searchList);
					result1 = pollDAO.update(updateParam);
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
	public int update(String uploadModulePath, int fnIdx, int pollIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	param.put("fnIdx", fnIdx);												// table flag

    	// 1. 검색조건 setting
		searchList.add(new DTForm("POLL_IDX", pollIdx));
		
		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
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
    	
    	dataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));

    	param.put("dataList", dataList);
    	param.put("searchList", searchList);
    	
		// 3. DB 저장
		int result = pollDAO.update(param);
				
		// 4. file 삭제
		List deleteFileList = StringUtil.getList(dataMap.get("deleteFileList"));
		if(deleteFileList != null) {
			FileUtil.isDelete(fileRealPath, deleteFileList);
		}

		int result1 = 0;
		if(result > 0) {
			// pitem 항목 삭제
			result1 = itemDAO.cdelete(true, param);

			String[] itemIdxs = StringUtil.stringToArray(parameterMap.get("itemIdxs"), ",");

			// pitem 항목 등록
			if(itemIdxs != null) {
				int answerType = 1;
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
		    		pollItemDataList.add(new DTForm("ITEM_IDX", itemIdx));
		    		pollItemDataList.add(new DTForm("CONTENTS", itemContents));
		    		pollItemDataList.add(new DTForm("POINTS", itemPoints));
		    		pollItemDataList.add(new DTForm("ISTEXT", inputText));
		    		pollItemDataList.add(new DTForm("ITEM_ORDER", ++ itemIdxOrder));

					Map<String, Object> sparam = new HashMap<String, Object>();
					sparam.put("fnIdx", fnIdx);
					sparam.put("dataList", pollItemDataList);
					result1 = itemDAO.insert(true, sparam);
				}
			}
			
			int useOnePoll = JSONObjectUtil.getInt(settingInfo, "use_list_noreply");
			if(useOnePoll == 1) {
				// 진행설문 1개 사용 : '진행'인 경우 다른 '진행'설문 '중지'로 update
				int isstop = StringUtil.getInt(parameterMap.get("isstop"));
				if(isstop == 1) {
					// 다른 '진행'설문 '중지'로 update
					/*Map<String, Object> updateParam = new HashMap<String, Object>();		// mapper parameter 데이터
			    	updateParam.put("fnIdx", fnIdx);
					updateParam.put("pollIdx", pollIdx);
					pollDAO.update(updateParam);*/
					
					Map<String, Object> updateParam = new HashMap<String, Object>();		// mapper parameter 데이터
					dataList = new ArrayList<DTForm>();										// 저장항목
					searchList = new ArrayList<DTForm>();						// 조건
					searchList.add(new DTForm("POLL_IDX", pollIdx, "<>"));
					searchList.add(new DTForm("ISSTOP", "1"));
			    	dataList.add(new DTForm("ISSTOP", "2"));
			    	updateParam.put("fnIdx", fnIdx);
					updateParam.put("dataList", dataList);
					updateParam.put("searchList", searchList);
					result1 = pollDAO.update(updateParam);
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
    	return pollDAO.getDeleteCount(param);
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
		return pollDAO.getDeleteList(param);
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
	public int delete(int fnIdx, String[] deleteIdxs, String regiIp) throws Exception {
		if(deleteIdxs == null) return 0;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	param.put("fnIdx", fnIdx);												// table flag
		
		// 1. 저장조건
		searchList.add(new DTForm("POLL_IDX", deleteIdxs));
		
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
		return pollDAO.delete(param);
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
	public int restore(int fnIdx, String[] restoreIdxs, String regiIp) throws Exception {
		if(restoreIdxs == null) return 0;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	param.put("fnIdx", fnIdx);												// table flag

		// 1. 저장조건
		searchList.add(new DTForm("POLL_IDX", restoreIdxs));

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
		return pollDAO.restore(param);
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
	public int cdelete(String uploadModulePath, int fnIdx, String[] deleteIdxs, JSONObject items, JSONArray itemOrder) throws Exception {
		if(deleteIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
    	param.put("fnIdx", fnIdx);												// table flag
		
		// 1. 저장조건
		searchList.add(new DTForm("POLL_IDX", deleteIdxs));
		param.put("searchList", searchList);
		
		// 2. 삭제할 파일 select
		List<Object> deleteFileList = null;
		List deleteFileColumnList = ModuleUtil.getFileObjectList(items, itemOrder);
		if(deleteFileColumnList != null) {
			param.put("columnList", deleteFileColumnList);
			
			deleteFileList = pollDAO.getDeleteFileList(param);
		}
		
		// 3. delete
		int result = pollDAO.cdelete(param);
		if(result > 0) {
			// 4. 파일(단일항목) 삭제
			if(deleteFileList != null) {
				String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
				FileUtil.isKeyDelete(fileRealPath, deleteFileList);
			}
		}
		
		return result;
	}
}
