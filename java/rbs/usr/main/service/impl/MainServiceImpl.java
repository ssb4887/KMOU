package rbs.usr.main.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rbs.egovframework.LoginVO;
import rbs.modules.basket.mapper.BasketOraDevMapper;
import rbs.modules.basket.mapper.BasketOraMapper;
import rbs.usr.main.mapper.MainMapper;
import rbs.usr.main.mapper.MainOceanMapper;
import rbs.usr.main.mapper.MainRbsMapper;
import rbs.usr.main.service.MainService;


@Service("usrMainService")
public class MainServiceImpl extends EgovAbstractServiceImpl implements MainService {
	private final Logger logger = Logger.getLogger(getClass());

	@Resource(name="usrMainMapper")
	private MainMapper mainDao;
	
	@Resource(name="usrMainOceanMapper")
	private MainOceanMapper mainOceanDao;
	
	@Resource(name="usrMainRbsMapper")
	private MainRbsMapper mainRbsDao;
	
	@Resource(name="basketOraMapper")
	private BasketOraMapper basketOraDAO;
	
//	개발
	@Resource(name="basketOraDevMapper")
	private BasketOraDevMapper basketOraDevDAO;

	@Override
	public List<Object> getModuleList(String moduleId, int fnIdx, int limitNumber, String itemMasterCode){
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		param.put("itemMasterCode", itemMasterCode);
		param.put("searchList", searchList);
		param.put("firstIndex", 0);
		param.put("lastIndex", limitNumber);
		param.put("recordCountPerPage", limitNumber);
    	param.put("fnIdx", fnIdx);
		return mainDao.getModuleList(moduleId, param);
	}
	
	@Override
	public Map<Object, List<Object>> getBoardListHashMap(DataForm queryString, int lastIndex, JSONArray boardMenuJsonArray, JSONObject boardTotalSetting){
		if(JSONObjectUtil.isEmpty(boardMenuJsonArray) || JSONObjectUtil.isEmpty(boardTotalSetting)) return null;

		String totalKey = StringUtil.toLowerCase(StringUtil.replaceAll(queryString.getString("fn_totalKey"), " ", ""));
		if(StringUtil.isEmpty(totalKey)) return null;
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		int loginUsertypeIdx = 0;
		String loginMemberIdx = null;
		if(loginVO != null) {
			loginUsertypeIdx = loginVO.getUsertypeIdx();
			loginMemberIdx = loginVO.getMemberIdx();
		}
		String sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));
		boolean isNmPage = loginUsertypeIdx == RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
		
		Map<String, Object> param = new HashMap<String, Object>();
		JSONArray boardJsonArray = null;
		int boardMenuLen = boardMenuJsonArray.size();
		for (int i = 0; i < boardMenuLen; i++){
			JSONObject board = new JSONObject();
			JSONObject boardMenu = JSONObjectUtil.getJSONObject(boardMenuJsonArray, i);
			if(JSONObjectUtil.isEmpty(boardMenu)) continue;
			
			int fnIdx = JSONObjectUtil.getInt(boardMenu, "fn_idx");
			if(fnIdx < 1) continue;
			board.put("fnIdx", fnIdx);
			
			int menuIdx = JSONObjectUtil.getInt(boardMenu, "menu_idx");
			board.put("menuIdx", menuIdx);
			
			String boardDesignType = JSONObjectUtil.getString(boardMenu, "design_type");
			board.put("boardDesignType", boardDesignType);
			
			boolean useQna = false;
			boolean useDldate = false;
			boolean useCate = false;
			boolean usePrivate = false;
			String dsetCateListColumnId = null;
			String dsetCateListMasterCode = null;
			String boardIdxColumn = "BRD_IDX";
			JSONObject boardSetting = JSONObjectUtil.getJSONObject(boardTotalSetting, "item" + fnIdx);
			if(!JSONObjectUtil.isEmpty(boardSetting)){
				useQna = JSONObjectUtil.isEquals(boardSetting, "use_qna", "1");
				useDldate = JSONObjectUtil.isEquals(boardSetting, "use_dldate", "1");
				usePrivate = JSONObjectUtil.isEquals(boardSetting, "use_private", "1") || JSONObjectUtil.isEquals(boardMenu, "fn_use_private", "1");
				dsetCateListColumnId = JSONObjectUtil.getString(boardSetting, "dset_cate_list_column_id");
				dsetCateListMasterCode = JSONObjectUtil.getString(boardSetting, "dset_cate_list_master_code");
				boardIdxColumn = JSONObjectUtil.getString(boardSetting, "idx_column");
				
				if(!StringUtil.isEmpty(dsetCateListColumnId) && !StringUtil.isEmpty(dsetCateListMasterCode)) useCate = true;
				else{
					dsetCateListColumnId = null;
					dsetCateListMasterCode = null;
				}
			}
			
			board.put("useQna", useQna);
			board.put("useDldate", useDldate);
			board.put("useCate", useCate);
			board.put("usePrivate", usePrivate);
			board.put("dsetCateListColumnId", dsetCateListColumnId);
			board.put("dsetCateListMasterCode", dsetCateListMasterCode);
			board.put("boardIdxColumn", boardIdxColumn);

			List<DTForm> searchList = new ArrayList<DTForm>();
			if(StringUtil.isEquals(boardDesignType, RbsProperties.getProperty("Globals.design.NAME_DESIGN_TYPE_MEMO"))){
				searchList.add(new DTForm("A.CONTENTS", "%" + totalKey + "%", 0, "LIKE"));	
			}
			else{
				searchList.add(new DTForm("A.SUBJECT", "%" + totalKey + "%", 0, "LIKE", "AND", "(", ""));
				searchList.add(new DTForm("A.CONTENTS", "%" + totalKey + "%", 0, "LIKE", "OR", "", ")"));
			}
			board.put("searchList", searchList);
			
			if(JSONObjectUtil.isEmpty(boardJsonArray)) boardJsonArray = new JSONArray();
			
			boardJsonArray.add(board);
		}
		
		param.put("boardJsonArray", boardJsonArray);
		param.put("memberIdx", loginMemberIdx);
		if(isNmPage) param.put("memberDup", sDupInfo);
		param.put("isNmPage", isNmPage);
		param.put("firstIndex", 0);
		param.put("lastIndex", lastIndex);
		param.put("recordCountPerPage", lastIndex);
		
		return mainDao.getBoardHashMap(param);
	}
	
	@Override
	public Map<Object, List<Object>> getContentsListHashMap(DataForm queryString, JSONArray contentsMenuJsonArray){
		if(JSONObjectUtil.isEmpty(contentsMenuJsonArray)) return null;

		String totalKey = StringUtil.toLowerCase(StringUtil.replaceAll(queryString.getString("fn_totalKey"), " ", ""));
		if(StringUtil.isEmpty(totalKey)) return null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		JSONArray contentsJsonArray = null;
		int contentsMenuLen = contentsMenuJsonArray.size();
		for (int i = 0; i < contentsMenuLen; i++){
			JSONObject contents = new JSONObject();
			JSONObject contentsMenu = JSONObjectUtil.getJSONObject(contentsMenuJsonArray, i);
			if(JSONObjectUtil.isEmpty(contentsMenu)) continue;
			
			int menuIdx = JSONObjectUtil.getInt(contentsMenu, "menu_idx");
			contents.put("menuIdx", menuIdx);
			
			List<DTForm> searchList = new ArrayList<DTForm>();
			searchList.add(new DTForm("A.CONTENTS_CODE", JSONObjectUtil.getString(contentsMenu, "contents_code")));
			searchList.add(new DTForm("A.BRANCH_IDX", JSONObjectUtil.getString(contentsMenu, "branch_idx")));
			searchList.add(new DTForm("A.WORK_CONTENTS", "%" + totalKey + "%", 0, "LIKE"));	
			contents.put("searchList", searchList);
			
			if(JSONObjectUtil.isEmpty(contentsJsonArray)) contentsJsonArray = new JSONArray();
			
			contentsJsonArray.add(contents);
		}
		
		param.put("contentsJsonArray", contentsJsonArray);
		
		return mainDao.getContentsHashMap(param);
	}

	@Override
	public DataMap getMyCurriculum(Map<String, Object> param) {
		return mainDao.getMyCurriculum(param);
	}
	
	@Override
	public DataMap getMyInfomation(Map<String, Object> param) {
		return mainDao.getMyInfomation(param);
	}
	
	@Override
	public List<Object> getMyGoalCDT(Map<String, Object> param) {
		List<Object> list = mainDao.getMyGoalCDT(param);
		if(list == null || list.size() < 1) list = mainDao.getTransferMyGoalCDT(param);
		return list;	
	}
	
	@Override
	public DataMap getMyCDT(Map<String, Object> param) {
		return (DataMap) mainDao.getMyCDT(param);	
	}
	
	@Override
	public List<Object> getMyGPA(Map<String, Object> param) {
		return mainDao.getMyGPA(param);
	}
	
	@Override
	public List<Object> getMyCdtDetail(Map<String, Object> param) {
		return mainDao.getMyCdtDetail(param);
	}
	
	@Override
	public List<Object> getMyScholAmt(Map<String, Object> param) {
		return mainDao.getMyScholAmt(param);
	}
	
	@Override
	public List<Object> getMyMajorReq(Map<String, Object> param) {
		return mainDao.getMyMajorReq(param);
	}
	
	@Override
	public List<Object> getMyMinorReq(Map<String, Object> param) {
		return mainDao.getMyMinorReq(param);
	}
	
	@Override
	public List<Object> getMyCurrMinor(Map<String, Object> param) {
		List<Object> list = mainDao.getMyCurrMinor(param);
		if(list == null || list.size() < 1) list = mainDao.getTransferMyCurrMinor(param);
		return list;
	}
	
	@Override
	public List<Object> getAiMajorSubject(List<Map<String, Object>> paramsList) {
		return mainDao.getAiMajorSubject(paramsList);
	}
	
	@Override
	public List<Object> getAiNonCourse(List<Map<String, Object>> paramsList) {
		return mainOceanDao.getAiNonCourse(paramsList);
	}
	
	@Override
	public List<Object> getCoreCompDiagnosis(Map<String, Object> param) {
		return mainOceanDao.getCoreCompDiagnosis(param);
	}
	
	@Override
	public List<Object> getMajorCompDiagnosis(Map<String, Object> param) {
		return mainOceanDao.getMajorCompDiagnosis(param);
	}
	
	@Override
	public List<Object> getAcademicCalendar(Map<String, Object> param) {
		return mainDao.getAcademicCalendar(param);
	}
	
	@Override
	public String loginTis(String siteMode, String regiIp, ParamForm parameterMap, JSONObject itemInfo, JSONObject settingInfo, JSONObject memberItemInfo) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();

		String mbrId = parameterMap.getString("mbrId");
		String mbrPwd = parameterMap.getString("mbrPwd");
		
		String result = "";
		
		if(mbrId.equals("kmouadmin") && mbrPwd.equals("kmouadmin")){
			// CMS의 최고관리자 권한의 로그인 아이디 하나는 열어놓는다(개발자 접근용 - 추후에 삭제할수도있음)
			result = "Y";
		}else {			
	    	// 1. 종합정보시스템 계정정보와 일치 여부 체크
			result = mainDao.loginTis(param);				    	
		}
		return result;
	}

	@Override
	public LoginVO setUser(ParamForm parameterMap, HttpServletRequest request) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();		
		
		//종합정보시스템 로그인
		param.put("MEMBER_ID", parameterMap.getString("mbrId"));
		param.put("MEMBER_PWD", parameterMap.getString("mbrPwd"));
		
		
		return mainDao.setUser(param);
	}

	@Override
	public List<Object> getEmploymentRate() throws Exception {
		return mainDao.getEmploymentRate();
	}

	@Override
	public String getIsPrivacyPolicy(String memberId) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("MEMBER_ID", memberId);
		
		return mainRbsDao.getIsPrivacyPolicy(param);
	}

	@Override
	public int insertPrivacyPolicy() throws Exception {				
		Map<String, Object> param = new HashMap<String, Object>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		param.put("MEMBER_ID", loginVO.getMemberId());
		
		return mainRbsDao.insertPrivacyPolicy(param);		
	}


}