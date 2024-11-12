package rbs.modules.prof.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.util.ClientUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.egovframework.LoginVO;
import rbs.modules.prof.mapper.ProfMapper;
import rbs.modules.prof.mapper.ProfMariaMapper;
import rbs.modules.prof.service.ProfService;

/**
 * 샘플 모듈에 관한 구현 클래스를 정의한다.
 * @author user
 *
 */
@Service("profService")
public class ProfServiceImpl extends EgovAbstractServiceImpl implements ProfService {
	@Resource(name="profMapper")
	private ProfMapper profDAO;
	
	@Resource(name="profMariaMapper")
	private ProfMariaMapper profMariaDAO;
	
	@Override
	public DataMap getInitProfListCount(Map<String, Object> param) throws Exception {
		return profDAO.getInitProfListCount(param);
	}
	
	@Override
	public List<Object> getInitProfList(Map<String, Object> param) throws Exception {
		return profDAO.getInitProfList(param);
	}
	
	/**
	 * 상세조회
	 * @author 석수빈
	 * @param empNo
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getView(Map<String, Object> param) throws Exception {
		DataMap viewDAO = profDAO.getView(param);
		return viewDAO;
	}
	
	/**
	 * 연구번호조회
	 * @author 석수빈
	 * @param empNo
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getProfSn(Map<String, Object> param) throws Exception {
		DataMap viewDAO = profMariaDAO.getProfSn(param);
		return viewDAO;
	}
	
	/**
	 * 연구기록조회
	 * @author 석수빈
	 * @param empNo
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getProfRsh(Map<String, Object> param) throws Exception {
		DataMap viewDAO = profDAO.getProfRsh(param);
		return viewDAO;
	}
	
	/**
	 * 소속 대학 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getCollegeList() {
		return profDAO.getCollegeList();
	}
	
	/**
	 * 소속 대학 - 학부(과) 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDepartList(Map<String, Object> param) {
		return profDAO.getDepartList(param);
	}
	
	/**
	 * 소속 대학 - 학부(과) - 전공 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getMajorList(Map<String, Object> param) {
		return profDAO.getMajorList(param);
	}
	
	 /**
	 * 교수 강의과목 목록
	 * @param empNo
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(Map<String, Object> param) {
		return profDAO.getList(param);
	}
	
	/**
	 * 교수 강의과목 목록
	 * @param empNo
	 * @param param
	 * @return
	 */
    @Override
	public int getTotalCount(Map<String, Object> param) {
    	return profDAO.getTotalCount(param);
    }
	
	/**
	 * 관심 교과목 북마크 등록 리스트
	 * @param docId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getProfBk(List<Object> arr) throws Exception {
		return profDAO.getProfBk(arr);
	}
	
	/**
	 * 관심 교과목 북마크 등록 여부
	 * @param 
	 * @return
	 */
	@Override
	public int bkProfCount(Map<String, Object> param) throws Exception {
		return profDAO.bkProfCount(param);
	}
	
	/**
	 * 등록 처리 : 관심 교과목 북마크 등록
	 * @return
	 * @throws Exception
	 */
	@Override
	public int insertBkProf(Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		// LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser(); // 로그인 사용자 정보
		// SSO 값 꺼내쓰기
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		Map<String, Object> param = new HashMap<String, Object>(); // mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>(); // 저장 항목
		List<DTForm> searchList = new ArrayList<DTForm>(); // 검색 항목
		// 2.1 저장 항목
		List itemDataList = StringUtil.getList(paramMap.get("queryList"));
		
		if (itemDataList != null) dataList.addAll(itemDataList);
		if (itemDataList != null) searchList.addAll(itemDataList);
		
		// 개인 번호는 나중에 로그인할 때 얻는건지 확인 필요
		// dataList.add(new DTForm("PERS_NO", "20240112"));

		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		
		if (loginVO != null) {
			// loginMemberIdx = resultVO.getUserIdx();
			// loginMemberId = resultVO.getMemberId();
			// loginMemberName = resultVO.getMemberName();
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
		int result = profDAO.insertBkProf(param);
		
		return result;
	}
	
	/**
	 * 수정 : 관심 교과목 북마크 업데이트
	 */
	@Override
	public int updateBkProf(Map<String, Object> paramMap, HttpServletRequest request) throws Exception  {
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser(); // 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>(); // mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>(); // 저장 항목
		// SSO 값 꺼내쓰기
		HttpSession session = request.getSession(true); 
		// LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		// 검색 항목
		List itemDataList = StringUtil.getList(paramMap.get("searchList"));
		
		if (itemDataList != null) searchList.addAll(itemDataList);

		// 개인 번호는 나중에 로그인할 때 얻는건지 확인 필요
		searchList.add(new DTForm("PERS_NO", loginVO.getMemberId()));
		param.put("isdelete", paramMap.get("isdelete"));	
		param.put("searchList", searchList);	
		
		// 4. DB 저장
		int result = profDAO.updateBkProf(param);
		
		return result;
	}
}