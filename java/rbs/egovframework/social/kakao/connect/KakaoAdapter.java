package rbs.egovframework.social.kakao.connect;

import org.springframework.social.ApiException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;

import rbs.egovframework.social.kakao.api.Kakao;
import rbs.egovframework.social.kakao.api.User;

public class KakaoAdapter implements ApiAdapter<Kakao> {
	public boolean test(Kakao kakao) {
		try {
			kakao.userOperations().getUserProfile();
			return true;
		} catch (ApiException e) {
		}
		return false;
	}

	public void setConnectionValues(Kakao kakao, ConnectionValues values) {
		User profile = (User) kakao.fetchObject("user/me", User.class);
		//User profile = (User) kakao.fetchObject("me", User.class, new String[] { "id", "name", "link" });
		values.setProviderUserId(profile.getId());
		values.setDisplayName(profile.getName());
		//values.setProfileUrl(profile.getLink());
		//values.setImageUrl(kakao.getBaseGraphApiUrl() + profile.getId()	+ "/picture");
	}

	public UserProfile fetchUserProfile(Kakao kakao) {
		User profile = kakao.userOperations().getUserProfile();
		return new UserProfileBuilder().setName(profile.getName())
				.setEmail(profile.getEmail()).build();
	}

	/**
	 * 미사용
	 */
	public void updateStatus(Kakao kakao, String message) {
		//kakao.feedOperations().updateStatus(message);
	}
}