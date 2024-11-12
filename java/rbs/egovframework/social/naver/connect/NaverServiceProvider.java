package rbs.egovframework.social.naver.connect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

import rbs.egovframework.social.naver.api.Naver;
import rbs.egovframework.social.naver.api.impl.NaverTemplate;

public class NaverServiceProvider extends AbstractOAuth2ServiceProvider<Naver> {
	
	private String appNamespace;
	//private static final String API_VERSION = "1";
	//private static final String GRAPH_API_URL = "https://kapi.kakao.com/v1/";

	public NaverServiceProvider(String appId, String appSecret,	String appNamespace) {
		super(getOAuth2Template(appId, appSecret));
		this.appNamespace = appNamespace;
	}

	private static OAuth2Template getOAuth2Template(String appId, String appSecret) {
		OAuth2Template oAuth2Template = new OAuth2Template(appId, appSecret,
				"https://nid.naver.com/oauth2.0/authorize",
				"https://nid.naver.com/oauth2.0/token");

		oAuth2Template.setUseParametersForClientAuthentication(true);
		return oAuth2Template;
	}

	public Naver getApi(String accessToken) {
		return new NaverTemplate(accessToken, this.appNamespace);
	}
}