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
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Used to process a specific type of request.<p>
 * A request is built, sent, parsed and converted into a {@link T} instance using this class.
 *
 * @param <T> The result type of this configuration.
 * @author C8FF
 */
public interface IConfig<T> {
	/**
	 * Returns the base url. For example: {@code "https://foo.com/bar"}.
	 * The return value will be used to create the request.
	 *
	 * @return The base url.
	 */
	String getBaseURL(GraphQLMap graphQL);

	/**
	 * Builds the {@link URI} that the request will be requested to.
	 *
	 * @param gson    The gson instance.
	 * @param builder The {@link URIBuilder} created with the URL from {@link #getBaseURL(GraphQLMap)}
	 * @param graphQL A {@link GraphQLMap} instance. Used to apply the latest urls to fetch from the public API.
	 * @return A valid {@link URI} instance.
	 */
	default URI buildURI(Gson gson, URIBuilder builder, GraphQLMap graphQL) throws URISyntaxException {
		return builder.build();
	}

	/**
	 * Creates the request that will be sent.
	 *
	 * @param gson    The gson instance.
	 * @param uri     The result of the built uri using {@link #buildURI(Gson, URIBuilder, GraphQLMap)}
	 * @param graphQL A {@link GraphQLMap} instance. Should contain the latest urls for API fetching.
	 * @return The request.
	 */
	default HttpUriRequestBase createRequest(Gson gson, URI uri, GraphQLMap graphQL) throws URISyntaxException {
		return new HttpGet(uri);
	}

	/**
	 * Sends the request and parses the result into a {@link T} instance.
	 *
	 * @param client  The {@link HttpClient} instance to send the request with.
	 * @param request The request to be sent.
	 * @param gson    The gson instance.
	 * @return A {@link T} instance.
	 */
	T resolve(HttpClient client, HttpUriRequestBase request, Gson gson) throws IOException, TwitterException;
}
