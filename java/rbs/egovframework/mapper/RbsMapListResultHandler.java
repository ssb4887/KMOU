package rbs.egovframework.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import com.woowonsoft.egovframework.util.StringUtil;

public class RbsMapListResultHandler<K, V> implements ResultHandler {
	private final Map<Object, List<Object>> mappedResults;
	private final String mapKey;
	private final ObjectFactory objectFactory;
	private final ObjectWrapperFactory objectWrapperFactory;

	public RbsMapListResultHandler(String mapKey, ObjectFactory objectFactory,
			ObjectWrapperFactory objectWrapperFactory) {
		this.objectFactory = objectFactory;
		this.objectWrapperFactory = objectWrapperFactory;
		this.mappedResults = ((Map) objectFactory.create(Map.class));
		this.mapKey = mapKey;
	}

	public void handleResult(ResultContext context) {
		handleResult(context, 0);
	}

	public void handleResult(ResultContext context, int type) {
		Object value = context.getResultObject();
		MetaObject mo = MetaObject.forObject(value, this.objectFactory,
				this.objectWrapperFactory, null);

		Object key = mo.getValue(this.mapKey);
		if(type == 1) key = StringUtil.getInt(key);// && key instanceof java.math.BigDecimal) key = ((BigDecimal)key).intValue();
		List<Object> valList = null;
		if(value != null) {
			valList = this.mappedResults.get(key);
			if(valList == null) valList = new ArrayList<Object>();
			valList.add(value);
		}
		//System.out.println("-----------key.getClass():" + key + ":" + key.getClass());
		this.mappedResults.put(key, valList);
		//this.mappedResults.put(StringUtil.getObjectToString(key), valList);
	}

	public Map<Object, List<Object>> getMappedResults() {
		return this.mappedResults;
	}
}