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
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.TwitterException;
import okhttp3.*;

import java.io.IOException;
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
    public Request.Builder createRequest(Gson gson, HttpUrl url, TwitterApi api) throws URISyntaxException {
        return new Request.Builder().url(url)
                .post(new MultipartBody.Builder("----")
                        .addFormDataPart("media", null, RequestBody.create(this.mediaBytes, MediaType.get("application/octet-stream")))
                        .build());
    }

    @Override
    public Void resolve(OkHttpClient client, Request request, Gson gson) throws IOException, TwitterException {
        var b = request.newBuilder();
        ConfigUploadInit.stripHeaders(b);
        try (var response = client.newCall(b.build()).execute()) {
            if (response.code() != 200 && response.code() != 204) {
                // uuhh
                throw new AssertionError();
            }
            return null;
        }
    }

    @Override
    public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
        return HttpUrl.get("https://upload.x.com/i/media/upload.json").newBuilder()
                .addQueryParameter("command", this.command.name())
                .addQueryParameter("media_id", this.mediaId)
                .addQueryParameter("segment_index", String.valueOf(segmentIndex))
                .build();
    }
}
