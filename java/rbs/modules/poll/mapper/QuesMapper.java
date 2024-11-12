package rbs.modules.poll.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.RbsAbstractMapper;

import com.woowonsoft.egovframework.form.DataMap;

/**
 * 설문관리에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("pollQuesMapper")
public class QuesMapper extends RbsAbstractMapper{

	/**
     * 등록된 문항정보를 데이터베이스에서 읽어와 화면에 출력
	 * @param param
	 * @return
	 */
	public List<Object> getQuesList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.poll.quesMapper.optnList", param);
    }
	
	/**
     * 등록된 내부문항정보를 데이터베이스에서 읽어와 화면에 출력
	 * @param param
	 * @return
	 */
    public Map<String, List<Object>> getInquesList(Map<String, Object> param) {
        List<Object> list = (List<Object>)selectList("rbs.modules.poll.quesMapper.inquesList", param);
    	return selectMapList(list, "PAR_QUES_CLASS_IDX");
    }
    
    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.poll.quesMapper.selectList", param);
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getTotalCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.poll.quesMapper.selectCount", param);
    }
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getFileView(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.poll.quesMapper.selectFileView", param);
	}
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getView(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.poll.quesMapper.selectView", param);
	}
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getModify(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.poll.quesMapper.selectModify", param);
	}

    /**
     * 게시물 key 조회한다.
     * @return int key
     */
    public int getNextId(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.poll.quesMapper.nextId", param);
    }

    public int getMaxOrdIdx(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.poll.quesMapper.selectMaxOrdIdx", param);
    }

    public int getOrdIdx(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.poll.quesMapper.selectOrdIdx", param);
    }

    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insert(Map<String, Object> param){
    	Object result = super.insert("rbs.modules.poll.quesMapper.insert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }

    /**
     * 화면에 조회된일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int update(Map<String, Object> param){
        return super.update("rbs.modules.poll.quesMapper.update",param);
    }

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 삭제목록정보
     */
	public List<Object> getDeleteList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.poll.quesMapper.deleteList", param);
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getDeleteCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.poll.quesMapper.deleteCount", param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
     * @param param 삭제정보
     */
    public int delete(Map<String, Object> param){
    	return super.update("rbs.modules.poll.quesMapper.delete",param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
     * @param param 복원정보
     */
    public int restore(Map<String, Object> param){
    	return super.update("rbs.modules.poll.quesMapper.restore",param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제
     * @param param 완전삭제정보
     */
    public int cdelete(Map<String, Object> param){
    	return super.delete("rbs.modules.poll.quesMapper.cdelete", param);
    }
    
    /**
     * 순서 update
     * @param param
     * @return
     */
    public int updateTotOrdIdx(Map<String, Object> param) {
    	return super.update("rbs.modules.poll.quesMapper.updateTotOrdIdx", param);
    }
	
	/**
     * 등록된 내부문항정보를 데이터베이스에서 읽어와 화면에 출력
	 * @param param
	 * @return
	 */
    public Map<String, List<Object>> getInclassMap(Map<String, Object> param) {
        List<Object> list = (List<Object>)selectList("rbs.modules.poll.quesMapper.inclassList", param);
    	return selectMapList(list, "QUESTION_IDX");
    }
	
	/**
     * 등록된 문항 내부분류정보를 데이터베이스에서 읽어와 화면에 출력
	 * @param param
	 * @return
	 */
	public List<Object> getInclassList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.poll.quesMapper.inclassList", param);
    }
    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int inclassInsert(Map<String, Object> param){
    	Object result = super.insert("rbs.modules.poll.quesMapper.inclassInsert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제
     * @param param 완전삭제정보
     */
    public int inclassCdelete(Map<String, Object> param){
    	return super.delete("rbs.modules.poll.quesMapper.inclassCdelete", param);
    }
}