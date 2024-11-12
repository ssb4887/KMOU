package rbs.modules.majorInfo.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.ClientUtil;
import com.woowonsoft.egovframework.util.FileUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rbs.egovframework.LoginVO;
import rbs.egovframework.util.LogHelper;
import rbs.modules.majorInfo.mapper.MajorInfoFileMapper;
import rbs.modules.majorInfo.mapper.MajorInfoMapper;
import rbs.modules.majorInfo.mapper.MajorInfoMultiMapper;
import rbs.modules.majorInfo.mapper.MajorInfoOraMapper;
import rbs.modules.majorInfo.service.MajorInfoService;

/**
 * 샘플모듈에 관한 구현클래스를 정의한다.
 * @author user
 *
 */
@Service("majorInfoService")
public class MajorInfoServiceImpl extends EgovAbstractServiceImpl implements MajorInfoService {

	@Resource(name="majorInfoMapper")
	private MajorInfoMapper majorInfoDAO;
	
	@Resource(name="majorInfoOraMapper")
	private MajorInfoOraMapper majorInfoOraDAO;
	
	@Resource(name="majorInfoFileMapper")
	private MajorInfoFileMapper majorInfoFileDAO;
	
	@Resource(name="majorInfoMultiMapper")
	private MajorInfoMultiMapper majorInfoMultiDAO;
	
	/**
	 * 전체 목록 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
    @Override
	public int getTotalCount(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	return majorInfoDAO.getTotalCount(param);
    }
    
    /**
	 * 전체 목록 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
    @Override
	public int getDeptCount(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	return majorInfoDAO.getDeptCount(param);
    }
    
    /**
	 * 주관대학 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getCollegeList() {
		return majorInfoDAO.getCollegeList();
	}
	
	/**
	 * 주관 대학 - 학부(과) 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDepartList(Map<String, Object> param) {
		return majorInfoDAO.getDepartList(param);
	}
	
	/**
	 * 주관 대학 - 학부(과) - 전공 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getMajorList(Map<String, Object> param) {
		return majorInfoDAO.getMajorList(param);
	}
    
    /**
     * 트랙 목록 수
     * @param fnIdx
     * @param param
     * @return
     */
    @Override
    public int getTrackCount(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	
    	return majorInfoDAO.getTrackCount(param);
    }
    
    public int getAbilityCount(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	
    	return majorInfoDAO.getAbilityCount(param);
    }
    public int getMajorCount(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	
    	return majorInfoDAO.getMajorCount(param);
    }
    public int getMajorEtcCount(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	
    	return majorInfoDAO.getMajorEtcCount(param);
    }
    public int getMajorYearCount(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	
    	return majorInfoDAO.getMajorYearCount(param);
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
		return majorInfoDAO.getList(param);
	}
	
	 /**
	 * 전체 목록
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDeptList(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return majorInfoDAO.getDeptList(param);
	}
	
	public List<Object> getTalentAbtyList(Map<String, Object> param) {
		return majorInfoDAO.getTalentAbtyList(param);
	}
	public List<Object> getAbtyMngList(Map<String, Object> param) {
		return majorInfoDAO.getAbtyMngList(param);
	}
	public List<Object> getFieldList(Map<String, Object> param) {
		return majorInfoDAO.getFieldList(param);
	}
	public List<Object> getNonSbjtList(Map<String, Object> param) {
		return majorInfoDAO.getNonSbjtList(param);
	}
	public List<Object> getLicenseList(Map<String, Object> param) {
		return majorInfoDAO.getLicenseList(param);
	}
	
	
	public List<Object> getTrackList(int fnIdx, Map<String, Object> param) {
		param.put("fnIdx", fnIdx);
		return majorInfoDAO.getTrackList(param);
	}
	public List<Object> getAbilityList(int fnIdx, Map<String, Object> param) {
		param.put("fnIdx", fnIdx);
		return majorInfoDAO.getAbilityList(param);
	}
	public List<Object> getDtlList(int fnIdx, Map<String, Object> param) {
		param.put("fnIdx", fnIdx);
		return majorInfoDAO.getDtlList(param);
	}
	public List<Object> getCourMajorList(int fnIdx, Map<String, Object> param) {
		param.put("fnIdx", fnIdx);
		return majorInfoDAO.getCourMajorList(param);
	}
	public List<Object> getAddMajorList(int fnIdx, Map<String, Object> param) {
		param.put("fnIdx", fnIdx);
		return majorInfoOraDAO.getAddMajorList(param);
	}
	public List<Object> getRcmdCultList(int fnIdx, Map<String, Object> param) {
		param.put("fnIdx", fnIdx);
		return majorInfoDAO.getRcmdCultList(param);
	}
	public List<Object> getHaksaRcmdCultList(int fnIdx, Map<String, Object> param) {
		param.put("fnIdx", fnIdx);
		return majorInfoDAO.getHaksaRcmdCultList(param);
	}
	
	
	
	public List<Object> insertAddMajorList(int fnIdx, Map<String, Object> param) {
		param.put("fnIdx", fnIdx);
		return majorInfoDAO.insertAddMajorList(param);
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
		DataMap viewDAO = majorInfoDAO.getView(param);
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
		DataMap viewDAO = majorInfoDAO.getFileView(param);
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
		DataMap viewDAO = majorInfoFileDAO.getFileView(param);
		return viewDAO;
	}
	
	/**
	 * 다운로드 수 수정
	 * @param fnIdx
	 * @param brdIdx
	 * @return
	 * @throws Exception
	 */
	public int updateFileDown(int fnIdx, int keyMajorIdx, int keyYear, String fileColumnName) throws Exception{
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
    	param.put("fnIdx", fnIdx);
		param.put("MAJOR_IDX", keyMajorIdx);
		param.put("YEAR", keyYear);
    	param.put("searchList", searchList);
    	param.put("FILE_COLUMN", fileColumnName);
		return majorInfoDAO.updateFileDown(param);
		
	}
	
	/**
	 * multi file 다운로드 수 수정
	 * @param fnIdx
	 * @param brdIdx
	 * @return
	 * @throws Exception
	 */
	public int updateMultiFileDown(int fnIdx, int keyIdx, int fleIdx, String itemId) throws Exception{
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("FLE_IDX", fleIdx));
		searchList.add(new DTForm("ITEM_ID", itemId));
    	param.put("searchList", searchList);
    	param.put("fnIdx", fnIdx);
		param.put("IDX", keyIdx);
		return majorInfoFileDAO.updateFileDown(param);
		
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
		DataMap viewDAO = majorInfoDAO.getModify(param);
		return viewDAO;
	}
	
	@Override
	public DataMap getDeptModify(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		DataMap viewDAO = majorInfoDAO.getDeptModify(param);
		return viewDAO;
	}
	
	@Override
	public DataMap getModifyCourMajor(int fnIdx, Map<String, Object> param) {
		param.put("fnIdx", fnIdx);
		DataMap viewDAO = majorInfoDAO.getModifyCourMajor(param);
		return viewDAO;
	}
	
	@Override
	public DataMap getModifyAbility(int fnIdx, Map<String, Object> param) {
		param.put("fnIdx", fnIdx);
		DataMap viewDAO = majorInfoDAO.getModifyAbility(param);
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
    	return majorInfoDAO.getAuthCount(param);
    }

	/**
	 * 등록처리 : 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param settingInfo
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int insert(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		Map<String, Object> findParam = new HashMap<String, Object>();					// mapper parameter 데이터
		Map<String, Object> param = new HashMap<String, Object>();					// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목

		String columnName = JSONObjectUtil.getString(settingInfo, "idx_column");	// key 컬럼
				
		// 1. key 얻기
		param.put("fnIdx", fnIdx);
		int keyIdx = majorInfoDAO.getNextMajorId(param);
		if(keyIdx > 0) {
			dataList.add(new DTForm("ORD", keyIdx));
		}
		

		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		System.out.println("dataMap : " + dataMap);
		System.out.println("dataList : " + dataMap.get("dataList"));
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		findParam.put("searchList", dataList);
//		findParam.put("majorCd", parameterMap.get("connMjMngtHgCd"));
		
		// 값이 있는지 확인
//		int majorIdx = majorInfoDAO.getMajorIdx(findParam);
//		if(majorIdx > 0) {
//			System.out.println("소속 값이 이미 존재합니다. 소속 MAJOR_IDX 값 : " + majorIdx);
//			return majorIdx;
//		}
//		
//		dataList.add(new DTForm(columnName, keyIdx));

		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
    	
		dataList.add(new DTForm("REGI_ID", loginMemberId));
		dataList.add(new DTForm("REGI_IP", regiIp));
		
		dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
		dataList.add(new DTForm("LAST_MODI_IP", regiIp));
		
		/*dataList.add(new DTForm("MJ_NM_KOR", parameterMap.get("mjNmKor") ));
		dataList.add(new DTForm("MJ_NM_ENG", parameterMap.get("mjNmEng") ));*/
    	
		System.out.println("dataList : " + dataMap.get("dataList"));
		param.put("dataList", dataList);
		
		System.out.println("param : " + param);

		// 3. DB 저장
		// 3.1 기본 정보 테이블
		
		int result = majorInfoDAO.insertMajor(param);
		
		if(result > 0) {
//			// 3.2 multi data 저장
//			List multiDataList = StringUtil.getList(dataMap.get("multiDataList"));
//			if(multiDataList != null) {
//				int multiDataSize = multiDataList.size();
//				for(int i = 0 ; i < multiDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)multiDataList.get(i);
//					fileParam.put("fnIdx", fnIdx);
//					fileParam.put("IDX", keyIdx);
//					result = majorInfoMultiDAO.insert(fileParam);
//				}
//			}
//			
//			// 3.3 multi file 저장
//			List fileDataList = StringUtil.getList(dataMap.get("fileDataList"));
//			if(fileDataList != null) {
//				int fileDataSize = fileDataList.size();
//				for(int i = 0 ; i < fileDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)fileDataList.get(i);
//					fileParam.put("fnIdx", fnIdx);
//					fileParam.put("IDX", keyIdx);
//					int fleIdx = majorInfoFileDAO.getNextId(fileParam);
//					fileParam.put("FLE_IDX", fleIdx);
//					result = majorInfoFileDAO.insert(fileParam);
//				}
//			}
		}
		
		return result;
//		return 1;
	}
	
	/**
	 * 등록 처리 : 신설된 학부/학과 등록
	 * @return
	 * @throws Exception
	 */
	@Override
	public int insertDept(Map<String, Object> param) throws Exception  {
		
		// DB 저장
		int result = majorInfoDAO.insertDept(param);
		
		return result;
	}
	
	
	/**
	 * 등록처리 : 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param settingInfo
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int insertYear(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		Map<String, Object> param = new HashMap<String, Object>();					// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목
		List<DTForm> searchList = new ArrayList<DTForm>();							// 검색조건

		String columnName = JSONObjectUtil.getString(settingInfo, "idx_year_column");	// key 컬럼
		String majorColumnName = JSONObjectUtil.getString(settingInfo, "idx_major_column");	// major key 컬럼
				
		// 1. key 얻기
		param.put("fnIdx", fnIdx);
		searchList.add(new DTForm(majorColumnName, majorIdx));
		
		param.put("searchList", searchList);
		int keyIdx = majorInfoDAO.getNextYearId(param);

		// 2. 항목설정으로 저장항목 setting
		
//		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + "major/1";
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		System.out.println("dataMap : " + dataMap);
		System.out.println("dataList : " + dataMap.get("dataList"));
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// yearIdx 추가
		dataList.add(new DTForm(columnName, keyIdx));

		// majorIdx 추가
		dataList.add(new DTForm("MAJOR_IDX", majorIdx));
		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
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
    	
		System.out.println("dataList : " + dataMap.get("dataList"));
		param.put("dataList", dataList);
		
		System.out.println("param : " + param);
		// 나누고 싶음

		// 3. DB 저장
		// 3.1 기본 정보 테이블
		int result = majorInfoDAO.insertMajorYear(param);
		
		if(result > 0) {
//			// 3.2 multi data 저장
//			List multiDataList = StringUtil.getList(dataMap.get("multiDataList"));
//			
//			System.out.println( "multiDataList" );
//			System.out.println( multiDataList );
//			if(multiDataList != null) {
//				int multiDataSize = multiDataList.size();
//				for(int i = 0 ; i < multiDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)multiDataList.get(i);
//					fileParam.put("fnIdx", fnIdx);
//					fileParam.put("IDX", keyIdx);
//					result = majorInfoMultiDAO.insert(fileParam);
//				}
//			}
//			
//			// 3.3 multi file 저장
//			List fileDataList = StringUtil.getList(dataMap.get("fileDataList"));
//			System.out.println( "fileDataList" );
//			System.out.println( fileDataList );
//			if(fileDataList != null) {
//				int fileDataSize = fileDataList.size();
//				for(int i = 0 ; i < fileDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)fileDataList.get(i);
//					fileParam.put("fnIdx", fnIdx);
//					fileParam.put("IDX", keyIdx);
//					int fleIdx = majorInfoFileDAO.getNextId(fileParam);
//					fileParam.put("FLE_IDX", fleIdx);
//					result = majorInfoFileDAO.insert(fileParam);
//				}
//			}
		}
		
		return result > 0 ? keyIdx : result;
	}
	
	public int copyYear(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx, int yearIdx) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		Map<String, Object> param = new HashMap<String, Object>();					// mapper parameter 데이터
		Map<String, Object> copyParam = new HashMap<String, Object>();					// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목
		List<DTForm> searchList = new ArrayList<DTForm>();							// 검색조건
		List<DTForm> searchCList = new ArrayList<DTForm>();							// 검색조건
		
		String columnName = JSONObjectUtil.getString(settingInfo, "idx_year_column");	// key 컬럼
		String majorColumnName = JSONObjectUtil.getString(settingInfo, "idx_major_column");	// major key 컬럼
		
		// 1. key 얻기
		param.put("fnIdx", fnIdx);
		searchList.add(new DTForm(majorColumnName, majorIdx));
		searchCList.add(new DTForm(majorColumnName, majorIdx));

		searchList.add(new DTForm(columnName, yearIdx));
		param.put("searchList", searchList);
		copyParam.put("searchList", searchCList);
		
		int copyYear = majorInfoDAO.getCopyYear(copyParam); // ###### 추가할 연도
		int keyIdx = majorInfoDAO.getNextYearId(copyParam); // ###### 이거 그대로 쓰면 됨
		
		System.out.println("copyYear : "+ copyYear);
		System.out.println("keyIdx : "+ keyIdx);
		
		
		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		System.out.println("dataMap : " + dataMap);
		System.out.println("dataList : " + dataMap.get("dataList"));
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// yearIdx 추가
		
		
		// majorIdx 추가
		dataList.add(new DTForm("MAJOR_IDX", majorIdx));
		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
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
		
//		dataList.add(new DTForm(columnName, keyIdx));
//		dataList.add(new DTForm("YEAR", copyYear));
		
		System.out.println("dataList : " + dataMap.get("dataList"));
		param.put("dataList", dataList);
		param.put("copyYear", copyYear);
		param.put("keyIdx", keyIdx);
		
		System.out.println("param : " + param);
		// 나누고 싶음
		
		// 3. DB 저장
		// 3.1 기본 정보 테이블
//		int result = majorInfoDAO.insertMajorYear(param);
		int result = majorInfoDAO.copyMajorYear(param);
		if(result > 0) {
			// 3.2 multi data 저장
//			List multiDataList = StringUtil.getList(dataMap.get("multiDataList"));
//			if(multiDataList != null) {
//				int multiDataSize = multiDataList.size();
//				for(int i = 0 ; i < multiDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)multiDataList.get(i);
//					fileParam.put("fnIdx", fnIdx);
//					fileParam.put("IDX", keyIdx);
//					result = majorInfoMultiDAO.insert(fileParam);
//				}
//			}
//			
//			// 3.3 multi file 저장
//			List fileDataList = StringUtil.getList(dataMap.get("fileDataList"));
//			if(fileDataList != null) {
//				int fileDataSize = fileDataList.size();
//				for(int i = 0 ; i < fileDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)fileDataList.get(i);
//					fileParam.put("fnIdx", fnIdx);
//					fileParam.put("IDX", keyIdx);
//					int fleIdx = majorInfoFileDAO.getNextId(fileParam);
//					fileParam.put("FLE_IDX", fleIdx);
//					result = majorInfoFileDAO.insert(fileParam);
//				}
//			}
		}
		
		return result > 0 ? keyIdx : result;
	}
	
	public int copyStatistic(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx, int yearIdx, int copyYearIdx) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		Map<String, Object> param = new HashMap<String, Object>();					// mapper parameter 데이터
		Map<String, Object> copyParam = new HashMap<String, Object>();					// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목
		List<DTForm> searchList = new ArrayList<DTForm>();							// 검색조건
		List<DTForm> searchCList = new ArrayList<DTForm>();							// 검색조건
		
		String columnName = JSONObjectUtil.getString(settingInfo, "idx_year_column");	// key 컬럼
		String majorColumnName = JSONObjectUtil.getString(settingInfo, "idx_major_column");	// major key 컬럼
		
		// 1. key 얻기
		param.put("fnIdx", fnIdx);
		searchList.add(new DTForm(majorColumnName, majorIdx));
		searchCList.add(new DTForm(majorColumnName, majorIdx));
		
		searchList.add(new DTForm(columnName, yearIdx));
		param.put("searchList", searchList);
		copyParam.put("searchList", searchCList);
		
		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		System.out.println("dataMap : " + dataMap);
		System.out.println("dataList : " + dataMap.get("dataList"));
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// yearIdx 추가
		
		
		// majorIdx 추가
		dataList.add(new DTForm("MAJOR_IDX", majorIdx));
		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
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
		
//		dataList.add(new DTForm(columnName, keyIdx));
//		dataList.add(new DTForm("YEAR", copyYear));
		
		System.out.println("dataList : " + dataMap.get("dataList"));
		param.put("dataList", dataList);
		param.put("keyIdx", copyYearIdx);
		
		System.out.println("param : " + param);
		// 나누고 싶음
		
		// 3. DB 저장
		// 3.1 기본 정보 테이블
//		int result = majorInfoDAO.insertMajorYear(param);
		int result = majorInfoDAO.copyMajorStatistic(param);
		if(result > 0) {
		}
		
		return result > 0 ? 1 : result;
	}
	
	public int copyTrack(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx, int yearIdx, int copyYearIdx) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		Map<String, Object> param = new HashMap<String, Object>();					// mapper parameter 데이터
		Map<String, Object> copyParam = new HashMap<String, Object>();					// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목
		List<DTForm> searchList = new ArrayList<DTForm>();							// 검색조건
		List<DTForm> searchCList = new ArrayList<DTForm>();							// 검색조건
		
		String columnName = JSONObjectUtil.getString(settingInfo, "idx_year_column");	// key 컬럼
		String majorColumnName = JSONObjectUtil.getString(settingInfo, "idx_major_column");	// major key 컬럼
		
		// 1. key 얻기
		param.put("fnIdx", fnIdx);
		searchList.add(new DTForm(majorColumnName, majorIdx));
		searchCList.add(new DTForm(majorColumnName, majorIdx));
		
		searchList.add(new DTForm(columnName, yearIdx));
		param.put("searchList", searchList);
		copyParam.put("searchList", searchCList);
		
		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		System.out.println("dataMap : " + dataMap);
		System.out.println("dataList : " + dataMap.get("dataList"));
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// yearIdx 추가
		
		
		// majorIdx 추가
		dataList.add(new DTForm("MAJOR_IDX", majorIdx));
		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
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
		
//		dataList.add(new DTForm(columnName, keyIdx));
//		dataList.add(new DTForm("YEAR", copyYear));
		
		System.out.println("dataList : " + dataMap.get("dataList"));
		param.put("dataList", dataList);
		param.put("keyIdx", copyYearIdx);
		
		System.out.println("param : " + param);
		// 나누고 싶음
		
		// 3. DB 저장
		// 3.1 기본 정보 테이블
//		int result = majorInfoDAO.insertMajorYear(param);
		int result = majorInfoDAO.copyMajorTrack(param);
		if(result > 0) {
		}
		
		return result > 0 ? 1 : result;
	}
	
	public int copyJob(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx, int yearIdx, int copyYearIdx) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		Map<String, Object> param = new HashMap<String, Object>();					// mapper parameter 데이터
		Map<String, Object> copyParam = new HashMap<String, Object>();					// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목
		List<DTForm> searchList = new ArrayList<DTForm>();							// 검색조건
		List<DTForm> searchCList = new ArrayList<DTForm>();							// 검색조건
		
		String columnName = JSONObjectUtil.getString(settingInfo, "idx_year_column");	// key 컬럼
		String majorColumnName = JSONObjectUtil.getString(settingInfo, "idx_major_column");	// major key 컬럼
		
		// 1. key 얻기
		param.put("fnIdx", fnIdx);
		searchList.add(new DTForm(majorColumnName, majorIdx));
		searchCList.add(new DTForm(majorColumnName, majorIdx));
		
		searchList.add(new DTForm(columnName, yearIdx));
		param.put("searchList", searchList);
		copyParam.put("searchList", searchCList);
		
		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		System.out.println("dataMap : " + dataMap);
		System.out.println("dataList : " + dataMap.get("dataList"));
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// yearIdx 추가
		
		
		// majorIdx 추가
		dataList.add(new DTForm("MAJOR_IDX", majorIdx));
		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
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
		
//		dataList.add(new DTForm(columnName, keyIdx));
//		dataList.add(new DTForm("YEAR", copyYear));
		
		System.out.println("dataList : " + dataMap.get("dataList"));
		param.put("dataList", dataList);
		param.put("keyIdx", copyYearIdx);
		
		System.out.println("param : " + param);
		// 나누고 싶음
		
		// 3. DB 저장
		// 3.1 기본 정보 테이블
//		int result = majorInfoDAO.insertMajorYear(param);
		int result = majorInfoDAO.copyMajorJob(param);
		if(result > 0) {
		}
		
		return result > 0 ? 1 : result;
	}
	
	public int copyDtl(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx, int yearIdx, int copyYearIdx) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		Map<String, Object> param = new HashMap<String, Object>();					// mapper parameter 데이터
		Map<String, Object> copyParam = new HashMap<String, Object>();					// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목
		List<DTForm> searchList = new ArrayList<DTForm>();							// 검색조건
		List<DTForm> searchCList = new ArrayList<DTForm>();							// 검색조건
		
		String columnName = JSONObjectUtil.getString(settingInfo, "idx_year_column");	// key 컬럼
		String majorColumnName = JSONObjectUtil.getString(settingInfo, "idx_major_column");	// major key 컬럼
		
		// 1. key 얻기
		param.put("fnIdx", fnIdx);
		searchList.add(new DTForm(majorColumnName, majorIdx));
		searchCList.add(new DTForm(majorColumnName, majorIdx));
		
		searchList.add(new DTForm(columnName, yearIdx));
		param.put("searchList", searchList);
		copyParam.put("searchList", searchCList);
		
		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		System.out.println("dataMap : " + dataMap);
		System.out.println("dataList : " + dataMap.get("dataList"));
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// yearIdx 추가
		
		
		// majorIdx 추가
		dataList.add(new DTForm("MAJOR_IDX", majorIdx));
		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
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
		
//		dataList.add(new DTForm(columnName, keyIdx));
//		dataList.add(new DTForm("YEAR", copyYear));
		
		System.out.println("dataList : " + dataMap.get("dataList"));
		param.put("dataList", dataList);
		param.put("keyIdx", copyYearIdx);
		
		System.out.println("param : " + param);
		// 나누고 싶음
		
		// 3. DB 저장
		// 3.1 기본 정보 테이블
//		int result = majorInfoDAO.insertMajorYear(param);
		int result = majorInfoDAO.copyMajorDtl(param);
		if(result > 0) {
		}
		
		return result > 0 ? 1 : result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int insertStatistic(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx, int yearIdx) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		Map<String, Object> param = new HashMap<String, Object>();					// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목
		
		String columnName = JSONObjectUtil.getString(settingInfo, "idx_statistic_column");	// key 컬럼
		
		// 1. key 얻기
		param.put("fnIdx", fnIdx);
//		int keyIdx = majorInfoDAO.getNextStatisticId(param);
		int keyIdx = 1;
		
		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		System.out.println("dataMap : " + dataMap);
		System.out.println("dataList : " + dataMap.get("dataList"));
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// statisticIdx 추가
		dataList.add(new DTForm(columnName, keyIdx));
		
		// majorIdx 추가
		dataList.add(new DTForm("MAJOR_IDX", majorIdx));
		dataList.add(new DTForm("YEAR_IDX", yearIdx));
		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
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
		
		System.out.println("dataList : " + dataMap.get("dataList"));
		param.put("dataList", dataList);
		
		System.out.println("param : " + param);
		// 나누고 싶음
		
		// 3. DB 저장
		// 3.1 기본 정보 테이블
		int result = majorInfoDAO.insertMajorStatistic(param);
		
		if(result > 0) {
			// 3.2 multi data 저장
//			List multiDataList = StringUtil.getList(dataMap.get("multiDataList"));
//			if(multiDataList != null) {
//				int multiDataSize = multiDataList.size();
//				for(int i = 0 ; i < multiDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)multiDataList.get(i);
//					fileParam.put("fnIdx", fnIdx);
//					fileParam.put("IDX", keyIdx);
//					result = majorInfoMultiDAO.insert(fileParam);
//				}
//			}
//			
//			// 3.3 multi file 저장
//			List fileDataList = StringUtil.getList(dataMap.get("fileDataList"));
//			if(fileDataList != null) {
//				int fileDataSize = fileDataList.size();
//				for(int i = 0 ; i < fileDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)fileDataList.get(i);
//					fileParam.put("fnIdx", fnIdx);
//					fileParam.put("IDX", keyIdx);
//					int fleIdx = majorInfoFileDAO.getNextId(fileParam);
//					fileParam.put("FLE_IDX", fleIdx);
//					result = majorInfoFileDAO.insert(fileParam);
//				}
//			}
		}
		
		return result > 0 ? keyIdx : result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int insertTrack(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx, int yearIdx) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		Map<String, Object> param = new HashMap<String, Object>();					// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목
		
		String columnName = JSONObjectUtil.getString(settingInfo, "idx_track_column");	// key 컬럼
		
		// 1. key 얻기
		param.put("fnIdx", fnIdx);
		param.put("majorIdx", majorIdx);
		param.put("yearIdx", yearIdx);
		
		int keyIdx = majorInfoDAO.getNextTrackId(param);
		
		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		System.out.println("dataMap : " + dataMap);
		System.out.println("dataList : " + dataMap.get("dataList"));
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// statisticIdx 추가
		dataList.add(new DTForm(columnName, keyIdx));
		
		// majorIdx 추가
//		dataList.add(new DTForm("MAJOR_IDX", majorIdx));
//		dataList.add(new DTForm("YEAR_IDX", yearIdx));
		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
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
		
		System.out.println("dataList : " + dataMap.get("dataList"));
		param.put("dataList", dataList);
		
		System.out.println("param : " + param);
		// 나누고 싶음
		
		// 3. DB 저장
		// 3.1 기본 정보 테이블
		int result = majorInfoDAO.insertMajorTrack(param);
		
		if(result > 0) {
			// 3.2 multi data 저장
//			List multiDataList = StringUtil.getList(dataMap.get("multiDataList"));
//			if(multiDataList != null) {
//				int multiDataSize = multiDataList.size();
//				for(int i = 0 ; i < multiDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)multiDataList.get(i);
//					fileParam.put("fnIdx", fnIdx);
//					fileParam.put("IDX", keyIdx);
//					result = majorInfoMultiDAO.insert(fileParam);
//				}
//			}
//			
//			// 3.3 multi file 저장
//			List fileDataList = StringUtil.getList(dataMap.get("fileDataList"));
//			if(fileDataList != null) {
//				int fileDataSize = fileDataList.size();
//				for(int i = 0 ; i < fileDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)fileDataList.get(i);
//					fileParam.put("fnIdx", fnIdx);
//					fileParam.put("IDX", keyIdx);
//					int fleIdx = majorInfoFileDAO.getNextId(fileParam);
//					fileParam.put("FLE_IDX", fleIdx);
//					result = majorInfoFileDAO.insert(fileParam);
//				}
//			}
		}
		
		return result > 0 ? keyIdx : result;
	}
	
	// 고정으로 4개
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int insertJob(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx, int yearIdx) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		Map<String, Object> param = new HashMap<String, Object>();					// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목
		
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
		
		
		int result = 0;
		// 반복문 4번
		for(int i=1; i<7; i++) {
			
			param = new HashMap<String, Object>();
			
			dataList = new ArrayList<DTForm>();
			dataList.add(new DTForm("JOB_IDX", i));
			dataList.add(new DTForm("MAJOR_IDX", majorIdx));
			dataList.add(new DTForm("YEAR_IDX", yearIdx));
			dataList.add(new DTForm("REGI_IDX", loginMemberIdx));
			dataList.add(new DTForm("REGI_ID", loginMemberId));
			dataList.add(new DTForm("REGI_NAME", loginMemberName));
			dataList.add(new DTForm("REGI_IP", regiIp));
			
			dataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
			dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
			dataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
			dataList.add(new DTForm("LAST_MODI_IP", regiIp));
			
			String title = "";
			
			switch(i) {
				case 1: title = "주요 진출 현황 : 워크넷";
					break;
				case 2: title = "주요 진출 현황 : 기업 및 직무";
					break;
				case 3: title = "주요 진출 현황 : NCS";
					break;
				case 4: title = "취업성공수기 (1)";
					break;
				case 5: title = "취업성공수기 (2)";
					break;
				case 6: title = "취업성공수기 (3)";
					break;
				default: title = "";
					break;
			}
			
			dataList.add(new DTForm("TITLE", title));
			param.put("dataList", dataList);
			System.out.println("param : " + param);
			result = majorInfoDAO.insertMajorJob(param);
			
			if(result <= 0) return result;
		}
		
		
		// 3. DB 저장
		// 3.1 기본 정보 테이블
		
		if(result > 0) {
		}
		
		return result > 0 ? 1 : result;
	}
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	@Override
//	public int insertJob(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx, int yearIdx) throws Exception  {
//		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
//		
//		Map<String, Object> param = new HashMap<String, Object>();					// mapper parameter 데이터
//		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목
//		
//		String columnName = JSONObjectUtil.getString(settingInfo, "idx_job_column");	// key 컬럼
//		
//		// 1. key 얻기
//		param.put("fnIdx", fnIdx);
//		int keyIdx = majorInfoDAO.getNextJobId(param);
//		
//		// 2. 항목설정으로 저장항목 setting
//		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
//		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
//		if(dataMap == null || dataMap.size() == 0) return -1;
//		
//		// 2.1 저장항목
//		System.out.println("dataMap : " + dataMap);
//		System.out.println("dataList : " + dataMap.get("dataList"));
//		
//		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
//		if(itemDataList != null) dataList.addAll(itemDataList);
//		
//		// statisticIdx 추가
//		dataList.add(new DTForm(columnName, keyIdx));
//		
//		// majorIdx 추가
////		dataList.add(new DTForm("MAJOR_IDX", majorIdx));
////		dataList.add(new DTForm("YEAR_IDX", yearIdx));
//		// 2.2 등록자 정보
//		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
//		String loginMemberIdx = null;
//		String loginMemberId = null;
//		String loginMemberName = null;
//		if(loginVO != null) {
//			loginMemberIdx = loginVO.getMemberIdx();
//			loginMemberId = loginVO.getMemberId();
//			loginMemberName = loginVO.getMemberName();
//		}
//		
//		
//		dataList.add(new DTForm("REGI_IDX", loginMemberIdx));
//		dataList.add(new DTForm("REGI_ID", loginMemberId));
//		dataList.add(new DTForm("REGI_NAME", loginMemberName));
//		dataList.add(new DTForm("REGI_IP", regiIp));
//		
//		dataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
//		dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
//		dataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
//		dataList.add(new DTForm("LAST_MODI_IP", regiIp));
//		
//		System.out.println("dataList : " + dataMap.get("dataList"));
//		param.put("dataList", dataList);
//		
//		System.out.println("param : " + param);
//		// 나누고 싶음
//		
//		// 3. DB 저장
//		// 3.1 기본 정보 테이블
//		int result = majorInfoDAO.insertMajorJob(param);
//		
//		if(result > 0) {
//		}
//		
//		return result > 0 ? keyIdx : result;
//	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int insertDtl(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx, int yearIdx, int jobIdx) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		Map<String, Object> param = new HashMap<String, Object>();					// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목
		List<DTForm> searchList = new ArrayList<DTForm>();							// 저장항목
		
		String columnName = JSONObjectUtil.getString(settingInfo, "idx_dtl_column");	// key 컬럼
		
		// 1. key 얻기
		param.put("fnIdx", fnIdx);
		
		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		
		// 2.1 저장항목
		System.out.println("dataMap : " + dataMap);
		System.out.println("dataList : " + dataMap.get("dataList"));
		
//		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
//		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
		
		
		//
		searchList.add(new DTForm("MAJOR_IDX", majorIdx));
		searchList.add(new DTForm("YEAR_IDX", yearIdx));
		searchList.add(new DTForm("JOB_IDX", jobIdx));
		param.put("searchList", searchList);
		
		int result = 0;
		
		String dtlIdxArr[] = StringUtil.getStringArray(parameterMap.get("dtlIdx"));
		if(dtlIdxArr != null ){
			for (int i = 0; i < dtlIdxArr.length; i++) {
				
				int keyIdx = majorInfoDAO.getNextDtlId(param);
				dataList = new ArrayList<DTForm>();
				dataList.add(new DTForm(columnName, keyIdx));
				dataList.add(new DTForm("TITLE", parameterMap.get("dtlTitle"+dtlIdxArr[i]) ));
				dataList.add(new DTForm("CONTENTS", parameterMap.get("dtlContents"+dtlIdxArr[i]) ));
				dataList.add(new DTForm("ORDER_IDX", dtlIdxArr[i]));
				
				// majorIdx 추가
				dataList.add(new DTForm("MAJOR_IDX", majorIdx));
				dataList.add(new DTForm("YEAR_IDX", yearIdx));
				dataList.add(new DTForm("JOB_IDX", jobIdx));
				
				dataList.add(new DTForm("REGI_IDX", loginMemberIdx));
				dataList.add(new DTForm("REGI_ID", loginMemberId));
				dataList.add(new DTForm("REGI_NAME", loginMemberName));
				dataList.add(new DTForm("REGI_IP", regiIp));
				
				dataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
				dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
				dataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
				dataList.add(new DTForm("LAST_MODI_IP", regiIp));
				
				System.out.println("dataList : " + dataMap.get("dataList"));
				param.put("dataList", dataList);
				
				System.out.println("param : " + param);
				// 나누고 싶음
				
				// 3. DB 저장
				// 3.1 기본 정보 테이블
				result = majorInfoDAO.insertMajorJobDtl(param);
			}
		}else result = 1;
		
		
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int deleteDtl(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx, int yearIdx, int jobIdx) throws Exception  {
//		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		Map<String, Object> param = new HashMap<String, Object>();					// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목
		
		// majorIdx 추가
		dataList.add(new DTForm("MAJOR_IDX", majorIdx));
		dataList.add(new DTForm("YEAR_IDX", yearIdx));
		dataList.add(new DTForm("JOB_IDX", jobIdx));
		
		
		param.put("searchList", dataList);
		
		System.out.println("param : " + param);
		// 나누고 싶음
		
		// 3. DB 저장
		// 3.1 기본 정보 테이블
		int result = majorInfoDAO.deleteMajorJobDtl(param);
		
		if(result > 0) {
		}
		
		return result > 0 ? 1 : result;
	}

	/**
	 * 수정처리 : 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param keyIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param settingInfo
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int update(String uploadModulePath, int fnIdx, Map<String,Object> param, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1; 
			
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목
    	
		// 1. 검색조건 setting

		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);

		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		String resprCfmYn = (String) parameterMap.get("resprCfmYn");
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
		
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));
    	dataList.add(new DTForm("RESPR_CFM_YN", resprCfmYn));

    	
    	param.put("dataList", dataList);
    	param.put("fnIdx", fnIdx);

    	// 3. DB 저장
    	// 3.1 기본 정보 테이블
		int result = majorInfoDAO.updateMajor(param);
		

		if(result > 0){
			// 3.2 multi data 저장
			// 3.2.1 multi data 삭제
//			int result1 = 0;
//			Map<String, Object> multiDelParam = new HashMap<String, Object>();
//			multiDelParam.put("fnIdx", fnIdx);
//			multiDelParam.put("IDX", keyIdx);
//			result1 = majorInfoMultiDAO.cdelete(multiDelParam);
//
//			// 3.2.2 multi data 등록
//			List multiDataList = StringUtil.getList(dataMap.get("multiDataList"));
//			if(multiDataList != null) {
//				int multiDataSize = multiDataList.size();
//				for(int i = 0 ; i < multiDataSize ; i ++) {
//					Map<String, Object> multiParam = (HashMap)multiDataList.get(i);
//					multiParam.put("fnIdx", fnIdx);
//					multiParam.put("IDX", keyIdx);
//					result = majorInfoMultiDAO.insert(multiParam);
//				}
//			}
//
//			// 3.3 multi file 저장
//			// 3.3.1 multi file 신규 저장
//			List fileDataList = StringUtil.getList(dataMap.get("fileDataList"));
//			if(fileDataList != null) {
//	
//				// 3.3.1.1 key 얻기
//				/*Map<String, Object> fileParam1 = new HashMap<String, Object>();
//				fileParam1.put("IDX", keyIdx);
//				fileParam1.put("fnIdx", fnIdx);
//				int fleIdx = majorInfoFileDAO.getNextId(fileParam1);*/
//				
//				// 3.3.1.2 DB 저장
//				int fileDataSize = fileDataList.size();
//				for(int i = 0 ; i < fileDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)fileDataList.get(i);
//					fileParam.put("fnIdx", fnIdx);
//					fileParam.put("IDX", keyIdx);
//					int fleIdx = majorInfoFileDAO.getNextId(fileParam);
//					fileParam.put("FLE_IDX", fleIdx);
//					result = majorInfoFileDAO.insert(fileParam);
//				}
//			}
//	
//			// 3.3.2 multi file 수정
//			List fileModifyDataList = StringUtil.getList(dataMap.get("fileModifyDataList"));
//			if(fileModifyDataList != null) {
//				int fileDataSize = fileModifyDataList.size();
//				for(int i = 0 ; i < fileDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)fileModifyDataList.get(i);
//					fileParam.put("IDX", keyIdx);
//					fileParam.put("fnIdx", fnIdx);
//					result = majorInfoFileDAO.update(fileParam);
//				}
//			}
//			
//			// 3.3.3 multi file 삭제
//			List fileDeleteSearchList = StringUtil.getList(dataMap.get("fileDeleteSearchList"));
//			if(fileDeleteSearchList != null) {
//	
//				List<Object> deleteMultiFileList = new ArrayList<Object>();
//				int fileDataSize = fileDeleteSearchList.size();
//				for(int i = 0 ; i < fileDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)fileDeleteSearchList.get(i);
//					fileParam.put("fnIdx", fnIdx);
//					fileParam.put("IDX", keyIdx);
//					// 6.1 삭제목록 select
//					List<Object> deleteFileList2 = majorInfoFileDAO.getList(fileParam);
//					if(deleteFileList2 != null) deleteMultiFileList.addAll(deleteFileList2);
//					// 6.2 DB 삭제
//					result = majorInfoFileDAO.cdelete(fileParam);
//				}
//				
//				// 3.3.4 multi file 삭제
//				FileUtil.isKeyDelete(fileRealPath, deleteMultiFileList, "FILE_SAVED_NAME");
//			}
//			
//			// 4. file(단일항목) 삭제
//			List deleteFileList = StringUtil.getList(dataMap.get("deleteFileList"));
//			if(deleteFileList != null) {
//				FileUtil.isDelete(fileRealPath, deleteFileList);
//			}
		}
		return result;
	}
	
	/**
	 * 수정처리 : 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param keyIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param settingInfo
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int deptUpdate(String uploadModulePath, int fnIdx, Map<String,Object> param, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1; 
			
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목
		String level = "";
		if(param.get("DEPT_LEVEL") != null) {
			level = (String) param.get("DEPT_LEVEL");
		}
		
    	
		// 1. 검색조건 setting

		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);

		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		String resprCfmYn = (String) parameterMap.get("resprCfmYn");
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
		
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));
    	dataList.add(new DTForm("RESPR_CFM_YN", resprCfmYn));

    	
    	param.put("dataList", dataList);
    	param.put("fnIdx", fnIdx);

    	// 3. DB 저장
    	// 3.1 기본 정보 테이블
		int result = majorInfoDAO.updateDept(param);
		
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int updateCourMajor(String keyMajorIdx, int keyYearIdx, String uploadModulePath, int fnIdx, HashMap<String,Object> param, String logInIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) {
			System.out.println("items :: " + items);
			System.out.println("itemOrder :: " + itemOrder);
			return -1;
		} 
		List<DTForm> searchList = new ArrayList<DTForm>();	
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목

		// 2. 항목설정으로 저장항목 setting
		/*String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		System.out.println("itemOrder ::: " + itemOrder);
		// 2.1 저장항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);*/

		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		String loginMemberId = null;
		
		if(loginVO != null) {
			loginMemberId = loginVO.getMemberId();			
		}
		
		int dtLength = StringUtil.getInt(parameterMap.get("dtLength"));		
		int insertMajorCour = 0;
		
		
		param.put("MAJOR_CD", keyMajorIdx);
		/*param.put("OPEN_SUST_MJ_CD", keyMajorIdx);*/	
		// delete 쿼리 실행
		majorInfoDAO.deleteMajorCour(param);
		System.out.println("parameterMap L::::" + parameterMap);
		System.out.println("dtLength ::::" + dtLength);
		for(int i = 0; i <= dtLength; i++) {
			
			if (parameterMap.get("subjectCd" + i) != null && parameterMap.get("field" + i) != null ) {
				
				System.out.println("삭제후 인서트 진입 성공");
				searchList.add(new DTForm("MAJOR_CD", keyMajorIdx));
							
				searchList.add(new DTForm("SUBJECT_CD", StringUtil.getString(parameterMap.get("subjectCd" + i))));
	
				param.put("SUBJECT_CD", StringUtil.getString(parameterMap.get("subjectCd" + i)));
				
				
				String comdivCd = (String) parameterMap.get("comdivCd" + i);				
				String grade = (String) parameterMap.get("grade" + i);
				String openDept = (String) parameterMap.get("openDept" + i);
				String subjectNm = StringUtil.getString(parameterMap.get("subjectNm"+ i));
				String subjectCd = (String) parameterMap.get("subjectCd" + i);				
				String field = (String) parameterMap.get("fieldNm" + i);
				String fieldCd = (String) parameterMap.get("field" + i);
				
				String regiDate = (String) parameterMap.get("regiDate" + i);
				String regiId = (String) parameterMap.get("regiId" + i);
				String regiIp = (String) parameterMap.get("regiIp" + i);
				
				String year = (String) parameterMap.get("year" + i);
				String smt = (String) parameterMap.get("smt" + i);
				String cdtNum = (String) parameterMap.get("cdtNum" + i);
				/*String sbjtNmEng = (String) parameterMap.get("sbjtNmEng" + i);*/
				
				// REGI, LAST_MODI 항목 setting
				if(regiDate == null || regiId == null || regiIp == null) {
					dataList.add(new DTForm("REGI_ID", loginMemberId));
					dataList.add(new DTForm("REGI_IP", logInIp));						
				}else {
					dataList.add(new DTForm("REGI_ID", regiId));
					dataList.add(new DTForm("REGI_IP", regiIp));
					param.put("REGI_DATE", regiDate);
				}
				
				dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
				dataList.add(new DTForm("LAST_MODI_IP", regiIp));
								
				dataList.add(new DTForm("MAJOR_CD", keyMajorIdx));
				
				
				//dataList.add(new DTForm("COMDIV_CODE", comdivCd));
				
				dataList.add(new DTForm("GRADE", grade));
				//dataList.add(new DTForm("OPEN_DEPT", openDept));
				dataList.add(new DTForm("SUBJECT_NM", subjectNm));
				dataList.add(new DTForm("SUBJECT_CD", subjectCd));
				dataList.add(new DTForm("FIELD", field));
				dataList.add(new DTForm("FIELD_CD", fieldCd));
				dataList.add(new DTForm("YEAR", year));
				dataList.add(new DTForm("SMT", smt));
				dataList.add(new DTForm("CDT_NUM", cdtNum));
				/*dataList.add(new DTForm("SINBUN_CODE", sinbunCd));*/
				dataList.add(new DTForm("ISDELETE", 0));
				
				param.put("dataList", dataList);
				
				insertMajorCour = majorInfoDAO.insertMajorCour(param);
				
				System.out.println("인서트 후 실패");
				searchList.clear();
				dataList.clear();
			}
			
		}
		

    	param.put("fnIdx", fnIdx);

		return insertMajorCour;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int updateRcmdCult(String keyMajorIdx, int keyYearIdx, String uploadModulePath, int fnIdx, HashMap<String,Object> param, String logInIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) {
			System.out.println("items :: " + items);
			System.out.println("itemOrder :: " + itemOrder);
			return -1;
		} 
		List<DTForm> searchList = new ArrayList<DTForm>();	
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목

		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);

		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		String loginMemberId = null;
		
		if(loginVO != null) {
			loginMemberId = loginVO.getMemberId();			
		}
		
		int dtLength = StringUtil.getInt(parameterMap.get("dtLength"));		
		System.out.println("parameterMap cult ==== " + parameterMap);
		
		param.put("YY", keyYearIdx);
		param.put("MJ_CD", keyMajorIdx);
		param.put("OPEN_SUST_MJ_CD", keyMajorIdx);	
		// delete 쿼리 실행
		majorInfoDAO.deleteRcmdCult(param);
		int result = 0;
		for(int i = 0; i <= dtLength; i++) {
			
			if (parameterMap.get("courseNo" + i) != null && parameterMap.get("eduCorsCapbFg" + i) != null) {
				
				
				searchList.add(new DTForm("MJ_CD", keyMajorIdx));
				searchList.add(new DTForm("YY", keyYearIdx));			
	
				String courseNo = (String) parameterMap.get("courseNo" + i);
				String eduCorsCapbFg = (String) parameterMap.get("eduCorsCapbFg" + i);
				String nmKor = (String) parameterMap.get("nmKor" + i);
				String nmEng = (String) parameterMap.get("nmEng" + i);
				String regiDate = (String) parameterMap.get("regiDate" + i);
				String regiId = (String) parameterMap.get("regiId" + i);
				String regiIp = (String) parameterMap.get("regiIp" + i);
				String pnt = (String) parameterMap.get("pnt" + i);
				String theo = (String) parameterMap.get("theo" + i);
				String prac = (String) parameterMap.get("prac" + i);
				String ord = (String) parameterMap.get("ord" + i);
				
				
				// REGI, LAST_MODI 항목 setting
				if(regiDate == null || regiId == null || regiIp == null) {
					dataList.add(new DTForm("REGI_ID", loginMemberId));
					dataList.add(new DTForm("REGI_IP", logInIp));						
				}else {
					dataList.add(new DTForm("REGI_ID", regiId));
					dataList.add(new DTForm("REGI_IP", regiIp));
//					param.put("REGI_DATE", regiDate);
				}
				
//				int ord = majorInfoDAO.getNextOrd(param);
				
				dataList.add(new DTForm("YY", keyYearIdx));
				dataList.add(new DTForm("MJ_CD", keyMajorIdx));
				dataList.add(new DTForm("COURSE_NO", courseNo));
				dataList.add(new DTForm("EDU_CORS_CAPB_FG", eduCorsCapbFg));
				dataList.add(new DTForm("SBJT_NM_KOR", nmKor));
				dataList.add(new DTForm("SBJT_NM_ENG", nmEng));
				dataList.add(new DTForm("PNT", pnt));
				dataList.add(new DTForm("THEO_TM_CNT", theo));
				dataList.add(new DTForm("PRAC_TM_CNT", prac));
				dataList.add(new DTForm("ORD", ord));
				
		    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
		    	dataList.add(new DTForm("LAST_MODI_IP", logInIp));
		    	
		    	param.put("dataList", dataList);
		    	
		    	result += majorInfoDAO.insertCourCult(param);
		    	dataList.clear();
			}
			
		}
		

    	param.put("fnIdx", fnIdx);

		return result;
	}
	
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int updateYear(String uploadModulePath, int fnIdx, int keyMajorIdx, int keyYearIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		Map<String, Object> param = new HashMap<String, Object>();					// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();							// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목
		
		String columnMajorName = JSONObjectUtil.getString(settingInfo, "idx_major_column");	// key major 컬럼
		String columnYearName = JSONObjectUtil.getString(settingInfo, "idx_year_column");	// key year 컬럼
		// 1. 검색조건 setting
		searchList.add(new DTForm(columnMajorName, keyMajorIdx));
		searchList.add(new DTForm(columnYearName, keyYearIdx));
		

		
		// 2. 항목설정으로 저장항목 setting
//		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + "major" + File.separator + "1";		
		
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
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
		param.put("fnIdx", fnIdx);
		
		// 3. DB 저장
		// 3.1 기본 정보 테이블
		int result = majorInfoDAO.updateYear(param);
		
		if(result > 0){
			// 3.2 multi data 저장
			// 3.2.1 multi data 삭제
//			int result1 = 0;
//			Map<String, Object> multiDelParam = new HashMap<String, Object>();
//			multiDelParam.put("fnIdx", fnIdx);
//			multiDelParam.put("IDX", keyIdx);
//			result1 = majorInfoMultiDAO.cdelete(multiDelParam);
//			
//			// 3.2.2 multi data 등록
			List multiDataList = StringUtil.getList(dataMap.get("multiDataList"));
//			System.out.println( "multiDataList" );
//			System.out.println( multiDataList );
//			if(multiDataList != null) {
//				int multiDataSize = multiDataList.size();
//				for(int i = 0 ; i < multiDataSize ; i ++) {
//					Map<String, Object> multiParam = (HashMap)multiDataList.get(i);
//					multiParam.put("fnIdx", fnIdx);
//					multiParam.put("IDX", keyIdx);
//					result = majorInfoMultiDAO.insert(multiParam);
//				}
//			}
//			// 3.3 multi file 저장
//			// 3.3.1 multi file 신규 저장
			List fileDataList = StringUtil.getList(dataMap.get("fileDataList"));
//			System.out.println( "fileDataList" );
//			System.out.println( fileDataList );
//			if(fileDataList != null) {
//				
//				// 3.3.1.1 key 얻기
//				/*Map<String, Object> fileParam1 = new HashMap<String, Object>();
//				fileParam1.put("IDX", keyIdx);
//				fileParam1.put("fnIdx", fnIdx);
//				int fleIdx = majorInfoFileDAO.getNextId(fileParam1);*/
//				
//				// 3.3.1.2 DB 저장
//				int fileDataSize = fileDataList.size();
//				for(int i = 0 ; i < fileDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)fileDataList.get(i);
//					fileParam.put("fnIdx", fnIdx);
//					fileParam.put("IDX", keyIdx);
//					int fleIdx = majorInfoFileDAO.getNextId(fileParam);
//					fileParam.put("FLE_IDX", fleIdx);
//					result = majorInfoFileDAO.insert(fileParam);
//				}
//			}
//			
//			// 3.3.2 multi file 수정
//			List fileModifyDataList = StringUtil.getList(dataMap.get("fileModifyDataList"));
//			if(fileModifyDataList != null) {
//				int fileDataSize = fileModifyDataList.size();
//				for(int i = 0 ; i < fileDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)fileModifyDataList.get(i);
//					fileParam.put("IDX", keyIdx);
//					fileParam.put("fnIdx", fnIdx);
//					result = majorInfoFileDAO.update(fileParam);
//				}
//			}
//			
//			// 3.3.3 multi file 삭제
//			List fileDeleteSearchList = StringUtil.getList(dataMap.get("fileDeleteSearchList"));
//			if(fileDeleteSearchList != null) {
//				
//				List<Object> deleteMultiFileList = new ArrayList<Object>();
//				int fileDataSize = fileDeleteSearchList.size();
//				for(int i = 0 ; i < fileDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)fileDeleteSearchList.get(i);
//					fileParam.put("fnIdx", fnIdx);
//					fileParam.put("IDX", keyIdx);
//					// 6.1 삭제목록 select
//					List<Object> deleteFileList2 = majorInfoFileDAO.getList(fileParam);
//					if(deleteFileList2 != null) deleteMultiFileList.addAll(deleteFileList2);
//					// 6.2 DB 삭제
//					result = majorInfoFileDAO.cdelete(fileParam);
//				}
//				
//				// 3.3.4 multi file 삭제
//				FileUtil.isKeyDelete(fileRealPath, deleteMultiFileList, "FILE_SAVED_NAME");
//			}
//			
//			// 4. file(단일항목) 삭제
//			List deleteFileList = StringUtil.getList(dataMap.get("deleteFileList"));
//			if(deleteFileList != null) {
//				FileUtil.isDelete(fileRealPath, deleteFileList);
//			}
		}
		return result > 0 ? keyMajorIdx : result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int updateStatistic(String uploadModulePath, int fnIdx, int keyMajorIdx, int keyYearIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		Map<String, Object> param = new HashMap<String, Object>();					// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();							// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목
		
		String columnMajorName = JSONObjectUtil.getString(settingInfo, "idx_major_column");	// key major 컬럼
		String columnYearName = JSONObjectUtil.getString(settingInfo, "idx_year_column");	// key year 컬럼
		// 1. 검색조건 setting
		searchList.add(new DTForm(columnMajorName, keyMajorIdx));
		searchList.add(new DTForm(columnYearName, keyYearIdx));
		
		param.put("searchList", searchList);
		
		// 필수값이 아니기 때문에.. 값이 있는지 확인
		int cnt = majorInfoDAO.getStatisticIdx(param);
		
		System.out.println("itemOrder " + itemOrder);
		System.out.println("parameterMap" + parameterMap);
		
		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
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
//		param.put("searchList", searchList);
		param.put("fnIdx", fnIdx);
		
		System.out.println("param " + param);
		
		// 3. DB 저장
		// 3.1 기본 정보 테이블
		int result = 0;
		
		if(cnt > 0 ) {
			// 값이 있으면 update
			result = majorInfoDAO.updateStatistic(param);
		}else {
			// 값이 없으면 insert
			result = majorInfoDAO.insertMajorStatistic(param);
		}
		
//		int result = majorInfoDAO.updateYear(param);
		
		if(result > 0){
			// 3.2 multi data 저장
			// 3.2.1 multi data 삭제
//			int result1 = 0;
//			Map<String, Object> multiDelParam = new HashMap<String, Object>();
//			multiDelParam.put("fnIdx", fnIdx);
//			multiDelParam.put("IDX", keyIdx);
//			result1 = majorInfoMultiDAO.cdelete(multiDelParam);
//			
//			// 3.2.2 multi data 등록
//			List multiDataList = StringUtil.getList(dataMap.get("multiDataList"));
//			if(multiDataList != null) {
//				int multiDataSize = multiDataList.size();
//				for(int i = 0 ; i < multiDataSize ; i ++) {
//					Map<String, Object> multiParam = (HashMap)multiDataList.get(i);
//					multiParam.put("fnIdx", fnIdx);
//					multiParam.put("IDX", keyIdx);
//					result = majorInfoMultiDAO.insert(multiParam);
//				}
//			}
//			
//			// 3.3 multi file 저장
//			// 3.3.1 multi file 신규 저장
//			List fileDataList = StringUtil.getList(dataMap.get("fileDataList"));
//			if(fileDataList != null) {
//				
//				// 3.3.1.1 key 얻기
//				/*Map<String, Object> fileParam1 = new HashMap<String, Object>();
//				fileParam1.put("IDX", keyIdx);
//				fileParam1.put("fnIdx", fnIdx);
//				int fleIdx = majorInfoFileDAO.getNextId(fileParam1);*/
//				
//				// 3.3.1.2 DB 저장
//				int fileDataSize = fileDataList.size();
//				for(int i = 0 ; i < fileDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)fileDataList.get(i);
//					fileParam.put("fnIdx", fnIdx);
//					fileParam.put("IDX", keyIdx);
//					int fleIdx = majorInfoFileDAO.getNextId(fileParam);
//					fileParam.put("FLE_IDX", fleIdx);
//					result = majorInfoFileDAO.insert(fileParam);
//				}
//			}
//			
//			// 3.3.2 multi file 수정
//			List fileModifyDataList = StringUtil.getList(dataMap.get("fileModifyDataList"));
//			if(fileModifyDataList != null) {
//				int fileDataSize = fileModifyDataList.size();
//				for(int i = 0 ; i < fileDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)fileModifyDataList.get(i);
//					fileParam.put("IDX", keyIdx);
//					fileParam.put("fnIdx", fnIdx);
//					result = majorInfoFileDAO.update(fileParam);
//				}
//			}
//			
//			// 3.3.3 multi file 삭제
//			List fileDeleteSearchList = StringUtil.getList(dataMap.get("fileDeleteSearchList"));
//			if(fileDeleteSearchList != null) {
//				
//				List<Object> deleteMultiFileList = new ArrayList<Object>();
//				int fileDataSize = fileDeleteSearchList.size();
//				for(int i = 0 ; i < fileDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)fileDeleteSearchList.get(i);
//					fileParam.put("fnIdx", fnIdx);
//					fileParam.put("IDX", keyIdx);
//					// 6.1 삭제목록 select
//					List<Object> deleteFileList2 = majorInfoFileDAO.getList(fileParam);
//					if(deleteFileList2 != null) deleteMultiFileList.addAll(deleteFileList2);
//					// 6.2 DB 삭제
//					result = majorInfoFileDAO.cdelete(fileParam);
//				}
//				
//				// 3.3.4 multi file 삭제
//				FileUtil.isKeyDelete(fileRealPath, deleteMultiFileList, "FILE_SAVED_NAME");
//			}
//			
//			// 4. file(단일항목) 삭제
//			List deleteFileList = StringUtil.getList(dataMap.get("deleteFileList"));
//			if(deleteFileList != null) {
//				FileUtil.isDelete(fileRealPath, deleteFileList);
//			}
		}
		return result > 0 ? keyMajorIdx : result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int updateTrack(String uploadModulePath, int fnIdx, int keyMajorIdx, int keyYearIdx, int keyTrackIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		Map<String, Object> param = new HashMap<String, Object>();					// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();							// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목
		
		String columnMajorName = JSONObjectUtil.getString(settingInfo, "idx_major_column");	// key major 컬럼
		String columnYearName = JSONObjectUtil.getString(settingInfo, "idx_year_column");	// key year 컬럼
		String columnTrackName = JSONObjectUtil.getString(settingInfo, "idx_track_column");	// key year 컬럼
		// 1. 검색조건 setting
		searchList.add(new DTForm(columnMajorName, keyMajorIdx));
		searchList.add(new DTForm(columnYearName, keyYearIdx));
		searchList.add(new DTForm(columnTrackName, keyTrackIdx));
		
		param.put("searchList", searchList);
		
		// 필수값이 아니기 때문에.. 값이 있는지 확인
		int cnt = majorInfoDAO.getTrackIdx(param);
		
		System.out.println("itemOrder " + itemOrder);
		System.out.println("parameterMap" + parameterMap);
		
		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
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
//		param.put("searchList", searchList);
		param.put("fnIdx", fnIdx);
		
		System.out.println("param " + param);
		
		// 3. DB 저장
		// 3.1 기본 정보 테이블
		int result = 0;
		
		if(cnt > 0 ) {
			// 값이 있으면 update
			result = majorInfoDAO.updateTrack(param);
		}else {
			// 값이 없으면 insert
			result = majorInfoDAO.insertMajorTrack(param);
		}
		
//		int result = majorInfoDAO.updateYear(param);
		
		if(result > 0){
			// 3.2 multi data 저장
			// 3.2.1 multi data 삭제
//			int result1 = 0;
//			Map<String, Object> multiDelParam = new HashMap<String, Object>();
//			multiDelParam.put("fnIdx", fnIdx);
//			multiDelParam.put("IDX", keyIdx);
//			result1 = majorInfoMultiDAO.cdelete(multiDelParam);
//			
//			// 3.2.2 multi data 등록
//			List multiDataList = StringUtil.getList(dataMap.get("multiDataList"));
//			if(multiDataList != null) {
//				int multiDataSize = multiDataList.size();
//				for(int i = 0 ; i < multiDataSize ; i ++) {
//					Map<String, Object> multiParam = (HashMap)multiDataList.get(i);
//					multiParam.put("fnIdx", fnIdx);
//					multiParam.put("IDX", keyIdx);
//					result = majorInfoMultiDAO.insert(multiParam);
//				}
//			}
//			
//			// 3.3 multi file 저장
//			// 3.3.1 multi file 신규 저장
//			List fileDataList = StringUtil.getList(dataMap.get("fileDataList"));
//			if(fileDataList != null) {
//				
//				// 3.3.1.1 key 얻기
//				/*Map<String, Object> fileParam1 = new HashMap<String, Object>();
//				fileParam1.put("IDX", keyIdx);
//				fileParam1.put("fnIdx", fnIdx);
//				int fleIdx = majorInfoFileDAO.getNextId(fileParam1);*/
//				
//				// 3.3.1.2 DB 저장
//				int fileDataSize = fileDataList.size();
//				for(int i = 0 ; i < fileDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)fileDataList.get(i);
//					fileParam.put("fnIdx", fnIdx);
//					fileParam.put("IDX", keyIdx);
//					int fleIdx = majorInfoFileDAO.getNextId(fileParam);
//					fileParam.put("FLE_IDX", fleIdx);
//					result = majorInfoFileDAO.insert(fileParam);
//				}
//			}
//			
//			// 3.3.2 multi file 수정
//			List fileModifyDataList = StringUtil.getList(dataMap.get("fileModifyDataList"));
//			if(fileModifyDataList != null) {
//				int fileDataSize = fileModifyDataList.size();
//				for(int i = 0 ; i < fileDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)fileModifyDataList.get(i);
//					fileParam.put("IDX", keyIdx);
//					fileParam.put("fnIdx", fnIdx);
//					result = majorInfoFileDAO.update(fileParam);
//				}
//			}
//			
//			// 3.3.3 multi file 삭제
//			List fileDeleteSearchList = StringUtil.getList(dataMap.get("fileDeleteSearchList"));
//			if(fileDeleteSearchList != null) {
//				
//				List<Object> deleteMultiFileList = new ArrayList<Object>();
//				int fileDataSize = fileDeleteSearchList.size();
//				for(int i = 0 ; i < fileDataSize ; i ++) {
//					Map<String, Object> fileParam = (HashMap)fileDeleteSearchList.get(i);
//					fileParam.put("fnIdx", fnIdx);
//					fileParam.put("IDX", keyIdx);
//					// 6.1 삭제목록 select
//					List<Object> deleteFileList2 = majorInfoFileDAO.getList(fileParam);
//					if(deleteFileList2 != null) deleteMultiFileList.addAll(deleteFileList2);
//					// 6.2 DB 삭제
//					result = majorInfoFileDAO.cdelete(fileParam);
//				}
//				
//				// 3.3.4 multi file 삭제
//				FileUtil.isKeyDelete(fileRealPath, deleteMultiFileList, "FILE_SAVED_NAME");
//			}
//			
//			// 4. file(단일항목) 삭제
//			List deleteFileList = StringUtil.getList(dataMap.get("deleteFileList"));
//			if(deleteFileList != null) {
//				FileUtil.isDelete(fileRealPath, deleteFileList);
//			}
		}
		return result > 0 ? keyMajorIdx : result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int updateJob(String uploadModulePath, int fnIdx, int keyMajorIdx, int keyYearIdx, int keyJobIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		Map<String, Object> param = new HashMap<String, Object>();					// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();							// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목
		
		String columnMajorName = JSONObjectUtil.getString(settingInfo, "idx_major_column");	// key major 컬럼
		String columnYearName = JSONObjectUtil.getString(settingInfo, "idx_year_column");	// key year 컬럼
		String columnJobName = JSONObjectUtil.getString(settingInfo, "idx_job_column");	// key year 컬럼
		// 1. 검색조건 setting
		searchList.add(new DTForm(columnMajorName, keyMajorIdx));
		searchList.add(new DTForm(columnYearName, keyYearIdx));
		searchList.add(new DTForm(columnJobName, keyJobIdx));
		
		param.put("searchList", searchList);
		
		// 필수값이 아니기 때문에.. 값이 있는지 확인
		int cnt = majorInfoDAO.getJobIdx(param);
		
		System.out.println("itemOrder " + itemOrder);
		System.out.println("parameterMap" + parameterMap);
		
		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
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
//		param.put("searchList", searchList);
		param.put("fnIdx", fnIdx);
		
		System.out.println("param " + param);
		
		// 3. DB 저장
		// 3.1 기본 정보 테이블
		int result = 0;
		
		if(cnt > 0 ) {
			// 값이 있으면 update
			result = majorInfoDAO.updateJob(param);
		}else {
			// 값이 없으면 insert
			result = majorInfoDAO.insertMajorJob(param);
		}
		
//		int result = majorInfoDAO.updateYear(param);
		
		if(result > 0){
		}
		
		return result > 0 ? keyMajorIdx : result;
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
    	return majorInfoDAO.getDeleteCount(param);
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
		return majorInfoDAO.getDeleteList(param);
	}

	/**
	 * 삭제처리 : 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
	 * @param fnIdx
	 * @param parameterMap
	 * @param deleteIdxs
	 * @param regiIp
	 * @param settingInfo
	 * @return
	 * @throws Exception
	 */
	@Override
	public int delete(int fnIdx, ParamForm parameterMap, int[] deleteIdxs, String regiIp, JSONObject settingInfo) throws Exception {
		if(deleteIdxs == null) return 0;
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목

		// 1. 저장조건
    	String columnName = JSONObjectUtil.getString(settingInfo, "idx_column");
    	searchList.add(new DTForm(columnName, deleteIdxs));
    	
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
    	param.put("fnIdx", fnIdx);
		
		// 3. DB 저장
		return majorInfoDAO.delete(param);
	}
	
	@Override
	public int deleteTrack(int fnIdx, ParamForm parameterMap, int majorIdx, int yearIdx, int trackIdx, String regiIp, JSONObject settingInfo) throws Exception {
		if(majorIdx == 0 || yearIdx == 0 || trackIdx == 0) return 0;
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
		
		// 1. 저장조건
		String columnMajorName = JSONObjectUtil.getString(settingInfo, "idx_major_column");
		String columnYearName = JSONObjectUtil.getString(settingInfo, "idx_year_column");
		String columnTrackName = JSONObjectUtil.getString(settingInfo, "idx_track_column");
		searchList.add(new DTForm(columnMajorName, majorIdx));
		searchList.add(new DTForm(columnYearName, yearIdx));
		searchList.add(new DTForm(columnTrackName, trackIdx));
		
		
		
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
		param.put("fnIdx", fnIdx);
		
		// 3. DB 저장
		return majorInfoDAO.deleteTrack(param);
	}
	
	/**
	 * 등록 처리 : 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param settingInfo
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int abtyInsert(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		Map<String, Object> findParam = new HashMap<String, Object>();					// mapper parameter 데이터
		Map<String, Object> param = new HashMap<String, Object>();					// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목

		String columnName = JSONObjectUtil.getString(settingInfo, "idx_column");	// key 컬럼

		String majorCd = StringUtil.getObjectToString(parameterMap.get("majorCd"));
		// 1. key 얻기
		param.put("fnIdx", fnIdx);
		param.put("majorCd", majorCd);
		int keyIdx = majorInfoDAO.getNextMajorOrdId(param);
		if(keyIdx > 0) {
			dataList.add(new DTForm("ORD", keyIdx));
		}
		

		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		System.out.println("dataMap : " + dataMap);
		System.out.println("dataList : " + dataMap.get("dataList"));
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		findParam.put("searchList", dataList);
//		findParam.put("majorCd", parameterMap.get("connMjMngtHgCd"));
		
		// 값이 있는지 확인
//		int majorIdx = majorInfoDAO.getMajorIdx(findParam);
//		if(majorIdx > 0) {
//			System.out.println("소속 값이 이미 존재합니다. 소속 MAJOR_IDX 값 : " + majorIdx);
//			return majorIdx;
//		}
//		
//		dataList.add(new DTForm(columnName, keyIdx));

		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
    	
		dataList.add(new DTForm("REGI_ID", loginMemberId));
		dataList.add(new DTForm("REGI_IP", regiIp));
		
		dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
		dataList.add(new DTForm("LAST_MODI_IP", regiIp));
		
		/*dataList.add(new DTForm("MJ_NM_KOR", parameterMap.get("mjNmKor") ));
		dataList.add(new DTForm("MJ_NM_ENG", parameterMap.get("mjNmEng") ));*/
    	
		System.out.println("dataList : " + dataMap.get("dataList"));
		param.put("dataList", dataList);
		
		System.out.println("param : " + param);

		// 3. DB 저장
		// 3.1 기본 정보 테이블
		
		int result = majorInfoDAO.abtyInsert(param);
		
		return result;
	}
	
	/**
	 * 수정처리 : 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param keyIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param settingInfo
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int abtyUpdate(String uploadModulePath, int fnIdx, Map<String,Object> param, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1; 
			
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목
    	
		// 1. 검색조건 setting

		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);

		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		String abtyCd = (String) parameterMap.get("abtyCd");
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
		
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));
    	

    	param.put("ABTY_CD", abtyCd);
    	param.put("dataList", dataList);
    	param.put("fnIdx", fnIdx);

    	// 3. DB 저장
    	// 3.1 기본 정보 테이블
		int result = majorInfoDAO.abtyUpdate(param);

		return result;
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
	public int abtyDelete(ParamForm parameterMap, JSONObject settingInfo,  int fnIdx, int[] deleteIdxs, String siteMode, String regiIp) throws Exception {
		if(deleteIdxs == null) return 0;
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		Map<String, Object> param2 = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> searchList2 = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	param.put("fnIdx", fnIdx);												// table flag

		// 1. 저장조건
    	String idxName = JSONObjectUtil.getString(settingInfo, "idx_abty_name");
    	String columnName = JSONObjectUtil.getString(settingInfo, "idx_abty_column");
    	String columnName2 = "PARENT_ABTY_CD";
    	
    	searchList.add(new DTForm(columnName, deleteIdxs));
    	searchList2.add(new DTForm(columnName2, deleteIdxs));
    	
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
		
		param2.put("searchList", searchList2);
		param2.put("dataList", dataList);
		
		// 3. DB 저장
		int result = majorInfoDAO.abtyDelete(param);
		int result1 = majorInfoDAO.parentAbtyDelete(param2);

		if(result > 0 || result1 > 0) {
    		int result2 = LogHelper.insert(3, deleteIdxs);
		}
		
		return result;
	}
	
	/**
	 * 전공능력 정보 인서트
	 * @param searchList
	 * @param remoteAddr
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	@Override
	public int deleteAndInsertNonSbjt(List<DTForm> searchList, String logInIp, ParamForm parameterMap) throws Exception {
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();		// 로그인 사용자 정보
		String loginMemberId = null;
		if(loginVO != null) {
			loginMemberId = loginVO.getMemberId();
		}
		
		// HRD담당자 정보는 수정이 될 때 기존 데이터를 update 하는 것이 아니라 기존 데이터를 delete하고 새로 insert 한다.
		
		List<DTForm> dataList;
		
		Map<String, Object> param;					
				
		
		int insertNonSbjt = 0;
		
		param = new HashMap<String, Object>();					// mapper parameter 데이터		
		param.put("searchList", searchList);
		
		majorInfoDAO.deleteNonSbjt(param);
		
		dataList = new ArrayList<DTForm>();							// 데이터 항목		
		
		int abilityRowLength = StringUtil.getInt((String) parameterMap.get("fieldRowLength"));

		for(int i = 0; i < abilityRowLength; i++) {
			
			if(parameterMap.get("ord" + i) != null) {
				String ord = (String) parameterMap.get("ord" + i);
				String fieldCd = (String) parameterMap.get("fieldCd" + i);	
				String field = (String) parameterMap.get("field" + i);				
				String nonSbjtNm = (String) parameterMap.get("nonSbjtNm" + i);		
				String regiDate = (String) parameterMap.get("regiDate" + i);				
				String regiId = (String) parameterMap.get("regiId" + i);				
				String regiIp = (String) parameterMap.get("regiIp" + i);						
				
				// REGI, LAST_MODI 항목 setting
				if(regiDate == null || regiId == null || regiIp == null) {
					dataList.add(new DTForm("REGI_ID", loginMemberId));
					dataList.add(new DTForm("REGI_IP", logInIp));						
				}else {
					dataList.add(new DTForm("REGI_ID", regiId));
					dataList.add(new DTForm("REGI_IP", regiIp));
					param.put("REGI_DATE", regiDate);
				}
				
				dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
				dataList.add(new DTForm("LAST_MODI_IP", logInIp));
								
				dataList.add(new DTForm("MAJOR_CD", StringUtil.getString(parameterMap.get("majorCd"))));
				//dataList.add(new DTForm("YEAR", StringUtil.getInt(parameterMap.get("year"))));;
				dataList.add(new DTForm("ORD", ord));
				dataList.add(new DTForm("FIELD_CD", fieldCd));
				dataList.add(new DTForm("FIELD", field));
				dataList.add(new DTForm("NON_SBJT_NM", nonSbjtNm));
				dataList.add(new DTForm("ISDELETE", 0));
				
				param.put("dataList", dataList);
				
				insertNonSbjt += majorInfoDAO.insertNonSbjt(param);
				
				dataList.clear();
			}
			
			
		}
		
		return insertNonSbjt;
	}
	
	
	/**
	 * 전공능력 정보 인서트
	 * @param searchList
	 * @param remoteAddr
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	@Override
	public int deleteAndInsertLicense(List<DTForm> searchList, String logInIp, ParamForm parameterMap) throws Exception {
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();		// 로그인 사용자 정보
		String loginMemberId = null;
		if(loginVO != null) {
			loginMemberId = loginVO.getMemberId();
		}
		
		// HRD담당자 정보는 수정이 될 때 기존 데이터를 update 하는 것이 아니라 기존 데이터를 delete하고 새로 insert 한다.
		
		List<DTForm> dataList;
		
		Map<String, Object> param;					
				
		
		int insertLicense = 0;
		
		param = new HashMap<String, Object>();					// mapper parameter 데이터		
		param.put("searchList", searchList);
		
		majorInfoDAO.deleteLicense(param);
		
		dataList = new ArrayList<DTForm>();							// 데이터 항목		
		
		int abilityRowLength = StringUtil.getInt((String) parameterMap.get("fieldRowLength"));

		for(int i = 0; i < abilityRowLength; i++) {
			
			if(parameterMap.get("ord" + i) != null) {
				String ord = (String) parameterMap.get("ord" + i);
				String fieldCd = (String) parameterMap.get("fieldCd" + i);	
				String field = (String) parameterMap.get("field" + i);				
				String licenseNm = (String) parameterMap.get("licenseNm" + i);		
				String regiDate = (String) parameterMap.get("regiDate" + i);				
				String regiId = (String) parameterMap.get("regiId" + i);				
				String regiIp = (String) parameterMap.get("regiIp" + i);						
				
				// REGI, LAST_MODI 항목 setting
				if(regiDate == null || regiId == null || regiIp == null) {
					dataList.add(new DTForm("REGI_ID", loginMemberId));
					dataList.add(new DTForm("REGI_IP", logInIp));						
				}else {
					dataList.add(new DTForm("REGI_ID", regiId));
					dataList.add(new DTForm("REGI_IP", regiIp));
					param.put("REGI_DATE", regiDate);
				}
				
				dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
				dataList.add(new DTForm("LAST_MODI_IP", logInIp));
								
				dataList.add(new DTForm("MAJOR_CD", StringUtil.getString(parameterMap.get("majorCd"))));
				//dataList.add(new DTForm("YEAR", StringUtil.getInt(parameterMap.get("year"))));;
				dataList.add(new DTForm("ORD", ord));
				dataList.add(new DTForm("FIELD_CD", fieldCd));
				dataList.add(new DTForm("FIELD", field));
				dataList.add(new DTForm("LICENSE_NM", licenseNm));
				dataList.add(new DTForm("ISDELETE", 0));
				
				param.put("dataList", dataList);
				
				insertLicense += majorInfoDAO.insertLicense(param);
				
				dataList.clear();
			}
		}
		
		return insertLicense;
	}
	
	

	/**
	 * 복원처리 : 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
	 * @param fnIdx
	 * @param restoreIdxs
	 * @param regiIp
	 * @param settingInfo
	 * @return
	 * @throws Exception
	 */
	@Override
	public int restore(int fnIdx, int[] restoreIdxs, String regiIp, JSONObject settingInfo) throws Exception {
		if(restoreIdxs == null) return 0;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목

		// 1. 저장조건
		searchList.add(new DTForm(JSONObjectUtil.getString(settingInfo, "idx_column"), restoreIdxs));

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
    	param.put("fnIdx", fnIdx);
		
		// 3. DB 저장
		return majorInfoDAO.restore(param);
	}

	/**
	 * 완전삭제처리 : 화면에 조회된 정보를 데이터베이스에서 삭제
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param deleteIdxs
	 * @param settingInfo
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@Override
	public int cdelete(String uploadModulePath, int fnIdx, int[] deleteIdxs, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(deleteIdxs == null) return 0;

		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		
    	// 1. 저장조건
		searchList.add(new DTForm(JSONObjectUtil.getString(settingInfo, "idx_column"), deleteIdxs));
		param.put("searchList", searchList);
    	param.put("fnIdx", fnIdx);
		
		List<Object> deleteMultiFileList = null;
		List<Object> deleteFileList = null;
		// 2. 삭제할 파일 select
		// 2.1 삭제할  multi file 목록 select
		deleteMultiFileList = majorInfoFileDAO.getList(param);
		// 2.2 삭제할 file(단일항목) select
		List deleteFileColumnList = ModuleUtil.getFileObjectList(items, itemOrder);
		if(deleteFileColumnList != null) {
			param.put("columnList", deleteFileColumnList);
			
			deleteFileList = majorInfoDAO.getFileList(param);
		}
		
		// 3. delete
		int result = majorInfoDAO.cdelete(param);
		if(result > 0) {
			// 4. 파일 삭제
			// 4.1 multi file 삭제
			String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
			if(deleteMultiFileList != null) {
				FileUtil.isKeyDelete(fileRealPath, deleteMultiFileList, "FILE_SAVED_NAME");
			}
			
			// 4.2 file(단일항목) 삭제
			if(deleteFileList != null) {
				FileUtil.isKeyDelete(fileRealPath, deleteFileList);
			}
		}
		
		return result;
	}
	
	/**
	 * mult file 전체 목록 : 항목ID에 대한 HashMap
	 * @param fnIdx
	 * @param keyIdx
	 * @param settingInfo
	 * @param items
	 * @param itemOrder
	 * @return
	 */
	public HashMap<String, Object> getMultiFileHashMap(int fnIdx, int keyIdx, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) {
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
			    	searchList.add(new DTForm("A.IDX", keyIdx));
			    	searchList.add(new DTForm("A.ITEM_ID", itemId));
					param.put("searchList", searchList);
			    	param.put("fnIdx", fnIdx);
					resultHashMap.put(itemId, majorInfoFileDAO.getList(param));
				}
			}
		}
		
		return resultHashMap;
	}
	
	/**
	 * mult data 전체 목록 : 항목ID에 대한 HashMap
	 * @param fnIdx
	 * @param keyIdx
	 * @param settingInfo
	 * @param items
	 * @param itemOrder
	 * @return
	 */
	@Override
	public HashMap<String, Object> getMultiHashMap(int fnIdx, int brdIdx, JSONObject items, JSONArray itemOrder) {
		HashMap<String, Object> resultHashMap = new HashMap<String, Object>();
		if(!JSONObjectUtil.isEmpty(itemOrder) && !JSONObjectUtil.isEmpty(items)) {
			int searchOrderSize = itemOrder.size();
			for(int i = 0 ; i < searchOrderSize ; i ++) {
				String itemId = JSONObjectUtil.getString(itemOrder, i);
				JSONObject item = JSONObjectUtil.getJSONObject(items, itemId);
				int formatType = JSONObjectUtil.getInt(item, "format_type");
				int objectType = JSONObjectUtil.getInt(item, "object_type");
				if(formatType == 0 && (objectType == 3 || objectType == 4 || objectType == 11)) {
					// mult file
					Map<String, Object> param = new HashMap<String, Object>();
					List<DTForm> searchList = new ArrayList<DTForm>();
			    	searchList.add(new DTForm("A.IDX", brdIdx));
			    	searchList.add(new DTForm("A.ITEM_ID", itemId));
					param.put("searchList", searchList);
			    	param.put("fnIdx", fnIdx);
					resultHashMap.put(itemId, majorInfoMultiDAO.getList(param));
				}
			}
		}
		
		return resultHashMap;
	}

	/**
	 * 파일등록처리 : 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param settingInfo
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Object> getFileUpload(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return null;

		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return null;
		/*System.out.println("-----------itemOrder:" + itemOrder);
		System.out.println("-----------dataMap:" + dataMap);
		System.out.println("-----------file_origin_name:" + parameterMap.get("file_origin_name"));
		System.out.println("-----------file_saved_name:" + parameterMap.get("file_saved_name"));
		System.out.println("-----------file_size:" + parameterMap.get("file_size"));*/

		List originList = StringUtil.getList(parameterMap.get("file_origin_name"));
		Map<String, Object> fileInfo = null;
		if(originList != null && !originList.isEmpty()) {
			String fileOriginName = StringUtil.getString(originList.get(0));
			HashMap savedMap = StringUtil.getHashMap(parameterMap.get("file_saved_name"));
			HashMap sizeMap = StringUtil.getHashMap(parameterMap.get("file_size"));
			fileInfo = new HashMap<String, Object>();
			fileInfo.put("file_origin_name", fileOriginName);
			fileInfo.put("file_saved_name", savedMap.get(fileOriginName));
			fileInfo.put("file_size", sizeMap.get(fileOriginName));
		}
		return fileInfo;
	}
	
	

	/**
	 * 전공별 등록된 기본정보의 연도 가져오기
	 * @param MJ_CD
	 * @return YY
	 */
	@Override
	public List<Object> getRegisteredYear(String mjCd) throws Exception {
		return majorInfoDAO.getRegisteredYear(mjCd);
	}

	
	/**
	 * 전공능력 정보 인서트
	 * @param searchList
	 * @param remoteAddr
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	@Override
	public int deleteAndInsertMajorAbility(List<DTForm> searchList, String logInIp, ParamForm parameterMap) throws Exception {
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();		// 로그인 사용자 정보
		String loginMemberId = null;
		if(loginVO != null) {
			loginMemberId = loginVO.getMemberId();
		}
		
		// HRD담당자 정보는 수정이 될 때 기존 데이터를 update 하는 것이 아니라 기존 데이터를 delete하고 새로 insert 한다.
		
		List<DTForm> dataList;
		
		Map<String, Object> param;					
				
		
		int insertAbility = 0;
		
		param = new HashMap<String, Object>();					// mapper parameter 데이터		
		param.put("searchList", searchList);
		
		majorInfoDAO.deleteMajorAbility(param);
		
		dataList = new ArrayList<DTForm>();							// 데이터 항목		
		
		int abilityRowLength = StringUtil.getInt((String) parameterMap.get("abilityRowLength"));

		for(int i = 0; i < abilityRowLength; i++) {
			
			if(parameterMap.get("ord" + i) != null) {
				String ord = (String) parameterMap.get("ord" + i);
				String talent = (String) parameterMap.get("talent" + i);				
				String abtyCd = (String) parameterMap.get("abtyCd" + i);				
				String majorAbty = (String) parameterMap.get("majorAbty" + i);		
				String regiDate = (String) parameterMap.get("regiDate" + i);				
				String regiId = (String) parameterMap.get("regiId" + i);				
				String regiIp = (String) parameterMap.get("regiIp" + i);				
				
				// REGI, LAST_MODI 항목 setting
				if(regiDate == null || regiId == null || regiIp == null) {
					dataList.add(new DTForm("REGI_ID", loginMemberId));
					dataList.add(new DTForm("REGI_IP", logInIp));						
				}else {
					dataList.add(new DTForm("REGI_ID", regiId));
					dataList.add(new DTForm("REGI_IP", regiIp));
					param.put("REGI_DATE", regiDate);
				}
				
				dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
				dataList.add(new DTForm("LAST_MODI_IP", logInIp));
								
				dataList.add(new DTForm("MAJOR_CD", StringUtil.getString(parameterMap.get("majorCd"))));
				//dataList.add(new DTForm("YEAR", StringUtil.getInt(parameterMap.get("year"))));;
				dataList.add(new DTForm("ORD", ord));
				dataList.add(new DTForm("TALENT", talent));
				dataList.add(new DTForm("ABTY_CD", abtyCd));
				dataList.add(new DTForm("MAJOR_ABTY", majorAbty));
				dataList.add(new DTForm("ISDELETE", 0));
				
				param.put("dataList", dataList);
				
				insertAbility += majorInfoDAO.insertMajorAbility(param);
				
				dataList.clear();
			}
			
		}
		
		return insertAbility;
	}

	@Override
	public DataMap getModifyTrack(int fnIdx, Map<String, Object> param) {
		// TODO Auto-generated method st ub
		return null;
	}

	@Override
	public List<Object> getFrontCourMajorList(int fnIdx, Map<String, Object> param) throws Exception {
		return majorInfoDAO.getFrontCourMajorList(param);
	}

	@Override
	public List<Object> getMicroMajorList(Map<String, Object> param) throws Exception{
		return majorInfoDAO.getMicroMajorList(param);
	}

	@Override
	public List<Object> getMicroMajorSubjectList(Map<String, Object> param) throws Exception{
		return majorInfoDAO.getMicroMajorSubjectList(param);
	}

	@Override
	public List<Object> getMjCdList(int fnIdx, Map<String, Object> param) {
		List<Object> mjCdList = new ArrayList<Object>();
		
		mjCdList = majorInfoDAO.getMjCdList(param);
		for(int i = 0; i < mjCdList.size(); i++) {			
			System.out.println(mjCdList.get(i));
		}
		
		return mjCdList;
	}

	@Override
	public int copyMajorInfo(Map<String, Object> param) {
		
		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
		
		param.put("REGI_ID", loginMemberId);
		param.put("LAST_MODI_ID", loginMemberId);
		
		return majorInfoDAO.copyMajorInfo(param);
	}

	@Override
	public int insertRcmdCult(String rawBody, String regiIp) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> dataList = new ArrayList<DTForm>();						// 데이터항목
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색항목
		int result = 0;
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		String loginMemberId = null;
		
		if(loginVO != null) {
			loginMemberId = loginVO.getMemberId();
		}
		
		JSONObject reqJsonObj = JSONObject.fromObject(rawBody);
		String[] courseNoArray = reqJsonObj.get("courseNo").toString().split(",");
		String[] nmKorArray = reqJsonObj.get("nmKor").toString().split(",");
		String[] nmEngArray = reqJsonObj.get("nmEng").toString().split(",");
		String[] pntArray = reqJsonObj.get("pnt").toString().split(",");
		String[] theoArray = reqJsonObj.get("theo").toString().split(",");
		String[] pracArray = reqJsonObj.get("prac").toString().split(",");
		String edu = reqJsonObj.get("edu").toString();
		String yy = reqJsonObj.get("yy").toString();
		String mjCd = reqJsonObj.get("mjCd").toString();
		
		
		searchList.add(new DTForm("YY", yy));
		searchList.add(new DTForm("MJ_CD", mjCd));
		searchList.add(new DTForm("EDU_CORS_CAPB_FG", edu));
		
		param.put("searchList", searchList);
		
		for(int i = 0; i < courseNoArray.length; i++) {
			String courseNo = courseNoArray[i];
			String nmKor = nmKorArray[i];
			String nmEng = nmEngArray[i];
			String pnt = pntArray[i];
			String theo = theoArray[i];
			String prac = pracArray[i];			
			int ord = majorInfoDAO.getNextOrd(param);
			
			dataList.add(new DTForm("YY", yy));
			dataList.add(new DTForm("MJ_CD", mjCd));
			dataList.add(new DTForm("COURSE_NO", courseNo));
			dataList.add(new DTForm("EDU_CORS_CAPB_FG", edu));
			dataList.add(new DTForm("SBJT_NM_KOR", nmKor));
			dataList.add(new DTForm("SBJT_NM_ENG", nmEng));
			dataList.add(new DTForm("PNT", pnt));
			dataList.add(new DTForm("THEO_TM_CNT", theo));
			dataList.add(new DTForm("PRAC_TM_CNT", prac));
			dataList.add(new DTForm("ORD", ord));			
	    	dataList.add(new DTForm("REGI_ID", loginMemberId));
	    	dataList.add(new DTForm("REGI_IP", regiIp));
	    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
	    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));
	    	
	    	param.put("dataList", dataList);
	    	
	    	result += majorInfoDAO.insertCourCult(param);
	    	dataList.clear();
		}
		
		return result;
	}

	@Override
	public int getCourCultCount(String rawBody) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색항목
		
		JSONObject reqJsonObj = JSONObject.fromObject(rawBody);
		String yy = reqJsonObj.get("yy").toString();
		String mjCd = reqJsonObj.get("mjCd").toString();
		String edu = reqJsonObj.get("edu").toString();
		String courseNo = reqJsonObj.get("courseNo").toString();
		
		searchList.add(new DTForm("YY", yy));
		searchList.add(new DTForm("MJ_CD", mjCd));
		searchList.add(new DTForm("EDU_CORS_CAPB_FG", edu));
		searchList.add(new DTForm("COURSE_NO", courseNo));
		
		param.put("searchList", searchList);
		
		int result = majorInfoDAO.getCourCultCount(param);
		
		return result;
	}

	@Override
	public List<Object> getFrontRcmdCultList(int fnIdx, Map<String, Object> param) {
		return majorInfoDAO.getFrontRcmdCultList(param);
	}

	@Override
	public List<Object> getPermSustCdList(Map<String, Object> param) {
		return majorInfoDAO.getPermSustCdList(param);
	}
	
	
}