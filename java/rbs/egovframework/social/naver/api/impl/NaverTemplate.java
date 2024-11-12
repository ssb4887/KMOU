package rbs.egovframework.social.naver.api.impl;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.social.UncategorizedApiException;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.OAuth2Version;
import org.springframework.social.support.ClientHttpRequestFactorySelector;
import org.springframework.social.support.URIBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import rbs.egovframework.social.naver.api.Naver;
import rbs.egovframework.social.naver.api.UserOperations;
import rbs.egovframework.social.naver.api.impl.json.NaverModule;

public class NaverTemplate extends AbstractOAuth2ApiBinding implements Naver {
	private String appId;
	private String accessToken;
	private UserOperations userOperations;
	private ObjectMapper objectMapper;
	private String applicationNamespace;
	private String apiVersion;

	public NaverTemplate(String accessToken) {
		this(accessToken, null);
	}

	public NaverTemplate(String accessToken, String applicationNamespace) {
		this(accessToken, applicationNamespace, null);
	}

	public NaverTemplate(String accessToken, String applicationNamespace, String appId) {
		super(accessToken);

		this.accessToken = accessToken;
		this.apiVersion = "1";
		this.applicationNamespace = applicationNamespace;
		this.appId = appId;
		initialize();
	}

	public void setRequestFactory(ClientHttpRequestFactory requestFactory) {
		super.setRequestFactory(ClientHttpRequestFactorySelector.bufferRequests(requestFactory));
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}
	
	public rbs.egovframework.social.naver.api.UserOperations userOperations() {
		return this.userOperations;
	}
	public RestOperations restOperations() {
		return getRestTemplate();
	}
	public String getApplicationNamespace() {
		return this.applicationNamespace;
	}
	public <T> T fetchObject(String objectId, Class<T> type) {
		URI uri = URIBuilder.fromUri(getBaseGraphApiUrl() + objectId).build();
		return getRestTemplate().getForObject(uri, type);
		/*getRestTemplate().setInterceptors(null);
		URI uri = URIBuilder.fromUri(getBaseGraphApiUrl() + objectId).build();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + this.accessToken);
		HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> responseEntity = getRestTemplate().exchange(uri, HttpMethod.GET, requestEntity, String.class);
		
		T userProfile = null;
		try {
			userProfile = (T)objectMapper.readValue(responseEntity.getBody(), type);
		} catch(JsonMappingException e) {
			e.getStackTrace();
		} catch(JsonParseException e) {
			e.getStackTrace();
		} catch(IOException e) {
			e.getStackTrace();
		}
		return userProfile;*/
	}

	public <T> T fetchObject(String objectId, Class<T> type, String[] fields) {
		MultiValueMap<String, String> queryParameters = new LinkedMultiValueMap<String, String>();
		if (fields.length > 0) {
			String joinedFields = join(fields);
			queryParameters.set("property_keys", joinedFields);
		}
		return fetchObject(objectId, type, queryParameters);
	}

	public <T> T fetchObject(String objectId, Class<T> type, MultiValueMap<String, String> queryParameters) {
		URI uri = URIBuilder.fromUri(getBaseGraphApiUrl() + objectId).queryParams(queryParameters).build();
		return getRestTemplate().getForObject(uri, type);
	}

	public String getBaseGraphApiUrl() {
		if (this.apiVersion != null) {
			return "https://openapi.naver.com/v" + this.apiVersion + "/";
		}
		return "https://openapi.naver.com/";
	}

	protected OAuth2Version getOAuth2Version() {
		return OAuth2Version.BEARER;
	}

	protected void configureRestTemplate(RestTemplate restTemplate) {
		restTemplate.setErrorHandler(new NaverErrorHandler());
	}
	
	protected MappingJackson2HttpMessageConverter getJsonMessageConverter() {
		MappingJackson2HttpMessageConverter converter = super.getJsonMessageConverter();
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(new NaverModule());
		converter.setObjectMapper(this.objectMapper);
		return converter;
	}

	private void initialize() {
		//super.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		super.setRequestFactory(ClientHttpRequestFactorySelector.bufferRequests(getRestTemplate().getRequestFactory()));
		initSubApis();
	}

	private void initSubApis() {
		this.userOperations = new UserTemplate(this, getRestTemplate());
	}

	private <T> List<T> deserializeDataList(JsonNode jsonNode, Class<T> elementType) {
		try {
			CollectionType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, elementType);
			return ((List) this.objectMapper.reader(listType).readValue(jsonNode.toString()));
		} catch (IOException e) {
			throw new UncategorizedApiException(
					"naver",
					"Error deserializing data from Naver: " + e.getMessage(),
					e);
		}
	}

	private String join(String[] strings) {
		StringBuilder builder = new StringBuilder();
		if (strings.length > 0) {
			builder.append("\"" + strings[0] + "\"");
			for (int i = 1; i < strings.length; ++i) {
				builder.append(",\"" + strings[i] + "\"");
			}
		}
		return builder.toString();
	}
}