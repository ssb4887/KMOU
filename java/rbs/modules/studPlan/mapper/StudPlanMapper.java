package rbs.modules.studPlan.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;




import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.AcademicAbstractMapper;
import rbs.egovframework.mapper.RbsAbstractMapper;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.util.StringUtil;


@Repository("studPlanMapper")
public class StudPlanMapper extends RbsAbstractMapper{
    
	/**
	 * 다음 학생설계전공 가져오기
	 * @param aplyStudentNo
	 * @return
	 */
	public String getNextSdmCd(String aplyStudentNo) {
		return (String)selectOne("rbs.modules.studPlan.studPlanMapper.getNextSdmCd", aplyStudentNo);
	}

	/**
	 * 기본정보 저장
	 * @param param
	 * @return
	 */
	public int insertStudInfmt(Map<String, Object> param) {
		return insert("rbs.modules.studPlan.studPlanMapper.insertStudInfmt", param);
	}

	/**
	 * 교과목 delete(insert 전에 선행)
	 * @param param
	 * @return
	 */
	public int deleteStudSbjt(Map<String, Object> param) {
		return delete("rbs.modules.studPlan.studPlanMapper.deleteStudSbjt",param);		
	}

	/**
	 * 교과목 insert
	 * @param param
	 * @return
	 */
	public int insertStudSbjt(Map<String, Object> param) {
		return insert("rbs.modules.studPlan.studPlanMapper.insertStudSbjt", param);	
	}

	/**
	 * 상담교수 delete(insert 전에 선행)
	 * @param param
	 * @return
	 */
	public int deleteCnsltProf(Map<String, Object> param) {
		return delete("rbs.modules.studPlan.studPlanMapper.deleteCnsltProf",param);
		
	}
	
	/**
	 * 상담교수 insert
	 * @param param
	 * @return
	 */
	public int insertCnsltProf(Map<String, Object> param) {
		return insert("rbs.modules.studPlan.studPlanMapper.insertCnsltProf", param);		
	}

	/**
	 * 내 학생설계전공 리스트
	 * @param studentNo
	 * @return
	 */
	public List<Object> getMyList(String studentNo) {
		return selectList("rbs.modules.studPlan.studPlanMapper.getMyList", studentNo);
	}
	/**
	 * 내 학생설계전공 교과목별 전공 리스트
	 * @param studentNo
	 * @return
	 */
	public List<Object> getMySbjtList(String studentNo) {
		return selectList("rbs.modules.studPlan.studPlanMapper.getMySbjtList", studentNo);
	}

	/**
	 * 승인된 전체 학생설계전공 리스트
	 * @param param 
	 * @return
	 */
	public List<Object> getStudPlanList(Map<String, Object> param) {
		return selectList("rbs.modules.studPlan.studPlanMapper.getStudPlanList", param);
	}
	public int getStudPlanListCount(Map<String, Object> param) {
		return (Integer)selectOne("rbs.modules.studPlan.studPlanMapper.getStudPlanListCount", param);
	}
	/**
	 * 승인된 전체 학생설계전공 교과목별 전공 리스트
	 * @param param 
	 * @return
	 */
	public List<Object> getStudPlanSbjtList(Map<String, Object> param) {
		return selectList("rbs.modules.studPlan.studPlanMapper.getStudPlanSbjtList", param);
	}
	/**
	 * 승인된 전체 학생설계전공 전공목록
	 * @param param 
	 * @return
	 */
	public List<Object> getStudPlanMajorList() {
		return selectList("rbs.modules.studPlan.studPlanMapper.getStudPlanMajorList");
	}

	/**
	 * 학생설계전공 기본정보 select
	 * @return
	 */
	public DataMap getStudInfmt(Map<String, Object> param) {
		return (DataMap)selectOne("rbs.modules.studPlan.studPlanMapper.getStudInfmt", param);
	}

	/**
	 * 학생설계전공 교과목 select
	 * @param param
	 * @return
	 */
	public List<Object> getStudSbjt(Map<String, Object> param) {
		return selectList("rbs.modules.studPlan.studPlanMapper.getStudSbjt", param);
	}

	/**
	 * 학생설계전공 컨설팅 select
	 * @param param
	 * @return
	 */
	public List<Object> getCnslt(Map<String, Object> param) {
		return selectList("rbs.modules.studPlan.studPlanMapper.getCnslt", param);
	}

	/**
	 * 학생설계전공 알림정보 insert
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int insertNotiMsg(List<Map<String, Object>> param) {
		return insert("rbs.modules.studPlan.studPlanMapper.insertNotiMsg", param);	
	}
	
	/**
	 * 기본신청정보 update
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int updateStudInfmt(Map<String, Object> param) {
		return update("rbs.modules.studPlan.studPlanMapper.updateStudInfmt", param);
	}
	
	/**
	 * 학생설계전공 상태 update(상태값만 바꿈)
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int updateStatus(Map<String, Object> param) {
		return update("rbs.modules.studPlan.studPlanMapper.updateStatus", param);
	}

	/**
	 * 학생설계전공 변경(프로시저 call)
	 * @param param
	 * @throws Exception
	 */
	public void changeStud(Map<String, Object> param) {
		update("rbs.modules.studPlan.studPlanMapper.changeStud", param);
		
	}

	/**
	 * 학생설계전공 변경교과목 select
	 * @param param
	 * @return
	 */
	public List<Object> getStudChgSbjt(Map<String, Object> param) {
		return selectList("rbs.modules.studPlan.studPlanMapper.getStudChgSbjt", param);
	}

	/**
	 * 학생설계전공 변경교과목테이블 delete
	 * @param param
	 * @return
	 */
	public int deleteChgStudSbjt(Map<String, Object> param) {
		return delete("rbs.modules.studPlan.studPlanMapper.deleteChgStudSbjt",param);
	}

	/**
	 * 학생설계전공 변경교과목테이블 insert
	 * @param param
	 * @return
	 */
	public int insertChgStudSbjt(Map<String, Object> param) {
		return insert("rbs.modules.studPlan.studPlanMapper.insertChgStudSbjt", param);
	}

	/**
	 * 학생설계전공 기본정보 교수정보 update
	 * @param param
	 * @return
	 */
	public int updateChgStudInfmt(Map<String, Object> param) {
		return update("rbs.modules.studPlan.studPlanMapper.updateChgStudInfmt", param);
	}

	/**
	 * 알림 가져오기
	 * @param memberNo
	 * @return
	 * @throws Exception
	 */
	public List<Object> getNotiMsg(String memberNo) {
		return selectList("rbs.modules.studPlan.studPlanMapper.getNotiMsg", memberNo);
	}

	/**
	 * 알림 읽음처리
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int notiMsgCnf(Map<String, Object> param) {
		return update("rbs.modules.studPlan.studPlanMapper.notiMsgCnf", param);
	}
	
	/**
	 * 알림 삭제처리
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int notiMsgDel(Map<String,Object> param) {
		return update("rbs.modules.studPlan.studPlanMapper.notiMsgDel", param);
	}

	/**
	 * 학생설계전공 전체 탭 카운트
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<Object> getAllTabCnt(Map<String, Object> param) {
		return (List<Object>)selectList("rbs.modules.studPlan.studPlanMapper.getAllTabCnt", param);
	}

	
	/**
	 * 학생설계전공 전체 탭 리스트
	 * @param param
	 * @return
	 * @throws Exception
	 */
	//승인요청
	public List<Object> getRAList(Map<String, Object> param) {
		return (List<Object>)selectList("rbs.modules.studPlan.studPlanMapper.getRAList", param);
	}
	//상담요청
	public List<Object> getRCList(Map<String, Object> param) {
		return (List<Object>)selectList("rbs.modules.studPlan.studPlanMapper.getRCList", param);
	}
	//참여가능상담
	public List<Object> getACList(Map<String, Object> param) {
		return (List<Object>)selectList("rbs.modules.studPlan.studPlanMapper.getACList", param);
	}
	//상담진행
	public List<Object> getWCList(Map<String, Object> param) {
		return (List<Object>)selectList("rbs.modules.studPlan.studPlanMapper.getWCList", param);
	}
	//상담완료
	public List<Object> getCCList(Map<String, Object> param) {
		return (List<Object>)selectList("rbs.modules.studPlan.studPlanMapper.getCCList", param);
	}
	//심사진행
	public List<Object> getWJList(Map<String, Object> param) {
		return (List<Object>)selectList("rbs.modules.studPlan.studPlanMapper.getWJList", param);
	}
	//심사완료
	public List<Object> getCJList(Map<String, Object> param) {
		return (List<Object>)selectList("rbs.modules.studPlan.studPlanMapper.getCJList", param);
	}

	/**
	 * 학생설계전공 승인
	 * @param param 
	 * @return
	 * @throws Exception
	 */
	public int profApprove(Map<String, Object> param) {
		return update("rbs.modules.studPlan.studPlanMapper.profApprove", param);
	}

	/**
	 * 학생설계전공 반려
	 * @param param 
	 * @return
	 * @throws Exception
	 */
	public int profReject(Map<String, Object> param) {
		return update("rbs.modules.studPlan.studPlanMapper.profReject", param);
	}
	
	/**
	 * 학생설계전공 교수 개인 컨설팅정보 update
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int updateProfCnslt(Map<String, Object> param) {
		return update("rbs.modules.studPlan.studPlanMapper.updateProfCnslt", param);
	}

	/**
	 * 학생설계전공 교수 상담접수
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int insertProfCnslt(Map<String, Object> param) {
		return insert("rbs.modules.studPlan.studPlanMapper.insertProfCnslt", param);
	}
	
	/**
	 * 학생설계전공 교수 상담접수취소
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int deleteProfCnslt(Map<String, Object> param) {
		return delete("rbs.modules.studPlan.studPlanMapper.deleteProfCnslt",param);
	}

	
	/**
	 * 학생설계전공 유사도 보기
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public DataMap getSimilityMj(Map<String, Object> param) {
		return (DataMap)selectOne("rbs.modules.studPlan.studPlanMapper.getSimilityMj", param);
	}

	public int deleteBookmarkBySdmCd(String sdmCd) {
		return delete("rbs.modules.studPlan.studPlanMapper.deleteBookmarkBySdmCd",sdmCd);
		
	}

	public List<Map<String, Object>> getNotConfirmedStudPlan() {
		return selectList("rbs.modules.studPlan.studPlanMapper.getNotConfirmedStudPlan");
	}


	public int updateNotConfirmedStudPlan(Map<String, Object> param) {
		return update("rbs.modules.studPlan.studPlanMapper.updateNotConfirmedStudPlan", param);
	}

	public List<Object> getCollegeList(Map<String, Object> param) {
		return selectList("rbs.modules.studPlan.studPlanMapper.getCollegeList", param);
	}

	public List<Object> getDepartList(Map<String, Object> param) {
		return selectList("rbs.modules.studPlan.studPlanMapper.getDepartList", param);
	}

	public List<Object> getMajorList(Map<String, Object> param) {
		return selectList("rbs.modules.studPlan.studPlanMapper.getMajorList", param);
	}

}