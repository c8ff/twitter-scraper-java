/*
 * twitter-scraper-java
 * Copyright (C) 2024 c8ff
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.seeight.twitterscraper.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.seeight.twitterscraper.impl.search.SearchAdaptiveResult;

public class JsonUtil {
	private static Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().registerTypeAdapter(SearchAdaptiveResult.Timeline.class, new SearchAdaptiveResult.TimeLineCursorDeserializer()).create();

	public static void setGson(Gson gson) {
		JsonUtil.gson = gson;
	}

	public static Gson getGson() {
		return gson;
	}

	public static String toJson(Object obj) {
		return gson.toJson(obj);
	}
}
