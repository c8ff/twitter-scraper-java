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

public class ConfigListOwnerships implements IConfigJsonTree<ConfigListOwnerships.ListOwnerships> {
    /**
     * From User ID (in most cases, self ID)
     */
    public final String userId;
    /**
     * Target User ID
     */
    public final String isListMemberTargetUserId;
    public int count = 20;

    public ConfigListOwnerships(String userId, String isListMemberTargetUserId) {
        this.userId = userId;
        this.isListMemberTargetUserId = isListMemberTargetUserId;
    }

    @Override
    public ListOwnerships fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
        return ListOwnerships.fromJson(gson, element, new JsonHelper(element));
    }

    @Override
    public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
        var op = api.getGraphQLOperation("ListOwnerships");
        return op.getUrl(gson.toJson(this));
    }

    public static class ListOwnerships extends Timeline {
        public static ListOwnerships fromJson(Gson gson, JsonElement element, JsonHelper h) {
            h.set(element);
            var l = new ListOwnerships();
            if (h.tryNext("data") && h.tryNext("user") && h.tryNext("result") && h.tryNext("timeline") && h.tryNext("timeline")) {
                l.instructions = Instruction.fromInstructionsJson(gson, h, h.array("instructions"));
            } else {
                l.instructions = Collections.emptyList();
            }
            return l;
        }
    }
}
