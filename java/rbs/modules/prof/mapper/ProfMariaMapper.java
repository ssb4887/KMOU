package rbs.modules.prof.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.woowonsoft.egovframework.form.DataMap;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;
import rbs.egovframework.mapper.MartAbstractMapper;

/**
 * 샘플 모듈에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("profMariaMapper")
public class ProfMariaMapper extends EgovAbstractMapper {
	
	
	/**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param subjectCd 검색조건
     * @return 상세정보
     */
	public DataMap getProfSn(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.prof.profMariaMapper.selectProfSn", param);
	}
	
	
}