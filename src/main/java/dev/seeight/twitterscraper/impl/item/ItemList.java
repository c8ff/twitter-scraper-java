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

package dev.seeight.twitterscraper.impl.item;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.impl.Entry;
import dev.seeight.twitterscraper.impl.user.User;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ItemList extends ArrayList<Entry> {
	@Nullable
	public <T extends Entry> T findLast(@NotNull Class<T> c) {
		for (int i = this.size() - 1; i >= 0; i--) {
			Entry entry = this.get(i);
			if (entry.getClass().isAssignableFrom(c)) {
				//noinspection unchecked
				return (T) entry;
			}
		}

		return null;
	}

	@Nullable
	public <T extends Entry> T findFirst(@NotNull Class<T> c) {
		for (Entry entry : this) {
			if (entry.getClass().isAssignableFrom(c)) {
				//noinspection unchecked
				return (T) entry;
			}
		}

		return null;
	}

	public static ItemList fromJson(Gson gson, JsonArray entries) {
		ItemList list = new ItemList();

		JsonHelper helper = new JsonHelper(entries);

		for (JsonElement _0 : entries) {
			if (!(_0 instanceof JsonObject entry)) continue;

			helper.set(entry);
			if (helper.stringOrDefault("entryId", "").startsWith("user")) {
				// Parse entry as user.

				User u = User.fromJson(gson, helper.next("content").next("itemContent").next("user_results").next("result").object(), helper);
				list.add(u);
			} else if (helper.stringOrDefault("entryId", "").startsWith("cursor")) {
				// Parse entry as bottom cursor.

				Cursor c = Cursor.fromEntryContent(helper.next("content").object(), helper);
				list.add(c);
			}
		}

		return list;
	}
}
