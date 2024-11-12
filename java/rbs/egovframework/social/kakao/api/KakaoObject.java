package rbs.egovframework.social.kakao.api;

import java.util.HashMap;
import java.util.Map;

public class KakaoObject {
	private Map<String, Object> extraData;

	public KakaoObject() {
		this.extraData = new HashMap<String, Object>();
	}

	public Map<String, Object> getExtraData() {
		return this.extraData;
	}

	protected void add(String key, Object value) {
		this.extraData.put(key, value);
	}
}
