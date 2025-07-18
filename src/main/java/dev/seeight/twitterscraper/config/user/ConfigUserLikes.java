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
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.user.UserLikes;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigUserLikes implements IConfigJsonTree<UserLikes> {
	public final String userId;
	public int count = 40;
	public final String cursor;
	public boolean includePromotedContent = false;
	public boolean withQuickPromoteEligibilityTweetFields = false;
	public boolean withVoice = true;

	public ConfigUserLikes(String userId, String cursor) {
		this.userId = userId;
		this.cursor = cursor;
	}

	@Override
	public String getBaseURL(GraphQLMap graphQL) {
		return graphQL.get("Likes").url;
	}

	@Override
	public URI buildURI(Gson gson, URIBuilder builder, GraphQLMap graphQL) throws URISyntaxException {
		return builder
			.addParameter("variables", gson.toJson(this))
			.addParameter("features", gson.toJson(graphQL.get("Likes").features))
			.addParameter("fieldToggles", "{\"withArticlePlainText\":false}")
			.build();
	}

	@Override
	public UserLikes fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		return UserLikes.fromJson(gson, element);
	}
}
