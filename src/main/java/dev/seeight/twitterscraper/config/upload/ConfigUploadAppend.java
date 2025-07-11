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
import dev.seeight.twitterscraper.IConfig;
import dev.seeight.twitterscraper.TwitterException;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.AbstractHttpClientResponseHandler;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ConfigUploadAppend implements IConfig<Void> {
	public final Command command = Command.APPEND;
	public final byte[] mediaBytes;
	public final String mediaId;
	public final int segmentIndex;

	public ConfigUploadAppend(byte[] mediaBytes, String mediaId, int segmentIndex) {
		this.mediaBytes = mediaBytes;
		this.mediaId = mediaId;
		this.segmentIndex = segmentIndex;
	}

	@Override
	public HttpUriRequestBase createRequest(Gson gson, URI uri, GraphQLMap graphQL) throws URISyntaxException {
		HttpPost p = new HttpPost(uri);
		p.setEntity(
			MultipartEntityBuilder.create()
				.setContentType(ContentType.MULTIPART_FORM_DATA)
				.setBoundary("----")
				.addBinaryBody("media", this.mediaBytes, ContentType.APPLICATION_OCTET_STREAM, "blob")
			.build()
		);
		return p;
	}

	@Override
	public Void resolve(HttpClient client, HttpUriRequestBase request, Gson gson) throws IOException, TwitterException {
		ConfigUploadInit.stripHeaders(request);

		client.execute(request, new AbstractHttpClientResponseHandler<Void>() {
			@Override
			public Void handleEntity(HttpEntity entity) {
				return null;
			}
		});
		return null;
	}

	@Override
	public URI buildURI(Gson gson, URIBuilder builder, GraphQLMap graphQL) throws URISyntaxException {
		return builder
			.addParameter("command", this.command.name())
			.addParameter("media_id", mediaId)
			.addParameter("segment_index", String.valueOf(segmentIndex))
			.build();
	}

	@Override
	public String getBaseURL(GraphQLMap graphQL) {
		return "https://upload.x.com/i/media/upload.json";
	}
}
