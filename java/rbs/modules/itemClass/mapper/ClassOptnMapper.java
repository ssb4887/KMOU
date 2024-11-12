package rbs.modules.itemClass.mapper;

import java.util.List;
import java.util.Map;



import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.RbsAbstractMapper;

import com.woowonsoft.egovframework.form.DataMap;

/**
 * 일반회원관리에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("classOptnMapper")
public class ClassOptnMapper extends RbsAbstractMapper{

	public List<Object> getOptnList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.itemClass" + getLangFlag() + ".optionMapper.optnList", getLangParam(param));
    }
	
    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 기관 목록정보
     */
	public List<Object> getList(String lang, Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.itemClass" + getLangFlag() + ".optionMapper.selectList", getLangParam(lang, param));
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getTotalCount(String lang, Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.itemClass" + getLangFlag() + ".optionMapper.selectCount", getLangParam(lang, param));
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getDuplicateCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.itemClass.optionMapper.selectDuplicateCount", param);
    }

    public int getMaxOrdIdx(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.itemClass.optionMapper.selectMaxOrdIdx", param);
    }

    public int getOrdIdx(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.itemClass.optionMapper.selectOrdIdx", param);
    }


    public int getNextOrdIdx(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.itemClass.optionMapper.selectNextOrdIdx", param);
    }
    
    /**
     * 이동할 class_idx
     * @param param
     * @return
     */
	public List<Object> getSourceList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.itemClass.optionMapper.selectSourceList", param);
    }
	/*
    public int getOrdCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.itemClass.optionMapper.selectOrdCnt", param);
    }*/

    public int getInChildCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.itemClass.optionMapper.selectInChild", param);
    }

    public String getPrtClassIdx(Map<String, Object> param) {
        return (String)selectOne("rbs.modules.itemClass.optionMapper.selectPrtClassIdx", param);
    }

	public DataMap getClassKeyView(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.itemClass.optionMapper.selectClassKeyView", param);
	}
    

    /**
     * 화면에 조회된일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int updateOrdIdx(Map<String, Object> param){
        return super.update("rbs.modules.itemClass.optionMapper.updateOrdIdx",param);
    }


    /**
     * 게시물 key 조회한다.
     * @return int key
     */
    public int getNextId(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.itemClass.optionMapper.nextId", param);
    }
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getModify(String lang, Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.itemClass" + getLangFlag() + ".optionMapper.selectModify", getLangParam(lang, param));
	}

    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insert(Map<String, Object> param){
    	Object result = super.insert("rbs.modules.itemClass.optionMapper.insert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }
    public int insertLang(Map<String, Object> param){
    	Object result = super.insert("rbs.modules.itemClass" + getLangFlag() + ".optionMapper.insert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }

    /**
     * 화면에 조회된일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int update(Map<String, Object> param){
        return super.update("rbs.modules.itemClass.optionMapper.update",param);
    }
    
    public int updateLang(String lang, Map<String, Object> param){
        return super.update("rbs.modules.itemClass" + getLangFlag() + ".optionMapper.update",getLangParam(lang, param));
    }

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 삭제목록정보
     */
	public List<Object> getDeleteList(String lang, Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.itemClass" + getLangFlag() + ".optionMapper.deleteList", getLangParam(lang, param));
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getDeleteCount(String lang, Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.itemClass" + getLangFlag() + ".optionMapper.deleteCount", getLangParam(lang, param));
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
     * @param param 삭제정보
     */
    public int delete(Map<String, Object> param){
    	return super.update("rbs.modules.itemClass.optionMapper.delete",param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
     * @param param 삭제정보
     */
    public int deleteLang(Map<String, Object> param){
    	return super.update("rbs.modules.itemClass" + getLangFlag() + ".optionMapper.delete",param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
     * @param param 복원정보
     */
    public int restore(Map<String, Object> param){
    	return super.update("rbs.modules.itemClass.optionMapper.restore",param);
    }
    
    /**
     * 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
     * @param param 복원정보
     */
    public int restoreLang(Map<String, Object> param){
    	return super.update("rbs.modules.itemClass" + getLangFlag() + ".optionMapper.restore",param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제
     * @param param 완전삭제정보
     */
    public int cdelete(Map<String, Object> param){
    	return super.delete("rbs.modules.itemClass.optionMapper.cdelete", param);
    }
    
    /**
     * 순서 update
     * @param param
     * @return
     */
    public int updateTotOrdIdx(Map<String, Object> param) {
    	return super.update("rbs.modules.itemClass.optionMapper.updateTotOrdIdx", param);
    }
}