package rbs.modules.dashboard.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.StringUtil;

import rbs.egovframework.mapper.RbsAbstractMapper;

/**
 * dashboard에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("dashboardMapper")
public class DashboardMapper extends RbsAbstractMapper{

	public List<Object> getBoardList(String designType, Map<String, Object> param){

		String memoDesignType = RbsProperties.getProperty("Globals.design.NAME_DESIGN_TYPE_MEMO");
    	String designTypeFlag = (!StringUtil.isEquals(designType, memoDesignType))?"":designType;
        return (List<Object>)selectList("rbs.modules.dashboard.dashboardMapper."+ designTypeFlag +"boardList", param);
    }
	
	public DataMap getContentsView(String lang, Map<String, Object> param){
        return (DataMap)selectOne("rbs.modules.dashboard.dashboardMapper.contentsView", getLangParam(lang, param));
    }
	
	public List<Object> getModuleList(String moduleId, Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.dashboard.dashboardMapper." + moduleId + "List", param);
    }
}