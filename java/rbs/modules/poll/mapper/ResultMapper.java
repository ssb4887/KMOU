package rbs.modules.poll.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.RbsAbstractMapper;

import com.woowonsoft.egovframework.form.DataMap;

/**
 * 설문관리에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("pollResultMapper")
public class ResultMapper extends RbsAbstractMapper{

	/**
     * 등록된 참여결과정보를 데이터베이스에서 읽어와 화면에 출력
	 * @param param
	 * @return
	 */
    public Map<Object, Object> getResultMap(String tmpFlag, Map<String, Object> param) {
    	if(tmpFlag == null) tmpFlag = "";
        return (Map<Object, Object>)selectMap("rbs.modules.poll." + tmpFlag + "resultMapper.resultList", param, "QUESTION_ITEM_IDX");
    }
    public Map<String, List<Object>> getResultMap(Map<String, Object> param) {
    	if(param == null) param = new HashMap<String, Object>();
    	param.put("isResp", true);
        List<Object> list = (List<Object>)selectList("rbs.modules.poll.resultMapper.resultList", param);
    	return selectMapList(list, "RESP_QUESTION_IDX");
    }
    
    /**
     * 유형 결과
     * @param param
     * @return
     */
	public List<Object> getQuestypeResultList(boolean isPollItem, Map<String, Object> param){
    	String pollItemFlag = "";
    	if(isPollItem) pollItemFlag = "P";
        return (List<Object>)selectList("rbs.modules.poll.resultMapper.questype" + pollItemFlag + "ResultList", param);
    }
    
    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.poll.resultMapper.selectList", param);
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getTotalCount(String tmpFlag, Map<String, Object> param) {
    	if(tmpFlag == null) tmpFlag = "";
        return (Integer)selectOne("rbs.modules.poll." + tmpFlag + "resultMapper.selectCount", param);
    }
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getView(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.poll.resultMapper.selectView", param);
	}
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getModify(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.poll.resultMapper.selectModify", param);
	}

    /**
     * 게시물 key 조회한다.
     * @return int key
     */
    public int getNextId(String tmpFlag, Map<String, Object> param) {
    	if(tmpFlag == null) tmpFlag = "";
        return (Integer)selectOne("rbs.modules.poll." + tmpFlag + "resultMapper.nextId", param);
    }

    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insert(String tmpFlag, Map<String, Object> param){
    	if(tmpFlag == null) tmpFlag = "";
    	Object result = super.insert("rbs.modules.poll." + tmpFlag + "resultMapper.insert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }

    /**
     * 화면에 조회된일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int update(String tmpFlag, Map<String, Object> param){
    	if(tmpFlag == null) tmpFlag = "";
        return super.update("rbs.modules.poll." + tmpFlag + "resultMapper.update",param);
    }

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 삭제목록정보
     */
	public List<Object> getDeleteList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.poll.resultMapper.deleteList", param);
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getDeleteCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.poll.resultMapper.deleteCount", param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제
     * @param param 완전삭제정보
     */
    public int cdelete(String tmpFlag, Map<String, Object> param){
    	if(tmpFlag == null) tmpFlag = "";
    	return super.delete("rbs.modules.poll." + tmpFlag + "resultMapper.cdelete", param);
    }
}