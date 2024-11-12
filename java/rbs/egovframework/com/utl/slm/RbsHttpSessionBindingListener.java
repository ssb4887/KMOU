package rbs.egovframework.com.utl.slm;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * @Class Name : EgovHttpSessionBindingListener.java
 * @Description : 중복 로그인 방지를 위해 사용자의 로그인 아이디와 세션을 제어하는 구현 클래스
 * @Modification Information
 *
 *    수정일         수정자         수정내용
 *    -------        -------     -------------------
 *    2014.09.30	표준프레임워크		최초생성
* @author YJ Kwon
 * @since 2014.09.30
 * @version 3.5
 */
public enum RbsHttpSessionBindingListener implements HttpSessionBindingListener {
	INSTANCE;
	
	private static ConcurrentHashMap<String, HttpSession> loginUsers = new ConcurrentHashMap<String, HttpSession>();
	
	/**
	 * 사용자의 로그인 세션에 EgovHttpSessionBindingListener가 바인딩될 때 자동 호출되는 메소드로,
	 * 사용자 세션이 이미 존재하는지를 검사하여 하나의 어플리케이션 내에서 하나의 세션만 유지되도록 한다
	 * */
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		if (findByLoginId(event.getName())) {
			invalidateByLoginId(event.getName());
		}
		loginUsers.put(event.getName(), event.getSession());
	}

	/**
	 * 
	 * 로그아웃 혹은 세션타임아웃 설정에 따라 사용자 세션으로부터 
	 * EgovHttpSessionBindingListener가 제거될 때 자동 호출되는 메소드로,
	 * 사용자의 로그인 아이디에 해당하는 세션을 ConcurrentHashMap에서 모두 제거한다
	 * */
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		loginUsers.remove(event.getName(), event.getSession());
	}

	/**
	 * 사용자의 로그인 아이디로 생성된 세션이 있는지를 확인한다
	 * */
	public boolean findByLoginId(String loginId) {		
		return loginUsers.containsKey(loginId);
	}

	/**
	 * 사용자의 로그인 아이디로 이미 존재하는 세션을 무효화한다
	 * */
	public void invalidateByLoginId(String loginId) {
		Enumeration<String> e = loginUsers.keys();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			if (key.equals(loginId)) {
				loginUsers.get(key).invalidate();
			}
		}
	}
}
