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

package dev.seeight.twitterscraper.config.timeline;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.api2.GlobalObjects;
import dev.seeight.twitterscraper.impl.api2.LegacyTimeline;
import dev.seeight.twitterscraper.util.JsonHelper;
import okhttp3.HttpUrl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigApi2View implements IConfigJsonTree<ConfigApi2View.Api2View> {
    public final String url;

    public ConfigApi2View(String url) {
        this.url = url;
    }

    @Override
    public Api2View fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
        return Api2View.fromJson(new JsonHelper(element), element);
    }

    @Override
    public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
        return HttpUrl.get(this.url);
    }

    public static class Api2View {
        public GlobalObjects objects;
        public LegacyTimeline timeline;

        public Api2View() {
        }

        public Api2View(GlobalObjects objects, LegacyTimeline timeline) {
            this.objects = objects;
            this.timeline = timeline;
        }

        public static Api2View fromJson(JsonHelper h, JsonElement z) {
            h.set(z);
            return new Api2View(GlobalObjects.fromJson(h, h.object("globalObjects")), LegacyTimeline.fromJson(h, h.set(z).object("timeline")));
        }
    }
}
