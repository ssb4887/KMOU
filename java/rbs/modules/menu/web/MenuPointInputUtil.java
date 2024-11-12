package rbs.modules.menu.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.util.StringUtil;

import rbs.modules.menu.service.MenuPointService;

public class MenuPointInputUtil {
	private static MenuPointService menuPointService;

	static {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes()).getRequest();
		ApplicationContext act = WebApplicationContextUtils
				.getRequiredWebApplicationContext(request.getSession()
						.getServletContext());
		menuPointService = (MenuPointService) act.getBean("menuPointService");
	}
	
	public static DataMap getMenuPointDt(String siteId, int verIdx, String mId) {
		if(StringUtil.isEmpty(siteId) || verIdx <= 0 || StringUtil.isEmpty(mId)) {
			return null;
		}
		int menuIdx = StringUtil.getInt(mId);
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.SITE_ID", siteId));
		searchList.add(new DTForm("A.VER_IDX", verIdx));
		searchList.add(new DTForm("A.MENU_IDX", menuIdx));
		
		param.put("searchList", searchList);
		
		return menuPointService.getTotalView(param);
	}
}
