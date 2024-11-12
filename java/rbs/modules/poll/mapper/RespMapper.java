package rbs.modules.poll.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.RbsAbstractMapper;

import com.woowonsoft.egovframework.form.DataMap;

/**
 * 설문관리에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("pollRespMapper")
public class RespMapper extends RbsAbstractMapper{
    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.poll.respMapper.selectList", param);
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getTotalCount(String tmpFlag, Map<String, Object> param) {
    	if(tmpFlag == null) tmpFlag = "";
        return (Integer)selectOne("rbs.modules.poll." + tmpFlag + "respMapper.selectCount", param);
    }

    /**
     * key를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getId(String tmpFlag, Map<String, Object> param) {
    	if(tmpFlag == null) tmpFlag = "";
    	Object result = selectOne("rbs.modules.poll." + tmpFlag + "respMapper.selectId", param);
    	if(result != null) return (Integer)result;
    	return 0;
    }
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getPollView(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.poll.respMapper.pollView", param);
	}
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getView(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.poll.respMapper.selectView", param);
	}
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getModify(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.poll.respMapper.selectModify", param);
	}

    /**
     * 게시물 key 조회한다.
     * @return int key
     */
    public int getNextId(String tmpFlag, Map<String, Object> param) {
    	if(tmpFlag == null) tmpFlag = "";
        return (Integer)selectOne("rbs.modules.poll." + tmpFlag + "respMapper.nextId", param);
    }

    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insert(String tmpFlag, Map<String, Object> param){
    	if(tmpFlag == null) tmpFlag = "";
    	Object result = super.insert("rbs.modules.poll." + tmpFlag + "respMapper.insert", param);
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
        return super.update("rbs.modules.poll." + tmpFlag + "respMapper.update",param);
    }

    /**
     * 화면에 조회된일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int resultUpdate(Map<String, Object> param){
        return super.update("rbs.modules.poll.respMapper.resultUpdate",param);
    }

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 삭제목록정보
     */
	public List<Object> getDeleteList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.poll.respMapper.deleteList", param);
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getDeleteCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.poll.respMapper.deleteCount", param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제
     * @param param 완전삭제정보
     */
    public int cdelete(String tmpFlag, Map<String, Object> param){
    	if(tmpFlag == null) tmpFlag = "";
        return super.update("rbs.modules.poll." + tmpFlag + "respMapper.cdelete", param);
    }
}