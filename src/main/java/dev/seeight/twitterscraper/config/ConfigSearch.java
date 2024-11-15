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

package dev.seeight.twitterscraper.config;

import com.google.gson.Gson;
import dev.seeight.twitterscraper.IConfig;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.TwitterException;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.search.SearchAdaptiveResult;
import dev.seeight.twitterscraper.impl.search.SearchResult;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.Range;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ConfigSearch implements IConfig<SearchResult> {
	public final String query;
	public final int count;
	public final String querySource;
	public final String requestContext;
	@Range(from = 0, to = 1)
	public final int pc;
	@Range(from = 0, to = 1)
	public final int spellingCorrections;
	public final boolean includeExtEditControl;

	public ConfigSearch(String query, int count, String querySource, String requestContext, @Range(from = 0, to = 1) int pc, @Range(from = 0, to = 1) int spellingCorrections, boolean includeExtEditControl) {
		this.query = query;
		this.count = count;
		this.querySource = querySource;
		this.requestContext = requestContext;
		this.pc = pc;
		this.spellingCorrections = spellingCorrections;
		this.includeExtEditControl = includeExtEditControl;
	}

	@Override
	public String getBaseURL(GraphQLMap graphQL) {
		return "https://twitter.com/i/api/2/search/adaptive.json";
	}

	@Override
	public URI buildURI(Gson gson, URIBuilder builder, GraphQLMap graphQL) throws URISyntaxException {
		return builder
			.addParameter("q", this.query)
			.addParameter("count", String.valueOf(this.count))
			.addParameter("query_source", this.querySource)
			.addParameter("requestContext", this.requestContext)
			.addParameter("pc", String.valueOf(this.pc))
			.addParameter("spelling_corrections", String.valueOf(this.spellingCorrections))
			.addParameter("include_ext_edit_control", String.valueOf(this.includeExtEditControl))
			.build();
	}

	@Override
	public SearchResult resolve(HttpClient client, HttpUriRequestBase request, Gson gson) throws IOException, TwitterException {
		String responseStr = TwitterApi.executeString(client, request);
		return SearchResult.fromSearchAdaptiveResult(gson, gson.fromJson(responseStr, SearchAdaptiveResult.class));
	}

	public static Builder builder(String query) {
		return Builder.builder(query);
	}


	public static final class Builder {
		private String query;
		private int count;
		private String querySource;
		private String requestContext;
		private @Range(from = 0, to = 1) int pc;
		private @Range(from = 0, to = 1) int spellingCorrections;
		private boolean includeExtEditControl;

		private Builder(String query) {
			this.query = query;
			this.count = 20;
			this.querySource = "typed_query";
			this.requestContext = "launch";
			this.pc = 1;
			this.spellingCorrections = 1;
			this.includeExtEditControl = true;
		}

		public static Builder builder(String query) {
			return new Builder(query);
		}

		public Builder query(String query) {
			this.query = query;
			return this;
		}

		public Builder count(int count) {
			this.count = count;
			return this;
		}

		public Builder querySource(String querySource) {
			this.querySource = querySource;
			return this;
		}

		public Builder requestContext(String requestContext) {
			this.requestContext = requestContext;
			return this;
		}

		public Builder pc(int pc) {
			this.pc = pc;
			return this;
		}

		public Builder spellingCorrections(int spellingCorrections) {
			this.spellingCorrections = spellingCorrections;
			return this;
		}

		public Builder includeExtEditControl(boolean includeExtEditControl) {
			this.includeExtEditControl = includeExtEditControl;
			return this;
		}

		public ConfigSearch build() {
			return new ConfigSearch(query, count, querySource, requestContext, pc, spellingCorrections, includeExtEditControl);
		}
	}
}
