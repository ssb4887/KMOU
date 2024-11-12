package rbs.modules.bookmark.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import rbs.egovframework.mapper.RbsAbstractMapper;

/**
 * 샘플모듈에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("bookmarkMapper")
public class BookmarkMapper extends RbsAbstractMapper{

	/**
	 * 찜 등록
	 * */
	public int insertBookmark(Map<String, Object> param) {
		return super.insert("rbs.modules.bookmark.bookmarkMapper.insertBookmark", param);
	}

	public int deleteBookmark(Map<String, Object> param) {
		return super.delete("rbs.modules.bookmark.bookmarkMapper.deleteBookmark", param);
	}

	public int getBookmarkCount(Map<String, Object> bookMarkparam) {
		return super.selectOne("rbs.modules.bookmark.bookmarkMapper.getBookmarkCount", bookMarkparam);
	}

	public int deleteSelectedBookmark(Map<String, Object> param) {
		return super.delete("rbs.modules.bookmark.bookmarkMapper.deleteSelectedBookmark", param);
	}
	
	public String getIsSbjtInsert(Map<String, Object> param) {
		return super.selectOne("rbs.modules.bookmark.bookmarkMapper.getIsSbjtInsert", param);
	}

	public List<Object> getBookmarkList(Map<String, Object> param) {
		return super.selectList("rbs.modules.bookmark.bookmarkMapper.getBookmarkList", param);
	}
	
	public List<Object> getMyLoveCount(Map<String, Object> param) {
		return super.selectList("rbs.modules.bookmark.bookmarkMapper.getMyLoveCount", param);
	}
	
	
	/**
     * 찜하기 총 수
     * @param param 검색조건
     * @return int 총갯수
     */
	public int getBookmarkTypeCount(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.bookmark.bookmarkMapper.selectBookmarkTypeCount", param);
    }
	
	
	public List<Object> getMajorView(Map<String, Object> param) {
		return (List<Object>)selectList("rbs.modules.bookmark.bookmarkMapper.getMajorView", param);
	}

    public List<Object> getStudPlanView(Map<String, Object> param) {
    	return (List<Object>)selectList("rbs.modules.bookmark.bookmarkMapper.getStudPlanView", param);
    }
	
	
	
    
}