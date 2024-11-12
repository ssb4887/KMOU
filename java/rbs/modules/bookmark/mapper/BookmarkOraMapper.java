package rbs.modules.bookmark.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;




import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.AcademicAbstractMapper;
import rbs.egovframework.mapper.MartAbstractMapper;
import rbs.egovframework.mapper.RbsAbstractMapper;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.util.StringUtil;


@Repository("bookmarkOraMapper")
public class BookmarkOraMapper extends MartAbstractMapper{
	/**
	 * 마이페이지 나의 찜 교과목 상세정보
	 * @param param
	 * @return
	 */
    public List<Object> getSbjtView(Map<String, Object> param) {
    	return (List<Object>)selectList("mart.modules.bookmark.bookmarkOraMapper.getSbjtView", param);
    }
    
    
    public List<Object> getLecView(Map<String, Object> param) {
    	return (List<Object>)selectList("mart.modules.bookmark.bookmarkOraMapper.getLecView", param);
    }
    	
	public List<Object> getProfView(Map<String, Object> param) {
		return (List<Object>)selectList("mart.modules.bookmark.bookmarkOraMapper.getProfView", param);
	}
	
}