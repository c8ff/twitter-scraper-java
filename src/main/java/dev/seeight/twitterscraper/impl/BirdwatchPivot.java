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

import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.util.JsonHelper;

public class BirdwatchPivot {
	public String destinationUrl;
	public Information footer;
	public String restId;
	public Information subtitle;
	public String title;
	public String shortTitle;
	public String iconType;

	// TODO: Call to action missing lol

	public static class Information {
		public String text;

		// TODO: Add support for entities here
		public static Information fromJson(JsonObject root, JsonHelper h) {
			h.set(root);

			Information i = new Information();
			i.text = h.string("text");
			return i;
		}
	}

	public static BirdwatchPivot fromJson(JsonObject root, JsonHelper h) {
		h.set(root);

		BirdwatchPivot p = new BirdwatchPivot();
		p.title = h.string("title");
		p.shortTitle = h.string("shorttitle");
		p.iconType = h.string("iconType");
		p.destinationUrl = h.string("destinationUrl");
		p.footer = Information.fromJson(h.object("footer"), h);
		p.subtitle = Information.fromJson(h.set(root).object("subtitle"), h);
		h.set(root);
		p.restId = h.next("note").string("rest_id");

		return p;
	}
}
