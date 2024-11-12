package rbs.modules.board.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.AuthHelper;
import com.woowonsoft.egovframework.util.DataSecurityUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import rbs.egovframework.LoginVO;
import rbs.modules.board.service.BoardService;

public class BoardAuthUtil {
	private static BoardService boardService;

	static {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes()).getRequest();
		ApplicationContext act = WebApplicationContextUtils
				.getRequiredWebApplicationContext(request.getSession()
						.getServletContext());
		boardService = (BoardService) act
				.getBean("boardService");
	}
	
	/**
	 * 상세보기 페이지 비밀글 접근 권한 체크
	 * @param fnIdx
	 * @param dt
	 * @param isReply	: 답글여부
	 * @return
	 */
	public static boolean isViewSecretAuth(int fnIdx, int brdIdx, int pntIdx, boolean isReply, String regiIdx, String regiDupInfo, String regiPwd) {
		if(fnIdx <= 0 || brdIdx <= 0) return false;
		
		// 전체관리 권한 체크
		Boolean isMngAuth = AuthHelper.isAuthenticated("MNG");				// 전체관리
		if(isMngAuth) return true;
		
		// 등록자 정보 없는 경우 권한없음
		//if(StringUtil.isEmpty(regiIdx) && StringUtil.isEmpty(regiDupInfo) && StringUtil.isEmpty(regiPwd)) return false;
		
		// 전체관리권한 없는 경우 : 자신글, 자신글의 답글만 접근 가능
		
		// 1. 해당페이지가 비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 글쓰기 / 답글쓰기 권한인지 체크
		// 2. 로그인 체크
		// 3. 해당페이지가 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 / 답글쓰기 권한인지 체크
		
		// 해당글 권한 체크
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = request.getSession(true);
		
		String chkAuthName = null;
		if(isReply) {
			// 답글인 경우
			chkAuthName = "RWT";
		}
		
		if(StringUtil.isEmpty(chkAuthName)) chkAuthName = "WRT";
		
		// 1. 해당페이지가 비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 글쓰기 / 답글쓰기 권한인지 체크
		boolean isNoMemberPage = AuthHelper.isNoMemberAuthPage(chkAuthName);

		if(!isReply) {
			// 답글이 아닌 경우
			if(isNoMemberPage) {
				// 해당페이지가 비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 글쓰기 / 답글쓰기 권한인 경우  : 인증 비밀번호 체크
				
				// 해당글에 비밀번호 등록 안 된 경우 : 접근권한 없음
				if(StringUtil.isEmpty(regiPwd)) return false;
				
				// 해당글의 비밀번호와 인증 비밀번호(암호화) 일치 여부 체크
				String pwdSKey = "bpwd_" + fnIdx + "_" + brdIdx;
				String encPwd = StringUtil.getString(session.getAttribute(pwdSKey));
	    		// System.out.println(pwdSKey + ":" + encPwd);
				session.removeAttribute(pwdSKey);
				if(!StringUtil.isEmpty(encPwd)) {
					return (StringUtil.isEquals(encPwd, regiPwd));
				}
				return false;
			} 
			
			// 2. 로그인 체크
			boolean isLogin = UserDetailsHelper.isLogin();
			if(isLogin){
				// 로그인한 경우 : 등록자idx 체크
				
				// 해당글에 등록자idx 등록 안 된 경우 : 접근권한 없음
				if(StringUtil.isEmpty(regiIdx)) return false;
				
				// 해당글의 등록자idx와 로그인회원idx 일치 여부 체크
				String memberIdx = null;
				
				LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
				if(loginVO != null) memberIdx = loginVO.getMemberIdx();
	
				return (StringUtil.isEquals(memberIdx, regiIdx)); 
			}
			
	
			// 3. 해당페이지가 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 / 답글쓰기 권한인지 체크
			boolean isNm = (!isNoMemberPage) && AuthHelper.getModuleUTP(chkAuthName) <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
			
			if(isNm) {
				// 해당페이지가 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 / 답글쓰기 권한인 경우  : 본인인증 중복키 체크
				
				// 해당글에 본인인증 중복키 등록 안 된 경우 : 접근권한 없음
				if(StringUtil.isEmpty(regiDupInfo)) return false;
				
				// 해당글의 본인인증 중복키와 인증 중복키 일치 여부 체크
				String sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));
				
				return (StringUtil.isEquals(sDupInfo, regiDupInfo));
			}
			return false;
		} else {
			// 답글인 경우
			// 글쓰기 권한 체크, 부모글 비교
			DataMap pntDt = null;
			String pntRegiIdx = null;
			String pntRegiDupInfo = null;
			String pntRegiPwd = null;
			if(pntIdx > 0){
				// 답글인 경우 : 부모글 정보 setting
				Map<String, Object> param = new HashMap<String, Object>();
				List<DTForm> searchList = new ArrayList<DTForm>();
				
				searchList.add(new DTForm("A.BRD_IDX", pntIdx));
				
				param.put("searchList", searchList);
				pntDt = boardService.getView(fnIdx, param);
				
				if(pntDt == null) return false;
				
				pntRegiIdx = StringUtil.getString(pntDt.get("REGI_IDX"));
				pntRegiDupInfo = StringUtil.getString(pntDt.get("MEMBER_DUP"));
				pntRegiPwd = StringUtil.getString(pntDt.get("PWD"));
			}
			if(isNoMemberPage) {
				// 해당페이지가 비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 답글쓰기 권한인 경우  : 인증 비밀번호 체크
				// 글쓰기 권한 체크				
				
				String chkAuthName2 = "WRT";
				boolean isNoMemberWRT = AuthHelper.isNoMemberAuthPage(chkAuthName2);
				if(isNoMemberWRT) {
					//  비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 글쓰기
					
					if(StringUtil.isEmpty(regiPwd) && StringUtil.isEmpty(pntRegiPwd)) return false;

					// 해당글, 부모글의 비밀번호와 인증 비밀번호(암호화) 일치 여부 체크
					String pwdSKey = "bpwd_" + fnIdx + "_" + brdIdx;
					String encPwd = StringUtil.getString(session.getAttribute(pwdSKey));
		    		// System.out.println(pwdSKey + ":" + encPwd);
					session.removeAttribute(pwdSKey);
					if(!StringUtil.isEmpty(encPwd)) {
						return (StringUtil.isEquals(encPwd, regiPwd) || StringUtil.isEquals(encPwd, pntRegiPwd));
					}
					return false;
				} else {
					// 회원, 본인인증 글쓰기
	
					// 2. 회원 글쓰기 : 로그인 체크
					boolean isLogin = UserDetailsHelper.isLogin();
					if(isLogin){
						// 로그인한 경우 : 등록자idx 체크
						
						// 부모글에 등록자idx 등록 안 된 경우 : 접근권한 없음
						if(!StringUtil.isEmpty(pntRegiIdx)) {
							// 부모글의 등록자idx와 로그인회원idx 일치 여부 체크
							String memberIdx = null;
							
							LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
							if(loginVO != null) memberIdx = loginVO.getMemberIdx();
		
							if(StringUtil.isEquals(memberIdx, pntRegiIdx)) return true;
						}
					}
	
					// 3. 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 권한인지 체크
					/*
					boolean isNm = (!isNoMemberWRT) && AuthHelper.getModuleUTP(chkAuthName2) <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
					
					if(isNm) {
						// 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 권한인 경우  : 본인인증 중복키 체크
						
						// 부모글의에 본인인증 중복키 등록 안 된 경우 : 접근권한 없음
						if(!StringUtil.isEmpty(pntRegiDupInfo)) {
							// 해당글의 본인인증 중복키와 인증 중복키 일치 여부 체크
					 */
							String sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));
							
							if(!StringUtil.isEmpty(sDupInfo) && (StringUtil.isEquals(sDupInfo, regiDupInfo) || StringUtil.isEquals(sDupInfo, pntRegiDupInfo))) return true;
					/*		
						}
					}
					*/

					if(isLogin){
						String memberIdx = null;
						
						LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
						if(loginVO != null) memberIdx = loginVO.getMemberIdx();
						
						if(StringUtil.isEquals(memberIdx, regiIdx)) return true;
					}
					
					//  부모글 정보와 로그인 정보가 같지 않은 경우 : 입력된 비밀번호와 해당글 일치여부 확인
					if(StringUtil.isEmpty(regiPwd)) return false;

					// 해당글, 부모글의 비밀번호와 인증 비밀번호(암호화) 일치 여부 체크
					String pwdSKey = "bpwd_" + fnIdx + "_" + brdIdx;
					String encPwd = StringUtil.getString(session.getAttribute(pwdSKey));
		    		// System.out.println(pwdSKey + ":" + encPwd);
					session.removeAttribute(pwdSKey);
					if(!StringUtil.isEmpty(encPwd)) {
						return (StringUtil.isEquals(encPwd, regiPwd));
					}
				}
			} else {
				// 해당페이지가 회원 / 본인인증 답글쓰기 권한인 경우
				// 글쓰기 권한 체크
				String chkAuthName2 = "WRT";
				boolean isNoMemberWRT = AuthHelper.isNoMemberAuthPage(chkAuthName2);
				if(isNoMemberWRT) {
					//  비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 글쓰기
					
					// 2. 회원 글쓰기 : 로그인 체크
					boolean isLogin = UserDetailsHelper.isLogin();
					if(isLogin){
						// 로그인한 경우 : 등록자idx 체크
						
						// 부모글에 등록자idx 등록 안 된 경우 : 접근권한 없음
						if(!StringUtil.isEmpty(regiIdx)) {
							// 부모글의 등록자idx와 로그인회원idx 일치 여부 체크
							String memberIdx = null;
							
							LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
							if(loginVO != null) memberIdx = loginVO.getMemberIdx();
							if(StringUtil.isEquals(memberIdx, regiIdx)) return true;
						}
					}
	
					// 3. 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 권한인지 체크
					boolean isNm = /*(!isNoMemberWRT) && */AuthHelper.getModuleUTP(chkAuthName2) <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
					
					if(isNm) {
						// 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 권한인 경우  : 본인인증 중복키 체크
						
						// 부모글의에 본인인증 중복키 등록 안 된 경우 : 접근권한 없음
						if(!StringUtil.isEmpty(regiDupInfo)) {
						
							// 해당글의 본인인증 중복키와 인증 중복키 일치 여부 체크
							String sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));
							
							if(StringUtil.isEquals(sDupInfo, regiDupInfo)) return true;
						}
					}

					//  해당 정보와 로그인 정보가 같지 않은 경우 : 입력된 비밀번호와 부모글 비밀번호 일치여부 확인
					if(StringUtil.isEmpty(pntRegiPwd)) return false;

					// 해당글, 부모글의 비밀번호와 인증 비밀번호(암호화) 일치 여부 체크
					String pwdSKey = "bpwd_" + fnIdx + "_" + brdIdx;
					String encPwd = StringUtil.getString(session.getAttribute(pwdSKey));
		    		// System.out.println(pwdSKey + ":" + encPwd);
					session.removeAttribute(pwdSKey);
					if(!StringUtil.isEmpty(encPwd)) {
						return (StringUtil.isEquals(encPwd, pntRegiPwd));
					}
					
				} else {
					// 회원, 본인인증 글쓰기
					
					// 1. 회원 글쓰기 : 로그인 체크
					boolean isLogin = UserDetailsHelper.isLogin();
					if(isLogin){
						// 로그인한 경우 : 등록자idx 체크
						
						// 해당글, 부모글에 등록자idx 등록 안 된 경우 : 접근권한 없음
						if(StringUtil.isEmpty(regiIdx) && StringUtil.isEmpty(pntRegiIdx)) return false;
						// 부모글의 등록자idx와 로그인회원idx 일치 여부 체크
						String memberIdx = null;
						
						LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
						if(loginVO != null) memberIdx = loginVO.getMemberIdx();
	
						return (StringUtil.isEquals(memberIdx, regiIdx) || StringUtil.isEquals(memberIdx, pntRegiIdx));
					}
					/*
					// 2. 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 권한인지 체크
					boolean isNm = (!isNoMemberWRT) && AuthHelper.getModuleUTP(chkAuthName2) <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
					
					if(isNm) {
						// 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 권한인 경우  : 본인인증 중복키 체크
						
						// 부모글의에 본인인증 중복키 등록 안 된 경우 : 접근권한 없음
						if(StringUtil.isEmpty(regiDupInfo) || StringUtil.isEmpty(pntRegiDupInfo)) return false;
					*/
						
						// 해당글의 본인인증 중복키와 인증 중복키 일치 여부 체크
						String sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));
						
						return (StringUtil.isEquals(sDupInfo, regiDupInfo) || StringUtil.isEquals(sDupInfo, pntRegiDupInfo));
					/*}*/
					
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 수정 페이지 접근 권한 체크
	 * @param fnIdx
	 * @param brdIdx
	 * @param memIdx
	 * @param isReply		: 답글 여부
	 * @param regiIdx		: 등록자 일련번호 - 로그인
	 * @param regiDupInfo	: 본인인증key	 - 본인인증권한 
	 * @param regiPwd		: 비밀번호           - 로그인, 본인인증없이 부여된 비회원 권한
	 * @return
	 */
	public static boolean isModifyAuth(int fnIdx, int brdIdx, int memIdx, boolean isReply, String regiIdx, String regiDupInfo, String regiPwd) {
		if(fnIdx <= 0 || brdIdx <= 0) return false;
		
		// 전체관리 권한 체크
		Boolean isMngAuth = AuthHelper.isAuthenticated("MNG");				// 전체관리
		if(isMngAuth) return true;
		
		// 등록자 정보 없는 경우 권한없음
		if(StringUtil.isEmpty(regiIdx) && StringUtil.isEmpty(regiDupInfo) && StringUtil.isEmpty(regiPwd)) return false;
		
		// 전체관리권한 없는 경우 : 자신글만 접근 가능
		
		// 1. 해당페이지가 비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 글쓰기 / 답글쓰기 권한인지 체크
		// 2. 로그인 체크
		// 3. 해당페이지가 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 / 답글쓰기 권한인지 체크
		
		/*String regiIdx = null;
		String regiDupInfo = null;
		String dbPwd = null;
		
		regiIdx = StringUtil.getString(dt.get("REGI_IDX"));
		regiDupInfo = StringUtil.getString(dt.get("MEMBER_DUP"));
		dbPwd = StringUtil.getString(dt.get("PWD"));*/

		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = request.getSession(true);
		
		String chkAuthName = null;
		if(isReply) {
			// 답글 사용
			chkAuthName = "RWT";
			/*int reLevel = StringUtil.getInt(dt.get("RE_LEVEL"));
			if(reLevel > 1) chkAuthName = "RWT";*/
		} else if(memIdx > 0) {
			// 댓글(메모글)
			chkAuthName = "MWT";
		}
		
		if(StringUtil.isEmpty(chkAuthName)) chkAuthName = "WRT";
		
		// 1. 해당페이지가 비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 글쓰기 / 답글쓰기 권한인지 체크
		boolean isNoMemberPage = AuthHelper.isNoMemberAuthPage(chkAuthName);

		if(isNoMemberPage) {
			// 해당페이지가 비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 글쓰기 / 답글쓰기 권한인 경우  : 인증 비밀번호 체크
			
			// 해당글에 비밀번호 등록 안 된 경우 : 접근권한 없음
			if(StringUtil.isEmpty(regiPwd)) return false;
			
			// 해당글의 비밀번호와 인증 비밀번호(암호화) 일치 여부 체크
			//int brdIdx = StringUtil.getInt(dt.get("BRD_IDX"));
			String pwdSKey = "bpwd_" + fnIdx + "_" + brdIdx;
			if(memIdx > 0) pwdSKey += "_" + memIdx;
			//if(StringUtil.isEmpty(pwdSKey)) pwdSKey = "bpwd_" + fnIdx + "_" + brdIdx;
			String encPwd = StringUtil.getString(session.getAttribute(pwdSKey));
			session.removeAttribute(pwdSKey);
			if(!StringUtil.isEmpty(encPwd)) {
				return (StringUtil.isEquals(encPwd, regiPwd));
			}
			return false;
		} 
		
		// 2. 로그인 체크
		boolean isLogin = UserDetailsHelper.isLogin();
		if(isLogin){
			// 로그인한 경우 : 등록자idx 체크
			
			// 해당글에 등록자idx 등록 안 된 경우 : 접근권한 없음
			if(StringUtil.isEmpty(regiIdx)) return false;
			
			// 해당글의 등록자idx와 로그인회원idx 일치 여부 체크
			String memberIdx = null;
			
			LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
			if(loginVO != null) memberIdx = loginVO.getMemberIdx();

			return (StringUtil.isEquals(memberIdx, regiIdx)); 
		}
		

		// 3. 해당페이지가 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 / 답글쓰기 권한인지 체크
		boolean isNm = (!isNoMemberPage) && AuthHelper.getModuleUTP(chkAuthName) <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
		
		if(isNm) {
			// 해당페이지가 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 / 답글쓰기 권한인 경우  : 본인인증 중복키 체크
			
			// 해당글에 본인인증 중복키 등록 안 된 경우 : 접근권한 없음
			if(StringUtil.isEmpty(regiDupInfo)) return false;
			
			// 해당글의 본인인증 중복키와 인증 중복키 일치 여부 체크
			String sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));
			
			return (StringUtil.isEquals(sDupInfo, regiDupInfo));
		}
		
		return false;
	}

	/**
	 * 상세페이지에서 수정 / 삭제 버튼 display 여부 체크
	 * @param fnIdx
	 * @param brdIdx
	 * @param memIdx
	 * @param isReply		: 답글 여부
	 * @param regiIdx		: 등록자 일련번호 - 로그인
	 * @param regiDupInfo	: 본인인증key	 - 본인인증권한 
	 * @param regiPwd		: 비밀번호           - 로그인, 본인인증없이 부여된 비회원 권한
	 * @return
	 */
	public static boolean isDisplayAuth(int fnIdx, int brdIdx, int memIdx, boolean isReply, String regiIdx, String regiDupInfo, String regiPwd) {

		if(fnIdx <= 0 || brdIdx <= 0) return false;
		
		// 전체관리 권한 체크
		Boolean isMngAuth = AuthHelper.isAuthenticated("MNG");				// 전체관리
		if(isMngAuth) return true;
		
		// 등록자 정보 없는 경우 권한없음
		if(StringUtil.isEmpty(regiIdx) && StringUtil.isEmpty(regiDupInfo) && StringUtil.isEmpty(regiPwd)) return false;
		
		// 전체관리권한 없는 경우 : 자신글만 접근 가능
		
		// 1. 해당페이지가 비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 글쓰기 / 답글쓰기 권한인지 체크
		// 2. 로그인 체크
		// 3. 해당페이지가 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 / 답글쓰기 권한인지 체크
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = request.getSession(true);
		
		String chkAuthName = null;
		if(isReply) {
			// 답글 사용
			chkAuthName = "RWT";
		} else if(memIdx > 0) {
			// 댓글(메모글)
			chkAuthName = "MWT";
		}
		
		if(StringUtil.isEmpty(chkAuthName)) chkAuthName = "WRT";
		
		// 1. 해당페이지가 비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 글쓰기 / 답글쓰기 권한인지 체크
		boolean isNoMemberPage = AuthHelper.isNoMemberAuthPage(chkAuthName);

		if(isNoMemberPage) {
			// 해당페이지가 비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 글쓰기 / 답글쓰기 권한인 경우  : 인증 비밀번호 체크
			
			// 해당글에 비밀번호 등록 안 된 경우 : 접근권한 없음
			if(StringUtil.isEmpty(regiPwd)) return false;
			
			// 해당글에 비밀번호 등록 된 경우 display
			return true;
		} 
		
		// 2. 로그인 체크
		boolean isLogin = UserDetailsHelper.isLogin();
		if(isLogin){
			// 로그인한 경우 : 등록자idx 체크
			
			// 해당글에 등록자idx 등록 안 된 경우 : 접근권한 없음
			if(StringUtil.isEmpty(regiIdx)) return false;
			
			// 해당글의 등록자idx와 로그인회원idx 일치 여부 체크
			String memberIdx = null;
			
			LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
			if(loginVO != null) memberIdx = loginVO.getMemberIdx();

			return (StringUtil.isEquals(memberIdx, regiIdx)); 
		}

		// 3. 해당페이지가 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 / 답글쓰기 권한인지 체크
		boolean isNm = (!isNoMemberPage) && AuthHelper.getModuleUTP(chkAuthName) <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
		
		if(isNm) {
			// 해당페이지가 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 / 답글쓰기 권한인 경우  : 본인인증 중복키 체크
			
			// 해당글에 본인인증 중복키 등록 안 된 경우 : 접근권한 없음
			if(StringUtil.isEmpty(regiDupInfo)) return false;
			
			// 해당글의 본인인증 중복키와 인증 중복키 일치 여부 체크
			String sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));
			
			return (StringUtil.isEquals(sDupInfo, regiDupInfo));
		}
		
		return false;
	}

	/**
	 * 목록페이지에서 비밀글 상세보기 비밀번호 확인창 display 여부 체크
	 * @param isReply			: 답글 여부
	 * @param pntRegiIdx		: 부모글 등록자 일련번호 - 로그인
	 * @param pntRegiDupInfo	: 부모글 본인인증key	 - 본인인증권한 
	 * @return
	 */
	public static boolean isDisplaySecretPassAuth(boolean isReply, String regiIdx, String regiDupInfo, String pntRegiIdx, String pntRegiDupInfo) {
		 
		// 전체관리 권한 체크
		Boolean isMngAuth = AuthHelper.isAuthenticated("MNG");				// 전체관리
		if(isMngAuth) return false;
		
		// 등록자 정보 없는 경우 권한없음
		//if(StringUtil.isEmpty(regiPwd) && 
		//		StringUtil.isEmpty(pntRegiIdx) && StringUtil.isEmpty(pntRegiDupInfo) && StringUtil.isEmpty(pntRegiPwd)) return false;
		
		// 전체관리권한 없는 경우 : 자신글, 자신글의 답글만 접근 가능
		
		// 1. 해당페이지가 비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 글쓰기 / 답글쓰기 권한인지 체크
		// 2. 로그인 체크
		// 3. 해당페이지가 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 / 답글쓰기 권한인지 체크
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = request.getSession(true);
		
		String chkAuthName = null;
		if(isReply) {
			// 답글 사용
			chkAuthName = "RWT";
		}
		
		if(StringUtil.isEmpty(chkAuthName)) chkAuthName = "WRT";
		
		// 1. 해당페이지가 비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 글쓰기 / 답글쓰기 권한인지 체크
		boolean isNoMemberPage = AuthHelper.isNoMemberAuthPage(chkAuthName);
		
		if(!isReply) {
			// 답글이 아닌 경우
			if(isNoMemberPage) {
				// 해당페이지가 비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 글쓰기 권한인 경우  : 인증 비밀번호 체크
				
				// 해당글에 비밀번호 등록 안 된 경우 : 비밀번호 인증창 display 안 함
				//if(StringUtil.isEmpty(regiPwd)) return false;
				return true;
			}
			return false;
		} else {
			// 답글인 경우
			if(isNoMemberPage) {
				// 해당페이지가 비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 답글쓰기 권한인 경우  : 인증 비밀번호 체크
				// 글쓰기 권한 체크
				String chkAuthName2 = "WRT";
				boolean isNoMemberWRT = AuthHelper.isNoMemberAuthPage(chkAuthName2);
				if(isNoMemberWRT) {
					//  비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 글쓰기
					
					// 해당글, 부모글에 비밀번호 등록 안 된 경우 : 비밀번호 인증창 display 안 함
					//if(StringUtil.isEmpty(regiPwd) && StringUtil.isEmpty(pntRegiPwd)) return false;
					return true;
				} else {
					// 회원, 본인인증 글쓰기
	
					// 2. 회원 글쓰기 : 로그인 체크
					boolean isLogin = UserDetailsHelper.isLogin();
					if(isLogin){
						// 로그인한 경우 : 등록자idx 체크
						
						// 부모글에 등록자idx 등록 안 된 경우 : 접근권한 없음
						if(StringUtil.isEmpty(regiIdx) && StringUtil.isEmpty(pntRegiIdx)) return true;
	
						// 부모글의 등록자idx와 로그인회원idx 일치 여부 체크
						String memberIdx = null;
						
						LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
						if(loginVO != null) memberIdx = loginVO.getMemberIdx();
	
						if(!StringUtil.isEquals(memberIdx, regiIdx) && !StringUtil.isEquals(memberIdx, pntRegiIdx)) return true;
						return false;
					}
	
					// 3. 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 권한인지 체크
					boolean isNm = (!isNoMemberWRT) && AuthHelper.getModuleUTP(chkAuthName2) <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
					
					if(isNm) {
						// 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 권한인 경우  : 본인인증 중복키 체크
						
						// 부모글의에 본인인증 중복키 등록 안 된 경우 : 접근권한 없음
						if(StringUtil.isEmpty(pntRegiDupInfo)) return true;
						
						// 해당글의 본인인증 중복키와 인증 중복키 일치 여부 체크
						String sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));
						
						if(!StringUtil.isEquals(sDupInfo, regiDupInfo) && !StringUtil.isEquals(sDupInfo, pntRegiDupInfo)) return true;
						return false;
					}
				}
				
				return true;
			} else {
				// 해당페이지가 회원 / 본인인증 답글쓰기 권한인 경우
				// 글쓰기 권한 체크
				String chkAuthName2 = "WRT";
				boolean isNoMemberWRT = AuthHelper.isNoMemberAuthPage(chkAuthName2);
				if(isNoMemberWRT) {
					//  비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 글쓰기
					
					// 2. 회원 글쓰기 : 로그인 체크
					boolean isLogin = UserDetailsHelper.isLogin();
					if(isLogin){
						// 로그인한 경우 : 등록자idx 체크
						
						// 부모글에 등록자idx 등록 안 된 경우 : 접근권한 없음
						if(StringUtil.isEmpty(regiIdx)) return true;
	
						// 부모글의 등록자idx와 로그인회원idx 일치 여부 체크
						String memberIdx = null;
						
						LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
						if(loginVO != null) memberIdx = loginVO.getMemberIdx();
	
						if(!StringUtil.isEquals(memberIdx, regiIdx)) return true;
						return false;
					}
	
					// 3. 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 권한인지 체크
					boolean isNm = AuthHelper.getModuleUTP(chkAuthName2) <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
					
					if(isNm) {
						// 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 권한인 경우  : 본인인증 중복키 체크
						
						// 부모글의에 본인인증 중복키 등록 안 된 경우 : 접근권한 없음
						if(StringUtil.isEmpty(regiDupInfo)) return true;
						
						// 해당글의 본인인증 중복키와 인증 중복키 일치 여부 체크
						String sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));
						
						if(!StringUtil.isEquals(sDupInfo, regiDupInfo)) return true;
						return false;
					}
					
					return true;
				} else {
					// 회원, 본인인증 글쓰기
					return false;
				}
				
			}
		}
	}

	public static boolean isDisplayViewSecretPassAuth(boolean isSecret, boolean isReply, String regiIdx, String regiDupInfo, String regiPwd, String pntRegiIdx, String pntRegiDupInfo, String pntRegiPwd, String dtPwd) {
		if(!isSecret) return true;
		
		// 전체관리 권한 체크
		Boolean isMngAuth = AuthHelper.isAuthenticated("MNG");				// 전체관리
		if(isMngAuth) return true;
		
		// 등록자 정보 없는 경우 권한없음
		//if(StringUtil.isEmpty(regiIdx) && StringUtil.isEmpty(regiDupInfo) && StringUtil.isEmpty(regiPwd)) return false;
		
		// 전체관리권한 없는 경우 : 자신글, 자신글의 답글만 접근 가능
		
		// 1. 해당페이지가 비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 글쓰기 / 답글쓰기 권한인지 체크
		// 2. 로그인 체크
		// 3. 해당페이지가 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 / 답글쓰기 권한인지 체크
		
		// 해당글 권한 체크
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = request.getSession(true);
		
		String chkAuthName = null;
		if(isReply) {
			// 답글인 경우
			chkAuthName = "RWT";
		}
		
		if(StringUtil.isEmpty(chkAuthName)) chkAuthName = "WRT";
		
		// 1. 해당페이지가 비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 글쓰기 / 답글쓰기 권한인지 체크
		boolean isNoMemberPage = AuthHelper.isNoMemberAuthPage(chkAuthName);

		if(!isReply) {
			// 답글이 아닌 경우
			if(isNoMemberPage) {
				// 해당페이지가 비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 글쓰기 / 답글쓰기 권한인 경우  : 인증 비밀번호 체크
				
				// 해당글에 비밀번호 등록 안 된 경우 : 접근권한 없음
				if(StringUtil.isEmpty(regiPwd)) return false;
				
				if(!StringUtil.isEmpty(dtPwd)) {
					return (StringUtil.isEquals(dtPwd, regiPwd));
					//else return (StringUtil.isEquals(dtPwd, regiPwd) || StringUtil.isEquals(regiPwd, pntRegiPwd));
				}
				return false;
			} 
			
			// 2. 로그인 체크
			boolean isLogin = UserDetailsHelper.isLogin();
			if(isLogin){
				// 로그인한 경우 : 등록자idx 체크
				
				// 해당글에 등록자idx 등록 안 된 경우 : 접근권한 없음
				if(StringUtil.isEmpty(regiIdx)) return false;
				
				// 해당글의 등록자idx와 로그인회원idx 일치 여부 체크
				String memberIdx = null;
				
				LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
				if(loginVO != null) memberIdx = loginVO.getMemberIdx();
	
				return (StringUtil.isEquals(memberIdx, regiIdx)); 
			}
			
	
			// 3. 해당페이지가 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 / 답글쓰기 권한인지 체크
			boolean isNm = (!isNoMemberPage) && AuthHelper.getModuleUTP(chkAuthName) <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
			
			if(isNm) {
				// 해당페이지가 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 / 답글쓰기 권한인 경우  : 본인인증 중복키 체크
				
				// 해당글에 본인인증 중복키 등록 안 된 경우 : 접근권한 없음
				if(StringUtil.isEmpty(regiDupInfo)) return false;
				
				// 해당글의 본인인증 중복키와 인증 중복키 일치 여부 체크
				String sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));
				
				return (StringUtil.isEquals(sDupInfo, regiDupInfo));
			}
			return false;
		} else {
			// 답글인 경우
			// 글쓰기 권한 체크, 부모글 비교
			if(isNoMemberPage) {
				// 해당페이지가 비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 답글쓰기 권한인 경우  : 인증 비밀번호 체크
				// 글쓰기 권한 체크				
				String chkAuthName2 = "WRT";
				boolean isNoMemberWRT = AuthHelper.isNoMemberAuthPage(chkAuthName2);
				if(isNoMemberWRT) {
					//  비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 글쓰기
					if(StringUtil.isEmpty(regiPwd) && StringUtil.isEmpty(pntRegiPwd)) return false;

					if(!StringUtil.isEmpty(dtPwd)) {
						return (StringUtil.isEquals(dtPwd, regiPwd) || StringUtil.isEquals(dtPwd, pntRegiPwd));
					}
					return false;
				} else {
					// 회원, 본인인증 글쓰기
	
					// 2. 회원 글쓰기 : 로그인 체크
					boolean isLogin = UserDetailsHelper.isLogin();
					if(isLogin){
						// 로그인한 경우 : 등록자idx 체크
						
						// 부모글에 등록자idx 등록 안 된 경우 : 접근권한 없음
						if(!StringUtil.isEmpty(pntRegiIdx)) {
							// 부모글의 등록자idx와 로그인회원idx 일치 여부 체크
							String memberIdx = null;
							
							LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
							if(loginVO != null) memberIdx = loginVO.getMemberIdx();
		
							if(StringUtil.isEquals(memberIdx, pntRegiIdx)) return true;
						}
					}
	
					// 3. 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 권한인지 체크
					/*
					boolean isNm = (!isNoMemberWRT) && AuthHelper.getModuleUTP(chkAuthName2) <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
					
					if(isNm) {
						// 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 권한인 경우  : 본인인증 중복키 체크
						
						// 부모글의에 본인인증 중복키 등록 안 된 경우 : 접근권한 없음
						if(!StringUtil.isEmpty(pntRegiDupInfo)) {
							// 해당글의 본인인증 중복키와 인증 중복키 일치 여부 체크
					 */
							String sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));
							
							if(StringUtil.isEquals(sDupInfo, regiDupInfo) || StringUtil.isEquals(sDupInfo, pntRegiDupInfo)) return true;
					/*
						}
					}
					*/

					if(isLogin){
						String memberIdx = null;
						
						LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
						if(loginVO != null) memberIdx = loginVO.getMemberIdx();
						
						if(StringUtil.isEquals(memberIdx, regiIdx)) return true;
					}
					
					//  부모글 정보와 로그인 정보가 같지 않은 경우 : 입력된 비밀번호와 해당글 일치여부 확인
					if(StringUtil.isEmpty(regiPwd)) return false;

					/*
					// 해당글, 부모글의 비밀번호와 인증 비밀번호(암호화) 일치 여부 체크
					String pwdSKey = "bpwd_" + fnIdx + "_" + brdIdx;
					String encPwd = StringUtil.getString(session.getAttribute(pwdSKey));
		    		// System.out.println(pwdSKey + ":" + encPwd);
					session.removeAttribute(pwdSKey);
					*/
					if(!StringUtil.isEmpty(dtPwd)) {
						return (StringUtil.isEquals(dtPwd, regiPwd));
					}
				}
			} else {
				// 해당페이지가 회원 / 본인인증 답글쓰기 권한인 경우
				// 글쓰기 권한 체크
				String chkAuthName2 = "WRT";
				boolean isNoMemberWRT = AuthHelper.isNoMemberAuthPage(chkAuthName2);
				if(isNoMemberWRT) {
					//  비회원(로그인, 본인인증 여부 관계없이 접근권한 부여) 글쓰기
					
					// 2. 회원 글쓰기 : 로그인 체크
					boolean isLogin = UserDetailsHelper.isLogin();
					if(isLogin){
						// 로그인한 경우 : 등록자idx 체크
						
						// 부모글에 등록자idx 등록 안 된 경우 : 접근권한 없음
						if(!StringUtil.isEmpty(regiIdx)) {
							// 부모글의 등록자idx와 로그인회원idx 일치 여부 체크
							String memberIdx = null;
							
							LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
							if(loginVO != null) memberIdx = loginVO.getMemberIdx();
		
							if(StringUtil.isEquals(memberIdx, regiIdx)) return true;
						}
					}
	
					// 3. 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 권한인지 체크
					boolean isNm = /*(!isNoMemberWRT) && */AuthHelper.getModuleUTP(chkAuthName2) <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
					
					if(isNm) {
						// 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 권한인 경우  : 본인인증 중복키 체크
						
						// 부모글의에 본인인증 중복키 등록 안 된 경우 : 접근권한 없음
						if(!StringUtil.isEmpty(regiDupInfo)) {
						
							// 해당글의 본인인증 중복키와 인증 중복키 일치 여부 체크
							String sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));
							
							if(StringUtil.isEquals(sDupInfo, regiDupInfo)) return true;
						}
					}

					//  해당 정보와 로그인 정보가 같지 않은 경우 : 입력된 비밀번호와 부모글 비밀번호 일치여부 확인
					if(StringUtil.isEmpty(pntRegiPwd)) return false;

					/*
					// 해당글, 부모글의 비밀번호와 인증 비밀번호(암호화) 일치 여부 체크
					String pwdSKey = "bpwd_" + fnIdx + "_" + brdIdx;
					String encPwd = StringUtil.getString(session.getAttribute(pwdSKey));
		    		// System.out.println(pwdSKey + ":" + encPwd);
					session.removeAttribute(pwdSKey);
					*/
					if(!StringUtil.isEmpty(dtPwd)) {
						return (StringUtil.isEquals(dtPwd, pntRegiPwd));
					}
					
				} else {
					// 회원, 본인인증 글쓰기
					
					// 1. 회원 글쓰기 : 로그인 체크
					boolean isLogin = UserDetailsHelper.isLogin();
					if(isLogin){
						// 로그인한 경우 : 등록자idx 체크
						
						// 해당글, 부모글에 등록자idx 등록 안 된 경우 : 접근권한 없음
						if(StringUtil.isEmpty(regiIdx) && StringUtil.isEmpty(pntRegiIdx)) return false;
						// 부모글의 등록자idx와 로그인회원idx 일치 여부 체크
						String memberIdx = null;
						
						LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
						if(loginVO != null) memberIdx = loginVO.getMemberIdx();
	
						return (StringUtil.isEquals(memberIdx, regiIdx) || StringUtil.isEquals(memberIdx, pntRegiIdx));
					}
	
					/*
					// 2. 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 권한인지 체크
					boolean isNm = (!isNoMemberWRT) && AuthHelper.getModuleUTP(chkAuthName2) <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
					
					if(isNm) {
						// 본인인증회원 or 비회원 (로그인, 본인인증으로 접근권한 부여) 글쓰기 권한인 경우  : 본인인증 중복키 체크
						
						// 부모글의에 본인인증 중복키 등록 안 된 경우 : 접근권한 없음
						if(StringUtil.isEmpty(regiDupInfo) && StringUtil.isEmpty(pntRegiDupInfo)) return false;
					*/	
						// 해당글의 본인인증 중복키와 인증 중복키 일치 여부 체크
						String sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));
						
						return (StringUtil.isEquals(sDupInfo, regiDupInfo) || StringUtil.isEquals(sDupInfo, pntRegiDupInfo));
					/*}
					
					return false;
					*/
				}
				
			}
		}
		
		return false;
	}
	/**
	 * 관리권한 체크
	 * @param settingInfo
	 * @param fnIdx
	 * @param brdIdx
	 * @param memIdx
	 * @param useReply
	 * @param pwd
	 * @return
	 */
	public static boolean isMngProcAuth(JSONObject settingInfo, int fnIdx, int brdIdx, int memIdx, boolean useReply, String pwd) {

		String boardDesignType = JSONObjectUtil.getString(settingInfo, "design_type");
		String columnName = JSONObjectUtil.getString(settingInfo, "idx_column");
		String memoColumnName = JSONObjectUtil.getString(settingInfo, "memo_idx_column");

		if(memIdx > 0) boardDesignType = RbsProperties.getProperty("Globals.design.NAME_DESIGN_TYPE_MEMO");
		String chkAuthName = null;
		if(useReply) {
			// 답글 사용
			Map<String, Object> param = new HashMap<String, Object>();
			List<DTForm> searchList = new ArrayList<DTForm>();
			searchList.add(new DTForm("A." + columnName, brdIdx));
			if(memIdx > 0) searchList.add(new DTForm("A." + memoColumnName, memIdx));
			
			param.put("searchList", searchList);
			param.put("boardDesignType", boardDesignType);
			
			DataMap pwdDt = boardService.getPwdView(fnIdx, param);
			if(pwdDt != null) {
				int reLevel = StringUtil.getInt(pwdDt.get("RE_LEVEL"));
				if(reLevel > 1) chkAuthName = "RWT";
			}
		}
		else if(memIdx > 0) {
			// 댓글(메모글)
			chkAuthName = "MWT";
		}
		
		if(StringUtil.isEmpty(chkAuthName)) chkAuthName = "WRT";

		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = request.getSession(true);
		session.setAttribute("brdChkAuthName", chkAuthName);
		
		// 비회원 글쓰기 / 답글쓰기 권한 여부 (로그인, 본인인증 여부 관계없이 접근권한 부여)
		boolean isNoMemberPage = AuthHelper.isNoMemberAuthPage(chkAuthName);
		
		//본인인증회원 글쓰기이거나 비회원 글쓰기인 경우
		boolean isNm = (!isNoMemberPage) && AuthHelper.getModuleUTP(chkAuthName) <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
		
		int modiCnt = 0;
		// 권한 체크
		Boolean isMngAuth = AuthHelper.isAuthenticated("MNG");		// 전체관리
		
		if(!isMngAuth){
			Map<String, Object> param = new HashMap<String, Object>();
			List<DTForm> searchList = new ArrayList<DTForm>();
			
			searchList.add(new DTForm("A." + columnName, brdIdx));
			if(memIdx > 0) searchList.add(new DTForm("A." + memoColumnName, memIdx));
			
			// 전체관리권한 없는 경우 : 자신글만 수정
			boolean isLogin = UserDetailsHelper.isLogin();		// 로그인한 경우
			boolean isNmAuth = UserDetailsHelper.isNmAuth();	// 본인인증한 경우
			if(isNoMemberPage) {
				// 비회원 글쓰기 / 답글쓰기 권한인 경우 (로그인, 본인인증 여부 관계없이 접근권한 부여)
				String encPwd = DataSecurityUtil.getDigest(pwd);
				if(StringUtil.isEmpty(encPwd)) {
					String pwdSKey = "bpwd_" + fnIdx + "_" + brdIdx;
					if(memIdx > 0) pwdSKey += "_" + memIdx;
					encPwd = StringUtil.getString(session.getAttribute(pwdSKey));
					session.removeAttribute(pwdSKey);
				}
				
				if(StringUtil.isEmpty(encPwd)) return false;
				searchList.add(new DTForm("A.PWD", encPwd));
			}else if(isLogin){
				// 로그인한 경우
				String memberIdx = null;
				
				LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
				if(loginVO != null) memberIdx = loginVO.getMemberIdx();
				
				if(StringUtil.isEmpty(memberIdx)) return false;
				searchList.add(new DTForm("A.REGI_IDX", memberIdx));
			} else if(isNm && isNmAuth){
				//본인인증회원 글쓰기/ 답글쓰기인 경우
				String sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));

				if(StringUtil.isEmpty(sDupInfo)) return false;
				searchList.add(new DTForm("A.MEMBER_DUP", sDupInfo));
			} else {
				// 본인인증 or 로그인 안 한 경우
				return false;
			}

			param.put("searchList", searchList);
			param.put("boardDesignType", boardDesignType);
			modiCnt = boardService.getAuthCount(fnIdx, param);
			
			return (modiCnt > 0);
		}
		
		return true;
	}

}
