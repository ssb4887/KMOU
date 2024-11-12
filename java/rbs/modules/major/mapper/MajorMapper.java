package rbs.modules.major.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.woowonsoft.egovframework.form.DataMap;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

/**
 * 샘플 모듈에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("majorMapper")
public class MajorMapper extends EgovAbstractMapper { 
	
	/**
	 * 전공목록 최초 조회
	 * @return
	 */
	public List<Object> getInitMajorList(Map<String, Object> param) {
		return selectList("rbs.modules.major.majorMapper.getInitMajorList", param);
	}
	
	/**
	 * 전공 전체 건 수 최초 조회
	 * @return
	 */
	public DataMap getInitMajorListCount(Map<String, Object> param) {
		return (DataMap)selectOne("rbs.modules.major.majorMapper.getInitMajorListCount", param);
	}
	
	/**
	 * 전공 - 상세조회
	 * @param param
	 * @return
	 */
	public DataMap getView(Map<String, Object> param) {
		return (DataMap)selectOne("rbs.modules.major.majorMapper.selectView", param);
	}
	
	/**
	 * 전공 - 인재상
	 * @param param
	 * @return
	 */
	public List<Object> getMajorAbty(Map<String, Object> param) {
		return selectList("rbs.modules.major.majorMapper.selectMajorAbty", param);
	}
	
	/**
	 * 전공 - 하위역량 유무 체크
	 * @param param
	 * @return
	 */
	public int checkAbty(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.major.majorMapper.checkAbty", param);
    }
	
	/**
	 * 전공 - 전공능력(정의 및 하위역량)
	 * @param param
	 * @return
	 */
	public List<Object> getMajorAbtyDef(Map<String, Object> param) {
		return selectList("rbs.modules.major.majorMapper.selectMajorAbtyDef", param);
	}
	
	/**
	 * 전공 - 진출분야별 교육과정
	 * @param param
	 * @return
	 */
	public List<Object> getMajorSbjtList(Map<String, Object> param) {
		return selectList("rbs.modules.major.majorMapper.selectMajorSbjtList", param);
	}
	
	/**
	 * 교과목 직업 직무 코드명
	 * @param param
	 * @return
	 */
	public List<Object> getJobCdNm(Map<String, Object> param) {
		return selectList("rbs.modules.major.majorMapper.getJobCdNm", param);
	}
	
	/**
	 * 관심 교과목 북마크 등록 리스트 
	 * @param param
	 * @return
	 */
	public List<Object> getCourBk(Map<String, Object> param) {
		return selectList("rbs.modules.major.majorMapper.getCourBk", param);
	}
	
	/**
	 * 관심 교과목 북마크 등록 여부
	 * @param param 
	 * @return int 갯수
	 */
	public int bkCourCount(Map<String, Object> param) {
		return selectOne("rbs.modules.major.majorMapper.bkCourCount", param);
	}
	
	/**
	 * 관심 교과목 북마크 등록
	 * @param param 등록 정보
	 * @return String 등록 결과
	 */
	public int insertBkCour(Map<String, Object> param) {
		return super.insert("rbs.modules.major.majorMapper.insertBkCour", param);
	}

	/**
	 * 관심 교과목 북마크 업데이트
	 * @param param 등록 정보
	 * @return String 등록 결과
	 */
	public int updateBkCour(Map<String, Object> param) {
		return super.insert("rbs.modules.major.majorMapper.updateBkCour", param);
	}
	
	/**
	 * 관심 강좌 북마크 등록 리스트
	 * @param param
	 * @return
	 */
	public List<Object> getLectureBk(Map<String, Object> lectureParam) {
		return selectList("rbs.modules.major.majorMapper.getLectureBk", lectureParam);
	}
	
	/**
	 * 관심 강좌 북마크 등록 여부
	 * @param param 
	 * @return int 갯수
	 */
	public int bkLectureCount(Map<String, Object> param) {
		return selectOne("rbs.modules.major.majorMapper.bkLectureCount", param);
	}
	
	/**
	 * 관심 강좌 북마크 등록
	 * @param param 등록 정보
	 * @return String 등록 결과
	 */
	public int insertBkLecture(Map<String, Object> param) {
		return super.insert("rbs.modules.major.majorMapper.insertBkLecture", param);
	}
	
	/**
	 * 관심 강좌 북마크 업데이트
	 * @param param 등록 정보
	 * @return String 등록 결과
	 */
	public int updateBkLecture(Map<String, Object> param) {
		return super.insert("rbs.modules.major.majorMapper.updateBkLecture", param);
	}
	
	 /**
     * 권한 여부 조회
     * @param param
     * @return
     */
    public int getAuthCount(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.major.majorMapper.authCount", param);
    }
}