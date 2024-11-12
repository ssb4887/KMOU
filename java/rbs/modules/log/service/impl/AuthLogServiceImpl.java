package rbs.modules.log.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import rbs.modules.log.service.AuthLogService;
import rbs.modules.menu.mapper.MenuAuthLogMapper;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;


/**
 * 관리 로그에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
@Service("authLogService")
public class AuthLogServiceImpl extends EgovAbstractServiceImpl implements AuthLogService {

    // 엑셀 다운로드 사유
	@Resource(name="menuAuthLogMapper")
	private MenuAuthLogMapper menuAuthLogDAO;
    
	/**
	 * 전체 목록 수
	 * @param param
	 * @return
	 */
	@Override
	public int getTotalCount(Map<String, Object> param){
		return menuAuthLogDAO.getTotalCount(param);
	}
	
	/**
	 * 전체 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(Map<String, Object> param){
		return menuAuthLogDAO.getList(param);
	}
}