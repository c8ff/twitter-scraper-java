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
import dev.seeight.twitterscraper.TwitterList;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.util.JsonHelper;
import okhttp3.HttpUrl;
import okhttp3.Request;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigPinTimeline implements IConfigJsonTree<ConfigPinTimeline.PinTimelineResult> {
    public final String id;
    public final String pinnedTimelineType;

    public ConfigPinTimeline(String id, String pinnedTimelineType) {
        this.id = id;
        this.pinnedTimelineType = pinnedTimelineType;
    }

    @Override
    public PinTimelineResult fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
        return PinTimelineResult.fromJson(gson, new JsonHelper(element), element.getAsJsonObject());
    }

    @Override
    public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
        return api.getGraphQLOperation("PinTimeline").getBaseUrl();
    }

    @Override
    public Request.Builder createRequest(Gson gson, HttpUrl url, TwitterApi api) throws URISyntaxException, MalformedURLException {
        var op = api.getGraphQLOperation("PinTimeline");
        String variables = gson.toJson(new Variables(new Variables.PinnedTimelineItem(this.id, this.pinnedTimelineType)));
        String features = op.buildFeatures();
        String queryId = op.getId();
        return TwitterApi.jsonPostReq(url, "{\"variables\":" + variables + ",\"features\":" + features + ",\"queryId\":\"" + queryId + "\"}");
    }

    protected static class Variables {
        public PinnedTimelineItem pinnedTimelineItem;

        public Variables(PinnedTimelineItem pinnedTimelineItem) {
            this.pinnedTimelineItem = pinnedTimelineItem;
        }

        public static class PinnedTimelineItem {
            public String id;
            public String pinned_timeline_type;

            public PinnedTimelineItem(String id, String pinned_timeline_type) {
                this.id = id;
                this.pinned_timeline_type = pinned_timeline_type;
            }
        }
    }

    public static class PinTimelineResult {
        public TwitterList list;

        public static PinTimelineResult fromJson(Gson gson, JsonHelper h, JsonObject o) {
            PinTimelineResult tl = new PinTimelineResult();
            tl.list = TwitterList.fromJson(gson, h.set(o).next("data").next("pin_timeline").next("updated_pinned_timeline").next("list").object(), h);
            return tl;
        }

        public static PinTimelineResult fromJsonUnpin(Gson gson, JsonHelper h, JsonObject o) {
            PinTimelineResult tl = new PinTimelineResult();
            tl.list = TwitterList.fromJson(gson, h.set(o).next("data").next("unpin_timeline").next("updated_pinned_timeline").next("list").object(), h);
            return tl;
        }
    }
}
