package rbs.modules.member.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import rbs.egovframework.LoginVO;
import rbs.egovframework.com.utl.slm.RbsHttpSessionBindingListener;
import rbs.modules.member.mapper.LoginMapper;
import rbs.modules.member.service.LoginService;
import rbs.usr.main.service.MainService;


/**
 * 일반 로그인, 인증서 로그인을 처리하는 비즈니스 구현 클래스
 * @author 공통서비스 개발팀 박지욱
 * @since 2009.03.06
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2009.03.06  박지욱          최초 생성
 *  2011.08.26  서준식          EsntlId를 이용한 로그인 추가
 *  2014.12.08	이기하			암호화방식 변경(EgovFileScrty.encryptPassword)
 *  </pre>
 */
@Service("rbsLoginService")
public class LoginServiceImpl extends EgovAbstractServiceImpl implements LoginService {

    @Resource(name="loginMapper")
    private LoginMapper loginDAO;
    
    @Resource(name = "usrMainService")
	private MainService mainService;

    /**
	 * 로그인 처리
	 * @param siteMode
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
    @Override
	public LoginVO login(String siteMode, String regiIp, ParamForm parameterMap, JSONObject itemInfo, JSONObject settingInfo, JSONObject memberItemInfo) throws Exception {

		Map<String, Object> param = new HashMap<String, Object>();

		String mbrId = parameterMap.getString("mbrId");
		String mbrPwd = parameterMap.getString("mbrPwd");
		
    	// 1. 입력한 비밀번호를 암호화한다.
		int useRSA = JSONObjectUtil.getInt(settingInfo, "use_rsa");
		
		JSONObject mbrIdItem = JSONObjectUtil.getJSONObject(JSONObjectUtil.getJSONObject(itemInfo, "items"), "mbrId");
		String encId = ModuleUtil.getParamToDBValue(useRSA, mbrIdItem, mbrId);
		
		JSONObject mbrPwdItem = JSONObjectUtil.getJSONObject(JSONObjectUtil.getJSONObject(itemInfo, "items"), "mbrPwd");
		String encPwd = ModuleUtil.getParamPWToDBValue(useRSA, mbrPwdItem, mbrPwd, mbrId);
		
		param.put("memberId", encId);
		System.out.println("!!!!!!!!!!!!!!" + mbrPwd);
		System.out.println("!!!!!!!!!!!!!!" + encPwd);
		
		// param.put("memberPwd", encPwd);
		
    	// 2. 아이디와 암호화된 비밀번호가 DB와 일치하는지 확인한다.
		LoginVO loginVO = loginDAO.login(siteMode, param);
		
		String dbPwd = (loginVO != null)?loginVO.getMemberPwd():null;
		
		// 3. 로그인 정보가 없으면 로그인 실패 카운트 증가 
		//if(loginVO == null){
		if(StringUtil.isEmpty(encPwd) || StringUtil.isEmpty(dbPwd) || !StringUtil.isEquals(encPwd, dbPwd)) {
			
			if(loginVO != null) loginVO.setLogin(false);
			param = new HashMap<String, Object>();
			
			List<DTForm> dataList = new ArrayList<DTForm>();
			List<DTForm> searchList = new ArrayList<DTForm>();
			
			dataList.add(new DTForm("LAST_LOGIN_FAIL_IP", regiIp));
			
			searchList.add(new DTForm("MEMBER_ID", encId));
			
	    	param.put("dataList", dataList);
	    	param.put("searchList", searchList);

			loginDAO.loginFail(param);
		} else if(loginVO != null) {
			// 로그인한 경우
			loginVO.setLogin(true);

			// 암/복호화 여부에 따른 원본명 저장
			JSONObject items = JSONObjectUtil.getJSONObject(memberItemInfo, "items");
			JSONObject item = null;
			
			// 아이디
			item = JSONObjectUtil.getJSONObject(items, "mbrId");
			loginVO.setMemberIdOrg(ModuleUtil.getPrivDecValue(item, loginVO.getMemberId()));
	
			// 이름
			item = JSONObjectUtil.getJSONObject(items, "mbrName");
			loginVO.setMemberNameOrg(ModuleUtil.getPrivDecValue(item, loginVO.getMemberName()));
			
			// 이메일
			item = JSONObjectUtil.getJSONObject(items, "mbrEmail");
			loginVO.setMemberEmailOrg(ModuleUtil.getPrivDecValue(item, loginVO.getMemberEmail()));
	
			// 그룹정보
			param = new HashMap<String, Object>();
			param.put("memberIdx", loginVO.getMemberIdx());    	
			List<Object> groupList = loginDAO.getGroupList(param);
			loginVO.setGroupList(groupList);
		}
		
    	return loginVO;
    }

    /**
	 * SNS 로그인 처리
	 * @param siteMode
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
    @Override
	public LoginVO snsLogin(String regiIp, ParamForm parameterMap, JSONObject memberItemInfo) throws Exception {

		Map<String, Object> param = new HashMap<String, Object>();

		String snsType = parameterMap.getString("snsType");
		String snsId = parameterMap.getString("snsId");
		//String snsName = parameterMap.getString("name");
		String snsEmail = parameterMap.getString("snsEmail");

		param.put("snsType", snsType);
		param.put("snsId", snsId);
		//param.put("snsName", snsName);
		param.put("snsEmail", snsEmail);
		
    	// 해당 SNS의 아이디 OR 이메일이 DB와 일치하는지 확인한다.
		LoginVO loginVO = loginDAO.login("sns", param);

		if(loginVO != null){
			// 암/복호화 여부에 따른 원본명 저장
			JSONObject items = JSONObjectUtil.getJSONObject(memberItemInfo, "items");
			JSONObject item = null;
			
			// 아이디
			item = JSONObjectUtil.getJSONObject(items, "mbrId");
			loginVO.setMemberIdOrg(ModuleUtil.getPrivDecValue(item, loginVO.getMemberId()));
	
			// 이름
			item = JSONObjectUtil.getJSONObject(items, "mbrName");
			loginVO.setMemberNameOrg(ModuleUtil.getPrivDecValue(item, loginVO.getMemberName()));
			
			// 이메일
			item = JSONObjectUtil.getJSONObject(items, "mbrEmail");
			loginVO.setMemberEmailOrg(ModuleUtil.getPrivDecValue(item, loginVO.getMemberEmail()));
	
			// 그룹정보
			param = new HashMap<String, Object>();
			param.put("memberIdx", loginVO.getMemberIdx());    	
			List<Object> groupList = loginDAO.getGroupList(param);
			loginVO.setGroupList(groupList);
		}
    	return loginVO;
    }

    /**
	 * 사용자 그룹정보 얻기
     * @param siteMode
     * @param mbrCd
     * @return
     * @throws Exception
     */
    @Override
	public List<Object> getGroupList(String memberIdx) throws Exception {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("memberIdx", memberIdx);    	
		List<Object> groupList = loginDAO.getGroupList(param);
    	return groupList;
    }
    

	@Override
	public int loginUpdate(String mbrCd, String regiIp) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		
		List<DTForm> dataList = new ArrayList<DTForm>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		dataList.add(new DTForm("LAST_LOGIN_IP", regiIp));
		//dataList.add(new DTForm("LOGIN_FAIL", 0));
		
		searchList.add(new DTForm("MEMBER_IDX", mbrCd));
		
    	param.put("dataList", dataList);
    	param.put("searchList", searchList);

		int result = loginDAO.loginUpdate(param);
		
		return result;
	}
	
	public void loginProc(LoginVO resultVO) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
		session = request.getSession(true);
		// 2-1. 로그인 정보를 세션에 저장
		session.setAttribute("loginVO", resultVO);
		
		// 중복 로그인 방지
		RbsHttpSessionBindingListener listener = RbsHttpSessionBindingListener.INSTANCE;
		session.setAttribute(resultVO.getMemberId(), listener);
		
	}
	
	public void logout(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		if(loginVO != null){
			String loginMemberId = loginVO.getMemberId();
			if(!StringUtil.isEmpty(loginMemberId)) session.setAttribute(loginMemberId, null);
		}
		/*session.setAttribute("loginVO", null);
		session.setAttribute("selSiteVO", null);*/
		session.invalidate();
	}
}
