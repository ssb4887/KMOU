package rbs.usr.nice.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.web.NonModuleController;


@Controller
public class NiceController extends NonModuleController{
	
	public String getViewModulePath() {
		return "/nice";
	}
	
	@Override
	public String getViewPath(String filePath) {
		return RbsProperties.getProperty("Globals.rbs.jsp.path") + "/" + RbsProperties.getProperty("Globals.site.mode.usr") + "/nice" + filePath;
	}
	
	/** EgovMessageSource */
	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	@RequestMapping("/{siteId}/nice/nice.do")
	public String nice() {
		
		return getViewPath("/nice");
	}
	
	@RequestMapping("/{siteId}/nice/ipinProcess.do")
	public String ipinProcess() {
		
		return getViewPath("/ipinProcess");
	}
	
	@RequestMapping("/{siteId}/nice/ipinResult.do")
	public String ipinResult() {
		
		return getViewPath("/ipinResult");
	}
}
