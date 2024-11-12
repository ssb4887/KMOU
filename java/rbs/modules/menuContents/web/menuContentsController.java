package rbs.modules.menuContents.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.web.ModuleController;

@Controller
@ModuleMapping(moduleId="menuContents")
public class menuContentsController extends ModuleController{
	
	@RequestMapping("/{admSiteId}/menuContents/main.do")
	public String main(HttpServletRequest request, ModelMap model) {

		return getViewPath("/main");
	}
}
