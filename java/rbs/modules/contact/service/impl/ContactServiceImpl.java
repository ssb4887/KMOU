package rbs.modules.contact.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.woowonsoft.egovframework.form.DataMap;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.modules.contact.mapper.ContactMapper;
import rbs.modules.contact.service.ContactService;

@Service("contactService")
public class ContactServiceImpl extends EgovAbstractServiceImpl implements ContactService {

	/**
	 * 초기화 : 현재 접속자 삭제
	 * @throws Exception
	@PostConstruct
	@Override
	public void init() throws Exception{
		contactDao.deleteAll();
	}
	 */
	
	@Resource(name="contactMapper")
	private ContactMapper contactDao;
	
	@Override
	public List<Object> getBackupYearList(Map<String, Object> param) {
		return contactDao.getBackupYearList(param);
	}
	
	@Override
	public int getSiteTotalSum(Map<String, Object> param) {
		return contactDao.getSiteTotalSum(param);
	}
	
	@Override
	public List<Object> getSiteList(Map<String, Object> param) {
		return contactDao.getSiteList(param);
	}
	
	@Override
	public int getPageTotalSum(Map<String, Object> param) {
		return contactDao.getPageTotalSum(param);
	}
	
	@Override
	public List<Object> getPageList(Map<String, Object> param) {
		return contactDao.getPageList(param);
	}

	@Override
	public int getSitePageMaxCount(Map<String, Object> param){
		return contactDao.getSitePageMaxCount(param);
	}
	@Override
	public DataMap getSitePageTotalView(Map<String, Object> param){
		return contactDao.getSitePageTotalView(param);
	}
	@Override
	public List<Object> getSitePageList(Map<String, Object> param){
		return contactDao.getSitePageList(param);
	}

	@Override
	public int getMenuTotalSum(Map<String, Object> param) {
		return contactDao.getMenuTotalSum(param);
	}
	
	@Override
	public Map<Object, Object> getMenuMap(Map<String, Object> param) {
		return contactDao.getMenuMap(param);
	}
	
	@Override
	public int getUserTotalCount(Map<String, Object> param) {
		return contactDao.getUserTotalCount(param);
	}
	
	@Override
	public List<Object> getUserList(Map<String, Object> param) {
		return contactDao.getUserList(param);
	}
	
	@Override
	public int insert(Map<String, Object> param) throws Exception{
		return contactDao.insert(param);
	}
	
	/**
	 * 통합관리 시스템 접속로그정보
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Override
	public int insertMng(Map<String, Object> param) throws Exception{
		return contactDao.insertMng(param);
	}
	
	/**
	 * 접속자 메뉴로그정보
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Override
	public int insertMl(Map<String, Object> param) throws Exception{
		return contactDao.insertMl(param);
	}
	
	@Override
	public int delete(Map<String, Object> param) throws Exception{
		return contactDao.delete(param);
	}
}