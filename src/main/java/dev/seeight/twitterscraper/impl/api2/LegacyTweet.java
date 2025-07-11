package dev.seeight.twitterscraper.impl.api2;

import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.impl.Entry;
import dev.seeight.twitterscraper.impl.Range;
import dev.seeight.twitterscraper.impl.Tweet;
import dev.seeight.twitterscraper.util.JsonHelper;

public class LegacyTweet extends Entry {
	public String createdAt;
	public String id;
	public String fullText;
	public boolean truncated;
	public Range displayTextRange;
	public Tweet.TweetEntities entities;
	public Tweet.TweetEntities extendedEntities;
	public String source;
	public String replyingToStatusId;
	public String replyingToUserId;
	public String replyingToUserHandle;
	public String userId;
	public boolean isQuoteStatus;
	public String quotedStatusId;
	public String retweetedStatusId;
	public int retweetCount;
	public int favoriteCount;
	public int replyCount;
	public int quoteCount;
	public boolean conversationMuted;
	public ConversationControl conversationControl;
	public boolean favorited;
	public boolean retweeted;
	public boolean possiblySensitive;
	public String lang;

	public LegacyTweet() {
	}

	public LegacyTweet(String createdAt, String id, String fullText, boolean truncated, Range displayTextRange, Tweet.TweetEntities entities, Tweet.TweetEntities extendedEntities, String source, String replyingToStatusId, String replyingToUserId, String replyingToUserHandle, String userId, boolean isQuoteStatus, String quotedStatusId, String retweetedStatusId, int retweetCount, int favoriteCount, int replyCount, int quoteCount, boolean conversationMuted, ConversationControl conversationControl, boolean favorited, boolean retweeted, boolean possiblySensitive, String lang) {
		this.createdAt = createdAt;
		this.id = id;
		this.fullText = fullText;
		this.truncated = truncated;
		this.displayTextRange = displayTextRange;
		this.entities = entities;
		this.extendedEntities = extendedEntities;
		this.source = source;
		this.replyingToStatusId = replyingToStatusId;
		this.replyingToUserId = replyingToUserId;
		this.replyingToUserHandle = replyingToUserHandle;
		this.userId = userId;
		this.isQuoteStatus = isQuoteStatus;
		this.quotedStatusId = quotedStatusId;
		this.retweetedStatusId = retweetedStatusId;
		this.retweetCount = retweetCount;
		this.favoriteCount = favoriteCount;
		this.replyCount = replyCount;
		this.quoteCount = quoteCount;
		this.conversationMuted = conversationMuted;
		this.conversationControl = conversationControl;
		this.favorited = favorited;
		this.retweeted = retweeted;
		this.possiblySensitive = possiblySensitive;
		this.lang = lang;
	}

	public static LegacyTweet fromJson(JsonHelper h, JsonElement e) {
		return new LegacyTweet(
			h.set(e).string("created_at"),
			h.string("id_str"),
			h.string("full_text"),
			h.bool("truncated", false),
			Range.fromArray(h.array("display_text_range")),
			Tweet.TweetEntities.fromJson(h.object("entities"), h),
			!h.set(e).has("extended_entities") ? null :
				Tweet.TweetEntities.fromJson(h.set(e).object("extended_entities"), h),
			h.set(e).string("source", null),
			h.string("in_reply_to_status_id_str", null),
			h.string("in_reply_to_user_id_str", null),
			h.string("in_reply_to_screen_name", null),
			h.string("user_id_str"),
			h.bool("is_quote_status"),
			h.string("quoted_status_id_str", null),
			h.string("retweeted_status_id_str", null),
			h.integer("retweet_count"),
			h.integer("favorite_count"),
			h.integer("reply_count"),
			h.integer("quote_count"),
			h.bool("conversation_muted", false),
			!h.has("conversation_control") ? null : ConversationControl.fromJson(h, h.object("conversation_control")),
			h.set(e).bool("favorited"),
			h.set(e).bool("retweeted"),
			h.set(e).bool("possibly_sensitive", false),
			h.set(e).string("lang")
		);
	}

	public static class ConversationControl {
		public String policy;
		public String screenName;
		public ConversationControl() {
		}
		public ConversationControl(String policy, String screenName) {
			this.policy = policy;
			this.screenName = screenName;
		}
		public static ConversationControl fromJson(JsonHelper h, JsonElement e) {
			h.set(e);
			return new ConversationControl(
				h.string("policy"),
				h.next("conversation_owner").string("screen_name")
			);
		}
	}
}
