package rbs.modules.studPlan.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import net.sf.json.JSONObject;
import rbs.egovframework.LoginVO;
import rbs.modules.studPlan.mapper.StudPlanAdmMapper;
import rbs.modules.studPlan.mapper.StudPlanMartMapper;
import rbs.modules.studPlan.mapper.StudPlanOraMapper;
import rbs.modules.studPlan.service.StudPlanAdmService;

/**
 * 학생설계전공 serviceImple
 * @author 이동근
 */
@Service("studPlanAdmService")
@Transactional
public class StudPlanAdmServiceImpl extends EgovAbstractServiceImpl implements StudPlanAdmService{
	
	@Resource(name="studPlanAdmMapper")
	private StudPlanAdmMapper studPlanAdmDAO;
	
	@Resource(name="studPlanOraMapper")
	private StudPlanOraMapper studPlanOraDAO;
	
	@Resource(name="studPlanMartMapper")
	private StudPlanMartMapper studPlanMartDAO;

	@Override
	public List<Object> getEduCenterJudgList(Map<String, Object> param) {
		
		return studPlanAdmDAO.getEduCenterJudgList(param);
	}

	@Override
	public int getEduCenterJudgListCount(Map<String, Object> param) {
		return studPlanAdmDAO.getEduCenterJudgListCount(param);
	}

	@Override
	public int judgApprove(JSONObject rawJsonObj, HttpServletRequest request) throws Exception {
		Map<String, Object> param = new HashMap<>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		String departFg = rawJsonObj.getString("DEPART_FG");
		String status = "";
		switch(departFg) {
			case "EDU_CENTER" : status = "50"; break;
			case "DEPART" : status = "60"; break;
			case "AFFAIR_COMMITTEE" : status = "70"; break;
		}
        
		param.put("DEPART_FG", departFg);
        param.put("JUDG_OPIN", rawJsonObj.getString("JUDG_OPIN"));
		param.put("SDM_CD", rawJsonObj.getString("SDM_CD"));
		param.put("REVSN_NO", rawJsonObj.getString("REVSN_NO"));
		param.put("STATUS", status);
		param.put("LAST_MODI_ID", loginVO.getMemberId());
		param.put("LAST_MODI_IP", request.getRemoteAddr());
		
		return studPlanAdmDAO.judgApprove(param);
		
	}

	@Override
	public int judgReject(JSONObject rawJsonObj, HttpServletRequest request) throws Exception {
		Map<String, Object> param = new HashMap<>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		String departFg = rawJsonObj.getString("DEPART_FG");
		String status = "";
		switch(departFg) {
			case "EDU_CENTER" : status = "43"; break;
			case "DEPART" : status = "53"; break;
			case "AFFAIR_COMMITTEE" : status = "63"; break;
		}
        
		param.put("DEPART_FG", departFg);
        param.put("JUDG_OPIN", rawJsonObj.getString("JUDG_OPIN"));
		param.put("SDM_CD", rawJsonObj.getString("SDM_CD"));
		param.put("REVSN_NO", rawJsonObj.getString("REVSN_NO"));
		param.put("STATUS", status);
		param.put("LAST_MODI_ID", loginVO.getMemberId());
		param.put("LAST_MODI_IP", request.getRemoteAddr());
		
		return studPlanAdmDAO.judgReject(param);		
	}

	@Override
	public int getDepartJudgListCount(Map<String, Object> param) {
		return studPlanAdmDAO.getDepartJudgListCount(param);
	}

	@Override
	public List<Object> getDepartJudgList(Map<String, Object> param) {
		return studPlanAdmDAO.getDepartJudgList(param);
	}

	@Override
	public int getAffairCommitteeJudgListCount(Map<String, Object> param) {
		return studPlanAdmDAO.getAffairCommitteeJudgListCount(param);
	}

	@Override
	public List<Object> getAffairCommitteeJudgList(Map<String, Object> param) {
		return studPlanAdmDAO.getAffairCommitteeJudgList(param);
	}

	@Override
	public int getLastSupplementListCount(Map<String, Object> param) {
		return studPlanAdmDAO.getLastSupplementListCount(param);
	}

	@Override
	public List<Object> getLastSupplementList(Map<String, Object> param) {
		return studPlanAdmDAO.getLastSupplementList(param);
	}

	@Override
	public int lastSupplementSave(JSONObject rawJsonObj, HttpServletRequest request) {
	    Map<String, Object> param = new HashMap<>();
	    LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
	    
	    
	    String[] keys = {
	        "SDM_CD", "REVSN_NO", "SDM_KOR_NM", "SDM_ENG_NM", "AWD_DEGR_KOR_NM",
	        "AWD_DEGR_ENG_NM", "IDEAL_STU_KOR", "CONC_JOB_KOR", "CONC_STUD_FLD_KOR", "DMND_BASE_LRN_ABTY_KOR",
	        "CPTN_PLAN_KOR", "ETC_CTNT", "APLY_RESN_KOR"
	    };
        	    	    
	    param.put("LAST_MODI_IP", request.getRemoteAddr());
	    param.put("LAST_MODI_ID", loginVO.getMemberId());
	    
	    for (String key : keys) {
	        String value = StringUtil.getString(rawJsonObj.get(key));
	        if (value == null || value.isEmpty()) {
	            param.put(key, null);
	        } else {
	            param.put(key, value.replace("'", "´"));
	        }
	    }
	    
	    
	    return studPlanAdmDAO.lastSupplementSave(param);
	}

	@Override
	public int lastSupplementComplete(JSONObject rawJsonObj, HttpServletRequest request) throws Exception {
	    Map<String, Object> param = new HashMap<>();
	    LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
	    
	    param.put("SDM_CD", rawJsonObj.getString("SDM_CD"));
	    param.put("REVSN_NO", rawJsonObj.getString("REVSN_NO"));
	    param.put("STATUS", "100");
	    param.put("LAST_MODI_IP", request.getRemoteAddr());
	    param.put("LAST_MODI_ID", loginVO.getMemberId());
	    
	    return studPlanAdmDAO.lastSupplementComplete(param);
	}

	@Override
	public int sendStudPlanToAHS010TB(JSONObject rawJsonObj) throws Exception {
	    Map<String, Object> param = new HashMap<>();
	    
	    
	    param.put("SDM_CD", rawJsonObj.getString("SDM_CD"));
	    param.put("REVSN_NO", rawJsonObj.getString("REVSN_NO"));
	    
	    Map<String, Object> studAplyInfmt = studPlanAdmDAO.getStudAplyInfmt(param);
	    List<Map<String, Object>> studSbjtInfmt = studPlanAdmDAO.getStudSbjtInfmt(param);
	    
	    int result = 0;
	    
	    //신청정보(마스터) insert
	    result += studPlanMartDAO.sendStudPlanToAHS010TB(studAplyInfmt);
	    System.out.println(result);
	    if(result > 0) {
	        for (Map<String, Object> sbjt : studSbjtInfmt) {
	        	System.out.println(sbjt);
		    	//교과목 insert
	        	result += studPlanMartDAO.sendStudPlanToAHS020TB(sbjt);
	        }	 	    	
	    }
	    
	    
		return result;
	}
	








}