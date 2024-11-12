package rbs.modules.member.web;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import rbs.modules.member.service.MemberService;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.web.ModuleController;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * 사용자모드 : 회원가입/내정보수정/아이디찾기/비밀번호찾기/회원탈퇴
 * @author user
 *
 */
public class MemberJoinComController extends ModuleController{
	
	@Resource(name = "memberService")
	protected MemberService memberService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	protected RbsMessageSource rbsMessageSource;
	
	/**
	 * 내정보수정 경로
	 */
	public void fn_setMyInfoPath(ModuleAttrVO attrVO) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		DataForm queryString = attrVO.getQueryString();
		
		String inputName = "myInfo.do";
		String inputProcName = "myInfoProc.do";
		String pwdmodiName = "pwdmodi.do";
		String pwdmodiProcName = "pwdmodiProc.do";

		JSONObject settingInfo = attrVO.getSettingInfo();

		boolean useSsl = JSONObjectUtil.isEquals(settingInfo, "use_ssl", "1");
		
		if(useSsl){
			inputProcName = PathUtil.getSslPagePath(inputProcName);
			pwdmodiProcName = PathUtil.getSslPagePath(pwdmodiProcName);
		}
		
		PathUtil.fn_setInputPath(queryString, baseParams, inputName, inputProcName);
		
		String allQueryString = StringUtil.getString(request.getAttribute("allQueryString"));

		request.setAttribute("URL_PWDMODI", pwdmodiName + allQueryString);
		request.setAttribute("URL_PWDMODIPROC", pwdmodiProcName + allQueryString);
		
		//request.setAttribute("URL_FACEBOOKLOGIN", facebookOAuth2Parameters.getRedirectUri());
		request.setAttribute("URL_FACEBOOKLOGIN", "facebookJoin.do" + allQueryString);
		request.setAttribute("URL_NAVERLOGIN", "naverJoin.do" + allQueryString);
		request.setAttribute("URL_KAKAOLOGIN", "kakaoJoin.do" + allQueryString);
		request.setAttribute("URL_GOOGLELOGIN", "googleJoin.do" + allQueryString);
		request.setAttribute("URL_SNSJOINOK", "snsJoinProc.do" + allQueryString);
	}
}
