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
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * Used to process a specific type of request.<p>
 * A request is built, sent, parsed and converted into a {@link T} instance using this class.
 *
 * @param <T> The result type of this configuration.
 * @author C8FF
 */
public interface IConfig<T> {
	HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException;

	/**
     * Creates the request that will be sent.
     *
     * @param gson The gson instance.
     * @param url  The previously built URI
     * @param api  The Twitter API instance.
     * @return The request.
     */
	default Request.Builder createRequest(Gson gson, HttpUrl url, TwitterApi api) throws URISyntaxException, MalformedURLException {
        return new Request.Builder().url(url);
	}

	/**
	 * Sends the request and parses the result into a {@link T} instance.
	 *
	 * @param client  The {@link OkHttpClient} instance to send the request with.
	 * @param request The request to be sent.
	 * @param gson    The gson instance.
	 * @return A {@link T} instance.
	 */
	T resolve(OkHttpClient client, Request request, Gson gson) throws IOException, TwitterException;
}
