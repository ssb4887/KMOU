package rbs.modules.member.service;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.woowonsoft.egovframework.annotation.ParamMap;
import com.woowonsoft.egovframework.form.ParamForm;

import rbs.egovframework.LoginVO;

/**
 * 일반 로그인, 인증서 로그인을 처리하는 비즈니스 인터페이스 클래스
 * @author 공통서비스 개발팀 박지욱
 * @since 2009.03.06
 * @version 1.0
 * @see
 *  
 * <pre>
 * << 개정이력(Modification Information) >>
 * 
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2009.03.06  박지욱          최초 생성 
 *  2011.08.26  서준식          EsntlId를 이용한 로그인 추가
 *  </pre>
 */
public interface LoginService {
	
	/**
	 * 로그인 처리
	 * @param siteMode
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
    LoginVO login(String siteMode, String regiIp, ParamForm parameterMap, JSONObject itemInfo, JSONObject settingInfo, JSONObject memberItemInfo) throws Exception;
    
    /**
	 * SNS 로그인 처리
	 * @param regiIp
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
    public LoginVO snsLogin(String regiIp, ParamForm parameterMap, JSONObject memberItemInfo) throws Exception;
   
    /**
	 * 사용자 그룹정보 얻기
     * @param siteMode
     * @param mbrCd
     * @return
     * @throws Exception
     */
    public List<Object> getGroupList(String mbrCd) throws Exception;
    
    /**
     * 마지막 로그인 정보 남기기
     * @param mbrCd
     * @param regiIp
     * @return
     * @throws Exception
     */
	public int loginUpdate(String mbrCd, String regiIp) throws Exception;
	
	
	public void loginProc(LoginVO resultVO);
	public void logout();
    
}
