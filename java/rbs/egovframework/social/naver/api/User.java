package rbs.egovframework.social.naver.api;

import java.io.Serializable;

public class User implements Serializable  {
	private static final long serialVersionUID = 1L;
	private String id;
	private String email;
	private String name;
	private String resultcode;
	private String message;
	private UserAccount response;
	
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
	public String getResultcode() {
		return resultcode;
	}
	public void setResultcode(String resultcode) {
		this.resultcode = resultcode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public UserAccount getResponse() {
		return response;
	}
	public void setResponse(UserAccount response) {
		if(response != null) {
			this.id = response.getId();
			this.email = response.getEmail();
			this.name = response.getName();
		}
		this.response = response;
	}	
}