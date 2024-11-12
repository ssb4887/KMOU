package rbs.modules.studPlan.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.woowonsoft.egovframework.form.DataMap;

import net.sf.json.JSONObject;


/**
 * 학생설계융합전공 service
 * @author 이동근
 *
 */
public interface StudPlanService {
	
	//대학 리스트
	public List<Object> getCollegeList(Map<String, Object> param) throws Exception;
	//학부(과) 리스트
	public List<Object> getDepartList(Map<String, Object> param) throws Exception;
	//전공 리스트
	public List<Object> getMajorList(Map<String, Object> param) throws Exception;
	
	//교육과정 등록하기 - [전공교과목] 리스트
	public List<Object> getSbjtList(Map<String, Object> param) throws Exception;

	//[지도교수 검색] - 교수 리스트
	public List<Object> getProfList(Map<String, Object> param) throws Exception;

	//다음 학생설계전공 코드 가져오기
	public String getNextSdmCd(String aplyStudentNo) throws Exception;
	
	//유사도 보기
	public DataMap getSimilityMj(JSONObject rawJsonObj) throws Exception;	

	//기본정보 insert
	public int insertStudInfmt(String sdmCd, int revsnNo, JSONObject rawJsonObj, HttpServletRequest request, String status) throws Exception;	
	//기본정보 select
	public DataMap getStudInfmt(String sdmCd, String revsnNo) throws Exception;
	//기본정보 update
	public int updateStudInfmt(String sdmCd, int revsnNo, JSONObject rawJsonObj, HttpServletRequest request, String status) throws Exception;
	
	//교과목 insert
	public int insertStudSbjt(String sdmCd, int revsnCd, JSONObject rawJsonObj, HttpServletRequest request) throws Exception;
	//교과목 select
	public List<Object> getStudSbjt(String sdmCd, String revsnNo) throws Exception;	
	//교과목 delete
	public int deleteStudSbjt(String sdmCd, int revsnCd, HttpServletRequest request) throws Exception;	
	
	//컨설팅 insert
	public int insertCnslt(String sdmCd, int revsnCd, JSONObject rawJsonObj, HttpServletRequest request) throws Exception;
	//컨설팅 select
	public List<Object> getCnslt(String sdmCd, String revsnNo) throws Exception;
	//컨설팅 delete
	public int deleteCnslt(String sdmCd, int revsnCd, JSONObject rawJsonObj, HttpServletRequest request) throws Exception;	
	
	
	//내 학생설계전공 리스트
	public List<Object> getMyList(String studentNo) throws Exception;	
	
	//내 학생설계전공 교과목별 전공 리스트
	public List<Object> getMySbjtList(String studentNo) throws Exception;

	//승인된 전체 학생설계전공 리스트(페이징 포함)
	public int getStudPlanListCount(Map<String, Object> param) throws Exception;
	public List<Object> getStudPlanList(Map<String, Object> param) throws Exception;
	
	//승인된 전체 학생설계전공 교과목별 전공 리스트	
	public List<Object> getStudPlanSbjtList(Map<String, Object> param) throws Exception;
	
	//학생설계전공 전공 목록
	public List<Object> getStudPlanMajorList() throws Exception;
	
	//학생설계전공 알림정보 isnert
	public int insertNotiMsg(List<Map<String, Object>> mailData) throws Exception;
	
	//상태  update
	public int updateStatus(String sdmCd, int revsnNo, String status, HttpServletRequest request) throws Exception;
	
	//변경(프로시저 call)
	public void changeStud(Map<String, Object> param) throws Exception;
	
	//변경교과목테이블 insert	
	public int insertChgStudSbjt(String sdmCd, int revsnNo, JSONObject rawJsonObj, HttpServletRequest request) throws Exception;
	//변경교과목테이블 select
	public List<Object> getStudChgSbjt(String sdmCd, String revsnNo) throws Exception;
	//변경교과목 -> 교과목테이블 insert
	public int insertChgDataToStudSbjt(String sdmCd, int revsnNo, JSONObject rawJsonObj, HttpServletRequest request) throws Exception;
	//변경교과목테이블 delete
	public int deleteChgStudSbjt(String sdmCd, int revsnNo, HttpServletRequest request) throws Exception;
	
	//기본정보 교수정보만 update(변경시)
	public int updateChgStudInfmt(String sdmCd, int revsnNo, JSONObject rawJsonObj, HttpServletRequest request, String status) throws Exception;
	
	//알림 가져오기
	public List<Object> getNotiMsg(String memberNo) throws Exception;	
	//알림 읽음처리
	public int notiMsgCnf(Map<String, Object> param) throws Exception;
	//알림 삭제처리
	public int notiMsgDel(Map<String, Object> param) throws Exception;
	
	//교수 - 학생설계전공 전체 탭 카운트
	public List<Object> getAllTabCnt(Map<String, Object> param) throws Exception;	
	//교수 - 승인요청
	public List<Object> getRAList(Map<String, Object> param) throws Exception;
	//교수 - 학생설계전공 탭 별 리스트(ajax)
	public List<Object> getStudPlanTabList(JSONObject rawJsonObj) throws Exception;
	
	//교수 - 학생설계전공 승인
	public int profApprove(JSONObject rawJsonObj, String status, HttpServletRequest request) throws Exception;
	//교수 - 학생설계전공 반려
	public int profReject(JSONObject rawJsonObj, String status, HttpServletRequest request) throws Exception;
	
	//교수 - 개인 상담 정보
	public List<Object> getProfCnslt(String sdmCd, String revsnNo) throws Exception;
	//교수 - 상담정보  update
	public int updateProfCnslt(JSONObject rawJsonObj, HttpServletRequest request) throws Exception;
	//교수 - 상담정보  insert	
	public int insertProfCnslt(JSONObject rawJsonObj, HttpServletRequest request) throws Exception;
	//교수 - 상담정보  delete
	public int deleteProfCnslt(JSONObject rawJsonObj, HttpServletRequest request) throws Exception;
	
	
	public List<Object> getProfDepartList(Map<String, Object> param);
	
	public int deleteBookmarkBySdmCd(String sdmCd);
	
	//연계 - 학생설계전공 승인+전공코드 연계
	public void interfaceStudPlanConfirmation() throws Exception;

	

	
	




}