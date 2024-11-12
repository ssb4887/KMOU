package rbs.modules.sbjt.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.woowonsoft.egovframework.form.DataMap;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

/**
 * 샘플 모듈에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("sbjtMapper")
public class SbjtMapper extends EgovAbstractMapper {	
	/**
	 * 교과목 직업 직무 코드명
	 * @param param
	 * @return
	 */
	public List<Object> getJobCdNm(Map<String, Object> param) {
		return selectList("rbs.modules.sbjt.sbjtMapper.getJobCdNm", param);
	}
	
	/**
	 * 관심 교과목 북마크 등록 리스트 
	 * @param param
	 * @return
	 */
	public List<Object> getCourBk(Map<String, Object> param) {
		return selectList("rbs.modules.sbjt.sbjtMapper.getCourBk", param);
	}
	
	/**
	 * 관심 교과목 북마크 등록 여부
	 * @param param 
	 * @return int 갯수
	 */
	public int bkCourCount(Map<String, Object> param) {
		return selectOne("rbs.modules.sbjt.sbjtMapper.bkCourCount", param);
	}
	
	/**
	 * 관심 교과목 북마크 등록
	 * @param param 등록 정보
	 * @return String 등록 결과
	 */
	public int insertBkCour(Map<String, Object> param) {
		return super.insert("rbs.modules.sbjt.sbjtMapper.insertBkCour", param);
	}

	/**
	 * 관심 교과목 북마크 업데이트
	 * @param param 등록 정보
	 * @return String 등록 결과
	 */
	public int updateBkCour(Map<String, Object> param) {
		return super.insert("rbs.modules.sbjt.sbjtMapper.updateBkCour", param);
	}
	
	/**
	 * 관심 강좌 북마크 등록 리스트
	 * @param param
	 * @return
	 */
	public List<Object> getLectureBk(Map<String, Object> lectureParam) {
		return selectList("rbs.modules.sbjt.sbjtMapper.getLectureBk", lectureParam);
	}
	
	/**
	 * 관심 강좌 북마크 등록 여부
	 * @param param 
	 * @return int 갯수
	 */
	public int bkLectureCount(Map<String, Object> param) {
		return selectOne("rbs.modules.sbjt.sbjtMapper.bkLectureCount", param);
	}
	
	/**
	 * 관심 강좌 북마크 등록
	 * @param param 등록 정보
	 * @return String 등록 결과
	 */
	public int insertBkLecture(Map<String, Object> param) {
		return super.insert("rbs.modules.sbjt.sbjtMapper.insertBkLecture", param);
	}
	
	/**
	 * 관심 강좌 북마크 업데이트
	 * @param param 등록 정보
	 * @return String 등록 결과
	 */
	public int updateBkLecture(Map<String, Object> param) {
		return super.insert("rbs.modules.sbjt.sbjtMapper.updateBkLecture", param);
	}
	
	 /**
     * 권한 여부 조회
     * @param param
     * @return
     */
    public int getAuthCount(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.sbjt.sbjtMapper.authCount", param);
    }
}