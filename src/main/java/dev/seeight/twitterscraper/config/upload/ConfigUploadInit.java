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
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.TwitterException;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.upload.PartiallyUploadedMedia;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigUploadInit implements IConfigJsonTree<PartiallyUploadedMedia> {
	public final Command command = Command.INIT;
	public final int totalBytes;
	/**
	 * Must be a MIME type. For example, jpg images use "image/jpeg".
	 */
	public final String mediaType;
	public final MediaCategory mediaCategory;
	public double videoDurationMs = 0; // TODO: It works without this, although an original request does include it.

	public ConfigUploadInit(int totalBytes, String mediaType, MediaCategory mediaCategory) {
		this.totalBytes = totalBytes;
		this.mediaType = mediaType;
		this.mediaCategory = mediaCategory;
	}

	@Override
	public PartiallyUploadedMedia resolve(OkHttpClient client, Request request, Gson gson) throws IOException, TwitterException {
        var b = request.newBuilder();
        stripHeaders(b);
		return IConfigJsonTree.super.resolve(client, b.build(), gson);
	}

	@Override
	public PartiallyUploadedMedia fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		return gson.fromJson(element, PartiallyUploadedMedia.class);
	}

	@Override
	public Request.Builder createRequest(Gson gson, HttpUrl url, TwitterApi api) throws URISyntaxException {
        return new Request.Builder().url(url).post(RequestBody.EMPTY);
	}

	@Override
	public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
        return HttpUrl.get("https://upload.x.com/i/media/upload.json").newBuilder()
                .addQueryParameter("command", this.command.name())
                .addQueryParameter("total_bytes", String.valueOf(this.totalBytes))
                .addQueryParameter("media_type", this.mediaType)
                .addQueryParameter("media_category", this.mediaCategory.str).build();
	}

	public static void stripHeaders(Request.Builder request) {
		// request.removeHeaders("cookie");
		request.removeHeader("X-Client-Transaction-Id");
		// request.removeHeaders("authorization");
		request.removeHeader("x-twitter-active-user");
		request.removeHeader("x-twitter-client-language");
		request.removeHeader("TE");
		request.header("Referer", "https://x.com/");
		request.header("Sec-Fetch-Dest", "empty");
		request.header("Sec-Fetch-Mode", "cors");
		request.header("Sec-Fetch-Site", "same-site");
	}
}
