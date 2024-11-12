package rbs.modules.sbjt.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.MartAbstractMapper;
import rbs.egovframework.mapper.RbsAbstractMapper;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;

/**
 * 일반 회원 관리에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("sbjtOraMapper")
public class SbjtOraMapper extends MartAbstractMapper {
	
	
	/**
	 * 교과목 최초 조회
	 * @return
	 */
	public List<Object> getInitSbjtList(Map<String, Object> param) {
		return selectList("mart.modules.sbjt.sbjtOraMapper.getInitSbjtList", param);
	}
	
	/**
	 * 교과목 전체 건 수 최초 조회
	 * @return
	 */
	public DataMap getInitSbjtListCount(Map<String, Object> param) {
		return (DataMap)selectOne("mart.modules.sbjt.sbjtOraMapper.getInitSbjtListCount", param);
	}
	
	
	/**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param subjectCd 검색조건
     * @return 상세정보
     */
	public DataMap getView(Map<String, Object> param) {
         return (DataMap)selectOne("mart.modules.sbjt.sbjtOraMapper.selectView", param);
	}
	
	/**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param subjectCd 검색조건
     * @return 상세정보 전공능력/핵심역량
     */
	public DataMap getCoreView(Map<String, Object> param) {
         return (DataMap)selectOne("mart.modules.sbjt.sbjtOraMapper.selectCoreView", param);
	}
	
	/**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param subjectCd 검색조건
     * @return 상세정보 전공능력/핵심역량
     */
	public DataMap getAbiView(Map<String, Object> param) {
         return (DataMap)selectOne("mart.modules.sbjt.sbjtOraMapper.selectAbiView", param);
	}
	
	/**
	 * 주관 대학 목록
	 * @param param
	 * @return
	 */
	public List<Object> getCollegeList() {
		return selectList("mart.modules.sbjt.sbjtOraMapper.getCollegeList");
	}
	
	/**
	 * 주관 대학 - 학부(과) 목록
	 * @param param
	 * @return
	 */
	public List<Object> getDepartList(Map<String, Object> param) {
		return selectList("mart.modules.sbjt.sbjtOraMapper.getDepartList", param);
	}
	
	/**
	 * 주관 대학 - 학부(과) - 전공 목록
	 * @param param
	 * @return
	 */
	public List<Object> getMajorList(Map<String, Object> param) {
		return selectList("mart.modules.sbjt.sbjtOraMapper.getMajorList", param);
	}
	
	/**
     * 개설과목 목록
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getCoursesOffered(Map<String, Object> param){
		return (List<Object>)selectList("mart.modules.sbjt.sbjtOraMapper.selectCoursesOffered", param);
    }
	
	/**
     * 개설강의 목록(메인페이지 장바구니용)
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getClassList(Map<String, Object> param){
		return (List<Object>)selectList("mart.modules.sbjt.sbjtOraMapper.selectClassList", param);
    }
	
	/**
     * 최근개설강의 목록
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getLectureList(Map<String, Object> param){
		return (List<Object>)selectList("mart.modules.sbjt.sbjtOraMapper.selectLectureList", param);
    }
	
	/**
     * 최근개설강의 총 수
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getLectureCount(Map<String, Object> param) {
    	return (Integer)selectOne("mart.modules.sbjt.sbjtOraMapper.selectLectureCount", param);
    }
    
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param subjectCd 검색조건
     * @return 상세정보
     */
	public DataMap getLectureView(Map<String, Object> param) {
         return (DataMap)selectOne("mart.modules.sbjt.sbjtOraMapper.selectLectureView", param);
	}
	
	/**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param subjectCd 검색조건
     * @return 상세정보
     */
	public DataMap getCoreAbi(Map<String, String> param) {
         return (DataMap)selectOne("mart.modules.sbjt.sbjtOraMapper.getCoreAbi", param);
	}
	
	/**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param subjectCd 검색조건
     * @return 상세정보
     */
	public List<Object> getLectureCore(Map<String, Object> param) {
         return (List<Object>)selectList("mart.modules.sbjt.sbjtOraMapper.selectLectureCore", param);
	}
	
	/**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param subjectCd 검색조건
     * @return 상세정보
     */
	public List<Object> getLectureAbi(Map<String, Object> param) {
         return (List<Object>)selectList("mart.modules.sbjt.sbjtOraMapper.selectLectureAbi", param);
	}
	
	/**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param subjectCd 검색조건
     * @return 상세정보
     */
	public DataMap getPlanView(Map<String, Object> param) {
         return (DataMap)selectOne("mart.modules.sbjt.sbjtOraMapper.selectPlanView", param);
	}
	
	/**
     * 개설강좌 - 강의계획서(핵심역량)
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getCoreList(Map<String, Object> param){
		return (List<Object>)selectList("mart.modules.sbjt.sbjtOraMapper.selectCoreList", param);
    }
	
	/**
     * 개설강좌 - 강의계획서(전공능력)
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getAbiList(Map<String, Object> param){
		return (List<Object>)selectList("mart.modules.sbjt.sbjtOraMapper.selectAbiList", param);
    }
	
	/**
     * 개설강좌 - 강의계획서(교재 목록 수)
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getBookCount(Map<String, Object> param) {
    	return (Integer)selectOne("mart.modules.sbjt.sbjtOraMapper.selectBookCount", param);
    }
    
    /**
     * 개설강좌 - 강의계획서(교재 목록)
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getBookList(Map<String, Object> param){
		return (List<Object>)selectList("mart.modules.sbjt.sbjtOraMapper.selectBookList", param);
    }
	
	/**
     * 개설강좌 - 강의계획서(산학연연계)
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getLinkList(Map<String, Object> param){
		return (List<Object>)selectList("mart.modules.sbjt.sbjtOraMapper.selectLinkList", param);
    }
	
	/**
     * 개설강좌 - 강의계획서(산학연연계 수)
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getLinkCount(Map<String, Object> param) {
    	return (Integer)selectOne("mart.modules.sbjt.sbjtOraMapper.selectLinkCount", param);
    }
	
	/**
     * 개설강좌 - 강의계획서(주차별 수업계획)
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getWeekList(Map<String, Object> param){
		return (List<Object>)selectList("mart.modules.sbjt.sbjtOraMapper.selectWeekList", param);
    }
	
	/**
     * 강의평가 목록
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getEvalList(Map<String, Object> param){
		return (List<Object>)selectList("mart.modules.sbjt.sbjtOraMapper.selectEvalList", param);
    }
	
	/**
     * 강의평가 총 수
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getEvalCount(Map<String, Object> param) {
    	return (Integer)selectOne("mart.modules.sbjt.sbjtOraMapper.selectEvalCount", param);
    }
    
    /**
     * 수강생 통계
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getTotalStudent(Map<String, Object> param){
		return (List<Object>)selectList("mart.modules.sbjt.sbjtOraMapper.selectTotalStudent", param);
    }
	
	
	/**
	 * 교과목 상세-직업/직무
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getJobCd(String docId) {
		return selectList("rbs.modules.sbjt.sbjtOraMapper.getJobCd", docId);
	}
	
	/**
	 * 나노디그리 & 매트릭스 상단 정보
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getTrackList(Map<String, Object> param) {
		return selectList("rbs.modules.sbjt.sbjtOraMapper.getTrackList", param);
	}
	
	/**
	 * 개설 강좌-시간표 정보
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getLessonTime(List<Object> arr) {
		return selectList("rbs.modules.sbjt.sbjtOraMapper.getLessonTime", arr);
	}
	
	/**
	 * 개설 강좌-강의실 정보
	 * @param param
	 * @return
	 */
	public List<Object> getLessonRoom(List<Map<String, Object>> param) {
		return selectList("rbs.modules.sbjt.sbjtOraMapper.getLessonRoom", param);
	}
	
	/**
	 * 개설 강좌-강의계획서 정보
	 * @param param
	 * @return
	 */
	public Map<String, Object> getPlanInfo(String docId) {
		return selectOne("rbs.modules.sbjt.sbjtOraMapper.getPlanInfo", docId);
	}
	
	/**
	 * 개설 강좌-강의계획서 정보-수업 진행 방법 
	 * @param param
	 * @return
	 */
	public Map<String, Object> getPlanProg(String docId) {
		return selectOne("rbs.modules.sbjt.sbjtOraMapper.getPlanProg", docId);
	}
	
	/**
	 * 개설강좌-강의계획서 정보-기자재활용 
	 * @param param
	 * @return
	 */
	public Map<String, Object> getPlanAprt(String docId) {
		return selectOne("rbs.modules.sbjt.sbjtOraMapper.getPlanAprt", docId);
	}
	
	/**
	 * 개설 강좌-강의계획서 정보-교제 정보 
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getPlanBook(String docId) {
		return selectList("rbs.modules.sbjt.sbjtOraMapper.getPlanBook", docId);
	}
	
	/**
	 * 개설 강좌-강의계획서 정보-수업 계획 
	 * @param param
	 * @return
	 */
	public List<Object> getPlanWeek(String docId) {
		return selectList("rbs.modules.sbjt.sbjtOraMapper.getPlanWeek", docId);
	}
	
	/**
	 * 개설 강좌-강의계획서 정보-과제 
	 * @param param
	 * @return
	 */
	public List<Object> getPlanPrjt(String docId) {
		return selectList("rbs.modules.sbjt.sbjtOraMapper.getPlanPrjt", docId);
	}
	
	/**
	 * 개설 강좌-강의계획서 정보
	 * @param param
	 * @return
	 */
	public Map<String, Object> getLessonPlan(String docId) {
		return selectOne("rbs.modules.sbjt.sbjtOraMapper.getLessonPlan", docId);
	}
	
	/**
	 * 수강생 통계
	 * @param param
	 * @return
	 */
	public List< Map<String, Object>> getStatList(List<Map<String, Object>> param) {
		return selectList("rbs.modules.sbjt.sbjtOraMapper.getStatList", param);
	}
}