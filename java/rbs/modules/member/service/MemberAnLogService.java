package rbs.modules.member.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import net.sf.json.JSONObject;

import com.woowonsoft.egovframework.form.DataMap;

/**
 * 기관정보관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
public interface MemberAnLogService {
	
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
	
	public void setEprivacy(Logger logger, String logType, DataMap dt, String changePw, JSONObject items, String memberIdx, String regiIdx, String dbRegiId, String dbRegiName) throws Exception;
}