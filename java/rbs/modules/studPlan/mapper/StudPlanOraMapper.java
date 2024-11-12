package rbs.modules.studPlan.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.AcademicAbstractMapper;


@Repository("studPlanOraMapper")
public class StudPlanOraMapper extends AcademicAbstractMapper{
    
	
	
	public List<Object> selectCour121Info(Map<String, Object> param){
		
		return (List<Object>)selectList("rbs.modules.studPlan.studPlanOraMapper.selectCour121Info", param);
	}

	public Map<String, Object> getSdm(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return selectOne("rbs.modules.studPlan.studPlanOraMapper.getSdm", param);
	}

	public List<Map<String, Object>> getSdmSbjtList(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return selectList("rbs.modules.studPlan.studPlanOraMapper.getSdmSbjtList", param);
	}

	public List<Map<String, Object>> getSdmSbjtTotal(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return selectList("rbs.modules.studPlan.studPlanOraMapper.getSdmSbjtTotal", param);
	}
	
	public List<Map<String, Object>> getSdmSbjtChgTotal(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return selectList("rbs.modules.studPlan.studPlanOraMapper.getSdmSbjtChgTotal", param);
	}

	public List<Map<String, Object>> getSdmList(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return selectList("rbs.modules.studPlan.studPlanOraMapper.getSdmList", param);
	}
	
	public int getSdmListCnt(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return (Integer)selectOne("rbs.modules.studPlan.studPlanOraMapper.getSdmListCnt", param);
	}

	// 학생 검색 목록
	public List<Object> stuList(Map<String, Object> param) {
		return selectList("rbs.modules.studPlan.studPlanOraMapper.stuList", param);
	}

	// 교수 검색 목록
	public List<Object> proList(Map<String, Object> param) {
		return selectList("rbs.modules.studPlan.studPlanOraMapper.proList", param);
	}

	// 학생/교수 검색 모달 학과 목록
	public List<Object> deptList(Map<String, Object> param) {
		return selectList("rbs.modules.studPlan.studPlanOraMapper.deptList", param);
	}

	public List<Map<String, Object>> getGrpSdmYear() {
		// TODO Auto-generated method stub
		return selectList("rbs.modules.studPlan.studPlanOraMapper.getGrpSdmYear");
	}

	// 학생 설계 전공 수정 기본정보
	public Map<String, Object> sdmList(Map<String, Object> param) {
		return selectOne("rbs.modules.studPlan.studPlanOraMapper.sdmList", param);
	}

	public List<Map<String, Object>> msgProfList(String[] param) {
		// TODO Auto-generated method stub
		return selectList("rbs.modules.studPlan.studPlanOraMapper.msgProfList", param);
	}

	public int updateProfInfo(Map<String, Object> param) {
		return super.update("rbs.modules.studPlan.studPlanOraMapper.updateProfInfo",param);
	}
	


}