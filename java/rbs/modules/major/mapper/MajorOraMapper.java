package rbs.modules.major.mapper;

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
@Repository("majorOraMapper")
public class MajorOraMapper extends MartAbstractMapper {
	
	/**
	 * 추가 전공 목록 조회
	 * @return
	 */
	public List<Map<String, Object>> getNewMajorList() {
		return selectList("mart.modules.major.majorOraMapper.getNewMajorList");
	}
	
	/**
     * 전공 상세-기초 정보
     * @param subjectCd 검색조건
     * @return 상세정보
     */
	public DataMap getView(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.major.majorMapper.selectView", param);
	}
	
	/**
	 * 교과목 상세-직업/직무
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getJobCd(String docId) {
		return selectList("rbs.modules.major.majorOraMapper.getJobCd", docId);
	}
	
	/**
	 * 나노디그리 & 매트릭스 상단 정보
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getTrackList(Map<String, Object> param) {
		return selectList("rbs.modules.major.majorOraMapper.getTrackList", param);
	}
	
	/**
	 * 개설 강좌-시간표 정보
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getLessonTime(List<Object> arr) {
		return selectList("rbs.modules.major.majorOraMapper.getLessonTime", arr);
	}
	
	/**
	 * 개설 강좌-강의실 정보
	 * @param param
	 * @return
	 */
	public List<Object> getLessonRoom(List<Map<String, Object>> param) {
		return selectList("rbs.modules.major.majorOraMapper.getLessonRoom", param);
	}
	
	/**
	 * 개설 강좌-강의계획서 정보
	 * @param param
	 * @return
	 */
	public Map<String, Object> getPlanInfo(String docId) {
		return selectOne("rbs.modules.major.majorOraMapper.getPlanInfo", docId);
	}
	
	/**
	 * 개설 강좌-강의계획서 정보-수업 진행 방법 
	 * @param param
	 * @return
	 */
	public Map<String, Object> getPlanProg(String docId) {
		return selectOne("rbs.modules.major.majorOraMapper.getPlanProg", docId);
	}
	
	/**
	 * 개설강좌-강의계획서 정보-기자재활용 
	 * @param param
	 * @return
	 */
	public Map<String, Object> getPlanAprt(String docId) {
		return selectOne("rbs.modules.major.majorOraMapper.getPlanAprt", docId);
	}
	
	/**
	 * 개설 강좌-강의계획서 정보-교제 정보 
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getPlanBook(String docId) {
		return selectList("rbs.modules.major.majorOraMapper.getPlanBook", docId);
	}
	
	/**
	 * 개설 강좌-강의계획서 정보-수업 계획 
	 * @param param
	 * @return
	 */
	public List<Object> getPlanWeek(String docId) {
		return selectList("rbs.modules.major.majorOraMapper.getPlanWeek", docId);
	}
	
	/**
	 * 개설 강좌-강의계획서 정보-과제 
	 * @param param
	 * @return
	 */
	public List<Object> getPlanPrjt(String docId) {
		return selectList("rbs.modules.major.majorOraMapper.getPlanPrjt", docId);
	}
	
	/**
	 * 개설 강좌-강의계획서 정보
	 * @param param
	 * @return
	 */
	public Map<String, Object> getLessonPlan(String docId) {
		return selectOne("rbs.modules.major.majorOraMapper.getLessonPlan", docId);
	}
	
	/**
	 * 수강생 통계
	 * @param param
	 * @return
	 */
	public List< Map<String, Object>> getStatList(List<Map<String, Object>> param) {
		return selectList("rbs.modules.major.majorOraMapper.getStatList", param);
	}
}