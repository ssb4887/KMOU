package rbs.egovframework.social.naver.connect;

import org.springframework.social.ApiException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;

import rbs.egovframework.social.naver.api.Naver;
import rbs.egovframework.social.naver.api.User;

public class NaverAdapter implements ApiAdapter<Naver> {
	public boolean test(Naver naver) {
		try {
			naver.userOperations().getUserProfile();
			return true;
		} catch (ApiException e) {
		}
		return false;
	}

	public void setConnectionValues(Naver naver, ConnectionValues values) {
		User profile = (User) naver.fetchObject("nid/me", User.class);
		//User profile = (User) kakao.fetchObject("me", User.class, new String[] { "id", "name", "link" });
		values.setProviderUserId(profile.getId());
		values.setDisplayName(profile.getName());
		//values.setProfileUrl(profile.getLink());
		//values.setImageUrl(kakao.getBaseGraphApiUrl() + profile.getId()	+ "/picture");
	}

	public UserProfile fetchUserProfile(Naver naver) {
		User profile = naver.userOperations().getUserProfile();
		return new UserProfileBuilder().setName(profile.getName())
				.setEmail(profile.getEmail()).build();
	}

	/**
	 * 미사용
	 */
	public void updateStatus(Naver naver, String message) {
		//kakao.feedOperations().updateStatus(message);
	}
}