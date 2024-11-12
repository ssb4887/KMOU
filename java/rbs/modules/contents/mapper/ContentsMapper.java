package rbs.modules.contents.mapper;

import java.util.List;
import java.util.Map;



import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.RbsAbstractMapper;

import com.woowonsoft.egovframework.form.DataMap;


@Repository("contentsMapper")
public class ContentsMapper extends RbsAbstractMapper{
	
	public List<Object> getOptnJSONList(String lang, Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.contents" + getLangFlag() + ".contentsMapper.optionJSONList", getLangParam(lang, param));
    }

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 기관 목록정보
     */
	public List<Object> getList(String lang, Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.contents" + getLangFlag() + ".contentsMapper.selectList", getLangParam(lang, param));
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getTotalCount(String lang, Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.contents" + getLangFlag() + ".contentsMapper.selectCount", getLangParam(lang, param));
    }
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getView(String lang, Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.contents" + getLangFlag() + ".contentsMapper.selectView", getLangParam(lang, param));
	}
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getModify(String lang, Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.contents" + getLangFlag() + ".contentsMapper.selectModify", getLangParam(lang, param));
	}

    /**
     * CONTENTS_TYPE 조회한다.
     * @return String contentsType
     */
    public String getContentsType(Map<String, Object> param) {
        return (String)selectOne("rbs.modules.contents.contentsMapper.contentsType", param);
    }

    /**
     * 게시물 key 조회한다.
     * @return int key
     */
    public String getNextId(Map<String, Object> param) {
        return (String)selectOne("rbs.modules.contents.contentsMapper.nextId", param);
    }

    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insert(Map<String, Object> param){
    	Object result = super.insert("rbs.modules.contents.contentsMapper.insert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }

    /**
     * 화면에 조회된일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int update(Map<String, Object> param){
        return super.update("rbs.modules.contents.contentsMapper.update",param);
    }

    /**
     * 화면에 조회된일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int updateLang(String lang, Map<String, Object> param){
        // 언어 테이블 수정
        return super.update("rbs.modules.contents" + getLangFlag() + ".contentsMapper.update",getLangParam(lang, param));
    }

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 삭제목록정보
     */
	public List<Object> getDeleteList(String lang, Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.contents" + getLangFlag() + ".contentsMapper.deleteList", getLangParam(lang, param));
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getDeleteCount(String lang, Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.contents" + getLangFlag() + ".contentsMapper.deleteCount", getLangParam(lang, param));
    }


    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
     * @param param 삭제정보
     */
    public int delete(Map<String, Object> param){
    	return super.update("rbs.modules.contents.contentsMapper.delete",param);
    }
    

    public int deleteLang(Map<String, Object> param){
    	return super.update("rbs.modules.contents" + getLangFlag() + ".contentsMapper.delete",param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
     * @param param 복원정보
     */
    public int restore(Map<String, Object> param){
    	return super.update("rbs.modules.contents.contentsMapper.restore",param);
    }
    
    public int restoreLang(Map<String, Object> param){
    	return super.update("rbs.modules.contents" + getLangFlag() + ".contentsMapper.restore",param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제
     * @param param 완전삭제정보
     */
    public int cdelete(Map<String, Object> param){
    	return super.delete("rbs.modules.contents.contentsMapper.cdelete", param);
    }
}