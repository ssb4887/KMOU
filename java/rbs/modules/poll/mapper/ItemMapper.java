package rbs.modules.poll.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.RbsAbstractMapper;

import com.woowonsoft.egovframework.form.DataMap;

/**
 * 설문관리에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("pollItemMapper")
public class ItemMapper extends RbsAbstractMapper{
	
	/**
     * 등록된 항목정보를 데이터베이스에서 읽어와 화면에 출력
	 * @param param
	 * @return
	 */
    public Map<String, List<Object>> getItemMap(Map<String, Object> param) {
        List<Object> list = (List<Object>)selectList("rbs.modules.poll.itemMapper.itemList", param);
    	return selectMapList(list, "QUESTION_IDX");
    }
    
	public List<Object> getItemJSONList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.poll.itemMapper.itemJSONList", param);
    }

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getList(Map<String, Object> param){
		return getList(false, param);
	}
	public List<Object> getList(boolean isPollItem, Map<String, Object> param){
    	String pollItemFlag = "";
    	if(isPollItem) pollItemFlag = "p";
        return (List<Object>)selectList("rbs.modules.poll." + pollItemFlag + "itemMapper.selectList", param);
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getTotalCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.poll.itemMapper.selectCount", param);
    }
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getItemContView(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.poll.itemMapper.itemContView", param);
	}
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getView(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.poll.itemMapper.selectView", param);
	}
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getModify(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.poll.itemMapper.selectModify", param);
	}

    /**
     * 게시물 key 조회한다.
     * @return int key
     */
    public int getNextId(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.poll.itemMapper.nextId", param);
    }

    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insert(Map<String, Object> param){
    	return insert(false, param);
    }
    public int insert(boolean isPollItem, Map<String, Object> param){
    	String pollItemFlag = "";
    	if(isPollItem) pollItemFlag = "p";
    	Object result = super.insert("rbs.modules.poll." + pollItemFlag + "itemMapper.insert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }

    /**
     * 화면에 조회된일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int update(Map<String, Object> param){
        return super.update("rbs.modules.poll.itemMapper.update",param);
    }

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 삭제목록정보
     */
	public List<Object> getDeleteList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.poll.itemMapper.deleteList", param);
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getDeleteCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.poll.itemMapper.deleteCount", param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
     * @param param 삭제정보
     */
    public int delete(Map<String, Object> param){
    	return super.update("rbs.modules.poll.itemMapper.delete",param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
     * @param param 복원정보
     */
    public int restore(Map<String, Object> param){
    	return super.update("rbs.modules.poll.itemMapper.restore",param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제
     * @param param 완전삭제정보
     */
    public int cdelete(Map<String, Object> param){
    	return cdelete(false, param);
    }
    public int cdelete(boolean isPollItem, Map<String, Object> param){
    	String pollItemFlag = "";
    	if(isPollItem) pollItemFlag = "p";
    	return super.delete("rbs.modules.poll." + pollItemFlag + "itemMapper.cdelete", param);
    }

    /**
     * 삭제할 파일항목 목록
     * @param param 검색조건
     */
	public List<Object> getDeleteFileList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.poll.itemMapper.deleteFileList", param);
    }
}