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

package dev.seeight.twitterscraper.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigLastSeenCursor implements IConfigJsonTree<ConfigLastSeenCursor.OnlyCursor> {
	public final String cursor;

	public ConfigLastSeenCursor(String cursor) {
		this.cursor = cursor;
	}

	@Override
	public OnlyCursor fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		return OnlyCursor.fromJson(element, new JsonHelper(element));
	}

	@Override
	public HttpUriRequestBase createRequest(Gson gson, URI uri, GraphQLMap graphQL) throws URISyntaxException {
		HttpPost req = new HttpPost(uri);
		StringEntity entity = new StringEntity("cursor=" + cursor);
		req.setEntity(entity);
		req.addHeader("content-type", "application/x-www-form-urlencoded");
		return req;
	}

	@Override
	public String getBaseURL(GraphQLMap graphQL) {
		return "https://x.com/i/api/2/notifications/all/last_seen_cursor.json";
	}

	public static class OnlyCursor {
		public String cursor;

		public OnlyCursor() {
		}

		public OnlyCursor(String cursor) {
			this.cursor = cursor;
		}

		public static OnlyCursor fromJson(JsonElement element, JsonHelper helper) {
			return new OnlyCursor(helper.set(element).string("cursor"));
		}
	}
}
