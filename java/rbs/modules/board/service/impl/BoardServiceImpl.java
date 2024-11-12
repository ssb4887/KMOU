package rbs.modules.board.service.impl;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.AuthHelper;
import com.woowonsoft.egovframework.util.DataSecurityUtil;
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
import rbs.egovframework.util.MailUtil;
import rbs.modules.board.mapper.BoardFileMapper;
import rbs.modules.board.mapper.BoardMapper;
import rbs.modules.board.mapper.BoardMultiMapper;
import rbs.modules.board.mapper.BoardOraMapper;
import rbs.modules.board.service.BoardService;

/**
 * 다기능게시판관리에 관한 구현클래스를 정의한다.
 * @author user
 *
 */
@Service("boardService")
public class BoardServiceImpl extends EgovAbstractServiceImpl implements BoardService {

	@Resource(name="boardMapper")
	private BoardMapper boardDAO;
	
	@Resource(name="boardFileMapper")
	private BoardFileMapper boardFileDAO;
	
	@Resource(name="boardMultiMapper")
	private BoardMultiMapper boardMultiDAO;
	
	@Resource(name="boardOraMapper")
	private BoardOraMapper boardOraDAO;

	/**
	 * 전체 목록 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
    @Override
	public int getTotalCount(int fnIdx, Map<String, Object> param) {
    	/* 해양대 공지사항, 취업정보 조회 */
    	if(fnIdx == 1 || fnIdx == 5) {
    		param.put("fnIdx", fnIdx);
        	return boardOraDAO.getNotiTotalCount(param);
    	}
    	param.put("fnIdx", fnIdx);
    	return boardDAO.getTotalCount(param);
    }

    @Override
	public int getNextCount(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	return boardDAO.getNextCount(param);
    }
    
    @Override
	public int getPreCount(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	return boardDAO.getPreCount(param);
    }
    /**
	 * 전체 목록
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(int fnIdx, Map<String, Object> param) {
		/* 해양대 공지사항, 취업정보 조회 */
		if(fnIdx == 1 || fnIdx == 5) {
    		param.put("fnIdx", fnIdx);
        	return boardOraDAO.getList(param);
    	}
		param.put("fnIdx", fnIdx);
		return boardDAO.getList(param);
	}

	@Override
	public List<Object> getBnAList(int fnIdx, Map<String, Object> param) {
		param.put("fnIdx", fnIdx);
    	return boardOraDAO.getBnAList(param);
	}
	
	@Override
	public List<Object> getPreNextList(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return boardDAO.getPreNextList(param);
	}
	
	@Override
	public List<Object> getPntList(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return boardDAO.getPntList(param);
	}
	
	@Override
	public List<Object> getMainList(Map<String, Object> param) {
    	return boardOraDAO.getMainList(param);
	}
	
	/**
	 * 상세조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getView(int fnIdx, Map<String, Object> param) {
		/* 해양대 공지사항, 취업정보 조회 */
		if(fnIdx == 1 || fnIdx == 5) {
    		param.put("fnIdx", fnIdx);
        	return boardOraDAO.getView(param);
    	}
    	param.put("fnIdx", fnIdx);
		DataMap viewDAO = boardDAO.getView(param);
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
		DataMap viewDAO = boardDAO.getFileView(param);
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
		DataMap viewDAO = boardFileDAO.getFileView(param);
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
		searchList.add(new DTForm("BRD_IDX", brdIdx));

    	param.put("searchList", searchList);
		return boardDAO.updateViews(param);
	}
	
	/**
	 * 다운로드 수 수정
	 * @param fnIdx
	 * @param brdIdx
	 * @return
	 * @throws Exception
	 */
	public int updateFileDown(int fnIdx, int brdIdx, String fileColumnName) throws Exception{
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
    	param.put("fnIdx", fnIdx);
		searchList.add(new DTForm("BRD_IDX", brdIdx));

    	param.put("searchList", searchList);
    	param.put("FILE_COLUMN", fileColumnName);
		return boardDAO.updateFileDown(param);
		
	}
	
	/**
	 * multi file 다운로드 수 수정
	 * @param fnIdx
	 * @param brdIdx
	 * @return
	 * @throws Exception
	 */
	public int updateMultiFileDown(int fnIdx, int brdIdx, int fleIdx, String itemId) throws Exception{
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
    	param.put("fnIdx", fnIdx);
		searchList.add(new DTForm("BRD_IDX", brdIdx));
		searchList.add(new DTForm("FLE_IDX", fleIdx));
		searchList.add(new DTForm("ITEM_ID", itemId));

    	param.put("searchList", searchList);
		return boardFileDAO.updateFileDown(param);
		
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
		DataMap viewDAO = boardDAO.getModify(param);
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
    	return boardDAO.getAuthCount(param);
    }
	
	/**
	 * 비밀번호 조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public int getPwdCnt(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	return boardDAO.getPwdCnt(param);
	}

	public int getReLevel(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	return boardDAO.getReLevel(param);
	}

	/**
     * 비밀번호 조회 항목 : PWD, RE_LEVEL
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public DataMap getPwdView(int fnIdx, Map<String, Object> param){
    	param.put("fnIdx", fnIdx);
    	return boardDAO.getPwdView(param);
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
	public int insert(String uploadModulePath, String boardDesignType, int fnIdx, String siteMode, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		String pntIdxColumnName = "PNT_IDX";
		String reStepColumnName = "RE_STEP";
		String reLevelColumnName = "RE_LEVEL";
		int pntIdx = parameterMap.getInt("pntIdx");
    	int reStep = parameterMap.getInt("reStep");
    	int reLevel = parameterMap.getInt("reLevel");
    	boolean isDefaultBoard = StringUtil.isEquals(boardDesignType, "default");
    	boolean isFaqBoard = StringUtil.isEquals(boardDesignType, "faq");
    	boolean isMemoBoard = StringUtil.isEquals(boardDesignType, "memo");
		boolean isReply = StringUtil.isEquals(parameterMap.getString("mode"), "r") ;
		boolean useReply = JSONObjectUtil.isEquals(settingInfo, "use_reply", "1");
		boolean useQna = JSONObjectUtil.isEquals(settingInfo, "use_qna", "1");
		boolean useSecret = JSONObjectUtil.isEquals(settingInfo, "use_secret", "1");
		
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = request.getSession(); 
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	param.put("boardDesignType", boardDesignType);
		param.put("fnIdx", fnIdx);												// table flag

		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");
		String columnName = JSONObjectUtil.getString(settingInfo, "idx_column");
		int brdIdx = parameterMap.getInt(idxName);
		if(StringUtil.isEquals(boardDesignType, RbsProperties.getProperty("Globals.design.NAME_DESIGN_TYPE_MEMO")) && !StringUtil.isEquals(boardDesignType, JSONObjectUtil.getString(settingInfo, "design_type", "default"))){
			dataList.add(new DTForm(columnName, brdIdx));
			columnName = JSONObjectUtil.getString(settingInfo, "memo_idx_column");
			if(AuthHelper.isNoMemberAuthPage("MWT")){
				String pwd = StringUtil.getString(parameterMap.get("pwd"));
				if(!StringUtil.isEmpty(pwd)) {
			    	dataList.add(new DTForm("PWD", DataSecurityUtil.getDigest(pwd)));
				}
			}
		}
		else{
			// 비회원 글쓰기/댓글쓰기 권한 : 비밀번호 등록
			String authName = ((useReply || useQna) && isReply)? "RWT":"WRT";
			boolean isNoMemberAuthPage = AuthHelper.isNoMemberAuthPage(authName);
			if(isNoMemberAuthPage) {
				String pwd = StringUtil.getString(parameterMap.get("pwd"));
				if(!StringUtil.isEmpty(pwd)) {
			    	dataList.add(new DTForm("PWD", DataSecurityUtil.getDigest(pwd)));
				}
			}
		}
		
		// 1. key 얻기
		int idx = boardDAO.getNextId(param);

		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
    	dataList.add(new DTForm(columnName, idx));
		
		if(!isMemoBoard && !isFaqBoard && !isReply) dataList.add(new DTForm(pntIdxColumnName, idx));
		if(isDefaultBoard){
			
			if(useQna || useReply){
				dataList.add(new DTForm(reStepColumnName, reStep + 1));
				dataList.add(new DTForm(reLevelColumnName, reLevel + 1));

				if(isReply){
					dataList.add(new DTForm(pntIdxColumnName, pntIdx));

					Map<String, Object> reStepParam = new HashMap<String, Object>();
					List<DTForm> reStepDataList = new ArrayList<DTForm>();
					List<DTForm> reStepSearchList = new ArrayList<DTForm>();
					
					reStepDataList.add(new DTForm(reStepColumnName, reStepColumnName + " + 1", 1));
					
					reStepSearchList.add(new DTForm(pntIdxColumnName, pntIdx));
					reStepSearchList.add(new DTForm(reStepColumnName, reStep, ">"));
					
					reStepParam.put("dataList", reStepDataList);
					reStepParam.put("searchList", reStepSearchList);
					reStepParam.put("fnIdx", fnIdx);			
					
					boardDAO.update(reStepParam);
				}
			}
		}
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

    	/*
		boolean isWrtNmPage = false; 
		if(isReply) isWrtNmPage = AuthHelper.getModuleUTP("RWT") <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
		else isWrtNmPage = AuthHelper.getModuleUTP("WRT") <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
		
    	if(PrivAuthUtil.isUseNameAuth() && isWrtNmPage && !UserDetailsHelper.isLogin()){
    	*/
		String sAuthType = StringUtil.getString(session.getAttribute("sAuthType"), StringUtil.getString(session.getAttribute("iSAuthType")));
		String sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));
		dataList.add(new DTForm("CHECK_TYPE", sAuthType));
		dataList.add(new DTForm("MEMBER_DUP", sDupInfo));
    	/*
    	}
    	*/
		
    	param.put("dataList", dataList);

    	// 4. DB 저장
		int result = boardDAO.insert(param);
		if(result > 0 && isDefaultBoard && useQna && isReply){
			dataList = new ArrayList<DTForm>();
			dataList.add(new DTForm("REPLY_STATE", parameterMap.getString("replyState")));

			List<DTForm> searchList = new ArrayList<DTForm>();
			searchList.add(new DTForm(columnName, brdIdx));

			param.put("dataList", dataList);
			param.put("searchList", searchList);

			result = boardDAO.update(param);
		}

		// 5. multi file 저장
		List fileDataList = StringUtil.getList(dataMap.get("fileDataList"));
		if(fileDataList != null) {
			int fileDataSize = fileDataList.size();
			for(int i = 0 ; i < fileDataSize ; i ++) {
				Map<String, Object> fileParam = (HashMap)fileDataList.get(i);
				fileParam.put("fnIdx", fnIdx);
				fileParam.put("BRD_IDX", idx);
				int fleIdx = boardFileDAO.getNextId(fileParam);
				fileParam.put("FLE_IDX", fleIdx);
				result = boardFileDAO.insert(fileParam);
			}
		}
		
		// 5. multi data 저장
		List multiDataList = StringUtil.getList(dataMap.get("multiDataList"));
		if(multiDataList != null) {
			int multiDataSize = multiDataList.size();
			for(int i = 0 ; i < multiDataSize ; i ++) {
				Map<String, Object> fileParam = (HashMap)multiDataList.get(i);
				fileParam.put("fnIdx", fnIdx);
				fileParam.put("BRD_IDX", idx);
				result = boardMultiDAO.insert(fileParam);
			}
		}
		
		// 관리자 알림
		if(result > 0 && isDefaultBoard && useQna && JSONObjectUtil.isEquals(settingInfo, "use_sms", "1")){
			JSONObject siteInfo = JSONObjectUtil.getJSONObject(request.getAttribute("siteInfo"));
			JSONObject crtMenu = null;
			if(StringUtil.isEquals(siteMode, RbsProperties.getProperty("Globals.site.mode.adm")))
				crtMenu = JSONObjectUtil.getJSONObject(request.getAttribute("usrCrtMenu"));
			else crtMenu = JSONObjectUtil.getJSONObject(request.getAttribute("crtMenu"));
			
			String siteName = null;
			if(siteInfo != null) siteName = siteInfo.getString("site_name");
			String menuName = null;
			if(!JSONObjectUtil.isEmpty(crtMenu)) menuName = "[" + siteName + "]" + crtMenu.getString("menu_name");
			String mobilePhone = null; 
			String email = null; 
			String regiName = null;
			String subject = null;
			String toName = null;
			String emailTitle = null;
			String emailContents = null;
			
			if(isReply && StringUtil.isEquals(parameterMap.getString("replyState"), "2")){
				DataMap dt = boardDAO.getView(param);
				if(dt != null){
					if(StringUtil.isEquals(dt.get("SMS_GUBUN"), "1")){
						String mobilePhone1 = ModuleUtil.getMemberItemOrgValue("mobilePhone", StringUtil.getString(dt.get("MOBILE_PHONE1")));
						String mobilePhone2 = ModuleUtil.getMemberItemOrgValue("mobilePhone", StringUtil.getString(dt.get("MOBILE_PHONE2")));
						String mobilePhone3 = ModuleUtil.getMemberItemOrgValue("mobilePhone", StringUtil.getString(dt.get("MOBILE_PHONE3")));
						String memberEmail = ModuleUtil.getMemberItemOrgValue("mbrEmail", StringUtil.getString(dt.get("MEMBER_EMAIL")));
						mobilePhone = StringUtil.getString(mobilePhone1, "") + StringUtil.getString(mobilePhone2, "") + StringUtil.getString(mobilePhone3, "");
						email = StringUtil.getString(memberEmail, "");
						regiName = StringUtil.getString(dt.get("NAME"), "");
						subject = StringUtil.getString(dt.get("SUBJECT"), "");
						toName = regiName;
					}
				}
				emailTitle = MessageFormat.format(RbsProperties.getProperty("Globals.mail.board.answer.title"), new String[]{menuName});
				emailContents = MessageFormat.format(RbsProperties.getProperty("Globals.mail.board.answer.contents"), new String[]{menuName, regiName, subject});
				
			}
			else if(!isReply){
				mobilePhone = JSONObjectUtil.getString(settingInfo, "dset_sms_mgphone");
				email = JSONObjectUtil.getString(settingInfo, "dset_sms_mgemail");
				regiName = loginVO !=  null ? loginVO.getMemberNameOrg() : "";
				emailTitle = MessageFormat.format(RbsProperties.getProperty("Globals.mail.board.question.title"), new String[]{menuName});
				emailContents = MessageFormat.format(RbsProperties.getProperty("Globals.mail.board.question.contents"), new String[]{menuName, regiName});
			}
			
			// SMS 전송
			if(!StringUtil.isEmpty(mobilePhone)){
				
			}

			// 이메일 전송
			if(!StringUtil.isEmpty(email)){
				MailUtil.sendMail(RbsProperties.getProperty("Globals.mail.fromMail"), RbsProperties.getProperty("Globals.mail.fromName"), 
						email, toName, emailTitle, emailContents);
			}
		}
		
		if(result > 0) {
    		int result2 = LogHelper.insert(1, idx);
		}
		
		return result > 0 ? idx : result;
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
	public int update(String uploadModulePath, String boardDesignType, int fnIdx, int brdIdx, String siteMode, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
    	param.put("boardDesignType", boardDesignType);
    	param.put("fnIdx", fnIdx);												// table flag
    	
    	String columnName = JSONObjectUtil.getString(settingInfo, "idx_column");
		// 1. 검색조건 setting
		searchList.add(new DTForm(columnName, brdIdx));
		if(StringUtil.isEquals(boardDesignType, RbsProperties.getProperty("Globals.design.NAME_DESIGN_TYPE_MEMO")) && !StringUtil.isEquals(boardDesignType, JSONObjectUtil.getString(settingInfo, "design_type", "default")))
			searchList.add(new DTForm(JSONObjectUtil.getString(settingInfo, "memo_idx_column"), parameterMap.getInt(JSONObjectUtil.getString(settingInfo, "memo_idx_name"))));

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
		
		boolean isReply = parameterMap.getInt("reLevel") > 1;
		boolean useReply = JSONObjectUtil.isEquals(settingInfo, "use_reply", "1");
		boolean useQna = JSONObjectUtil.isEquals(settingInfo, "use_qna", "1");
		// 비회원 글쓰기/댓글쓰기 권한 : 비밀번호 등록
		String authName = ((useReply || useQna) && isReply)? "RWT":"WRT";
		//String authName = "WRT";
		boolean isNoMemberAuthPage = AuthHelper.isNoMemberAuthPage(authName);
		if(isNoMemberAuthPage) {
			String pwd = StringUtil.getString(parameterMap.get("pwd"));
			if(!StringUtil.isEmpty(pwd)) {
		    	dataList.add(new DTForm("PWD", DataSecurityUtil.getDigest(pwd)));
			}
		}
    	
    	dataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));
    	
    	param.put("dataList", dataList);
    	param.put("searchList", searchList);
    	

    	boolean isDefaultBoard = StringUtil.isEquals(boardDesignType, "default");
		//boolean useQna = JSONObjectUtil.isEquals(settingInfo, "use_qna", "1");
    	
    	// 3. DB 저장
		int result = boardDAO.update(param);

		if(result > 0 && isDefaultBoard && useQna && parameterMap.getInt("reLevel") > 1){
			dataList = new ArrayList<DTForm>();
			searchList = new ArrayList<DTForm>();
			
			dataList.add(new DTForm("REPLY_STATE", parameterMap.getString("replyState")));
			
			searchList.add(new DTForm(columnName, parameterMap.getInt("pntIdx")));

			param.put("dataList", dataList);
			param.put("searchList", searchList);

			result = boardDAO.update(param);
		}

		// 4. multi file 신규 저장
		List fileDataList = StringUtil.getList(dataMap.get("fileDataList"));
		if(fileDataList != null) {

			// 4.1 key 얻기
			/*Map<String, Object> fileParam1 = new HashMap<String, Object>();
			fileParam1.put("fnIdx", fnIdx);
			fileParam1.put("BRD_IDX", brdIdx);
			int fleIdx = boardFileDAO.getNextId(fileParam1);*/
			
			// 4.2 DB 저장
			int fileDataSize = fileDataList.size();
			for(int i = 0 ; i < fileDataSize ; i ++) {
				Map<String, Object> fileParam = (HashMap)fileDataList.get(i);
				fileParam.put("fnIdx", fnIdx);
				fileParam.put("BRD_IDX", brdIdx);
				int fleIdx = boardFileDAO.getNextId(fileParam);
				fileParam.put("FLE_IDX", fleIdx);
				result = boardFileDAO.insert(fileParam);
			}
		}

		// 5 multi file 수정
		List fileModifyDataList = StringUtil.getList(dataMap.get("fileModifyDataList"));
		if(fileModifyDataList != null) {
			int fileDataSize = fileModifyDataList.size();
			for(int i = 0 ; i < fileDataSize ; i ++) {
				Map<String, Object> fileParam = (HashMap)fileModifyDataList.get(i);
				fileParam.put("fnIdx", fnIdx);
				fileParam.put("BRD_IDX", brdIdx);
				result = boardFileDAO.update(fileParam);
			}
		}

		// 5. multi data 저장
		int result1 = 0;
		if(!StringUtil.isEquals(boardDesignType, RbsProperties.getProperty("Globals.design.NAME_DESIGN_TYPE_MEMO"))){
			Map<String, Object> multiDelParam = new HashMap<String, Object>();
			multiDelParam.put("fnIdx", fnIdx);
			multiDelParam.put("BRD_IDX", brdIdx);
			result1 = boardMultiDAO.cdelete(multiDelParam);
		}
		
		List multiDataList = StringUtil.getList(dataMap.get("multiDataList"));
		if(multiDataList != null) {
			int multiDataSize = multiDataList.size();
			for(int i = 0 ; i < multiDataSize ; i ++) {
				Map<String, Object> multiParam = (HashMap)multiDataList.get(i);
				multiParam.put("fnIdx", fnIdx);
				multiParam.put("BRD_IDX", brdIdx);
				result = boardMultiDAO.insert(multiParam);
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
				fileParam.put("BRD_IDX", brdIdx);
				// 6.1 삭제목록 select
				List<Object> deleteFileList2 = boardFileDAO.getList(fileParam);
				if(deleteFileList2 != null) deleteMultiFileList.addAll(deleteFileList2);
				// 6.2 DB 삭제
				result = boardFileDAO.cdelete(fileParam);
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

		// 관리자 알림
		if(result > 0 && isDefaultBoard && useQna && JSONObjectUtil.isEquals(settingInfo, "use_sms", "1")
				&& StringUtil.isEquals(parameterMap.getString("mode"), "m") && parameterMap.getInt("reStep") > 1 && StringUtil.isEquals(parameterMap.getString("replyState"), "2")){
			JSONObject siteInfo = JSONObjectUtil.getJSONObject(request.getAttribute("siteInfo"));
			JSONObject crtMenu = null;
			if(StringUtil.isEquals(siteMode, RbsProperties.getProperty("Globals.site.mode.adm")))
				crtMenu = JSONObjectUtil.getJSONObject(request.getAttribute("usrCrtMenu"));
			else crtMenu = JSONObjectUtil.getJSONObject(request.getAttribute("crtMenu"));
			
			String siteName = null;
			if(siteInfo != null) siteName = siteInfo.getString("site_name");
			String menuName = null;
			if(!JSONObjectUtil.isEmpty(crtMenu)) menuName = "[" + siteName + "]" + crtMenu.getString("menu_name");
			String mobilePhone = null;
			String email = null;
			String regiName = null;
			String subject = null;
			
			DataMap dt = boardDAO.getView(param);
			if(dt != null){
				brdIdx = StringUtil.getInt(dt.get(columnName));
				if(StringUtil.isEquals(dt.get("SMS_GUBUN"), "1")){
					String mobilePhone1 = ModuleUtil.getMemberItemOrgValue("mobilePhone", StringUtil.getString(dt.get("MOBILE_PHONE1")));
					String mobilePhone2 = ModuleUtil.getMemberItemOrgValue("mobilePhone", StringUtil.getString(dt.get("MOBILE_PHONE2")));
					String mobilePhone3 = ModuleUtil.getMemberItemOrgValue("mobilePhone", StringUtil.getString(dt.get("MOBILE_PHONE3")));
					String memberEmail = ModuleUtil.getMemberItemOrgValue("mbrEmail", StringUtil.getString(dt.get("MEMBER_EMAIL")));
					mobilePhone = StringUtil.getString(mobilePhone1, "") + StringUtil.getString(mobilePhone2, "") + StringUtil.getString(mobilePhone3, "");
					email = StringUtil.getString(memberEmail, "");
					regiName = StringUtil.getString(dt.get("NAME"), "");
					subject = StringUtil.getString(dt.get("SUBJECT"), "");
					
					/*mobilePhone = StringUtil.getString(dt.get("MOBILE_PHONE1"), "") + StringUtil.getString(dt.get("MOBILE_PHONE2"), "") + StringUtil.getString(dt.get("MOBILE_PHONE3"), "");
					email = StringUtil.getString(dt.get("MEMBER_EMAIL"), "");
					regiName = StringUtil.getString(dt.get("NAME"), "");
					subject = StringUtil.getString(dt.get("SUBJECT"), "");*/
				}
			}
			
			// SMS 전송
			if(!StringUtil.isEmpty(mobilePhone)){
				
			}

			// 이메일 전송
			if(!StringUtil.isEmpty(email)){
				MailUtil.sendMail(RbsProperties.getProperty("Globals.mail.fromMail"), RbsProperties.getProperty("Globals.mail.fromName"), 
						email, regiName, MessageFormat.format(RbsProperties.getProperty("Globals.mail.board.answer.title"), new String[]{menuName}),  
						MessageFormat.format(RbsProperties.getProperty("Globals.mail.board.answer.contents"), new String[]{menuName, regiName, subject}));
			}
		}
		
		if(result > 0) {
    		int result2 = LogHelper.insert(2, brdIdx);
		}
		
		return result > 0 ? brdIdx : result;
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
    	return boardDAO.getDeleteCount(param);
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
		return boardDAO.getDeleteList(param);
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
	public int delete(ParamForm parameterMap, JSONObject settingInfo, String boardDesignType, int fnIdx, int[] deleteIdxs, String siteMode, String regiIp) throws Exception {
		if(deleteIdxs == null) return 0;
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
		param.put("boardDesignType", boardDesignType);
    	param.put("fnIdx", fnIdx);												// table flag

		// 1. 저장조건
    	String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");
    	String columnName = JSONObjectUtil.getString(settingInfo, "idx_column");
    	if(StringUtil.isEquals(boardDesignType, RbsProperties.getProperty("Globals.design.NAME_DESIGN_TYPE_MEMO")) && !StringUtil.isEquals(boardDesignType, JSONObjectUtil.getString(settingInfo, "design_type", "default"))){
    		searchList.add(new DTForm(columnName, parameterMap.getInt(idxName)));
    		columnName = JSONObjectUtil.getString(settingInfo, "memo_idx_column");
    	}

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
		
		// 3. DB 저장
		int result = boardDAO.delete(param);

		if(result > 0) {
    		int result2 = LogHelper.insert(3, deleteIdxs);
		}
		
		return result;
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
	public int restore(JSONObject settingInfo, int fnIdx, int[] restoreIdxs, String siteMode, String regiIp) throws Exception {
		if(restoreIdxs == null) return 0;

		String boardDesignType = JSONObjectUtil.getString(settingInfo, "design_type", "default");
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
		param.put("boardDesignType", boardDesignType);
    	param.put("fnIdx", fnIdx);												// table flag

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
		
		// 3. DB 저장
		int result = boardDAO.restore(param);

		if(result > 0) {
    		int result2 = LogHelper.insert(4, restoreIdxs);
		}
		
		return result;
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
	public int cdelete(String uploadModulePath, JSONObject settingInfo, int fnIdx, int[] deleteIdxs, JSONObject items, JSONArray itemOrder) throws Exception {
		if(deleteIdxs == null) return 0;

		String boardDesignType = JSONObjectUtil.getString(settingInfo, "design_type", "default");
		
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		param.put("boardDesignType", boardDesignType);
    	param.put("fnIdx", fnIdx);												// table flag
		
    	// 1. 저장조건
		searchList.add(new DTForm(JSONObjectUtil.getString(settingInfo, "idx_column"), deleteIdxs));
		param.put("searchList", searchList);
		
		List<Object> deleteMultiFileList = null;
		List<Object> deleteFileList = null;
		if(!StringUtil.isEquals(boardDesignType, RbsProperties.getProperty("Globals.design.NAME_DESIGN_TYPE_MEMO"))){
			// 2. 삭제할 파일 select
			// 2.1 삭제할  multi file 목록 select
			deleteMultiFileList = boardFileDAO.getList(param);
			// 2.2 삭제할 file(단일항목) select
			List deleteFileColumnList = ModuleUtil.getFileObjectList(items, itemOrder);
			if(deleteFileColumnList != null) {
				param.put("columnList", deleteFileColumnList);
				
				deleteFileList = boardDAO.getFileList(param);
			}
		}
		
		// 3. delete
		int result = boardDAO.cdelete(param);
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

    		int result2 = LogHelper.insert(5, deleteIdxs);
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
		if(fnIdx == 1 || fnIdx == 5) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("nttSn", brdIdx);
			resultHashMap.put("file", boardOraDAO.getFileList(param));
		}else {
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
				    	searchList.add(new DTForm("A.BRD_IDX", brdIdx));
				    	searchList.add(new DTForm("A.ITEM_ID", itemId));
						param.put("searchList", searchList);
				    	param.put("fnIdx", fnIdx);
						resultHashMap.put(itemId, boardFileDAO.getList(param));
					}
				}
			}
		}
	
		return resultHashMap;
	}
	

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
			    	searchList.add(new DTForm("A.BRD_IDX", brdIdx));
			    	searchList.add(new DTForm("A.ITEM_ID", itemId));
					param.put("searchList", searchList);
			    	param.put("fnIdx", fnIdx);
					resultHashMap.put(itemId, boardMultiDAO.getList(param));
				}
			}
		}
		
		return resultHashMap;
	}

	/**
	 * 파일다운로드 사유 전체 목록 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
    @Override
	public int getFileCmtCount(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	return boardDAO.getFileCmtCount(param);
    }

    /**
	 * 파일다운로드 사유 전체 목록
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getFileCmtList(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
		return boardDAO.getFileCmtList(param);
	}
	
    /**
     * 파일다운로드 사유 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return int 등록결과
     */
	@Override
    public int fileCmtInsert(int fnIdx, int brdIdx, String regiIp, ParamForm parameterMap) {

		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색항목
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
		// 1. key 얻기
		searchList.add(new DTForm("FN_IDX", fnIdx));
		searchList.add(new DTForm("BRD_IDX", brdIdx));
		param.put("searchList", searchList);
		int cmtIdx = boardDAO.getFileCmtNextId(param);
		
		// 2. DB 저장
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberIdOrg();
			loginMemberName = loginVO.getMemberNameOrg();
		}

    	dataList.add(new DTForm("FN_IDX", fnIdx));
    	dataList.add(new DTForm("FN_NAME", parameterMap.get("fnName")));
    	dataList.add(new DTForm("BRD_IDX", brdIdx));
    	dataList.add(new DTForm("CMT_IDX", cmtIdx));
    	dataList.add(new DTForm("ITEM_ID", parameterMap.get("itemId")));
    	dataList.add(new DTForm("FLE_IDX", parameterMap.get("fleIdx")));
    	dataList.add(new DTForm("FILE_SAVED_NAME", parameterMap.get("fileSavedName")));
    	dataList.add(new DTForm("FILE_ORIGIN_NAME", parameterMap.get("fileOriginName")));
    	dataList.add(new DTForm("CONTENTS", parameterMap.get("fileCmt")));
    	dataList.add(new DTForm("REGI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("REGI_ID", loginMemberId));
    	dataList.add(new DTForm("REGI_NAME", loginMemberName));
    	dataList.add(new DTForm("REGI_IP", regiIp));

		param.put("dataList", dataList);
    	return boardDAO.fileCmtInsert(param);
    }

}