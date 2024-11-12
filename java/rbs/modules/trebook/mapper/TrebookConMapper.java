package rbs.modules.trebook.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.woowonsoft.egovframework.form.DataMap;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

/**
 * 이북 내용에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("trebookConMapper")
public class TrebookConMapper extends EgovAbstractMapper{

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.trebook.trebookConMapper.selectList", param);
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getTotalCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.trebook.trebookConMapper.selectCount", param);
    }
    
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getFileView(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.trebook.trebookConMapper.selectFileView", param);
	}
	
	/**
     * 다운로드 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int updateDownload(Map<String, Object> param){
        return super.update("rbs.modules.trebook.trebookConMapper.updateDownload",param);
    }

    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insert(Map<String, Object> param){
    	Object result = super.insert("rbs.modules.trebook.trebookConMapper.insert", param);
    	if(result != null) {
    		return ((Integer)result).intValue();
    	} else return 0;
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제 
     * @param param 삭제정보
     */
    public int delete(Map<String, Object> param){
    	return super.update("rbs.modules.trebook.trebookConMapper.delete",param);
    }
}