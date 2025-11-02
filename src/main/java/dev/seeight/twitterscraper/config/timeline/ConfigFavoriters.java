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
import dev.seeight.twitterscraper.Timeline;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.inst.Instruction;
import dev.seeight.twitterscraper.util.JsonHelper;
import okhttp3.HttpUrl;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

public class ConfigFavoriters implements IConfigJsonTree<ConfigFavoriters.Favoriters> {
    public final String tweetId;
    public int count = 20;
    public String cursor;
    public boolean enableRanking = false;
    public boolean includePromotedContent = true;

    public ConfigFavoriters(String tweetId) {
        this.tweetId = tweetId;
    }

    public ConfigFavoriters(String tweetId, String cursor) {
        this.tweetId = tweetId;
        this.cursor = cursor;
    }

    @Override
    public Favoriters fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
        return Favoriters.fromJson(gson, new JsonHelper(element), element);
    }

    @Override
    public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
        var op = api.getGraphQLOperation("Favoriters");
        return op.getUrl(gson.toJson(this));
    }

    public static class Favoriters extends Timeline {
        public Favoriters() {
        }

        public Favoriters(List<Instruction> instructions) {
            this.instructions = instructions;
        }

        public static Favoriters fromJson(Gson gson, JsonHelper h, JsonElement z) {
            h.set(z).next("data").next("favoriters_timeline");
            if (!h.tryNext("timeline")) return new Favoriters(Collections.emptyList());
            return new Favoriters(Instruction.fromInstructionsJson(gson, h, h.array("instructions")));
        }
    }
}
