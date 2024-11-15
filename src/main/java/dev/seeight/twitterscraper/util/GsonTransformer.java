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

package dev.seeight.twitterscraper.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.impl.user.User;

import java.util.HashMap;
import java.util.Map;

public class GsonTransformer {
	private final Map<String, Entry<User>> e = new HashMap<>();

	public GsonTransformer() {
		e.put("created_at", new Entry<>((t, object) -> t.createdAt = object.getAsString(), null, true));
		e.put("description", new Entry<>((t, object) -> t.description = object.getAsString(), null, true));
		e.put("favourites_count", new Entry<>((t, object) -> t.likedTweetsCount = object.getAsInt(), null, true));
		e.put("followers_count", new Entry<>((t, object) -> t.followersCount = object.getAsInt(), null, true));
		e.put("friends_count", new Entry<>((t, object) -> t.followingCount = object.getAsInt(), null, true));
		e.put("media_count", new Entry<>((t, object) -> t.mediaCount = object.getAsInt(), null, true));
		e.put("statuses_count", new Entry<>((t, object) -> t.tweetsCount = object.getAsInt(), null, true));
		e.put("name", new Entry<>((t, object) -> t.name = object.getAsString(), null, true));
		e.put("screen_name", new Entry<>((t, object) -> t.screenName = object.getAsString(), null, true));
		e.put("location", new Entry<>((t, object) -> t.location = object.getAsString(), null, true));
		e.put("profile_banner_url", new Entry<>((t, object) -> t.profileBannerUrl = object.getAsString(), null, false));
		e.put("profile_image_url_https", new Entry<>((t, object) -> t.profileImageUrl = object.getAsString(), null, false));
		e.put("protected", new Entry<>((t, object) -> t.isProtected = object.getAsBoolean(), null, false));
		e.put("verified", new Entry<>((t, object) -> t.verified = object.getAsBoolean(), null, true));
		e.put("verified_type", new Entry<>((t, object) -> t.verifiedType = object.getAsString(), null, false));
		e.put("can_dm", new Entry<>((t, object) -> t.canDM = object.getAsBoolean(), null, false));
		e.put("can_media_tag", new Entry<>((t, object) -> t.canMediaTag = object.getAsBoolean(), null, false));
		// e.put("friends_count", new Entry<>((t, object) -> t.friendsCount = object.getAsInt(), null, false));
		e.put("following", new Entry<>((t, object) -> t.following = object.getAsBoolean(), null, false));
		e.put("blocking", new Entry<>((t, object) -> t.blocking = object.getAsBoolean(), null, false));
	}

	public User oldFashionedWay(JsonObject obj, JsonHelper h) {
		var ref = new User();

		JsonObject legacy = h.set(obj).next("legacy").object();

		// (inside legacy object)
		h.set(legacy);

		ref.createdAt = h.string("created_at");
		ref.description = h.string("description");

		// counts
		ref.likedTweetsCount = h.integer("favourites_count");
		ref.followersCount = h.integer("followers_count");
		ref.followingCount = h.integer("friends_count");
		ref.mediaCount = h.integer("media_count");
		ref.tweetsCount = h.integer("statuses_count");

		// Info
		ref.name = h.string("name");
		ref.screenName = h.string("screen_name");
		ref.location = h.string("location");
		ref.profileBannerUrl = h.string("profile_banner_url", null);
		ref.profileImageUrl = h.string("profile_image_url_https");

		ref.isProtected = h.bool("protected", false);

		// verified shenanigans
		ref.verified = h.bool("verified");
		ref.verifiedType = h.string("verified_type", null);

		// user specific info
		ref.canDM = h.bool("can_dm", false);
		ref.canMediaTag = h.bool("can_media_tag", false);
		ref.friendsCount = h.integer("friends_count", 0);
		ref.following = h.bool("following", false);
		ref.blocking = h.bool("blocking", false);

		return ref;
	}

	public User fromU(JsonObject legacy) {
		User u = new User();

		for (Map.Entry<String, JsonElement> e : legacy.entrySet()) {
			Entry<User> ei = this.e.get(e.getKey());
			if (ei == null) continue;
			ei.name.consume(u, e.getValue());
		}

		return u;
	}

	public interface Consumer<T> {
		void consume(T parent, JsonElement object);
	}

	public static class Entry<T> {
		public final Consumer<T> name;
		public final Object defaultValue;
		public final boolean required;

		public Entry(Consumer<T> name, Object defaultValue, boolean required) {
			this.name = name;
			this.defaultValue = defaultValue;
			this.required = required;
		}
	}
}
