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
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.TypeAhead;
import dev.seeight.twitterscraper.util.JsonHelper;
import okhttp3.HttpUrl;

import java.net.URISyntaxException;
import java.util.List;

public class ConfigTypeAhead implements IConfigJsonTree<TypeAhead> {
    public int include_ext_is_blue_verified = 1;
    public int include_ext_verified_type = 1;
    public int include_ext_profile_image_shape = 1;
    public final String query;
    public String src = "search_box";
    public String result_type = "events,users,topics,lists";

    public ConfigTypeAhead(String query) {
        this.query = query;
    }

    @Override
    public TypeAhead fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
        return TypeAhead.fromJson(new JsonHelper(element), element.getAsJsonObject());
    }

    @Override
    public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
        return HttpUrl.get("https://x.com/i/api/1.1/search/typeahead.json").newBuilder()
                .addQueryParameter("include_ext_is_blue_verified", String.valueOf(this.include_ext_is_blue_verified))
                .addQueryParameter("include_ext_verified_type", String.valueOf(this.include_ext_verified_type))
                .addQueryParameter("include_ext_profile_image_shape", String.valueOf(this.include_ext_profile_image_shape))
                .addQueryParameter("q", String.valueOf(this.query))
                .addQueryParameter("src", String.valueOf(this.src))
                .addQueryParameter("result_type", String.valueOf(this.result_type))
                .build();
    }
}
