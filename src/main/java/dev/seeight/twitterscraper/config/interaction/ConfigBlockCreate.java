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

package dev.seeight.twitterscraper.config.interaction;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.user.User;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.net.URI;
import java.net.URISyntaxException;

public class ConfigBlockCreate implements IConfigJsonTree<User> {
	private final String userId;

	public ConfigBlockCreate(String userId) {
		this.userId = userId;
	}

	@Override
	public User fromJson(JsonElement element, Gson gson) {
		return User.fromJson(gson, (JsonObject) element, new JsonHelper(element));
	}

	@Override
	public String getBaseURL(GraphQLMap graphQL) {
		return "https://x.com/i/api/1.1/blocks/create.json";
	}

	@Override
	public HttpUriRequestBase createRequest(Gson gson, URI uri, GraphQLMap graphQL) throws URISyntaxException {
		HttpPost req = new HttpPost(uri);
		StringEntity entity = new StringEntity("user_id=" + userId);
		req.setEntity(entity);
		req.addHeader("content-type", "application/x-www-form-urlencoded");
		return req;
	}
}
