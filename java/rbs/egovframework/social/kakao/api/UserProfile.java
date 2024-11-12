package rbs.egovframework.social.kakao.api;

import java.io.Serializable;

public class UserProfile implements Serializable  {
	private static final long serialVersionUID = 1L;
	private String nickname;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}