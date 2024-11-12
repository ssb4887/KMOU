package rbs.modules.contents.mapper;

import java.util.List;
import java.util.Map;



import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.RbsAbstractMapper;

import com.woowonsoft.egovframework.form.DataMap;

/**
 * 일반회원관리에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("contWorkMapper")
public class ContWorkMapper extends RbsAbstractMapper{
	

	public DataMap getApplyView(String contentsType, String lang, Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.contents" + getLangFlag() + ".contWork" + contentsType + "Mapper.selectApplyView", getLangParam(lang, param));
	}
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getModify(String contentsType, String lang, Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.contents" + getLangFlag() + ".contWork" + contentsType + "Mapper.selectModify", getLangParam(lang, param));
	}

    public int copyInsert(String lang, String contentsType, Map<String, Object> param){
    	Object result = super.insert("rbs.modules.contents.contWork" + contentsType + "Mapper.copyInsert", getLangParam(lang, param));
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }

    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int allInsert(String contentsType, Map<String, Object> param){
    	Object result = super.insert("rbs.modules.contents.contWork" + contentsType + "Mapper.allInsert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }

    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insert(String contentsType, Map<String, Object> param){
    	Object result = super.insert("rbs.modules.contents.contWork" + contentsType + "Mapper.insert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }

    /**
     * 화면에 조회된일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int update(String contentsType, String lang, Map<String, Object> param){
        return super.update("rbs.modules.contents.contWork" + contentsType + "Mapper.update", getLangParam(lang, param));
    }

    /**
     * 화면에 조회된일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int apply(String contentsType, String lang, Map<String, Object> param){
        return super.update("rbs.modules.contents.contWork" + contentsType + "Mapper.apply", getLangParam(lang, param));
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
     * @param param 삭제정보
     */
    public int applyCdelete(String contentsType, String lang, Map<String, Object> param){
    	return super.update("rbs.modules.contents.contWork" + contentsType + "Mapper.applyCdelete",getLangParam(lang, param));
    }
}