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

package dev.seeight.twitterscraper.impl.search;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.Map;

public class SearchAdaptiveResult {
	@SerializedName("globalObjects")
	public GlobalObjects globalObjects;
	@SerializedName("timeline")
	public Timeline timeline;

	public static class GlobalObjects {
		@SerializedName("tweets")
		public Map<String, Tweet> tweets;
		@SerializedName("users")
		public Map<String, User> users;
	}

	public static class Tweet {
		@SerializedName("created_at")
		public String createdAt;
		@SerializedName("id_str")
		public String id;
		@SerializedName("full_text")
		public String fullText;
		@SerializedName("truncated")
		public boolean truncated;
		@SerializedName("entities")
		public Entities entities;
		@SerializedName("source")
		public String source;
		@SerializedName("in_reply_to_status_id_str")
		public String inReplyToStatusId;
		@SerializedName("in_reply_to_user_id_str")
		public String inReplyToUserId;
		@SerializedName("user_id_str")
		public String userId;
		@SerializedName("is_quote_status")
		public boolean isQuoteStatus;
		@SerializedName("retweet_count")
		public int retweetCount;
		@SerializedName("favorite_count")
		public int favoriteCount;
		@SerializedName("reply_count")
		public int replyCount;
		@SerializedName("quote_count")
		public int quoteCount;
		@SerializedName("conversation_id_str")
		public String conversationId;
		@SerializedName("ext_views")
		public ExtViews extViews;
	}

	public static class ExtViews {
		@SerializedName("state")
		public String state;
		@SerializedName("count")
		public String count;
	}

	public static class Entities {
		@SerializedName("media")
		public Media[] media;
	}

	public static class Media {
		@SerializedName("id_str")
		public String id;
		@SerializedName("media_url")
		public String mediaUrl;
		@SerializedName("type")
		public String type;
	}

	public static class User {
		@SerializedName("id_str")
		public String id;
		@SerializedName("name")
		public String name;
		@SerializedName("screen_name")
		public String screenName;
		@SerializedName("location")
		public String location;
		@SerializedName("description")
		public String description;
		// TODO: add urls here
		@SerializedName("followers_count")
		public int followersCount;
		@SerializedName("friends_count")
		public int friendsCount;
		@SerializedName("listed_count")
		public int listedCount;
		@SerializedName("created_at")
		public String createdAt;
		@SerializedName("favouritesCount")
		public int favouritesCount;
		@SerializedName("statuses_count")
		public int statusesCount;
		@SerializedName("media_count")
		public int mediaCount;
		@SerializedName("profile_image_url")
		public String profileImageUrl;
		@SerializedName("profile_banner_url")
		public String profileBannerUrl;
		@SerializedName("pinned_tweet_ids_str")
		public String[] pinnedTweetIds;
	}

	/**
	 * This class is different from the Twitter Json response, as it was unnecessarily
	 * complex.
	 */
	public static class Timeline {
		public String cursorTop;
		public String cursorBottom;
	}

	public static class TimeLineCursorDeserializer implements JsonDeserializer<Timeline> {

		@Override
		public Timeline deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			String cursorTop = "";
			String cursorBottom = "";

			// that $5 billion revenue shows here
			JsonObject j = json.getAsJsonObject();
			for (JsonElement instructions : j.get("instructions").getAsJsonArray()) {
				if (!instructions.getAsJsonObject().has("addEntries")) {
					continue;
				}

				for (JsonElement jsonElement : instructions.getAsJsonObject().get("addEntries").getAsJsonObject().get("entries").getAsJsonArray()) {
					JsonObject obj = jsonElement.getAsJsonObject();

					JsonElement c1 = obj.get("content");
					if (c1 == null) {
						continue;
					}

					JsonElement c2 = c1.getAsJsonObject().get("operation");
					if (c2 == null) {
						continue;
					}

					JsonElement c3 = c2.getAsJsonObject().get("cursor");
					if (c3 == null) {
						continue;
					}

					JsonObject cursor = c3.getAsJsonObject();
					String cursorType = cursor.get("cursorType").getAsString();
					String value = cursor.get("value").getAsString();

					if (cursorType.equals("Top")) {
						cursorTop = value;
					} else if (cursorType.equals("Bottom")) {
						cursorBottom = value;
					}
				}
				break;
			}

			Timeline timeline = new Timeline();
			timeline.cursorTop = cursorTop;
			timeline.cursorBottom = cursorBottom;
			return timeline;
		}
	}
}
