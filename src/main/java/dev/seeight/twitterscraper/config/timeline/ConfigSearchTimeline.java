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

package dev.seeight.twitterscraper.config.timeline;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.timeline.SearchByRawQuery;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

public class ConfigSearchTimeline implements IConfigJsonTree<SearchByRawQuery> {
	public final String rawQuery;
	public int count = 20;
	public final String cursor;
	public String querySource = "typed_query";
	public ProductType product = ProductType.Top;

	public ConfigSearchTimeline(String rawQuery, String cursor) {
		this.rawQuery = rawQuery;
		this.cursor = cursor;
	}

	@Override
	public String getBaseURL(GraphQLMap graphQL) {
		return graphQL.get("SearchTimeline").url;
	}

	@Override
	public URI buildURI(Gson gson, URIBuilder builder, GraphQLMap graphQL) throws URISyntaxException {
		return builder
			.addParameter("variables", gson.toJson(this))
			.addParameter("features", gson.toJson(graphQL.get("SearchTimeline").features))
			.build();
	}

	@Override
	public SearchByRawQuery fromJson(JsonElement element, Gson gson) {
		return SearchByRawQuery.fromJson(gson, new JsonHelper(element), element);
	}

	public enum ProductType {
		Top,
		Latest,
		People,
		Media,
		Lists
	}
}
