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
import dev.seeight.twitterscraper.util.GsonUtil;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class TweetConversation extends Tweet {
	public Entry[] entries;

	public static TweetConversation fromEntryJson(Gson gson, JsonElement element) {
		JsonHelper h = new JsonHelper(element);

		String entryId = h.string("entryId");

		if (!entryId.startsWith("conversationthread") && !entryId.startsWith("home-conversation") && !entryId.startsWith("profile-conversation")) {
			throw new IllegalArgumentException("Entry type is not 'profile-conversation'.");
		}

		JsonArray items = h.next("content").array("items");
		return fromJson(gson, items, h, entryId);
	}

	@NotNull
	private static TweetConversation fromJson(Gson gson, JsonArray items, JsonHelper h, String entryId) {
		List<Entry> entries = new ArrayList<>();
		TweetConversation c = GsonUtil.createObject(gson, TweetConversation.class);

		boolean first = false;

		for (JsonElement item : items) {
			h.set(item);

			String origEntryId = h.string("entryId");

			if (!h.has("item")) continue;
			if (!h.next("item").has("itemContent")) continue;
			if (!h.next("itemContent").has("itemType")) continue;
			String itemType = h.string("itemType");

			if (itemType.equals("TimelineTimelineCursor")) {
				String cursorType = h.string("cursorType");
				if (!cursorType.equals("ShowMore")) {
					System.out.println("unknown cursor type: " + cursorType);
					continue;
				}

				entries.add(ShowMore.fromJson0(gson, h.object(), h));
				continue;
			}

			if (!itemType.equals("TimelineTweet")) {
				System.out.println("Unknown itemType: " + itemType + ", entryId: " + origEntryId);
				continue;
			}

			JsonObject tweetJson = h.query("tweet_results", "result").getAsJsonObject();

			if (!first) {
				Tweet.fromJson(gson, tweetJson, h, c);
				first = true;
				continue;
			}

			Tweet e = Tweet.fromJson(gson, tweetJson, h, GsonUtil.createObject(gson, Tweet.class));
			entries.add(e);
		}

		c.entries = entries.toArray(new Entry[0]);
		return c;
	}
}
