package rbs.usr.main.web;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.DataSecurityUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.web.NonModuleDownloadController;

/**
 * 파일다운로드
 * @author user
 *
 */
@Controller
@RequestMapping({"/{siteId}/main"})
public class MainDownloadController extends NonModuleDownloadController{

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	@RequestMapping(value = "/download.do")
	public ModelAndView download(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		Map matchTemplate = (Map) request
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		String uploadPath = StringUtil.getString(matchTemplate.get("siteId"));
		
		String savedFileName = request.getParameter("sfn");
		String originFileName = request.getParameter("ofn");
		if(StringUtil.isEmpty(uploadPath) || StringUtil.isEmpty(savedFileName)){
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.error.data"), ""));
			return new ModelAndView(RbsProperties.getProperty("Globals.fail.path"));
		}

		originFileName = DataSecurityUtil.getImgNSeedDecrypt(StringUtil.getString(originFileName, savedFileName));
		savedFileName = DataSecurityUtil.getImgNSeedDecrypt(savedFileName);
		
		return download(attrVO, uploadPath, savedFileName, originFileName, model);
	}
}
