package rbs.egovframework.social.naver.api.impl.json;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class NaverObjectMixin {
	@JsonAnySetter
	abstract void add(String paramString, Object paramObject);
}
