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

package dev.seeight.twitterscraper.impl.item;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.impl.Entry;
import dev.seeight.twitterscraper.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class TimelineModule extends Entry {
	public List<Entry> items;
	public String displayType;
	public Label header;
	public Label footer;

	public static class Label {
		public String displayType;
		public String text;
		public boolean sticky;
		public LandingUrl landingUrl;

		public Label() {
		}

		public Label(String displayType, String text, boolean sticky, LandingUrl landingUrl) {
			this.displayType = displayType;
			this.text = text;
			this.sticky = sticky;
			this.landingUrl = landingUrl;
		}

		public static Label fromJson(JsonHelper h, JsonObject z) {
			h.set(z);
			return new Label(
				h.string("displayType"),
				h.string("text"),
				h.bool("sticky", false),
				h.has("landingUrl") ? LandingUrl.fromJson(h, h.object("landingUrl")) : null
			);
		}

		public static class LandingUrl {
			public String url;
			public String urlType;

			public LandingUrl() {
			}

			public LandingUrl(String url, String urlType) {
				this.url = url;
				this.urlType = urlType;
			}

			public static LandingUrl fromJson(JsonHelper h, JsonObject z) {
				h.set(z);
				return new LandingUrl(h.string("url"), h.string("urlType"));
			}
		}
	}

	public static Entry fromJson(Gson gson, JsonHelper h, JsonObject object) {
		JsonArray items = h.set(object).next("items").array();

		List<Entry> entries = new ArrayList<>();
		for (JsonElement item : items) {
			if (!(item instanceof JsonObject io)) continue;

			h.set(io);
			String entryId = h.string("entryId");
			JsonObject item0 = h.object("item");
			var e = parseTimelineItem(entryId, null, gson, h, item0);
			if (e != null) {
				entries.add(e);
			}
		}

		TimelineModule t = new TimelineModule();
		t.items = entries;
		t.displayType = h.set(object).string("displayType", null);
		t.header = h.has("header") ? Label.fromJson(h, h.object("header")) : null;
		t.footer = h.set(object).has("footer") ? Label.fromJson(h, h.object("footer")) : null;
		return t;
	}
}
