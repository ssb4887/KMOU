package rbs.modules.member.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.RbsAbstractMapper;

import com.woowonsoft.egovframework.form.DataMap;

/**
 * 일반회원관리에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("memberAnLogMapper")
public class MemberAnLogMapper extends RbsAbstractMapper{

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getTotalCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.member.memberAnLogMapper.selectCount", param);
    }
    
    /**
     * 게시물 key 조회한다.
     * @return int key
     */
    public long getNextIdx(Map<String, Object> param) {
        return (Long)selectOne("rbs.modules.member.memberAnLogMapper.nextIdx", param);
    }

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.member.memberAnLogMapper.selectList", getLangParam(param));
    }
	
	/**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	@SuppressWarnings("unchecked")
	public DataMap getView(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.member.memberAnLogMapper.selectView", param);
	}

    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insert(Map<String, Object> param){
    	Object result = super.insert("rbs.modules.member.memberAnLogMapper.insert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }
}