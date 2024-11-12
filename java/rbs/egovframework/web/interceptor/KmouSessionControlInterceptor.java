package rbs.egovframework.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.woowonsoft.egovframework.util.MenuUtil;

import rbs.egovframework.LoginVO;

public class KmouSessionControlInterceptor extends HandlerInterceptorAdapter{
	
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {		
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		
		if (loginVO == null){
			response.sendRedirect(MenuUtil.getMenuUrl(true, 3, null));
			return false;
		}				
		return true;
	}
}
