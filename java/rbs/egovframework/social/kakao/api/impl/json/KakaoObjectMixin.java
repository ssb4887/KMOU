package rbs.egovframework.social.kakao.api.impl.json;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class KakaoObjectMixin {
	@JsonAnySetter
	abstract void add(String paramString, Object paramObject);
}
