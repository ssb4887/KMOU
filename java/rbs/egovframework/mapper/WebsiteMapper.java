package rbs.egovframework.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.JSONMap;

import rbs.egovframework.WebsiteVO;

@Repository("rbsWebsiteMapper")
public class WebsiteMapper extends RbsAbstractMapper{


	public List<Object> getWebsiteList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.website.websiteMapper.optionList", param);
    }
	
	public WebsiteVO getDefSiteInfo(Map<String, Object> param){
        return (WebsiteVO)selectOne("rbs.modules.website.websiteMapper.defSiteInfo", param);
    }
	
	public DataMap getSiteVerInfo(Map<String, Object> param){
        return (DataMap)selectOne("rbs.modules.menu.menuVerMapper.siteVerInfo", param);
    }
	/**
	 * 적용 사이트, 버전 정보
	 * @param param
	 * @return
	 */
	public JSONMap getApplySiteVerInfo(Map<String, Object> param){
        return (JSONMap)selectOne("rbs.modules.menu.menuVerMapper.applySiteVerInfo", param);
    }
	
	public List<Object> getVersionList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.menu.menuVerMapper.optionList", param);
    }
	
	public List<Object> getMenuList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.menu.menuMapper.menuList", param);
    }
	
	public List<Object> getApplyMenuList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.menu.menuMapper.applyMenuList", param);
    }

	public List<Object> getApplyLogMenuList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.menu.menuMapper.applyLogMenuList", param);
    }
	
	public Map<String, List<Object>> getMenuMultiMapList(Map<String, Object> param){
        List<Object> list = (List<Object>)selectList("rbs.modules.menu.menuMultiMapper.selectMapList", param);
    	return selectMapList(list, "ITEM_ID");
    }
	
	public Map<String, List<Object>> getApplyMenuMultiMapList(Map<String, Object> param){
        List<Object> list = (List<Object>)selectList("rbs.modules.menu.menuMultiMapper.selectApplyMapList", param);
    	return selectMapList(list, "ITEM_ID");
    }
	
	public Map<String, List<Object>> getApplyLogMenuMultiMapList(Map<String, Object> param){
        List<Object> list = (List<Object>)selectList("rbs.modules.menu.menuMultiMapper.selectApplyLogMapList", param);
    	return selectMapList(list, "ITEM_ID");
    }
}