package rbs.modules.contents.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;

public class CommonController extends ModuleController{
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	/**
	 * 언어 속성 얻기
	 * @param attrVO
	 * @return
	 */
	public ModuleAttrVO getCommonLangAttrVO(ModuleAttrVO attrVO) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		DataForm queryString = attrVO.getQueryString();
		
		String slang = queryString.getString("slang");
		if(StringUtil.isEmpty(slang)) {
			slang = rbsMessageSource.getLocaleLang();
			queryString.put("slang", slang);
			attrVO.setQueryString(queryString);
			request.setAttribute("queryString", queryString);
		}
		request.setAttribute("langList", CodeHelper.getOptnList("LOCALE"));
		
		return attrVO;
	}
	
	/**
	 * 언어명 setting
	 * @param attrVO
	 * @return
	 */
	public void setSearchLangName(String slang) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		request.setAttribute("searchLangName", CodeHelper.getOptnName("LOCALE", slang));
	}	
}
