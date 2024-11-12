package rbs.egovframework.social.kakao.api.impl.json;

import rbs.egovframework.social.kakao.api.UserAccount;
import rbs.egovframework.social.kakao.api.UserProperties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class UserMixin extends KakaoObjectMixin {

	@JsonProperty("id")
	String id;
	@JsonProperty("connected_at")
	String connected_at;
	@JsonProperty("properties")
	UserProperties properties;
	@JsonProperty("kakao_account")
	UserAccount kakao_account;
}
