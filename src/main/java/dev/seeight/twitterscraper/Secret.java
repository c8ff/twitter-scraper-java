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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Used to define secret information to a {@link TwitterApi} instance.
 */
public class Secret {
	public String bearerAuthorization;
	public String cookie;
	public String userAgent;
	public String csrfToken;
	@Nullable
	public String clientUUID;

	/**
	 * Copies the secret specific values from a {@link TwitterApi} instance.
	 *
	 * @param api The api to copy the values from.
	 * @return A new secret from the api.
	 */
	@Contract(value = "_ -> new", pure = true)
	public static Secret fromTwitterApi(TwitterApi api) {
		if (api.bearerAuthorization == null) throw new IllegalArgumentException("bearer token is not defined.");
		if (api.cookie == null) throw new IllegalArgumentException("cookie is not defined.");
		if (api.userAgent == null) throw new IllegalArgumentException("userAgent is not defined.");
		if (api.csrfToken == null) throw new IllegalArgumentException("CSRF token is not defined.");

		Secret secret = new Secret();
		secret.bearerAuthorization = api.bearerAuthorization;
		secret.cookie = api.cookie;
		secret.userAgent = api.userAgent;
		secret.csrfToken = api.csrfToken;
		secret.clientUUID = api.clientUUID;
		return secret;
	}

	/**
	 * Calls {@link #defineFromFile(TwitterApi, File)} using the 'dumb_twitter_secrets' environment variable
	 * as the json file path.
	 */
	public static void defineFromFile(TwitterApi api) throws RuntimeException {
		String path = System.getenv("dumb_twitter_secrets");

		if (path == null) {
			throw new RuntimeException("Environment variable doesn't exist.");
		}

		defineFromFile(api, new File(path));
	}

	/**
	 * Reads {@code jsonFile} as a json file (in the scheme of {@link Secret}) and defines the corresponding
	 * result into the provided {@code api}.
	 *
	 * @throws RuntimeException Thrown when the secrets file could not be found.
	 */
	public static void defineFromFile(TwitterApi api, File jsonFile) throws RuntimeException {
		Gson gson = new Gson();
		try {
			Secret secret = gson.fromJson(new FileReader(jsonFile), Secret.class);

			api.bearerAuthorization = secret.bearerAuthorization;
			api.cookie = secret.cookie;
			api.csrfToken = secret.csrfToken;
			api.userAgent = secret.userAgent;
			api.clientUUID = secret.clientUUID;
		} catch (FileNotFoundException e) {
			throw new RuntimeException("The secrets file cannot be found '" + jsonFile.getAbsolutePath() + "'.");
		}
	}
}
