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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.Timeline;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.inst.Instruction;
import dev.seeight.twitterscraper.util.JsonHelper;
import okhttp3.HttpUrl;

import java.net.URISyntaxException;
import java.util.List;

public class ConfigBookmarkSearchTimeline implements IConfigJsonTree<ConfigBookmarkSearchTimeline.BookmarksSearch> {
    public String rawQuery;
	public int count = 20;
	public final String cursor;
	public boolean includePromotedContent = false;

	public ConfigBookmarkSearchTimeline(String rawQuery, String cursor) {
        this.rawQuery = rawQuery;
        this.cursor = cursor;
	}

	@Override
	public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
		var op = api.getGraphQLOperation("BookmarkSearchTimeline");
		return op.getUrl(gson.toJson(this));
	}

	@Override
	public BookmarksSearch fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		return BookmarksSearch.fromJson(gson, element);
	}

    public static class BookmarksSearch extends Timeline {
        public static BookmarksSearch fromJson(Gson gson, JsonElement element) {
            JsonHelper h = new JsonHelper(element);
            JsonArray jInstructions = h.next("data").next("search_by_raw_query").next("bookmarks_search_timeline").next("timeline").next("instructions").array();

            var l = new BookmarksSearch();
            l.instructions = Instruction.fromInstructionsJson(gson, h, jInstructions);
            return l;
        }
    }
}
