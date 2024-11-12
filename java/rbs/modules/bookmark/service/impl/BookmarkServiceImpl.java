package rbs.modules.bookmark.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.util.StringUtil;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.egovframework.LoginVO;
import rbs.modules.bookmark.mapper.BookmarkMapper;
import rbs.modules.bookmark.mapper.BookmarkOraMapper;
import rbs.modules.bookmark.service.BookmarkService;
import rbs.modules.nonSbjt.mapper.NonSbjtMapper;

/**
 * 샘플모듈에 관한 구현클래스를 정의한다.
 * @author user
 *
 */
@Service("bookmarkService")
public class BookmarkServiceImpl extends EgovAbstractServiceImpl implements BookmarkService {

	@Resource(name="bookmarkMapper")
	private BookmarkMapper bookmarkDAO;
	
	@Resource(name="bookmarkOraMapper")
	private BookmarkOraMapper bookmarkOraDAO;
	
	@Resource(name="nonSbjtMapper")
	private NonSbjtMapper nonSbjtkDAO;
	
	@Override
	public int insertBookmark(String docId, HttpServletRequest request) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
		
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		String memberId = loginVO.getMemberIdOrg();	
		
		String regiIp = request.getRemoteAddr();
		String menuFg = request.getParameter("menuFg");
		
		dataList.add(new DTForm("STD_NO", memberId));
		dataList.add(new DTForm("MENU_FG", menuFg));
		dataList.add(new DTForm("DOC_ID", docId));
    	dataList.add(new DTForm("REGI_IP", regiIp));
    	
    	param.put("dataList", dataList);
    	
    	int result = bookmarkDAO.insertBookmark(param);
    	
    	//개설강좌를 찜할 시 해당 개설강좌가 속한 교과목도 같이 찜해줌
    	/*if(menuFg.equals("lecture")) {    		
    		param.put("docId",docId);
    		
    		
    		String SbjtDocId = bookmarkDAO.getIsSbjtInsert(param);
    		if(!SbjtDocId.equals("N")) {
    			dataList.clear();
    			param.clear();
    			
    			dataList.add(new DTForm("STD_NO", memberId));
    			dataList.add(new DTForm("MENU_FG", "sbjt"));
    			dataList.add(new DTForm("DOC_ID", SbjtDocId));
    			dataList.add(new DTForm("REGI_IP", regiIp));
    			
    			param.put("dataList", dataList);
    			
    			result = bookmarkDAO.insertBookmark(param);
    			
    		}
    	}*/
    	
		return result;
	}

	@Override
	public int deleteBookmark(String docId, HttpServletRequest request) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색항목
		
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		String memberId = loginVO.getMemberId();
		String menuFg = request.getParameter("menuFg");
		
		searchList.add(new DTForm("DOC_ID", docId));
		searchList.add(new DTForm("STD_NO", memberId));
		searchList.add(new DTForm("MENU_FG", menuFg));
		
		param.put("searchList", searchList);
		
		int result = bookmarkDAO.deleteBookmark(param);
		
		return result;
	}
	
	@Override
	public int deleteSelectedBookmark(String[] docIdList, HttpServletRequest request) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색항목
		
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		String memberId = loginVO.getMemberId();
		
		searchList.add(new DTForm("STD_NO", memberId));
						
		param.put("searchList", searchList);
		param.put("docIdList",docIdList);
		
		int result = bookmarkDAO.deleteSelectedBookmark(param);
		
		return result;
	}

	@Override
	public int deleteAllBookmark(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색항목
		
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		String memberId = loginVO.getMemberId();
		String menuFg = request.getParameter("menuFg");
		
		searchList.add(new DTForm("STD_NO", memberId));
		searchList.add(new DTForm("MENU_FG", menuFg));
		
		param.put("searchList", searchList);
		
		int result = bookmarkDAO.deleteBookmark(param);
		
		return result;
	}
	
	@Override
	public int getBookmarkCount(Map<String, Object> bookMarkparam) {
		return bookmarkDAO.getBookmarkCount(bookMarkparam);
	}

	@Override
	public List<Object> getBookmarkList(Map<String, Object> param) {
		return bookmarkDAO.getBookmarkList(param);
	}
	
	@Override
	public List<Object> getMyLoveCount(Map<String, Object> param) {
		return bookmarkDAO.getMyLoveCount(param);
	}
	
	/**
	 * 찜하기 목록 총 수
	 * @param stdNo
	 * @param param
	 * @return
	 */
	@Override
	public int getBookmarkTypeCount(Map<String, Object> param) {
    	return bookmarkDAO.getBookmarkTypeCount(param);
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Object> getBookmarkTabList(Map<String, Object> param) {
		List bookmarkList = bookmarkDAO.getBookmarkList(param);
		List bookmarkTabList = new ArrayList<Object>();
		Map<String, Object> tabParam = new HashMap<String, Object>();
		String menuFg = param.get("MENU_FG").toString();
		
		
	
		
		if(bookmarkList != null) {
			int bookmarkListSize = bookmarkList.size();
			for(int i = 0 ; i < bookmarkListSize ; i ++) {
				
				Map<String, Object> bookmarkParam = (Map<String, Object>)bookmarkList.get(i);
				
				if( StringUtil.isEquals(menuFg, "sbjt") ) {// 교과목 찜하기 목록
					String[] paramInfo = (bookmarkParam.get("docId").toString()).split("_");
					String subjectCd = paramInfo[0];
					String deptCd = paramInfo[1];
					String year = paramInfo[2];
					String grade = paramInfo[3];
					String smt = paramInfo[4];
					
					
					tabParam.put("subjectCd", subjectCd);
					tabParam.put("deptCd", deptCd);
					tabParam.put("year", year);
					tabParam.put("grade", grade);
					tabParam.put("smt", smt);
					
					bookmarkTabList.addAll(bookmarkOraDAO.getSbjtView(tabParam)); 
					
					
					tabParam.clear();
				} else if(StringUtil.isEquals(menuFg, "lecture")) {// 강좌 찜하기 목록
					String[] paramInfo = (bookmarkParam.get("docId").toString()).split("_");
					String subjectCd = paramInfo[0];
					String deptCd = paramInfo[1];
					String year = paramInfo[2];
					String grade = paramInfo[3];
					String smt = paramInfo[4];
					String divcls = paramInfo[5];
					String empNo = paramInfo[6];
					
					
					tabParam.put("subjectCd", subjectCd);
					tabParam.put("deptCd", deptCd);
					tabParam.put("year", year);
					tabParam.put("grade", grade);
					tabParam.put("smt", smt);
					tabParam.put("divcls", divcls);
					tabParam.put("empNo", empNo);
					
					bookmarkTabList.addAll(bookmarkOraDAO.getLecView(tabParam)); 
					
					
					tabParam.clear();
				} else if(StringUtil.isEquals(menuFg, "nonSbjt")) {// 비교과 찜하기 목록
					String[] paramInfo = (bookmarkParam.get("docId").toString()).split("_");
					String idx = paramInfo[0];
					String tidx = paramInfo[1];
					
					
					tabParam.put("idx", idx);
					tabParam.put("tidx", tidx);
					
					bookmarkTabList.add(nonSbjtkDAO.getNonsbjtInfo(tabParam)); 
					
					
					tabParam.clear();
				} else if(StringUtil.isEquals(menuFg, "prof")) {// 교수 찜하기 목록
					String[] paramInfo = (bookmarkParam.get("docId").toString()).split("_");
					String empNo = paramInfo[0];
					//String deptCd = paramInfo[1];
					
					
					tabParam.put("empNo", empNo);
					//tabParam.put("deptCd", deptCd);
					
					bookmarkTabList.addAll(bookmarkOraDAO.getProfView(tabParam)); 
					
					
					tabParam.clear();
				} else if(StringUtil.isEquals(menuFg, "major")) {// 전공 찜하기 목록
					String[] paramInfo = (bookmarkParam.get("docId").toString()).split("_");
					String majorCd = paramInfo[0];
					/*String deptCd = paramInfo[1];
					String colgCd = paramInfo[2];*/
					
					
					tabParam.put("majorCd", majorCd);
					/*tabParam.put("deptCd", deptCd);
					tabParam.put("colgCd", colgCd);*/
					
					bookmarkTabList.addAll(bookmarkDAO.getMajorView(tabParam)); 
					
					
					tabParam.clear();	
				} else if(StringUtil.isEquals(menuFg, "studPlan")) {// 학생설계 찜하기 목록
					tabParam.put("SDM_DEPT_CD", bookmarkParam.get("docId").toString());
					
					bookmarkTabList.addAll(bookmarkDAO.getStudPlanView(tabParam)); 
					
					
					tabParam.clear();
				}
				
			}
		}
		return bookmarkTabList;
	}
	


}