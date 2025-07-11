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

package dev.seeight.twitterscraper.graphql;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class JsonGraphQLMap implements GraphQLMap {
	@NotNull
	private final Root root;

	public JsonGraphQLMap(@NotNull Gson gson) throws IOException {
		this(gson, getDefaultUrl().openStream());
	}

	public JsonGraphQLMap(@NotNull Gson gson, @NotNull InputStream stream) {
		this.root = gson.fromJson(new InputStreamReader(stream), Root.class);
	}

	@Override
	public @Nullable Entry getNullable(String queryId) {
		return this.root.graphql.get(queryId);
	}

	public static URL getDefaultUrl() throws MalformedURLException {
		return new URL("https://raw.githubusercontent.com/fa0311/TwitterInternalAPIDocument/develop/docs/json/API.json");
	}
}
