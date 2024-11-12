package rbs.modules.log.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import rbs.modules.board.mapper.BoardMapper;
import rbs.modules.log.service.FileLogService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;


/**
 * 관리 로그에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
@Service("fileLogService")
public class FileLogServiceImpl extends EgovAbstractServiceImpl implements FileLogService {

    // 엑셀 다운로드 사유
	@Resource(name="boardMapper")
	private BoardMapper boardDAO;
    
	/**
	 * 전체 목록 수
	 * @param param
	 * @return
	 */
	@Override
	public int getTotalCount(Map<String, Object> param){
		return boardDAO.getFileCmtCount(param);
	}
	
	/**
	 * 전체 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(Map<String, Object> param){
		return boardDAO.getFileCmtList(param);
	}
}