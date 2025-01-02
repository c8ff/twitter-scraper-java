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

package dev.seeight.twitterscraper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Serves the same purpose of {@link IConfig}, but simplifies the parsing of an expected JSON result.
 *
 * @author C8FF
 */
public interface IConfigJsonTree<T> extends IConfig<T> {
	@Override
	default T resolve(HttpClient client, HttpUriRequestBase request, Gson gson) throws IOException, TwitterException {
		String responseStr = TwitterApi.executeString(client, request);
		JsonElement json = assertErrors(JsonParser.parseString(responseStr));
		return this.fromJson(json, gson);
	}

	/**
	 * Builds a {@link T} instance from a JSON tree.
	 *
	 * @param element The root of the JSON tree.
	 * @param gson    The gson instance.
	 * @return A {@link T} instance.
	 */
	T fromJson(JsonElement element, Gson gson);

	/**
	 * Checks for errors inside the JSON root.
	 *
	 * @param elm The JSON root to be checked.
	 * @return The parameter {@code elm}.
	 * @throws TwitterException When an error is present on the element.
	 */
	static JsonElement assertErrors(@NotNull JsonElement elm) throws TwitterException {
		if (!(elm instanceof JsonObject object)) {
			return elm;
		}

		JsonElement errors = object.get("errors");
		if (errors == null) {
			return elm;
		}

		if (object.get("data") != null) {
			return elm;
		}

		JsonObject err = errors.getAsJsonArray().get(0).getAsJsonObject();
		JsonHelper helper = new JsonHelper(err);
		throw new TwitterException(helper.next("message").string(), helper.set(err).next("code").integer());
	}
}
