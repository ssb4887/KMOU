package rbs.modules.bookmark.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import egovframework.rte.psl.dataaccess.util.EgovMap;


/**
 * 찜모듈에 관한 인터페이스클래스를 정의한다.
 * @author LDG
 *
 */
public interface BookmarkService {

	/**
	 * 찜 등록
	 * @param bookmarkJson
	 * @param request
	 * */
	int insertBookmark(String bookmarkJson, HttpServletRequest request) throws Exception;

	/**
	 * 찜 삭제(다중)
	 * @param docIdList
	 * @param request
	 * */
	int deleteSelectedBookmark(String[] docIdList, HttpServletRequest request) throws Exception;
	/**
	 * 찜 삭제(선택)
	 * @param docIdList
	 * @param request
	 * */
	int deleteBookmark(String docId, HttpServletRequest request) throws Exception;

	/**
	 * 찜 전체삭제(선택한 탭만)
	 * @param request
	 * */
	int deleteAllBookmark(HttpServletRequest request) throws Exception;

	/**
	 * 찜 확인(단일)
	 * @param request
	 * */
	int getBookmarkCount(Map<String, Object> bookmarkparam) throws Exception;
	
	/**
	 * 찜 확인(목록)
	 * @param request
	 * */
	List<Object> getBookmarkList(Map<String, Object> param);
	
	/**
	 * 찜 메뉴별 갯수
	 * @param request
	 * */
	List<Object> getMyLoveCount(Map<String, Object> param);
	
	
	
	
	/**
	 * 찜하기 목록 총 수
	 * @param stdNo, menuFg
	 * @param param
	 * @return
	 */
	public int getBookmarkTypeCount(Map<String, Object> param) throws Exception;
	
	/**
	 * 찜하기 목록(마이페이지)
	 * @param request
	 * */
	List<Object> getBookmarkTabList(Map<String, Object> param);
	
	/*List<Object> getBookmarkSbjtList(Map<String, Object> param);
	
	List<Object> getBookmarkLecList(Map<String, Object> param);
	
	List<Object> getBookmarkNonSbjtList(Map<String, Object> param);
	
	List<Object> getBookmarkProfList(Map<String, Object> param);
	
	List<Object> getBookmarkMajorList(Map<String, Object> param);
	
	List<Object> getBookmarkStdPlanList(Map<String, Object> param);*/
	
	
	}