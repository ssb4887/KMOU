package rbs.modules.trebook.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.web.ModuleImageController;

/**
 * 이미지 다운로드
 * @author user
 *
 */
@Controller
@RequestMapping({"/{admSiteId}/menuContents/{usrSiteId}/trebook", "/{admSiteId}/moduleFn/trebook", "/{siteId}/trebook"})
@ModuleMapping(moduleId="trebook")
public class TrebookImageController extends ModuleImageController{

	@RequestMapping(value="/image.do", params="id")
	public ModelAndView image(@RequestParam("id") String id, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		id = request.getParameter("id");
		return super.image(id, attrVO, request, response, model);
	}
}