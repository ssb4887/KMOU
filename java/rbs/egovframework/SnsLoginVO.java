package rbs.egovframework;

import java.io.Serializable;

public class SnsLoginVO implements Serializable{
	
	private static final long serialVersionUID = 7212435260972339627L;
	
	/** SNS구분(facebook 등) */
	private String snsType;
	private String snsTypeId;
	private String snsTypeName;
	/** sns별 ID */
	private String snsId;
	/** 이름 */
	private String snsName;
	/** 이메일 */
	private String snsEmail;

	public String getSnsType() {
		return snsType;
	}

	public void setSnsType(String snsType) {
		this.snsType = snsType;
	}

	public String getSnsId() {
		return snsId;
	}

	public void setSnsId(String snsId) {
		this.snsId = snsId;
	}

	public String getSnsName() {
		return snsName;
	}

	public void setSnsName(String snsName) {
		this.snsName = snsName;
	}

	public String getSnsEmail() {
		return snsEmail;
	}

	public void setSnsEmail(String snsEmail) {
		this.snsEmail = snsEmail;
	}

	public String getSnsTypeName() {
		return snsTypeName;
	}

	public void setSnsTypeName(String snsTypeName) {
		this.snsTypeName = snsTypeName;
	}

	public String getSnsTypeId() {
		return snsTypeId;
	}

	public void setSnsTypeId(String snsTypeId) {
		this.snsTypeId = snsTypeId;
	}
}
