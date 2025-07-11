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

package dev.seeight.twitterscraper.config.upload;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.TwitterException;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.upload.UploadedMedia;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigUploadStatus implements IConfigJsonTree<UploadedMedia> {
	public final Command command = Command.STATUS;
	public final String mediaId;

	public ConfigUploadStatus(String mediaId) {
		this.mediaId = mediaId;
	}

	@Override
	public UploadedMedia resolve(HttpClient client, HttpUriRequestBase request, Gson gson) throws IOException, TwitterException {
		ConfigUploadInit.stripHeaders(request);
		return IConfigJsonTree.super.resolve(client, request, gson);
	}

	@Override
	public UploadedMedia fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		return gson.fromJson(element, UploadedMedia.class);
	}

	@Override
	public URI buildURI(Gson gson, URIBuilder builder, GraphQLMap graphQL) throws URISyntaxException {
		return builder
			.addParameter("command", this.command.name())
			.addParameter("media_id", this.mediaId)
			.build();
	}

	@Override
	public String getBaseURL(GraphQLMap graphQL) {
		return "https://upload.x.com/i/media/upload.json";
	}
}
