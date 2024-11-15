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

package dev.seeight.twitterscraper.config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.item.ItemList;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.URISyntaxException;

public class ConfigFollowing implements IConfigJsonTree<ItemList> {
	public final @NotNull String userId;
	public final @Nullable String cursor;
	public int count = 20;
	public boolean includePromotedContent = false;

	public ConfigFollowing(String userId) {
		this(userId, null);
	}

	public ConfigFollowing(@NotNull String userId, @Nullable String cursor) {
		this.userId = userId;
		this.cursor = cursor;
	}

	@Override
	public String getBaseURL(GraphQLMap graphQL) {
		return graphQL.get("Following").url;
	}

	@Override
	public URI buildURI(Gson gson, URIBuilder builder, GraphQLMap graphQL) throws URISyntaxException {
		return builder
			.addParameter("variables", gson.toJson(this))
			.addParameter("features", gson.toJson(graphQL.get("Following").features))
			.build();
	}

	@Override
	public ItemList fromJson(JsonElement element, Gson gson) {
		JsonArray entries = null;

		JsonHelper h = new JsonHelper(element);
		h.next("data")
			.next("user")
			.next("result")
			.next("timeline")
			.next("timeline")
			.next("instructions");

		for (JsonElement _0 : h.array()) {
			if (!(_0 instanceof JsonObject o)) continue;

			if (h.set(o).stringOrDefault("type", "").equals("TimelineAddEntries")) {
				entries = h.next("entries").array();
				break;
			}
		}

		if (entries == null) {
			throw new IllegalStateException("No 'TimelineAddEntries' instruction found.");
		}

		return ItemList.fromJson(gson, entries);
	}
}
