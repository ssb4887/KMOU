package rbs.modules.studPlan.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;




import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.AcademicAbstractMapper;
import rbs.egovframework.mapper.RbsAbstractMapper;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.util.StringUtil;


//rbs.modules.studPlan.studPlanAdmMapper
@Repository("studPlanAdmMapper")
public class StudPlanAdmMapper extends RbsAbstractMapper{


	/**
	 * 승인
	 * @param param 
	 * @return
	 */
	public int judgApprove(Map<String, Object> param) {
		return update("rbs.modules.studPlan.studPlanAdmMapper.judgApprove", param);
	}
	
	/**
	 * 승인
	 * @param param 
	 * @return
	 */
	public int judgReject(Map<String, Object> param) {
		return update("rbs.modules.studPlan.studPlanAdmMapper.judgReject", param);
	}
	
	/**
	 * 창의융합교육센터에서 심의할 학생설계전공 조회
	 * @param param 
	 * @return
	 */
	public List<Object> getEduCenterJudgList(Map<String, Object> param) {
		return selectList("rbs.modules.studPlan.studPlanAdmMapper.getEduCenterJudgList", param);
	}
	/**
	 * 창의융합교육센터에서 심의할 학생설계전공 count
	 * @param param 
	 * @return
	 */
	public int getEduCenterJudgListCount(Map<String, Object> param) {
		return (Integer)selectOne("rbs.modules.studPlan.studPlanAdmMapper.getEduCenterJudgListCount", param);
	}
	
	
	/**
	 * 학사과에서 심의할 학생설계전공 조회
	 * @param param 
	 * @return
	 */
	public List<Object> getDepartJudgList(Map<String, Object> param) {
		return selectList("rbs.modules.studPlan.studPlanAdmMapper.getDepartJudgList", param);
	}	
	/**
	 * 학사과에서 심의할 학생설계전공 count
	 * @param param 
	 * @return
	 */
	public int getDepartJudgListCount(Map<String, Object> param) {
		return (Integer)selectOne("rbs.modules.studPlan.studPlanAdmMapper.getDepartJudgListCount", param);
	}

	/**
	 * 교무회에서 심의할 학생설계전공 조회
	 * @param param 
	 * @return
	 */
	public List<Object> getAffairCommitteeJudgList(Map<String, Object> param) {
		return selectList("rbs.modules.studPlan.studPlanAdmMapper.getAffairCommitteeJudgList", param);
	}
	/**
	 * 교무회에서 심의할 학생설계전공 count
	 * @param param 
	 * @return
	 */
	public int getAffairCommitteeJudgListCount(Map<String, Object> param) {
		return (Integer)selectOne("rbs.modules.studPlan.studPlanAdmMapper.getAffairCommitteeJudgListCount", param);
	}

	/**
	 * 최종 수정보완할 학생설계전공 조회
	 * @param param 
	 * @return
	 */
	public List<Object> getLastSupplementList(Map<String, Object> param) {
		return selectList("rbs.modules.studPlan.studPlanAdmMapper.getLastSupplementList", param);
	}
	
	/**
	 * 최종 수정보완할 학생설계전공 count
	 * @param param 
	 * @return
	 */
	public int getLastSupplementListCount(Map<String, Object> param) {
		return (Integer)selectOne("rbs.modules.studPlan.studPlanAdmMapper.getLastSupplementListCount", param);
	}

	/**
	 * 수정보완 저장
	 * @param param 
	 * @return
	 */
	public int lastSupplementSave(Map<String, Object> param) {
		return update("rbs.modules.studPlan.studPlanAdmMapper.lastSupplementSave", param);
	}

	/**
	 * 수정보완 마감(최종승인)
	 * @param param 
	 * @return
	 */
	public int lastSupplementComplete(Map<String, Object> param) {
		return update("rbs.modules.studPlan.studPlanAdmMapper.lastSupplementComplete", param);
	}

	public Map<String, Object> getStudAplyInfmt(Map<String, Object> param) {
		return selectOne("rbs.modules.studPlan.studPlanAdmMapper.getStudAplyInfmt", param);
	}

	public List<Map<String, Object>> getStudSbjtInfmt(Map<String, Object> param) {
		return selectList("rbs.modules.studPlan.studPlanAdmMapper.getStudSbjtInfmt", param);
	}
	
    


}