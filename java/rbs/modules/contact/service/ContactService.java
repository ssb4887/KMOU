package rbs.modules.contact.service;

import java.util.List;
import java.util.Map;

import com.woowonsoft.egovframework.form.DataMap;

public interface ContactService {
	/**
	 * 초기화
	 * @throws Exception
	 */
	//public void init() throws Exception;
	public List<Object> getBackupYearList(Map<String, Object> param);
	
	public int getSiteTotalSum(Map<String, Object> param);
	public List<Object> getSiteList(Map<String, Object> param);
	
	public int getPageTotalSum(Map<String, Object> param);
	public List<Object> getPageList(Map<String, Object> param);
	
	public int getSitePageMaxCount(Map<String, Object> param);
	public DataMap getSitePageTotalView(Map<String, Object> param);
	public List<Object> getSitePageList(Map<String, Object> param);
	

	public int getMenuTotalSum(Map<String, Object> param);
	public Map<Object, Object> getMenuMap(Map<String, Object> param);
	
	public int getUserTotalCount(Map<String, Object> param);
	public List<Object> getUserList(Map<String, Object> param);
	
	public int insert(Map<String, Object> param) throws Exception;
	
	/**
	 * 통합관리 시스템 접속로그정보
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int insertMng(Map<String, Object> param) throws Exception;
	
	/**
	 * 접속자 메뉴로그정보
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int insertMl(Map<String, Object> param) throws Exception;
	public int delete(Map<String, Object> param) throws Exception;
}