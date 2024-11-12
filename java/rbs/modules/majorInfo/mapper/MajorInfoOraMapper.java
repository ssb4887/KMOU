package rbs.modules.majorInfo.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;
import rbs.egovframework.mapper.MartAbstractMapper;

/**
 * 샘플모듈에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("majorInfoOraMapper")
public class MajorInfoOraMapper extends MartAbstractMapper{

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	
	public List<Object> getAddMajorList(Map<String, Object> param){
		return (List<Object>)selectList("mart.modules.majorInfo.majorInfoOraMapper.selectAddMajorList", param);
	}
	 
}