package rbs.egovframework.schema.service;


import java.util.HashMap;
import java.util.Map;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.woowonsoft.egovframework.service.SchemaService;

import rbs.egovframework.schema.mapper.BoardMapper;

/**
 * 기관정보관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 */
@Service("boardSchemaService")
public class BoardServiceImpl extends EgovAbstractServiceImpl implements SchemaService {

	/** boardDAO */
	@Resource(name="boardSchemaMapper")
	private BoardMapper boardDAO;

	@Override
	public int addColumn(String columnName, String columnType) throws Exception{
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("columnName", columnName);
		param.put("columnType", columnType);
		return boardDAO.addColumn(param);
	}
	/*public int renameColumn(String columnName, String newColumnName) throws Exception{
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("columnName", columnName);
		param.put("newColumnName", newColumnName);
		return boardDAO.renameColumn(param);
		
	}*/
	@Override
	public int dropColumn(String columnName) throws Exception{
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("columnName", columnName);
		return boardDAO.dropColumn(param);
	}

}