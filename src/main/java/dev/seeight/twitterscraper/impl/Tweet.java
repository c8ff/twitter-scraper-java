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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.impl.entry.TweetTombstone;
import dev.seeight.twitterscraper.impl.user.User;
import dev.seeight.twitterscraper.impl.user.UserMedia;
import dev.seeight.twitterscraper.impl.user.UserMention;
import dev.seeight.twitterscraper.util.GsonUtil;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tweet extends Entry {
	public int bookmarks;
	public int quotes;
	public int likes;
	public int replies;
	public int retweets;
	public String views;
	@Deprecated
	public long creationDate;
	public String createdAt;
	public String postImageDescription;
	public String postVideoDescription;
	public String id;
	@Deprecated
	public String text;
	public String rawText;
	public String language;
	public String publishDevice;
	public String inReplyToScreenName;
	public String inReplyToStatusIdStr;
	public String inReplyToUserIdStr;
	public boolean isQuoteStatus;
	public boolean possiblySensitive;
	public boolean possiblySensitiveEditable;
	public User user;
	public String source;

	public boolean liked;
	public boolean retweeted;
	public boolean bookmarked;

	public Entry retweetTweet;
	public Entry quotedTweet;

	public boolean restricted;

	public Range displayTextRange;

	public TweetEntities entities;

	public boolean hasBirdwatchNotes;
	public BirdwatchPivot birdwatchPivot;

	public String _tweetDisplayType = "Tweet";

	public NoteTweet noteTweet;

	public Card card;

	// TODO: add support for limitedActionResults

	public static @NotNull Tweet fromJson(Gson gson, JsonObject rootObject, JsonHelper helper) {
		return Tweet.fromJson(gson, rootObject, helper, GsonUtil.createObject(gson, Tweet.class));
	}

	public static <T extends Tweet> @NotNull T fromJson(Gson gson, JsonObject rootObject, JsonHelper helper, T tweet) {
		final JsonObject legacy;
		helper.set(rootObject);
		if (helper.equals("__typename", "TweetWithVisibilityResults")) {
			rootObject = rootObject.getAsJsonObject("tweet");
			legacy = rootObject.getAsJsonObject("legacy");
		} else if (helper.equals("__typename", "TweetTombstone")) {
			throw new UnsupportedOperationException("TweetTombstone");
		} else {
			legacy = helper.object("legacy");
		}

		helper.set(rootObject);

		if (helper.has("card")) {
			tweet.card = Card.fromJson(helper, helper.object("card"));
			helper.set(rootObject);
		}

        tweet.postImageDescription = helper.string("post_image_description", null);
        tweet.postVideoDescription = helper.string("post_video_description", null);
		tweet.id = rootObject.get("rest_id").getAsString();
		tweet.views = helper.set(rootObject).next("views").string("count", null);
		tweet.publishDevice = helper.set(rootObject).string("source");

		helper.set(legacy);
		tweet.bookmarks = helper.integer("bookmark_count");
		tweet.createdAt = helper.string("created_at");
		tweet.displayTextRange = Range.fromArray(helper.array("display_text_range"));
		tweet.creationDate = TwitterApi.convertTwitterDateToEpochUTC(tweet.createdAt);

		String rawText = helper.string("full_text");

		JsonObject entities = legacy.get("entities").getAsJsonObject();
		if (entities != null) {
			tweet.entities = TweetEntities.fromJson(entities, helper);
		}

		// TODO: Add support for edits
		// TODO: Add support for 'focus' rectangles in tweet media (This might be used to crop pictures.)

		helper.set(legacy);
		tweet.likes = helper.integer("favorite_count", -1);
		tweet.inReplyToScreenName = helper.string("in_reply_to_screen_name", null);
		tweet.inReplyToStatusIdStr = helper.string("in_reply_to_status_id_str", null);
		tweet.inReplyToUserIdStr = helper.string("in_reply_to_user_id_str", null);
		tweet.isQuoteStatus = helper.bool("is_quote_status");
		tweet.language = helper.string("lang", null);
		tweet.possiblySensitive = helper.bool("possibly_sensitive", false);
		tweet.possiblySensitiveEditable = helper.bool("possibly_sensitive_editable", false);
		tweet.quotes = helper.integer("quote_count");
		tweet.replies = helper.integer("reply_count");
		tweet.retweets = helper.integer("retweet_count");
		tweet.source = helper.string("source", null);

		tweet.bookmarked = helper.bool("bookmarked", false);
		tweet.liked = helper.bool("favorited", false);
		tweet.retweeted = helper.bool("retweeted", false);

		tweet.text = rawText;
		tweet.rawText = rawText;

		tweet.user = User.fromJson(gson, helper.query(rootObject, "core", "user_results", "result").getAsJsonObject(), helper);
		helper.set(rootObject);

		if (helper.set(rootObject).has("note_tweet")) {
			helper.next("note_tweet");
			NoteTweet t = new NoteTweet();
			t.isExpandable = helper.bool("is_expandable", false);
			helper.next("note_tweet_results").next("result");
			t.id = helper.string("id");
			t.text = helper.string("text");
			tweet.noteTweet = t;
		}

		tweet.hasBirdwatchNotes = helper.set(rootObject).bool("has_birdwatch_notes", false);
		if (helper.has("birdwatch_pivot")) {
			tweet.birdwatchPivot = BirdwatchPivot.fromJson(helper.object("birdwatch_pivot"), helper);
		}

		JsonElement rt = legacy.get("retweeted_status_result");
		if (rt instanceof JsonObject) {
            tweet.retweetTweet = parseResult(gson, helper, helper.set(rt).next("result").object());
		}

		JsonElement qs = rootObject.get("quoted_status_result");
		if (qs instanceof JsonObject) {
			if (helper.set(qs).has("result")) {
				tweet.quotedTweet = parseResult(gson, helper, helper.next("result").object());
			}
		}

		return tweet;
	}

    public static Entry parseResult(Gson gson, JsonHelper helper, JsonObject result) {
        var typeName = helper.string("__typename");
        if (typeName.equals("Tweet") || typeName.equals("TweetWithVisibilityResults")) {
            return Tweet.fromJson(gson, result, helper);
        } else if (typeName.equals("TweetTombstone")) {
            return TweetTombstone.fromJson(helper, result);
        } else {
            throw new RuntimeException("Unsupported quote tweet type: " + typeName);
        }
    }

	public static class TweetEntities {
		public List<UserMedia.MediaEntity> media;
		public List<UserMention> userMentions;
		public List<Url> urls;
		public List<Hashtag> hashtags;

		public static TweetEntities fromJson(JsonObject legacy_entities, JsonHelper h) {
			h.set(legacy_entities);
			TweetEntities entities = new TweetEntities();

			entities.urls = Collections.emptyList();
			if (h.has("urls"))
				for (JsonElement elm : h.array("urls")) {
					if (!(elm instanceof JsonObject o)) continue;
					if (entities.urls.isEmpty()) entities.urls = new ArrayList<>();
					entities.urls.add(Url.fromJson(o, h));
				}

			entities.userMentions = Collections.emptyList();
			if (h.set(legacy_entities).has("user_mentions"))
				for (JsonElement elm : h.array("user_mentions")) {
					if (entities.userMentions.isEmpty()) entities.userMentions = new ArrayList<>();
					entities.userMentions.add(UserMention.fromJson(elm, h));
				}

			entities.hashtags = Collections.emptyList();
			if (h.set(legacy_entities).has("hashtags"))
				for (JsonElement elm : h.array("hashtags")) {
					if (entities.hashtags.isEmpty()) entities.hashtags = new ArrayList<>();

					h.set(elm);
					entities.hashtags.add(new Hashtag(
						h.string("text"),
						Range.fromArray(h.array("indices"))
					));
				}

			entities.media = Collections.emptyList();
			if (h.set(legacy_entities).has("media"))
				for (JsonElement elm : h.array("media")) {
					if (!(elm instanceof JsonObject object)) continue;
					if (entities.media.isEmpty()) entities.media = new ArrayList<>();
					entities.media.add(UserMedia.MediaEntity.fromJson(object, h));
				}

			return entities;
		}
	}
}
