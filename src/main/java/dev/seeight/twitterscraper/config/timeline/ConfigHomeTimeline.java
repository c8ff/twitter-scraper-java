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

package dev.seeight.twitterscraper.config.timeline;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.Timeline;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.config.SendType;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.inst.Instruction;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigHomeTimeline implements IConfigJsonTree<Timeline> {
	public int count = 20;
	public final String cursor;
	public boolean includePromotedContent = true;
	public boolean latestControlAvailable = true;
	public String requestContext;
	public boolean withCommunity = true;

	public transient @NotNull SendType sendType = SendType.GET;

	public ConfigHomeTimeline(String cursor) {
		this.cursor = cursor;
		if (cursor == null) {
			requestContext = "launch";
		}
	}

	@Override
	public Timeline fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		Timeline tl = new Timeline() {
		};

		JsonHelper h = new JsonHelper(element);
		h.next("data").next("home").next("home_timeline_urt").next("instructions");
		tl.instructions = Instruction.fromInstructionsJson(gson, h, h.array());

		return tl;
	}

	@Override
	public String getBaseURL(GraphQLMap graphQL) {
		return graphQL.get("HomeTimeline").url;
	}

	@Override
	public HttpUriRequestBase createRequest(Gson gson, URI uri, GraphQLMap graphQL) throws URISyntaxException {
		// No method body for GET request.
		if (sendType == SendType.GET) {
			return IConfigJsonTree.super.createRequest(gson, uri, graphQL);
		}

		GraphQLMap.Entry e = graphQL.get("HomeTimeline");

		String variables = gson.toJson(this);
		String features = gson.toJson(e.features);
		String queryId = e.queryId;
		return TwitterApi.newJsonPostRequest(uri, "{\"variables\":" + variables + ",\"features\":" + features + ",\"queryId\":\"" + queryId + "\"}");
	}

	@Override
	public URI buildURI(Gson gson, URIBuilder builder, GraphQLMap graphQL) throws URISyntaxException {
		if (sendType == SendType.POST) return builder.build();

		return builder
			.addParameter("variables", gson.toJson(this))
			.addParameter("features", gson.toJson(graphQL.get("HomeTimeline").features))
			.build();
	}
}
