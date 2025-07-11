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
import com.google.gson.annotations.SerializedName;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.timeline.NotificationsTimeline;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigNotificationsTimeline implements IConfigJsonTree<NotificationsTimeline> {
	@SerializedName("timeline_type")
	public TimelineType timelineType = TimelineType.All;
	public final String cursor;
	public int count;

	public ConfigNotificationsTimeline(String cursor) {
		this.cursor = cursor;
        this.count = cursor == null ? 40 : 20;
	}

	@Override
	public String getBaseURL(GraphQLMap graphQL) {
		return graphQL.get("NotificationsTimeline").url;
	}

	@Override
	public URI buildURI(Gson gson, URIBuilder builder, GraphQLMap graphQL) throws URISyntaxException {
		return builder
			.addParameter("variables", gson.toJson(this))
			.addParameter("features", gson.toJson(graphQL.get("HomeLatestTimeline").features))
			.build();
	}

	@Override
	public NotificationsTimeline fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		return NotificationsTimeline.fromJson(gson, element);
	}

	public enum TimelineType {
		All,
		Mentions,
		Verified
	}
}
