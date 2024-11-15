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

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import dev.seeight.twitterscraper.util.GsonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SearchResult {
	@SerializedName("tweets")
	public Tweet[] tweets;
	@SerializedName("users")
	public User[] users;
	@SerializedName("cursorTop")
	public String cursorTop;
	@SerializedName("cursorBottom")
	public String cursorBottom;

	@Override
	public String toString() {
		return "SearchResult{" +
			"tweets=" + Arrays.toString(tweets) +
			", users=" + Arrays.toString(users) +
			", cursorTop='" + cursorTop + '\'' +
			", cursorBottom='" + cursorBottom + '\'' +
			'}';
	}

	public static class Tweet {
		public String userId;
		public String id;
		public String text;
		public String html;
		public Media media;
		public int likes;
		public int retweets;
		public int replies;
		public int quotes;
		public String views;

		@SerializedName("replyingToStatusId")
		public String replyingToStatusId;
		@SerializedName("replyingToUserId")
		public String replyingToUserId;
		@SerializedName("isQuoteStatus")
		public boolean isQuoteStatus;


		@Override
		public String toString() {
			return "Tweet{" +
				"userId='" + userId + '\'' +
				", id='" + id + '\'' +
				", text='" + text + '\'' +
				", html='" + html + '\'' +
				", media=" + media +
				", likes=" + likes +
				", retweets=" + retweets +
				", replies=" + replies +
				", quotes=" + quotes +
				", views='" + views + '\'' +
				", replyingToStatusId='" + replyingToStatusId + '\'' +
				", replyingToUserId='" + replyingToUserId + '\'' +
				", isQuoteStatus=" + isQuoteStatus +
				'}';
		}
	}

	public static class Media {
		public String[] photos;
		public String[] videos;

		@Override
		public String toString() {
			return "Media{" +
				"photos=" + Arrays.toString(photos) +
				", videos=" + Arrays.toString(videos) +
				'}';
		}
	}

	public static class User {

	}

	public static SearchResult fromSearchAdaptiveResult(Gson gson, SearchAdaptiveResult result) {
		List<Tweet> tweetsList = new ArrayList<>();
		for (Map.Entry<String, SearchAdaptiveResult.Tweet> entry : result.globalObjects.tweets.entrySet()) {
			tweetsList.add(fromOtherTweet(gson, entry.getValue()));
		}

		SearchResult searchResult = new SearchResult();
		searchResult.tweets = tweetsList.toArray(new Tweet[0]);
		searchResult.cursorTop = result.timeline.cursorTop;
		searchResult.cursorBottom = result.timeline.cursorBottom;
		return searchResult;
	}

	private static Tweet fromOtherTweet(Gson gson, SearchAdaptiveResult.Tweet value) {
		Tweet tweet = new Tweet();
		tweet.id = value.id;
		tweet.userId = value.userId;
		tweet.text = value.fullText;
		tweet.html = value.source;
		tweet.likes = value.favoriteCount;
		tweet.retweets = value.retweetCount;
		tweet.replies = value.replyCount;
		tweet.quotes = value.quoteCount;

		if (value.extViews != null) {
			tweet.views = value.extViews.count;
		} else {
			tweet.views = null;
		}

		tweet.replyingToStatusId = value.inReplyToStatusId;
		tweet.replyingToUserId = value.inReplyToUserId;
		tweet.isQuoteStatus = value.isQuoteStatus;

		Media media = GsonUtil.createObject(gson, Media.class);
		tweet.media = media;

		if (value.entities.media != null) {
			List<String> photosList = new ArrayList<>();
			List<String> videosList = new ArrayList<>();
			for (SearchAdaptiveResult.Media m1 : value.entities.media) {
				if (m1.type.equals("photo")) {
					photosList.add(m1.mediaUrl);
				} else if (m1.type.equals("video")) {
					videosList.add(m1.mediaUrl);
				} else {
					new Exception("Unknown type: " + m1.type).printStackTrace();
				}
			}

			media.photos = photosList.toArray(new String[0]);
			media.videos = videosList.toArray(new String[0]);
		} else {
			media.photos = new String[0];
			media.videos = new String[0];
		}

		return tweet;
	}
}
