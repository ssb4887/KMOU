package rbs.modules.module.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import rbs.modules.module.service.ModuleService;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;

public class CommonController extends ModuleController{
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;

	@Resource(name = "moduleService")
	private ModuleService moduleService;


	/**
	 * 관리하는 모듈 목록
	 * @return
	 */
	public List<Object> getModuleList() {
		Map<String, Object> param = new HashMap<String, Object>();
		return moduleService.getManageList(param);
	}
	public List<Object> getAuthModuleList() {
		Map<String, Object> param = new HashMap<String, Object>();
		return moduleService.getAuthManageList(param);
	}
	
	/**
	 * 모듈 상세 정보
	 * @param moduleId
	 * @return
	 */
	public DataMap getModuleInfo(String moduleId) {
		Map<String, Object> moduleParam = new HashMap<String, Object>();
		List<DTForm> moduleSearchList = new ArrayList<DTForm>();
		moduleSearchList.add(new DTForm("A.MODULE_ID", moduleId));
		moduleParam.put("searchList", moduleSearchList);
		DataMap moduleDt = moduleService.getManageView(moduleParam);
		return moduleDt;
	}
}
