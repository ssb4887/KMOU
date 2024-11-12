package rbs.modules.member.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;


/**
 * 기관정보관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
public interface MemberService {
	
	/**
	 * 회원약관
	 * @param param
	 * @return
	 */
	public DataMap getMemberClause(Map<String, Object> param);

	/**
	 * 중복확인
	 * @param param
	 * @return
	 */
	public int getDuplicateCount(Map<String, Object> param);

	/**
	 * 전체 목록 수
	 * @param param
	 * @return
	 */
	public int getTotalCount(Map<String, Object> param);
	
	/**
	 * 전체 목록
	 * @param param
	 * @return
	 */
	public List<Object> getList(Map<String, Object> param);


	/**
	 * 전체 목록 수
	 * @param param
	 * @return
	 */
	public int getTotTotalCount(Map<String, Object> param);
	
	/**
	 * 전체 목록
	 * @param param
	 * @return
	 */
	public List<Object> getTotSearchList(Map<String, Object> param);
	
	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	public DataMap getView(Map<String, Object> param);

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	public DataMap getModify(Map<String, Object> param);
	

	public List<Object> getMemberGrupList(Map<String, Object> param);
	
	/**
	 * 등록
	 * @param param
	 * @return
	 */
	public int insert(String regiIp, boolean isUseNameAuth, int useRsa, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception;
	
	/**
	 * 수정
	 * @param param
	 * @return
	 */
	public int update(String submitType, String mbrCd, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception;

	/**
	 * 중복확인
	 * @param param
	 * @return
	 */
	public int getSnsDuplicateCount(Map<String, Object> param);
	
	public int snsInsert(String mbrCd, String regiIp, ParamForm parameterMap) throws Exception;
	/**
	 * 전체 목록 수
	 * @param param
	 * @return
	 */
	public int getDeleteCount(Map<String, Object> param);
	
	/**
	 * 전체 목록
	 * @param param
	 * @return
	 */
	public List<Object> getDeleteList(Map<String, Object> param);
	
	/**
	 * 삭제
	 * @param param
	 * @return
	 */
	public int delete(String[] deleteIdxs, String regiIp) throws Exception;
	
	/**
	 * 복원
	 * @param param
	 * @return
	 */
	public int restore(String[] restoreIdxs, String regiIp) throws Exception;

	/**
	 * 회원탈퇴
	 * @param param
	 * @return
	 */
	public int joinout(JSONObject items, String[] deleteIdxs) throws Exception;

	/**
	 * 완전삭제
	 * @param param
	 * @return
	 */
	public int cdelete(JSONObject items, String[] deleteIdxs) throws Exception;
	
	public int pwupdate(int useRSA, ParamForm parameterMap, JSONObject items) throws Exception;
	
	public int pwupdate1(String pwdModiType, String mbrCd, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception;
	
	public boolean isMbrPwdMatched(int useRSA, ParamForm parameterMap, JSONObject items) throws Exception;

	public List<Object> getReAgreeList(String dDays);
	
	public int reAgreeCdelete() throws Exception;
}