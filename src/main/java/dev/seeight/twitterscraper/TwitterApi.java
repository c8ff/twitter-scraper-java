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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import dev.seeight.twitterscraper.features.FeatureFetcher;
import dev.seeight.twitterscraper.graphql.GraphQL;
import dev.seeight.twitterscraper.impl.search.SearchAdaptiveResult;
import dev.seeight.util.MiscUtil;
import okhttp3.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
	public FeatureFetcher.TwitterPage page;
	protected Map<String, GraphQL.Entry> graphQL;
	protected Map<String, GraphQLHotfix> graphQLHotFix;

	protected TwitterApi(Gson gson) {
		this.gson = gson;

		try {
			this.graphQLHotFix = new HashMap<>();
			var ql = GraphQL.fromURL(gson).entries;
			Map<String, GraphQL.Entry> g = new HashMap<>();
			for (var a : ql) {
				g.put(a.exports.operationName, a);
			}
			this.graphQL = g;
		} catch (Exception e) {
			MiscUtil.sneakyThrow(e);
		}
	}

	/**
	 * Adds the authorization parameters to the provided request.
	 *
	 * @param request The request to add the parameters to.
	 */
	private void addCookies(Request.Builder request) {
		this.assertAuth();
		request.addHeader("authorization", this.bearerAuthorization);
		request.addHeader("cookie", this.cookie);
		request.addHeader("User-Agent", this.userAgent);
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
	 * Executes the provided configuration with a {@link okhttp3.OkHttpClient} instance.
	 *
	 * @param <T>    The result of the configuration execution.
	 * @param config The config to execute.
	 * @param client A {@link okhttp3.OkHttpClient} to send the config's request.
	 * @return The resulting {@link T}.
	 */
	public <T> T scrap(IConfig<T> config, OkHttpClient client) throws IOException, URISyntaxException {
		this.assertAuth();
		var url = config.getUrl(gson, this);
		var request = config.createRequest(this.gson, url, this);
		this.addCookies(request);
		if (eventHandler != null) eventHandler.onBeforeResolvingRequest(url, request);
        return config.resolve(client, request.build(), this.gson);
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

	public static GsonBuilder defaultBuilder() {
		return new GsonBuilder()
			.disableHtmlEscaping()
			.registerTypeAdapter(SearchAdaptiveResult.Timeline.class, new SearchAdaptiveResult.TimeLineCursorDeserializer());
	}

	public static Request.Builder jsonPostReq(HttpUrl url, String body) throws MalformedURLException {
		return new Request.Builder().url(url).post(RequestBody.create(body, MediaType.parse("application/json")));
	}

	public static long convertTwitterDateToEpochUTC(String date) {
		return LocalDateTime.parse(date, formatter).toEpochSecond(ZoneOffset.UTC);
	}

	public void addHotFix(String operationName, String featureName, @Nullable Object featureValue) {
		this.graphQLHotFix.computeIfAbsent(operationName, s -> new GraphQLHotfix()).addFix(featureName, featureValue);
	}

	public GraphQLBuilder getGraphQLOperation(String operationName) {
		var op = graphQL.get(operationName);
		if (op == null) throw new NullPointerException("GraphQL operation \"" + operationName + "\" mapping not found.");
		return new GraphQLBuilder(op);
	}

	public static class GraphQLHotfix {
		public List<Fix> fixes = new ArrayList<>();
		public void addFix(String feature, Object value) {
			fixes.add(new Fix(feature, value));
		}
		public static class Fix {
			public String feature;
			public Object value;

			public Fix(String feature, Object value) {
				this.feature = feature;
				this.value = value;
			}
		}
	}

	public class GraphQLBuilder {
		private final GraphQL.Entry e;
		public GraphQLBuilder(GraphQL.Entry e) {
			this.e = e;
		}

		public String getUrlStr() {
			if (page == null) throw new NullPointerException("page");
			return "https://x.com/i/api/graphql/" + e.exports.queryId + "/" + e.exports.operationName;
		}

        public HttpUrl getBaseUrl() {
            return HttpUrl.get(getUrlStr());
        }

		public HttpUrl getUrl(String variablesJson) {
            var b = getBaseUrl().newBuilder();
            b.addQueryParameter("variables", variablesJson);
			if (page == null) return b.build();
            b.addQueryParameter("features", buildFeatures());
			return b.build();
		}

		public String getId() {
			return e.exports.queryId;
		}

		public String buildFeatures() {
			StringBuilder z = new StringBuilder("{");
			var first = true;
			for (String s : e.exports.metadata.featureSwitches) {
				if (first) first = false; else z.append(",");
				z.append('"').append(s).append("\": ");
				var v = page.getFeatures().get(s);
				if (v instanceof JsonPrimitive p && p.isString()) {
					z.append('\"').append(v).append('\"');
				} else {
					// This is a fallback.
					if (v == null) {
						z.append("false");
					} else z.append(v);
				}
			}
			var hotfix = graphQLHotFix.get(e.exports.operationName);
			if (hotfix != null) {
				for (GraphQLHotfix.Fix fix : hotfix.fixes) {
					if (first) first = false; else z.append(",");

					var s = fix.feature;
					z.append('"').append(s).append("\": ");
					var v = fix.value != null ? fix.value : false;
					if (v instanceof JsonPrimitive p && p.isString()) {
						z.append('\"').append(v).append('\"');
					} else {
						z.append(v);
					}
				}
			}
			z.append("}");
			return z.toString();
		}
	}

	public interface EventHandler {
		/**
		 * Called before a {@link IConfig} processes and resolves a request.
		 *
		 * @param url The URL of request.
		 * @param request The request.
		 */
		void onBeforeResolvingRequest(HttpUrl url, Request.Builder request);
	}
}
