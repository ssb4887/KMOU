package rbs.modules.adminAuth.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.RbsAbstractMapper;

import com.woowonsoft.egovframework.form.DataMap;

import com.woowonsoft.egovframework.form.EgovMap;

@Repository("adminAuthMapper")
public class AdminAuthMapper extends RbsAbstractMapper{

	public List<Object> getOptnList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.adminAuth.adminAuthMapper.optnList", param);
    }

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.adminAuth.adminAuthMapper.selectList", param);
    }

    /**
     * 게시물 key 조회한다.
     * @return int key
     */
    public int getMaxMenuLevel(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.adminAuth.adminAuthMapper.maxMenuLevel", param);
    }
    
    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getTotalCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.adminAuth.adminAuthMapper.selectCount", param);
    }
    
    /**
     * 이동할 menu_idx
     * @param param
     * @return
     */
	public List<Object> getSourceList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.adminAuth.adminAuthMapper.selectSourceList", param);
    }

    public int getInChildCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.adminAuth.adminAuthMapper.selectInChild", param);
    }

    public int getPrtMenuIdx(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.adminAuth.adminAuthMapper.selectPrtMenuIdx", param);
    }

	public DataMap getMenuKeyView(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.adminAuth.adminAuthMapper.selectMenuKeyView", param);
	}
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getModify(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.adminAuth.adminAuthMapper.selectModify", param);
	}
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public EgovMap getJSONModify(Map<String, Object> param) {
         return (EgovMap)selectOne("rbs.modules.adminAuth.adminAuthMapper.selectJSONModify", param);
	}
	
	public List<Object> getApplyMenuList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.adminAuth.adminAuthMapper.applyMenuList", param);
    }
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public EgovMap getJSONApplyView(Map<String, Object> param) {
         return (EgovMap)selectOne("rbs.modules.adminAuth.adminAuthMapper.selectJSONApplyView", getLangParam(param));
	}
	
    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insert(Map<String, Object> param){
    	Object result = super.insert("rbs.modules.adminAuth.adminAuthMapper.insert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }

    /**
     * 화면에 조회된일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int update(Map<String, Object> param){
        return super.update("rbs.modules.adminAuth.adminAuthMapper.update",param);
    }
	
}