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
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.TwitterList;
import dev.seeight.twitterscraper.impl.item.Cursor;
import dev.seeight.twitterscraper.impl.item.TimelineModule;
import dev.seeight.twitterscraper.impl.item.NotificationF;
import dev.seeight.twitterscraper.impl.user.User;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

// TODO: Separate entry from items. (An entry is a wrapper, can contain an item, or various items.) (an item is a user, tweet, etc.)
public class Entry {
	public String entryId;
	@Nullable
	public String sortIndex;

	public static Entry parseTimelineItem(String entryId, String sortIndex, Gson gson, JsonHelper h, JsonObject entry) {
		h.set(entry);
		if (!h.has("itemContent")) return null;
		h.next("itemContent");
		if (!h.has("itemType")) return null;

		JsonObject itemContent = h.object();
		String itemType = h.string("itemType");

		switch (itemType) {
			case "TimelineNotification" -> {
				var e = NotificationF.fromJson(h, itemContent);
				e.entryId = entryId;
				e.sortIndex = sortIndex;
				return e;
			}
			case "TimelineTweet" -> {
				// Omit ads.
				if (entryId.contains("promoted-tweet") || h.has("promotedMetadata")) {
					return null;
				}

				h.next("tweet_results");
				if (!h.has("result"))
					return null;

				Tweet e = Tweet.fromJson(gson, h.object("result"), h);
				e._tweetDisplayType = h.set(itemContent).string("tweetDisplayType", "Tweet");
				e.entryId = entryId;
				e.sortIndex = sortIndex;
				return e;
			}
			case "TimelineTimelineCursor" -> {
				Cursor e = Cursor.fromEntryContent(itemContent, h);
				e.entryId = entryId;
				e.sortIndex = sortIndex;
				return e;
			}
			case "TimelineTwitterList" -> {
				TwitterList e = TwitterList.fromJson(gson, h.object("list"), h);
				e.entryId = entryId;
				e.sortIndex = sortIndex;
				return e;
			}
			case "TimelineUser" -> {
				h.next("user_results");
				if (!h.has("result"))
					return null;

				var e = User.fromJson(gson, h.object("result"), h);
				e.entryId = entryId;
				e.sortIndex = sortIndex;
				return e;
			}
			default -> {
				System.out.println("Entry: unknown item type: " + itemType + ", entryId: " + entryId);
				return null;
			}
		}
	}

	public static Entry parseEntry(Gson gson, JsonHelper h, JsonObject entry) {
		h.set(entry);

		String entryId = h.string("entryId");
		String sortIndex = h.stringOrDefault("sortIndex", null);

		if (entryId.startsWith("tweetdetailrelatedtweets")) {
			return null;
		}

		if (!h.has("content")) return null;
		h.next("content");

		String entryType = h.string("entryType");

		switch (entryType) {
			case "TimelineTimelineItem" -> {
				return parseTimelineItem(entryId, sortIndex, gson, h, h.object());
			}
			case "TimelineTimelineModule" -> {
				Entry e = TimelineModule.fromJson(gson, h, h.object());
				e.entryId = entryId;
				e.sortIndex = sortIndex;
				return e;
			}
			case "TimelineTimelineCursor" -> {
				Cursor e = Cursor.fromEntryContent(h.object(), h);
				e.entryId = entryId;
				e.sortIndex = sortIndex;
				return e;
			}
			default -> {
				System.out.println("Unknown entryType: " + entryType);
				return null;
			}
		}
	}

}
