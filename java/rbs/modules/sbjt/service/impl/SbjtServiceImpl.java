package rbs.modules.sbjt.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.util.ClientUtil;
import com.woowonsoft.egovframework.util.StringUtil;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.egovframework.LoginVO;
import rbs.modules.sbjt.mapper.SbjtFileMapper;
import rbs.modules.sbjt.mapper.SbjtMapper;
import rbs.modules.sbjt.mapper.SbjtMultiMapper;
import rbs.modules.sbjt.service.SbjtService;

/**
 * 샘플 모듈에 관한 구현 클래스를 정의한다.
 * @author user
 *
 */
@Service("sbjtService")
public class SbjtServiceImpl extends EgovAbstractServiceImpl implements SbjtService {
	@Resource(name="sbjtMapper")
	private SbjtMapper sbjtDAO;
	
	@Resource(name="sbjtFileMapper")
	private SbjtFileMapper sbjtFileDAO;
	
	@Resource(name="sbjtMultiMapper")
	private SbjtMultiMapper sbjtMultiDAO;
	
	
	/**
	 * 교과목 직업 직무 코드명
	 * @param docId
	 * @return
	 */
	@Override
	public List<Object> getJobCdNm(Map<String, Object> jobparam) throws Exception {
		return sbjtDAO.getJobCdNm(jobparam);
	}
	
	/**
	 * 관심 교과목 북마크 등록 리스트
	 * @param docId
	 * @return
	 */
	@Override
	public List<Object> getCourBk(Map<String, Object> courParam) throws Exception {
		return sbjtDAO.getCourBk(courParam);
	}
	
	/**
	 * 관심 교과목 북마크 등록 여부
	 * @param 
	 * @return
	 */
	@Override
	public int bkCourCount(Map<String, Object> param) throws Exception {
		return sbjtDAO.bkCourCount(param);
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
		int result = sbjtDAO.insertBkCour(param);
		
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
		int result = sbjtDAO.updateBkCour(param);
		
		return result;
	}
	
	/**
	 * 관심 강좌 북마크 등록 리스트
	 * @param docId
	 * @return
	 */
	@Override
	public List<Object> getLectureBk(Map<String, Object> lectureParam) throws Exception {
		return sbjtDAO.getLectureBk(lectureParam);
	}
	
	/**
	 * 관심 강좌 북마크 등록 여부
	 * @param 
	 * @return
	 */
	@Override
	public int bkLectureCount(Map<String, Object> param) throws Exception {
		return sbjtDAO.bkLectureCount(param);
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
		int result = sbjtDAO.insertBkLecture(param);
		
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
		int result = sbjtDAO.updateBkLecture(param);
		
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
    	
    	return sbjtDAO.getAuthCount(param);
    }
}