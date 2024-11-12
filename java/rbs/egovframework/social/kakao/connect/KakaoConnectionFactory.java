package rbs.egovframework.social.kakao.connect;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;

import rbs.egovframework.social.kakao.api.Kakao;

public class KakaoConnectionFactory extends OAuth2ConnectionFactory<Kakao> {

	public KakaoConnectionFactory(String appId) {
		this(appId, null, null);
	}
	
	public KakaoConnectionFactory(String appId, String appSecret) {
		this(appId, appSecret, null);
	}

	public KakaoConnectionFactory(String appId, String appSecret, String appNamespace) {
		super("kakao", new KakaoServiceProvider(appId, appSecret, appNamespace), new KakaoAdapter());
	}
}
