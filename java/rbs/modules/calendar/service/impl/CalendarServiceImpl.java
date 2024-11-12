package rbs.modules.calendar.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.FileUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import rbs.egovframework.LoginVO;
import rbs.modules.calendar.mapper.CalendarFileMapper;
import rbs.modules.calendar.mapper.CalendarMapper;
import rbs.modules.calendar.mapper.CalendarMultiMapper;
import rbs.modules.calendar.service.CalendarService;

/**
 * 다기능게시판관리에 관한 구현클래스를 정의한다.
 * @author user
 *
 */
@Service("calendarService")
public class CalendarServiceImpl extends EgovAbstractServiceImpl implements CalendarService {

	@Resource(name="calendarMapper")
	private CalendarMapper calendarDAO;
	
	@Resource(name="calendarFileMapper")
	private CalendarFileMapper calendarFileDAO;
	
	@Resource(name="calendarMultiMapper")
	private CalendarMultiMapper calendarMultiDAO;


	/**
	 * 전체 목록 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
    @Override
	public int getTotalCount(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	return calendarDAO.getTotalCount(param);
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
		return calendarDAO.getList(param);
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
		DataMap viewDAO = calendarDAO.getView(param);
		return viewDAO;
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
		DataMap viewDAO = calendarDAO.getFileView(param);
		return viewDAO;
	}

	/**
	 * multi파일 상세조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getMultiFileView(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		DataMap viewDAO = calendarFileDAO.getFileView(param);
		return viewDAO;
	}
	
	/**
	 * 조회수 수정
	 * @param fnIdx
	 * @param brdIdx
	 * @return
	 * @throws Exception
	 */
	public int updateViews(int fnIdx, int brdIdx) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
    	param.put("fnIdx", fnIdx);
		searchList.add(new DTForm("CAL_IDX", brdIdx));

    	param.put("searchList", searchList);
		return calendarDAO.updateViews(param);
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
		DataMap viewDAO = calendarDAO.getModify(param);
		return viewDAO;
	}
	
	/**
	 * 권한여부
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public int getAuthCount(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	return calendarDAO.getAuthCount(param);
    }

	/**
	 * 등록처리 : 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param siteMode
	 * @param regiIp
	 * @param parameterMap
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int insert(String uploadModulePath, int fnIdx, String siteMode, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	param.put("fnIdx", fnIdx);												// table flag

		// 1. key 얻기
		int brdIdx = calendarDAO.getNextId(param);

		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
    	dataList.add(new DTForm("CAL_IDX", brdIdx));

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
		int result = calendarDAO.insert(param);

		// 5. multi file 저장
		List fileDataList = StringUtil.getList(dataMap.get("fileDataList"));
		if(fileDataList != null) {
			int fileDataSize = fileDataList.size();
			for(int i = 0 ; i < fileDataSize ; i ++) {
				Map<String, Object> fileParam = (HashMap)fileDataList.get(i);
				fileParam.put("fnIdx", fnIdx);
				fileParam.put("CAL_IDX", brdIdx);
				fileParam.put("FLE_IDX", i + 1);
				result = calendarFileDAO.insert(fileParam);
			}
		}
		
		
		return result;
	}

	/**
	 * 수정처리 : 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param brdIdx
	 * @param siteMode
	 * @param regiIp
	 * @param parameterMap
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int update(String uploadModulePath, int fnIdx, int brdIdx, String siteMode, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	param.put("fnIdx", fnIdx);												// table flag

    	// 1. 검색조건 setting
		searchList.add(new DTForm("CAL_IDX", brdIdx));
		
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
		int result = calendarDAO.update(param);

		// 4. multi file 신규 저장
		List fileDataList = StringUtil.getList(dataMap.get("fileDataList"));
		if(fileDataList != null) {

			// 4.1 key 얻기
			Map<String, Object> fileParam1 = new HashMap<String, Object>();
			fileParam1.put("fnIdx", fnIdx);
			fileParam1.put("CAL_IDX", brdIdx);
			int fleIdx = calendarFileDAO.getNextId(fileParam1);
			
			// 4.2 DB 저장
			int fileDataSize = fileDataList.size();
			for(int i = 0 ; i < fileDataSize ; i ++) {
				Map<String, Object> fileParam = (HashMap)fileDataList.get(i);
				fileParam.put("fnIdx", fnIdx);
				fileParam.put("CAL_IDX", brdIdx);
				fileParam.put("FLE_IDX", fleIdx + i);
				result = calendarFileDAO.insert(fileParam);
			}
		}

		// 5 multi file 수정
		List fileModifyDataList = StringUtil.getList(dataMap.get("fileModifyDataList"));
		if(fileModifyDataList != null) {
			int fileDataSize = fileModifyDataList.size();
			for(int i = 0 ; i < fileDataSize ; i ++) {
				Map<String, Object> fileParam = (HashMap)fileModifyDataList.get(i);
				fileParam.put("fnIdx", fnIdx);
				fileParam.put("CAL_IDX", brdIdx);
				result = calendarFileDAO.update(fileParam);
			}
		}
		
		
		
		// 6. multi file 삭제
		List fileDeleteSearchList = StringUtil.getList(dataMap.get("fileDeleteSearchList"));
		if(fileDeleteSearchList != null) {

			List<Object> deleteMultiFileList = new ArrayList<Object>();
			int fileDataSize = fileDeleteSearchList.size();
			for(int i = 0 ; i < fileDataSize ; i ++) {
				Map<String, Object> fileParam = (HashMap)fileDeleteSearchList.get(i);
				fileParam.put("fnIdx", fnIdx);
				fileParam.put("CAL_IDX", brdIdx);
				// 6.1 삭제목록 select
				List<Object> deleteFileList2 = calendarFileDAO.getList(fileParam);
				if(deleteFileList2 != null) deleteMultiFileList.addAll(deleteFileList2);
				// 6.2 DB 삭제
				result = calendarFileDAO.cdelete(fileParam);
			}
			
			// 6.3 multi file 삭제
			FileUtil.isKeyDelete(fileRealPath, deleteMultiFileList, "FILE_SAVED_NAME");
			/*
			int deleteFileListSize = deleteFileList.size();
			if(deleteFileListSize > 0) {
				for(int i = 0 ; i < deleteFileListSize ; i ++) {
					DataMap listDt = (DataMap)deleteFileList.get(i);
					FileUtil.isDelete(fileRealPath, StringUtil.getString(listDt.get("FILE_SAVED_NAME")));
				}
			}*/
		}
		
		// 7. file(단일항목) 삭제
		List deleteFileList = StringUtil.getList(dataMap.get("deleteFileList"));
		if(deleteFileList != null) {
			FileUtil.isDelete(fileRealPath, deleteFileList);
			/*
			int deleteFileListSize = deleteFileList.size();
			if(deleteFileListSize > 0) {
				String fileName = null;
				for(int i = 0 ; i < deleteFileListSize ; i ++) {
					fileName = StringUtil.getString(deleteFileList.get(i));
					FileUtil.isDelete(fileRealPath, fileName);
				}
			}*/
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
    	return calendarDAO.getDeleteCount(param);
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
		return calendarDAO.getDeleteList(param);
	}

	/**
	 * 삭제처리 : 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
	 * @param fnIdx
	 * @param deleteIdxs
	 * @param siteMode
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	@Override
	public int delete(int fnIdx, String[] deleteIdxs, String siteMode, String regiIp) throws Exception {
		if(deleteIdxs == null) return 0;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	param.put("fnIdx", fnIdx);												// table flag

		// 1. 저장조건
		searchList.add(new DTForm("CAL_IDX", deleteIdxs));

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
		return calendarDAO.delete(param);
	}

	/**
	 * 복원처리 : 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
	 * @param fnIdx
	 * @param restoreIdxs
	 * @param siteMode
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	@Override
	public int restore(int fnIdx, String[] restoreIdxs, String siteMode, String regiIp) throws Exception {
		if(restoreIdxs == null) return 0;
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	param.put("fnIdx", fnIdx);												// table flag

		// 1. 저장조건
		searchList.add(new DTForm("CAL_IDX", restoreIdxs));

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
		return calendarDAO.restore(param);
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
		searchList.add(new DTForm("CAL_IDX", deleteIdxs));
		param.put("searchList", searchList);
		
		// 2. 삭제할 파일 select
		// 2.1 삭제할  multi file 목록 select
		List<Object> deleteMultiFileList = calendarFileDAO.getList(param);
		// 2.2 삭제할 file(단일항목) select
		List<Object> deleteFileList = null;
		List deleteFileColumnList = ModuleUtil.getFileObjectList(items, itemOrder);
		if(deleteFileColumnList != null) {
			param.put("columnList", deleteFileColumnList);
			
			deleteFileList = calendarDAO.getFileList(param);
		}
		
		// 3. delete
		int result = calendarDAO.cdelete(param);
		if(result > 0) {
			// 4. 파일 삭제
			// 4.1 multi file 삭제
			String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
			if(deleteMultiFileList != null) {
				FileUtil.isKeyDelete(fileRealPath, deleteMultiFileList, "FILE_SAVED_NAME");
				/*
				int deleteFileListSize = deleteMultiFileList.size();
				if(deleteFileListSize > 0) {
					for(int i = 0 ; i < deleteFileListSize ; i ++) {
						DataMap listDt = (DataMap)deleteMultiFileList.get(i);
						FileUtil.isDelete(fileRealPath, StringUtil.getString(listDt.get("FILE_SAVED_NAME")));
					}
				}*/
			}
			
			// 4.2 file(단일항목) 삭제
			if(deleteFileList != null) {
				FileUtil.isKeyDelete(fileRealPath, deleteFileList);
				/*int deleteFileSize = deleteFileList.size();
				DataMap deleteFileDt = null;
				String savedFileName = null;
				for(int i = 0 ; i < deleteFileSize ; i ++) {
					try {
						deleteFileDt = (DataMap)deleteFileList.get(i);
						Set<String> set = deleteFileDt.keySet();
						Iterator<String> keys = set.iterator();
						String key = null;
						while(keys.hasNext()) {
							key = (String)keys.next();
							savedFileName = StringUtil.getString(deleteFileDt.get(key));
							if(!StringUtil.isEmpty(savedFileName)) {
								FileUtil.isDelete(fileRealPath, savedFileName);
							}
						}
					} catch(Exception e) {
						System.out.println("파일삭제 에러발생 : " + e);
					}
				}*/
			}
		}
		
		return result;
	}
	
	/**
	 * mult file 전체 목록 : 항목ID에 대한 HashMap
	 * @param fnIdx
	 * @param brdIdx
	 * @param items
	 * @param itemOrder
	 * @return
	 */
	public HashMap<String, Object> getMultiFileHashMap(int fnIdx, int brdIdx, JSONObject items, JSONArray itemOrder) {
		HashMap<String, Object> resultHashMap = new HashMap<String, Object>();
		if(!JSONObjectUtil.isEmpty(itemOrder) && !JSONObjectUtil.isEmpty(items)) {
			int searchOrderSize = itemOrder.size();
			for(int i = 0 ; i < searchOrderSize ; i ++) {
				String itemId = JSONObjectUtil.getString(itemOrder, i);
				JSONObject item = JSONObjectUtil.getJSONObject(items, itemId);
				int formatType = JSONObjectUtil.getInt(item, "format_type");
				int objectType = JSONObjectUtil.getInt(item, "object_type");
				if(formatType == 0 && objectType == 9) {
					// mult file
					Map<String, Object> param = new HashMap<String, Object>();
					List<DTForm> searchList = new ArrayList<DTForm>();
			    	searchList.add(new DTForm("A.CAL_IDX", brdIdx));
			    	searchList.add(new DTForm("A.ITEM_ID", itemId));
					param.put("searchList", searchList);
			    	param.put("fnIdx", fnIdx);
					resultHashMap.put(itemId, calendarFileDAO.getList(param));
				}
			}
		}
		
		return resultHashMap;
	}

	@Override
	public HashMap<String, Object> getMultiHashMap(int fnIdx, int brdIdx,
			JSONObject items, JSONArray itemOrder) {
		// TODO Auto-generated method stub
		return null;
	}
	

	
	
}