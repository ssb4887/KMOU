package rbs.modules.studPlan.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.MartAbstractMapper;


@Repository("studPlanMartMapper")
public class StudPlanMartMapper extends MartAbstractMapper{
    
	
	
	/**
	 * 주관 대학 목록
	 * @param param 
	 * @param param
	 * @return
	 */
	public List<Object> getCollegeList(Map<String, Object> param) {
		return selectList("mart.modules.studPlan.studPlanOraMapper.getCollegeList", param);
	}
	
	/**
	 * 주관 대학 - 학부(과) 목록
	 * @param param
	 * @return
	 */
	public List<Object> getDepartList(Map<String, Object> param) {
		return selectList("mart.modules.studPlan.studPlanOraMapper.getDepartList", param);
	}
	
	/**
	 * 주관 대학 - 학부(과) - 전공 목록
	 * @param param
	 * @return
	 */
	public List<Object> getMajorList(Map<String, Object> param) {
		return selectList("mart.modules.studPlan.studPlanOraMapper.getMajorList", param);
	}
	
	public List<Object> getProfDepartList() {
		return selectList("mart.modules.studPlan.studPlanOraMapper.getProfDepartList");
	}

	/**
	 * 교육과정등록하기 - [전공교과목] 목록
	 * @param param
	 * @return
	 */
	public List<Object> getSbjtList(Map<String, Object> param) {
		return selectList("mart.modules.studPlan.studPlanOraMapper.getSbjtList", param);
	}

	/**
	 * [지도교수 검색] - 교수 목록
	 * @param param
	 * @return
	 */
	public List<Object> getProfList(Map<String, Object> param) {
		return selectList("mart.modules.studPlan.studPlanOraMapper.getProfList", param);
	}

	public List<Map<String, Object>> getConfirmedStudPlan(List<Map<String, Object>> notConfirmedList) {
		return selectList("mart.modules.studPlan.studPlanOraMapper.getConfirmedStudPlan", notConfirmedList);
	}

	public int sendStudPlanToAHS010TB(Map<String, Object> studAplyInfmt) {
		return insert("mart.modules.studPlan.studPlanOraMapper.sendStudPlanToAHS010TB", studAplyInfmt);
	}

	public int sendStudPlanToAHS020TB(Map<String, Object> sbjt) {
		return insert("mart.modules.studPlan.studPlanOraMapper.sendStudPlanToAHS020TB", sbjt);
	}





}