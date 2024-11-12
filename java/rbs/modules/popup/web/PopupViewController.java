package rbs.modules.popup.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import rbs.modules.popup.service.PopupService;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.web.ModuleController;


@Controller
@RequestMapping("/{siteId}/popup")
@ModuleMapping(moduleId="popup")
public class PopupViewController extends ModuleController{
	
	public boolean checkController(HttpServletRequest request, HttpServletResponse response
			, String moduleId
			, int fnIdx) throws Exception {
		
		ModuleAttrVO moduleAttrVO = new ModuleAttrVO();
		
    	// 3. 모듈 설정 정보
		String siteMode = RbsProperties.getProperty("Globals.site.mode.usr");
		moduleAttrVO.setSiteMode(siteMode);
		moduleAttrVO.setConfModule(moduleId);
		moduleAttrVO.setModuleId(moduleId);
		moduleAttrVO.setFnIdx(fnIdx);
		
		moduleAttrVO.setDesign("default");

		// 5. 모듈별 jsp root경로
		String moduleJspRTPath = RbsProperties.getProperty("Globals.root.jsp.path") + moduleJspPath + getViewModulePath();
		String moduleJspRPath = moduleJspRTPath;
		if(!StringUtil.isEmpty(moduleId)) {
			moduleJspRPath += "/" + moduleId;
		}
		
		request.setAttribute("moduleJspRTPath", moduleJspRTPath);
		request.setAttribute("moduleJspRPath", moduleJspRPath);
		//request.setAttribute("moduleJspRPath", RbsProperties.getProperty("Globals.root.jsp.path") + moduleJspPath + getViewModulePath());
		
		request.setAttribute("moduleAttrVO", moduleAttrVO);
		
		return true;
	}
	
	@Resource(name = "popupService")
	private PopupService popupService;


	@RequestMapping(value = "/popup.do")
	public String popup(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		int fnIdx = attrVO.getFnIdx();
		
		int brdIdx = StringUtil.getInt(request.getParameter("popIdx"));
		if(fnIdx <= 0 || brdIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400.path");
		}
		
		// 2. DB
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A.POP_IDX", brdIdx));
		param.put("searchList", searchList);
		
		dt = popupService.getView(fnIdx, param);
		
		model.addAttribute("dt", dt);
    	
		return getViewPath("/popup");
	}
}
