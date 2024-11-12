package rbs.egovframework.social.kakao.api;

public abstract interface UserOperations {
	/*public static final String[] PROFILE_FIELDS = { "id", "about", "age_range",
		"bio", "birthday", "context", "cover", "currency", "devices",
		"education", "email", "favorite_athletes", "favorite_teams",
		"first_name", "gender", "hometown", "inspirational_people",
		"installed", "install_type", "is_verified", "languages",
		"last_name", "link", "locale", "location", "meeting_for",
		"middle_name", "name", "name_format", "political", "quotes",
		"payment_pricepoints", "relationship_status", "religion",
		"security_settings", "significant_other", "sports", "test_group",
		"timezone", "third_party_id", "updated_time", "verified",
		"video_upload_limits", "viewer_can_send_gift", "website", "work" };*/

	public abstract User getUserProfile();
	
	public abstract User getUserProfile(String paramString);

}
