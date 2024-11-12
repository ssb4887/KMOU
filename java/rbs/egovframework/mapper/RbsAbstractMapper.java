package rbs.egovframework.mapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.executor.result.DefaultResultContext;
import org.springframework.beans.factory.annotation.Value;

import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.StringUtil;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

/**
 * 언어사용여부에 따른 쿼리
 * @author user
 *
 */
public abstract class RbsAbstractMapper extends EgovAbstractMapper{

	@Value("${Globals.locale.lang.use}")
	protected int useLang;						// 언어사용여부
	
	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	public Map<String, Object> getLangParam(Map<String, Object> param){
		return getLangParam(null, param);
	}
	
	public Map<String, Object> getLangParam(String localeLang, Map<String, Object> param){
		if(useLang != 1) return param;
		if(param == null) param = new HashMap<String, Object>();
		if(StringUtil.isEmpty(localeLang)) localeLang = rbsMessageSource.getLocaleLang();
		param.put("localeLang", localeLang);
		param.put("LOCALE_LANG", localeLang.toUpperCase());
		return param;
	}
	
	public String getLangFlag() {
		return (useLang == 1)? ".lang":"";
	}
	
	public <K, V> Map<K, V> selectMapList(List list, String mapKey) {
		return selectMapList(list, mapKey, 0);
	}
	
	public <K, V> Map<K, V> selectMapList(List list, String mapKey, int type) {
		RbsMapListResultHandler mapResultHandler = new RbsMapListResultHandler(
				mapKey, getSqlSession().getConfiguration().getObjectFactory(),
				getSqlSession().getConfiguration().getObjectWrapperFactory());

		DefaultResultContext context = new DefaultResultContext();
		for (Iterator i$ = list.iterator(); i$.hasNext();) {
			Object o = i$.next();
			context.nextResultObject(o);
			mapResultHandler.handleResult(context, type);
		}
		Map selectedMap = mapResultHandler.getMappedResults();
		return selectedMap;
	}
}