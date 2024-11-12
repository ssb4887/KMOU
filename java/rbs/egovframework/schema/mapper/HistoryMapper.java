package rbs.egovframework.schema.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;


/**
 * 테이블, 컬럼 생성/삭제
 * @author user
 *
 */
@Repository("historySchemaMapper")
public class HistoryMapper extends SchemaMapper{

	/**
	 * 테이블 생성
	 * @param param
	 * @return
	 */
	@Override
    public int createTable(String designType, Map<String, Object> param){
    	int result = 0;
		result = super.update("rbs.egovframework.schema.history.schemaMapper.allCreateTable", param);
        return result;
    }
    
    /**
     * 테이블 삭제
     * @param param
     * @return
     */
	@Override
    public int dropTable(Map<String, Object> param){
    	int result = 0;
        result = super.update("rbs.egovframework.schema.history.schemaMapper.allDropTable", param);
        
        return result;
    }
    
    /**
     * 컬럼추가
     * @param param
     * @return
     */
	@Override
    public int addColumn(Map<String, Object> param){
    	int result = 0;
    	result = super.update("rbs.egovframework.schema.history.schemaMapper.addColumn",param);

        return result;
    }
    
    /**
     * 컬럼수정
     * @param param
     * @return
     */
	@Override
    public int modifyColumn(Map<String, Object> param){
    	int result = 0;
    	result = super.update("rbs.egovframework.schema.history.schemaMapper.modifyColumn",param);

        return result;
    }
    
    /**
     * 코멘트 등록
     * @param param
     * @return
     */
	@Override
    public int modifyComment(Map<String, Object> param){
    	int result = 0;
    	result = super.update("rbs.egovframework.schema.history.schemaMapper.modifyComment",param);

        return result;
    }
    
    /**
     * 컬럼삭제
     * @param param
     * @return
     */
	@Override
    public int dropColumn(Map<String, Object> param){
        return super.update("rbs.egovframework.schema.history.schemaMapper.dropColumn",param);
    	
    }
}