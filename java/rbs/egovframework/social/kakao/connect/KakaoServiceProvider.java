package rbs.egovframework.social.kakao.connect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

import rbs.egovframework.social.kakao.api.Kakao;
import rbs.egovframework.social.kakao.api.impl.KakaoTemplate;

public class KakaoServiceProvider extends AbstractOAuth2ServiceProvider<Kakao> {
	
	private String appNamespace;
	//private static final String API_VERSION = "1";
	//private static final String GRAPH_API_URL = "https://kapi.kakao.com/v1/";

	public KakaoServiceProvider(String appId, String appSecret,	String appNamespace) {
		super(getOAuth2Template(appId, appSecret));
		this.appNamespace = appNamespace;
	}

	private static OAuth2Template getOAuth2Template(String appId, String appSecret) {
		OAuth2Template oAuth2Template = new OAuth2Template(appId, appSecret,
				"https://kauth.kakao.com/oauth/authorize",
				"https://kauth.kakao.com/oauth/token");

		oAuth2Template.setUseParametersForClientAuthentication(true);
		return oAuth2Template;
	}

	public Kakao getApi(String accessToken) {
		return new KakaoTemplate(accessToken, this.appNamespace);
	}
}
