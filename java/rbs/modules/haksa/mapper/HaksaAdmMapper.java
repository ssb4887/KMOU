package rbs.modules.haksa.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.woowonsoft.egovframework.form.DataMap;

import rbs.egovframework.mapper.MartAbstractMapper;

/**
 * 샘플모듈에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("haksaAdmMapper")
public class HaksaAdmMapper extends MartAbstractMapper{

	public int getTotalCount(Map<String, Object> param) {
		return selectOne("mart.modules.haksa.haksaAdmMapper.getHaksaListCount", param);
	}

	public List<?> getList(Map<String, Object> param) {
		return selectList("mart.modules.haksa.haksaAdmMapper.getHaksaList", param);
	}

	public DataMap getModify(Map<String, Object> param) {
		return selectOne("mart.modules.haksa.haksaAdmMapper.getHaksaModify", param);
	}

	public int update(Map<String, Object> param) {
		return super.update("mart.modules.haksa.haksaAdmMapper.updateHaksa",param);
	}
}