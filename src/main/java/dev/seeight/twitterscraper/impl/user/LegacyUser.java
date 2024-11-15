/*
 * twitter-scraper-java
 * Copyright (C) 2024 c8ff
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.seeight.twitterscraper.impl.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.util.JsonHelper;

import javax.annotation.processing.Generated;
import java.util.List;

public class LegacyUser {
	public int id;
	public String id_str;
	public String name;
	public String screen_name;
	public String location;
	public String description;
	public String url;
	public Entities entities;
	public int followers_count;
	public int fast_followers_count;
	public int normal_followers_count;
	public int friends_count;
	public int listed_count;
	public String created_at;
	public int favourites_count;
	public String utc_offset;
	public String time_zone;
	public boolean geo_enabled;
	public boolean verified;
	public int statuses_count;
	public int media_count;
	public String lang;
	public boolean contributors_enabled;
	public boolean is_translator;
	public boolean is_translation_enabled;
	public String profile_background_color;
	public String profile_background_image_url;
	public String profile_background_image_url_https;
	public boolean profile_background_tile;
	public String profile_image_url;
	public String profile_image_url_https;
	public String profile_banner_url;
	public String profile_link_color;
	public String profile_sidebar_border_color;
	public String profile_sidebar_fill_color;
	public String profile_text_color;
	public boolean profile_use_background_image;
	public boolean has_extended_profile;
	public boolean default_profile;
	public boolean default_profile_image;
	public int[] pinned_tweet_ids;
	public List<String> pinned_tweet_ids_str;
	public boolean has_custom_timelines;
	public boolean can_dm;
	public boolean can_media_tag;
	public boolean following;
	public boolean follow_request_sent;
	public boolean notifications;
	public boolean muting;
	public boolean blocking;
	public boolean blocked_by;
	public boolean want_retweets;
	public String advertiser_account_type;
	public String profile_interstitial_type;
	public String business_profile_state;
	public String translator_type;
	public boolean followed_by;
	public String ext_profile_image_shape;
	public boolean ext_is_blue_verified;
	public boolean require_some_consent;

	@Generated("dev.seeight.GenerateJsonParser")
	public static LegacyUser fromJson(Gson gson, JsonHelper h, JsonObject obj) {
		h.set(obj);
		var inst = new LegacyUser();
		inst.id = h.integer("id");
		inst.id_str = h.string("id_str");
		inst.name = h.string("name");
		inst.screen_name = h.string("screen_name");
		inst.location = h.string("location");
		inst.description = h.string("description");
		inst.url = h.string("url");
		inst.entities = Entities.fromJson(gson, h, h.object("entities"));
		h.set(obj);
		inst.followers_count = h.integer("followers_count");
		inst.fast_followers_count = h.integer("fast_followers_count");
		inst.normal_followers_count = h.integer("normal_followers_count");
		inst.friends_count = h.integer("friends_count");
		inst.listed_count = h.integer("listed_count");
		inst.created_at = h.string("created_at");
		inst.favourites_count = h.integer("favourites_count");
		inst.utc_offset = h.string("utc_offset");
		inst.time_zone = h.string("time_zone");
		inst.geo_enabled = h.bool("geo_enabled");
		inst.verified = h.bool("verified");
		inst.statuses_count = h.integer("statuses_count");
		inst.media_count = h.integer("media_count");
		inst.lang = h.string("lang");
		inst.contributors_enabled = h.bool("contributors_enabled");
		inst.is_translator = h.bool("is_translator");
		inst.is_translation_enabled = h.bool("is_translation_enabled");
		inst.profile_background_color = h.string("profile_background_color");
		inst.profile_background_image_url = h.string("profile_background_image_url");
		inst.profile_background_image_url_https = h.string("profile_background_image_url_https");
		inst.profile_background_tile = h.bool("profile_background_tile");
		inst.profile_image_url = h.string("profile_image_url");
		inst.profile_image_url_https = h.string("profile_image_url_https");
		inst.profile_banner_url = h.string("profile_banner_url", null);
		inst.profile_link_color = h.string("profile_link_color");
		inst.profile_sidebar_border_color = h.string("profile_sidebar_border_color");
		inst.profile_sidebar_fill_color = h.string("profile_sidebar_fill_color");
		inst.profile_text_color = h.string("profile_text_color");
		inst.profile_use_background_image = h.bool("profile_use_background_image");
		inst.has_extended_profile = h.bool("has_extended_profile");
		inst.default_profile = h.bool("default_profile");
		inst.default_profile_image = h.bool("default_profile_image");
		inst.pinned_tweet_ids = h.intArray("pinned_tweet_ids");
		inst.pinned_tweet_ids_str = h.next("pinned_tweet_ids_str").stringList();
		h.set(obj);
		inst.has_custom_timelines = h.bool("has_custom_timelines");
		inst.can_dm = h.bool("can_dm");
		inst.can_media_tag = h.bool("can_media_tag");
		inst.following = h.bool("following");
		inst.follow_request_sent = h.bool("follow_request_sent");
		inst.notifications = h.bool("notifications");
		inst.muting = h.bool("muting");
		inst.blocking = h.bool("blocking");
		inst.blocked_by = h.bool("blocked_by");
		inst.want_retweets = h.bool("want_retweets");
		inst.advertiser_account_type = h.string("advertiser_account_type");
		inst.profile_interstitial_type = h.string("profile_interstitial_type");
		inst.business_profile_state = h.string("business_profile_state");
		inst.translator_type = h.string("translator_type");
		inst.followed_by = h.bool("followed_by");
		inst.ext_profile_image_shape = h.string("ext_profile_image_shape");
		inst.ext_is_blue_verified = h.bool("ext_is_blue_verified");
		inst.require_some_consent = h.bool("require_some_consent");

		return inst;
	}

	public static class Entities {
		public Description description;

		@Generated("dev.seeight.GenerateJsonParser")
		public static Entities fromJson(Gson gson, JsonHelper h, JsonObject obj) {
			h.set(obj);
			var inst = new Entities();
			inst.description = Description.fromJson(gson, h, h.object("description"));
			return inst;
		}

		public static class Description {
			public List<String> urls;

			@Generated("dev.seeight.GenerateJsonParser")
			public static Description fromJson(Gson gson, JsonHelper h, JsonObject obj) {
				h.set(obj);
				var inst = new Description();
				inst.urls = h.next("urls").stringList();
				h.set(obj);

				return inst;
			}
		}
	}
}