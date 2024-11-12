package rbs.modules.log.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import rbs.modules.board.mapper.BoardMapper;
import rbs.modules.contact.mapper.ContactMapper;
import rbs.modules.log.service.FileLogService;
import rbs.modules.log.service.MlLogService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;


/**
 * 관리 로그에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
@Service("mlLogService")
public class MlLogServiceImpl extends EgovAbstractServiceImpl implements MlLogService {

	@Resource(name="contactMapper")
	private ContactMapper contactDao;
    
	/**
	 * 전체 목록 수
	 * @param param
	 * @return
	 */
	@Override
	public int getTotalCount(Map<String, Object> param){
		return contactDao.getMLTotalCount(param);
	}
	
	/**
	 * 전체 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(Map<String, Object> param){
		return contactDao.getMLList(param);
	}
}