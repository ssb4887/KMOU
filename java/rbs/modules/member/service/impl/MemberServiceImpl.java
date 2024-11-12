package rbs.modules.member.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.DataSecurityUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import rbs.egovframework.LoginVO;
import rbs.egovframework.SnsLoginVO;
import rbs.egovframework.util.PrivAuthUtil;
import rbs.modules.member.mapper.MemberMapper;
import rbs.modules.member.service.MemberLogService;
import rbs.modules.member.service.MemberService;

/**
 * 기관정보관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 */
@Service("memberService")
public class MemberServiceImpl extends EgovAbstractServiceImpl implements MemberService {

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;

	@Resource(name = "memberLogService")
	private MemberLogService memberLogService;
	
	@Resource(name="memberMapper")
	private MemberMapper memberDAO;

	private Logger logger = LoggerFactory.getLogger("usrMember");

	/**
	 * 회원약관
	 * @param param
	 * @return
	 */
	public DataMap getMemberClause(Map<String, Object> param) {
		return memberDAO.getMemberClause(param);
	}
    
    /**
     * 중복확인
	 * @param param
	 * @return
     */
    public int getDuplicateCount(Map<String, Object> param){
    	return memberDAO.getDuplicateCount(param);
    }
	
    /**
     * 전체 목록 수
	 * @param mapFlag
	 * @param param
	 * @return
	 */
    @Override
	public int getTotalCount(Map<String, Object> param) {
    	return memberDAO.getTotalCount(param);
    }

	/**
	 * 전체 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(Map<String, Object> param) {
		return memberDAO.getList(param);
	}
    /**
     * 전체 목록 수
	 * @param mapFlag
	 * @param param
	 * @return
	 */
    @Override
	public int getTotTotalCount(Map<String, Object> param) {
    	return memberDAO.getTotTotalCount(param);
    }

	/**
	 * 전체 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getTotSearchList(Map<String, Object> param) {
		return memberDAO.getTotSearchList(param);
	}

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getView(Map<String, Object> param) {
		DataMap viewDAO = memberDAO.getView(param);
		return viewDAO;
	}

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getModify(Map<String, Object> param) {
		DataMap viewDAO = memberDAO.getModify(param);
		return viewDAO;
	}

	@Override
	public List<Object> getMemberGrupList(Map<String, Object> param) {
		return memberDAO.getMemberGrupList(param);
		
	}
    
	/**
	 * 기본정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param Map 등록정보
	 * @return result 등록결과
	 * @throws Exception
	 */
	@Override
	public int insert(String regiIp, boolean isUseNameAuth, int useRsa, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		
		//고유아이디 셋팅
		String memberIdx = memberDAO.getNextId(null);//idgenService.getNextStringId();

		// 항목설정으로 저장항목 setting
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(null, parameterMap, items, itemOrder);

		if(dataMap == null || dataMap.size() == 0) return -1;
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);

		JSONObject mbrIdItem = JSONObjectUtil.getJSONObject(items, "mbrId");	// mbrId 항목 설정 정보
		JSONObject mbrNameItem = JSONObjectUtil.getJSONObject(items, "mbrName");	// mbrName 항목 설정 정보
		JSONObject mbrEmailItem = JSONObjectUtil.getJSONObject(items, "mbrEmail");	// mbrName 항목 설정 정보

		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		
		String sAuthType = null;
		String sName = null;
		String sBirthDate = null;
		String sGender = null;
		String sNationalInfo = null;
		String sDupInfo = null;
		String sConnInfo = null;
		
		if(isUseNameAuth)
		{
			if(session.getAttribute("iSAuthType") != null) sAuthType = StringUtil.getString(session.getAttribute("iSAuthType"));
			if(session.getAttribute("iSName") != null) sName = StringUtil.getString(session.getAttribute("iSName"));
			if(session.getAttribute("iSBirthDate") != null) sBirthDate = StringUtil.getString(session.getAttribute("iSBirthDate"));
			if(session.getAttribute("iSGender") != null) sGender = StringUtil.getString(session.getAttribute("iSGender"));
			if(session.getAttribute("iSNationalInfo") != null) sNationalInfo = StringUtil.getString(session.getAttribute("iSNationalInfo"));
			if(session.getAttribute("iSDupInfo") != null) sDupInfo = StringUtil.getString(session.getAttribute("iSDupInfo"));
			if(session.getAttribute("iSConnInfo") != null) sConnInfo = StringUtil.getString(session.getAttribute("iSConnInfo"));

			dataList.add(new DTForm("CHECK_TYPE", sAuthType));
			dataList.add(new DTForm("MEMBER_NAME", ModuleUtil.getParamToDBValue(mbrNameItem, sName)));
			dataList.add(new DTForm("MEMBER_BIRTH", DataSecurityUtil.getSeedEncrypt(sBirthDate)));
			dataList.add(new DTForm("MEMBER_SEX", sGender));
			dataList.add(new DTForm("MEMBER_NAT", sNationalInfo));
			dataList.add(new DTForm("MEMBER_DI", DataSecurityUtil.getSeedEncrypt(sDupInfo)));
			dataList.add(new DTForm("MEMBER_CI", DataSecurityUtil.getSeedEncrypt(sConnInfo)));
		}
		
		String regiIdx = null;
		String regiId = null;
		String regiName = null;
		String dbMbrId = null;
		String dbMbrName = null;
		
		if(UserDetailsHelper.isLogin())
		{
			regiIdx = loginVO.getMemberIdx();
			regiId = loginVO.getMemberId();
			regiName = loginVO.getMemberName();
		}
		else
		{
			regiIdx = memberIdx;
			regiId = DataSecurityUtil.getRSADecrypt(useRsa, parameterMap.getString("mbrId"));
			if(isUseNameAuth) regiName = sName;
			else regiName = DataSecurityUtil.getRSADecrypt(useRsa, parameterMap.getString("mbrName"));

			dbMbrId = ModuleUtil.getParamToDBValue(mbrIdItem, regiId);	// RSA 및 암호화 설정에 따른 값 얻기
			regiId = dbMbrId;

			dbMbrName = ModuleUtil.getParamToDBValue(mbrNameItem, regiName);	// RSA 및 암호화 설정에 따른 값 얻기
			regiName= dbMbrName;
		}
		
		// 저장 항목
    	dataList.add(new DTForm("MEMBER_IDX", memberIdx));
    	
    	dataList.add(new DTForm("REGI_IDX", regiIdx));
    	dataList.add(new DTForm("REGI_ID", regiId));
    	dataList.add(new DTForm("REGI_NAME", regiName));
    	dataList.add(new DTForm("REGI_IP", regiIp));
    	
    	dataList.add(new DTForm("LAST_MODI_IDX", regiIdx));
    	dataList.add(new DTForm("LAST_MODI_ID", regiId));
    	dataList.add(new DTForm("LAST_MODI_NAME", regiName));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));

    	param.put("dataList", dataList);
    	
		int result = memberDAO.insert(param);
		
		if(result > 0) {
			// sns로그인 사용여부
			int useSns = RbsProperties.getPropertyInt("Globals.sns.login.use");
			if(useSns == 1) {
				SnsLoginVO snsLoginVO = (SnsLoginVO)session.getAttribute("iSnsLoginVO");
				if(snsLoginVO != null && !StringUtil.isEmpty(snsLoginVO.getSnsType()) && !StringUtil.isEmpty(snsLoginVO.getSnsId())) {

					List<DTForm> searchList = new ArrayList<DTForm>();
					searchList.add(new DTForm("MEMBER_IDX", memberIdx));
					searchList.add(new DTForm("SNS_TYPE", snsLoginVO.getSnsType()));
					
					dataList = new ArrayList<DTForm>();
			    	/*dataList.add(new DTForm("MEMBER_IDX", memberIdx));
					dataList.add(new DTForm("SNS_TYPE", snsLoginVO.getSnsType()));*/
					dataList.add(new DTForm("SNS_ID", snsLoginVO.getSnsId()));

			    	dataList.add(new DTForm("REGI_IDX", regiIdx));
			    	dataList.add(new DTForm("REGI_ID", regiId));
			    	dataList.add(new DTForm("REGI_NAME", regiName));
			    	dataList.add(new DTForm("REGI_IP", regiIp));
			    	
			    	param.put("dataList", dataList);
			    	param.put("searchList", searchList);
			    	
			    	result = memberDAO.snsInsert(param);
				}
			}
			
			// 사용자그룹 등록
			String[] mbrGrps = StringUtil.getStringArray(parameterMap.get("mbrGrp"));
			int result1 = 0;
			if(mbrGrps != null) {
				int grpCdLen = mbrGrps.length;
				for(int i = 0 ; i < grpCdLen ; i ++) {
					param = new HashMap<String, Object>();
					dataList = new ArrayList<DTForm>();
					
			    	dataList.add(new DTForm("MEMBER_IDX", memberIdx));
			    	dataList.add(new DTForm("GROUP_CODE", mbrGrps[i]));
			    	param.put("dataList", dataList);
			    	
			    	result1 = memberDAO.insertGrp(param);
				}
			}
			
			memberLogService.setEprivacy(logger, rbsMessageSource.getMessage("message.member.log.insert"), null, null, items, memberIdx, regiIdx, regiId, regiName);
			/*
			regiId = ModuleUtil.getPrivDecValue(mbrIdItem, regiId);
			regiName = ModuleUtil.getPrivDecValue(mbrNameItem, regiName);

			Map<String, Object> searchParam = new HashMap<String, Object>();
			List searchList = new ArrayList<DTForm>();
			searchList.add(new DTForm("A.MEMBER_IDX", memberIdx));
			searchParam.put("searchList", searchList);
			
			DataMap dt = memberDAO.getView(searchParam);
			
			String mbrId = ModuleUtil.getPrivDecValue(mbrIdItem, StringUtil.getString(dt.get("MEMBER_ID")));
			String mbrName = ModuleUtil.getPrivDecValue(mbrNameItem, StringUtil.getString(dt.get("MEMBER_NAME")));
			String mbrEmail = ModuleUtil.getPrivDecValue(mbrEmailItem, StringUtil.getString(dt.get("MEMBER_EMAIL")));

			logger.info(mbrId + ", " + "회원가입" + ", " + regiId + ", " + regiName + ", " + DateUtil.getTimestampFormat((Timestamp)dt.get("REGI_DATE"), "yyyy-MM-dd HH:mm:ss") + ", " +  regiIp);

			MailUtil.sendMail(RbsProperties.getProperty("Globals.mail.fromMail"), RbsProperties.getProperty("Globals.mail.fromName"), 
					mbrEmail, mbrName, MessageFormat.format(RbsProperties.getProperty("Globals.mail.join.title"), new String[]{mbrName}),  
					MessageFormat.format(RbsProperties.getProperty("Globals.mail.join.content"), new String[]{mbrName, mbrId, DateUtil.getTimestampFormat((Timestamp)dt.get("REGI_DATE"), "yyyy년 MM월 dd일")}));
			*/
		}
		return result;
	}

	/**
	 * 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param param 수정정보
	 * @throws Exception
	 */
	@Override
	public int update(String submitType, String mbrCd, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		searchList.add(new DTForm("MEMBER_IDX", mbrCd));
		
		// 항목설정으로 저장항목 setting
		//commandMap.put("mbrId", loginVO.getMbrId()); - 내정보수정시에만 사용
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(null, parameterMap, items, itemOrder);
		
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// 저장 항목
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;

		String loginMemberIdOrg = null;
		String loginMemberNameOrg = null;
		String loginMemberEmailOrg = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
			loginMemberIdOrg = loginVO.getMemberIdOrg();
			loginMemberNameOrg = loginVO.getMemberNameOrg();
			loginMemberEmailOrg = loginVO.getMemberEmailOrg();
		}

    	dataList.add(new DTForm("LOGIN_FAIL", 0));
    	dataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));

    	param.put("dataList", dataList);
    	param.put("searchList", searchList);
    	
    	int result = memberDAO.update(param);
		int result1 =  0;
		if(result > 0 && !StringUtil.isEquals(submitType, "myinfo")) {
			// 사용자그룹 삭제
			param = new HashMap<String, Object>();
			searchList = new ArrayList<DTForm>();
	    	searchList.add(new DTForm("MEMBER_IDX", mbrCd));
	    	param.put("searchList", searchList);
	    	
	    	result1 = memberDAO.cdeleteGrp(param);

			// 사용자그룹 등록
			String[] mbrGrps = StringUtil.getStringArray(parameterMap.get("mbrGrp"));
			if(mbrGrps != null) {
				int grpCdLen = mbrGrps.length;
				for(int i = 0 ; i < grpCdLen ; i ++) {
					param = new HashMap<String, Object>();
					dataList = new ArrayList<DTForm>();
					
			    	dataList.add(new DTForm("MEMBER_IDX", mbrCd));
			    	dataList.add(new DTForm("GROUP_CODE", mbrGrps[i]));
			    	param.put("dataList", dataList);
			    	
			    	result1 = memberDAO.insertGrp(param);
				}
			}
/*
    		String mbrId = loginMemberIdOrg;
    		String mbrName = loginMemberNameOrg;
    		String mbrEmail = loginMemberEmailOrg;
    		
    		param = new HashMap<String, Object>();
        	param.put("searchList", searchList);
        	DataMap dt = memberDAO.getView(param);

			logger.info(mbrId + ", " + "회원정보수정" + ", " + mbrId + ", " + mbrName + ", " + DateUtil.getTimestampFormat((Timestamp)dt.get("LAST_MODI_DATE"), "yyyy-MM-dd HH:mm:ss") + ", " +  regiIp);
        	
    		MailUtil.sendMail(RbsProperties.getProperty("Globals.mail.fromMail"), RbsProperties.getProperty("Globals.mail.fromName"), 
					mbrEmail, mbrName, MessageFormat.format(RbsProperties.getProperty("Globals.mail.myinfo.title"), new String[]{mbrName}),  
					MessageFormat.format(RbsProperties.getProperty("Globals.mail.myinfo.content"), new String[]{mbrName, mbrId, DateUtil.getDateFormat(new Date(), "yyyy년 MM월 dd일"), DateUtil.getTimestampFormat((Timestamp)dt.get("REGI_DATE"), "yyyy년 MM월 dd일")}));
*/					
		}

		memberLogService.setEprivacy(logger, rbsMessageSource.getMessage("message.member.log.modify"), null, null, items, mbrCd, loginMemberIdx, loginMemberId, loginMemberName);
		
		return result;
	}

	/**
	 * 중복확인
	 * @param param
	 * @return
	 */
	public int getSnsDuplicateCount(Map<String, Object> param){
		return memberDAO.getDuplicateCount(param);
	}
	
	/**
	 * 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param param 수정정보
	 * @throws Exception
	 */
	@Override
	public int snsInsert(String mbrCd, String regiIp, ParamForm parameterMap) throws Exception {
		String snsType = parameterMap.getString("snsType");
		String snsId = parameterMap.getString("snsId");
		
		if(StringUtil.isEmpty(snsType) || StringUtil.isEmpty(snsId)) return -1;
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		searchList.add(new DTForm("MEMBER_IDX", mbrCd));
		searchList.add(new DTForm("SNS_TYPE", snsType));

		//String snsEmail = parameterMap.getString("snsEmail");

		/*dataList.add(new DTForm("MEMBER_IDX", snsType));
		dataList.add(new DTForm("SNS_TYPE", snsType));*/
		dataList.add(new DTForm("SNS_ID", snsId));
		
		// 저장 항목
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;

		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		} else {
			loginMemberIdx = StringUtil.getString(parameterMap.get("memberIdx"));
			loginMemberId = StringUtil.getString(parameterMap.get("memberId"));
			loginMemberName = StringUtil.getString(parameterMap.get("memberName"));
		}
    	
    	dataList.add(new DTForm("REGI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("REGI_ID", loginMemberId));
    	dataList.add(new DTForm("REGI_NAME", loginMemberName));
    	dataList.add(new DTForm("REGI_IP", regiIp));

    	param.put("dataList", dataList);
    	param.put("searchList", searchList);
    	
    	int result = memberDAO.snsInsert(param);
		
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
    	return memberDAO.getDeleteCount(param);
    }

	/**
	 * 삭제 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDeleteList(Map<String, Object> param) {
		return memberDAO.getDeleteList(param);
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
		searchList.add(new DTForm("MEMBER_IDX", deleteIdxs));
		
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
		
		return memberDAO.delete(param);
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
		searchList.add(new DTForm("MEMBER_IDX", restoreIdxs));
		
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
		
		return memberDAO.restore(param);
	}

	/**
	 * 회원탈퇴 - 화면에 조회된 정보를 데이터베이스에서 삭제
	 * @param param 완전삭제정보
	 * @throws Exception
	 */
	@Override
	public int joinout(JSONObject items, String[] deleteIdxs) throws Exception {
		if(deleteIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		// 저장조건
		searchList.add(new DTForm("MEMBER_IDX", deleteIdxs));
		param.put("searchList", searchList);

		List<?> list = memberDAO.getList(param);
		String maxMemberIdx = memberDAO.getMaxMemberIdx();
		int result = memberDAO.cdelete(param);
		if(result > 0){
			LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
			
			String regiIdx = null;
			String regiId = null;
			String regiName = null;
			if(loginVO != null){
				regiIdx = loginVO.getMemberIdx();
				regiId = loginVO.getMemberIdOrg();
				regiName = loginVO.getMemberNameOrg();
			}		

			int listLen = 0;
			if(list != null) {
				DataMap deleteMaxDt = (DataMap)list.get(0);
				if(StringUtil.isEquals(deleteMaxDt.get("MEMBER_IDX"), maxMemberIdx)) {
					// 마지막 memberIdx가 삭제된 경우 - 빈 data 등록 (자신 정보 가져오는 키가 잘 못 매칭되는 것 방지)
					param = new HashMap<String, Object>();
					List<DTForm> dataList = new ArrayList<DTForm>();
					
					// 저장조건
					dataList.add(new DTForm("MEMBER_IDX", maxMemberIdx));
					dataList.add(new DTForm("MEMBER_ID", "-"));
					dataList.add(new DTForm("MEMBER_NAME", "-"));
					dataList.add(new DTForm("MEMBER_STATE", "2"));
					dataList.add(new DTForm("ISDELETE", "2"));
					param.put("dataList", dataList);
					result = memberDAO.insert(param);
				}
				
				listLen = list.size();
				for (int i = 0; i < listLen; i++) {
					DataMap dt = (DataMap)list.get(i);
					if(dt == null) continue;
					memberLogService.setEprivacy(logger, rbsMessageSource.getMessage("message.member.log.delete"), dt, null, items, StringUtil.getString(dt.get("MEMBER_IDX")), regiIdx, regiId, regiName);
				}
			}
		}
		
		return result;
	}

	/**
	 * 화면에 조회된 정보를 데이터베이스에서 삭제
	 * @param param 완전삭제정보
	 * @throws Exception
	 */
	@Override
	public int cdelete(JSONObject items, String[] deleteIdxs) throws Exception {
		if(deleteIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		// 저장조건
		searchList.add(new DTForm("MEMBER_IDX", deleteIdxs));
		param.put("searchList", searchList);

		List<?> list = memberDAO.getDeleteList(param);
		String maxMemberIdx = memberDAO.getMaxMemberIdx();
		int result = memberDAO.cdelete(param);
		if(result > 0){
			LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
			
			String regiIdx = null;
			String regiId = null;
			String regiName = null;
			if(loginVO != null){
				regiIdx = loginVO.getMemberIdx();
				regiId = loginVO.getMemberIdOrg();
				regiName = loginVO.getMemberNameOrg();
			}		

			int listLen = 0;
			if(list != null) {
				DataMap deleteMaxDt = (DataMap)list.get(0);
				if(StringUtil.isEquals(deleteMaxDt.get("MEMBER_IDX"), maxMemberIdx)) {
					// 마지막 memberIdx가 삭제된 경우 - 빈 data 등록 (자신 정보 가져오는 키가 잘 못 매칭되는 것 방지)
					param = new HashMap<String, Object>();
					List<DTForm> dataList = new ArrayList<DTForm>();
					
					// 저장조건
					dataList.add(new DTForm("MEMBER_IDX", maxMemberIdx));
					dataList.add(new DTForm("MEMBER_ID", "-"));
					dataList.add(new DTForm("MEMBER_NAME", "-"));
					dataList.add(new DTForm("MEMBER_STATE", "2"));
					dataList.add(new DTForm("ISDELETE", "2"));
					param.put("dataList", dataList);
					result = memberDAO.insert(param);
				}
				
				listLen = list.size();
				for (int i = 0; i < listLen; i++) {
					DataMap dt = (DataMap)list.get(i);
					if(dt == null) continue;
					memberLogService.setEprivacy(logger, rbsMessageSource.getMessage("message.member.log.delete"), dt, null, items, StringUtil.getString(dt.get("MEMBER_IDX")), regiIdx, regiId, regiName);
				}
			}
			/*
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
			
			
			JSONObject mbrIdItem = JSONObjectUtil.getJSONObject(items, "mbrId");
			JSONObject mbrNameItem = JSONObjectUtil.getJSONObject(items, "mbrName");
			JSONObject mbrEmailItem = JSONObjectUtil.getJSONObject(items, "mbrEmail");
			
			int listLen = 0;
			if(list != null) listLen = list.size();
			for (int i = 0; i < listLen; i++) {
				DataMap dt = (DataMap)list.get(i);
				
				String mbrId = ModuleUtil.getPrivDecValue(mbrIdItem, StringUtil.getString(dt.get("MEMBER_ID")));
				String mbrName = ModuleUtil.getPrivDecValue(mbrNameItem, StringUtil.getString(dt.get("MEMBER_NAME")));
				String mbrEmail = ModuleUtil.getPrivDecValue(mbrEmailItem, StringUtil.getString(dt.get("MEMBER_EMAIL")));
				Date today = new Date();
				
				logger.info(mbrId + ", " + "회원탈퇴" + ", " + regiId + ", " + regiName + ", " + DateUtil.getDateFormat(today, "yyyy-MM-dd HH:mm:ss") + ", " +  request.getRemoteAddr());

				MailUtil.sendMail(RbsProperties.getProperty("Globals.mail.fromMail"), RbsProperties.getProperty("Globals.mail.fromName")
						, mbrEmail, mbrName, MessageFormat.format(RbsProperties.getProperty("Globals.mail.joinout.title"), new String[]{mbrName})
						, MessageFormat.format(RbsProperties.getProperty("Globals.mail.joinout.content"), new String[]{mbrName, mbrId, DateUtil.getDateFormat(today, "yyyy년 MM월 dd일"), DateUtil.getTimestampFormat((Timestamp)dt.get("REGI_DATE"), "yyyy년 MM월 dd일")}));
			}
			*/
		}
		
		return result;
	}
	
	@Override
	public int pwupdate(int useRSA, ParamForm parameterMap, JSONObject items) throws Exception {
		if(JSONObjectUtil.isEmpty(items)) return -1;

		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();

		String mbrId = null;
		String mbrName = null;
		String mbrEmail = null;
		
		JSONObject mbrIdItem = JSONObjectUtil.getJSONObject(items, "mbrId");	// mbrId 항목 설정 정보
		JSONObject mbrNameItem = JSONObjectUtil.getJSONObject(items, "mbrName");	// mbrName 항목 설정 정보
		JSONObject mbrEmailItem = JSONObjectUtil.getJSONObject(items, "mbrEmail");	// mbrEmail 항목 설정 정보
		
		if(PrivAuthUtil.isUseNameAuth()){
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
    		HttpSession session = request.getSession();
    		
    		String sDupInfo = StringUtil.getString(session.getAttribute("iSDupInfo"));

    		searchList.add(new DTForm("MEMBER_DI", DataSecurityUtil.getSeedEncrypt(sDupInfo)));
		}
		else{
			mbrId = DataSecurityUtil.getRSADecrypt(useRSA, parameterMap.getString("mbrId"));
			String dbMbrId = ModuleUtil.getParamToDBValue(mbrIdItem, mbrId);	// RSA 및 암호화 설정에 따른 값 얻기
			
			mbrName = DataSecurityUtil.getRSADecrypt(useRSA, parameterMap.getString("mbrName"));
			String dbMbrName = ModuleUtil.getParamToDBValue(mbrNameItem, mbrName);	// RSA 및 암호화 설정에 따른 값 얻기

			mbrEmail = DataSecurityUtil.getRSADecrypt(useRSA, parameterMap.getString("mbrEmail"));
			String dbMbrEmail = ModuleUtil.getParamToDBValue(mbrEmailItem, mbrEmail);	// RSA 및 암호화 설정에 따른 값 얻기
			
			searchList.add(new DTForm("MEMBER_ID", dbMbrId));
			searchList.add(new DTForm("MEMBER_NAME", dbMbrName));
			searchList.add(new DTForm("MEMBER_EMAIL", dbMbrEmail));
			searchList.add(new DTForm("USERTYPE_IDX", RbsProperties.getProperty("Globals.code.USERTYPE_ADMIN"), "<"));
		}

    	param.put("searchList", searchList);
    		
		DataMap dt = memberDAO.getView(param);
		
		int result = 0;
    	
		if(dt != null)
    	{
			String columnId = "column_id";
			String mbrIdColumn = JSONObjectUtil.getString(mbrIdItem, columnId);
			mbrId = ModuleUtil.getPrivDecValue(mbrIdItem, StringUtil.getString(dt.get(mbrIdColumn)));
			
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
    		// 변경할 임시 비밀번호
    		String changePw = StringUtil.changePw();

    		JSONObject mbrPwdItem = JSONObjectUtil.getJSONObject(items, "mbrPwd");	// mbrPwd 항목 설정 정보
    		String encPwd = ModuleUtil.getParamPWToDBValue(useRSA, mbrPwdItem, changePw, mbrId);
    		
    		// 저장 항목
    		dataList.add(new DTForm(JSONObjectUtil.getString(mbrPwdItem, columnId), encPwd));
    		
        	param.put("dataList", dataList);
        	
        	result = memberDAO.pwupdate(param);
    		
    		if(result > 0) {
				if(StringUtil.isEmpty(mbrEmail)) mbrEmail = ModuleUtil.getPrivDecValue(mbrEmailItem, StringUtil.getString(dt.get("MEMBER_EMAIL")));
    			request.setAttribute("pwsearchMbrEmail", mbrEmail);
    			
    			String mbrIdx = StringUtil.getString(dt.get("MEMBER_IDX"));
    			memberLogService.setEprivacy(logger, rbsMessageSource.getMessage("message.member.log.pwsearch"), null, changePw, items, mbrIdx, mbrIdx, StringUtil.getString(dt.get(mbrIdColumn)), StringUtil.getString(dt.get(JSONObjectUtil.getString(JSONObjectUtil.getJSONObject(items, "mbrName"), columnId))));
    			/*
    			logger.info(mbrId + ", " + "비밀번호 찾기" + ", " + mbrId + ", " + mbrName + ", " + DateUtil.getDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss") + ", " +  request.getRemoteAddr());
    			
    			MailUtil.sendMail(RbsProperties.getProperty("Globals.mail.fromMail"), RbsProperties.getProperty("Globals.mail.fromName")
    					, mbrEmail, mbrName, MessageFormat.format(RbsProperties.getProperty("Globals.mail.password.title"), new String[]{mbrName})
    					, MessageFormat.format(RbsProperties.getProperty("Globals.mail.password.content"), new String[]{mbrName, mbrId, changePw, DateUtil.getDateFormat(new Date(), "yyyy년 MM월 dd일"), DateUtil.getTimestampFormat((Timestamp)dt.get("REGI_DATE"), "yyyy년 MM월 dd일")}));
    			*/
    		}
    	}
    	
		return result;
	}

	@Override
	public int pwupdate1(String pwdModiType, String mbrCd, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		searchList.add(new DTForm("MEMBER_IDX", mbrCd));
    	
		String columnName = null;
		if(!StringUtil.isEquals(pwdModiType, "1"))
		{
			// 항목설정으로 저장항목 setting
			HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(null, parameterMap, items, itemOrder);
			
			if(dataMap == null || dataMap.size() == 0) return -1;
			
			List itemDataList = StringUtil.getList(dataMap.get("dataList"));
			if(itemDataList != null) dataList.addAll(itemDataList);
			
			columnName = "";
			dataList.add(new DTForm("PWD_MODI_TYPE", "0"));
		}
		else
		{
			columnName = "2";
			dataList.add(new DTForm("PWD_MODI_TYPE", "1"));
		}
		// 저장 항목
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;

		String loginMemberIdOrg = null;
		String loginMemberNameOrg = null;
		String loginMemberEmailOrg = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
			loginMemberIdOrg = loginVO.getMemberIdOrg();
			loginMemberNameOrg = loginVO.getMemberNameOrg();
			loginMemberEmailOrg = loginVO.getMemberEmailOrg();
		}
    	
    	dataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));
    	
    	dataList.add(new DTForm("PWD_MODI_IDX" + columnName, loginMemberIdx));
    	dataList.add(new DTForm("PWD_MODI_ID" + columnName, loginMemberId));
    	dataList.add(new DTForm("PWD_MODI_NAME" + columnName, loginMemberName));
    	dataList.add(new DTForm("PWD_MODI_IP" + columnName, regiIp));

    	param.put("dataList", dataList);
    	param.put("searchList", searchList);
    	param.put("columnName", columnName);
    	
    	int result = memberDAO.pwupdate1(param);
    	if(result > 0){
    		
    		String mbrId = loginMemberIdOrg;
    		String mbrName = loginMemberNameOrg;
    		String mbrEmail = loginMemberEmailOrg;
    		
    		param = new HashMap<String, Object>();
        	param.put("searchList", searchList);
        	DataMap dt = memberDAO.getView(param);

			memberLogService.setEprivacy(logger, rbsMessageSource.getMessage("message.member.log.modify"), null, null, items, mbrCd, loginMemberIdx, loginMemberId, loginMemberName);
    		
    		/*MailUtil.sendMail(RbsProperties.getProperty("Globals.mail.fromMail"), RbsProperties.getProperty("Globals.mail.fromName"), 
					mbrEmail, mbrName, MessageFormat.format(RbsProperties.getProperty("Globals.mail.myinfo.title"), new String[]{mbrName}),  
					MessageFormat.format(RbsProperties.getProperty("Globals.mail.myinfo.content"), new String[]{mbrName, mbrId, DateUtil.getThisDate("yyyy년 MM월 dd일"), DateUtil.getTimestampFormat((Timestamp)dt.get("REGI_DATE"), "yyyy년 MM월 dd일")}));*/
    	}
		
		return result;
	}
	
	@Override
	public boolean isMbrPwdMatched(int useRSA, ParamForm parameterMap, JSONObject items) throws Exception {
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		String mbrCd = null;
		String mbrId = null;
		if(loginVO != null) {
			mbrCd = loginVO.getMemberIdx();
			mbrId = loginVO.getMemberIdOrg();
		}
		
		String mbrPwd = parameterMap.getString("pre_mbrPwd");
		JSONObject mbrPwdItem = JSONObjectUtil.getJSONObject(items, "mbrPwd");	// mbrPwd 항목 설정 정보
		String encPwd = ModuleUtil.getParamPWToDBValue(useRSA, mbrPwdItem, mbrPwd, mbrId);
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.MEMBER_IDX", mbrCd));
		searchList.add(new DTForm("A.MEMBER_PWD", encPwd));
		
		param.put("searchList", searchList);
		
		// 비밀번호 일치하는지 확인
		int result = memberDAO.getTotalCount(param);
		
		return result > 0;
	}

	@Override
	public List<Object> getReAgreeList(String dDays){
		return memberDAO.getReAgreeList(dDays);
	}
	
	@Override
	public int reAgreeCdelete() throws Exception {
		String regAgreeMaxMemberIdx = memberDAO.getReAgreeMaxMemberIdx();
		String maxMemberIdx = memberDAO.getMaxMemberIdx();
		int result = memberDAO.reAgreeCdelete();
		if(result > 0) {
			if(StringUtil.isEquals(regAgreeMaxMemberIdx, maxMemberIdx)) {

				Map<String, Object> param = new HashMap<String, Object>();
				List<DTForm> dataList = new ArrayList<DTForm>();
				
				// 저장조건
				dataList.add(new DTForm("MEMBER_IDX", maxMemberIdx));
				dataList.add(new DTForm("MEMBER_ID", "-"));
				dataList.add(new DTForm("MEMBER_NAME", "-"));
				dataList.add(new DTForm("MEMBER_STATE", "2"));
				dataList.add(new DTForm("ISDELETE", "2"));
				param.put("dataList", dataList);
				result = memberDAO.insert(param);
			}
		}
		return result;
	}
}