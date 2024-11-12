package rbs.egovframework.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import rbs.egovframework.LoginVO;
import rbs.egovframework.util.PrivAuthUtil;

/**
 * 
 * @author Administrator
 *
 */
public class JoinISessionControlInterceptor extends HandlerInterceptorAdapter {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		int usertypeIdx = 0;
		if(loginVO != null) usertypeIdx = loginVO.getUsertypeIdx();
		if(usertypeIdx != RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER"))
			PrivAuthUtil.removeNameAuthSession();
		return true;
	}
}
