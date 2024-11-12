package rbs.usr.main.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.woowonsoft.egovframework.form.DataMap;

import rbs.egovframework.mapper.OceanAbstractMapper;

/**
 * 다기능게시판에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("usrMainOceanMapper")
public class MainOceanMapper extends OceanAbstractMapper{
	
	public List<Object> getModuleList(String moduleId, Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.dashboard.dashboardMapper." + moduleId + "List", param);
    }

	public Map<Object, List<Object>> getBoardHashMap(Map<String, Object> param){
		List<Object> list = (List<Object>)selectList("rbs.usr.main.mainMapper.boardHashMap", getLangParam(param));
        return selectMapList(list, "MENU_IDX", 1);
    }

	public Map<Object, List<Object>> getContentsHashMap(Map<String, Object> param){
		List<Object> list = (List<Object>)selectList("rbs.usr.main.mainMapper.contentsHashMap", getLangParam(param));
        return selectMapList(list, "MENU_IDX", 1);
    }
	
	
	/*
	 * OceanCTS 
	 */
	
	public List<Object> getAiNonCourse(List<Map<String, Object>> paramsList) {
		return (List<Object>)selectList("ocean.main.mainOceanMapper.selectAiNonCourse", paramsList);
	}
	
	public List<Object> getCoreCompDiagnosis(Map<String, Object> param){
		return (List<Object>)selectList("ocean.main.mainOceanMapper.selectCoreCompDiagnosis", param);
	}
	
	public List<Object> getMajorCompDiagnosis(Map<String, Object> param){
		return (List<Object>)selectList("ocean.main.mainOceanMapper.selectMajorCompDiagnosis", param);
	}
}