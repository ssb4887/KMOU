package rbs.egovframework.schema.mapper;

import java.util.Map;








import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

/**
 * 테이블, 컬럼 생성/삭제
 * @author user
 *
 */
public abstract class SchemaMapper extends EgovAbstractMapper{

	/**
	 * 테이블 생성
	 * @param param
	 * @return
	 */
    public abstract int createTable(String designType, Map<String, Object> param);
    
    /**
     * 테이블 삭제
     * @param param
     * @return
     */
    public abstract int dropTable(Map<String, Object> param);
    
    /**
     * 컬럼추가
     * @param param
     * @return
     */
    public abstract int addColumn(Map<String, Object> param);
    
    public abstract int modifyColumn(Map<String, Object> param);
    
    public abstract int modifyComment(Map<String, Object> param);
    
    /**
     * 컬럼명수정
     * @param param
     * @return
     */
    //public abstract int renameColumn(Map<String, Object> param);
    
    /**
     * 컬럼삭제
     * @param param
     * @return
     */
    public abstract int dropColumn(Map<String, Object> param);
}