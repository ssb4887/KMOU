package rbs.modules.board.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.StringUtil;

import rbs.egovframework.mapper.MartAbstractMapper;

/**
 * 다기능게시판에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("boardOraMapper")
public class BoardOraMapper extends MartAbstractMapper{

	private boolean isMemoBoard(Map<String, Object> param){
		if(param != null && StringUtil.isEquals(param.get("boardDesignType"), RbsProperties.getProperty("Globals.design.NAME_DESIGN_TYPE_MEMO"))) return true;
		return false;
	}
	/**
	 * 해양대 게시판 BBS_ID
     * 공지사항			10373
     * 학사안내			11786
     * 행사/세미나			10375
     * 초빙/채용			10378
     * 취업정보			10002722
     * 장학정보			10004365
     * 코로나19 안내 자료	10001542
     */
	
    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getList(Map<String, Object> param){
		return (List<Object>)selectList("mart.modules.board.boardOraMapper.selectList", param);
    }
	
	public List<Object> getBnAList(Map<String, Object> param){
		return (List<Object>)selectList("mart.modules.board.boardOraMapper.selectBnAList", param);
    }

	public List<Object> getPreNextList(Map<String, Object> param){
		return (List<Object>)selectList("mart.modules.board.boardOraMapper.selectPreNextList", param);
    }
	
	public List<Object> getPntList(Map<String, Object> param){
		return (List<Object>)selectList("mart.modules.board.boardOraMapper.selectPntList", param);
    }
	/**
     * 메인화면에 표출
     * @param param 검색조건
     * @return List<Object> 목록정보 
     */
	public List<Object> getMainList(Map<String, Object> param){
		return (List<Object>)selectList("mart.modules.board.boardOraMapper.selectMainList", param);
    }
	/**
     * 해양대 공지사항 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getNotiTotalCount(Map<String, Object> param) {
    	return (Integer)selectOne("mart.modules.board.boardOraMapper.selectCount", param);
    }
	
    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getTotalCount(Map<String, Object> param) {
    	if(isMemoBoard(param)) return (Integer)selectOne("rbs.modules.board.boardMemoMapper.selectCount", param);
    	else return (Integer)selectOne("rbs.modules.board.boardMapper.selectCount", param);
    }

    public int getNextCount(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.board.boardMapper.selectNextCount", param);
    }
    
    public int getPreCount(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.board.boardMapper.selectPreCount", param);
    }
    /**
     * 파일항목 목록
     * @param param
     * @return
     */
	public List<Object> getFileList(Map<String, Object> param){
        return (List<Object>)selectList("mart.modules.board.boardOraMapper.selectFileList", param);
    }
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getFileView(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.board.boardMapper.selectFileView", param);
	}
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getView(Map<String, Object> param) {
         return (DataMap)selectOne("mart.modules.board.boardOraMapper.selectView", param);
	}

	/**
     * 조회수 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int updateViews(Map<String, Object> param){
        return super.update("rbs.modules.board.boardMapper.updateViews",param);
    }

	/**
     * 다운로드수 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int updateFileDown(Map<String, Object> param){
        return super.update("rbs.modules.board.boardMapper.updateFileDown",param);
    }
    
    /**
     * 권한여부 조회
     * @param param
     * @return
     */
    public int getAuthCount(Map<String, Object> param) {
    	if(isMemoBoard(param)) return (Integer)selectOne("rbs.modules.board.boardMemoMapper.authCount", param);
    	else return (Integer)selectOne("rbs.modules.board.boardMapper.authCount", param);
    }
	
    /**
     * 비밀번호 조회
     * @param param
     * @return
     */
    public int getPwdCnt(Map<String, Object> param) {
    	if(isMemoBoard(param)) return (Integer)selectOne("rbs.modules.board.boardMemoMapper.selectPwdCnt", param);
    	return (Integer)selectOne("rbs.modules.board.boardMapper.selectPwdCnt", param);
    }
    
    public int getReLevel(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.board.boardMapper.selectReLevel", param);
    }
    
    /**
     * 비밀번호 조회 항목 : PWD, RE_LEVEL
     * @param param
     * @return
     */
    public DataMap getPwdView(Map<String, Object> param) {
    	if(isMemoBoard(param)) return (DataMap)selectOne("rbs.modules.board.boardMemoMapper.selectPwdView", param);
    	return (DataMap)selectOne("rbs.modules.board.boardMapper.selectPwdView", param);
    }
    
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getModify(Map<String, Object> param) {
		if(isMemoBoard(param)) return (DataMap)selectOne("rbs.modules.board.boardMemoMapper.selectModify", param);
		else return (DataMap)selectOne("rbs.modules.board.boardMapper.selectModify", param);
	}

    /**
     * 게시물 key 조회한다.
     * @return int key
     */
    public int getNextId(Map<String, Object> param) {
    	if(isMemoBoard(param)) return (Integer)selectOne("rbs.modules.board.boardMemoMapper.nextId", param); 
    	else return (Integer)selectOne("rbs.modules.board.boardMapper.nextId", param);
    }

    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insert(Map<String, Object> param){
    	Object result = null;
    	if(isMemoBoard(param)) result = super.insert("rbs.modules.board.boardMemoMapper.insert", param);
    	else result = super.insert("rbs.modules.board.boardMapper.insert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }

    /**
     * 화면에 조회된일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int update(Map<String, Object> param){
    	if(isMemoBoard(param)) return super.update("rbs.modules.board.boardMemoMapper.update",param);
    	else return super.update("rbs.modules.board.boardMapper.update",param);
    }

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 삭제목록정보
     */
	public List<Object> getDeleteList(Map<String, Object> param){
		if(isMemoBoard(param)) return (List<Object>)selectList("rbs.modules.board.boardMemoMapper.deleteList", param);
		else return (List<Object>)selectList("rbs.modules.board.boardMapper.deleteList", param);
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getDeleteCount(Map<String, Object> param) {
    	if(isMemoBoard(param)) return (Integer)selectOne("rbs.modules.board.boardMemoMapper.deleteCount", param);
        return (Integer)selectOne("rbs.modules.board.boardMapper.deleteCount", param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
     * @param param 삭제정보
     */
    public int delete(Map<String, Object> param){
    	if(isMemoBoard(param)) return super.update("rbs.modules.board.boardMemoMapper.delete",param);
    	else return super.update("rbs.modules.board.boardMapper.delete",param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
     * @param param 복원정보
     */
    public int restore(Map<String, Object> param){
    	if(isMemoBoard(param)) return super.update("rbs.modules.board.boardMemoMapper.restore",param);
    	else return super.update("rbs.modules.board.boardMapper.restore",param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제
     * @param param 완전삭제정보
     */
    public int cdelete(Map<String, Object> param){
    	if(isMemoBoard(param)) return super.delete("rbs.modules.board.boardMemoMapper.cdelete", param);
    	else return super.delete("rbs.modules.board.boardMapper.cdelete", param);
    }

    /**
     * 파일다운로드 사유 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 삭제목록정보
     */
	public List<Object> getFileCmtList(Map<String, Object> param){
		return (List<Object>)selectList("rbs.modules.board.boardMapper.fileCmtList", param);
    }

    /**
     * 파일다운로드 사유 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getFileCmtCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.board.boardMapper.fileCmtCount", param);
    }

    /**
     * 파일다운로드 사유 key 조회한다.
     * @return int key
     */
    public int getFileCmtNextId(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.board.boardMapper.fileCmtNextId", param); 
    }

    /**
     * 파일다운로드 사유 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return int 등록결과
     */
    public int fileCmtInsert(Map<String, Object> param){
    	Object result = super.insert("rbs.modules.board.boardMapper.fileCmtInsert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }
}