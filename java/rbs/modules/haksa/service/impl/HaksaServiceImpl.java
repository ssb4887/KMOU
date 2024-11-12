package rbs.modules.haksa.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import rbs.egovframework.LoginVO;
import rbs.modules.haksa.mapper.HaksaMapper;
import rbs.modules.haksa.service.HaksaService;
import rbs.modules.sample.service.SampleService;
import rbs.modules.sbjt.mapper.SbjtFileMapper;
import rbs.modules.sbjt.mapper.SbjtMapper;
import rbs.modules.sbjt.mapper.SbjtMultiMapper;
import rbs.modules.sbjt.service.SbjtService;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.ClientUtil;
import com.woowonsoft.egovframework.util.FileUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * 샘플모듈에 관한 구현클래스를 정의한다.
 * @author user
 *
 */
@Service("haksaService")
public class HaksaServiceImpl extends EgovAbstractServiceImpl implements HaksaService {

	@Resource(name="haksaMapper")
	private HaksaMapper haksaDAO;
	
	/**
	 * 관심교과목 북마크 등록 리스트
	 * @param docId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getHaksaBk(List<Object> arr) throws Exception {
		return haksaDAO.getHaksaBk(arr);
	}
	
	/**
	 * 관심교과목 북마크 등록 여부
	 * @param 
	 * @return
	 */
	@Override
	public int bkHaksaCount(Map<String, Object> param) throws Exception {
		return haksaDAO.bkHaksaCount(param);
	}
	
	/**
	 * 등록처리 : 관심교과목 북마크 등록
	 * @return
	 * @throws Exception
	 */
	@Override
	public int insertBkHaksa(Map<String, Object> paramMap, HttpServletRequest request) throws Exception  {
		//LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		
		//SSO값 꺼내쓰기
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색항목
		
		// 2.1 저장항목
		List itemDataList = StringUtil.getList(paramMap.get("queryList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		if(itemDataList != null) searchList.addAll(itemDataList);
		
		//개인번호는 나중에 로그인할때 얻는건지 확인 필요
		//dataList.add(new DTForm("PERS_NO", "20240112"));

		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
//			loginMemberIdx = resultVO.getUserIdx();
//			loginMemberId = resultVO.getMemberId();
//			loginMemberName = resultVO.getMemberName();
			loginMemberIdx = "20140748";
			loginMemberId = "telpion";
			loginMemberName = "홍길동";
		}
		dataList.add(new DTForm("REGI_IDX", loginMemberIdx));
		dataList.add(new DTForm("REGI_ID", loginMemberId));
		dataList.add(new DTForm("REGI_NAME", loginMemberName));
		dataList.add(new DTForm("REGI_IP", ClientUtil.getClientIp(request)));

		param.put("searchList", searchList);	
		param.put("dataList", dataList);	
		// 4. DB 저장
		
		int result = haksaDAO.insertBkHaksa(param);
		return result;
	}
	
	/**
	 * 수정 : 관심교과목 북마크 업데이트
	 */
	@Override
	public int updateBkHaksa(Map<String, Object> paramMap, HttpServletRequest request) throws Exception  {
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 저장항목
		
		//SSO값 꺼내쓰기
		HttpSession session = request.getSession(true); 
		//LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		
		// 검색 항목
		List itemDataList = StringUtil.getList(paramMap.get("searchList"));
		if(itemDataList != null) searchList.addAll(itemDataList);

		//개인번호는 나중에 로그인할때 얻는건지 확인 필요
		searchList.add(new DTForm("PERS_NO", loginVO.getMemberId()));
		
		param.put("isdelete", paramMap.get("isdelete"));	
		param.put("searchList", searchList);	
		
		// 4. DB 저장
		
		int result = haksaDAO.updateBkHaksa(param);
		return result;
	}

}