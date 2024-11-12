package rbs.modules.code.mapper;

import java.util.List;
import java.util.Map;


import org.springframework.stereotype.Repository;
import rbs.egovframework.mapper.RbsAbstractMapper;
import com.woowonsoft.egovframework.form.DataMap;


@Repository("codeMstrMapper")
public class CodeMstrMapper extends RbsAbstractMapper{

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 기관 목록정보
     */
	public List<Object> getList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.code" + getLangFlag() + ".masterMapper.selectList", getLangParam(param));
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getTotalCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.code" + getLangFlag() + ".masterMapper.selectCount", getLangParam(param));
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getDuplicateCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.code.masterMapper.selectDuplicateCount", getLangParam(param));
    }

    public int getMaxOrdIdx(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.code.masterMapper.selectMaxOrdIdx", param);
    }
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	@SuppressWarnings("unchecked")
	public DataMap getView(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.code" + getLangFlag() + ".masterMapper.selectView", getLangParam(param));
	}
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	@SuppressWarnings("unchecked")
	public DataMap getModify(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.code" + getLangFlag() + ".masterMapper.selectModify", getLangParam(param));
	}

    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insert(Map<String, Object> param){
    	Object result = super.insert("rbs.modules.code.masterMapper.insert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }

    /**
     * 화면에 조회된일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int update(Map<String, Object> param){
        return super.update("rbs.modules.code.masterMapper.update",param);
    }

    /**
     * 화면에 조회된일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int updateLang(Map<String, Object> param){
        // 언어 테이블 수정
        return super.update("rbs.modules.code" + getLangFlag() + ".masterMapper.update",getLangParam(param));
    }

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 삭제목록정보
     */
	public List<Object> getDeleteList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.code" + getLangFlag() + ".masterMapper.deleteList", getLangParam(param));
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getDeleteCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.code" + getLangFlag() + ".masterMapper.deleteCount", getLangParam(param));
    }


    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
     * @param param 삭제정보
     */
    public int delete(Map<String, Object> param){
    	return super.update("rbs.modules.code.masterMapper.delete",param);
    }
    
    /*
    public int deleteLang(Map<String, Object> param){
    	return super.update("rbs.modules.code" + getLangFlag() + ".masterMapper.delete",param);
    }*/

    /**
     * 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
     * @param param 복원정보
     */
    public int restore(Map<String, Object> param){
    	return super.update("rbs.modules.code.masterMapper.restore",param);
    }
    /*
    public int restoreLang(Map<String, Object> param){
    	return super.update("rbs.modules.code" + getLangFlag() + ".masterMapper.restore",param);
    }*/

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제
     * @param param 완전삭제정보
     */
    public int cdelete(Map<String, Object> param){
    	return super.delete("rbs.modules.code.masterMapper.cdelete", param);
    }
}