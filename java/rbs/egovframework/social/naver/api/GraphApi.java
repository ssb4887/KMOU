package rbs.egovframework.social.naver.api;

import org.springframework.util.MultiValueMap;

public abstract interface GraphApi {

	@Deprecated
	public static final String GRAPH_API_URL = "https://openapi.naver.com/v1/";

	public abstract <T> T fetchObject(String paramString, Class<T> paramClass);

	public abstract <T> T fetchObject(String paramString, Class<T> paramClass,
			String[] paramArrayOfString);

	public abstract <T> T fetchObject(String paramString, Class<T> paramClass,
			MultiValueMap<String, String> paramMultiValueMap);

	public abstract String getApplicationNamespace();
	public abstract String getBaseGraphApiUrl();
}
