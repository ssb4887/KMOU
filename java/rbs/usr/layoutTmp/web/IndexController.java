package rbs.usr.layoutTmp.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sun.star.io.IOException;
import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.util.MenuUtil;

/**
 * 사용자 템플릿 사이트 index
 * @author user
 *
 */
@Controller
@RequestMapping("/{siteId}")
public class IndexController{
	
	@RequestMapping("/index.ido")
	public String main(@ModuleAttr ModuleAttrVO attrVO, HttpServletResponse response, HttpServletRequest request, ModelMap model) throws IOException {
		try {
			response.sendRedirect(MenuUtil.getMenuUrl(true, 1, null));
		} catch(Exception e) {System.out.println("e:" + e);}
		return null;
		//return "redirect:" + MenuUtil.getViewRedirectMenuUrl(1);
	}
}