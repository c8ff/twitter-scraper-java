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
import com.google.gson.GsonBuilder;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.graphql.JsonGraphQLMap;
import dev.seeight.twitterscraper.impl.search.SearchAdaptiveResult;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * The base API to execute configurations (instances of {@link IConfig}).
 *
 * @author C8FF
 */
public class TwitterApi {
	/**
	 * The formatter to parse Twitter's date format.
	 */
	public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss Z yyyy", Locale.US);

	public String bearerAuthorization;
	public String cookie;
	public String userAgent;
	public String csrfToken;
	public final Gson gson;
	String clientUUID;
	public EventHandler eventHandler;

	protected GraphQLMap graphQL;

	protected TwitterApi(Gson gson) {
		this.gson = gson;
		this.createGraphQL(TwitterApi.class.getResourceAsStream("/graphql/2024-06-20.json"));
	}

	public void createGraphQL(@NotNull InputStream graphqlJson) {
		this.setGraphQL(new JsonGraphQLMap(gson, graphqlJson));
	}

	public void setGraphQL(@NotNull GraphQLMap graphQL) {
		this.graphQL = graphQL;
	}

	public GraphQLMap graphQL() {
		return graphQL;
	}

	/**
	 * Adds the authorization parameters to the provided request.
	 *
	 * @param request The request to add the parameters to.
	 */
	private void addCookies(HttpUriRequestBase request) {
		this.assertAuth();
		request.addHeader(HttpHeaders.AUTHORIZATION, this.bearerAuthorization);
		request.addHeader(HttpHeaders.COOKIE, this.cookie);
		request.addHeader(HttpHeaders.USER_AGENT, this.userAgent);
		request.addHeader("x-csrf-token", this.csrfToken);
		if (this.clientUUID != null) {
			request.addHeader("x-Client-UUID", this.clientUUID);
		}
		request.addHeader("x-twitter-active-user", "yes");
		request.addHeader("x-twitter-auth-type", "OAuth2Session");
		request.addHeader("x-twitter-client-language", "en");
		request.addHeader("TE", "trailers");
		request.addHeader("Sec-Fetch-Site", "same-origin");
		request.addHeader("Sec-Fetch-Dest", "empty");
		request.addHeader("Sec-Fetch-Mode", "cors");
	}

	/**
	 * Executes the provided configuration with a {@link HttpClient} instance.
	 *
	 * @param <T>    The result of the configuration execution.
	 * @param config The config to execute.
	 * @param client A {@link HttpClient} to send the config's request.
	 * @return The resulting {@link T}.
	 */
	public <T> T scrap(IConfig<T> config, HttpClient client) throws IOException, URISyntaxException {
		this.assertAuth();
		GraphQLMap ql = this.graphQL();
		URI uri = config.buildURI(gson, new URIBuilder(config.getBaseURL(ql)), ql);
		HttpUriRequestBase request = config.createRequest(this.gson, uri, ql);
		this.addCookies(request);
		if (eventHandler != null) eventHandler.onBeforeResolvingRequest(uri, request);
		return config.resolve(client, request, this.gson);
	}

	/**
	 * Checks if the authorization parameters are set or if they are valid.
	 *
	 * @throws RuntimeException If any of the parameters are not set, or they are invalid.
	 */
	private void assertAuth() throws RuntimeException {
		if (this.bearerAuthorization == null || this.bearerAuthorization.isEmpty() || !this.bearerAuthorization.startsWith("Bearer ")) {
			throw new RuntimeException("Bearer Authorization is invalid: " + this.bearerAuthorization);
		}

		if (this.cookie == null || this.cookie.isEmpty()) {
			throw new RuntimeException("Cookie is invalid: " + this.cookie);
		}

		if (this.userAgent == null || this.userAgent.isEmpty()) {
			throw new RuntimeException("userAgent is invalid: " + this.userAgent);
		}

		if (this.csrfToken == null || this.csrfToken.isEmpty()) {
			throw new RuntimeException("csrfToken is invalid: " + this.csrfToken);
		}
	}

	/**
	 * Creates a new instance of {@link TwitterApi} with the default {@link Gson} builder.
	 *
	 * @return A new {@link TwitterApi} instance.
	 */
	@Contract("-> new")
	public static TwitterApi newTwitterApi() {
		return new TwitterApi(defaultBuilder().create());
	}

	/**
	 * Creates a new instance of {@link TwitterApi} using the provided {@link Gson} instance.
	 *
	 * @param gson The gson instance to create a {@link TwitterApi} instance.
	 * @return A new {@link TwitterApi} instance.
	 */
	@Contract("_ -> new")
	public static TwitterApi newTwitterApi(Gson gson) {
		return new TwitterApi(gson);
	}

	public static String executeString(HttpClient client, HttpUriRequestBase request) throws IOException {
		// goofy ahh api
		return client.execute(request, response -> {
			if (response.getEntity() == null) {
				throw new NullPointerException();
			}

			return EntityUtils.toString(response.getEntity());
		});
	}

	public static GsonBuilder defaultBuilder() {
		return new GsonBuilder()
			.disableHtmlEscaping()
			.registerTypeAdapter(SearchAdaptiveResult.Timeline.class, new SearchAdaptiveResult.TimeLineCursorDeserializer());
	}

	public static HttpPost newJsonPostRequest(URI uri, String body) {
		HttpPost req = new HttpPost(uri);
		StringEntity entity = new StringEntity(body);
		req.setEntity(entity);
		req.addHeader("content-type", "application/json");
		return req;
	}

	public static long convertTwitterDateToEpochUTC(String date) {
		return LocalDateTime.parse(date, formatter).toEpochSecond(ZoneOffset.UTC);
	}

	public interface EventHandler {
		/**
		 * Called before a {@link IConfig} processes and resolves a request.
		 *
		 * @param uri The URI of request.
		 * @param request The request.
		 */
		void onBeforeResolvingRequest(URI uri, HttpUriRequestBase request);
	}
}
