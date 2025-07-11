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

package dev.seeight.twitterscraper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Serves the same purpose of {@link IConfig}, but simplifies the parsing of an expected JSON result.
 *
 * @author C8FF
 */
public interface IConfigJsonTree<T> extends IConfig<T> {
	@Override
	default T resolve(HttpClient client, HttpUriRequestBase request, Gson gson) throws IOException, TwitterException {
		String responseStr = TwitterApi.executeString(client, request);
		List<TwitterError> errors = new ArrayList<>();
		JsonElement json;
		try {
			json = assertErrors(JsonParser.parseString(responseStr), request, errors);
		} catch (Throwable e) {
			throw new RuntimeException("Cannot parse JSON. Original response: " + responseStr, e);
		}
		return this.fromJson(json, gson, errors);
	}

	/**
	 * Builds a {@link T} instance from a JSON tree.
	 *
	 * @param element The root of the JSON tree.
	 * @param gson    The gson instance.
	 * @param errors  A list containing errors from Twitter. Any fatal errors will be thrown before this point.
	 * @return A {@link T} instance.
	 */
	T fromJson(JsonElement element, Gson gson, List<TwitterError> errors);

	/**
	 * Checks for errors inside the JSON root.
	 *
	 * @param elm       The JSON root to be checked.
	 * @param request   The request that provided the JSON data.
	 * @param errorList A list of errors. If any errors are parsed, they will be added to this list.
	 * @return The parameter {@code elm}.
	 * @throws TwitterException When an error is present on the element.
	 */
	static JsonElement assertErrors(@NotNull JsonElement elm, @Nullable HttpUriRequestBase request, @Nullable List<TwitterError> errorList) throws TwitterException {
		if (!(elm instanceof JsonObject object)) {
			return elm;
		}

		JsonElement errors = object.get("errors");
		if (errors == null) {
			return elm;
		}

		if (errorList == null) errorList = new ArrayList<>(errors.getAsJsonArray().size());
		TwitterError.fromJsonArray(new JsonHelper(elm), errors.getAsJsonArray(), errorList);
		for (TwitterError err : errorList) {
			if (err.code == TwitterError.ALREADY_RETWEETED || err.code == TwitterError.ALREADY_FAVORITED || err.code == TwitterError.ALREADY_UNFAVORITED || err.code == TwitterError.TIMEOUT_UNSPECIFIED) continue;
			if (err.kind != null && err.kind.equalsIgnoreCase("NonFatal")) continue;
			throw new TwitterException(err.message, err.code, request);
		}

		return elm;
	}
}
