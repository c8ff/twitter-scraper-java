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

package dev.seeight.twitterscraper.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.impl.user.User;
import dev.seeight.twitterscraper.impl.user.UserMedia;
import dev.seeight.twitterscraper.impl.user.UserMention;
import dev.seeight.twitterscraper.util.GsonUtil;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
	public String id;
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
	@Deprecated
	public UserMedia.Media media;
	public User user;
	public String source;

	public boolean liked;
	public boolean retweeted;
	public boolean bookmarked;

	public Tweet retweetTweet;
	public Tweet quotedTweet;

	public boolean restricted;

	public Range displayTextRange;

	public TweetEntities entities;

	public boolean hasBirdwatchNotes;
	public BirdwatchPivot birdwatchPivot;

	public String _tweetDisplayType = "Tweet";

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
			// TODO: Handle this correctly.
			tweet.user = new User();
			tweet.user.name = "null";
			tweet.user.screenName = "null";
			tweet.id = "0";
			tweet.bookmarked = false;
			tweet.restricted = true;
			tweet.text = "You're unable to view this tweet.";
			return tweet;
		} else {
			legacy = helper.object("legacy");
		}

		helper.set(rootObject);
		tweet.id = rootObject.get("rest_id").getAsString();
		tweet.views = helper.set(rootObject).next("views").stringOrDefault("count", null);
		tweet.publishDevice = helper.set(rootObject).string("source");

		helper.set(legacy);
		tweet.bookmarks = helper.integer("bookmark_count");
		tweet.createdAt = helper.string("created_at");
		tweet.displayTextRange = Range.fromArray(helper.array("display_text_range"));
		tweet.creationDate = TwitterApi.convertTwitterDateToEpochUTC(tweet.createdAt);

		String rawText = helper.string("full_text");
		String fullText = rawText;

		List<UserMedia.Photo> photos = new ArrayList<>();
		List<UserMedia.Video> videos = new ArrayList<>();
		List<UserMedia.Video> animatedGif = new ArrayList<>();

		if (legacy.has("extended_entities")) {
			JsonObject extendedEntities = helper.set(legacy).next("extended_entities").object();

			if (extendedEntities.has("media")) {
				JsonArray extMedia = extendedEntities.get("media").getAsJsonArray();

				for (JsonElement jsonElement : extMedia) {
					if (!(jsonElement instanceof JsonObject obj)) {
						continue;
					}

					String url = obj.get("url").getAsString();
					fullText = fullText.replace(url, "");

					helper.set(obj);
					String type = helper.string("type");
					String mediaUrlHttps = helper.string("media_url_https");

					if (type.equals("photo")) {
						JsonObject originalInfo = helper.object("original_info");

						UserMedia.Photo e = GsonUtil.createObject(gson, UserMedia.Photo.class);

						// I hate this
						List<UserMedia.PhotoSize> listSizes = new ArrayList<>();
						JsonObject sizes = helper.object("sizes");

						UserMedia.PhotoSize f = GsonUtil.createObject(gson, UserMedia.PhotoSize.class);
						f.name = "orig";
						f.width = originalInfo.get("width").getAsInt();
						f.height = originalInfo.get("height").getAsInt();
						listSizes.add(f);

						sizes.asMap().forEach((s, element) -> {
							if (!(element instanceof JsonObject b)) return;

							UserMedia.PhotoSize e1 = GsonUtil.createObject(gson, UserMedia.PhotoSize.class);
							e1.name = s;
							e1.width = b.get("w").getAsInt();
							e1.height = b.get("h").getAsInt();
							listSizes.add(e1);
						});
						e.sizes = listSizes.toArray(new UserMedia.PhotoSize[0]);

						photos.add(e);

						String id = mediaUrlHttps.substring(mediaUrlHttps.lastIndexOf('/') + 1);
						int dotIndex = id.indexOf('.');
						int paramIndex = id.indexOf('&');
						String format = paramIndex != -1 ? id.substring(dotIndex + 1, paramIndex) : id.substring(dotIndex + 1);
						id = id.substring(0, dotIndex);

						e.url = url;
						e.id = id;
						e.format = format;
						continue;
					}

					if (!type.equals("video") && !type.equals("animated_gif")) {
						System.out.println("Unsupported media type: " + type);
						continue;
					}

					UserMedia.Video video = GsonUtil.createObject(gson, UserMedia.Video.class);
					video.thumbnail = mediaUrlHttps;
					JsonArray variants = helper.query("video_info", "variants").getAsJsonArray();

					JsonObject best = null;
					int bestBitrate = 0;
					for (JsonElement element : variants) {
						// Prefer mp4 over another type
						if (!helper.set(element).next("content_type").string().equals("video/mp4")) {
							continue;
						}

						// Select best bitrate
						int bitrate = element.getAsJsonObject().get("bitrate").getAsInt();
						if (best == null || (bestBitrate < bitrate)) {
							bestBitrate = bitrate;
							best = (JsonObject) element;
						}
					}

					if (best == null) {
						best = helper.set(variants).get(0).object();
					}

					helper.set(best);
					video.url = url;
					video.contentType = helper.string("content_type");

					// Add video to the corresponding list
					if (type.equals("video")) {
						JsonElement md = obj.get("mediaStats");
						if (md != null) {
							video.viewCount = helper.set(md).next("viewCount").integer();
						} else {
							video.viewCount = -1;
						}

						videos.add(video);
					} else {
						video.viewCount = -1;
						animatedGif.add(video);
					}
				}
			}
		}

		// Replace "t.co" URLs with original urls.
		JsonObject entities = legacy.get("entities").getAsJsonObject();
		if (entities != null) {
			tweet.entities = TweetEntities.fromJson(entities);

			for (Url url : tweet.entities.urls) {
				if (url.expandedUrl != null) fullText = fullText.replace(url.url, url.expandedUrl);
			}
		}

		fullText = fullText.trim();

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

		tweet.media = GsonUtil.createObject(gson, UserMedia.Media.class);
		tweet.media.photos = photos.toArray(new UserMedia.Photo[0]);
		tweet.media.videos = videos.toArray(new UserMedia.Video[0]);
		tweet.media.animatedGif = animatedGif.toArray(new UserMedia.Video[0]);
		tweet.text = fullText;
		tweet.rawText = rawText;

		tweet.user = User.fromJson(gson, helper.query(rootObject, "core", "user_results", "result").getAsJsonObject(), helper);
		helper.set(rootObject);

		tweet.hasBirdwatchNotes = helper.set(rootObject).bool("has_birdwatch_notes", false);
		if (helper.has("birdwatch_pivot")) {
			tweet.birdwatchPivot = BirdwatchPivot.fromJson(helper.object("birdwatch_pivot"), helper);
		}

		JsonElement rt = legacy.get("retweeted_status_result");
		if (rt instanceof JsonObject) {
			JsonObject result = helper.set(rt).next("result").object();
			tweet.retweetTweet = Tweet.fromJson(gson, result, helper, GsonUtil.createObject(gson, Tweet.class));
		}

		JsonElement qs = rootObject.get("quoted_status_result");
		if (qs instanceof JsonObject) {
			if (helper.set(qs).has("result")) {
				JsonObject result = helper.next("result").object();
				tweet.quotedTweet = Tweet.fromJson(gson, result, helper, GsonUtil.createObject(gson, Tweet.class));
			}
		}

		return tweet;
	}

	public static class TweetEntities {
		public List<UserMedia.MediaEntity> media;
		public List<UserMention> userMentions;
		public List<Url> urls;
		public List<Hashtag> hashtags;

		public static TweetEntities fromJson(JsonObject legacy_entities) {
			JsonHelper helper = new JsonHelper(legacy_entities);

			TweetEntities entities = new TweetEntities();

			{
				List<Url> urls = Collections.emptyList();

				for (JsonElement elm : helper.next("urls").array()) {
					if (!(elm instanceof JsonObject o)) {
						continue;
					}

					if (urls == Collections.<Url>emptyList()) {
						urls = new ArrayList<>();
					}

					Url url = Url.fromJson(o, helper);
					urls.add(url);
				}

				entities.urls = urls;
			}

			{
				List<UserMention> userMentions = Collections.emptyList();

				for (JsonElement elm : helper.set(legacy_entities).next("user_mentions").array()) {
					if (userMentions == Collections.<UserMention>emptyList()) {
						userMentions = new ArrayList<>();
					}

					UserMention mention = UserMention.fromJson(elm, helper);

					userMentions.add(mention);
				}

				entities.userMentions = userMentions;
			}

			{
				List<Hashtag> hashtags = Collections.emptyList();

				for (JsonElement elm : helper.set(legacy_entities).next("hashtags").array()) {
					if (hashtags == Collections.<Hashtag>emptyList()) {
						hashtags = new ArrayList<>();
					}

					helper.set(elm);
					Hashtag hashtag = new Hashtag();
					hashtag.range = Range.fromArray(helper.array("indices"));
					hashtag.text = helper.string("text");

					hashtags.add(hashtag);
				}

				entities.hashtags = hashtags;
			}

			b:
			{
				List<UserMedia.MediaEntity> media = Collections.emptyList();
				entities.media = media;
				if (!helper.set(legacy_entities).has("media")) break b;

				for (JsonElement elm : helper.next("media").array()) {
					if (media == Collections.<UserMedia.MediaEntity>emptyList()) {
						media = new ArrayList<>();
					}

					if (!(elm instanceof JsonObject object)) continue;

					UserMedia.MediaEntity e = new UserMedia.MediaEntity();
					e.url = Url.fromJson(object, helper);
					helper.set(object);
					e.id = helper.string("id_str");
					e.mediaKey = helper.string("media_key");
					e.mediaUrlHttps = helper.string("media_url_https");
					e.type = helper.string("type");

					List<UserMedia.PhotoSize> sizesL = new ArrayList<>();

					{
						helper.next("original_info");

						UserMedia.OriginalInfo originalInfo = new UserMedia.OriginalInfo();
						originalInfo.width = helper.integer("width");
						originalInfo.height = helper.integer("height");

						List<UserMedia.FocusRect> focusRects = new ArrayList<>();

						for (JsonElement r : helper.array("focus_rects")) {
							if (!(r instanceof JsonObject v)) continue;

							helper.set(v);
							UserMedia.FocusRect rect = new UserMedia.FocusRect();
							rect.x = helper.integer("x");
							rect.y = helper.integer("y");
							rect.width = helper.integer("w");
							rect.height = helper.integer("h");
							focusRects.add(rect);
						}

						originalInfo.focusRects = focusRects.toArray(new UserMedia.FocusRect[0]);
						e.originalInfo = originalInfo;
					}

					helper.set(object);
					JsonObject sizes = helper.object("sizes");
					for (Map.Entry<String, JsonElement> o : sizes.entrySet()) {
						UserMedia.PhotoSize s = new UserMedia.PhotoSize();
						s.name = o.getKey();
						helper.set(o.getValue());
						s.width = helper.integer("w");
						s.height = helper.integer("h");
						s.resize = helper.string("resize", "fit");
						sizesL.add(s);
					}

					helper.set(object);
					if (helper.has("video_info")) {
						helper.next("video_info");
						UserMedia.VideoInfo videoInfo = new UserMedia.VideoInfo();

						videoInfo.aspectRatio = helper.intArray("aspect_ratio");
						videoInfo.durationMillis = helper.integer("duration_millis", -1);

						List<UserMedia.Variant> variants = new ArrayList<>();
						for (JsonElement variant : helper.array("variants")) {
							helper.set(variant);

							UserMedia.Variant v = new UserMedia.Variant();
							v.bitrate = helper.integer("bitrate", 0);
							v.contentType = helper.string("content_type");
							v.url = helper.string("url");
							variants.add(v);
						}
						videoInfo.variants = variants.toArray(new UserMedia.Variant[0]);

						e.videoInfo = videoInfo;
					}

					e.sizes = sizesL.toArray(new UserMedia.PhotoSize[0]);

					media.add(e);
				}

				entities.media = media;
			}

			return entities;
		}
	}
}
