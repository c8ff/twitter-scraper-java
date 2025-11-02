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

package dev.seeight.twitterscraper.config.interaction;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.user.User;
import dev.seeight.twitterscraper.util.JsonHelper;
import okhttp3.*;

import java.net.URISyntaxException;
import java.util.List;

public class ConfigBlockCreate implements IConfigJsonTree<User> {
	private final String userId;

	public ConfigBlockCreate(String userId) {
		this.userId = userId;
	}

	@Override
	public User fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		return User.fromJson(gson, (JsonObject) element, new JsonHelper(element));
	}

	@Override
	public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
		return HttpUrl.get("https://x.com/i/api/1.1/blocks/create.json");
	}

	@Override
	public Request.Builder createRequest(Gson gson, HttpUrl url, TwitterApi api) throws URISyntaxException {
		return new Request.Builder()
                .url(url).post(new FormBody.Builder().add("user_id", userId).build());
	}
}
