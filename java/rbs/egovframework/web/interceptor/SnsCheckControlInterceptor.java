package rbs.egovframework.web.interceptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.PathUtil;

/**
 * SNS 로그인 사용여부 체크
 * @author Administrator
 *
 */
public class SnsCheckControlInterceptor extends HandlerInterceptorAdapter {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		// sns로그인 사용여부 - 사용하지 않는 경우 404오류
		int useSns = RbsProperties.getPropertyInt("Globals.sns.login.use");
		if(useSns != 1) {
			RequestDispatcher dispatcher = request.getRequestDispatcher(PathUtil.getDispatcherPath("Globals.error.404.path"));
			dispatcher.forward(request, response);
			return false;
		}
		return true;
	}
}
