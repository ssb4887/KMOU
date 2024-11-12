package rbs.egovframework.social.naver.api.impl.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class UserMixin extends NaverObjectMixin {

	@JsonProperty("id")
	String id;
	@JsonProperty("name")
	String name;
	@JsonProperty("email")
	String email;
}
