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

package dev.seeight.twitterscraper.impl;

import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

public class Url {
	public String displayUrl;
	public @Nullable String expandedUrl;
	public String url;
	public Range range;

	public static Url fromJson(JsonElement element, JsonHelper h) {
		h.set(element);

		Url url = new Url();

		url.displayUrl = h.string("display_url");
		url.expandedUrl = h.stringOrDefault("expanded_url", null);
		url.url = h.string("url");
		url.range = Range.fromArray(h.array("indices"));

		return url;
	}
}