package rbs.egovframework;

import java.io.Serializable;

/**
 * 통합관리시스템에서 선택한 사용자 사이트 정보
 * @author user
 *
 */
public class WebsiteVO implements Serializable{
	
	private static final long serialVersionUID = -6841958569264366755L;
	private String siteId;
	private String siteName;
	private String siteType;
	private String localeLang;
	private int popupFnIdx;
	private int bannerFnIdx;
	private int verIdx;
	private int siteVerIdx;				// 콘텐츠관리 : 선택 버전
	
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	public String getLocaleLang() {
		return localeLang;
	}
	public void setLocaleLang(String localeLang) {
		this.localeLang = localeLang;
	}
	public int getPopupFnIdx() {
		return popupFnIdx;
	}
	public void setPopupFnIdx(int popupFnIdx) {
		this.popupFnIdx = popupFnIdx;
	}
	public int getBannerFnIdx() {
		return bannerFnIdx;
	}
	public void setBannerFnIdx(int bannerFnIdx) {
		this.bannerFnIdx = bannerFnIdx;
	}
	public int getVerIdx() {
		return verIdx;
	}
	public void setVerIdx(int verIdx) {
		this.verIdx = verIdx;
	}
	public int getSiteVerIdx() {
		return siteVerIdx;
	}
	public void setSiteVerIdx(int siteVerIdx) {
		this.siteVerIdx = siteVerIdx;
	}
	
}
