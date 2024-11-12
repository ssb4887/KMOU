package rbs.egovframework.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
 * 회원가입시 사용할 SNS 등록 session 삭제
 * @author Administrator
 *
 */
public class SnsJoinISessionControlInterceptor extends HandlerInterceptorAdapter {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession(true);
		session.setAttribute("iSnsLoginVO", null);
		return true;
	}
}
