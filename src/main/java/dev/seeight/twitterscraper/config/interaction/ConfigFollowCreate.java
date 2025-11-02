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
import dev.seeight.twitterscraper.impl.api2.LegacyUser;
import dev.seeight.twitterscraper.util.JsonHelper;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;

import java.net.URISyntaxException;
import java.util.List;

public class ConfigFollowCreate implements IConfigJsonTree<LegacyUser> {
    private final String userId;

    public ConfigFollowCreate(String userId) {
        this.userId = userId;
    }

    @Override
    public LegacyUser fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
        return LegacyUser.fromJson(new JsonHelper(element), (JsonObject) element);
    }

    @Override
    public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
        return HttpUrl.get("https://x.com/i/api/1.1/friendships/create.json");
    }

    @Override
    public Request.Builder createRequest(Gson gson, HttpUrl url, TwitterApi api) throws URISyntaxException {
        return new Request.Builder().url(url).post(
                new FormBody.Builder()
                        .add("include_profile_interstitial_type", "1")
                        .add("include_blocking", "1")
                        .add("include_blocked_by", "1")
                        .add("include_followed_by", "1")
                        .add("include_want_retweets", "1")
                        .add("include_mute_edge", "1")
                        .add("include_can_dm", "1")
                        .add("include_can_media_tag", "1")
                        .add("include_ext_is_blue_verified", "1")
                        .add("include_ext_verified_type", "1")
                        .add("include_ext_profile_image_shape", "1")
                        .add("skip_status", "1")
                        .add("user_id", userId)
                        .build()
        );
    }
}
