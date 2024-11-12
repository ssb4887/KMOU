package rbs.modules.prof.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.woowonsoft.egovframework.form.DataMap;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;
import rbs.egovframework.mapper.MartAbstractMapper;

/**
 * 샘플 모듈에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("profMapper")
public class ProfMapper extends MartAbstractMapper {
	
	/**
	 * 교수 최초 조회
	 * @return
	 */
	public List<Object> getInitProfList(Map<String, Object> param) {
		return selectList("mart.modules.prof.profMapper.getInitProfList", param);
	}
	
	/**
	 * 교수 전체 건 수 최초 조회
	 * @return
	 */
	public DataMap getInitProfListCount(Map<String, Object> param) {
		return (DataMap)selectOne("mart.modules.prof.profMapper.getInitProfListCount", param);
	}
	
	
	/**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param subjectCd 검색조건
     * @return 상세정보
     */
	public DataMap getView(Map<String, Object> param) {
         return (DataMap)selectOne("mart.modules.prof.profMapper.selectView", param);
	}
	
	/**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param V_TEA_SN 검색조건
     * @return 연구기록조회
     */
	public DataMap getProfRsh(Map<String, Object> param) {
         return (DataMap)selectOne("mart.modules.prof.profMapper.selectProfRsh", param);
	}
	
	/**
	 * 소속 대학 목록
	 * @param param
	 * @return
	 */
	public List<Object> getCollegeList() {
		return selectList("mart.modules.prof.profMapper.getCollegeList");
	}
	
	/**
	 * 소속 대학 - 학부(과) 목록
	 * @param param
	 * @return
	 */
	public List<Object> getDepartList(Map<String, Object> param) {
		return selectList("mart.modules.prof.profMapper.getDepartList", param);
	}
	
	/**
	 * 소속 대학 - 학부(과) - 전공 목록
	 * @param param
	 * @return
	 */
	public List<Object> getMajorList(Map<String, Object> param) {
		return selectList("mart.modules.prof.profMapper.getMajorList", param);
	}
	
	/**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getList(Map<String, Object> param){
		return (List<Object>)selectList("mart.modules.prof.profMapper.selectSubList", param);
    }
	
	/**
     * 강의과목 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getTotalCount(Map<String, Object> param) {
    	return (Integer)selectOne("mart.modules.prof.profMapper.selectCount", param);
    }
	
	/**
	 * 관심 교과목 북마크 등록 리스트 
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getProfBk(List<Object> arr) {
		return selectList("mart.modules.prof.profMapper.getProfBk", arr);
	}
	
	/**
	 * 관심 교과목 북마크 등록 여부
	 * @param param 
	 * @return int 갯수
	 */
	public int bkProfCount(Map<String, Object> param) {
		return selectOne("mart.modules.prof.profMapper.bkProfCount", param);
	}
	
	/**
	 * 관심 교과목 북마크 등록
	 * @param param 등록 정보
	 * @return String 등록 결과
	 */
	public int insertBkProf(Map<String, Object> param) {
		return super.insert("mart.modules.prof.profMapper.insertBkProf", param);
	}

	/**
	 * 관심 교과목 북마크 업데이트
	 * @param param 등록 정보
	 * @return String 등록 결과
	 */
	public int updateBkProf(Map<String, Object> param) {
		return super.insert("mart.modules.prof.profMapper.updateBkProf", param);
	}
}