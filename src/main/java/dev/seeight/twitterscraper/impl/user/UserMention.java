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

package dev.seeight.twitterscraper.impl.user;

import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.impl.Range;
import dev.seeight.twitterscraper.impl.TextEntity;
import dev.seeight.twitterscraper.util.JsonHelper;

public class UserMention extends TextEntity {
	public String id;
	public String name;
	public String screenName;

	@Override
	public Type getType() {
		return Type.USER_MENTION;
	}

	public static UserMention fromJson(JsonElement element, JsonHelper h) {
		h.set(element);

		UserMention mention = new UserMention();

		mention.id = h.string("id_str");
		mention.name = h.string("name");
		mention.screenName = h.string("screen_name");
		mention.range = Range.fromArray(h.array("indices"));

		return mention;
	}
}
