package rbs.modules.menu.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.RbsAbstractMapper;

import com.woowonsoft.egovframework.form.DataMap;

import com.woowonsoft.egovframework.form.EgovMap;

@Repository("menuMapper")
public class MenuMapper extends RbsAbstractMapper{

	public List<Object> getOptnList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.menu.menuMapper.optnList", param);
    }

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.menu.menuMapper.selectList", param);
    }

    /**
     * 게시물 key 조회한다.
     * @return int key
     */
    public int getMaxMenuLevel(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.menu.menuMapper.maxMenuLevel", param);
    }
    
	public List<Object> selectExcelList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.menu.menuMapper.selectExcelList", getLangParam(param));
    }
    
	/**
	 * 메뉴 작업 테이블 copy
	 * @param param
	 * @return
	 */
    public int copyInsert(boolean isDefault, Map<String, Object> param){
    	String sqlNameFlag = (isDefault)?"D":"";
    	Object result = super.insert("rbs.modules.menu.menuMapper.copy" + sqlNameFlag + "Insert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }
    
    /**
     * 적용 테이블 copy
     * @param param
     * @return
     */
    public int copyAInsert(boolean isDefault, Map<String, Object> param){
    	String sqlNameFlag = (isDefault)?"D":"";
    	Object result = super.insert("rbs.modules.menu.menuMapper.copy" + sqlNameFlag + "AInsert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }
    
    /**
     * 적용
     * @param param
     * @return
     */
    public int apply(Map<String, Object> param){
    	Object result = super.insert("rbs.modules.menu.menuMapper.apply", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제
     * @param param 완전삭제정보
     */
    public int applyCdelete(Map<String, Object> param){
    	return super.delete("rbs.modules.menu.menuMapper.applyCdelete", param);
    }
    


    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getTotalCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.menu.menuMapper.selectCount", param);
    }

    public int getMaxOrdIdx(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.menu.menuMapper.selectMaxOrdIdx", param);
    }

    public int getOrdIdx(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.menu.menuMapper.selectOrdIdx", param);
    }


    public int getNextOrdIdx(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.menu.menuMapper.selectNextOrdIdx", param);
    }
    
    /**
     * 이동할 menu_idx
     * @param param
     * @return
     */
	public List<Object> getSourceList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.menu.menuMapper.selectSourceList", param);
    }

    public int getInChildCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.menu.menuMapper.selectInChild", param);
    }

    public int getPrtMenuIdx(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.menu.menuMapper.selectPrtMenuIdx", param);
    }

	public DataMap getMenuKeyView(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.menu.menuMapper.selectMenuKeyView", param);
	}
    

    /**
     * 화면에 조회된일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int updateOrdIdx(Map<String, Object> param){
        return super.update("rbs.modules.menu.menuMapper.updateOrdIdx",param);
    }


    /**
     * 게시물 key 조회한다.
     * @return int key
     */
    public int getNextId(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.menu.menuMapper.nextId", param);
    }
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getModify(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.menu.menuMapper.selectModify", param);
	}
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public EgovMap getJSONModify(Map<String, Object> param) {
         return (EgovMap)selectOne("rbs.modules.menu.menuMapper.selectJSONModify", param);
	}
	
	public List<Object> getApplyMenuList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.menu.menuMapper.applyMenuList", param);
    }
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public EgovMap getJSONApplyView(Map<String, Object> param) {
         return (EgovMap)selectOne("rbs.modules.menu.menuMapper.selectJSONApplyView", getLangParam(param));
	}
	
    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insert(Map<String, Object> param){
    	Object result = super.insert("rbs.modules.menu.menuMapper.insert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }

    /**
     * 화면에 조회된일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int update(Map<String, Object> param){
        return super.update("rbs.modules.menu.menuMapper.update",param);
    }

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 삭제목록정보
     */
	public List<Object> getDeleteList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.menu.menuMapper.deleteList", param);
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getDeleteCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.menu.menuMapper.deleteCount", param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
     * @param param 삭제정보
     */
    public int delete(Map<String, Object> param){
    	return super.update("rbs.modules.menu.menuMapper.delete",param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
     * @param param 복원정보
     */
    public int restore(Map<String, Object> param){
    	return super.update("rbs.modules.menu.menuMapper.restore",param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제
     * @param param 완전삭제정보
     */
    public int cdelete(Map<String, Object> param){
    	return super.delete("rbs.modules.menu.menuMapper.cdelete", param);
    }
    
    /**
     * 순서 update
     * @param param
     * @return
     */
    public int updateTotOrdIdx(Map<String, Object> param) {
    	return super.update("rbs.modules.menu.menuMapper.updateTotOrdIdx", param);
    }
	
}