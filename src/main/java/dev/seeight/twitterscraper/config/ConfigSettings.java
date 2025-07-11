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
import com.google.gson.JsonParser;
import dev.seeight.twitterscraper.IConfig;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.TwitterException;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.Settings;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class ConfigSettings implements IConfig<Settings> {
	@Override
	public String getBaseURL(GraphQLMap graphQL) {
		return "https://api.x.com/1.1/account/settings.json";
	}

	@Override
	public URI buildURI(Gson gson, URIBuilder builder, GraphQLMap graphQL) throws URISyntaxException {
		return builder
			.addParameter("include_ext_sharing_audiospaces_listening_data_with_followers", "true")
			.addParameter("include_mention_filter", "true")
			.addParameter("include_nsfw_user_flag", "true")
			.addParameter("include_nsfw_admin_flag", "true")
			.addParameter("include_ranked_timeline", "true")
			.addParameter("include_alt_text_compose", "true")
			.addParameter("ext", "ssoConnections")
			.addParameter("include_country_code", "true")
			.addParameter("include_ext_dm_nsfw_media_filter", "true")
			.build();
	}

	@Override
	public Settings resolve(HttpClient client, HttpUriRequestBase request, Gson gson) throws IOException, TwitterException {
		String s = TwitterApi.executeString(client, request);
		return gson.fromJson(IConfigJsonTree.assertErrors(JsonParser.parseString(s), request, null), Settings.class);
	}
}
