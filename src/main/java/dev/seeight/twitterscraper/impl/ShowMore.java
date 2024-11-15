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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.util.GsonUtil;
import dev.seeight.twitterscraper.util.JsonHelper;

@Deprecated
public class ShowMore extends Entry {
	public String value;
	public String actionText;
	public String labelText;

	public static ShowMore fromJson(Gson gson, JsonObject entry, JsonHelper helper) {
		return fromJson0(gson, helper.set(entry).next("content").object("itemContent"), helper);
	}

	public static ShowMore fromJson0(Gson gson, JsonObject entry, JsonHelper helper) {
		ShowMore s = GsonUtil.createObject(gson, ShowMore.class);
		helper.set(entry);

		s.value = helper.string("value");
		helper.next("displayTreatment");
		s.actionText = helper.string("actionText");
		s.labelText = helper.string("labelText", null);

		return s;
	}
}
