package rbs.egovframework.social.kakao.api;

import java.io.Serializable;

public class User implements Serializable  {
	private static final long serialVersionUID = 1L;
	private String id;
	private String connected_at;
	private UserProperties properties;
	private UserAccount kakao_account; 
	private String email;
	private String name;
	private String gender;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public UserAccount getKakao_account() {
		return kakao_account;
	}
	public void setKakao_account(UserAccount kakao_account) {
		if(kakao_account != null) {
			this.email = kakao_account.getEmail();
			this.name = kakao_account.getProfile().getNickname();
		}
		this.kakao_account = kakao_account;
	}
	public String getConnected_at() {
		return connected_at;
	}
	public void setConnected_at(String connected_at) {
		this.connected_at = connected_at;
	}
	public UserProperties getProperties() {
		return properties;
	}
	public void setProperties(UserProperties properties) {
		this.properties = properties;
	}
	
}