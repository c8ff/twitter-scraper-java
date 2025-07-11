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

package dev.seeight.twitterscraper.impl.api2;

import dev.seeight.twitterscraper.Timeline;
import dev.seeight.twitterscraper.impl.Tweet;
import dev.seeight.twitterscraper.impl.Url;
import dev.seeight.twitterscraper.impl.api2.instruction.LegacyEntry;
import dev.seeight.twitterscraper.impl.api2.instruction.LegacyInstructionAddEntries;
import dev.seeight.twitterscraper.impl.inst.TimelineAddEntries;
import dev.seeight.twitterscraper.impl.item.Cursor;
import dev.seeight.twitterscraper.impl.user.ProfileImageShape;
import dev.seeight.twitterscraper.impl.user.User;
import dev.seeight.util.ListUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Note: entryIds and sortIndexes are not defined in {@link #toUser(LegacyUser)} and {@link #toTweet(LegacyTweet)} by default.
 */
public class Api2Util {
    public static User toUser(LegacyUser l) {
        var user = new User();

        user.restId = l.id_str;
        user.createdAt = l.created_at;
        user.rawDescription = l.description;
        user.description = l.description;
        user.descriptionUrls = Collections.emptyList();
        user.url = new Url();
        user.url.url = l.url;
        user.url.expandedUrl = l.url;
        user.url.displayUrl = l.url;
        user.likedTweetsCount = l.favourites_count;
        user.followersCount = l.followers_count;
        user.followingCount = l.friends_count;
        user.friendsCount = l.friends_count;
        user.name = l.name;
        user.screenName = l.screen_name;
        user.location = l.location;
        user.pinnedTweetsIds = ListUtil.mapArray(l.pinned_tweet_ids, new String[0], String::valueOf);
        user.verified = l.verified;
        user.blueVerified = l.ext_is_blue_verified;

        user.profileImageUrl = l.profile_image_url;
        if (user.profileImageUrl != null) user.profileImageUrl = user.profileImageUrl.replace("http://", "https://"); // sekuriti
        user.profileBannerUrl = l.profile_banner_url;
        if (user.profileBannerUrl != null) user.profileBannerUrl = user.profileBannerUrl.replace("http://", "https://");
        user.profileImageShape = ProfileImageShape.CIRCLE;
        user.canDM = l.can_dm;
        user.canMediaTag = l.can_media_tag;
        user.following = l.following;
        user.followedBy = l.followed_by;
        user.blocking = l.blocking;
        user.blockedBy = l.blocked_by;
        user.muting = l.muting;
        user.notifications = l.notifications;
        user.wantRetweets = l.want_retweets;

        return user;
    }

    public static Tweet toTweet(LegacyTweet t) {
        var tweet = new Tweet();
        tweet.entryId = t.entryId;

        tweet.bookmarks = -1;
        tweet.quotes = t.quoteCount;
        tweet.likes = t.favoriteCount;
        tweet.replies = t.replyCount;
        tweet.retweets = t.retweetCount;
        tweet.createdAt = t.createdAt;
        tweet.id = t.id;
        tweet.text = t.fullText;
        tweet.rawText = t.fullText;
        tweet.language = t.lang;
        tweet.inReplyToScreenName = t.replyingToUserHandle;
        tweet.inReplyToUserIdStr = t.replyingToUserId;
        tweet.inReplyToStatusIdStr = t.replyingToStatusId;
        tweet.isQuoteStatus = t.isQuoteStatus;
        tweet.possiblySensitive = t.possiblySensitive;
        tweet.source = t.source;
        tweet.liked = t.favorited;
        tweet.displayTextRange = t.displayTextRange;

        tweet.entities = new Tweet.TweetEntities();
        if (t.extendedEntities != null) tweet.entities.media = t.extendedEntities.media;
        tweet.entities.urls = t.entities.urls;
        tweet.entities.hashtags = t.entities.hashtags;
        tweet.entities.userMentions = t.entities.userMentions;

        tweet.sortIndex = t.sortIndex;
        return tweet;
    }

    public static Cursor toCursor(LegacyCursorItem t) {
        Cursor c = new Cursor();
        c.cursorType = t.type;
        c.value = t.value;
        c.displayTreatment = new Cursor.DisplayTreatment("Load more", "Load more"); // TODO: What
        return c;
    }

    public static ConvertedTimeline convertToModernTimeline(GlobalObjects globalObjects, LegacyTimeline tl) {
        var convertedUsers = new HashMap<String, User>();
        for (var e : globalObjects.users.entrySet()) {
            convertedUsers.put(e.getKey(), toUser(e.getValue()));
        }

        var timeline = new ConvertedTimeline();
        timeline.instructions = new ArrayList<>();
        for (var instruction : tl.instructions) {
            if (instruction instanceof LegacyInstructionAddEntries z) {
                var addEntries = new TimelineAddEntries();
                addEntries.entries = new ArrayList<>();

                for (LegacyEntry entry : z.entries) {
                    if (entry.item instanceof LegacyTweetItem a) {
                        var t = globalObjects.tweets.get(a.id);
                        var c = toTweet(t);
                        c.user = convertedUsers.get(t.userId);
                        c.entryId = entry.entryId;
                        c.sortIndex = entry.sortIndex;

                        if (t.retweetedStatusId != null) {
                            var b = globalObjects.tweets.get(t.retweetedStatusId);
                            c.retweetTweet = toTweet(b);
                            c.retweetTweet.user = convertedUsers.get(b.userId);
                            c.retweetTweet.entryId = "tweet-" + b.id;
                            c.retweetTweet.sortIndex = entry.sortIndex;
                        }
                        if (t.quotedStatusId != null) {
                            var b = globalObjects.tweets.get(t.quotedStatusId);
                            c.quotedTweet = toTweet(b);
                            c.quotedTweet.user = convertedUsers.get(b.userId);
                            c.quotedTweet.entryId = "tweet-" + b.id;
                            c.quotedTweet.sortIndex = entry.sortIndex;
                        }

                        addEntries.entries.add(c);
                    } else if (entry.item instanceof LegacyCursorItem a) {
                        Cursor c = toCursor(a);
                        c.entryId = entry.entryId;
                        c.sortIndex = entry.sortIndex;
                        addEntries.entries.add(c);
                    }
                }

                timeline.instructions.add(addEntries);
            }
        }
        return timeline;
    }

    public static class ConvertedTimeline extends Timeline {
    }
}
