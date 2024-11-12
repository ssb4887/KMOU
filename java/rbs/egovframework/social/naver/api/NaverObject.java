package rbs.egovframework.social.naver.api;

import java.util.HashMap;
import java.util.Map;

public class NaverObject {
	private Map<String, Object> extraData;

	public NaverObject() {
		this.extraData = new HashMap<String, Object>();
	}

	public Map<String, Object> getExtraData() {
		return this.extraData;
	}

	protected void add(String key, Object value) {
		this.extraData.put(key, value);
	}
}
