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
import dev.seeight.twitterscraper.impl.item.Cursor;
import dev.seeight.twitterscraper.impl.item.TimelineModule;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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

		if (itemType.equals("TimelineTweet")) {
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
		} else if (itemType.equals("TimelineTimelineCursor")) {
			Cursor e = Cursor.fromEntryContent(itemContent, h);
			e.entryId = entryId;
			e.sortIndex = sortIndex;
			return e;
		} else {
			System.out.println("Entry: unknown item type: " + itemType + ", entryId: " + entryId);
			return null;
		}
	}

	public static Entry parseEntry(Gson gson, JsonHelper h, JsonObject entry) {
		h.set(entry);

		String entryId = h.string("entryId");
		String sortIndex = h.stringOrDefault("sortIndex", null);

		if (!h.has("content")) return null;
		h.next("content");

		String entryType = h.string("entryType");

		switch (entryType) {
			case "TimelineTimelineItem" -> {
				return parseTimelineItem(entryId, sortIndex, gson, h, h.object());
			}
			case "TimelineTimelineModule" -> {
				Entry e = parseTimelineModule(gson, h, h.object());
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

	private static Entry parseTimelineModule(Gson gson, JsonHelper h, JsonObject object) {
		JsonArray items = h.set(object).next("items").array();

		List<Entry> entries = new ArrayList<>();
		for (JsonElement item : items) {
			if (!(item instanceof JsonObject io)) continue;

			h.set(io);
			String entryId = h.string("entryId");
			JsonObject item0 = h.object("item");
			var e = parseTimelineItem(entryId, null, gson, h, item0);
			if (e != null) {
				entries.add(e);
			}
		}

		TimelineModule t = new TimelineModule();
		t.items = entries;
		t.displayType = h.set(object).string("displayType", null);
		return t;
	}
}
