package rbs.egovframework.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * SNS 로그인, SNS 등록 정보(회원정보수정에서 사용) 삭제
 * @author Administrator
 *
 */
public class SnsJoinSessionControlInterceptor extends HandlerInterceptorAdapter {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession(true);
		session.setAttribute("snsLoginVO", null);
		return true;
	}
}
