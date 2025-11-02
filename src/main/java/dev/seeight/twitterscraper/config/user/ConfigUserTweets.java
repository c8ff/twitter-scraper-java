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
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.user.UserTweets;
import okhttp3.HttpUrl;

import java.net.URISyntaxException;
import java.util.List;

public class ConfigUserTweets implements IConfigJsonTree<UserTweets> {
	public final String userId;
	public int count = 40;
	public final String cursor;
	public boolean includePromotedContent = true;
	public boolean withQuickPromoteEligibilityTweetFields = true;
	public boolean withVoice = true;

	public ConfigUserTweets(String userId) {
		this(userId, null);
	}

	public ConfigUserTweets(String userId, String cursor) {
		this.userId = userId;
		this.cursor = cursor;
	}

	@Override
	public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
		var op = api.getGraphQLOperation("UserTweets");
		return op.getUrl(gson.toJson(this));
	}

	@Override
	public UserTweets fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		return UserTweets.fromRawJson(gson, element);
	}
}
