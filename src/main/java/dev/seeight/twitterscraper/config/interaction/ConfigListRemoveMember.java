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
import com.google.gson.JsonParser;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.TwitterList;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.util.JsonHelper;
import okhttp3.HttpUrl;
import okhttp3.Request;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigListRemoveMember implements IConfigJsonTree<TwitterList> {
    private final String listId;
    private final String userId;

    public ConfigListRemoveMember(String listId, String userId) {
        this.listId = listId;
        this.userId = userId;
    }

    @Override
    public TwitterList fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
        JsonHelper h = new JsonHelper(element);
        h.next("data").next("list");
        return TwitterList.fromJson(gson, h.object(), h);
    }

    @Override
    public Request.Builder createRequest(Gson gson, HttpUrl url, TwitterApi api) throws URISyntaxException, MalformedURLException {
        var op = api.getGraphQLOperation("ListRemoveMember");

        JsonObject variables = new JsonObject();
        variables.addProperty("listId", this.listId);
        variables.addProperty("userId", this.userId);

        JsonObject o = new JsonObject();
        o.add("features", JsonParser.parseString(op.buildFeatures()));
        o.addProperty("queryId", op.getId());
        o.add("variables", variables);

        return TwitterApi.jsonPostReq(url, o.toString());
    }

    @Override
    public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
        return api.getGraphQLOperation("ListRemoveMember").getBaseUrl();
    }
}
