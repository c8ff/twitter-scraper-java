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
import dev.seeight.twitterscraper.UserUnavailableException;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.user.User;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigUserByScreenName implements IConfigJsonTree<User> {
	@NotNull
	public final String username;
	public boolean withSafetyModeUserFields = true;

	public ConfigUserByScreenName(@NotNull String username) {
		this.username = username;
	}

	@Override
	public String getBaseURL(GraphQLMap graphQL) {
		return graphQL.get("UserByScreenName").url;
	}

	@Override
	public URI buildURI(Gson gson, URIBuilder builder, GraphQLMap graphQL) throws URISyntaxException {
		return builder
			.addParameter("variables", String.format("{\"screen_name\":\"%s\",\"withSafetyModeUserFields\":%s}", username, withSafetyModeUserFields))
			.addParameter("features", gson.toJson(graphQL.get("UserByScreenName").features))
			.build();
	}

	@Override
	public User fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		JsonHelper helper = new JsonHelper(element).next("data");

		// Check if the user exists.
		if (!helper.has("user")) {
			throw new UserUnavailableException("The user doesn't exist.", "xeno.unknown_user");
		}

		// Navigate into the result
		helper.next("user").next("result");

		// Check if the user is unavailable.
		if (helper.stringOrDefault("__typename", "").equalsIgnoreCase("UserUnavailable")) {
			throw new UserUnavailableException(helper.string("message"), helper.string("reason"));
		}

		// Parse and return the user.
		return User.fromJson(gson, helper.object(), helper);
	}
}
