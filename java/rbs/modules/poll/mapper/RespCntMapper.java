package rbs.modules.poll.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.woowonsoft.egovframework.form.DataMap;

import rbs.egovframework.mapper.RbsAbstractMapper;

/**
 * 설문관리에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("respCntMapper")
public class RespCntMapper extends RbsAbstractMapper{

	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getPollView(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.poll.respCntMapper.pollView", param);
	}
	
    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getPollList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.poll.respCntMapper.pollList", param);
    }
    
    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getQuesList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.poll.respCntMapper.quesList", param);
    }

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
    public Map<String, List<Object>> getItemMap(Map<String, Object> param) {
    	return getItemMap(false, param);
    }

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
    public Map<String, List<Object>> getItemMap(boolean isPollItem, Map<String, Object> param) {
    	String pollItemFlag = "";
    	if(isPollItem) pollItemFlag = "p";
        List<Object> list = (List<Object>)selectList("rbs.modules.poll.respCntMapper." + pollItemFlag + "itemList", param);
    	return selectMapList(list, "QUESTION_IDX");
    }
}