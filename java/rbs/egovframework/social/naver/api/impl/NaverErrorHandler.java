package rbs.egovframework.social.naver.api.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.social.UncategorizedApiException;
import org.springframework.web.client.DefaultResponseErrorHandler;

public class NaverErrorHandler extends DefaultResponseErrorHandler {

	private static final String FACEBOOK_PROVIDER_ID = "naver";
	private static final Log logger = LogFactory.getLog(NaverErrorHandler.class);

	public void handleError(ClientHttpResponse response) throws IOException {
		String errorMessage = extractErrorFromResponse(response);
		throw new UncategorizedApiException(FACEBOOK_PROVIDER_ID, errorMessage, null);
		//FacebookError error = extractErrorFromResponse(response);
		//handleFacebookError(response.getStatusCode(), error);
	}

	/*void handleFacebookError(HttpStatus statusCode, FacebookError error) {
		if ((error != null) && (error.getCode() != null)) {
			int code = error.getCode().intValue();

			if (code == 1)
				throw new UncategorizedApiException(FACEBOOK_PROVIDER_ID,
						error.getMessage(), null);
			if (code == 2)
				throw new ServerException(FACEBOOK_PROVIDER_ID, error.getMessage());
			if ((code == 4) || (code == 17) || (code == 340) || (code == 341))
				throw new RateLimitExceededException(FACEBOOK_PROVIDER_ID);
			if ((code == 10) || (FacebookErrors.isUserPermissionError(code)))
				throw new InsufficientPermissionException("facebook");
			if ((code == 102) || (code == 104))
				throw new InvalidAuthorizationException(FACEBOOK_PROVIDER_ID,
						error.getMessage());
			if ((code == 190) && (error.getSubcode() == null))
				throw new InvalidAuthorizationException(FACEBOOK_PROVIDER_ID,
						error.getMessage());
			if ((code == 190) && (error.getSubcode().intValue() == 463))
				throw new ExpiredAuthorizationException(FACEBOOK_PROVIDER_ID);
			if (code == 190)
				throw new RevokedAuthorizationException(FACEBOOK_PROVIDER_ID,
						error.getMessage());
			if (code == 506)
				throw new DuplicateStatusException(FACEBOOK_PROVIDER_ID,
						error.getMessage());
			if ((code == 803) || (code == 2500)) {
				throw new ResourceNotFoundException(FACEBOOK_PROVIDER_ID,
						error.getMessage());
			}
			throw new UncategorizedApiException(FACEBOOK_PROVIDER_ID, error.getMessage(),
					null);
		}
	}*/

	private /*FacebookError*/String extractErrorFromResponse(ClientHttpResponse response)
			throws IOException {
		String json = readResponseJson(response);
		/*try {
			ObjectMapper mapper = new ObjectMapper(new JsonFactory());
			JsonNode jsonNode = (JsonNode) mapper.readValue(json,
					JsonNode.class);
			if (jsonNode.has("error")) {
				JsonNode errorNode = jsonNode.get("error");
				Integer code = (errorNode.has("code")) ? Integer
						.valueOf(errorNode.get("code").intValue()) : null;
				String type = (errorNode.has("type")) ? errorNode.get("type")
						.asText() : null;
				String message = (errorNode.has("message")) ? errorNode.get(
						"message").asText() : null;
				Integer subcode = (errorNode.has("error_subcode")) ? Integer
						.valueOf(errorNode.get("error_subcode").intValue())
						: null;
				String userMessage = (errorNode.has("error_user_msg")) ? errorNode
						.get("error_user_msg").asText() : null;
				String userTitle = (errorNode.has("error_user_title")) ? errorNode
						.get("error_user_title").asText() : null;

				FacebookError error = new FacebookError(code, type, message,
						subcode, userMessage, userTitle);
				if (logger.isDebugEnabled()) {
					logger.debug("KAKAO error: ");
					logger.debug("   CODE        : " + error.getCode());
					logger.debug("   TYPE        : " + error.getType());
					logger.debug("   SUBCODE     : " + error.getSubcode());
					logger.debug("   MESSAGE     : " + error.getMessage());
					logger.debug("   USER TITLE  : " + error.getUserTitle());
					logger.debug("   USER MESSAGE: " + error.getUserMessage());
				}
				return error;
			}
		} catch (JsonParseException e) {
			return null;
		}
		return null;*/
		return json;
	}

	private String readResponseJson(ClientHttpResponse response)
			throws IOException {
		String json = readFully(response.getBody());
		if (logger.isDebugEnabled()) {
			logger.debug("Error from NAVER: " + json);
		}
		return json;
	}

	private String readFully(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();
		while (reader.ready()) {
			sb.append(reader.readLine());
		}
		return sb.toString();
	}
}
