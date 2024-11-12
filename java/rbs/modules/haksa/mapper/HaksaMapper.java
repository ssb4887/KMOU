package rbs.modules.haksa.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.woowonsoft.egovframework.form.DataMap;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

/**
 * 샘플모듈에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("haksaMapper")
public class HaksaMapper extends EgovAbstractMapper{

	/**
	 * 관심교과목 북마크 등록 리스트 
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getHaksaBk(List<Object> arr) {
		return selectList("rbs.modules.haksa.haksaMapper.getHaksaBk", arr);
	}
	
	/**
	 * 관심교과목 북마크 등록 여부
	 * @param param 
	 * @return int 갯수
	 */
	public int bkHaksaCount(Map<String, Object> param){
		return selectOne("rbs.modules.haksa.haksaMapper.bkHaksaCount",param);
	}
	
	/**
	 * 관심교과목 북마크 등록
	 * @param param 등록정보
	 * @return String 등록결과
	 */
	public int insertBkHaksa(Map<String, Object> param){
		return super.insert("rbs.modules.haksa.haksaMapper.insertBkHaksa",param);
	}

	/**
	 * 관심교과목 북마크 업데이트
	 * @param param 등록정보
	 * @return String 등록결과
	 */
	public int updateBkHaksa(Map<String, Object> param){
		return super.insert("rbs.modules.haksa.haksaMapper.updateBkHaksa",param);
	}
}