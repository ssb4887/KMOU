package rbs.modules.contents.mapper;

import java.util.List;
import java.util.Map;




import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.RbsAbstractMapper;

import com.woowonsoft.egovframework.form.DataMap;

/**
 * 일반회원관리에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("contVersionMapper")
public class ContVersionMapper extends RbsAbstractMapper{

	public List<Object> getOptnList(String lang, Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.contents" + getLangFlag() + ".contVersionMapper.optnList", getLangParam(lang, param));
    }
	
    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 기관 목록정보
     */
	public List<Object> getList(String lang, Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.contents" + getLangFlag() + ".contVersionMapper.selectList", getLangParam(lang, param));
    }

    /**
     * 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.contents.contVersionMapper.selectDuplicateCount", param);
    }

    /**
     * 전체 언어 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getTotalLangCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.contents" + getLangFlag() + ".contVersionMapper.selectTotalCount", param);
    }
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getModify(String lang, Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.contents" + getLangFlag() + ".contVersionMapper.selectModify", getLangParam(lang, param));
	}

    /**
     * 게시물 key 조회한다.
     * @return int key
     */
    public int getNextId(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.contents.contVersionMapper.nextId", param);
    }
    
    /**
     * 게시물 key 조회한다.
     * @return int key
     */
    public int getNextId(String lang, Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.contents" + getLangFlag() + ".contVersionMapper.nextId", getLangParam(lang, param));
    }

    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insert(Map<String, Object> param){
    	Object result = super.insert("rbs.modules.contents.contVersionMapper.insert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }
    public int allInsertLang(Map<String, Object> param){
    	Object result = super.insert("rbs.modules.contents" + getLangFlag() + ".contVersionMapper.langAllInsert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }
    public int insertLang(String lang, Map<String, Object> param){
    	Object result = super.insert("rbs.modules.contents" + getLangFlag() + ".contVersionMapper.insert", getLangParam(lang, param));
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }
    public int copyInsertLang(String lang, Map<String, Object> param){
    	Object result = super.insert("rbs.modules.contents" + getLangFlag() + ".contVersionMapper.copyInsert", getLangParam(lang, param));
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }

    /**
     * 화면에 조회된일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int update(Map<String, Object> param){
        return super.update("rbs.modules.contents.contVersionMapper.update",param);
    }
    
    public int updateLang(String lang, Map<String, Object> param){
        return super.update("rbs.modules.contents" + getLangFlag() + ".contVersionMapper.update",getLangParam(lang, param));
    }
    
    public int workInsertLang(String lang, Map<String, Object> param){
        return super.update("rbs.modules.contents" + getLangFlag() + ".contVersionMapper.workInsert",getLangParam(lang, param));
    }
    
    public int workUpdateLang(String lang, Map<String, Object> param){
        return super.update("rbs.modules.contents" + getLangFlag() + ".contVersionMapper.workUpdate",getLangParam(lang, param));
    }

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 삭제목록정보
     */
	public List<Object> getDeleteList(String lang, Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.contents" + getLangFlag() + ".contVersionMapper.deleteList", getLangParam(lang, param));
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getDeleteCount(String lang, Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.contents" + getLangFlag() + ".contVersionMapper.deleteCount", getLangParam(lang, param));
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
     * @param param 삭제정보
     */
    public int delete(Map<String, Object> param){
    	return super.update("rbs.modules.contents.contVersionMapper.delete",param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
     * @param param 삭제정보
     */
    public int deleteLang(String lang, Map<String, Object> param){
    	return super.update("rbs.modules.contents" + getLangFlag() + ".contVersionMapper.delete",getLangParam(lang, param));
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
     * @param param 복원정보
     */
    public int restore(Map<String, Object> param){
    	return super.update("rbs.modules.contents.contVersionMapper.restore",param);
    }
    
    /**
     * 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
     * @param param 복원정보
     */
    public int restoreLang(String lang, Map<String, Object> param){
    	return super.update("rbs.modules.contents" + getLangFlag() + ".contVersionMapper.restore",getLangParam(lang, param));
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제
     * @param param 완전삭제정보
     */
    public int cdelete(Map<String, Object> param){
    	return super.delete("rbs.modules.contents.contVersionMapper.cdelete", param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제
     * @param param 완전삭제정보
     */
    public int cdeleteLang(String lang, Map<String, Object> param){
    	return super.update("rbs.modules.contents" + getLangFlag() + ".contVersionMapper.cdelete",getLangParam(lang, param));
    }
    
    /**
     * 순서 update
     * @param param
     * @return
     */
    public int updateTotOrdIdx(Map<String, Object> param) {
    	return super.update("rbs.modules.contents.contVersionMapper.updateTotOrdIdx", param);
    }
}