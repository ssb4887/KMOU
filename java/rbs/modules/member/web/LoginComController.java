package rbs.modules.member.web;

import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import rbs.modules.member.service.LoginService;
import rbs.usr.main.service.MainService;

import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.web.ModuleController;

public class LoginComController extends ModuleController{
	
	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	@Resource(name = "rbsLoginService")
	protected LoginService loginService;
	
	@Resource(name = "usrMainService")
	protected MainService mainService;
	
	public JSONObject getSettingInfo(JSONObject moduleSetting) {
		//this.moduleSetting = moduleSetting;
		String settingName = "setting_info";
		settingName = "login_" + settingName;
		return JSONObjectUtil.getJSONObject(moduleSetting, settingName);
	}

	public JSONObject getItemInfo(JSONObject moduleItem) {
		//this.moduleItem = moduleItem;
		String itemName = "item_info";
		itemName = "login_" + itemName;
		return JSONObjectUtil.getJSONObject(moduleItem, itemName);
	}
	
	/**
	 * 경로 setting
	 * @param request
	 * @param queryString
	 * @param pageEncode
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO, boolean useSsl) {	

		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		DataForm queryString = attrVO.getQueryString();
		JSONObject settingInfo = getSettingInfo(attrVO.getModuleSetting());

		// 항목 설정 정보 : 검색 
		//if(itemInfo == null) itemInfo = JSONObjectUtil.getJSONObject(moduleItem, "login_item_info");
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");
		
		String logInName = "login.do";
		String logInPreOkName = "loginPreProc.do";
		String logInOkName = "loginProc.do";
		String logInExtName = "loginExtOraFunc.do";
		String pwdModiName = request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/member/pwdmodi.do";
		
		if(useSsl){
			logInPreOkName = PathUtil.getSslPagePath(logInPreOkName);	
			logInOkName = PathUtil.getSslPagePath(logInOkName);	
//			logInExtName = PathUtil.getSslPagePath(logInExtName);
		}

		// 기본 parameter
		StringBuffer baseQueryString = PathUtil.getQueryString(queryString, baseParams);
		
		StringBuffer allQueryString = new StringBuffer();			// 모든 queryString setting
		
		int baseQLen = baseQueryString.length();
		if(baseQLen > 0) {
			baseQueryString.insert(0,  "?");
			allQueryString.append(baseQueryString);
		}

		int allQLen = 0;

		if(!StringUtil.isEmpty(idxName)) {
			String[] idxNames = StringUtil.stringToArray(idxName, ",");
			String idx = null;
			int idxNameLen = 0;
			if(idxNames != null) {
				idxNameLen = idxNames.length;
				for(int i = 0 ; i < idxNameLen ; i ++) {
					//idx = StringUtil.replaceScript(queryString.getString(idxNames[i]));
					idx = queryString.getString(idxNames[i]);
					if(!StringUtil.isEmpty(idx))
					{
						allQLen = allQueryString.length();
						
						if(allQLen > 0)	allQueryString.append("&");
						else allQueryString.append("?");
						allQueryString.append(idxNames[i] + "=");
						try	{
							allQueryString.append(URLEncoder.encode(idx, "UTF-8"));
						} catch(Exception e){}
					}
				}
			}
		}
		
		// 로그인
		request.setAttribute("URL_LOGIN", logInName + allQueryString);
		// 로그인전처리
		request.setAttribute("URL_LOGINPREPROC", logInPreOkName + allQueryString);
		// 로그인처리
		request.setAttribute("URL_LOGINPROC", logInOkName + allQueryString);
		// 로그인처리(종합정보시스템 Oracle Login Function)
		request.setAttribute("URL_LOGINORACLEPROC", logInExtName + allQueryString);
		// 비밀번호변경
		request.setAttribute("URL_PWDMODI", pwdModiName + allQueryString);

		//request.setAttribute("URL_FACEBOOKLOGIN", facebookOAuth2Parameters.getRedirectUri());
		request.setAttribute("URL_FACEBOOKLOGIN", "facebookLogin.do" + allQueryString);
		request.setAttribute("URL_NAVERLOGIN", "naverLogin.do" + allQueryString);
		request.setAttribute("URL_KAKAOLOGIN", "kakaoLogin.do" + allQueryString);
		request.setAttribute("URL_GOOGLELOGIN", "googleLogin.do" + allQueryString);
		request.setAttribute("URL_SNSLOGINOK", "snsLoginProc.do" + allQueryString);
		request.setAttribute("URL_SNSJOIN", request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/member/snsJoin.do" + allQueryString);
		request.setAttribute("URL_SNSJOINOK", request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/member/snsJoinProc.do" + allQueryString);
		
	}
}
