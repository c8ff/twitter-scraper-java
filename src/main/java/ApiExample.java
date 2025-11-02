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

import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.config.timeline.ConfigSearchTimeline;
import dev.seeight.twitterscraper.config.user.ConfigUserByScreenName;
import dev.seeight.twitterscraper.config.user.ConfigUserMedia;
import dev.seeight.twitterscraper.impl.timeline.SearchByRawQuery;
import dev.seeight.twitterscraper.impl.user.User;
import dev.seeight.twitterscraper.impl.user.UserMedia;
import dev.seeight.twitterscraper.util.JsonUtil;
import okhttp3.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class ApiExample {
	public static void main(String[] args) {
		// Create an api instance
		TwitterApi api = TwitterApi.newTwitterApi();
		// Set the authorization stuff
		api.bearerAuthorization = "Bearer <token>";
		api.cookie = "<cookies>";
		api.userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/111.0";
		api.csrfToken = "<csrf_token>";

        var client = new OkHttpClient.Builder().build();
		try {
			// Get the user id (required for some requests)
			User user = api.scrap(new ConfigUserByScreenName("x"), client);
			// Request the media tab of the user (using the returned id) and parse the results
			UserMedia userMedia = api.scrap(new ConfigUserMedia(user.restId, null), client);
			// Print the results
			System.out.println(userMedia);
			// Save the results to a file.
			Files.writeString(new File("user-media.json").toPath(), JsonUtil.toJson(userMedia));

			// Similar as before, but this sends a search request.
			ConfigSearchTimeline config = new ConfigSearchTimeline("(from:x) -filter:replies", null);
			config.count = 4;
			// Send the request.
			SearchByRawQuery result = api.scrap(config, client);
			// Print results.
			System.out.println(result);
			// Save results.
			Files.writeString(new File("search-result.json").toPath(), JsonUtil.toJson(result));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
}