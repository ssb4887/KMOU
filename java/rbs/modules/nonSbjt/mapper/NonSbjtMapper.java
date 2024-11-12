package rbs.modules.nonSbjt.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.woowonsoft.egovframework.form.DataMap;

import rbs.egovframework.mapper.OceanAbstractMapper;

/**
 * 비교과에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("nonSbjtMapper")
public class NonSbjtMapper extends OceanAbstractMapper {
	
	/**
	 * 비교과 최초 조회
	 * @return
	 */
	public List<Object> getInitNonSbjtList(Map<String, Object> param) {
		return selectList("rbs.modules.nonSbjt.nonSbjtMapper.getInitNonSbjtList", param);
	}
	
	/**
	 * 비교과 전체 건 수 최초 조회
	 * @param param 
	 * @return
	 */
	public DataMap getInitNonSbjtListCount(Map<String, Object> param) {
		return (DataMap)selectOne("rbs.modules.nonSbjt.nonSbjtMapper.getInitNonSbjtListCount", param);
	}
	
	
	/**
	 * 비교과 리스트(겁색 값의 유무에 따라 분기)
	 * @param idList
	 * @return
	 */
	public List<Object> getNonSbjtListEmptySearchY(Map<String, Object> param) {
		return selectList("rbs.modules.nonSbjt.nonSbjtMapper.getNonSbjtListEmptySearchY", param);
	}
	public List<Object> getNonSbjtListEmptySearchN(Map<String, Object> param) {
		return selectList("rbs.modules.nonSbjt.nonSbjtMapper.getNonSbjtListEmptySearchN", param);
	}
	
	/**
	 * 비교과 핵심역량 분류
	 * @param parent
	 * @return
	 */
	public List<Object> getCategory(Map<String, Object> param) {
		return selectList("rbs.modules.nonSbjt.nonSbjtMapper.getCategory", param);
	}

	/**
	 * 비교과 태그
	 * @return
	 */
	public List<Object> getTag() {
		return selectList("rbs.modules.nonSbjt.nonSbjtMapper.getTag");
	}
	
	/**
	 * 비교과 프로그램 상세정보
	 * @param idx
	 * @return
	 */
	public DataMap getNonsbjtInfo(Map<String, Object> param) {
		return (DataMap)selectOne("rbs.modules.nonSbjt.nonSbjtMapper.getNonSbjtInfo", param);
	}

	/**
	 * 비교과 프로그램 태그
	 * @param idx
	 * @return
	 */
	public List<Object> getNonSbjtTag(Map<String, Object> param) {
		return selectList("rbs.modules.nonSbjt.nonSbjtMapper.getNonSbjtTag", param);
	}

	/**
	 * 비교과 프로그램 핵심역량 지수
	 * @param idx
	 * @return
	 */
	public List<Object> getNonSbjtEssential(Map<String, Object> param) {
		return selectList("rbs.modules.nonSbjt.nonSbjtMapper.getNonSbjtEssential", param);
	}

	/**
	 * 비교과 프로그램 첨부파일
	 * @return filesList
	 */
	public List<Object> getNonSbjtAttachmentFile(Map<String, Object> param) {
		return selectList("rbs.modules.nonSbjt.nonSbjtMapper.getNonSbjtAttachmentFile", param);
	}

	public List<Object> getNonSbjtHist(Map<String, Object> param) {
		return selectList("rbs.modules.nonSbjt.nonSbjtMapper.getNonSbjtHist", param);
	}

	public List<Object> getMyNonSbjtSigninHist(Map<String, Object> param) {
		return selectList("rbs.modules.nonSbjt.nonSbjtMapper.getMyNonSbjtSigninHist", param);
	}

	public List<Object> getProgramType() {
		return selectList("rbs.modules.nonSbjt.nonSbjtMapper.getProgramType");
	}

	public List<Object> getMethod() {
		return selectList("rbs.modules.nonSbjt.nonSbjtMapper.getMethod");
	}




}