package rbs.egovframework.social.naver.connect;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;

import rbs.egovframework.social.naver.api.Naver;

public class NaverConnectionFactory extends OAuth2ConnectionFactory<Naver> {

	public NaverConnectionFactory(String appId) {
		this(appId, null, null);
	}
	
	public NaverConnectionFactory(String appId, String appSecret) {
		this(appId, appSecret, null);
	}

	public NaverConnectionFactory(String appId, String appSecret, String appNamespace) {
		super("naver", new NaverServiceProvider(appId, appSecret, appNamespace), new NaverAdapter());
	}
}
