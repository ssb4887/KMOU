package rbs.modules.studPlan.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rbs.egovframework.LoginVO;
import rbs.modules.studPlan.mapper.StudPlanMapper;
import rbs.modules.studPlan.mapper.StudPlanMartMapper;
import rbs.modules.studPlan.mapper.StudPlanOraMapper;
import rbs.modules.studPlan.service.StudPlanService;

/**
 * 학생설계전공 serviceImple
 * @author 이동근
 */
@Service("studPlanService")
@Transactional
public class StudPlanServiceImpl extends EgovAbstractServiceImpl implements StudPlanService{
	
	@Resource(name="studPlanMapper")
	private StudPlanMapper studPlanDAO;
	
	@Resource(name="studPlanOraMapper")
	private StudPlanOraMapper studPlanOraDAO;
	
	@Resource(name="studPlanMartMapper")
	private StudPlanMartMapper studPlanMartDAO;
	
	
	/**
	 * 주관대학 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getCollegeList(Map<String, Object> param) throws Exception {
		return studPlanDAO.getCollegeList(param);
	}
	
	/**
	 * 주관 대학 - 학부(과) 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDepartList(Map<String, Object> param) throws Exception {
		return studPlanDAO.getDepartList(param);
	}
	
	/**
	 * 주관 대학 - 학부(과) - 전공 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getMajorList(Map<String, Object> param) throws Exception {
		return studPlanDAO.getMajorList(param);
	}

	@Override
	public List<Object> getProfDepartList(Map<String, Object> param) {
		return studPlanMartDAO.getProfDepartList();
	}
	
	
	/**
	 * 교육과정등록하기 - [전공교과목] 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getSbjtList(Map<String, Object> param) throws Exception {
		return studPlanMartDAO.getSbjtList(param);
	}

	
	/**
	 * [지도교수 검색] - 교수 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getProfList(Map<String, Object> param) throws Exception {
		return studPlanMartDAO.getProfList(param);
	}

	/**
	 * 다음 학생설계전공 코드 가져오기
	 * @param param
	 * @return
	 */
	@Override
	public String getNextSdmCd(String aplyStudentNo) throws Exception {
		return studPlanDAO.getNextSdmCd(aplyStudentNo);
	}
	
	/**
	 * 교과목 delete
	 * @param sdmCd
	 * @param revsnCd
	 * @param rawJsonObj
	 * @param request
	 * @return
	 */
	@Override
	public int deleteStudSbjt(String sdmCd, int revsnNo, HttpServletRequest request) throws Exception {
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("SDM_CD", sdmCd);
		param.put("REVSN_NO", revsnNo);
		
		//교과목 삭제
		studPlanDAO.deleteStudSbjt(param);
		
		return 0;
	}
	
	/**
	 * 교과목 insert
	 * @param sdmCd
	 * @param revsnCd
	 * @param rawJsonObj
	 * @param request
	 * @return
	 */
	@Override
	public int insertStudSbjt(String sdmCd, int revsnNo, JSONObject rawJsonObj, HttpServletRequest request) throws Exception {
				
	    Map<String, Object> param = new HashMap<>();
	    List<Map<String, String>> subjectList = new ArrayList<>();

	    String[] keys = {
	        "ORG_CODE", "COLG_CD", "COLG_NM", "DEPT_CD", "DEPT_NM", "MAJOR_CD", "MAJOR_NM", "COMDIV_CODE", 
	        "COMDIV_CODE_NM", "ORG_COMDIV_CODE", "SUBJECT_CD", "SUBJECT_NM", "SUBJECT_ENM", 
	        "COM_GRADE", "COM_GRADE_NM", "YEAR", "SMT", "SMT_NM", "CDT_NUM", "WTIME_NUM", "PTIME_NUM"
	    };

	    if (rawJsonObj.get("MAJOR_CD") != null) {
	        int size = rawJsonObj.getJSONArray("MAJOR_CD").size();

	        for (int i = 0; i < size; i++) {
	            Map<String, String> subjectMap = new HashMap<>();
	            for (String key : keys) {
	                if (rawJsonObj.containsKey(key)) {
	                    String value = rawJsonObj.getJSONArray(key).optString(i, "");
	                    if (value.isEmpty()) {
	                        subjectMap.put(key, null); // 빈 문자열을 null로 치환
	                    } else {
	                        subjectMap.put(key, value.replace("'", "´"));
	                    }
	                }
	            }
	            subjectList.add(subjectMap);
	        }
	    }

	    if (!subjectList.isEmpty()) {
	        String lastModiId = StringUtil.getString(rawJsonObj.get("APLY_STUDENT_NO"));
	        String lastModiIp = request.getRemoteAddr();

	        for (int i = 0; i < subjectList.size(); i++) {
	            param.clear();
	            Map<String, String> subjectMap = subjectList.get(i);

	            param.put("SDM_CD", sdmCd);
	            param.put("REVSN_NO", revsnNo);
	            param.put("LAST_MODI_ID", lastModiId);
	            param.put("LAST_MODI_IP", lastModiIp);
	            param.put("SBJT_SEQ", i + 1);

	            for (String key : keys) {
	                param.put(key, subjectMap.get(key));
	            }

	            // 교과목 insert(개별행 -> 반복)
	            studPlanDAO.insertStudSbjt(param);
	        }
	    }

	    return 0;
	}

	/**
	 * 기본신청정보 insert
	 * @param sdmCd
	 * @param rawJsonObj
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public int insertStudInfmt(String sdmCd, int revsnNo, JSONObject rawJsonObj, HttpServletRequest request, String status) throws Exception {
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		String APLY_RESN_KOR = StringUtil.getString(rawJsonObj.get("APLY_RESN_KOR"));
		String APLY_STUDENT_NO = StringUtil.getString(rawJsonObj.get("APLY_STUDENT_NO"));
		String APLY_STUDENT_NM = StringUtil.getString(rawJsonObj.get("APLY_STUDENT_NM"));
		String CPTN_PLAN_KOR = StringUtil.getString(rawJsonObj.get("CPTN_PLAN_KOR"));
		String ETC_CTNT = StringUtil.getString(rawJsonObj.get("ETC_CTNT"));
		String CONC_STUD_FLD_KOR = StringUtil.getString(rawJsonObj.get("CONC_STUD_FLD_KOR"));
		String GUID_PROF_STAFF_NO = StringUtil.getString(rawJsonObj.get("GUID_PROF_STAFF_NO"));
		String GUID_PROF_STAFF_NM = StringUtil.getString(rawJsonObj.get("GUID_PROF_STAFF_NM"));
		String CNSLT_PROF = StringUtil.getString(rawJsonObj.get("CNSLT_PROF"));
		String CNSLT_PROF_NM = StringUtil.getString(rawJsonObj.get("CNSLT_PROF_NM"));
		String SDM_KOR_NM = StringUtil.getString(rawJsonObj.get("SDM_KOR_NM"));
		String SDM_ENG_NM = StringUtil.getString(rawJsonObj.get("SDM_ENG_NM"));
		String AWD_DEGR_ENG_NM = StringUtil.getString(rawJsonObj.get("AWD_DEGR_ENG_NM"));
		String AWD_DEGR_KOR_NM = StringUtil.getString(rawJsonObj.get("AWD_DEGR_KOR_NM"));
		String SBJT_DGN_RNG_FG = StringUtil.getString(rawJsonObj.get("SBJT_DGN_RNG_FG"));
		String SBJT_DGN_RNG_FG_NM = StringUtil.getString(rawJsonObj.get("SBJT_DGN_RNG_FG_NM"));
		String IDEAL_STU_KOR = StringUtil.getString(rawJsonObj.get("IDEAL_STU_KOR"));
		String DMND_BASE_LRN_ABTY_KOR = StringUtil.getString(rawJsonObj.get("DMND_BASE_LRN_ABTY_KOR"));
		String CONC_JOB_KOR = StringUtil.getString(rawJsonObj.get("CONC_JOB_KOR"));
		
		
		
		param.put("SDM_CD", sdmCd);
		param.put("REVSN_NO", revsnNo);
		param.put("STATUS", status);
		param.put("APLY_RESN_KOR", APLY_RESN_KOR);
		param.put("APLY_STUDENT_NO", APLY_STUDENT_NO);
		param.put("APLY_STUDENT_NM", APLY_STUDENT_NM);
		param.put("CPTN_PLAN_KOR", CPTN_PLAN_KOR);
		param.put("ETC_CTNT", ETC_CTNT);
		param.put("CONC_STUD_FLD_KOR", CONC_STUD_FLD_KOR);
		param.put("CNSLT_PROF", CNSLT_PROF);
		param.put("CNSLT_PROF_NM", CNSLT_PROF_NM);
		param.put("GUID_PROF_STAFF_NO", GUID_PROF_STAFF_NO);
		param.put("GUID_PROF_STAFF_NM", GUID_PROF_STAFF_NM);
		param.put("SDM_KOR_NM", SDM_KOR_NM);
		param.put("SDM_ENG_NM", SDM_ENG_NM);
		param.put("AWD_DEGR_ENG_NM", AWD_DEGR_ENG_NM);
		param.put("AWD_DEGR_KOR_NM", AWD_DEGR_KOR_NM);
		param.put("SBJT_DGN_RNG_FG", SBJT_DGN_RNG_FG);
		param.put("SBJT_DGN_RNG_FG_NM", SBJT_DGN_RNG_FG_NM);
		param.put("IDEAL_STU_KOR", IDEAL_STU_KOR);
		param.put("DMND_BASE_LRN_ABTY_KOR", DMND_BASE_LRN_ABTY_KOR);
		param.put("CONC_JOB_KOR", CONC_JOB_KOR);
		param.put("REGI_IP", request.getRemoteAddr());
		
		return studPlanDAO.insertStudInfmt(param);
		
		//변경교과목에서 리펙토링한 코드로 돌아가면 이걸로 교체
//	    Map<String, Object> param = new HashMap<>();
//	    String[] keys = {
//	        "APLY_RESN_KOR", "APLY_STUDENT_NO", "CPTN_PLAN_KOR", "ETC_CTNT", "CONC_STUD_FLD_KOR", 
//	        "GUID_PROF_STAFF_NO", "GUID_PROF_STAFF_NM", "CNSLT_PROF", "CNSLT_PROF_NM", "SDM_KOR_NM", 
//	        "SDM_ENG_NM", "AWD_DEGR_ENG_NM", "AWD_DEGR_KOR_NM", "SBJT_DGN_RNG_FG", "SBJT_DGN_RNG_FG_NM", 
//	        "IDEAL_STU_KOR", "DMND_BASE_LRN_ABTY_KOR", "CONC_JOB_KOR"
//	    };
//
//	    param.put("SDM_CD", sdmCd);
//	    param.put("REVSN_NO", revsnNo);
//	    param.put("STATUS", status);
//	    param.put("REGI_IP", request.getRemoteAddr());
//
//	    for (String key : keys) {
//	        String value = StringUtil.getString(rawJsonObj.get(key));
//	        if (value == null || value.isEmpty()) {
//	            param.put(key, null);
//	        } else {
//	            param.put(key, StringEscapeUtils.escapeSql(value));
//	        }
//	    }
//
//	    return studPlanDAO.insertStudInfmt(param);
	}
	
	/**
	 * 기본신청정보 update
	 * @param sdmCd
	 * @param revsnNo
	 * @param rawJsonObj
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateStudInfmt(String sdmCd, int revsnNo, JSONObject rawJsonObj, HttpServletRequest request, String status) throws Exception {
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		String APLY_RESN_KOR = StringUtil.getString(rawJsonObj.get("APLY_RESN_KOR"));
		String APLY_STUDENT_NO = StringUtil.getString(rawJsonObj.get("APLY_STUDENT_NO"));
		String CPTN_PLAN_KOR = StringUtil.getString(rawJsonObj.get("CPTN_PLAN_KOR"));
		String ETC_CTNT = StringUtil.getString(rawJsonObj.get("ETC_CTNT"));
		String CONC_STUD_FLD_KOR = StringUtil.getString(rawJsonObj.get("CONC_STUD_FLD_KOR"));
		String GUID_PROF_STAFF_NO = StringUtil.getString(rawJsonObj.get("GUID_PROF_STAFF_NO"));
		String GUID_PROF_STAFF_NM = StringUtil.getString(rawJsonObj.get("GUID_PROF_STAFF_NM"));
		String CNSLT_PROF = StringUtil.getString(rawJsonObj.get("CNSLT_PROF"));
		String CNSLT_PROF_NM = StringUtil.getString(rawJsonObj.get("CNSLT_PROF_NM"));
		String SDM_KOR_NM = StringUtil.getString(rawJsonObj.get("SDM_KOR_NM"));
		String SDM_ENG_NM = StringUtil.getString(rawJsonObj.get("SDM_ENG_NM"));
		String AWD_DEGR_ENG_NM = StringUtil.getString(rawJsonObj.get("AWD_DEGR_ENG_NM"));
		String AWD_DEGR_KOR_NM = StringUtil.getString(rawJsonObj.get("AWD_DEGR_KOR_NM"));
		String SBJT_DGN_RNG_FG = StringUtil.getString(rawJsonObj.get("SBJT_DGN_RNG_FG"));
		String SBJT_DGN_RNG_FG_NM = StringUtil.getString(rawJsonObj.get("SBJT_DGN_RNG_FG_NM"));
		String IDEAL_STU_KOR = StringUtil.getString(rawJsonObj.get("IDEAL_STU_KOR"));
		String DMND_BASE_LRN_ABTY_KOR = StringUtil.getString(rawJsonObj.get("DMND_BASE_LRN_ABTY_KOR"));
		String CONC_JOB_KOR = StringUtil.getString(rawJsonObj.get("CONC_JOB_KOR"));
		
		
		
		param.put("SDM_CD", sdmCd);
		param.put("REVSN_NO", revsnNo);
		param.put("STATUS", status);
		param.put("APLY_RESN_KOR", APLY_RESN_KOR);
		param.put("APLY_STUDENT_NO", APLY_STUDENT_NO);
		param.put("CPTN_PLAN_KOR", CPTN_PLAN_KOR);
		param.put("ETC_CTNT", ETC_CTNT);
		param.put("CONC_STUD_FLD_KOR", CONC_STUD_FLD_KOR);
		param.put("CNSLT_PROF", CNSLT_PROF);
		param.put("CNSLT_PROF_NM", CNSLT_PROF_NM);
		param.put("GUID_PROF_STAFF_NO", GUID_PROF_STAFF_NO);
		param.put("GUID_PROF_STAFF_NM", GUID_PROF_STAFF_NM);
		param.put("SDM_KOR_NM", SDM_KOR_NM);
		param.put("SDM_ENG_NM", SDM_ENG_NM);
		param.put("AWD_DEGR_ENG_NM", AWD_DEGR_ENG_NM);
		param.put("AWD_DEGR_KOR_NM", AWD_DEGR_KOR_NM);
		param.put("SBJT_DGN_RNG_FG", SBJT_DGN_RNG_FG);
		param.put("SBJT_DGN_RNG_FG_NM", SBJT_DGN_RNG_FG_NM);
		param.put("IDEAL_STU_KOR", IDEAL_STU_KOR);
		param.put("DMND_BASE_LRN_ABTY_KOR", DMND_BASE_LRN_ABTY_KOR);
		param.put("CONC_JOB_KOR", CONC_JOB_KOR);
		param.put("LAST_MODI_IP", request.getRemoteAddr());
		
		return studPlanDAO.updateStudInfmt(param);
		
		//변경교과목에서 리펙토링한 코드로 돌아가면 이걸로 교체
//	    Map<String, Object> param = new HashMap<>();
//	    String[] keys = {
//	        "APLY_RESN_KOR", "APLY_STUDENT_NO", "CPTN_PLAN_KOR", "ETC_CTNT", "CONC_STUD_FLD_KOR", 
//	        "GUID_PROF_STAFF_NO", "GUID_PROF_STAFF_NM", "CNSLT_PROF", "CNSLT_PROF_NM", "SDM_KOR_NM", 
//	        "SDM_ENG_NM", "AWD_DEGR_ENG_NM", "AWD_DEGR_KOR_NM", "SBJT_DGN_RNG_FG", "SBJT_DGN_RNG_FG_NM", 
//	        "IDEAL_STU_KOR", "DMND_BASE_LRN_ABTY_KOR", "CONC_JOB_KOR"
//	    };
//
//	    param.put("SDM_CD", sdmCd);
//	    param.put("REVSN_NO", revsnNo);
//	    param.put("STATUS", status);
//	    param.put("LAST_MODI_IP", request.getRemoteAddr());
//
//	    for (String key : keys) {
//	        String value = StringUtil.getString(rawJsonObj.get(key));
//	        if (value == null || value.isEmpty()) {
//	            param.put(key, null);
//	        } else {
//	            param.put(key, StringEscapeUtils.escapeSql(value));
//	        }
//	    }
//
//	    return studPlanDAO.updateStudInfmt(param);
	}
	

	/**
	 * 상담교수 delete
	 * @param sdmCd
	 * @param rawJsonObj
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public int deleteCnslt(String sdmCd, int revsnNo, JSONObject rawJsonObj, HttpServletRequest request) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("SDM_CD", sdmCd);
		param.put("REVSN_NO", revsnNo);
		
		//교과목 삭제
		studPlanDAO.deleteCnsltProf(param);
		
		return 0;
	}
	
	/**
	 * 상담교수 insert
	 * @param sdmCd
	 * @param rawJsonObj
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public int insertCnslt(String sdmCd, int revsnNo, JSONObject rawJsonObj, HttpServletRequest request) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		
		List<String> cnsltProfList = new ArrayList<String>();
		cnsltProfList = Arrays.asList(rawJsonObj.get("CNSLT_PROF").toString().split(","));			//상담교수 listify
		List<String> cnsltProfNmList = new ArrayList<String>();
		cnsltProfNmList = Arrays.asList(rawJsonObj.get("CNSLT_PROF_NM").toString().split(","));			//상담교수 listify
		
		for(int i = 0; i < cnsltProfList.size(); i++ ) {	
			param.clear();
			param.put("SDM_CD", sdmCd);
			param.put("REVSN_NO", revsnNo);
			param.put("CNSLT_PROF_STAFF_NO", cnsltProfList.get(i));
			param.put("CNSLT_PROF_STAFF_NM", cnsltProfNmList.get(i));
			param.put("REGI_ID", StringUtil.getString(rawJsonObj.get("APLY_STUDENT_NO")));
			param.put("REGI_IP", request.getRemoteAddr());
			
			studPlanDAO.insertCnsltProf(param);
		}
		return 0;
	}

	/**
	 * 내 학생설계전공 리스트
	 * @param studentNo
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Object> getMyList(String studentNo) throws Exception {
		return studPlanDAO.getMyList(studentNo);
	}
	
	/**
	 * 내 학생설계전공 교과목별 전공 리스트
	 * @param studentNo
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Object> getMySbjtList(String studentNo) throws Exception {
		return studPlanDAO.getMySbjtList(studentNo);
	}


	/**
	 * 승인된 전체 학생설계전공 리스트
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Object> getStudPlanList(Map<String, Object> param) throws Exception {
		return studPlanDAO.getStudPlanList(param);
	}
	@Override
	public int getStudPlanListCount(Map<String, Object> param) throws Exception {
		return studPlanDAO.getStudPlanListCount(param);
	}
	
	/**
	 * 승인된 전체  학생설계전공 교과목별 전공 리스트
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Object> getStudPlanSbjtList(Map<String, Object> param) throws Exception {
		return studPlanDAO.getStudPlanSbjtList(param);
	}
	
	/**
	 * 승인된 전체 학생설계전공 전공목록
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Object> getStudPlanMajorList() throws Exception {
		return studPlanDAO.getStudPlanMajorList();
	}
	
	/**
	 * 학생설계전공 기본정보 select
	 * @param sdmCd
	 * @param revsnNo
	 * @return
	 * @throws Exception
	 */
	@Override
	public DataMap getStudInfmt(String sdmCd, String revsnNo) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("SDM_CD", sdmCd);
		param.put("REVSN_NO", revsnNo);
		
		return studPlanDAO.getStudInfmt(param);
	}

	/**
	 * 학생설계전공 교과목 select
	 * @param sdmCd
	 * @param revsnNo
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Object> getStudSbjt(String sdmCd, String revsnNo) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("SDM_CD", sdmCd);
		param.put("REVSN_NO", revsnNo);
		
		return studPlanDAO.getStudSbjt(param);
	}

	/**
	 * 학생설계전공 컨설팅 select
	 * @param sdmCd
	 * @param revsnNo
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Object> getCnslt(String sdmCd, String revsnNo) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("SDM_CD", sdmCd);
		param.put("REVSN_NO", revsnNo);
		
		return studPlanDAO.getCnslt(param);
	}

	
	/**
	 * 학생설계전공 알림정보 insert
	 * @param mailData
	 * @return
	 * @throws Exception
	 */
	@Override
	public int insertNotiMsg(List<Map<String, Object>> param) throws Exception {
		return studPlanDAO.insertNotiMsg(param);
	}

	/**
	 * 학생설계전공 상태 update(상태값만 바꿈)
	 * @param sdmCd
	 * @param revsnNo
	 * @param status
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateStatus(String sdmCd, int revsnNo, String status, HttpServletRequest request) throws Exception {
		
		Map<String, Object> param = new HashMap<String, Object>();
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		String memberNo = loginVO.getMemberId();
		
		param.put("SDM_CD", sdmCd);
		param.put("REVSN_NO", revsnNo);
		param.put("STATUS", status);
		param.put("LAST_MODI_IP", request.getRemoteAddr());
		param.put("LAST_MODI_ID", memberNo);
		
		return studPlanDAO.updateStatus(param);
	}

	/**
	 * 학생설계전공 변경(프로시저 call)
	 * @param param
	 * @throws Exception
	 */
	@Override
	public void changeStud(Map<String, Object> param) throws Exception {
		studPlanDAO.changeStud(param);
		
	}

	/**
	 * 학생설계전공 변경교과목 select
	 * @param sdmCd
	 * @param revsnNo
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Object> getStudChgSbjt(String sdmCd, String revsnNo)  throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("SDM_CD", sdmCd);
		param.put("REVSN_NO", revsnNo);
		
		return studPlanDAO.getStudChgSbjt(param);
	}

	/**
	 * 학생설계전공 변경교과목 insert
	 * @param sdmCd
	 * @param revsnNo
	 * @param rawJsonObj
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public int insertChgStudSbjt(String sdmCd, int revsnNo, JSONObject rawJsonObj, HttpServletRequest request)  throws Exception {
		Map<String, Object> param = new HashMap<>();
	    List<Map<String, String>> subjectList = new ArrayList<>();
	    
	    String[] keys = {
	        "ORG_CODE", "COLG_CD", "COLG_NM" , "DEPT_CD", "DEPT_NM" , "MAJOR_CD", "MAJOR_NM", "COMDIV_CODE", 
	        "COMDIV_CODE_NM", "ORG_COMDIV_CODE", "SUBJECT_CD", "SUBJECT_NM", "SUBJECT_ENM", "COM_GRADE", 
	        "COM_GRADE_NM", "YEAR", "SMT", "SMT_NM", "CDT_NUM", "WTIME_NUM", "PTIME_NUM", "CHG_RESN", "CHG_FG",
	        "CHG_ORG_CODE", "CHG_COLG_CD", "CHG_COLG_NM", "CHG_DEPT_CD", "CHG_DEPT_NM" , "CHG_MAJOR_CD", "CHG_MAJOR_NM", 
	        "CHG_COMDIV_CODE", "CHG_COMDIV_CODE_NM", "CHG_ORG_COMDIV_CODE", "CHG_SUBJECT_CD", 
	        "CHG_SUBJECT_NM", "CHG_SUBJECT_ENM", "CHG_COM_GRADE", "CHG_COM_GRADE_NM", "CHG_YEAR", "CHG_SMT", 
	        "CHG_SMT_NM", "CHG_CDT_NUM", "CHG_WTIME_NUM", "CHG_PTIME_NUM"
	    };

	    
	    if (rawJsonObj.get("MAJOR_CD") != null) {
	        int size = rawJsonObj.getJSONArray("MAJOR_CD").size();

	        // 미리 모든 키의 값을 이스케이프 처리하여 배열에 저장
	        Map<String, String[]> escapedValues = new HashMap<>();
	        for (String key : keys) {
	            if (rawJsonObj.containsKey(key)) {
	                JSONArray jsonArray = rawJsonObj.getJSONArray(key);
	                String[] escapedArray = new String[size];
	                for (int i = 0; i < size; i++) {
	                    String value = jsonArray.optString(i, "");
	                    escapedArray[i] = value.isEmpty() ? null : value.replace("'", "´");
	                }
	                escapedValues.put(key, escapedArray);
	            }
	        }

	        // 이스케이프 처리된 값을 subjectMap에 추가
	        for (int i = 0; i < size; i++) {
	            Map<String, String> subjectMap = new HashMap<>();
	            for (String key : keys) {
	                if (escapedValues.containsKey(key)) {
	                    subjectMap.put(key, escapedValues.get(key)[i]);
	                }
	            }
	            subjectList.add(subjectMap);
	        }
	    }

	    if (!subjectList.isEmpty()) {
	        String lastModiId = StringUtil.getString(rawJsonObj.get("APLY_STUDENT_NO"));
	        String lastModiIp = request.getRemoteAddr();

	        for (int i = 0; i < subjectList.size(); i++) {
	            param.clear();
	            Map<String, String> subjectMap = subjectList.get(i);

	            param.put("SDM_CD", sdmCd);
	            param.put("REVSN_NO", revsnNo);
	            param.put("LAST_MODI_ID", lastModiId);
	            param.put("LAST_MODI_IP", lastModiIp);
	            param.put("SBJT_SEQ", i + 1);

	            for (String key : keys) {
	                param.put(key, subjectMap.get(key));
	            }

	            // 교과목 insert(개별행 -> 반복)
	            studPlanDAO.insertChgStudSbjt(param);
	        }
	    }

	    return 0;
	}
	
	/**
	 * 학생설계전공 변경교과목 -> 교과목테이블 insert
	 * @param sdmCd
	 * @param revsnNo
	 * @param rawJsonObj
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public int insertChgDataToStudSbjt(String sdmCd, int revsnNo, JSONObject rawJsonObj, HttpServletRequest request)  throws Exception {	    	    
	    
	    Map<String, Object> param = new HashMap<>();
	    List<Map<String, String>> subjectList = new ArrayList<>();
	    
	    String[] keys = {
	        "CHG_FG",
	        "CHG_ORG_CODE", "CHG_COLG_CD", "CHG_COLG_NM", "CHG_DEPT_CD", "CHG_DEPT_NM", "CHG_MAJOR_CD", "CHG_MAJOR_NM", 
	        "CHG_COMDIV_CODE", "CHG_COMDIV_CODE_NM", "CHG_ORG_COMDIV_CODE", "CHG_SUBJECT_CD", 
	        "CHG_SUBJECT_NM", "CHG_SUBJECT_ENM", "CHG_COM_GRADE", "CHG_COM_GRADE_NM", "CHG_YEAR", "CHG_SMT", 
	        "CHG_SMT_NM", "CHG_CDT_NUM", "CHG_WTIME_NUM", "CHG_PTIME_NUM"
	    };
	      

	    
	    if (rawJsonObj.get("MAJOR_CD") != null) {
	        int size = rawJsonObj.getJSONArray("MAJOR_CD").size();

	        // 미리 모든 키의 값을 이스케이프 처리하여 배열에 저장
	        Map<String, String[]> escapedValues = new HashMap<>();
	        for (String key : keys) {
	            if (rawJsonObj.containsKey(key)) {
	                JSONArray jsonArray = rawJsonObj.getJSONArray(key);
	                String[] escapedArray = new String[size];
	                for (int i = 0; i < size; i++) {
	                    String value = jsonArray.optString(i, "");
	                    escapedArray[i] = value.isEmpty() ? null : value.replace("'", "´");
	                }
	                escapedValues.put(key, escapedArray);
	            }
	        }
	        
	        for (int i = 0; i < size; i++) {
	            Map<String, String> subjectMap = new HashMap<>();
	            boolean isDeleted = true; // 변경 시 삭제된 교과목인지 확인하기 위한 플래그

	            for (String key : keys) {
	                if (rawJsonObj.containsKey(key)) {
	                    String value = rawJsonObj.getJSONArray(key).optString(i, "");
	                    if (value.isEmpty()) {
	                        subjectMap.put(key, null); // 빈 문자열을 null로 치환
	                    } else {
	                        subjectMap.put(key, escapedValues.get(key)[i]);
	                    }
	                }
	            }

	            //삭제(D)일 경우 false세팅
	            if ("D".equals(subjectMap.get("CHG_FG"))) {
	            	isDeleted = false;
	            }

	            if (isDeleted) {
	                subjectList.add(subjectMap);
	            }
	        }
	    }

	    if (!subjectList.isEmpty()) {
	        String lastModiId = StringUtil.getString(rawJsonObj.get("APLY_STUDENT_NO"));
	        String lastModiIp = request.getRemoteAddr();

	        for (int i = 0; i < subjectList.size(); i++) {
	            param.clear();
	            Map<String, String> subjectMap = subjectList.get(i);

	            param.put("SDM_CD", sdmCd);
	            param.put("REVSN_NO", revsnNo);
	            param.put("LAST_MODI_ID", lastModiId);
	            param.put("LAST_MODI_IP", lastModiIp);
	            param.put("SBJT_SEQ", i + 1);

	            //CHG_ 접두사 제거
	            for (String key : keys) {
	                String newKey = key.startsWith("CHG_") ? key.substring(4) : key; // "CHG_" 접두사를 제거
	                param.put(newKey, subjectMap.get(key));
	            }

	            // 교과목 insert(개별행 -> 반복)
	            studPlanDAO.insertStudSbjt(param);
	        }
	    }

	    return 0;
	}
	


	/**
	 * 학생설계전공 변경교과목 delete
	 * @param sdmCd
	 * @param revsnNo
	 * @param rawJsonObj
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public int deleteChgStudSbjt(String sdmCd, int revsnNo, HttpServletRequest request)  throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("SDM_CD", sdmCd);
		param.put("REVSN_NO", revsnNo);
		
		return studPlanDAO.deleteChgStudSbjt(param);
	}

	/**
	 * 학생설계전공 기본정보 교수정보 update
	 * @param sdmCd
	 * @param revsnNo
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateChgStudInfmt(String sdmCd, int revsnNo, JSONObject rawJsonObj, HttpServletRequest request, String status)  throws Exception {
		String lastModiId = StringUtil.getString(rawJsonObj.get("APLY_STUDENT_NO"));
		
		
	    Map<String, Object> param = new HashMap<>();
	    String[] keys = {
	        "GUID_PROF_STAFF_NO", "GUID_PROF_STAFF_NM", "CNSLT_PROF", "CNSLT_PROF_NM", "SDM_CHG_RESN"
	    };

        	    	    
	    param.put("SDM_CD", sdmCd);
	    param.put("REVSN_NO", revsnNo);
	    param.put("STATUS", status);
	    param.put("LAST_MODI_IP", request.getRemoteAddr());
	    param.put("LAST_MODI_ID", lastModiId);

	    for (String key : keys) {
	        String value = StringUtil.getString(rawJsonObj.get(key));
	        if (value == null || value.isEmpty()) {
	            param.put(key, null);
	        } else {
	            param.put(key, value.replace("'", "´"));
	        }
	    }
		
		return studPlanDAO.updateChgStudInfmt(param);
	}

	
	/**
	 * 알림 가져오기
	 * @param memberNo
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Object> getNotiMsg(String memberNo)  throws Exception {
		return studPlanDAO.getNotiMsg(memberNo);
	}

	/**
	 * 알림 읽음처리
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Override
	public int notiMsgCnf(Map<String, Object> param) throws Exception {
		return studPlanDAO.notiMsgCnf(param);
	}
	
	/**
	 * 알림 삭제처리
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Override
	public int notiMsgDel(Map<String, Object> param) throws Exception {
		return studPlanDAO.notiMsgDel(param);
	}

	/**
	 * 학생설계전공 교수 전체 탭 카운트
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Object> getAllTabCnt(Map<String, Object> param) {
		return studPlanDAO.getAllTabCnt(param);
	}
	
	/**
	 * 승인요청
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Object> getRAList(Map<String, Object> param) {
		return studPlanDAO.getRAList(param);
	}

	/**
	 * 학생설계전공 탭 별 리스트
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Object> getStudPlanTabList(JSONObject rawJsonObj) throws Exception {
		Map<String, Object> param = new HashMap<>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		List<Object> resultList = null;
		
		param.put("MEMBER_ID", loginVO.getMemberId());
		param.put("MEMBER_DEPT_CD", loginVO.getDeptCd());
		
		switch(rawJsonObj.get("TAB").toString()) {
			case "RA" : resultList = studPlanDAO.getRAList(param); break;
			case "RC" : resultList = studPlanDAO.getRCList(param); break;
			case "AC" : resultList = studPlanDAO.getACList(param); break;
			case "WC" : resultList = studPlanDAO.getWCList(param); break;
			case "CC" : resultList = studPlanDAO.getCCList(param); break;
			case "WJ" : resultList = studPlanDAO.getWJList(param); break;
			case "CJ" : resultList = studPlanDAO.getCJList(param); break;
		}
		
		return resultList;
	}

	/**
	 * 학생설계전공 승인
	 * @param sdmCd
	 * @param revsnNo
	 * @param status
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public int profApprove(JSONObject rawJsonObj, String status, HttpServletRequest request) throws Exception {
		Map<String, Object> param = new HashMap<>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		String sdmCd = rawJsonObj.getString("SDM_CD");
		String revsnNo = rawJsonObj.getString("REVSN_NO");
        String lastModiId = loginVO.getMemberId();
        String lastModiIp = request.getRemoteAddr();

		
		param.put("SDM_CD", sdmCd);
		param.put("REVSN_NO", revsnNo);
		param.put("STATUS", status);
		param.put("LAST_MODI_ID", lastModiId);
		param.put("LAST_MODI_IP", lastModiIp);
		
		return studPlanDAO.profApprove(param);
	}

	/**
	 * 학생설계전공 반려
	 * @param sdmCd
	 * @param revsnNo
	 * @param status
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public int profReject(JSONObject rawJsonObj, String status, HttpServletRequest request) throws Exception {
		Map<String, Object> param = new HashMap<>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		String sdmCd = rawJsonObj.getString("SDM_CD");
		String revsnNo = rawJsonObj.getString("REVSN_NO");	
		String rjtResn = rawJsonObj.getString("RJT_RESN");	
		String lastModiId = loginVO.getMemberId();
        String lastModiIp = request.getRemoteAddr();

            
		param.put("SDM_CD", sdmCd);
		param.put("REVSN_NO", revsnNo);
		param.put("STATUS", status);
		param.put("RJT_RESN", rjtResn);
		param.put("LAST_MODI_ID", lastModiId);
		param.put("LAST_MODI_IP", lastModiIp);
		
		
		return studPlanDAO.profReject(param);
	}

	/**
	 * 학생설계전공 교수 개인 컨설팅정보 select
	 * @param sdmCd
	 * @param revsnNo
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Object> getProfCnslt(String sdmCd, String revsnNo) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		param.put("SDM_CD", sdmCd);
		param.put("REVSN_NO", revsnNo);
		param.put("MEMBER_ID", loginVO.getMemberId());
		
		return studPlanDAO.getCnslt(param);
	}

	
	/**
	 * 학생설계전공 교수 개인 컨설팅정보 update
	 * @param rawJsonObj
	 * @param request
	 * @param action
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateProfCnslt(JSONObject rawJsonObj, HttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		param.put("SDM_CD", rawJsonObj.getString("SDM_CD"));
		param.put("REVSN_NO", rawJsonObj.getString("REVSN_NO"));
		param.put("SBJT_CNST_ADV_OPIN", rawJsonObj.getString("SBJT_CNST_ADV_OPIN"));
		param.put("MJ_NM_ADV_OPIN", rawJsonObj.getString("MJ_NM_ADV_OPIN"));
		param.put("AWD_DEGR_NM_ADV_OPIN", rawJsonObj.getString("AWD_DEGR_NM_ADV_OPIN"));
		param.put("MEET_CNSLT_ADV_OPIN", rawJsonObj.getString("MEET_CNSLT_ADV_OPIN"));
		param.put("ACTION", rawJsonObj.getString("ACTION"));
		param.put("LAST_MODI_ID", loginVO.getMemberId());
		param.put("LAST_MODI_IP", request.getRemoteAddr());
		
		
		return studPlanDAO.updateProfCnslt(param);
		
	}

	/**
	 * 학생설계전공 교수 상담접수
	 * @param rawJsonObj
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public int insertProfCnslt(JSONObject rawJsonObj, HttpServletRequest request) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		param.put("SDM_CD", rawJsonObj.getString("SDM_CD"));
		param.put("REVSN_NO", rawJsonObj.getString("REVSN_NO"));
		param.put("CNSLT_PROF_STAFF_NO", loginVO.getMemberId());
		param.put("CNSLT_PROF_STAFF_NM", loginVO.getMemberName());
		param.put("LAST_MODI_IP", request.getRemoteAddr());
		
		return studPlanDAO.insertProfCnslt(param);
	}
	
	/**
	 * 학생설계전공 교수 상담접수 취소
	 * @param rawJsonObj
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public int deleteProfCnslt(JSONObject rawJsonObj, HttpServletRequest request) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		param.put("SDM_CD", rawJsonObj.getString("SDM_CD"));
		param.put("REVSN_NO", rawJsonObj.getString("REVSN_NO"));
		param.put("CNSLT_PROF_STAFF_NO", loginVO.getMemberId());
		
		return studPlanDAO.deleteProfCnslt(param);
	}

	@Override
	public DataMap getSimilityMj(JSONObject rawJsonObj) throws Exception {
        Map<String, Object> param = new HashMap<>();
        List<Map<String, String>> subjectList = new ArrayList<>();
        String[] keys = {"SUBJECT_CD", "MAJOR_CD", "YEAR", "COM_GRADE", "SMT"};

        JSONArray sbjtArray = rawJsonObj.getJSONArray("sbjtArray");

        for (int i = 0; i < sbjtArray.size(); i++) {
            JSONObject sbjtObj = sbjtArray.getJSONObject(i);
            Map<String, String> subjectMap = new HashMap<>();
            for (String key : keys) {
                String value = sbjtObj.optString(key, "");
                if (value != null && !value.isEmpty()) {
                    subjectMap.put(key, value);
                }
            }
            subjectList.add(subjectMap);
        }

        param.put("SUBJECTLIST", subjectList);
	    
		return studPlanDAO.getSimilityMj(param);
	}

	@Override
	public int deleteBookmarkBySdmCd(String sdmCd) {
		return studPlanDAO.deleteBookmarkBySdmCd(sdmCd);
		
	}
	
	/**
	 * 학생설계전공 연계(승인+전공코드 연계)
	 * @throws Exception
	 */
	@Override
	public void interfaceStudPlanConfirmation() throws Exception {
		List<Map<String, Object>> notConfirmedList = studPlanDAO.getNotConfirmedStudPlan();

        if (!notConfirmedList.isEmpty()) {
            List<Map<String, Object>> confirmedList = studPlanMartDAO.getConfirmedStudPlan(notConfirmedList);

            if (!confirmedList.isEmpty()) {
                for (Map<String, Object> confirmedItem : confirmedList) {
                	Map<String, Object> param = new HashMap<>();
                    param.put("SDM_CD",String.valueOf(confirmedItem.get("SDM_CD")));
                    param.put("REVSN_NO",String.valueOf(confirmedItem.get("REVSN_NO")));
                    param.put("SDM_DEPT_CD",String.valueOf(confirmedItem.get("SDM_DEPT_CD")));

                	studPlanDAO.updateNotConfirmedStudPlan(param);
                }
            }
        }
		
	}










}