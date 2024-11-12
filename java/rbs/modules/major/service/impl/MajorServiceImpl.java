package rbs.modules.major.service.impl;

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

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.egovframework.LoginVO;
import rbs.modules.major.mapper.MajorFileMapper;
import rbs.modules.major.mapper.MajorMapper;
import rbs.modules.major.mapper.MajorMultiMapper;
import rbs.modules.major.service.MajorService;

/**
 * 샘플 모듈에 관한 구현 클래스를 정의한다.
 * @author user
 *
 */
@Service("majorService")
public class MajorServiceImpl extends EgovAbstractServiceImpl implements MajorService {
	@Resource(name="majorMapper")
	private MajorMapper majorDAO;
	
	@Resource(name="majorFileMapper")
	private MajorFileMapper majorFileDAO;
	
	@Resource(name="majorMultiMapper")
	private MajorMultiMapper majorMultiDAO;
	
	/**
	 * 전공 최초 조회
	 * @return
	 */
	@Override
	public List<Object> getInitMajorList(Map<String, Object> param) throws Exception {
		return majorDAO.getInitMajorList(param);
	}
	
	
	@Override
	public DataMap getInitMajorListCount(Map<String, Object> param) throws Exception {
		return majorDAO.getInitMajorListCount(param);
	}
	
	/**
	 * 전공 - 상세조회
	 * @param majorCd
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getView(Map<String, Object> param) throws Exception {
		DataMap data = majorDAO.getView(param);
		String grdtAfterCarrier = data.get("GRDT_AF_CARR").toString().replace("\n", "<br><br>");
		String goal = data.get("GOAL").toString().replace("\n", "<br>");
		data.put("GRDT_AF_CARR", grdtAfterCarrier);
		data.put("GOAL", goal);
		return data;
	}
	
	/**
	 * 전공 - 인재상
	 * @param majorCd
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getMajorAbty(Map<String, Object> param) throws Exception {
		return majorDAO.getMajorAbty(param);
	}
	
	/**
	 * 전공 - 하위역량 유무 체크
	 * @param majorCd
	 * @param param
	 * @return
	 */
	public int checkAbty(Map<String, Object> param) {
    	return majorDAO.checkAbty(param);
    }
	
	/**
	 * 전공 - 전공능력(정의 및 하위역량)
	 * @param majorCd
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getMajorAbtyDef(Map<String, Object> param) throws Exception {
		return majorDAO.getMajorAbtyDef(param);
	}
	
	/**
	 * 전공 - 진출분야별 교육과정
	 * @param majorCd
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getMajorSbjtList(Map<String, Object> param) throws Exception {
		return majorDAO.getMajorSbjtList(param);
	}
	
	/**
	 * 교과목 직업 직무 코드명
	 * @param docId
	 * @return
	 */
	@Override
	public List<Object> getJobCdNm(Map<String, Object> jobparam) throws Exception {
		return majorDAO.getJobCdNm(jobparam);
	}
	
	/**
	 * 관심 교과목 북마크 등록 리스트
	 * @param docId
	 * @return
	 */
	@Override
	public List<Object> getCourBk(Map<String, Object> courParam) throws Exception {
		return majorDAO.getCourBk(courParam);
	}
	
	/**
	 * 관심 교과목 북마크 등록 여부
	 * @param 
	 * @return
	 */
	@Override
	public int bkCourCount(Map<String, Object> param) throws Exception {
		return majorDAO.bkCourCount(param);
	}
	
	/**
	 * 등록 처리 : 관심 교과목 북마크 등록
	 * @return
	 * @throws Exception
	 */
	@Override
	public int insertBkCour(Map<String, Object> paramMap, HttpServletRequest request) throws Exception  {
		Map<String, Object> param = new HashMap<String, Object>(); // mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>(); // 저장 항목
		List<DTForm> searchList = new ArrayList<DTForm>(); // 검색 항목
		// 2.1 저장 항목
		List itemDataList = StringUtil.getList(paramMap.get("queryList"));
		
		if (itemDataList != null) dataList.addAll(itemDataList);
		if (itemDataList != null) searchList.addAll(itemDataList);

		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		String memberId = loginVO.getMemberId();
		String memberName = loginVO.getMemberName();

		dataList.add(new DTForm("REGI_IDX", memberId));
		dataList.add(new DTForm("REGI_ID", memberId));
		dataList.add(new DTForm("REGI_NAME", memberName));
		dataList.add(new DTForm("REGI_IP", ClientUtil.getClientIp(request)));
		param.put("searchList", searchList);
		param.put("dataList", dataList);
		
		// 4. DB 저장
		int result = majorDAO.insertBkCour(param);
		
		return result;
	}
	
	/**
	 * 수정 : 관심 교과목 북마크 업데이트
	 */
	@Override
	public int updateBkCour(Map<String, Object> paramMap, HttpServletRequest request) throws Exception  {
		Map<String, Object> param = new HashMap<String, Object>(); // mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>(); // 검색 항목
		List<DTForm> dataList = new ArrayList<DTForm>(); // 저장 항목
		// 검색 항목
		List itemDataList = StringUtil.getList(paramMap.get("searchList"));
		
		if (itemDataList != null) searchList.addAll(itemDataList);
		
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		String memberId = loginVO.getMemberId();
		String memberName = loginVO.getMemberName();

		dataList.add(new DTForm("REGI_IDX", memberId));
		dataList.add(new DTForm("REGI_ID", memberId));
		dataList.add(new DTForm("REGI_NAME", memberName));
		dataList.add(new DTForm("REGI_IP", ClientUtil.getClientIp(request)));
		param.put("isdelete", paramMap.get("isdelete"));
		param.put("searchList", searchList);
		param.put("dataList", dataList);
		
		// 4. DB 저장
		int result = majorDAO.updateBkCour(param);
		
		return result;
	}
	
	/**
	 * 관심 강좌 북마크 등록 리스트
	 * @param docId
	 * @return
	 */
	@Override
	public List<Object> getLectureBk(Map<String, Object> lectureParam) throws Exception {
		return majorDAO.getLectureBk(lectureParam);
	}
	
	/**
	 * 관심 강좌 북마크 등록 여부
	 * @param 
	 * @return
	 */
	@Override
	public int bkLectureCount(Map<String, Object> param) throws Exception {
		return majorDAO.bkLectureCount(param);
	}
	
	/**
	 * 등록 처리 : 관심 강좌 북마크 등록
	 * @return
	 * @throws Exception
	 */
	@Override
	public int insertBkLecture(Map<String, Object> paramMap, HttpServletRequest request) throws Exception  {
		Map<String, Object> param = new HashMap<String, Object>(); // mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>(); // 저장 항목
		List<DTForm> searchList = new ArrayList<DTForm>(); // 검색 항목
		// 2.1 저장 항목
		List itemDataList = StringUtil.getList(paramMap.get("queryList"));
		
		if (itemDataList != null) dataList.addAll(itemDataList);
		if (itemDataList != null) searchList.addAll(itemDataList);
		
		// SSO 값 꺼내쓰기
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
	
		dataList.add(new DTForm("REGI_IDX", loginVO.getMemberId()));
		dataList.add(new DTForm("REGI_ID", loginVO.getMemberId()));
		dataList.add(new DTForm("REGI_NAME", loginVO.getMemberName()));
		dataList.add(new DTForm("REGI_IP", ClientUtil.getClientIp(request)));
		param.put("searchList", searchList);
		param.put("dataList", dataList);
		
		// 4. DB 저장
		int result = majorDAO.insertBkLecture(param);
		
		return result;
	}
	
	/**
	 * 수정 : 관심 강좌 북마크 업데이트
	 */
	@Override
	public int updateBkLecture(Map<String, Object> paramMap,HttpServletRequest request) throws Exception  {
		Map<String, Object> param = new HashMap<String, Object>(); // mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>(); // 검색 항목
		List<DTForm> dataList = new ArrayList<DTForm>(); // 저장 항목
		// 검색 항목
		List itemDataList = StringUtil.getList(paramMap.get("searchList"));
		
		if (itemDataList != null) searchList.addAll(itemDataList);
		
		// SSO 값 꺼내쓰기
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
	
		dataList.add(new DTForm("REGI_IDX", loginVO.getMemberId()));
		dataList.add(new DTForm("REGI_ID", loginVO.getMemberId()));
		dataList.add(new DTForm("REGI_NAME", loginVO.getMemberName()));
		dataList.add(new DTForm("REGI_IP", ClientUtil.getClientIp(request)));
		param.put("isdelete", paramMap.get("isdelete"));
		param.put("searchList", searchList);
		param.put("dataList", dataList);
		
		// 4. DB 저장
		int result = majorDAO.updateBkLecture(param);
		
		return result;
	}
	
	/**
	 * 권한 여부
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public int getAuthCount(int fnIdx, Map<String, Object> param) {
    	param.put("fnIdx", fnIdx);
    	
    	return majorDAO.getAuthCount(param);
    }
}