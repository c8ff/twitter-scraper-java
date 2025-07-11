/*
 * twitter-scraper-java.main
 * Copyright (C) 2025 c8ff
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.seeight.twitterscraper.impl;

import com.google.gson.annotations.SerializedName;

// thanks chatgpt
public class Settings {
	@SerializedName("protected")
	public boolean isProtected;
	@SerializedName("screen_name")
	public String screenName;
	@SerializedName("always_use_https")
	public boolean alwaysUseHttps;
	@SerializedName("use_cookie_personalization")
	public boolean useCookiePersonalization;
	@SerializedName("sleep_time")
	public SleepTime sleepTime;
	@SerializedName("geo_enabled")
	public boolean geoEnabled;
	@SerializedName("language")
	public String language;
	@SerializedName("discoverable_by_email")
	public boolean discoverableByEmail;
	@SerializedName("discoverable_by_mobile_phone")
	public boolean discoverableByMobilePhone;
	@SerializedName("display_sensitive_media")
	public boolean displaySensitiveMedia;
	@SerializedName("personalized_trends")
	public boolean personalizedTrends;
	@SerializedName("allow_media_tagging")
	public String allowMediaTagging;
	@SerializedName("allow_contributor_request")
	public String allowContributorRequest;
	@SerializedName("allow_ads_personalization")
	public boolean allowAdsPersonalization;
	@SerializedName("allow_logged_out_device_personalization")
	public boolean allowLoggedOutDevicePersonalization;
	@SerializedName("allow_location_history_personalization")
	public boolean allowLocationHistoryPersonalization;
	@SerializedName("allow_sharing_data_for_third_party_personalization")
	public boolean allowSharingDataForThirdPartyPersonalization;
	@SerializedName("allow_dms_from")
	public String allowDmsFrom;
	@SerializedName("always_allow_dms_from_subscribers")
	public Object alwaysAllowDmsFromSubscribers;
	@SerializedName("allow_dm_groups_from")
	public String allowDmGroupsFrom;
	@SerializedName("translator_type")
	public String translatorType;
	@SerializedName("country_code")
	public String countryCode;
	@SerializedName("nsfw_user")
	public boolean nsfwUser;
	@SerializedName("nsfw_admin")
	public boolean nsfwAdmin;
	@SerializedName("ranked_timeline_setting")
	public int rankedTimelineSetting;
	@SerializedName("ranked_timeline_eligible")
	public Object rankedTimelineEligible;
	@SerializedName("address_book_live_sync_enabled")
	public boolean addressBookLiveSyncEnabled;
	@SerializedName("universal_quality_filtering_enabled")
	public String universalQualityFilteringEnabled;
	@SerializedName("dm_receipt_setting")
	public String dmReceiptSetting;
	@SerializedName("alt_text_compose_enabled")
	public Object altTextComposeEnabled;
	@SerializedName("mention_filter")
	public String mentionFilter;
	@SerializedName("allow_authenticated_periscope_requests")
	public boolean allowAuthenticatedPeriscopeRequests;
	@SerializedName("protect_password_reset")
	public boolean protectPasswordReset;
	@SerializedName("require_password_login")
	public boolean requirePasswordLogin;
	@SerializedName("requires_login_verification")
	public boolean requiresLoginVerification;
	@SerializedName("ext_sharing_audiospaces_listening_data_with_followers")
	public boolean extSharingAudioSpacesListeningDataWithFollowers;
	@SerializedName("dm_quality_filter")
	public String dmQualityFilter;
	@SerializedName("autoplay_disabled")
	public boolean autoplayDisabled;

	public static class SleepTime {
		@SerializedName("enabled")
		public boolean enabled;
		@SerializedName("end_time")
		public Object endTime;
		@SerializedName("start_time")
		public Object startTime;
	}
}
