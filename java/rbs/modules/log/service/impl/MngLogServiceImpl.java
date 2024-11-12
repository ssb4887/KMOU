package rbs.modules.log.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import rbs.egovframework.LoginVO;
import rbs.modules.log.mapper.MngLogMapper;
import rbs.modules.log.service.MngLogService;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.ClientUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;


/**
 * 관리 로그에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
@Service("mngLogService")
public class MngLogServiceImpl extends EgovAbstractServiceImpl implements MngLogService {

	// 관리 로그
    @Resource(name="mngLogMapper")
    private MngLogMapper mngLogDAO;
    
	/**
	 * 전체 목록 수
	 * @param param
	 * @return
	 */
	@Override
	public int getTotalCount(Map<String, Object> param){
		return mngLogDAO.getTotalCount(param);
	}
	
	/**
	 * 전체 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(Map<String, Object> param){
		return mngLogDAO.getList(param);
	}
	
	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getView(Map<String, Object> param){
		return mngLogDAO.getView(param);
	}

	/**
	 * 등록
	 * @param logType		로그구분(1:등록,2:수정,3:삭제,4:복원,5:완전삭제, 9:적용)
	 * @param confSModule	내부모듈경로
	 * @param keyCodes		key값
	 * @return
	 * @throws Exception
	 */
	@Override
	public int insert(String confSModule, int logType, Object ... keyCodes) throws Exception{
		return insert(confSModule, null, logType, keyCodes);
	}
	
	/**
	 * 등록
	 * @param logType		로그구분(1:등록,2:수정,3:삭제,4:복원,5:완전삭제, 9:적용)
	 * @param confSModule	내부모듈경로
	 * @param preKeyCode	keyCode앞에 추가할 key값
	 * @param keyCodes		key값
	 * @return
	 * @throws Exception
	 */
	@Override
	public int insert(String confSModule, String preKeyCode, int logType, Object ... keyCodes) throws Exception{
		int result = 0;
		if(keyCodes == null || keyCodes.length == 0) return result;
		int useMngerLog = RbsProperties.getPropertyInt("Globals.mnger.log.use.db", 0);
		if(useMngerLog == 0) return result;
    	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();	// request
		boolean isAdmMode = StringUtil.getBoolean(request.getAttribute("isAdmMode"));											// 관리자사이트여부
		if(!isAdmMode) return result;
		String accessUrl = request.getScheme() + "//" + request.getServerName() + request.getRequestURI() + ((!StringUtil.isEmpty(request.getQueryString()))?"?" + request.getQueryString():"");
    	
    	LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();				// 로그인 사용자 정보
    	String accessIdx = loginVO.getMemberIdx();					// 회원코드
		String accessId = loginVO.getMemberIdOrg();					// 회원ID
		String accessName = loginVO.getMemberNameOrg();				// 회원명
		String accessIp = ClientUtil.getClientIp(request);
		String userAgent = request.getHeader("User-Agent");			// userAgent

		int menuType = StringUtil.getInt(request.getAttribute("menuType"));									// 2:메뉴콘텐츠관리 사용자사이트 메뉴
		String crtSiteName = (menuType == 2)?"usrSiteInfo":"siteInfo";
		String crtMenuName = (menuType == 2)?"usrCrtMenu":"crtMenu";
		JSONObject siteInfo = JSONObjectUtil.getJSONObject(request.getAttribute(crtSiteName));
		JSONObject crtMenu = JSONObjectUtil.getJSONObject(request.getAttribute(crtMenuName));				// 현재 메뉴 설정정보
		
		String siteId = JSONObjectUtil.getString(siteInfo, "site_id");			// 사이트ID
		int verIdx = JSONObjectUtil.getInt(siteInfo, "ver_idx");				// 버전일련번호
		int menuIdx = JSONObjectUtil.getInt(crtMenu, "menu_idx");				// 메뉴일련번호
		String siteName = JSONObjectUtil.getString(siteInfo, "site_name");		// 사이트명
		String menuName = JSONObjectUtil.getString(crtMenu, "menu_name");		// 메뉴명
		String confModule = JSONObjectUtil.getString(crtMenu, "conf_module");	// conf 모듈
		String moduleId = JSONObjectUtil.getString(crtMenu, "module_id");		// 모듈ID
		int fnIdx = JSONObjectUtil.getInt(crtMenu, "fn_idx");					// 기능IDX
	    String logTypeName = null;
	    if(logType == 1) logTypeName = "등록";
	    else if(logType == 2) logTypeName = "수정";
	    else if(logType == 3) logTypeName = "삭제";
	    else if(logType == 4) logTypeName = "복원";
	    else if(logType == 5) logTypeName = "완전삭제";
	    else if(logType == 9) logTypeName = "적용";
		for(Object keyCode:keyCodes) {
			Map<String, Object> param = new HashMap<String, Object>();
			List<DTForm> dataList = new ArrayList<DTForm>();
			
			//long logIdx = mngLogDAO.getNextId(null);
	    	
	    	// 등록항목 setting
	    	//dataList.add(new DTForm("LOG_IDX", logIdx));							// 로그일련번호
	    	dataList.add(new DTForm("LOG_TYPE", logType));							// 로그구분
	    	dataList.add(new DTForm("LOG_TYPE_NAME", logTypeName));					// 로그구분명
	    	dataList.add(new DTForm("SITE_ID", siteId));							// 사이트 ID
	    	dataList.add(new DTForm("SITE_NAME", siteName));						// 사이트명
	    	dataList.add(new DTForm("VER_IDX", verIdx));							// 버전일련번호
	    	dataList.add(new DTForm("MENU_IDX", menuIdx));							// 접속메뉴일련번호
	    	dataList.add(new DTForm("MENU_NAME", menuName));						// 접속메뉴명
	    	dataList.add(new DTForm("CONF_MODULE", confModule));					// conf 모듈
	    	dataList.add(new DTForm("MODULE_ID", moduleId));						// 모듈ID
	    	dataList.add(new DTForm("CONF_SMODULE", confSModule));					// 내부모듈명
	    	dataList.add(new DTForm("FN_IDX", fnIdx));								// 기능IDX
	    	dataList.add(new DTForm("ACCESS_URL", accessUrl));						// 접속경로
	    	dataList.add(new DTForm("USER_AGENT", userAgent));						// userAgent
	    	dataList.add(new DTForm("KEY_CODE", ((!StringUtil.isEmpty(preKeyCode))?preKeyCode + ",":"") + keyCode));							// KEY CODE
	    	dataList.add(new DTForm("ACCESS_IDX", accessIdx));						// 접속회원일련번호
	    	dataList.add(new DTForm("ACCESS_ID", accessId));						// 접속회원ID
	    	dataList.add(new DTForm("ACCESS_NAME", accessName));					// 접속회원명
	    	dataList.add(new DTForm("ACCESS_IP", accessIp));						// 접속IP
	    	
			param.put("dataList", dataList);
			result += mngLogDAO.insert(param);
		}
		return result;
	}
}