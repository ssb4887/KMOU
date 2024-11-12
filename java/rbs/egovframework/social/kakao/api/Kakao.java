package rbs.egovframework.social.kakao.api;

import org.springframework.social.ApiBinding;
import org.springframework.web.client.RestOperations;

public abstract interface Kakao extends GraphApi, ApiBinding {
	public abstract UserOperations userOperations();
	//public abstract FeedOperations feedOperations();
	public abstract RestOperations restOperations();
	public abstract String getApplicationNamespace();
}
