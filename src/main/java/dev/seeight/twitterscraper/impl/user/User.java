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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import dev.seeight.twitterscraper.impl.Entry;
import dev.seeight.twitterscraper.impl.Url;
import dev.seeight.twitterscraper.util.GsonUtil;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User extends Entry {
	public String restId;

	// legacy
	public String createdAt;
	public String rawDescription;
	public String description;
	public List<Url> descriptionUrls;

	public @Nullable User.Birthdate birthdate;

	public Url url;

	public int likedTweetsCount;
	public int followersCount;
	public int followingCount;
	public int mediaCount;
	public int tweetsCount;

	public int friendsCount;

	public String name;
	public String screenName;
	public @Nullable String location;
	public String[] pinnedTweetsIds;
	public boolean possiblySensitive;
	public boolean verified;
	public boolean blueVerified;
	@SerializedName("protected")
	public boolean isProtected;
	public String verifiedType;

	public String profileImageUrl;
	public String profileBannerUrl;
	public ProfileImageShape profileImageShape;

	public boolean canDM;
	public boolean canMediaTag;

	public boolean following;
	public boolean followedBy;
	public boolean blocking;
	public boolean blockedBy;
	public boolean muting;
	public boolean notifications;
	public boolean wantRetweets;

	public static User fromJson(Gson gson, JsonObject obj, JsonHelper h) {
		User ref = GsonUtil.createObject(gson, User.class);

		h.set(obj);
		ref.restId = h.string("rest_id", null);

		JsonObject legacy;
		if (ref.restId == null) {
			ref.restId = h.string("id_str");
			legacy = obj;
		} else {
			legacy = h.set(obj).next("legacy").object();
		}

		// (inside 'root' object)
		h.set(obj);

		ref.blueVerified = h.bool("is_blue_verified", false);

		String s = h.string("profile_image_shape", "");
		switch (s) {
			case "Square" -> ref.profileImageShape = ProfileImageShape.SQUARE;
			case "Circle" -> ref.profileImageShape = ProfileImageShape.CIRCLE;
			default -> ref.profileImageShape = ProfileImageShape.UNKNOWN;
		}

		if (h.has("legacy_extended_profile") && h.next("legacy_extended_profile").has("birthdate")) {
			h.next("birthdate");
			var birthdate = new Birthdate();
			birthdate.day = h.integer("day", -1);
			birthdate.month = h.integer("month", -1);
			birthdate.year = h.integer("year", -1);
			birthdate.visibility = Birthdate.Visibility.from(h.string("visibility"));
			birthdate.yearVisibility = Birthdate.Visibility.from(h.string("year_visibility"));
			ref.birthdate = birthdate;
		}

		h.set(legacy);
		ref.createdAt = h.string("created_at");
		ref.rawDescription = ref.description = h.string("description");

		// Replace "t.co" links with normal links
		JsonObject entities = h.object("entities");
		JsonArray v = h.set(entities).next("description").next("urls").array();

		ref.descriptionUrls = Collections.emptyList();

		for (JsonElement elm : v) {
			if (!(elm instanceof JsonObject o)) {
				continue;
			}

			if (ref.descriptionUrls == Collections.<Url>emptyList()) {
				ref.descriptionUrls = new ArrayList<>();
			}

			Url u = Url.fromJson(o, h);
			ref.descriptionUrls.add(u);

			String url = u.url;
			String originalUrl = u.expandedUrl;
			ref.description = ref.description.replace(url, originalUrl);
		}

		// Define URL in bio
		if (entities.has("url")) {
			ref.url = Url.fromJson(h.set(entities).next("url").next("urls").get(0).object(), h);
		}

		// (inside legacy object)
		h.set(legacy);

		// counts
		ref.likedTweetsCount = h.integer("favourites_count");
		ref.followersCount = h.integer("followers_count");
		ref.followingCount = h.integer("friends_count");
		ref.mediaCount = h.integer("media_count", -1);
		ref.tweetsCount = h.integer("statuses_count");

		// Info
		ref.name = h.string("name");
		ref.screenName = h.string("screen_name");
		ref.location = h.string("location", null);
		ref.profileBannerUrl = h.string("profile_banner_url", null);
		ref.profileImageUrl = h.string("profile_image_url_https");

		ref.pinnedTweetsIds = h.set(legacy).stringArray("pinned_tweet_ids_str", new String[0]);
		h.set(legacy);

		if (h.has("possibly_sensitive")) {
			ref.possiblySensitive = h.bool("possibly_sensitive", false);
		} else if (h.has("status")) {
			ref.possiblySensitive = h.next("status").bool("possibly_sensitive", false);
			h.set(legacy);
		}

		ref.isProtected = h.bool("protected", false);

		// verified shenanigans
		ref.verified = h.bool("verified", false);
		ref.verifiedType = h.string("verified_type", null);

		// user specific info
		ref.canDM = h.bool("can_dm", false);
		ref.canMediaTag = h.bool("can_media_tag", false);
		ref.friendsCount = h.integer("friends_count", 0);
		ref.following = h.bool("following", false);
		ref.followedBy = h.bool("followed_by", false);
		ref.blocking = h.bool("blocking", false);
		ref.blockedBy = h.bool("blocked_by", false);
		ref.muting = h.bool("muting", false);
		ref.notifications = h.bool("notifications", false);
		ref.wantRetweets = h.bool("want_retweets", false);

		return ref;
	}

	public static class Birthdate {
		public int day;
		public int month;
		public int year;
		public Visibility visibility;
		public Visibility yearVisibility;

		public enum Visibility {
			Public,
			Followers,
			Following,
			MutualFollow,
			Self,
			;

			static Visibility from(String str) {
				for (Visibility value : values()) {
					if (value.name().equalsIgnoreCase(str)) {
						return value;
					}
				}

				throw new RuntimeException("Unknown visibility type: " + str);
			}
		}
	}
}
