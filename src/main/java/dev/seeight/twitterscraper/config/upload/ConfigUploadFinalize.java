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
import dev.seeight.twitterscraper.impl.upload.UploadedMedia;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigUploadFinalize implements IConfigJsonTree<UploadedMedia> {
	public final Command command = Command.FINALIZE;
	public final String mediaId;
	public final String originalMd5;
	public boolean allowAsync = false;

	public ConfigUploadFinalize(String mediaId, String originalMd5) {
		this.mediaId = mediaId;
		this.originalMd5 = originalMd5;
	}

	@Override
	public UploadedMedia resolve(OkHttpClient client, Request request, Gson gson) throws IOException, TwitterException {
        Request.Builder b = request.newBuilder();
        ConfigUploadInit.stripHeaders(b);
		return IConfigJsonTree.super.resolve(client, b.build(), gson);
	}

	@Override
	public UploadedMedia fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		return gson.fromJson(element, UploadedMedia.class);
	}

	@Override
	public Request.Builder createRequest(Gson gson, HttpUrl url, TwitterApi api) throws URISyntaxException {
		return new Request.Builder().url(url).post(RequestBody.EMPTY);
	}

	@Override
	public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
		var b = HttpUrl.get("https://upload.x.com/i/media/upload.json").newBuilder()
                .addQueryParameter("command", this.command.name())
                .addQueryParameter("media_id", this.mediaId)
                .addQueryParameter("original_md5", this.originalMd5);
		if (allowAsync) b.addQueryParameter("allow_async", String.valueOf(true));
		return b.build();
    }
}
