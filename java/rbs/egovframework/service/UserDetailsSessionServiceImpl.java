package rbs.egovframework.service;

import java.util.ArrayList;
import java.util.List;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.service.UserDetailsService;

import rbs.egovframework.LoginVO;

public class UserDetailsSessionServiceImpl extends EgovAbstractServiceImpl implements UserDetailsService {

	/**
	 * 로그인 사용자 정보
	 */
	@Override
	public Object getAuthenticatedUser() {
		if (RequestContextHolder.getRequestAttributes() == null) {
			return null;
		}

		return RequestContextHolder.getRequestAttributes().getAttribute("loginVO", RequestAttributes.SCOPE_SESSION);

	}

	/**
	 * 회원그룹목록
	 */
	@Override
	//@SuppressWarnings("unchecked")
	public List<Object> getAuthorities() {

		// 권한 설정을 리턴한다. : GRUP_INFO
		Object loginVOObj = getAuthenticatedUser();
		if (loginVOObj == null) {
			return null;
		}

		LoginVO loginVO = (LoginVO) loginVOObj;
		return loginVO.getGroupList();
		/*int usertypeIdx = loginVO.getUsertypeIdx();
		Object authObj = RequestContextHolder.getRequestAttributes().getAttribute("SGroupIdxs", RequestAttributes.SCOPE_SESSION);
		if(authObj != null) return (List<String>) authObj;
		
		List<String> listAuth = new ArrayList<String>();
		return listAuth;*/
	}
	
	/**
	 * 로그인 여부 체크
	 * @return
	 */
	@Override
	public Boolean isLogin() {
		if (RequestContextHolder.getRequestAttributes() == null) {
			return false;
		} else {

			Object loginVOObj = getAuthenticatedUser();//RequestContextHolder.getRequestAttributes().getAttribute("loginVO", RequestAttributes.SCOPE_SESSION);
			if (loginVOObj == null) {
				return false;
			}

			LoginVO loginVO = (LoginVO) loginVOObj;
			int usertypeIdx = loginVO.getUsertypeIdx();
			int nmMbrType = RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
			if(usertypeIdx <= nmMbrType) return false;
		}
		
		return true;
	}
	
	/**
	 * 비회원 본인인증 여부 체크
	 * @return
	 */
	@Override
	public Boolean isNmAuth() {
		if (RequestContextHolder.getRequestAttributes() == null) {
			return false;
		} else {

			Object loginVOObj = getAuthenticatedUser();//RequestContextHolder.getRequestAttributes().getAttribute("loginVO", RequestAttributes.SCOPE_SESSION);
			if (loginVOObj == null) {
				return false;
			}

			LoginVO loginVO = (LoginVO) loginVOObj;
			int usertypeIdx = loginVO.getUsertypeIdx();
			int nmMbrType = RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
			if(usertypeIdx == nmMbrType) return true;
		}
		
		return false;
	}
	
	/**
	 * 로그인, 본인인증 여부 체크 한다.
	 * @return
	 */
	@Override
	public Boolean isLoginAuth() {
		if (RequestContextHolder.getRequestAttributes() == null) {
			return false;
		} else {

			Object loginVOObj = getAuthenticatedUser();//RequestContextHolder.getRequestAttributes().getAttribute("loginVO", RequestAttributes.SCOPE_SESSION);
			if (loginVOObj == null) {
				return false;
			}

			LoginVO loginVO = (LoginVO) loginVOObj;
			int usertypeIdx = loginVO.getUsertypeIdx();
			int nmMbrType = RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
			if(usertypeIdx >= nmMbrType) return true;
		}
		
		return false;
	}
	
	/**
	 * 관리자 회원 여부
	 */
	@Override
	public Boolean isAdmMbrType(){
		Object loginVOObj = null;
		if (RequestContextHolder.getRequestAttributes() == null) {
			return false;
		} else {

			loginVOObj = RequestContextHolder.getRequestAttributes().getAttribute("loginVO", RequestAttributes.SCOPE_SESSION);
			if (loginVOObj == null) {
				return false;
			}
		}

		LoginVO loginVO = (LoginVO) loginVOObj;
		
		int admMbrType = RbsProperties.getPropertyInt("Globals.code.USERTYPE_ADMIN");//Defines.CODE_USERTYPE_ADMIN;
		int mbrType = loginVO.getUsertypeIdx();
		if(mbrType >= admMbrType) return true;
		return false;
	}
}
