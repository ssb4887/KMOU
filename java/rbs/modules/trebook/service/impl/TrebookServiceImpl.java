package rbs.modules.trebook.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import rbs.egovframework.LoginVO;
import rbs.modules.trebook.mapper.TrebookMapper;
import rbs.modules.trebook.mapper.TrebookConMapper;
import rbs.modules.trebook.service.TrebookService;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * 이북게시판관리에 관한 구현클래스를 정의한다.
 * @author user
 *
 */
@Service("trebookService")
public class TrebookServiceImpl extends EgovAbstractServiceImpl implements TrebookService {

	@Resource(name="trebookMapper")
	private TrebookMapper trebookDAO;
	
	@Resource(name="trebookConMapper")
	private TrebookConMapper trebookConDAO;
	
	/**
	 * 전체 목록 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
    @Override
	public int getTotalCount(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	return trebookDAO.getTotalCount(param);
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
		return trebookDAO.getList(param);
	}
	
	/**
	 * Min Year
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getMinYear(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return trebookDAO.getMinYear(param);
	}
	
	/**
	 * Con 전체 목록 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
    @Override
	public int getConTotalCount(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	return trebookConDAO.getTotalCount(param);
    }

    /**
	 * Con 전체 목록
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getConList(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return trebookConDAO.getList(param);
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
    	DataMap viewDAO = trebookDAO.getFileView(param);
		return viewDAO;
	}
	
	/**
	 * Download 수정
	 * @param fnIdx
	 * @param treIdx
	 * @return
	 * @throws Exception
	 */
	public int updateDownload(int fnIdx, int treIdx) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
    	param.put("fnIdx", fnIdx);
		searchList.add(new DTForm("TRE_IDX", treIdx));

    	param.put("searchList", searchList);
		return trebookDAO.updateDownload(param);
	}
	
	/**
	 * Con 파일 상세조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getConFileView(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	DataMap viewDAO = trebookConDAO.getFileView(param);
		return viewDAO;
	}
	
	/**
	 * Download 수정
	 * @param fnIdx
	 * @param treIdx
	 * @return
	 * @throws Exception
	 */
	public int updateConDownload(int fnIdx, int treIdx, int conIdx) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
    	param.put("fnIdx", fnIdx);
		searchList.add(new DTForm("TRE_IDX", treIdx));
		searchList.add(new DTForm("CON_IDX", conIdx));

    	param.put("searchList", searchList);
		return trebookConDAO.updateDownload(param);
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
		DataMap viewDAO = trebookDAO.getView(param);
		return viewDAO;
	}
	
	/**
	 * 조회수 수정
	 * @param fnIdx
	 * @param treIdx
	 * @return
	 * @throws Exception
	 */
	public int updateViews(int fnIdx, int treIdx) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
    	param.put("fnIdx", fnIdx);
		searchList.add(new DTForm("TRE_IDX", treIdx));

    	param.put("searchList", searchList);
		return trebookDAO.updateViews(param);
	}
	
	/**
	 * 권한여부
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public int getAuthCount(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	return trebookDAO.getAuthCount(param);
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
		DataMap viewDAO = trebookDAO.getModify(param);
		return viewDAO;
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
	public int insert(String uploadModulePath, int fnIdx, String siteMode, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, JSONObject conFileItem) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	param.put("fnIdx", fnIdx);												// table flag

		// 1. key 얻기
		int treIdx = trebookDAO.getNextId(param);

		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;		
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder, "conPdfFile", conFileItem);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
    	dataList.add(new DTForm("TRE_IDX", treIdx));

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
		int result = trebookDAO.insert(param);
		
		// 4. Con 저장
		if(result > 0){
			// 4.1 Con 저장 조건
			String[] conIdxs= StringUtil.getStringArray(parameterMap.get("conIdx"));
			if(conIdxs != null && conIdxs.length > 0){
				int orderNum = 1;
				for(String conIdx:conIdxs){
					String conSubject = StringUtil.getString(parameterMap.get("conSubject"+conIdx));
					if(!StringUtil.isEmpty(conSubject)){						
						Map<String, Object> conParam = new HashMap<String, Object>();			// mapper parameter 데이터
						List<DTForm> conDataList = new ArrayList<DTForm>();						// 저장항목
						conParam.put("fnIdx", fnIdx);
						
						// 4.4 Con 저장 데이터
						conDataList.add(new DTForm("TRE_IDX", treIdx));
						conDataList.add(new DTForm("CON_IDX", Integer.parseInt(conIdx)+1));
						conDataList.add(new DTForm("CON_NUM", StringUtil.getString(parameterMap.get("conNum"+conIdx))));
						conDataList.add(new DTForm("CON_SUBJECT", conSubject));																	
						conDataList.add(new DTForm("CON_LINK_URL", StringUtil.getString(parameterMap.get("conLinkUrl"+conIdx))));						
						if(!StringUtil.isEmpty(parameterMap.get("conPdfFile" + conIdx + "_saved_name"))){
							conDataList.add(new DTForm("CON_PDF_FILE_SAVED_NAME", parameterMap.get("conPdfFile" + conIdx + "_saved_name")));
							conDataList.add(new DTForm("CON_PDF_FILE_ORIGIN_NAME", parameterMap.get("conPdfFile" + conIdx + "_origin_name")));
							conDataList.add(new DTForm("CON_PDF_FILE_SIZE", parameterMap.get("conPdfFile" + conIdx + "_size")));
							conDataList.add(new DTForm("CON_PDF_FILE_TEXT", StringUtil.getString(parameterMap.get("conPdfFile_text"+conIdx))));
						}
						conDataList.add(new DTForm("CON_CONTENTS", StringUtil.getString(parameterMap.get("conContents"+conIdx))));
						conDataList.add(new DTForm("ORDER_IDX", orderNum));
						
						conDataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
						conDataList.add(new DTForm("LAST_MODI_IP", regiIp));
						
						conParam.put("dataList", conDataList);
						
						// 4.5 Con 저장
						result += trebookConDAO.insert(conParam);
						
						orderNum++;
					}					
				}
			}
		}

		return result;
	}

	/**
	 * 수정처리 : 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param treIdx
	 * @param siteMode
	 * @param regiIp
	 * @param parameterMap
	 * @param items
	 * @param itemOrder
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int update(String uploadModulePath, int fnIdx, int treIdx, String siteMode, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, JSONObject conFileItem) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	param.put("fnIdx", fnIdx);												// table flag

    	// 1. 검색조건 setting
		searchList.add(new DTForm("TRE_IDX", treIdx));
		
		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder, "conPdfFile", conFileItem);
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
		int result = trebookDAO.update(param);
		
		// 4. Con 저장
		if(result > 0){
			// 4.1 이전 Con 삭제
			Map<String, Object> conParam = new HashMap<String, Object>();			// mapper parameter 데이터
			List<DTForm> conSearchList = new ArrayList<DTForm>();					// 검색조건
			conParam.put("fnIdx", fnIdx);											// table flag
			
	    	// 4.1 이전 Con 삭제 조건
			conSearchList.add(new DTForm("TRE_IDX", treIdx));
			conParam.put("searchList", conSearchList);
			
			result += trebookConDAO.delete(conParam);
			if(result > 0){		
				// 4.2 Con 저장 조건
				String[] conIdxs= StringUtil.getStringArray(parameterMap.get("conIdx"));							
				if(conIdxs != null &&  conIdxs.length > 0){
					int orderNum = 1;
					for(String conIdx:conIdxs){
						String conSubject = StringUtil.getString(parameterMap.get("conSubject"+conIdx));
						if(!StringUtil.isEmpty(conSubject)){
							conParam = new HashMap<String, Object>();
							List<DTForm> conDataList = new ArrayList<DTForm>();						// 저장항목
							conParam.put("fnIdx", fnIdx);
							
							// 4.3 Con 저장 데이터
							conDataList.add(new DTForm("TRE_IDX", treIdx));
							conDataList.add(new DTForm("CON_IDX", Integer.parseInt(conIdx)+1));
							conDataList.add(new DTForm("CON_NUM", StringUtil.getString(parameterMap.get("conNum"+conIdx))));
							conDataList.add(new DTForm("CON_SUBJECT", conSubject));																	
							conDataList.add(new DTForm("CON_LINK_URL", StringUtil.getString(parameterMap.get("conLinkUrl"+conIdx))));
							
							String deleteIdx = StringUtil.getString(parameterMap.get("conPdfFile_deleted_idxs"+conIdx));
							String savedFileName = StringUtil.getString(parameterMap.get("conPdfFile" + conIdx + "_saved_name"));
							if(!StringUtil.isEmpty(savedFileName)){
								conDataList.add(new DTForm("CON_PDF_FILE_SAVED_NAME", savedFileName));
								conDataList.add(new DTForm("CON_PDF_FILE_ORIGIN_NAME", parameterMap.get("conPdfFile" + conIdx + "_origin_name")));
								conDataList.add(new DTForm("CON_PDF_FILE_SIZE", parameterMap.get("conPdfFile" + conIdx + "_size")));
								conDataList.add(new DTForm("CON_PDF_FILE_TEXT", StringUtil.getString(parameterMap.get("conPdfFile_text"+conIdx))));
							}else if(StringUtil.isEmpty(savedFileName) && StringUtil.isEmpty(deleteIdx)){
								conDataList.add(new DTForm("CON_PDF_FILE_SAVED_NAME", StringUtil.getString(parameterMap.get("conPdfFile_saved"+conIdx))));
								conDataList.add(new DTForm("CON_PDF_FILE_ORIGIN_NAME", StringUtil.getString(parameterMap.get("conPdfFile_origin"+conIdx))));
								conDataList.add(new DTForm("CON_PDF_FILE_SIZE", StringUtil.getString(parameterMap.get("conPdfFile_size"+conIdx))));
								conDataList.add(new DTForm("CON_PDF_FILE_TEXT", StringUtil.getString(parameterMap.get("conPdfFile_text"+conIdx))));
							}
							conDataList.add(new DTForm("CON_CONTENTS", StringUtil.getString(parameterMap.get("conContents"+conIdx))));
							conDataList.add(new DTForm("ORDER_IDX", orderNum));
							
							conDataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
							conDataList.add(new DTForm("LAST_MODI_IP", regiIp));
							
							conParam.put("dataList", conDataList);
							
							// 4.4 Con 저장
							result += trebookConDAO.insert(conParam);
							
							orderNum++;
						}						
					}
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
    	return trebookDAO.getDeleteCount(param);
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
		return trebookDAO.getDeleteList(param);
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
		searchList.add(new DTForm("TRE_IDX", deleteIdxs));

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
		return trebookDAO.delete(param);
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
		searchList.add(new DTForm("TRE_IDX", restoreIdxs));

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
		return trebookDAO.restore(param);
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
		searchList.add(new DTForm("TRE_IDX", deleteIdxs));
		param.put("searchList", searchList);
				
		// 3. delete
		int result = trebookDAO.cdelete(param);
		
		return result;
	}
}