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

package dev.seeight.twitterscraper.config.user;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.TwitterList;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigListByRestId implements IConfigJsonTree<TwitterList> {
	public final @NotNull String listId;

	public ConfigListByRestId(@NotNull String listId) {
		this.listId = listId;
	}

	@Override
	public TwitterList fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		JsonHelper h = new JsonHelper(element);
		return TwitterList.fromJson(gson, h.next("data").next("list").object(), h);
	}

	@Override
	public String getBaseURL(GraphQLMap graphQL) {
		return graphQL.get("ListByRestId").url;
	}

	@Override
	public URI buildURI(Gson gson, URIBuilder builder, GraphQLMap graphQL) throws URISyntaxException {
		return builder
			.addParameter("variables", gson.toJson(this))
			.addParameter("features", gson.toJson(graphQL.get("ListByRestId").features))
			.build();
	}
}
