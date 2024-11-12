package rbs.egovframework.social.naver.api.impl;

import org.springframework.web.client.RestTemplate;

import rbs.egovframework.social.naver.api.GraphApi;
import rbs.egovframework.social.naver.api.User;
import rbs.egovframework.social.naver.api.UserOperations;

public class UserTemplate implements UserOperations {
	private final GraphApi graphApi;
	private final RestTemplate restTemplate;

	public UserTemplate(GraphApi graphApi, RestTemplate restTemplate) {
		this.graphApi = graphApi;
		this.restTemplate = restTemplate;
	}
	/*public UserTemplate(GraphApi graphApi, RestTemplate restTemplate) {
		this.graphApi = graphApi;
		this.restTemplate = restTemplate;
	}*/

	public User getUserProfile() {
		return getUserProfile("nid/me");
	}

	public User getUserProfile(String id) {
		return ((User) this.graphApi.fetchObject(id, User.class));
		//return ((User) this.graphApi.fetchObject(id, User.class, PROFILE_FIELDS));
	}
}