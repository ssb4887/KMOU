package rbs.modules.member.mapper;

import java.util.List;
import java.util.Map;




import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.RbsAbstractMapper;

import com.woowonsoft.egovframework.form.DataMap;

/**
 * 일반회원관리에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("memberDprtMapper")
public class MemberDprtMapper extends RbsAbstractMapper{

	public List<Object> getOptnList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.member" + getLangFlag() + ".memberDprtMapper.optnList", getLangParam(param));
    }
	
    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 기관 목록정보
     */
	public List<Object> getSearchList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.member" + getLangFlag() + ".memberDprtMapper.searchList", getLangParam(param));
    }
	
    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 기관 목록정보
     */
	public List<Object> getList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.member" + getLangFlag() + ".memberDprtMapper.selectList", getLangParam(param));
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getTotalCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.member" + getLangFlag() + ".memberDprtMapper.selectCount", getLangParam(param));
    }

    public int getMaxOrdIdx(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.member.memberDprtMapper.selectMaxOrdIdx", param);
    }

    public int getOrdIdx(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.member.memberDprtMapper.selectOrdIdx", param);
    }


    public int getNextOrdIdx(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.member.memberDprtMapper.selectNextOrdIdx", param);
    }
    
    /**
     * 이동할 depart_idx
     * @param param
     * @return
     */
	public List<Object> getSourceList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.member.memberDprtMapper.selectSourceList", param);
    }

    public int getInChildCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.member.memberDprtMapper.selectInChild", param);
    }

    public int getPrtDepartIdx(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.member.memberDprtMapper.selectPrtDepartIdx", param);
    }

	public DataMap getDepartKeyView(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.member.memberDprtMapper.selecttDepartKeyView", param);
	}
    

    /**
     * 화면에 조회된일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int updateOrdIdx(Map<String, Object> param){
        return super.update("rbs.modules.member.memberDprtMapper.updateOrdIdx",param);
    }


    /**
     * 게시물 key 조회한다.
     * @return int key
     */
    public int getNextId(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.member.memberDprtMapper.nextId", param);
    }
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getModify(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.member" + getLangFlag() + ".memberDprtMapper.selectModify", getLangParam(param));
	}

    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insert(Map<String, Object> param){
    	Object result = super.insert("rbs.modules.member.memberDprtMapper.insert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }
    public int insertLang(Map<String, Object> param){
    	Object result = super.insert("rbs.modules.member" + getLangFlag() + ".memberDprtMapper.insert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }

    /**
     * 화면에 조회된일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int update(Map<String, Object> param){
        return super.update("rbs.modules.member.memberDprtMapper.update",param);
    }
    
    public int updateLang(Map<String, Object> param){
        return super.update("rbs.modules.member" + getLangFlag() + ".memberDprtMapper.update",getLangParam(param));
    }

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 삭제목록정보
     */
	public List<Object> getDeleteList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.member" + getLangFlag() + ".memberDprtMapper.deleteList", getLangParam(param));
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getDeleteCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.member" + getLangFlag() + ".memberDprtMapper.deleteCount", getLangParam(param));
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
     * @param param 삭제정보
     */
    public int delete(Map<String, Object> param){
    	return super.update("rbs.modules.member.memberDprtMapper.delete",param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
     * @param param 삭제정보
     */
    /*
    public int deleteLang(Map<String, Object> param){
    	return super.update("rbs.modules.member" + getLangFlag() + ".memberDprtMapper.delete",param);
    }*/

    /**
     * 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
     * @param param 복원정보
     */
    public int restore(Map<String, Object> param){
    	return super.update("rbs.modules.member.memberDprtMapper.restore",param);
    }
    
    /**
     * 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
     * @param param 복원정보
     */
    /*
    public int restoreLang(Map<String, Object> param){
    	return super.update("rbs.modules.member" + getLangFlag() + ".memberDprtMapper.restore",param);
    }*/

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제
     * @param param 완전삭제정보
     */
    public int cdelete(Map<String, Object> param){
    	return super.delete("rbs.modules.member.memberDprtMapper.cdelete", param);
    }
    
    /**
     * 순서 update
     * @param param
     * @return
     */
    public int updateTotOrdIdx(Map<String, Object> param) {
    	return super.update("rbs.modules.member.memberDprtMapper.updateTotOrdIdx", param);
    }
}